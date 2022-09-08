package kim.nzxy.robin.config;

import kim.nzxy.robin.autoconfigure.RobinBasicStrategy;
import kim.nzxy.robin.autoconfigure.ValidatorConfig;
import kim.nzxy.robin.enums.RobinExceptionEnum;
import kim.nzxy.robin.exception.RobinException;
import kim.nzxy.robin.handler.RobinCacheHandler;
import kim.nzxy.robin.handler.RobinContextHandler;
import kim.nzxy.robin.interceptor.DefaultRobinInterceptorImpl;
import kim.nzxy.robin.interceptor.RobinInterceptor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author xy
 * @since 2021/6/4
 */
@Slf4j
public class RobinManagement {
    /**
     * 验证器的策略配置
     */
    private static final Map<String, ValidatorConfig> VALIDATOR_CONFIG_MAP = new HashMap<>();
    /**
     * 缓存管理器
     */
    @Setter
    private static RobinCacheHandler cacheHandler;
    /**
     * 上下文管理器
     */
    @Getter
    @Setter
    private static RobinContextHandler contextHandler;
    /**
     * 过滤器
     */
    @Setter
    private static RobinInterceptor robinInterceptor;


    public static RobinInterceptor getRobinInterceptor() {
        if (robinInterceptor == null) {
            robinInterceptor = new DefaultRobinInterceptorImpl();
        }
        return robinInterceptor;
    }

    public static RobinCacheHandler getCacheHandler() {
        if (cacheHandler == null) {
            // todo: default cache handler
            throw new RobinException.Panic(RobinExceptionEnum.Panic.CacheHandlerMissing);
        }
        return cacheHandler;
    }


    /**
     * @return 全局策略
     */
    public static List<String> getGlobalValidatorTopic() {
        return VALIDATOR_CONFIG_MAP.entrySet().stream()
                .filter(entry -> entry.getValue().getBasic().getAsDefault())
                .map(Map.Entry::getKey).collect(Collectors.toList());
    }

    public static <T> T getStrategyConfig(String key, Class<T> type) {
        Object o = VALIDATOR_CONFIG_MAP.get(key).getConfig();
        // noinspection unchecked
        return (T) o;
    }
}
