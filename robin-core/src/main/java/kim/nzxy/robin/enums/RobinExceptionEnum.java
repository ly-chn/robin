package kim.nzxy.robin.enums;

/**
 * 内置异常类型
 *
 * @author xuyf
 * @since 2022/8/25 17:37
 */
public interface RobinExceptionEnum {
    enum Panic implements RobinExceptionEnum {
        /**
         * 摘要算法初始化异常
         */
        DigestUtilInitError,
        ;
    }
    enum Validate implements RobinExceptionEnum {
        /**
         * 参数校验失败
         */
        ParameterValidateError,
        ;
    }
}
