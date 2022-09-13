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
import kim.nzxy.robin.interceptor.RobinInterceptor;
import lombok.extern.slf4j.Slf4j;

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
        RobinInterceptor interceptor = RobinManagement.getRobinInterceptor();
        if (!interceptor.beforeCatch()) {
            return;
        }
        // 缓存
        RobinCacheHandler cacheHandler = RobinManagement.getCacheHandler();
        RobinEffortFactory.getGlobalValidatorTopic().forEach((topic, postureKey) -> {
            // 元数据
            String metadata = RobinMetadataFactory.getMetadataHandler(topic).getMetadata();
            // 配置信息
            RobinEffort effort = RobinEffortFactory.getEffort(topic);
            // 包装元数据
            RobinMetadata robinMetadata = new RobinMetadata(topic, metadata, effort.getBasic().getDigest());
            if (cacheHandler.locked(robinMetadata)) {
                throw new RobinException.Verify(RobinExceptionEnum.Verify.MetadataHasLocked, robinMetadata);
            }
            log.debug("robin running, topic: {}, postureKey: {}, metadata: {}, effort: {}", topic, postureKey, metadata, effort);
            try {
                // 执行逻辑
                boolean preHandleSuccess = RobinPostureFactory.getInvokeStrategy(postureKey)
                        .preHandle(robinMetadata);
                // 判断执行结果
                if (!preHandleSuccess && interceptor.onCatch(robinMetadata)) {
                    cacheHandler.lock(robinMetadata, effort.getBasic().getLockDuration());
                    throw new RobinException.Verify(RobinExceptionEnum.Verify.VerifyFailed, robinMetadata);
                }
            } catch (Exception e) {
                log.error("posture drop the ball", e);
                throw new RobinException.Verify(RobinExceptionEnum.Verify.RobinPostureDropTheBall, robinMetadata);
            }
        });
    }

    public static void postHandle() {

    }
}
