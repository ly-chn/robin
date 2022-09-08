package kim.nzxy.robin.validator.bucket;

import kim.nzxy.robin.autoconfigure.RobinValidatorBasicConfig;
import kim.nzxy.robin.validator.RobinValidator;

/**
 * 令牌桶实现
 *
 * @author lyun-chn
 * @since 2022/9/1 14:03
 */
@RobinValidator.RobinValidatorConfig(key = "bucket")
public class BucketValidator implements RobinValidator {
    @Override
    public void preHandle(String topic, String metadata, RobinValidatorBasicConfig basicConfig, Object validatorConfig) {

    }
}
