package kim.nzxy.robin.spring.boot.autoconfigure;

import kim.nzxy.robin.autoconfigure.RobinProperties;
import kim.nzxy.robin.config.RobinManagement;
import kim.nzxy.robin.handler.RobinContextHandler;
import kim.nzxy.robin.spring.boot.RedisRobinCacheHandlerImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RobinAutowired {
    //<editor-fold desc="robin properties autowired">
    @ConfigurationProperties(prefix = "robin")
    @Bean
    public RobinProperties robinProperties() {
        return new RobinProperties();
    }

    @Autowired
    public void define(RobinProperties robinProperties) {
        RobinManagement.setRobinProperties(robinProperties);
    }
    //</editor-fold>

    /**
     * 自定义缓存管理器
     */
    @Autowired
    @DependsOn("robinProperties")
    public void define(RedisRobinCacheHandlerImpl handler) {
        while (true) {
            if (RobinManagement.getRobinProperties() == null) break;
        }
        RobinManagement.setCacheHandler(handler);
    }

    /**
     * 上下文管理器
     */
    @Autowired
    public void define(RobinContextHandler handler) {
        RobinManagement.setContextHandler(handler);
    }
}
