package kim.nzxy.robin;

import kim.nzxy.robin.config.RobinManagement;
import kim.nzxy.robin.config.RobinMetaData;
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
     * @param metaData 元数据，为null表示解除所有封禁
     */
    public static void unlock(RobinMetaData metaData) {
        RobinManagement.getCacheHandler().unlock(metaData);
    }
}
