package kim.nzxy.robin.factory;

import kim.nzxy.robin.autoconfigure.RobinEffort;
import kim.nzxy.robin.autoconfigure.RobinEffortBasic;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
     * 校验策略
     */
    private static final Map<String, RobinEffort> EFFORT_MAP = new HashMap<>();
    /**
     * key为topic, value为postureKey
     */
    private static final Map<String, String> TOPIC_POSTURE_KEY_MAP = new HashMap<>();

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
    }

    /**
     * 注册配置内容
     *
     * @param configMap 配置
     * @param <T>       RobinEffort
     */
    public static <T extends RobinEffort> void register(String postureKey, Map<String, T> configMap) {
        if (log.isDebugEnabled()) {
            log.info("register validator map: {}", configMap);
        }
        for (Map.Entry<String, T> entry : configMap.entrySet()) {
            TOPIC_POSTURE_KEY_MAP.put(entry.getKey(), postureKey);
        }
        EFFORT_MAP.putAll(configMap);
    }

    /**
     * 读取基础配置信息
     *
     * @param topic 主题
     * @return 基础配置
     */
    public static RobinEffortBasic getEffortBasic(String topic) {
        return EFFORT_MAP.get(topic).getBasic();
    }

    /**
     * todo: 缓存起来, 不必每次计算
     * 注册时还是需要手动维护config与配置的关系
     * @return key为topic, value为postureKey
     */
    public static List<String> getGlobalValidatorTopic() {
        return EFFORT_MAP.entrySet().stream()
                .filter(entry -> entry.getValue().getBasic().getAsDefault())
                .sorted(Comparator.comparingInt(it -> it.getValue().getBasic().getPrecedence()))
                .map(Map.Entry::getKey).collect(Collectors.toList());
    }

    public static <T> T getStrategyConfig(String key, Class<T> type) {
        Object o = EFFORT_MAP.get(key).getConfig();
        // noinspection unchecked
        return (T) o;
    }

    /**
     * 计算验证策略(含排序)
     * LinkedHashMap
     */
    private static void calcGlobalConfig() {

    }
}
