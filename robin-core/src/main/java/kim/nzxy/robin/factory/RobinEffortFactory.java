package kim.nzxy.robin.factory;

import kim.nzxy.robin.autoconfigure.RobinEffortBasic;
import kim.nzxy.robin.enums.RobinExceptionEnum;
import kim.nzxy.robin.exception.RobinException;
import lombok.AccessLevel;
import lombok.CustomLog;
import lombok.NoArgsConstructor;

import java.util.*;

/**
 * robin验证策略配置工厂
 *
 * @author lyun-chn
 * @since 2022/9/8 14:15
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@CustomLog
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
     * 默认使用的topic
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
     * 读取全部topic
     * @return 全部topic集合
     */
    public static Set<String> getAllTopic() {
        return TOPIC_POSTURE_KEY_MAP.keySet();
    }

    /**
     * 注册配置内容
     */
    public static <T extends RobinEffortBasic> void register(String postureKey, List<T> effortList) {
        if (log.isDebugEnabled()) {
            log.debug("register effort map: " + effortList);
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
    public static Map<String, String> getValidatorTopic(Set<String> extraTopic) {
        extraTopic.addAll(DEFAULT_TOPIC_SET);
        extraTopic.forEach(topic->{
            if (!EFFORT_MAP.containsKey(topic)) {
                log.error("topic [" + topic + "] has not configured");
                throw new RobinException.Panic(RobinExceptionEnum.Panic.TopicIsNotConfigured);
            }
        });
        Map<String, String> result = new LinkedHashMap<>();
        EFFORT_MAP.entrySet().stream().filter(it -> extraTopic.contains(it.getKey()))
                .sorted(Comparator.comparingInt(it -> it.getValue().getPrecedence()))
                .forEach(it -> result.put(it.getKey(), TOPIC_POSTURE_KEY_MAP.get(it.getKey())));
        return result;
    }

    /**
     * 根据策略key读取对应topic列表
     *
     * @param postureKey 验证策略key
     * @return 根据postureKey获取应用的topic
     */
    public static Set<String> getTopicByKey(String postureKey) {
        if (postureKey == null) {
            return Collections.emptySet();
        }
        Set<String> result = new HashSet<>();
        TOPIC_POSTURE_KEY_MAP.forEach((k, v) -> {
            if (Objects.equals(postureKey, v)) {
                result.add(k);
            }
        });
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
        log.debug("update default topic: " + DEFAULT_TOPIC_SET);
    }

}
