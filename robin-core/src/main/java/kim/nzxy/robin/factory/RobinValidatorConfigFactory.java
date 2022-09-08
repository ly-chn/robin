package kim.nzxy.robin.factory;

import kim.nzxy.robin.autoconfigure.RobinValidatorBasicConfig;
import kim.nzxy.robin.autoconfigure.ValidatorConfig;
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
public class RobinValidatorConfigFactory {
    /**
     * 校验策略
     */
    private static final Map<String, ValidatorConfig> VALIDATOR_CONFIG_MAP = new HashMap<>();

    /**
     * 注册配置内容
     *
     * @return 所有策略(用户定义 + 内置)
     */
    public static ValidatorConfig getValidatorConfig(String topic) {
        return VALIDATOR_CONFIG_MAP.get(topic);
    }

    /**
     * 注册配置内容
     *
     * @param topic  主题
     * @param config 配置内容
     */
    public static void registerValidatorConfig(String topic, ValidatorConfig config) {
        if (log.isDebugEnabled()) {
            log.info("register validator config, topic: {}, config: {}", topic, config);
        }
        VALIDATOR_CONFIG_MAP.put(topic, config);
    }

    /**
     * 注册配置内容
     *
     * @param configMap 配置
     * @param <T>       ValidatorConfig
     */
    public static <T extends ValidatorConfig> void registerValidatorConfig(Map<String, T> configMap) {
        if (log.isDebugEnabled()) {
            log.info("register validator map: {}", configMap);
        }
        VALIDATOR_CONFIG_MAP.putAll(configMap);
    }

    /**
     * 读取基础配置信息
     *
     * @param topic 主题
     * @return 基础配置
     */
    public static RobinValidatorBasicConfig getBasicConfig(String topic) {
        return VALIDATOR_CONFIG_MAP.get(topic).getBasic();
    }
    /**
     * todo: 返回topic和key, topic即当前类中, key在RobinValidatorFactory中获取, 但是不见得唯一
     * 注册时还是需要手动维护config与配置的关系
     * @return 全局策略
     */
    public static List<String> getGlobalValidatorTopic() {
        return VALIDATOR_CONFIG_MAP.entrySet().stream()
                .filter(entry -> entry.getValue().getBasic().getAsDefault())
                .sorted(Comparator.comparingInt(it -> it.getValue().getBasic().getPrecedence()))
                .map(Map.Entry::getKey).collect(Collectors.toList());
    }

    public static <T> T getStrategyConfig(String key, Class<T> type) {
        Object o = VALIDATOR_CONFIG_MAP.get(key).getConfig();
        // noinspection unchecked
        return (T) o;
    }
}
