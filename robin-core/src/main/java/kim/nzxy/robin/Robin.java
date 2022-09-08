package kim.nzxy.robin;

import kim.nzxy.robin.config.RobinManagement;
import kim.nzxy.robin.config.RobinMetadata;
import kim.nzxy.robin.factory.RobinValidFactory;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

/**
 * 用户工具类
 *
 * @author xy
 * @since 2021/6/4
 */
@Slf4j
public class Robin {
    /**
     * 逐个执行策略
     */
    public static void start() {
        log.debug("robin start");
        val interceptor = RobinManagement.getRobinInterceptor();
        if (!interceptor.beforeValidate()) {
            return;
        }
        for (String topic : RobinManagement.getGlobalValidatorTopic()) {
            // RobinValidFactory.getInvokeStrategy();
        }
        System.out.println();

        // val includeRule = RobinManagement.getRobinProperties().getIncludeRule();
        // try {
        //     for (RobinValidator validator : RobinValidFactory.getInvokeStrategy()) {
        //         validator.preHandle();
        //     }
        // } catch (RobinBuiltinException e) {
        //     if (!e.getError().equals(RobinBuiltinErrEnum.EXPECTED_USER_BREAK)) {
        //         throw e;
        //     }
        // }
    }

    /**
     * 解除对某限制的封禁
     *
     * @param metadata 元数据，为null表示解除所有封禁
     */
    public static void unlock(RobinMetadata metadata) {
        RobinManagement.getCacheHandler().unlock(metadata);
    }
}
