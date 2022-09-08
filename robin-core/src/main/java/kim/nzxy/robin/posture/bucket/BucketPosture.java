package kim.nzxy.robin.posture.bucket;

import kim.nzxy.robin.autoconfigure.RobinEffortBasic;
import kim.nzxy.robin.posture.RobinPosture;

/**
 * 令牌桶实现
 *
 * @author lyun-chn
 * @since 2022/9/1 14:03
 */
@RobinPosture.PostureConfig(key = "bucket")
public class BucketPosture implements RobinPosture {
    @Override
    public void preHandle(String topic, String metadata, RobinEffortBasic basicConfig, Object validatorConfig) {

    }
}
