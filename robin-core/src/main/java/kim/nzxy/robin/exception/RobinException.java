package kim.nzxy.robin.exception;

import kim.nzxy.robin.config.RobinMetadata;
import kim.nzxy.robin.enums.RobinExceptionEnum;
import lombok.Getter;

/**
 * @author xy
 * @since 2021/6/4
 */
@Getter
public class RobinException extends RuntimeException {
    /**
     * 异常类型
     */
    private final RobinExceptionEnum error;
    /**
     * 异常附加信息
     */
    private final RobinMetadata target;

    private RobinException(RobinExceptionEnum error, RobinMetadata target) {
        super(error.toString());
        this.error = error;
        this.target = target;
    }

    /**
     * 严重异常，将导致robin无法正常运行
     */
    public static class Panic extends RobinException {
        public Panic(RobinExceptionEnum.Panic error) {
            super(error, null);
        }
    }
    /**
     * 校验异常, 校验过程中的异常
     */
    public static class Verify extends RobinException {
        public Verify(RobinExceptionEnum.Verify error, RobinMetadata target) {
            super(error, target);
        }
    }
}
