package kim.nzxy.robin.handler;

import kim.nzxy.robin.metadata.RobinMetadata;
import kim.nzxy.robin.util.RobinUtil;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认的缓存处理器
 * 为什么这里不提供单独 API 呢, 现在耦合性也太强了呀! 因为:
 * 1. 不方便处理事务,
 * 2. 还没想好怎么设计
 * 3. 还在想缓存隔离的设计
 *
 * @author lyun-chn
 * @since 2022/9/13 12:21
 */
@Slf4j
public class DefaultRobinCacheHandle implements RobinCacheHandler {
    /**
     * 持续访问记录, 格式: {topic: {metadata: 上次访问时间.连续访问次数}]}
     */
    public static final Map<String, ConcurrentHashMap<String, Double>> SUSTAIN_CACHE_MAP = new ConcurrentHashMap<>();
    /**
     * 封禁缓存, 格式: {topic: {metadata: 解禁时间时间戳(秒级)}}
     * todo: change to long, 避免有人封禁99年
     */
    public static final Map<String, ConcurrentHashMap<String, Integer>> LOCK_CACHE_MAP = new ConcurrentHashMap<>();
    /**
     * 持续访问记录key列表，用于定期清理数据
     */
    public static final Map<String, Duration> SUSTAIN_TOPIC_MAP = new ConcurrentHashMap<>();

    @Override
    public boolean sustainVisit(RobinMetadata robinMetadata, Duration timeFrameSize, Integer maxTimes) {
        String topic = robinMetadata.getTopic();
        String metadata = robinMetadata.getMetadata();
        int currentTimeFrame = RobinUtil.currentTimeFrame(timeFrameSize);
        SUSTAIN_TOPIC_MAP.put(topic, timeFrameSize);
        ConcurrentHashMap<String, Double> topicMap = SUSTAIN_CACHE_MAP.get(topic);
        // 不存在topic, 创建topic
        if (!SUSTAIN_CACHE_MAP.containsKey(topic)) {
            topicMap = new ConcurrentHashMap<>(16);
            SUSTAIN_CACHE_MAP.put(topic, topicMap);
        }
        Double latestVisit = topicMap.get(metadata);
        // 非连续访问
        if (latestVisit == null || currentTimeFrame - latestVisit > 1) {
            topicMap.put(metadata, currentTimeFrame + Constant.SUSTAIN_VISIT_STEP);
            return false;
        }
        // 窗口+1, 窗口连续数+1
        if (currentTimeFrame > latestVisit) {
            latestVisit = latestVisit + Constant.SUSTAIN_VISIT_STEP + 1;
        }
        if (latestVisit % 1 * Constant.SUSTAIN_VISIT_PRECISION >= maxTimes) {
            return true;
        }
        topicMap.put(metadata, latestVisit);
        return true;
    }

    private void cleanSustainVisit() {
        for (Map.Entry<String, Duration> entry : SUSTAIN_TOPIC_MAP.entrySet()) {
            int usefulTimeFrame = RobinUtil.currentTimeFrame(entry.getValue()) - 1;
            ConcurrentHashMap<String, Double> topicMap = SUSTAIN_CACHE_MAP.get(entry.getKey());
            for (String latestVisit : topicMap.keySet()) {
                if (topicMap.get(latestVisit) < usefulTimeFrame) {
                    topicMap.remove(latestVisit);
                }
            }
        }
    }

    @Override
    public void lock(RobinMetadata metadata, Duration lock) {
        ConcurrentHashMap<String, Integer> topicMap = LOCK_CACHE_MAP.get(metadata.getTopic());
        if (!LOCK_CACHE_MAP.containsKey(metadata.getTopic())) {
            topicMap = new ConcurrentHashMap<>();
            LOCK_CACHE_MAP.put(metadata.getTopic(), topicMap);
        }
        topicMap.put(metadata.getMetadata(), Math.toIntExact(lock.getSeconds() + RobinUtil.now()));
    }

    @Override
    public boolean locked(RobinMetadata metadata) {
        if (!LOCK_CACHE_MAP.containsKey(metadata.getTopic())) {
            return false;
        }
        ConcurrentHashMap<String, Integer> topicMap = LOCK_CACHE_MAP.get(metadata.getTopic());
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
        for (ConcurrentHashMap<String, Integer> topicMap : LOCK_CACHE_MAP.values()) {
            for (String metadata : topicMap.keySet()) {
                Integer lockTo = topicMap.get(metadata);
                if (lockTo == null || lockTo < now) {
                    topicMap.remove(metadata);
                }
            }
        }
    }

    public interface Constant {
        /**
         * 持续访问记录最大记录数量
         */
        int SUSTAIN_VISIT_PRECISION = 100000;
        /**
         * 持续访问记录步长
         */
        double SUSTAIN_VISIT_STEP = 1.0d / SUSTAIN_VISIT_PRECISION;
    }
}
