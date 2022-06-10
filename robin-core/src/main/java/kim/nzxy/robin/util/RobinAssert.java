package kim.nzxy.robin.util;

import kim.nzxy.robin.config.RobinManagement;
import kim.nzxy.robin.enums.RobinBuiltinErrEnum;
import kim.nzxy.robin.enums.RobinRuleEnum;
import kim.nzxy.robin.exception.RobinBuiltinException;
import kim.nzxy.robin.exception.RobinException;

/**
 * 断言类
 *
 * @author xy
 * @since 2021/6/4
 */
public class RobinAssert {
    /**
     * 断言是否锁定
     *
     * @see #assertRobinException(boolean, kim.nzxy.robin.enums.RobinRuleEnum, String)
     */
    public static void assertLocked(RobinRuleEnum ruleEnum, String target) {
        assertRobinException(RobinManagement.getCacheHandler().lock(ruleEnum, target), ruleEnum, target);
    }

    /**
     * @see #assertRobinException(boolean, RobinRuleEnum, String, Runnable)
     */
    public static void assertRobinException(boolean expression, RobinRuleEnum ruleEnum, String target) {
        assertRobinException(expression, ruleEnum, target, null);
    }

    /**
     * todo: lock几乎每次都有, 回头改成固定的
     *
     * @param expression  if true, throwing an {@link RobinException}
     * @param beforeThrow 抛出异常前的回调
     */
    public static void assertRobinException(boolean expression, RobinRuleEnum ruleEnum, String target, Runnable beforeThrow) {
        if (expression) {
            RobinException exception = new RobinException(ruleEnum, target);
            if (beforeThrow != null) {
                beforeThrow.run();
            }
            if (RobinManagement.getRobinInterceptor().onCatch(exception)) {
                throw exception;
            } else {
                throw new RobinBuiltinException(RobinBuiltinErrEnum.EXPECTED_USER_BREAK);
            }
        }
    }
}
