package kim.nzxy.robin;

import kim.nzxy.robin.config.RobinManagement;
import kim.nzxy.robin.config.RobinMetadata;
import kim.nzxy.robin.factory.RobinValidFactory;
import kim.nzxy.robin.factory.RobinValidatorConfigFactory;
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
        for (String topic : RobinValidatorConfigFactory.getGlobalValidatorTopic()) {
            // RobinValidFactory.getInvokeStrategy();
        }
        System.out.println();
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
