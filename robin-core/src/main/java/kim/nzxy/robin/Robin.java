package kim.nzxy.robin;

import kim.nzxy.robin.config.RobinManagement;
import kim.nzxy.robin.enums.RobinBuiltinErrEnum;
import kim.nzxy.robin.enums.RobinRuleEnum;
import kim.nzxy.robin.exception.RobinBuiltinException;
import kim.nzxy.robin.factory.RobinValidFactory;
import kim.nzxy.robin.validator.RobinValidator;
import lombok.val;

/**
 * 用户工具类
 *
 * @author xy
 * @since 2021/6/4
 */
public class Robin {
    /**
     * 逐个执行策略
     */
    public static void start() {
        val interceptor = RobinManagement.getRobinInterceptor();
        if (!interceptor.beforeValidate()) {
            return;
        }

        val includeRule = RobinManagement.getRobinProperties().getIncludeRule();
        try {
            for (RobinValidator validator : RobinValidFactory.getInvokeStrategy(includeRule)) {
                validator.execute();
            }
        } catch (RobinBuiltinException e) {
            if (!e.getError().equals(RobinBuiltinErrEnum.EXPECTED_USER_BREAK)) {
                throw e;
            }
        }
    }

    /**
     * 解除对某限制的封禁
     *
     * @param rule   如果为 null, 则解除所有策略对其的封禁, 建议不为 null, 否则效率会稍微降低
     * @param target 解除封禁的目标, 如IP地址, token, 用户 id 等
     */
    public static void unlock(RobinRuleEnum rule, String target) {
        RobinManagement.getCacheHandler().unlock(rule, target);
    }
}
