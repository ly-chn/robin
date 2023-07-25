package kim.nzxy.robin.data.redis;

import kim.nzxy.robin.metadata.RobinMetadata;
import kim.nzxy.robin.handler.RobinCacheHandler;
import kim.nzxy.robin.util.RobinUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;

/**
 * @author lyun-chn
 * @since 2021/6/5
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class RedisRobinCacheHandlerImpl implements RobinCacheHandler {
    private final StringRedisTemplate redisTemplate;
    private static final DefaultRedisScript<Boolean> SUSTAIN_VISIT_LUA;

    static {
        SUSTAIN_VISIT_LUA = new DefaultRedisScript<>();
        SUSTAIN_VISIT_LUA.setScriptSource(new ResourceScriptSource(new ClassPathResource("robin-lua/sustain-visit.lua")));
        SUSTAIN_VISIT_LUA.setResultType(Boolean.class);
    }



    @Override
    public boolean sustainVisit(RobinMetadata metadata, Duration timeFrameSize, Integer maxTimes) {
        String key = Constant.SUSTAIN_VISIT_PREFIX + metadata.getTopic();
        String value = metadata.getMetadata();
        int currentTimeFrame = RobinUtil.currentTimeFrame(timeFrameSize);
        return Boolean.TRUE.equals(redisTemplate.execute(SUSTAIN_VISIT_LUA,
                Collections.singletonList(key),
                value,
                String.valueOf(currentTimeFrame),
                maxTimes.toString(),
                String.valueOf(Constant.SUSTAIN_VISIT_PRECISION),
                Boolean.toString(log.isDebugEnabled())
        ));

    }

    private void cleanSustainVisit() {
        /*ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        for (Map.Entry<String, Duration> entry : SUSTAIN_TOPIC_MAP.entrySet()) {
            zSetOperations.removeRangeByScore(entry.getKey(), 0, RobinUtil.currentTimeFrame(entry.getValue()) - 1);
        }*/
    }

    @Override
    public void lock(RobinMetadata metadata, Duration lock) {
        redisTemplate.opsForZSet().add(Constant.LOCKED_PREFIX + metadata.getTopic(), metadata.getMetadata(), lock.getSeconds() + RobinUtil.now());
    }

    @Override
    public boolean locked(RobinMetadata metadata) {
        Double score = redisTemplate.opsForZSet()
                .score(Constant.LOCKED_PREFIX + metadata.getTopic(), metadata.getMetadata());
        return score != null && score > RobinUtil.now();
    }

    @Override
    public void unlock(RobinMetadata metadata) {
        if (metadata != null) {
            redisTemplate.opsForZSet().remove(Constant.LOCKED_PREFIX + metadata.getTopic(), metadata.getMetadata());
            return;
        }
        // todo: null for all
    }

    @Override
    public void freshenUp() {
        cleanSustainVisit();
        log.debug("sustain visit record is cleaned");
        cleanLock();
        log.debug("locked record is cleaned");
    }

    private void cleanLock() {
        Set<String> keys = redisTemplate.keys(Constant.LOCKED_PREFIX + "*");
        if (keys != null) {
            for (String topic : keys) {
                redisTemplate.opsForZSet().removeRange(topic, 0, RobinUtil.now());
            }
        }
    }

    interface Constant {
        /**
         * 缓存前缀
         */
        String CACHE_PREFIX = "robin:";
        /**
         * 持续访问缓存前缀
         */
        String SUSTAIN_VISIT_PREFIX = CACHE_PREFIX + "sustain:";
        /**
         * 持续访问记录最大记录数量
         */
        int SUSTAIN_VISIT_PRECISION = 100000;
        /**
         * 锁定元数据
         */
        String LOCKED_PREFIX = CACHE_PREFIX + "lock:";
    }
}
