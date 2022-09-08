package kim.nzxy.robin.posture;

import kim.nzxy.robin.autoconfigure.RobinEffort;
import kim.nzxy.robin.autoconfigure.RobinEffortBasic;
import kim.nzxy.robin.factory.RobinEffortFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * robin策略器
 *
 * @author xy
 * @since 2021/6/4
 */
public interface RobinPosture {
    /**
     * 在Controller执行之前调用
     *
     * @param topic           主题
     * @param metadata        元数据
     * @param basicConfig     基础配置
     * @param validatorConfig 附加配置
     */
    void preHandle(String topic, String metadata, RobinEffortBasic basicConfig, Object validatorConfig);

    /**
     * controller执行之后
     *
     * @param topic           主题
     * @param metadata        元数据
     * @param basicConfig     基础配置
     * @param validatorConfig 附加配置
     */
    default void postHandle(String topic, String metadata, RobinEffortBasic basicConfig, Object validatorConfig) {
    }

    /**
     * 读取基础配置
     *
     * @return 基础配置
     */
    default RobinEffortBasic getBasicConfig() {
        return null;
    }

    /**
     * 读取配置并转为指定类型
     * 如果没有配置{@link RobinValidatorConfig}注解，则返回null
     *
     * @param type 配置类型
     * @param <T>  类型
     * @return 指定类型的配置信息
     * @see RobinValidatorConfig
     */
    default <T> T getStrategyConfig(Class<T> type) {
        return RobinEffortFactory.getStrategyConfig(this.getClass().getAnnotation(RobinValidatorConfig.class).key(), type);
    }

    /**
     * 可能这不是一个很好的选择，但是目前看来我没有找到更好的方式，或许以后会的
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    @interface RobinValidatorConfig {
        /**
         * @return 策略自动注入的前缀，方便快速读取相关配置
         */
        String key();

        /**
         * @return 对应的配置类, 默认没有对应配置类, 会抛出异常
         */
        Class<RobinEffort> configClass() default RobinEffort.class;
    }
}
