package kim.nzxy.robin.daily;

import kim.nzxy.robin.annotations.RobinTopic;
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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

/**
 * 工作周期
 *
 * @author lyun-chn
 * @since 2022/9/9 15:07
 */
@Slf4j
public class RobinGetUp {
    public static void getUp(Method method) {
        String[] extraTopic = getExtraTopic(method);
        log.debug("robin pre handle, extra topic: {}", Arrays.toString(extraTopic));
        // 用户取消拦截
        RobinInterceptor interceptor = RobinManagement.getRobinInterceptor();
        if (!interceptor.beforeCatch()) {
            return;
        }
        // 缓存
        RobinCacheHandler cacheHandler = RobinManagement.getCacheHandler();
        RobinEffortFactory.getValidatorTopic(extraTopic).forEach((topic, postureKey) -> {
            // 配置信息
            RobinEffort effort = RobinEffortFactory.getEffort(topic);
            // 包装元数据
            RobinPosture posture = RobinPostureFactory.getInvokeStrategy(postureKey);
            RobinMetadata robinMetadata = new RobinMetadata(topic,
                    RobinMetadataFactory.getMetadataHandler(topic).getMetadata(),
                    effort.getBasic().getDigest());
            if (cacheHandler.locked(robinMetadata)) {
                throw new RobinException.Verify(RobinExceptionEnum.Verify.MetadataHasLocked, robinMetadata);
            }
            log.debug("robin running, postureKey: {}, metadata: {}, effort: {}", postureKey, robinMetadata, effort);
            boolean preHandleSuccess;
            try {
                // 执行逻辑
                preHandleSuccess = posture.handler(robinMetadata);
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

    /**
     * 获取类/方法上添加的topic
     */
    private static String[] getExtraTopic(Method method) {
        RobinTopic[] methodTopics = method.getAnnotationsByType(RobinTopic.class);
        RobinTopic[] classTopics = method.getDeclaringClass().getAnnotationsByType(RobinTopic.class);
        int methodTopicsLength = methodTopics.length;
        String[] result = new String[methodTopicsLength + classTopics.length];
        for (int i = 0; i < methodTopicsLength; i++) {
            result[i] = methodTopics[i].value();
        }
        for (int i = 0; i < classTopics.length; i++) {
            result[i] = classTopics[methodTopicsLength + i].value();
        }
        return result;
    }


}
