package kim.nzxy.robin.enums;

import kim.nzxy.robin.posture.RobinPosture;

/**
 * 内置异常类型
 *
 * @author lyun-chn
 * @since 2022/8/25 17:37
 */
public interface RobinExceptionEnum {
    /**
     * 导致robin无法正常运行的异常，此类异常类似SQL异常等，用户无法自行处理的异常
     */
    enum Panic implements RobinExceptionEnum {
        /**
         * 摘要算法初始化异常
         */
        DigestUtilInitError,
        /**
         * 未找到对应的元数据处理器
         */
        MetadataHandlerMissing,
        /**
         * 缺乏PostureConfig注解
         *
         * @see RobinPosture
         * @see RobinPosture.PostureConfig
         */
        AnnotationWithConfigMissing,
        /**
         * 对应主题未经过配置
         */
        TopicIsNotConfigured,
        /**
         * 配置参数校验异常
         */
        ConfigParamVerifyFailed,
    }

    /**
     * 校验异常，此类异常由用户行为触发
     */
    enum Verify implements RobinExceptionEnum {
        /**
         * 元数据已被锁定
         */
        MetadataHasLocked,
        /**
         * 校验未通过
         */
        VerifyFailed,
        /**
         * 验证器发生异常
         */
        RobinPostureDropTheBall;
    }
}
