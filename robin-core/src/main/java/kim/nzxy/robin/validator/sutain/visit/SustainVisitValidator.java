package kim.nzxy.robin.validator.sutain.visit;

import kim.nzxy.robin.autoconfigure.RobinValidatorBasicConfig;
import kim.nzxy.robin.validator.RobinValidator;

/**
 * 持续访问控制
 *
 * @author lyun-chn
 * @since 2022/9/1 9:00
 */
@RobinValidator.RobinValidatorConfig(key = "sustain")
public class SustainVisitValidator implements RobinValidator{
    @Override
    public void preHandle(String topic, String metadata, RobinValidatorBasicConfig basicConfig, Object validatorConfig) {

    }
}
