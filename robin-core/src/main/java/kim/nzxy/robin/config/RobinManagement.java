package kim.nzxy.robin.config;

import kim.nzxy.robin.factory.RobinPostureFactory;
import kim.nzxy.robin.handler.DefaultRobinLockHandler;
import kim.nzxy.robin.handler.RobinLockHandler;
import kim.nzxy.robin.interceptor.RobinInterceptor;
import kim.nzxy.robin.posture.RobinPosture;
import lombok.AccessLevel;
import lombok.CustomLog;
import lombok.NoArgsConstructor;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author lyun-chn
 * @since 2021/6/4
 */
@CustomLog
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RobinManagement {
    /**
     * 缓存管理器
     */
    private static volatile RobinLockHandler robinLockHandler;
    /**
     * 过滤器
     */
    private static volatile RobinInterceptor robinInterceptor;

    static {
        // noinspection AlibabaThreadPoolCreation
        Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(() -> {
            try {
                getRobinLockHandler().freshenUp();
                log.debug("robin lock cache cleaned");
                RobinPostureFactory.getInvokeStrategyMap().values()
                        .forEach(RobinPosture::freshenUp);
                log.debug("robin posture cache cleaned");
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

    public static RobinLockHandler getRobinLockHandler() {
        if (robinLockHandler == null) {
            synchronized (RobinManagement.class) {
                if (robinLockHandler == null) {
                    setRobinLockHandler(new DefaultRobinLockHandler());
                }
            }
        }
        return robinLockHandler;
    }

    public static void setRobinLockHandler(RobinLockHandler robinLockHandler) {
        if (log.isDebugEnabled()) {
            log.debug("register cache handler: " + robinLockHandler.getClass());
        }
        RobinManagement.robinLockHandler = robinLockHandler;
    }
}
