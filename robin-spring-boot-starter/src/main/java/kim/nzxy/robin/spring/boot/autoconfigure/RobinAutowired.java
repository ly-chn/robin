package kim.nzxy.robin.spring.boot.autoconfigure;

import kim.nzxy.robin.config.RobinManagement;
import kim.nzxy.robin.handler.RobinLockHandler;
import kim.nzxy.robin.interceptor.RobinInterceptor;
import lombok.CustomLog;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author lyun-chn
 */
@CustomLog
public class RobinAutowired {
    /**
     * 自定义缓存管理器
     * @param handler 锁定缓存管理器
     */
    @Autowired(required = false)
    public void define(RobinLockHandler handler) {
        RobinManagement.setRobinLockHandler(handler);
    }

    /**
     * 检测钩子
     * @param filter 检测钩子
     */
    @Autowired(required = false)
    public void define(RobinInterceptor filter) {
        RobinManagement.setRobinInterceptor(filter);
    }
}
