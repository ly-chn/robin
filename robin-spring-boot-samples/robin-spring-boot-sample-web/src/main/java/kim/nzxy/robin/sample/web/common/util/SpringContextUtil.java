package kim.nzxy.robin.sample.web.common.util;

import lombok.val;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * pring 上下文工具
 *
 * @author xy
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

    /**
     * get referer
     *
     * @return referer
     */
    public static String referer() {
        val request = currentRequest();
        return Optional.ofNullable(request.getHeader("Referer")).orElse("");
    }
}
