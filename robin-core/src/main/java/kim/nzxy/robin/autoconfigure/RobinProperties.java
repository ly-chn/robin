package kim.nzxy.robin.autoconfigure;

import kim.nzxy.robin.enums.RobinModeEnum;
import kim.nzxy.robin.enums.RobinRuleEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Data
public class RobinProperties {
    /**
     * 应用校验的规则, 会依照顺序进行检测, 默认为所有规则
     */
    private List<RobinRuleEnum> includeRule = Arrays.asList(RobinRuleEnum.values());
    /**
     * 基础配置
     */
    private RobinResourcePattern resource = new RobinResourcePattern();
    /**
     * IP 校验细则
     */
    private RobinIpRule ip = new RobinIpRule();

    @Data
    public static class RobinResourcePattern {
        /**
         * 应用模式, 方便在打包后简单启用禁用
         */
        private RobinModeEnum mode = RobinModeEnum.DEFAULT;
        /**
         * 应用规则的路径, 和[excludePatterns]共同作用用于打包后的反悔现象
         */
        private List<String> includePatterns = Collections.singletonList("/**");
        /**
         * 排除规则的路径, 优先级高于[includePatterns]
         */
        private List<String> excludePatterns = new ArrayList<>();
    }


    /**
     * 频率控制, 单位时间[duration]内最大可访问次数[frequency]
     */
    @Data
    public static class RobinFrequency {

        /**
         * 存续时间, 受控期
         */
        private Duration duration = Duration.ofMinutes(1);

        /**
         * 可访问次数
         */
        private Long frequency = 10L;

        /**
         * 到期后自动解封
         */
        private Duration unlock = Duration.ofHours(1L);
    }

    /**
     * IP 规则
     */
    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class RobinIpRule extends RobinFrequency {
        /**
         * IP地址白名单
         */
        private List<String> whitelist = new ArrayList<>();
        /**
         * IP地址黑名单
         */
        private List<String> blacklist = new ArrayList<>();
    }

    /**
     * 一些细节优化的地方
     */
    @Data
    public static class Detail {
        private String cache;

        @Data
        public static class CacheDetail {
            /**
             * 缓存清理时间
             */
            private List<LocalTime> cleanAt = Collections.singletonList(LocalTime.of(0, 0));
            // 最大缓存数等等? 暂时不考虑
        }
    }
}
