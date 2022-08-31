package kim.nzxy.robin.factory;

import kim.nzxy.robin.config.RobinManagement;
import kim.nzxy.robin.enums.RobinExceptionEnum;
import kim.nzxy.robin.enums.RobinRuleEnum;
import kim.nzxy.robin.exception.RobinException;
import kim.nzxy.robin.util.RobinUtil;
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
     * 校验策略
     */
    private static final Map<String, RobinValidator> INVOKE_STRATEGY_MAP = new HashMap<>();

    /**
     * 用于缓存所有用到的策略
     */
    private static volatile List<RobinValidator> invokeStrategyList;


    /**
     * @param includeRule 启用的内置策略
     * @return 所有策略(用户定义+内置)
     */
    public static List<RobinValidator> getInvokeStrategy(List<RobinRuleEnum> includeRule) {
        return null;
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
