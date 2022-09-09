package kim.nzxy.robin.posture;

import kim.nzxy.robin.autoconfigure.RobinEffortBasic;
import kim.nzxy.robin.config.RobinMetadata;
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
     * @param robinMetadata 元数据
     * @return true表示校验通过
     */
    boolean preHandle(RobinMetadata robinMetadata);

    /**
     * controller执行之后
     *
     * @param robinMetadata 元数据
     */
    default void postHandle(RobinMetadata robinMetadata) {
    }

    /**
     * 读取基础配置
     *
     * @return 基础配置
     */
    default RobinEffortBasic getBasicEffort() {
        return null;
    }

    /**
     * 读取配置并转为指定类型
     * 如果没有配置{@link PostureConfig}注解，则返回null
     *
     * @param topic 主题
     * @param <T>   类型
     * @return 指定类型的配置信息
     * @see PostureConfig
     */
    default <T> T getExpandEffort(String topic) {
        return RobinEffortFactory.getExpandConfig(topic);
    }


    /**
     * 可能这不是一个很好的选择，但是目前看来我没有找到更好的方式，或许以后会的
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    @interface PostureConfig {
        /**
         * @return 策略自动注入的前缀，方便快速读取相关配置
         */
        String key();
    }
}
