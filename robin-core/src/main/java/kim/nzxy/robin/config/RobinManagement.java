package kim.nzxy.robin.config;

import kim.nzxy.robin.handler.DefaultRobinCacheHandle;
import kim.nzxy.robin.handler.RobinCacheHandler;
import kim.nzxy.robin.interceptor.DefaultRobinInterceptorImpl;
import kim.nzxy.robin.interceptor.RobinInterceptor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author xy
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
        //noinspection AlibabaThreadPoolCreation
        Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(() -> {
            try {
                log.debug("robin clean cache");
                getCacheHandler().freshenUp();
                log.info("robin clean cache over");
            } catch (Exception e) {
                log.error("robin clean cache error: {}", e.getMessage());
            }
        }, 1, 60, TimeUnit.MINUTES);
    }

    public static RobinInterceptor getRobinInterceptor() {
        if (robinInterceptor == null) {
            robinInterceptor = new DefaultRobinInterceptorImpl();
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

    public synchronized static void setCacheHandler(RobinCacheHandler cacheHandler) {
        RobinManagement.cacheHandler = cacheHandler;
    }
}
