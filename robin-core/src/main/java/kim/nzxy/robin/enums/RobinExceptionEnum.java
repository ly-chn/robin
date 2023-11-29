package kim.nzxy.robin.enums;

import kim.nzxy.robin.posture.RobinPosture;

/**
 * 内置异常类型
 *
 * @author ly-chn
 * @since 2022/8/25 17:37
 */
public interface RobinExceptionEnum {
    /**
     * 导致robin无法正常运行的异常，此类异常类似SQL异常等，用户无法自行处理的异常
     */
    enum Panic implements RobinExceptionEnum {
        /**
         * 字符串编码异常
         */
        StrCodecEncodeError,
        /**
         * 字符串解码异常
         */
        StrCodecDecodeError,
        /**
         * 不可逆的字符串编码器
         */
        StrCodecIrreversible,
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
         * 无法获取到对应的元数据
         */
        MetadataIsEmpty,
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
