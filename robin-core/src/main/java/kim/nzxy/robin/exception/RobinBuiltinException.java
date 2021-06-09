package kim.nzxy.robin.exception;

import kim.nzxy.robin.enums.RobinBuiltinErrEnum;
import lombok.Getter;

/**
 * @author xy
 * @since 2021/6/4
 */
@Getter
public class RobinBuiltinException extends RuntimeException {
    /**
     * 错误类型
     */
    private final RobinBuiltinErrEnum error;
    private final String message;

    public RobinBuiltinException(RobinBuiltinErrEnum error) {
        this.error = error;
        this.message = error.getMessage();
    }
}
