package kim.nzxy.robin.exception;

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
}
