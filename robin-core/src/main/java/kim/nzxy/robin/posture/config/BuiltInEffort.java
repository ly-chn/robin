package kim.nzxy.robin.posture.config;

import kim.nzxy.robin.autoconfigure.RobinEffortBasic;
import kim.nzxy.robin.enums.RobinExceptionEnum;
import kim.nzxy.robin.exception.RobinException;
import lombok.CustomLog;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * 这里分解除去, 嵌套深了用着不太舒适
 * @author ly-chn
 * @since 2022/9/19 8:50
 */
@Data
@CustomLog
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
         * 令牌生成间隔, 多久产生一次令牌
         */
        private Duration generationInterval = Duration.ofMinutes(10);
        /**
         * 令牌生成数量
         */
        private Integer tokenCount = 1;
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
        private Integer maxTimes = 1;

        public void setMaxTimes(Integer maxTimes) {
            if (maxTimes >= BuiltInEffortConstant.SUSTAIN_VISIT_PRECISION) {
                log.error("参数初始化失败, 原因: 最大连续访问次数需小于: " + BuiltInEffortConstant.SUSTAIN_VISIT_PRECISION);
                throw new RobinException.Panic(RobinExceptionEnum.Panic.ConfigParamVerifyFailed);
            }
            this.maxTimes = maxTimes;
        }
    }
}
