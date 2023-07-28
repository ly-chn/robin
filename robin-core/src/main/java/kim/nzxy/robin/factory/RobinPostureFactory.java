package kim.nzxy.robin.factory;

import kim.nzxy.robin.enums.RobinExceptionEnum;
import kim.nzxy.robin.exception.RobinException;
import kim.nzxy.robin.posture.RobinPosture;
import kim.nzxy.robin.posture.action.BucketPosture;
import kim.nzxy.robin.posture.action.SustainVisitPosture;
import lombok.AccessLevel;
import lombok.CustomLog;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * robin 验证策略工厂
 *
 * @author lyun-chn
 * @since 2021/6/4
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@CustomLog
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
        if (!validator.getClass().isAnnotationPresent(RobinPosture.PostureConfig.class)) {
            log.error("register validator handler error: " + validator.getClass() + " without annotation: " + RobinPosture.PostureConfig.class);
        }
        RobinPosture.PostureConfig annotation = validator.getClass().getAnnotation(RobinPosture.PostureConfig.class);
        if (annotation == null) {
            throw new RobinException.Panic(RobinExceptionEnum.Panic.AnnotationWithConfigMissing);
        }
        if (log.isDebugEnabled()) {
            log.debug("register validator：" + annotation.key() + " with class: " + validator.getClass());
        }
        INVOKE_STRATEGY_MAP.put(annotation.key(), validator);
    }
}
