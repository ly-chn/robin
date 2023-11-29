package kim.nzxy.robin.data.redis.action;

import kim.nzxy.robin.data.redis.util.RobinLuaUtil;
import kim.nzxy.robin.factory.RobinEffortFactory;
import kim.nzxy.robin.metadata.RobinMetadata;
import kim.nzxy.robin.posture.RobinPosture;
import kim.nzxy.robin.posture.config.BuiltInEffort;
import kim.nzxy.robin.posture.config.BuiltInEffortConstant;
import kim.nzxy.robin.util.RobinUtil;
import lombok.CustomLog;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 令牌桶实现
 *
 * @author ly-chn
 * @since 2022/9/1 14:03
 */
@RobinPosture.PostureConfig(key = "bucket")
@CustomLog
public class BucketPosture extends AbstractRobinRedisPosture {
    private static final DefaultRedisScript<Boolean> BUCKET_LUA = RobinLuaUtil.loadBool("bucket");
    private static final DefaultRedisScript<Boolean> BUCKET_CLEAN_LUA = RobinLuaUtil.loadBool("sustain-visit-clean");

    @Override
    public boolean handler(RobinMetadata robinMetadata) {
        BuiltInEffort.Bucket effort = getExpandEffort(robinMetadata.getTopic());
        String key = Constant.BUCKET_PREFIX + robinMetadata.getTopic();
        int currentTimeFrame = RobinUtil.currentTimeFrame(effort.getGenerationInterval());
        return Boolean.TRUE.equals(getStringRedisTemplate().execute(BUCKET_LUA,
                Collections.singletonList(key),
                robinMetadata.getMetadata(),
                String.valueOf(currentTimeFrame),
                String.valueOf(effort.getCapacity()),
                String.valueOf(effort.getTokenCount()),
                String.valueOf(BuiltInEffortConstant.BUCKET_PRECISION),
                Boolean.toString(log.isDebugEnabled())
        ));
    }

    @Override
    public void freshenUp() {
        Set<String> topicSet = RobinEffortFactory.getTopicByKey(BuiltInEffort.Fields.bucket);
        if (topicSet.isEmpty()) {
            return;
        }
        List<String> keys = new ArrayList<>();
        List<String> values = new ArrayList<>();
        topicSet.forEach(topic -> {
            keys.add(topic);
            BuiltInEffort.Bucket bucket = getExpandEffort(topic);
            int currentTimeFrame = RobinUtil.currentTimeFrame(bucket.getGenerationInterval());
            int maxTimeframe = currentTimeFrame - (bucket.getCapacity() + bucket.getTokenCount() - 1) / bucket.getTokenCount();
            values.add(String.valueOf(maxTimeframe));
        });
        getStringRedisTemplate().execute(BUCKET_CLEAN_LUA, keys, values.toArray());
    }

    interface Constant {
        /**
         * 缓存前缀
         */
        String CACHE_PREFIX = "robin:";
        /**
         * 持续访问缓存前缀
         */
        String BUCKET_PREFIX = CACHE_PREFIX + "bucket:";
    }
}
