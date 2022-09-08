package kim.nzxy.robin.posture.sutain.visit;

import kim.nzxy.robin.autoconfigure.RobinEffortBasic;
import kim.nzxy.robin.posture.RobinPosture;

/**
 * 持续访问控制
 *
 * @author lyun-chn
 * @since 2022/9/1 9:00
 */
@RobinPosture.RobinValidatorConfig(key = "sustain")
public class SustainVisitPosture implements RobinPosture {
    @Override
    public void preHandle(String topic, String metadata, RobinEffortBasic basicConfig, Object validatorConfig) {

    }
}
