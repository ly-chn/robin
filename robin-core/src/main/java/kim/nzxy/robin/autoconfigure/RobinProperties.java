package kim.nzxy.robin.autoconfigure;

import kim.nzxy.robin.enums.RobinModeEnum;
import kim.nzxy.robin.interceptor.RobinInterceptor;
import lombok.Data;

import java.time.Duration;
import java.util.*;

/**
 * @author xy
 * @since 2021/6/5
 */
@Data
public class RobinProperties {

    /**
     * 基础配置
     */
    private RobinResourcePattern resource = new RobinResourcePattern();
    /**
     * 策略默认配置，不推荐使用
     */
    private RobinBasicStrategy strategyBasic = new RobinBasicStrategy();
    /**
     * 持续访问策略
     */
    private List<StrategyConfig> strategy = new ArrayList<>();

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

    @SuppressWarnings("AlibabaAbstractClassShouldStartWithAbstractNaming")
    @Data
    public static abstract class StrategyConfig {
        private RobinBasicStrategy basic;
    }

    /**
     * 持续访问配置类
     */
    @Data
    public static class RobinSustainVisit {
        public static final RobinInterceptor strategyClass = null;
        /**
         * 基础配置
         */
        private RobinBasicStrategy basic;
        /**
         * 拓展配置
         */
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
