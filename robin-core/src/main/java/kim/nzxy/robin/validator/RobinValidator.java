package kim.nzxy.robin.validator;

import kim.nzxy.robin.autoconfigure.RobinBasicStrategy;
import kim.nzxy.robin.autoconfigure.ValidatorConfig;
import kim.nzxy.robin.config.RobinManagement;

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
public interface RobinValidator {
    /**
     * 可能这不是一个很好的选择，但是目前看来我没有找到更好的方式，或许以后会的
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    @interface WithConfig {
        /**
         * @return 策略自动注入的前缀，方便快速读取相关配置
         */
        String key();
    }

    /**
     * 在Controller执行之前调用
     */
    void preHandle(String topic, String metadata, RobinBasicStrategy basicConfig, Object validatorConfig);

    /**
     * controller执行之后
     */
    default void postHandle() {
    }

    /**
     * 读取基础配置
     * @return 基础配置
     */
    default RobinBasicStrategy getBasicConfig() {
        return null;
    }

    /**
     * 读取配置并转为指定类型
     * 如果没有配置{@link WithConfig}注解，则返回null
     * @param type 配置类型
     * @return 指定类型的配置信息
     * @param <T> 类型
     * @see WithConfig
     */
    default <T> T getStrategyConfig(Class<T> type) {
        return RobinManagement.getStrategyConfig(this.getClass().getAnnotation(WithConfig.class).key(), type);
    }
}
