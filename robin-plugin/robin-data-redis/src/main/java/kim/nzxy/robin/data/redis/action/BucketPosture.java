package kim.nzxy.robin.data.redis.action;

import kim.nzxy.robin.data.redis.util.RobinLuaLoader;
import kim.nzxy.robin.metadata.RobinMetadata;
import kim.nzxy.robin.posture.RobinPosture;
import kim.nzxy.robin.posture.config.BuiltInEffort;
import kim.nzxy.robin.posture.config.BuiltInEffortConstant;
import kim.nzxy.robin.util.RobinUtil;
import lombok.CustomLog;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;

/**
 * 令牌桶实现
 *
 * @author lyun-chn
 * @since 2022/9/1 14:03
 */
@RobinPosture.PostureConfig(key = "bucket")
@CustomLog
public class BucketPosture extends AbstractRobinRedisPosture {
    private static final DefaultRedisScript<Boolean> BUCKET_LUA = RobinLuaLoader.file("bucket");
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
