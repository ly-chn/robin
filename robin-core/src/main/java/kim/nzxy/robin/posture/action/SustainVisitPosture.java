package kim.nzxy.robin.posture.action;

import kim.nzxy.robin.metadata.RobinMetadata;
import kim.nzxy.robin.posture.RobinPosture;
import kim.nzxy.robin.posture.config.BuiltInEffort;
import kim.nzxy.robin.posture.config.BuiltInEffortConstant;
import kim.nzxy.robin.util.RobinUtil;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 持续访问控制
 *
 * @author lyun-chn
 * @since 2022/9/1 9:00
 */
@RobinPosture.PostureConfig(key = "sustain")
@Slf4j
public class SustainVisitPosture implements RobinPosture {

    /**
     * 持续访问记录, 格式: {topic: {metadata: 上次访问时间.连续访问次数}]}
     */
    public static final Map<String, ConcurrentHashMap<String, Double>> SUSTAIN_CACHE_MAP = new ConcurrentHashMap<>();
    /**
     * 持续访问记录key列表，用于定期清理数据, todo: 去掉它
     */
    public static final Map<String, Duration> SUSTAIN_TOPIC_MAP = new ConcurrentHashMap<>();

    @Override
    public boolean handler(RobinMetadata robinMetadata) {
        BuiltInEffort.SustainVisit expandEffort = getExpandEffort(robinMetadata.getTopic());
        Integer maxTimes = expandEffort.getMaxTimes();
        if (maxTimes < 1) {
            log.debug("直接禁止访问: {}", robinMetadata);
            return false;
        }
        String topic = robinMetadata.getTopic();
        Duration timeFrameSize = expandEffort.getTimeFrameSize();

        String metadata = robinMetadata.getMetadata();
        int currentTimeFrame = RobinUtil.currentTimeFrame(timeFrameSize);
        SUSTAIN_TOPIC_MAP.put(topic, timeFrameSize);
        // 不存在topic, 创建topic
        if (!SUSTAIN_CACHE_MAP.containsKey(topic)) {
            SUSTAIN_CACHE_MAP.put(topic, new ConcurrentHashMap<>(16));
        }
        ConcurrentHashMap<String, Double> topicMap = SUSTAIN_CACHE_MAP.get(topic);
        Double latestVisit = topicMap.get(metadata);
        // 非连续访问
        if (latestVisit == null || currentTimeFrame - latestVisit > 1) {
            topicMap.put(metadata, currentTimeFrame + BuiltInEffortConstant.SUSTAIN_VISIT_STEP);
            return false;
        }
        // 窗口+1, 窗口连续数+1
        if (currentTimeFrame > latestVisit) {
            latestVisit = latestVisit + BuiltInEffortConstant.SUSTAIN_VISIT_STEP + 1;
        }
        if (latestVisit % 1 * BuiltInEffortConstant.SUSTAIN_VISIT_PRECISION >= maxTimes) {
            return true;
        }
        topicMap.put(metadata, latestVisit);
        return true;
    }
}