package kim.nzxy.robin;

import kim.nzxy.robin.config.RobinManagement;
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
        val includeRule = RobinManagement.getRobinProperties().getIncludeRule();
        for (RobinValidator validator : RobinValidFactory.getInvokeStrategy(includeRule)) {
            validator.execute();
        }
    }
}
