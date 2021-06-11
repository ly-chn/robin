package kim.nzxy.robin.factory;

import kim.nzxy.robin.config.RobinManagement;
import kim.nzxy.robin.enums.RobinBuiltinErrEnum;
import kim.nzxy.robin.enums.RobinRuleEnum;
import kim.nzxy.robin.exception.RobinBuiltinException;
import kim.nzxy.robin.validator.RobinValidator;
import kim.nzxy.robin.validator.FrequentIpAccessValidator;
import kim.nzxy.robin.validator.IpBlacklistValidator;
import kim.nzxy.robin.util.RobinUtil;
import lombok.val;

import java.util.*;

/**
 * robin 验证策略工厂
 * todo: 如何更好的注册工厂
 *
 * @author xy
 * @since 2021/6/4
 */
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
        INVOKE_STRATEGY_MAP.put(RobinRuleEnum.BLACKLIST_IP_ADDRESS, new IpBlacklistValidator());
    }


    public static List<RobinValidator> getInvokeStrategy(List<RobinRuleEnum> includeRule) {
        if (invokeStrategyList == null) {
            invokeStrategyList = new ArrayList<>();
            for (RobinRuleEnum robinRuleEnum : includeRule) {
                invokeStrategyList.add(INVOKE_STRATEGY_MAP.get(robinRuleEnum));
            }
            invokeStrategyList.addAll(INVOKE_STRATEGY_SET);
            invokeStrategyList.sort(Comparator.comparingInt(o -> {
                if (RobinUtil.getMapKey(INVOKE_STRATEGY_MAP, o) == null) {
                    return o.getOrder();
                }
                val index = RobinManagement.getRobinProperties().getIncludeRule().indexOf(RobinUtil.getMapKey(INVOKE_STRATEGY_MAP, o));
                if (index == -1) {
                    throw new RobinBuiltinException(RobinBuiltinErrEnum.MODE_NOT_IMPLEMENTED_YET);
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
