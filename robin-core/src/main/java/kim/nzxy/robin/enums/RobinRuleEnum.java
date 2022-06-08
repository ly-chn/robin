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
     * 忘了干嘛的了, 且先留着吧
     */
    FREQUENT_ACCESS(1004, "访问频繁"),
    /**
     * IP 黑名单拦截
     */
    BLOCKLIST_IP_ADDRESS(1002, "IP 已被列入黑名单"),
    /**
     * IP 访问频繁, 这里的访问频繁是指在连续的时间窗口内均有访问, 比如一般人不会在每分钟都无规律的访问连续一天
     */
    CONTINUOUS_VISIT(1003, "IP 持续访问"),
    /**
     * 算是{@link #FREQUENT_IP_ACCESS}的简单优化版本, 将根据IP/UA等多重标志, 防止同一个网络下多个用户进行访问, 但是防范的目标是 IP
     * @see #FREQUENT_IP_ACCESS
     */
    FREQUENT_BETTER_IP_ACCESS(1001, "IP 访问频繁");


    /**
     * [1k-2k) -> ip相关异常
     */
    private final int code;
    private final String message;
}
