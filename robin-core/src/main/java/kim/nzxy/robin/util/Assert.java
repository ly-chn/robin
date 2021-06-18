package kim.nzxy.robin.util;

import kim.nzxy.robin.config.RobinManagement;
import kim.nzxy.robin.enums.RobinRuleEnum;
import kim.nzxy.robin.exception.RobinException;

/**
 * 断言类
 *
 * @author xy
 * @since 2021/6/4
 */
public class Assert {
    /**
     * @see #assertRobinException(boolean, RobinRuleEnum, String, Runnable)
     */
    public static void assertRobinException(boolean expression, RobinRuleEnum ruleEnum, String target) {
        assertRobinException(expression, ruleEnum, target, null);
    }

    /**
     * @param expression  if true, throwing an {@link RobinException}
     * @param beforeThrow 抛出异常前的回调
     */
    public static void assertRobinException(boolean expression, RobinRuleEnum ruleEnum, String target, Runnable beforeThrow) {
        if (expression) {
            RobinException exception = new RobinException(ruleEnum, target);
            if (RobinManagement.getRobinInterceptor().onCache(exception)) {
                throw exception;
            }
            if (beforeThrow != null) {
                beforeThrow.run();
            }
            throw exception;
        }
    }
}
