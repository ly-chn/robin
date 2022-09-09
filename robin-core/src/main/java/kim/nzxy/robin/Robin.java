package kim.nzxy.robin;

import kim.nzxy.robin.autoconfigure.RobinEffort;
import kim.nzxy.robin.config.RobinManagement;
import kim.nzxy.robin.config.RobinMetadata;
import kim.nzxy.robin.factory.RobinEffortFactory;
import kim.nzxy.robin.factory.RobinMetadataFactory;
import kim.nzxy.robin.factory.RobinPostureFactory;
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
        RobinEffortFactory.getGlobalValidatorTopic().forEach((topic, postureKey) -> {
            String metadata = RobinMetadataFactory.getMetadataHandler(topic).getMetadata();
            RobinEffort effort = RobinEffortFactory.getEffort(topic);
            RobinMetadata robinMetadata = new RobinMetadata(topic, metadata, effort.getBasic().getDigest());
            log.debug("robin running, topic: {}, postureKey: {}, metadata: {}, effort: {}", topic, postureKey, metadata, effort);
            boolean preHandleSuccess = RobinPostureFactory.getInvokeStrategy(postureKey)
                    .preHandle(robinMetadata);
            if (!preHandleSuccess) {
                RobinManagement.getCacheHandler().lock(robinMetadata, effort.getBasic().getLockDuration());
            }
            log.error("拒绝访问: {}", robinMetadata);
        });
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
