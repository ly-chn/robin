package kim.nzxy.robin.sample.web.common.config;

import kim.nzxy.robin.handler.RobinMetadataHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;

/**
 * 元数据处理
 *
 * @author lyun-chn
 * @since 2022/9/8 17:11
 */
@Configuration
@RequiredArgsConstructor
public class RobinMetadataProvider {
    private final HttpServletRequest request;

    @Bean
    public RobinMetadataHandler ip() {
        return request::getRemoteAddr;
    }

    @Bean
    public RobinMetadataHandler referer() {
        return () -> request.getHeader(HttpHeaders.REFERER);
    }
}
