package kim.nzxy.robin.spring.boot.autoconfigure;

import kim.nzxy.robin.config.RobinManagement;
import kim.nzxy.robin.handler.RobinCacheHandler;
import kim.nzxy.robin.interceptor.RobinInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author lyun-chn
 */
@Component
@Slf4j
public class RobinAutowired {
    /**
     * 自定义缓存管理器
     */
    @Autowired(required = false)
    public void define(RobinCacheHandler handler) {
        RobinManagement.setCacheHandler(handler);
    }

    /**
     * 检测钩子
     */
    @Autowired(required = false)
    public void define(RobinInterceptor filter) {
        RobinManagement.setRobinInterceptor(filter);
    }
}
