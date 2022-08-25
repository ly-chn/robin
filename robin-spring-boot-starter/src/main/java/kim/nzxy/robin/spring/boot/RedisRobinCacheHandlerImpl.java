package kim.nzxy.robin.spring.boot;

import kim.nzxy.robin.constant.Constant;
import kim.nzxy.robin.enums.RobinRuleEnum;
import kim.nzxy.robin.handler.RobinCacheHandler;
import kim.nzxy.robin.util.RobinUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author xy
 * @since 2021/6/5
 */
@Component
@Slf4j
public class RedisRobinCacheHandlerImpl implements RobinCacheHandler {
    private final StringRedisTemplate redisTemplate;
    /**
     * key通配符
     */
    private static final String ASTERISK = "*";

    public RedisRobinCacheHandlerImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.clean();
    }


    @Override
    public void accessRecord(RobinRuleEnum type, String target, int expire) {
        redisTemplate.opsForList().rightPushAll(Constant.recordPrefix + type + target, String.valueOf(expire));
    }


    @Override
    public List<Integer> getAccessRecord(RobinRuleEnum type, String target, int length) {
        val stringList = redisTemplate.opsForList().range(Constant.recordPrefix + type + target, -length, -1);
        if (stringList != null) {
            return stringList.stream().map(Integer::valueOf).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public void lock(RobinRuleEnum type, String target, Duration unlock) {
        redisTemplate.opsForZSet().add(Constant.lockedPrefix + type, target, unlock.getSeconds() + RobinUtil.now());
    }

    @Override
    public boolean lock(RobinRuleEnum type, String target) {
        val score = redisTemplate.opsForZSet().score(Constant.lockedPrefix + type, target);
        if (score == null) {
            return false;
        }
        return score > RobinUtil.now();
    }

    @Override
    public void unlock(RobinRuleEnum type, String target) {
        if (type != null) {
            redisTemplate.opsForZSet().remove(Constant.lockedPrefix + type, target);
            return;
        }

        for (RobinRuleEnum it : RobinRuleEnum.values()) {
            redisTemplate.opsForZSet().remove(Constant.lockedPrefix + it, target);
        }
    }

    public void clean() {
        new FutureTask<>(() -> {
            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
            final long anHour = 60 * 60;
            executor.scheduleWithFixedDelay(
                    () -> {
                        log.debug("cleaning access record and lock");
                        cleanAccessRecord();
                        log.debug("access record is cleaned");
                        cleanLock();
                        log.debug("lock is cleaned");
                    },
                    0,
                    anHour,
                    TimeUnit.SECONDS);
            return null;
        }).run();
    }

    private void cleanAccessRecord() {
        val keys = redisTemplate.keys(Constant.recordPrefix + ASTERISK);
        if (keys == null) {
            return;
        }
        val now = RobinUtil.now();
        // todo: 肯定有更好的方式吧, 这也太麻烦了
        for (String key : keys) {
            val range = redisTemplate.opsForList().range(key, 0, -1);
            // 理论上来说不可能为 null 避免 IDE 检查
            if (range == null) {
                continue;
            }
            val count = range.stream().filter(item -> Integer.parseInt(item) < now).count();
            for (long i = 0; i < count; i++) {
                redisTemplate.opsForList().leftPop(key);
            }
        }
    }

    private void cleanLock() {
        redisTemplate.opsForZSet().removeRange(Constant.lockedPrefix, 0, RobinUtil.now());
        log.debug("after clean lock");
    }
}
