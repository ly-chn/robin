package kim.nzxy.robin.posture.action;

import kim.nzxy.robin.metadata.RobinMetadata;
import kim.nzxy.robin.posture.RobinPosture;
import kim.nzxy.robin.posture.config.BuiltInEffort;
import kim.nzxy.robin.posture.config.BuiltInEffortConstant;
import kim.nzxy.robin.util.RobinUtil;
import lombok.CustomLog;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 令牌桶实现
 *
 * @author ly-chn
 * @since 2022/9/1 14:03
 */
@RobinPosture.PostureConfig(key = "bucket")
@CustomLog
public class BucketPosture implements RobinPosture {
    /**
     * 令牌桶限流, 格式: {topic: {metadata: 上次访问时间窗口.剩余token数量}}
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
            topicMap.put(robinMetadata.getMetadata(),
                    RobinUtil.assembleDecimal(currentTimeFrame, tokenCount - 1, BuiltInEffortConstant.BUCKET_PRECISION));
            return true;
        }
        int[] bucketInfoParts = RobinUtil.disassembleDecimal(bucketInfo, BuiltInEffortConstant.BUCKET_PRECISION);
        // 上次访问窗口
        int latestTimeframe = bucketInfoParts[0];
        // 剩余token数量
        int latestTokenCount = bucketInfoParts[1];
        int maxTimeframe = (capacity + tokenCount - 1) / tokenCount;
        // 剩余token
        int restToken = Math.min(Math.min((currentTimeFrame - latestTimeframe), maxTimeframe) * tokenCount + latestTokenCount, capacity) - 1;
        log.info("robin bucket topic: " + robinMetadata.getTopic() + ", metadata: " + robinMetadata.getMetadata() + " rest token: " + restToken);
        if (restToken < 0) {
            return false;
        }
        topicMap.put(robinMetadata.getMetadata(),
                RobinUtil.assembleDecimal(currentTimeFrame, restToken, BuiltInEffortConstant.BUCKET_PRECISION));
        return true;
    }

    @Override
    public void freshenUp() {
        BUCKET_CACHE_MAP.forEach((topic, topicMap) -> {
            if (RobinUtil.isEmpty(topicMap)) {
                return;
            }
            BuiltInEffort.Bucket bucket = getExpandEffort(topic);
            int currentTimeFrame = RobinUtil.currentTimeFrame(bucket.getGenerationInterval());
            int maxTimeframe = currentTimeFrame - (bucket.getCapacity() + bucket.getTokenCount() - 1) / bucket.getTokenCount();
            topicMap.forEach((metadata, lastVisit) -> {
                if (lastVisit.intValue() <= maxTimeframe) {
                    topicMap.remove(metadata);
                }
            });
        });
    }
}
