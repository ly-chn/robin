package kim.nzxy.robin.posture.sutain.visit;

import kim.nzxy.robin.autoconfigure.RobinEffort;
import kim.nzxy.robin.autoconfigure.RobinEffortBasic;
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
public class SustainVisitEffort {
    private Map<String, SustainVisit> sustain = new HashMap<>();

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
