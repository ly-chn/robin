package kim.nzxy.robin.posture;

import kim.nzxy.robin.autoconfigure.RobinEffort;
import kim.nzxy.robin.autoconfigure.RobinEffortBasic;
import lombok.Data;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xuyf
 * @since 2022/9/19 8:50
 */
@Data
public class BuiltInEffort {
    private Map<String, SustainVisit> sustain = new HashMap<>();
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

    @Data
    public static class SustainVisit implements RobinEffort {

        private RobinEffortBasic basic;

        private EffortExpand expand;

        @Data
        public static class EffortExpand {
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
