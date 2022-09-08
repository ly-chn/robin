package kim.nzxy.robin.factory;

import kim.nzxy.robin.enums.RobinExceptionEnum;
import kim.nzxy.robin.exception.RobinException;
import kim.nzxy.robin.posture.RobinPosture;
import kim.nzxy.robin.posture.bucket.BucketPosture;
import kim.nzxy.robin.posture.sutain.visit.SustainVisitPosture;
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
public class RobinPostureFactory {
    /**
     * 校验策略
     */
    private static final Map<String, RobinPosture> INVOKE_STRATEGY_MAP = new HashMap<>();

    static {
        register(new SustainVisitPosture());
        register(new BucketPosture());
    }

    /**
     * @return 所有策略(用户定义 + 内置)
     */
    public static RobinPosture getInvokeStrategy(String key) {
        return INVOKE_STRATEGY_MAP.get(key);
    }

    /**
     * @return 所有策略(用户定义 + 内置)
     */
    public static List<RobinPosture> getGlobalStrategy() {
        return null;
    }

    /**
     * 添加用户自定义验证策略
     *
     * @param validator 自定义策略
     */
    public static void register(RobinPosture validator) {
        if (log.isDebugEnabled()) {
            log.debug("register validator：{}", validator.getClass());
        }
        if (!validator.getClass().isAnnotationPresent(RobinPosture.PostureConfig.class)) {
            log.error("register validator handler error: {} without annotation: {}", validator.getClass(), RobinPosture.PostureConfig.class);
        }
        RobinPosture.PostureConfig annotation = validator.getClass().getAnnotation(RobinPosture.PostureConfig.class);
        if (annotation == null) {
            throw new RobinException.Panic(RobinExceptionEnum.Panic.AnnotationWithConfigMissing);
        }
        INVOKE_STRATEGY_MAP.put(annotation.key(), validator);
    }
}
