package kim.nzxy.robin.validator.bucket;

import kim.nzxy.robin.autoconfigure.RobinBasicStrategy;
import kim.nzxy.robin.validator.RobinValidator;

/**
 * 令牌桶实现
 *
 * @author lyun-chn
 * @since 2022/9/1 14:03
 */
@RobinValidator.ValidatorConfig(key = "bucket")
public class BucketValidator implements RobinValidator {
    @Override
    public void preHandle(String topic, String metadata, RobinBasicStrategy basicConfig, Object validatorConfig) {

    }
}
