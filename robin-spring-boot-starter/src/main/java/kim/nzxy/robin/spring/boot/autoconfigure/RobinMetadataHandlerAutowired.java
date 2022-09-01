package kim.nzxy.robin.spring.boot.autoconfigure;

import kim.nzxy.robin.handler.RobinContextHandler;
import kim.nzxy.robin.handler.RobinMetadataHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 元数据自动注入类
 *
 * @author xuyf
 * @since 2022/9/1 14:36
 */
@Component
public class RobinMetadataHandlerAutowired {
    @Bean
    public RobinMetadataHandler handler() {
        return () -> "";
    }
}
