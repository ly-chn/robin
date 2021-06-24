package kim.nzxy.robin.spring.boot;

import kim.nzxy.robin.handler.RobinContextHandler;
import kim.nzxy.robin.spring.boot.util.SpringContextUtil;
import org.springframework.stereotype.Component;

/**
 * @author xy
 * @since 2021/6/8
 */
@Component
public class RobinContextHandlerImpl implements RobinContextHandler {
    @Override
    public String ip() {
        return SpringContextUtil.currentRequest().getRemoteAddr();
    }

    @Override
    public String uri() {
        return SpringContextUtil.currentRequest().getRequestURI();
    }

    @Override
    public String ua() {
        return SpringContextUtil.currentRequest().getParameter("User-Agent");
    }
}
