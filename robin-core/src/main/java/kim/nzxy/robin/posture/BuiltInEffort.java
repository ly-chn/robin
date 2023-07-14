package kim.nzxy.robin.posture;

import kim.nzxy.robin.autoconfigure.RobinEffortBasic;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 这里分解除去, 嵌套深了用着不太舒适
 * @author xuyf
 * @since 2022/9/19 8:50
 */
@Data
public class BuiltInEffort {
    /**
     * 持续访问策略配置
     */
    private List<SustainVisit> sustain = new ArrayList<>();

    /**
     * 令牌桶策略配置
     */
    private List<Bucket> bucket = new ArrayList<>();

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Bucket extends RobinEffortBasic {
        /**
         * 令牌桶大小
         */
        private Integer capacity = 1;
        /**
         * 多久产生一个令牌
         */
        private Duration rate = Duration.ofMinutes(10);
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class SustainVisit extends RobinEffortBasic {

        /**
         * 时间窗口大小
         */
        private Duration timeFrameSize = Duration.ofMinutes(1);
        /**
         * 可访问最大次数
         */
        private Integer maxTimes =  1;
    }
}
