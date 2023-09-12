package kim.nzxy.robin.data.redis.lock;

import kim.nzxy.robin.data.redis.autoconfigure.RobinRedisManage;
import kim.nzxy.robin.data.redis.util.RobinLuaUtil;
import kim.nzxy.robin.factory.RobinEffortFactory;
import kim.nzxy.robin.handler.RobinLockHandler;
import kim.nzxy.robin.metadata.RobinMetadata;
import kim.nzxy.robin.util.RobinUtil;
import lombok.CustomLog;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * robin 锁定逻辑实现
 *
 * @author ly-chn
 */
@CustomLog
public class RedisRobinLockHandler implements RobinLockHandler {

    private StringRedisTemplate getStringRedisTemplate() {
        return RobinRedisManage.getStringRedisTemplate();
    }

    private static final DefaultRedisScript<Boolean> LOCK_CLEAN = RobinLuaUtil.loadBool("lock-clean");


    @Override
    public void lock(RobinMetadata metadata, Duration lock) {
        getStringRedisTemplate().opsForZSet().add(Constant.LOCKED_PREFIX + metadata.getTopic(), metadata.getMetadata(),
                lock.getSeconds() + RobinUtil.now());
    }

    @Override
    public boolean locked(RobinMetadata metadata) {
        Double score = getStringRedisTemplate().opsForZSet()
                .score(Constant.LOCKED_PREFIX + metadata.getTopic(), metadata.getMetadata());
        return score != null && score > RobinUtil.now();
    }

    @Override
    public void unlock(RobinMetadata metadata) {
        StringRedisTemplate redisTemplate = getStringRedisTemplate();
        if (Objects.isNull(metadata) || !metadata.hasTopic()) {
            List<String> keyList = RobinEffortFactory.getAllTopic().stream()
                    .map(it -> Constant.LOCKED_PREFIX + it).collect(Collectors.toList());
            redisTemplate.delete(keyList);
            return;
        }
        if (!metadata.hasMetadata()) {
            redisTemplate.delete(Constant.LOCKED_PREFIX + metadata.getTopic());
            return;
        }
        redisTemplate.opsForZSet().remove(Constant.LOCKED_PREFIX + metadata.getTopic(), metadata.getMetadata());
    }

    @Override
    public void freshenUp() {
        Set<String> topics = RobinEffortFactory.getTopicByKey(null);
        List<String> lockedKeys = new ArrayList<>();
        topics.forEach(topic->lockedKeys.add(Constant.LOCKED_PREFIX + topic));
        getStringRedisTemplate().execute(LOCK_CLEAN, lockedKeys, String.valueOf(RobinUtil.now()));
    }


    interface Constant {
        /**
         * 缓存前缀
         */
        String CACHE_PREFIX = "robin:";
        /**
         * 锁定元数据
         */
        String LOCKED_PREFIX = CACHE_PREFIX + "lock:";
    }
}
