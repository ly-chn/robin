package kim.nzxy.robin.spring.boot.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Spring 上下文工具
 *
 * @author xy
 * @since 2021/6/8
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
