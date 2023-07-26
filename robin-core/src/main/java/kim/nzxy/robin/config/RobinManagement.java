package kim.nzxy.robin.config;

import kim.nzxy.robin.handler.DefaultRobinCacheHandle;
import kim.nzxy.robin.handler.RobinCacheHandler;
import kim.nzxy.robin.interceptor.RobinInterceptor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author lyun-chn
 * @since 2021/6/4
 */
@Slf4j
public class RobinManagement {
    /**
     * 缓存管理器
     */
    private static volatile RobinCacheHandler cacheHandler;
    /**
     * 过滤器
     */
    private static volatile RobinInterceptor robinInterceptor;

    static {
        // noinspection AlibabaThreadPoolCreation
        Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(() -> {
            try {
                getCacheHandler().freshenUp();
                log.debug("robin cache cleaned");
            } catch (Exception e) {
                log.error("robin cache cleaned with error: ", e);
            }
        }, 1, 60, TimeUnit.MINUTES);
    }

    public static RobinInterceptor getRobinInterceptor() {
        if (robinInterceptor == null) {
            synchronized (RobinManagement.class) {
                if (robinInterceptor == null) {
                    setRobinInterceptor(new RobinInterceptor() {});
                }
            }
        }
        return robinInterceptor;
    }

    public static void setRobinInterceptor(RobinInterceptor robinInterceptor) {
        RobinManagement.robinInterceptor = robinInterceptor;
    }

    public static RobinCacheHandler getCacheHandler() {
        if (cacheHandler == null) {
            synchronized (RobinManagement.class) {
                if (cacheHandler == null) {
                    setCacheHandler(new DefaultRobinCacheHandle());
                }
            }
        }
        return cacheHandler;
    }

    public static void setCacheHandler(RobinCacheHandler cacheHandler) {
        log.debug("register cache handler: {}", cacheHandler.getClass());
        RobinManagement.cacheHandler = cacheHandler;
    }
}
