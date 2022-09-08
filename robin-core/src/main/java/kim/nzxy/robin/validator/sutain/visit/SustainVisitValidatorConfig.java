package kim.nzxy.robin.validator.sutain.visit;

import kim.nzxy.robin.autoconfigure.RobinBasicStrategy;
import kim.nzxy.robin.autoconfigure.ValidatorConfig;
import lombok.Data;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * 持续访问配置
 *
 * @author lyun-chn
 * @since 2022/9/1 9:03
 */
@Data
public class SustainVisitValidatorConfig {
    private Map<String, SustainVisit> sustain = new HashMap<>();

    @Data
    public static class SustainVisit implements ValidatorConfig {

        private RobinBasicStrategy basic;

        private Config config;

        @Data
        public static class Config {
            /**
             * 时间窗口大小
             */
            private Duration timeFrameSize;
            /**
             * 可访问最大次数
             */
            private Integer maxTimes;
        }
    }
}
