package kim.nzxy.robin.spring.boot.interceptor;

import kim.nzxy.robin.Robin;
import kim.nzxy.robin.annotations.RobinSkip;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author xy
 * @since 2021/6/4
 */
@Slf4j
public class RobinHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod) {
            Method method = ((HandlerMethod) handler).getMethod();
            if (method.isAnnotationPresent(RobinSkip.class) || method.getDeclaringClass().isAnnotationPresent(RobinSkip.class)) {
                return true;
            }
            Robin.start();
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // todo: metadata等数据放到ThreadLocal中
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }
}
