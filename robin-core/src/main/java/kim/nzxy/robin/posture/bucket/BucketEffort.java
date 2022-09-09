package kim.nzxy.robin.posture.bucket;

import kim.nzxy.robin.autoconfigure.RobinEffort;
import kim.nzxy.robin.autoconfigure.RobinEffortBasic;
import lombok.Data;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * 令牌桶配置
 *
 * @author lyun-chn
 * @since 2022/9/1 9:03
 */
@Data
public class BucketEffort {
    private Map<String, Bucket> bucket = new HashMap<>();

    @Data
    public static class Bucket implements RobinEffort {

        private RobinEffortBasic basic = new RobinEffortBasic();

        private EffortExpand expand;

        @Data
        public static class EffortExpand {
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
