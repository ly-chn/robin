package kim.nzxy.robin.exception;

import kim.nzxy.robin.enums.RobinExceptionEnum;
import kim.nzxy.robin.enums.RobinRuleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xy
 * @since 2021/6/4
 */
@AllArgsConstructor
@Getter
public class RobinException extends RuntimeException {
    private RobinRuleEnum error;
    private String target;

    public RobinException(RobinExceptionEnum error, String target) {
    }

    /**
     * 严重异常，将导致robin无法正常运行
     */
    public static class Panic extends RobinException {
        public Panic(RobinExceptionEnum error, String target) {
            super(error, target);
        }
    }
}
