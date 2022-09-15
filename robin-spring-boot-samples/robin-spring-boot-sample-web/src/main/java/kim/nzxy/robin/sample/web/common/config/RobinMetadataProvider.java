package kim.nzxy.robin.sample.web.common.config;

import kim.nzxy.robin.factory.RobinMetadataFactory;
import kim.nzxy.robin.sample.web.common.util.SpringContextUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
        RobinMetadataFactory.register("ip-b", this::getIp);
        RobinMetadataFactory.register("ip", this::getIp);
        RobinMetadataFactory.register("referer", this::getIp);
    }
}
