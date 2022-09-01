package kim.nzxy.robin.factory;

import kim.nzxy.robin.validator.RobinValidator;
import kim.nzxy.robin.validator.bucket.BucketValidator;
import kim.nzxy.robin.validator.sutain.visit.SustainVisitValidator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * robin 验证策略工厂
 *
 * @author xy
 * @since 2021/6/4
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class RobinValidFactory {
    /**
     * 校验策略
     */
    private static final Map<String, RobinValidator> INVOKE_STRATEGY_MAP = new HashMap<>();

    static {
        register(new SustainVisitValidator());
        register(new BucketValidator());
    }

    /**
     * @return 所有策略(用户定义 + 内置)
     */
    public static List<RobinValidator> getInvokeStrategy() {
        return null;
    }

    /**
     * 添加用户自定义验证策略
     *
     * @param validator 自定义策略
     */
    public static void register(RobinValidator validator) {
        if (log.isDebugEnabled()) {
            log.debug("register validator：{}", validator.getClass());
        }
        if (!validator.getClass().isAnnotationPresent(RobinValidator.WithConfig.class)) {
            log.error("register validator handler error: {} without annotation: {}", validator.getClass(), RobinValidator.WithConfig.class);
        }
        INVOKE_STRATEGY_MAP.put(validator.getClass().getAnnotation(RobinValidator.WithConfig.class).key(), validator);
    }
}
