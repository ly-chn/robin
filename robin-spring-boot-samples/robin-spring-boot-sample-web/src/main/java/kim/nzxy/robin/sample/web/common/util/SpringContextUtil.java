package kim.nzxy.robin.sample.web.common.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * pring 上下文工具
 *
 * @author lyun-chn
 * @since 2021/6/9
 */
public class SpringContextUtil {
    /**
     * 获取当前 HttpServletRequest
     */
    public static HttpServletRequest currentRequest() {
        // noinspection ConstantConditions
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
}
