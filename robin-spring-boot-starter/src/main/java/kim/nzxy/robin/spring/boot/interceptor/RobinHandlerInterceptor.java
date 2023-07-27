package kim.nzxy.robin.spring.boot.interceptor;

import kim.nzxy.robin.Robin;
import kim.nzxy.robin.annotations.RobinIgnore;
import kim.nzxy.robin.annotations.RobinTopic;
import kim.nzxy.robin.annotations.RobinTopicCollector;
import kim.nzxy.robin.config.RobinManagement;
import kim.nzxy.robin.daily.RobinGetUp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * @author lyun-chn
 * @since 2021/6/4
 */
@Slf4j
public class RobinHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (catchAble(handler)) {
            Robin.getUp(getExtraTopic((HandlerMethod) handler));
        }
        return true;
    }

    /**
     * @param handler 处理类
     * @return 返回true表示需要Robin处理, 否则不需要
     */
    private boolean catchAble(Object handler) {
        if (handler instanceof HandlerMethod) {
            // 用户取消拦截
            if (!RobinManagement.getRobinInterceptor().beforeCatch()) {
                return false;
            }
            // RobinIgnore
            Method method = ((HandlerMethod) handler).getMethod();
            return !method.isAnnotationPresent(RobinIgnore.class) && !method.getDeclaringClass().isAnnotationPresent(RobinIgnore.class);
        }
        return false;
    }

    /**
     * 寻找适配的策略
     *
     * @return 适配的验证策略集合
     */
    private Set<String> getExtraTopic(HandlerMethod handler) {
        Set<String> topics = new HashSet<>();
        AnnotatedElementUtils.getMergedRepeatableAnnotations(handler.getMethod(), RobinTopic.class, RobinTopicCollector.class)
                .forEach(it -> topics.add(it.value()));
        AnnotatedElementUtils.getMergedRepeatableAnnotations(handler.getMethod().getDeclaringClass(), RobinTopic.class, RobinTopicCollector.class)
                .forEach(it -> topics.add(it.value()));
        return topics;
    }
}
