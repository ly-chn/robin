package kim.nzxy.robin.exception;

import kim.nzxy.robin.config.RobinMetaData;
import kim.nzxy.robin.enums.RobinExceptionEnum;
import kim.nzxy.robin.enums.RobinRuleEnum;
import lombok.AllArgsConstructor;
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
    private final RobinMetaData target;

    private RobinException(RobinExceptionEnum error, RobinMetaData target) {
        this.error = error;
        this.target = target;
    }

    /**
     * 严重异常，将导致robin无法正常运行
     */
    public static class Panic extends RobinException {
        public Panic(RobinExceptionEnum error) {
            super(error, null);
        }
    }
    /**
     * 严重异常，将导致robin无法正常运行
     */
    public static class Verify extends RobinException {
        public Verify(RobinExceptionEnum error, RobinMetaData target) {
            super(error, target);
        }
    }
}
