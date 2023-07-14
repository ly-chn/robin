package kim.nzxy.robin.factory;

import kim.nzxy.robin.autoconfigure.RobinEffortBasic;
import kim.nzxy.robin.enums.RobinExceptionEnum;
import kim.nzxy.robin.exception.RobinException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * robin验证策略配置工厂
 *
 * @author lyun-chn
 * @since 2022/9/8 14:15
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class RobinEffortFactory {
    /**
     * 配置内容, key为topic, value为Effort
     */
    private static final Map<String, RobinEffortBasic> EFFORT_MAP = new HashMap<>();
    /**
     * key为topic, value为postureKey
     */
    private static final Map<String, String> TOPIC_POSTURE_KEY_MAP = new HashMap<>();
    /**
     * 默认适用的topic
     */
    private static final Set<String> DEFAULT_TOPIC_SET = new HashSet<>();

    /**
     * 注册配置内容
     *
     * @return 所有策略(用户定义 + 内置)
     */
    public static <T extends RobinEffortBasic> T getEffort(String topic) {
        // noinspection unchecked
        return (T) EFFORT_MAP.get(topic);
    }

    /**
     * 注册配置内容
     *
     * @param topic  主题
     * @param config 配置内容
     */
    public static <T extends RobinEffortBasic> void register(String topic, String postureKey, T config) {
        if (log.isDebugEnabled()) {
            log.debug("register validator config, topic: {}, config: {}", topic, config);
        }
        TOPIC_POSTURE_KEY_MAP.put(topic, postureKey);
        EFFORT_MAP.put(topic, config);
        resetDefaultTopic();
    }

    /**
     * 注册配置内容
     */
    public static <T extends RobinEffortBasic> void register(String postureKey, List<T> effortList) {
        if (log.isDebugEnabled()) {
            log.debug("register effort map: {}", effortList);
        }
        effortList.forEach(it -> {
            TOPIC_POSTURE_KEY_MAP.put(it.getTopic(), postureKey);
            EFFORT_MAP.put(it.getTopic(), it);
        });
        resetDefaultTopic();
    }

    /**
     * 注册时还是需要手动维护config与配置的关系
     *
     * @return key为topic, value为postureKey
     */
    public static Map<String, String> getValidatorTopic(String[] extraTopic) {
        Set<String> topicSet = new HashSet<>(DEFAULT_TOPIC_SET);
        topicSet.addAll(Arrays.asList(extraTopic));
        for (String s : topicSet) {
            if (!EFFORT_MAP.containsKey(s)) {
                log.error("topic [{}] has not configured", s);
                throw new RobinException.Panic(RobinExceptionEnum.Panic.TopicIsNotConfigured);
            }
        }
        Map<String, String> result = new LinkedHashMap<>();
        EFFORT_MAP.entrySet().stream().filter(it -> topicSet.contains(it.getKey()))
                .sorted(Comparator.comparingInt(it -> it.getValue().getPrecedence()))
                .forEach(it -> result.put(it.getKey(), TOPIC_POSTURE_KEY_MAP.get(it.getKey())));
        return result;
    }


    /**
     * 计算验证策略(含排序)
     * LinkedHashMap
     */
    private static void resetDefaultTopic() {
        DEFAULT_TOPIC_SET.clear();
        EFFORT_MAP.entrySet().stream()
                .filter(entry -> entry.getValue().getAsDefault())
                .forEach(e -> DEFAULT_TOPIC_SET.add(e.getKey()));
        log.debug("update default topic: {}", DEFAULT_TOPIC_SET);
    }
}
