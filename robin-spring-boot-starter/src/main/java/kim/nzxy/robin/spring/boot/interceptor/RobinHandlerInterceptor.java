package kim.nzxy.robin.spring.boot.interceptor;

import kim.nzxy.robin.Robin;
import kim.nzxy.robin.annotations.RobinIgnore;
import kim.nzxy.robin.annotations.RobinTopic;
import kim.nzxy.robin.annotations.RobinTopicCollector;
import kim.nzxy.robin.config.RobinManagement;
import kim.nzxy.robin.util.RobinUtil;
import lombok.CustomLog;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lyun-chn
 * @since 2021/6/4
 */
@CustomLog
public class RobinHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        if (catchAble(handler)) {
            dine((HandlerMethod) handler);
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
            return !method.isAnnotationPresent(RobinIgnore.class)
                    && !method.getDeclaringClass().isAnnotationPresent(RobinIgnore.class);
        }
        return false;
    }

    /**
     * 寻找适配的策略, 并提交暂存区, 或直接执行
     */
    private void dine(HandlerMethod handler) {
        Map<String, String> topicMetadataMap = new HashMap<>(8);
        AnnotatedElementUtils.getMergedRepeatableAnnotations(handler.getMethod().getDeclaringClass(),
                        RobinTopic.class,
                        RobinTopicCollector.class)
                .forEach(it -> topicMetadataMap.put(it.value(), it.metadata()));
        AnnotatedElementUtils.getMergedRepeatableAnnotations(handler.getMethod(),
                        RobinTopic.class,
                        RobinTopicCollector.class)
                .forEach(it -> topicMetadataMap.put(it.value(), it.metadata()));
        // 空集合, 直接执行
        if (topicMetadataMap.isEmpty()) {
           Robin.hunger(topicMetadataMap);
           return;
        }
        // 任意一个metadata不为空, 则暂存, 否则直接执行, metadata转交给AOP解析
        if (topicMetadataMap.values().stream().anyMatch(RobinUtil::isNotEmpty)) {
            Robin.crop(topicMetadataMap);
            return;
        }
        Robin.hunger(topicMetadataMap);
    }
}
