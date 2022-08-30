package kim.nzxy.robin.enums;

/**
 * 内置异常类型
 *
 * @author xuyf
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
        ;
    }

    /**
     * 校验异常，此类异常由用户行为触发
     */
    enum Verify implements RobinExceptionEnum {
        /**
         * 访问频率过高
         */
        FrequentAccess,

        ;
    }
}
