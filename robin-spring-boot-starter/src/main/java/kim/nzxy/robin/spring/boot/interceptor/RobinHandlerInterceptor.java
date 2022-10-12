package kim.nzxy.robin.spring.boot.interceptor;

import kim.nzxy.robin.annotations.RobinSkip;
import kim.nzxy.robin.daily.RobinGetUp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author lyun-chn
 * @since 2021/6/4
 */
@Slf4j
public class RobinHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (catchAble(handler)) {
            RobinGetUp.getUp(((HandlerMethod) handler).getMethod());
        }
        return true;
    }

    /**
     * @param handler 处理类
     * @return 返回true表示需要Robin处理, 否则不需要
     */
    private boolean catchAble(Object handler) {
        if (handler instanceof HandlerMethod) {
            Method method = ((HandlerMethod) handler).getMethod();
            return !method.isAnnotationPresent(RobinSkip.class) && !method.getDeclaringClass().isAnnotationPresent(RobinSkip.class);
        }
        return false;
    }
}
