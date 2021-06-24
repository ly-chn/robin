package kim.nzxy.robin.autoconfigure;

import kim.nzxy.robin.enums.RobinModeEnum;
import kim.nzxy.robin.enums.RobinRuleEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author xy
 * @since 2021/6/5
 */
@Data
public class RobinProperties {
    /**
     * 应用校验的规则, 会依照顺序进行检测, 默认为所有规则
     */
    private List<RobinRuleEnum> includeRule = new ArrayList<>();
    /**
     * 基础配置
     */
    private RobinResourcePattern resource = new RobinResourcePattern();
    /**
     * IP 持续访问校验细则
     */
    private RobinIpRule ipFrequentAccess = new RobinIpRule();

    /**
     * IP 持续访问检验规则
     */
    private IpContinuousVisit continuousVisit = new IpContinuousVisit();
    /**
     * 一些细节优化的地方, 可以提升系统效率, 合理分配资源等
     */
    private Detail detail = new Detail();

    /**
     * 黑边名单配置
     */
    private BlackWhiteListConfig blackWhiteList = new BlackWhiteListConfig();

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
     * 黑白名单配置
     */
    @Data
    public static class BlackWhiteListConfig {
        /**
         * 通用 IP 地址黑边名单
         */
        private BlackWhiteList ip = new BlackWhiteList();
    }

    /**
     * 黑边名单
     */
    @Data
    public static class BlackWhiteList {
        /**
         * IP地址白名单
         */
        private List<String> allowlist = new ArrayList<>();
        /**
         * IP地址黑名单
         */
        private List<String> blocklist = new ArrayList<>();
    }

    /**
     * 访问频率控制相关
     */
    @Data
    public static class FrequencyControl {
        /**
         * 存续时间, 受控期
         */
        private Duration duration = Duration.ofMinutes(1);

        /**
         * 可访问次数
         */
        private Integer frequency = 10;

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
    public static class RobinIpRule extends FrequencyControl {
    }

    /**
     * IP 频繁访问次数
     * 默认为连续访问达到100次则
     */
    @Data
    public static class IpContinuousVisit {
        /**
         * 时间窗口
         */
        private Duration duration = Duration.ofMinutes(1);
        /**
         * 持续访问次数
         */
        private Integer times = 100;
        /**
         * 到期后自动解封
         */
        private Duration unlock = Duration.ofHours(1L);
    }

    /**
     * 一些细节优化的地方
     */
    @Data
    public static class Detail {
        private CacheDetail cache = new CacheDetail();

        @Data
        public static class CacheDetail {
            /**
             * 缓存清理时间
             */
            private List<LocalTime> cleanAt = new ArrayList<LocalTime>() {{
                for (int i = 0; i < 24; i++) {
                    add(LocalTime.of(i, 0));
                }
            }};
            // 最大缓存数等等? 暂时不考虑
        }
    }
}
