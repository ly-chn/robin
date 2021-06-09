package kim.nzxy.robin.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支持的所有规则
 *
 * @author xy
 * @since 2021/6/4
 */
@AllArgsConstructor
@Getter
public enum RobinRuleEnum implements RobinEnum {


    /**
     * IP 访问频率限制
     */
    FREQUENT_IP_ACCESS(1001, "IP 访问频繁"),
    /**
     * IP 黑名单拦截
     */
    BLACKLIST_IP_ADDRESS(1002, "IP 已被列入黑名单"),
    ;

    /**
     * [1k-2k) -> ip相关异常
     */
    private final int code;
    private final String message;
}
