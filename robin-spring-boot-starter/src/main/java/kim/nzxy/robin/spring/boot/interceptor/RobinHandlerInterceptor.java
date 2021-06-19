package kim.nzxy.robin.spring.boot.interceptor;

import kim.nzxy.robin.Robin;
import kim.nzxy.robin.config.RobinManagement;
import kim.nzxy.robin.enums.RobinModeEnum;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author xy
 * @since 2021/6/4
 */
@Slf4j
public class RobinHandlerInterceptor implements HandlerInterceptor {
    @Getter
    private static final PathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (cacheAble(request)) {
            Robin.start();
        }
        return true;
    }

    public boolean cacheAble(HttpServletRequest request) {
        val resource = RobinManagement.getRobinProperties().getResource();
        if (resource.getMode() == RobinModeEnum.DISABLED) {
            return false;
        }
        if (resource.getMode() == RobinModeEnum.GLOBAL) {
            return true;
        }
        return matcher(resource.getIncludePatterns(), resource.getExcludePatterns(), request.getRequestURI());
    }

    /**
     * 路由匹配, 使用[AntPathMatcher]
     *
     * @return 为 true 表示匹配
     */
    private static boolean matcher(List<String> patterns, List<String> excludePatterns, String target) {
        for (String excludePattern : excludePatterns) {
            if (pathMatcher.match(excludePattern, target)) {
                return false;
            }
        }
        for (String pattern : patterns) {
            if (pathMatcher.match(pattern, target)) {
                return true;
            }
        }
        return false;
    }
}
