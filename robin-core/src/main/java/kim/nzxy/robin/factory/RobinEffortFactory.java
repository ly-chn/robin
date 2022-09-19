package kim.nzxy.robin.factory;

import kim.nzxy.robin.autoconfigure.RobinEffort;
import kim.nzxy.robin.enums.RobinExceptionEnum;
import kim.nzxy.robin.exception.RobinException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

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
    private static final Map<String, RobinEffort> EFFORT_MAP = new HashMap<>();
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
    public static RobinEffort getEffort(String topic) {
        return EFFORT_MAP.get(topic);
    }

    /**
     * 注册配置内容
     *
     * @param topic  主题
     * @param config 配置内容
     */
    public static void register(String topic, String postureKey, RobinEffort config) {
        if (log.isDebugEnabled()) {
            log.info("register validator config, topic: {}, config: {}", topic, config);
        }
        TOPIC_POSTURE_KEY_MAP.put(topic, postureKey);
        EFFORT_MAP.put(topic, config);
        resetDefaultTopic();
    }

    /**
     * 注册配置内容
     *
     * @param effortMap 配置
     * @param <T>       RobinEffort
     */
    public static <T extends RobinEffort> void register(String postureKey, Map<String, T> effortMap) {
        if (log.isDebugEnabled()) {
            log.info("register effort map: {}", effortMap);
        }
        for (Map.Entry<String, T> entry : effortMap.entrySet()) {
            TOPIC_POSTURE_KEY_MAP.put(entry.getKey(), postureKey);
        }
        EFFORT_MAP.putAll(effortMap);
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
        EFFORT_MAP.entrySet().stream().filter(it->topicSet.contains(it.getKey()))
                .sorted(Comparator.comparingInt(it->it.getValue().getBasic().getPrecedence()))
                .forEach(it-> result.put(it.getKey(), TOPIC_POSTURE_KEY_MAP.get(it.getKey())));
        return result;
    }

    /**
     * 读取拓展配置
     *
     * @param topic 主题
     * @param <T>   type of posture config
     * @return 拓展配置
     */
    public static <T> T getExpandConfig(String topic) {
        Object o = EFFORT_MAP.get(topic).getExpand();
        if (o == null) {
            log.error("expand config missing, topic: {}", topic);
            throw new RobinException.Panic(RobinExceptionEnum.Panic.ExpandConfigOfTopicMissing);
        }
        // noinspection unchecked
        return (T) o;
    }

    /**
     * 计算验证策略(含排序)
     * LinkedHashMap
     */
    private static void resetDefaultTopic() {
        Set<String> topicList = EFFORT_MAP.entrySet().stream()
                .filter(entry -> entry.getValue().getBasic().getAsDefault())
                .map(Map.Entry::getKey).collect(Collectors.toSet());
        DEFAULT_TOPIC_SET.clear();
        DEFAULT_TOPIC_SET.addAll(topicList);
        log.debug("update default topic: {}", DEFAULT_TOPIC_SET);
    }
}
