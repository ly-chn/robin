package kim.nzxy.robin.sample.web.common.config;

import kim.nzxy.robin.factory.RobinMetadataFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 元数据处理
 *
 * @author lyun-chn
 * @since 2022/9/8 17:11
 */
@Component
@RequiredArgsConstructor
public class RobinMetadataProvider implements InitializingBean {
    private final HttpServletRequest request;

    public String getIp() {
        return request.getRemoteAddr();
    }

    @Override
    public void afterPropertiesSet() {
        RobinMetadataFactory.register("ip-sensitive", this::getIp);
        RobinMetadataFactory.register("ip-normal", this::getIp);
        RobinMetadataFactory.register("referer", this::getIp);
    }
}
