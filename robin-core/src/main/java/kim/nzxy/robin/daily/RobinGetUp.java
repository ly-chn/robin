package kim.nzxy.robin.daily;

import kim.nzxy.robin.autoconfigure.RobinEffortBasic;
import kim.nzxy.robin.config.RobinManagement;
import kim.nzxy.robin.enums.RobinExceptionEnum;
import kim.nzxy.robin.exception.RobinException;
import kim.nzxy.robin.factory.RobinEffortFactory;
import kim.nzxy.robin.factory.RobinMetadataFactory;
import kim.nzxy.robin.factory.RobinPostureFactory;
import kim.nzxy.robin.handler.RobinLockHandler;
import kim.nzxy.robin.metadata.RobinMetadata;
import kim.nzxy.robin.posture.RobinPosture;
import lombok.CustomLog;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 工作周期
 *
 * @author ly-chn
 * @since 2022/9/9 15:07
 */
@CustomLog
public class RobinGetUp {
    public static void getUp(Map<String, String> extraTopic) {
        // 缓存
        RobinLockHandler cacheHandler = RobinManagement.getRobinLockHandler();
        List<RobinMetadata> metadataList = new ArrayList<>();
        Map<String, String> validatorTopic = RobinEffortFactory.getValidatorTopic(extraTopic.keySet());
        // 收集元数据
        validatorTopic.forEach((topic, postureKey) -> {
            // 配置信息
            RobinEffortBasic effort = RobinEffortFactory.getEffort(topic);
            String metadata =extraTopic.get(topic);
            if (metadata == null || metadata.isEmpty()) {
                metadata = RobinMetadataFactory.getMetadataHandler(effort.getMetadataHandler()).getMetadata();
            }
            if (metadata == null || metadata.isEmpty()) {
                RobinManagement.getRobinInterceptor()
                        .onCatch(RobinExceptionEnum.Verify.MetadataIsEmpty, null);
                return;
            }
            RobinMetadata robinMetadata = new RobinMetadata(topic, metadata, effort.getDigest());
            if (cacheHandler.locked(robinMetadata)) {
                RobinManagement.getRobinInterceptor()
                        .onCatch(RobinExceptionEnum.Verify.MetadataHasLocked, robinMetadata);
            }
            metadataList.add(robinMetadata);
        });
        metadataList.forEach(robinMetadata -> {
            boolean passed = false;
            try {
                log.debug("robin validate start with metadata: " + robinMetadata);
                // 执行逻辑
                RobinPosture posture = RobinPostureFactory.getInvokeStrategy(validatorTopic.get(robinMetadata.getTopic()));
                passed = posture.handler(robinMetadata);
            } catch (Exception e) {
                log.error("posture drop the ball", e);
                RobinManagement.getRobinInterceptor()
                        .onCatch(RobinExceptionEnum.Verify.RobinPostureDropTheBall, robinMetadata);
            }
            // 判断执行结果
            if (!passed) {
                RobinManagement.getRobinInterceptor().onCatch(RobinExceptionEnum.Verify.VerifyFailed, robinMetadata);
            }
        });
    }
}
