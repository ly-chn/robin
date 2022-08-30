package kim.nzxy.robin.factory;

import kim.nzxy.robin.config.RobinManagement;
import kim.nzxy.robin.enums.RobinBuiltinErrEnum;
import kim.nzxy.robin.enums.RobinExceptionEnum;
import kim.nzxy.robin.enums.RobinRuleEnum;
import kim.nzxy.robin.exception.RobinBuiltinException;
import kim.nzxy.robin.exception.RobinException;
import kim.nzxy.robin.util.RobinUtil;
import kim.nzxy.robin.validator.ContinuousVisitValidator;
import kim.nzxy.robin.validator.FrequentIpAccessValidator;
import kim.nzxy.robin.validator.IpBlocklistValidator;
import kim.nzxy.robin.validator.RobinValidator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;

import java.util.*;

/**
 * robin 验证策略工厂
 *
 * @author xy
 * @since 2021/6/4
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RobinValidFactory {
    /**
     * 内置策略
     */
    private static final Map<RobinRuleEnum, RobinValidator> INVOKE_STRATEGY_MAP;
    /**
     * 用户自定义策略
     */
    private static final Set<RobinValidator> INVOKE_STRATEGY_SET = new HashSet<>();
    /**
     * 用于缓存所有用到的策略
     */
    private static volatile List<RobinValidator> invokeStrategyList;

    static {
        INVOKE_STRATEGY_MAP = new HashMap<>();
        INVOKE_STRATEGY_MAP.put(RobinRuleEnum.FREQUENT_IP_ACCESS, new FrequentIpAccessValidator());
        INVOKE_STRATEGY_MAP.put(RobinRuleEnum.BLOCKLIST_IP_ADDRESS, new IpBlocklistValidator());
        INVOKE_STRATEGY_MAP.put(RobinRuleEnum.CONTINUOUS_VISIT, new ContinuousVisitValidator());
    }


    /**
     * @param includeRule 启用的内置策略
     * @return 所有策略(用户定义+内置)
     */
    public static List<RobinValidator> getInvokeStrategy(List<RobinRuleEnum> includeRule) {
        if (invokeStrategyList == null) {
            invokeStrategyList = new ArrayList<>();
            for (RobinRuleEnum robinRuleEnum : includeRule) {
                if (INVOKE_STRATEGY_MAP.get(robinRuleEnum) != null) {
                    invokeStrategyList.add(INVOKE_STRATEGY_MAP.get(robinRuleEnum));
                }
            }
            invokeStrategyList.addAll(INVOKE_STRATEGY_SET);
            invokeStrategyList.sort(Comparator.comparingInt(o -> {
                if (RobinUtil.getMapKey(INVOKE_STRATEGY_MAP, o) == null) {
                    return o.getOrder();
                }
                val index = RobinManagement.getRobinProperties().getIncludeRule().indexOf(RobinUtil.getMapKey(INVOKE_STRATEGY_MAP, o));
                if (index == -1) {
                    throw new RobinException.Panic(RobinExceptionEnum.Panic.ModeNotImplementedYet);
                }
                return index * 100;
            }));
        }
        return invokeStrategyList;
    }


    /**
     * 添加用户自定义验证策略
     *
     * @param handler 自定义策略
     */
    public static void register(RobinValidator handler) {
        INVOKE_STRATEGY_SET.add(handler);
    }
}
