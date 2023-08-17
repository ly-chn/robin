package kim.nzxy.robin.spring.boot;

import kim.nzxy.robin.spring.boot.interceptor.RobinHandlerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * todo: 为避免用户配置冲突, 应去除此自动装配
 * @author lyun-chn
 */
public class RobinConfiguration implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RobinHandlerInterceptor()).order(Ordered.HIGHEST_PRECEDENCE);
    }
}
