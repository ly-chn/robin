package kim.nzxy.robin.config;

import kim.nzxy.robin.handler.DefaultRobinCacheHandle;
import kim.nzxy.robin.handler.RobinCacheHandler;
import kim.nzxy.robin.interceptor.DefaultRobinInterceptorImpl;
import kim.nzxy.robin.interceptor.RobinInterceptor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

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
    private static RobinInterceptor robinInterceptor;

    /**
     * 垃圾清理线程池
     */
    @SuppressWarnings({"AlibabaThreadPoolCreation", "AlibabaConstantFieldShouldBeUpperCase"})
    private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

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
        // 停止原来缓存清理器
        if (RobinManagement.cacheHandler != null) {
            // todo: 定时清理缓存
            // cacheHandler.freshenUp();
        }
        RobinManagement.cacheHandler = cacheHandler;
        // 启用新的缓存清理器
    }
}
