package kim.nzxy.robin.daily;

import kim.nzxy.robin.autoconfigure.RobinEffortBasic;
import kim.nzxy.robin.config.RobinManagement;
import kim.nzxy.robin.metadata.RobinMetadata;
import kim.nzxy.robin.enums.RobinExceptionEnum;
import kim.nzxy.robin.exception.RobinException;
import kim.nzxy.robin.factory.RobinEffortFactory;
import kim.nzxy.robin.factory.RobinMetadataFactory;
import kim.nzxy.robin.factory.RobinPostureFactory;
import kim.nzxy.robin.handler.RobinCacheHandler;
import kim.nzxy.robin.posture.RobinPosture;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 工作周期
 *
 * @author ly-chn
 * @since 2022/9/9 15:07
 */
@Slf4j
public class RobinGetUp {
    public static void getUp(Set<String> extraTopic) {
        if (log.isDebugEnabled()) {
            log.debug("robin pre handle, extra topic: {}", extraTopic);
        }
        // 缓存
        RobinCacheHandler cacheHandler = RobinManagement.getCacheHandler();
        List<RobinMetadata> metadataList = new ArrayList<>();
        Map<String, String> validatorTopic = RobinEffortFactory.getValidatorTopic(extraTopic);
        // 收集元数据
        validatorTopic.forEach((topic, postureKey) -> {
            // 配置信息
            RobinEffortBasic effort = RobinEffortFactory.getEffort(topic);
            String metadata = RobinMetadataFactory.getMetadataHandler(effort.getMetadataHandler()).getMetadata();
            if (metadata == null || metadata.length() == 0) {
                log.error("topic :[{}] has empty metadata, skip it", topic);
                return;
            }
            RobinMetadata robinMetadata = new RobinMetadata(topic, metadata, effort.getDigest());
            if (cacheHandler.locked(robinMetadata)) {
                throw new RobinException.Verify(RobinExceptionEnum.Verify.MetadataHasLocked, robinMetadata);
            }
            metadataList.add(robinMetadata);
        });
        metadataList.forEach(robinMetadata -> {
            boolean passed;
            try {
                log.debug("robin validate start with metadata: {}", robinMetadata);
                // 执行逻辑
                RobinPosture posture = RobinPostureFactory.getInvokeStrategy(validatorTopic.get(robinMetadata.getTopic()));
                passed = posture.handler(robinMetadata);
            } catch (Exception e) {
                log.error("posture drop the ball", e);
                throw new RobinException.Verify(RobinExceptionEnum.Verify.RobinPostureDropTheBall, robinMetadata);
            }
            // 判断执行结果
            if (!passed && RobinManagement.getRobinInterceptor().onCatch(robinMetadata)) {
                // 配置信息
                cacheHandler.lock(robinMetadata, RobinEffortFactory.getEffort(robinMetadata.getTopic()).getLockDuration());
                throw new RobinException.Verify(RobinExceptionEnum.Verify.VerifyFailed, robinMetadata);
            }
        });
    }
}
