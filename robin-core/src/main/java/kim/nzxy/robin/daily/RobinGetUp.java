package kim.nzxy.robin.daily;

import kim.nzxy.robin.autoconfigure.RobinEffort;
import kim.nzxy.robin.config.RobinManagement;
import kim.nzxy.robin.config.RobinMetadata;
import kim.nzxy.robin.enums.RobinExceptionEnum;
import kim.nzxy.robin.exception.RobinException;
import kim.nzxy.robin.factory.RobinEffortFactory;
import kim.nzxy.robin.factory.RobinMetadataFactory;
import kim.nzxy.robin.factory.RobinPostureFactory;
import kim.nzxy.robin.handler.RobinCacheHandler;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

/**
 * 工作周期
 *
 * @author xuyf
 * @since 2022/9/9 15:07
 */
@Slf4j
public class RobinGetUp {
    public static void preHandle() {
        log.debug("robin start");
        // 用户取消拦截
        val interceptor = RobinManagement.getRobinInterceptor();
        if (!interceptor.beforeCatch()) {
            return;
        }
        // 缓存
        RobinCacheHandler cacheHandler = RobinManagement.getCacheHandler();
        try {
            RobinEffortFactory.getGlobalValidatorTopic().forEach((topic, postureKey) -> {
                String metadata = RobinMetadataFactory.getMetadataHandler(topic).getMetadata();
                RobinEffort effort = RobinEffortFactory.getEffort(topic);
                RobinMetadata robinMetadata = new RobinMetadata(topic, metadata, effort.getBasic().getDigest());
                if (cacheHandler.locked(robinMetadata)) {
                    throw new RobinException.Verify(RobinExceptionEnum.Verify.MetadataHasLocked, robinMetadata);
                }
                log.debug("robin running, topic: {}, postureKey: {}, metadata: {}, effort: {}", topic, postureKey, metadata, effort);
                boolean preHandleSuccess = RobinPostureFactory.getInvokeStrategy(postureKey)
                        .preHandle(robinMetadata);
                if (!preHandleSuccess) {
                    cacheHandler.lock(robinMetadata, effort.getBasic().getLockDuration());
                }
                log.error("拒绝访问: {}", robinMetadata);
            });
        } catch (RobinException.Verify e) {
            if (interceptor.onCatch(e)) {
                throw e;
            }
        } finally {
            interceptor.beforeCatch();
        }
    }

    public static void postHandle() {

    }
}
