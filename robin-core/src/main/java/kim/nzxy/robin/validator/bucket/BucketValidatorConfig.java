package kim.nzxy.robin.validator.bucket;

import kim.nzxy.robin.autoconfigure.RobinBasicStrategy;
import kim.nzxy.robin.autoconfigure.ValidatorConfig;
import lombok.Data;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * 令牌桶配置
 *
 * @author xuyf
 * @since 2022/9/1 9:03
 */
@Data
public class BucketValidatorConfig {
    private Map<String, Bucket> bucket = new HashMap<>();

    @Data
    public static class Bucket implements ValidatorConfig {

        private RobinBasicStrategy basic;

        private Config config;

        @Data
        public static class Config {
            /**
             * 时间窗口大小
             */
            private Integer capacity = 100;
            /**
             * 可访问最大次数
             */
            private Duration rate = Duration.ofMillis(10);
        }
    }
}
