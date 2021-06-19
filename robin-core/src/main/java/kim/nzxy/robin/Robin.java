package kim.nzxy.robin;

import kim.nzxy.robin.config.RobinManagement;
import kim.nzxy.robin.enums.RobinBuiltinErrEnum;
import kim.nzxy.robin.exception.RobinBuiltinException;
import kim.nzxy.robin.factory.RobinValidFactory;
import kim.nzxy.robin.validator.RobinValidator;
import lombok.val;

/**
 * @author xy
 * @since 2021/6/4
 */
public class Robin {
    /**
     * 逐个执行策略
     */
    public static void execute() {
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
}
