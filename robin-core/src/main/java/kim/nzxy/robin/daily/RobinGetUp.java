package kim.nzxy.robin.daily;

import kim.nzxy.robin.autoconfigure.RobinEffortBasic;
import kim.nzxy.robin.config.RobinManagement;
import kim.nzxy.robin.enums.RobinExceptionEnum;
import kim.nzxy.robin.factory.RobinEffortFactory;
import kim.nzxy.robin.factory.RobinMetadataFactory;
import kim.nzxy.robin.factory.RobinPostureFactory;
import kim.nzxy.robin.handler.RobinLockHandler;
import kim.nzxy.robin.metadata.RobinMetadata;
import kim.nzxy.robin.posture.RobinPosture;
import kim.nzxy.robin.util.RobinUtil;
import lombok.CustomLog;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * 工作周期
 *
 * @author ly-chn
 * @since 2022/9/9 15:07
 */
@CustomLog
public class RobinGetUp {
    static final ThreadLocal<Map<String, String>> TOPIC_THREAD_LOCAL = new ThreadLocal<>();

    private static void getUp(@NotNull Map<String, String> extraTopic) {
        // 缓存
        RobinLockHandler cacheHandler = RobinManagement.getRobinLockHandler();
        List<RobinMetadata> metadataList = new ArrayList<>();
        Map<String, String> validatorTopic = RobinEffortFactory.getValidatorTopic(new HashSet<>(extraTopic.keySet()));
        // 收集元数据
        validatorTopic.forEach((topic, postureKey) -> {
            // 配置信息
            RobinEffortBasic effort = RobinEffortFactory.getEffort(topic);
            String metadata =extraTopic.get(topic);
            if (RobinUtil.isEmpty(metadata)) {
                metadata = RobinMetadataFactory.getMetadataHandler(effort.getMetadataHandler()).getMetadata();
            }
            if (RobinUtil.isEmpty(metadata)) {
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

    public static void crop(@Nullable Map<String, String> extraTopic) {
        if (RobinUtil.isEmpty(extraTopic)) {
            return;
        }
        Map<String, String> topicMap = TOPIC_THREAD_LOCAL.get();
        if (topicMap == null) {
            TOPIC_THREAD_LOCAL.set(extraTopic);
        } else {
            topicMap.putAll(extraTopic);
        }
    }

    public static void clear() {
        TOPIC_THREAD_LOCAL.remove();
    }

    public static void hunger(@Nullable Map<String, String> topics) {
        try{
            // 有暂存, 有提交
            if (TOPIC_THREAD_LOCAL.get() != null && RobinUtil.isNotEmpty(topics)) {
                Map<String, String> topicMap = TOPIC_THREAD_LOCAL.get();
                topicMap.putAll(topics);
                getUp(topicMap);
                return;
            }
            // 有暂存, 无提交
            if (TOPIC_THREAD_LOCAL.get() != null) {
                getUp(TOPIC_THREAD_LOCAL.get());
                return;
            }
            // 无暂存, 有提交
            if (RobinUtil.isNotEmpty(topics)) {
                getUp(topics);
            }
        }finally {
            clear();
        }
    }
}
