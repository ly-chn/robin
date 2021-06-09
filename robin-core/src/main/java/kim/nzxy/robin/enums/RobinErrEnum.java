package kim.nzxy.robin.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xy
 * @since 2021/6/4
 */
@AllArgsConstructor
@Getter
public enum RobinErrEnum implements RobinEnum {
    FREQUENT_IP_ACCESS(1001, "IP 访问频繁");
    private final int code;
    private final String message;
}
