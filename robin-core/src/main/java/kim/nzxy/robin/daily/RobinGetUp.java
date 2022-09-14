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
import kim.nzxy.robin.posture.RobinPosture;
import lombok.extern.slf4j.Slf4j;

/**
 * 工作周期
 *
 * @author lyun-chn
 * @since 2022/9/9 15:07
 */
@Slf4j
public class RobinGetUp {
    public static void preHandle() {
        log.debug("robin pre handle");
        // 用户取消拦截
        RobinInterceptor interceptor = RobinManagement.getRobinInterceptor();
        if (!interceptor.beforeCatch()) {
            return;
        }
        // 缓存
        RobinCacheHandler cacheHandler = RobinManagement.getCacheHandler();
        RobinEffortFactory.getGlobalValidatorTopic().forEach((topic, postureKey) -> {
            // 配置信息
            RobinEffort effort = RobinEffortFactory.getEffort(topic);
            // 包装元数据
            RobinPosture posture = RobinPostureFactory.getInvokeStrategy(postureKey);
            RobinMetadata robinMetadata = getMetadata(topic, posture, effort.getBasic().getDigest());
            if (cacheHandler.locked(robinMetadata)) {
                throw new RobinException.Verify(RobinExceptionEnum.Verify.MetadataHasLocked, robinMetadata);
            }
            log.debug("robin running, postureKey: {}, metadata: {}, effort: {}", postureKey, robinMetadata, effort);
            boolean preHandleSuccess;
            try {
                // 执行逻辑
                preHandleSuccess = posture.preHandle(robinMetadata);
            } catch (Exception e) {
                log.error("posture drop the ball", e);
                throw new RobinException.Verify(RobinExceptionEnum.Verify.RobinPostureDropTheBall, robinMetadata);
            }
            // 判断执行结果
            if (!preHandleSuccess && interceptor.onCatch(robinMetadata)) {
                cacheHandler.lock(robinMetadata, effort.getBasic().getLockDuration());
                throw new RobinException.Verify(RobinExceptionEnum.Verify.VerifyFailed, robinMetadata);
            }
        });
    }

    public static void postHandle() {
        log.debug("robin post handle");
        RobinEffortFactory.getGlobalValidatorTopic().forEach((topic, postureKey) -> {
            // 配置信息
            RobinEffort effort = RobinEffortFactory.getEffort(topic);
            // 包装元数据
            RobinPosture posture = RobinPostureFactory.getInvokeStrategy(postureKey);
            RobinMetadata robinMetadata = getMetadata(topic, posture, effort.getBasic().getDigest());
            log.debug("robin post handle run, postureKey: {}, metadata: {}, effort: {}", postureKey, robinMetadata, effort);
            posture.postHandle(robinMetadata);
        });
        cleanMetadata();
    }

    /**
     * 包装元数据
     */
    private static RobinMetadata getMetadata(String topic, RobinPosture posture, Boolean digest) {
        // log.error("判断重写: {}", Arrays.toString(posture.getClass().getDeclaredMethods()));
        // todo: 判断是否需要缓存metadata, 感觉意义不大, 后续处理, 目测很少有需要后续清理工作的posture
        // 元数据
        String metadata = RobinMetadataFactory.getMetadataHandler(topic).getMetadata();
        return new RobinMetadata(topic, metadata, digest);
    }

    /**
     * 元数据清理工作
     */
    private static void cleanMetadata() {

    }
}
