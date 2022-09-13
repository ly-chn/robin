package kim.nzxy.robin.handler;

import kim.nzxy.robin.config.RobinMetadata;
import kim.nzxy.robin.util.RobinTimeFrameUtil;
import kim.nzxy.robin.util.RobinUtil;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 默认的缓存处理器
 *
 * @author ly-chn
 * @since 2022/9/13 12:21
 */
@Slf4j
public class DefaultRobinCacheHandle implements RobinCacheHandler {
    /**
     * 持续访问记录, 格式: {topic: {metadata: 上次访问时间.连续访问次数}]}
     */
    public static final Map<String, HashMap<String, Double>> SUSTAIN_CACHE_MAP = new HashMap<>();
    /**
     * 封禁缓存, 格式: {topic: {metadata: 解禁时间时间戳(秒级)}}
     */
    public static final Map<String, HashMap<String, Integer>> LOCK_CACHE_MAP = new HashMap<>();
    /**
     * 持续访问记录key列表，用于定期清理数据
     */
    public static final Map<String, Duration> SUSTAIN_TOPIC_MAP = new HashMap<>();

    @Override
    public int sustainVisit(RobinMetadata robinMetadata, Duration timeFrameSize) {
        String topic = robinMetadata.getTopic();
        String metadata = robinMetadata.getMetadata();
        int currentTimeFrame = RobinTimeFrameUtil.currentTimeFrame(timeFrameSize);
        SUSTAIN_TOPIC_MAP.put(topic, timeFrameSize);
        HashMap<String, Double> topicMap = SUSTAIN_CACHE_MAP.get(topic);
        // 不存在topic, 创建topic
        if (!SUSTAIN_CACHE_MAP.containsKey(topic)) {
            topicMap = new HashMap<>();
            SUSTAIN_CACHE_MAP.put(topic, topicMap);
        }
        Double latestVisit = topicMap.get(metadata);
        // 非连续访问
        if (latestVisit == null || currentTimeFrame - latestVisit > 1) {
            topicMap.put(metadata, currentTimeFrame + Constant.SUSTAIN_VISIT_STEP);
            return 1;
        }
        // 当前连续访问次数
        double i = latestVisit % 1;
        if (currentTimeFrame - latestVisit > 0) {
            i += Constant.SUSTAIN_VISIT_STEP;
        }
        topicMap.put(metadata, currentTimeFrame + (i / Constant.SUSTAIN_VISIT_PRECISION));
        return (int) (i * Constant.SUSTAIN_VISIT_PRECISION);
    }

    private void cleanSustainVisit() {
        for (Map.Entry<String, Duration> entry : SUSTAIN_TOPIC_MAP.entrySet()) {
            int usefulTimeFrame = RobinTimeFrameUtil.currentTimeFrame(entry.getValue()) - 1;
            HashMap<String, Double> topicMap = SUSTAIN_CACHE_MAP.get(entry.getKey());
            topicMap.forEach((value, timeFrame)->{
                if (topicMap.get(value)<usefulTimeFrame) {
                    topicMap.remove(value);
                }
            });
        }
    }

    @Override
    public void lock(RobinMetadata metadata, Duration lock) {
        HashMap<String, Integer> topicMap  = LOCK_CACHE_MAP.get(metadata.getTopic());
        if (!LOCK_CACHE_MAP.containsKey(metadata.getTopic())) {
            topicMap = new HashMap<>();
            LOCK_CACHE_MAP.put(metadata.getTopic(), topicMap);
        }
        topicMap.put(metadata.getMetadata(), Math.toIntExact(lock.getSeconds() + RobinUtil.now()));
    }

    @Override
    public boolean locked(RobinMetadata metadata) {
        if (!LOCK_CACHE_MAP.containsKey(metadata.getTopic())) {
            return false;
        }
        HashMap<String, Integer> topicMap  = LOCK_CACHE_MAP.get(metadata.getTopic());
        Integer score = topicMap.get(metadata.getMetadata());
        return score == null || score > RobinUtil.now();
    }

    @Override
    public void unlock(RobinMetadata metadata) {
        if (!LOCK_CACHE_MAP.containsKey(metadata.getTopic())) {
            return;
        }
        LOCK_CACHE_MAP.get(metadata.getTopic()).remove(metadata.getMetadata());
    }

    @Override
    public void freshenUp() {
        log.debug("robin cleaning");
        cleanSustainVisit();
        log.debug("sustain visit record is cleaned");
        cleanLock();
        log.debug("locked record is cleaned");
    }

    private void cleanLock() {
        if (LOCK_CACHE_MAP.isEmpty()) {
            return;
        }
        int now = RobinUtil.now();
        for (HashMap<String, Integer> topicMap : LOCK_CACHE_MAP.values()) {
            topicMap.forEach((metadata, lockTo)->{
                if (lockTo<=now) {
                    topicMap.remove(metadata);
                }
            });
        }
    }

    interface Constant {
        /**
         * 持续访问记录最大记录数量
         */
        int SUSTAIN_VISIT_PRECISION = 10000;
        /**
         * 持续访问记录步长
         */
        double SUSTAIN_VISIT_STEP = 1.0d / SUSTAIN_VISIT_PRECISION;
    }
}
