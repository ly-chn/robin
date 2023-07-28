package kim.nzxy.robin.posture.action;

import kim.nzxy.robin.metadata.RobinMetadata;
import kim.nzxy.robin.posture.RobinPosture;
import kim.nzxy.robin.posture.config.BuiltInEffort;
import kim.nzxy.robin.posture.config.BuiltInEffortConstant;
import kim.nzxy.robin.util.RobinUtil;
import lombok.CustomLog;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 令牌桶实现
 *
 * @author lyun-chn
 * @since 2022/9/1 14:03
 */
@RobinPosture.PostureConfig(key = "bucket")
@CustomLog
public class BucketPosture implements RobinPosture {
    /**
     * 令牌桶限流, 格式: {topic: {metadata: 解禁时间时间戳(秒级)}}
     */
    private static final Map<String, ConcurrentHashMap<String, Double>> BUCKET_CACHE_MAP = new ConcurrentHashMap<>();
    @Override
    public boolean handler(RobinMetadata robinMetadata) {
        BuiltInEffort.Bucket expandEffort = getExpandEffort(robinMetadata.getTopic());
        Integer tokenCount = expandEffort.getTokenCount();
        Integer capacity = expandEffort.getCapacity();

        int currentTimeFrame = RobinUtil.currentTimeFrame(expandEffort.getGenerationInterval());
        if (!BUCKET_CACHE_MAP.containsKey(robinMetadata.getTopic())) {
            BUCKET_CACHE_MAP.put(robinMetadata.getTopic(), new ConcurrentHashMap<>(16));
        }
        ConcurrentHashMap<String, Double> topicMap = BUCKET_CACHE_MAP.get(robinMetadata.getTopic());
        Double bucketInfo = topicMap.get(robinMetadata.getMetadata());
        // 初始化桶
        if (bucketInfo == null) {
            topicMap.put(robinMetadata.getMetadata(), currentTimeFrame + (tokenCount - 1) / BuiltInEffortConstant.BUCKET_STEP);
            return true;
        }
        // 上次访问窗口
        int latestTimeframe = bucketInfo.intValue();
        // 剩余token数量
        int latestTokenCount = (int) (bucketInfo % 1 * BuiltInEffortConstant.BUCKET_PRECISION);
        // 剩余token todo: 溢出问题
        int restToken = Math.min((currentTimeFrame - latestTimeframe) * tokenCount + latestTokenCount, capacity) - 1;
        log.info("robin bucket topic: " + robinMetadata.getTopic() + ", metadata: " + robinMetadata.getMetadata() + " rest token: " + restToken);
        if (restToken < 0) {
            return false;
        }
        topicMap.put(robinMetadata.getMetadata(), currentTimeFrame + (restToken * BuiltInEffortConstant.BUCKET_STEP));
        return true;
    }
}
