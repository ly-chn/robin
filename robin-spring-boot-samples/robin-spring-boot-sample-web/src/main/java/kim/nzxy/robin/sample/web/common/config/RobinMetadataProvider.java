package kim.nzxy.robin.sample.web.common.config;

import kim.nzxy.robin.factory.RobinMetadataFactory;
import kim.nzxy.robin.sample.web.common.util.SpringContextUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * 元数据处理
 *
 * @author xuyf
 * @since 2022/9/8 17:11
 */
@Component
public class RobinMetadataProvider implements InitializingBean {

    public String getIp() {
        return SpringContextUtil.currentRequest().getRemoteAddr();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        RobinMetadataFactory.register("ip-b", this::getIp);
        RobinMetadataFactory.register("ip", this::getIp);
        RobinMetadataFactory.register("referer", this::getIp);
    }
}
