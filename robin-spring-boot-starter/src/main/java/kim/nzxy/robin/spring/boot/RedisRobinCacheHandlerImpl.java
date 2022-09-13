package kim.nzxy.robin.spring.boot;

import kim.nzxy.robin.config.RobinMetadata;
import kim.nzxy.robin.handler.RobinCacheHandler;
import kim.nzxy.robin.util.RobinTimeFrameUtil;
import kim.nzxy.robin.util.RobinUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author xy
 * @since 2021/6/5
 */
@Component
@Slf4j
public class RedisRobinCacheHandlerImpl implements RobinCacheHandler {
    /**
     * key通配符
     */
    private static final String ASTERISK = "*";
    /**
     * 持续访问记录key列表，用于定期清理数据
     */
    private static final Map<String, Duration> CONTINUOUS_VISIT_TOPIC_MAP = new HashMap<>();
    private final StringRedisTemplate redisTemplate;

    public RedisRobinCacheHandlerImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.clean();
    }


    @Override
    public int sustainVisit(RobinMetadata metadata, Duration timeFrameSize) {
        String key = Constant.SUSTAIN_VISIT_PREFIX + metadata.getTopic();
        String value = metadata.getMetadata();
        CONTINUOUS_VISIT_TOPIC_MAP.put(key, timeFrameSize);
        int currentTimeFrame = RobinTimeFrameUtil.currentTimeFrame(timeFrameSize);
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        Double latestVisit = zSetOperations.score(key, value);
        // 非连续访问
        if (Objects.isNull(latestVisit) || currentTimeFrame - latestVisit > 1) {
            zSetOperations.add(key, value, currentTimeFrame + Constant.SUSTAIN_VISIT_STEP);
            return 1;
        }
        // 当前连续访问次数
        double i = latestVisit % 1;
        if (currentTimeFrame - latestVisit > 0) {
            i += Constant.SUSTAIN_VISIT_STEP;
        }
        zSetOperations.add(key, value, currentTimeFrame + (i / Constant.SUSTAIN_VISIT_PRECISION));
        return (int) (i * Constant.SUSTAIN_VISIT_PRECISION);
    }

    private void cleanSustainVisit() {
        ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
        for (Map.Entry<String, Duration> entry : CONTINUOUS_VISIT_TOPIC_MAP.entrySet()) {
            zSetOperations.removeRangeByScore(entry.getKey(), 0, RobinTimeFrameUtil.currentTimeFrame(entry.getValue()) - 1);
        }
    }

    @Override
    public void lock(RobinMetadata metadata, Duration lock) {
        redisTemplate.opsForZSet().add(Constant.LOCKED_PREFIX + metadata.getTopic(), metadata.getMetadata(), lock.getSeconds() + RobinUtil.now());
    }

    @Override
    public boolean locked(RobinMetadata metadata) {
        Double score = redisTemplate.opsForZSet()
                .score(Constant.LOCKED_PREFIX + metadata.getTopic(), metadata.getMetadata());
        if (score == null) {
            return false;
        }
        return score > RobinUtil.now();
    }

    @Override
    public void unlock(RobinMetadata metadata) {
        if (metadata != null) {
            redisTemplate.opsForZSet().remove(Constant.LOCKED_PREFIX + metadata.getTopic(), metadata.getMetadata());
            return;
        }
        // todo: null for all
    }

    public void clean() {
        new FutureTask<>(() -> {
            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
            final long anHour = 60 * 60;
            executor.scheduleWithFixedDelay(
                    () -> {
                        log.debug("robin cleaning");
                        // cleanAccessRecord();
                        // log.debug("access record is cleaned");
                        cleanSustainVisit();
                        log.debug("sustain visit record is cleaned");
                        cleanLock();
                        log.debug("locked record is cleaned");
                    },
                    0,
                    anHour,
                    TimeUnit.SECONDS);
            return null;
        }).run();
    }

    private void cleanLock() {
        redisTemplate.opsForZSet().removeRange(Constant.LOCKED_PREFIX, 0, RobinUtil.now());
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
        int SUSTAIN_VISIT_PRECISION = 10000;
        /**
         * 持续访问记录步长
         */
        double SUSTAIN_VISIT_STEP = 1.0d / SUSTAIN_VISIT_PRECISION;
        /**
         * 锁定元数据
         */
        String LOCKED_PREFIX = CACHE_PREFIX + "lock";
    }
}
