package kim.nzxy.robin.factory;

import kim.nzxy.robin.autoconfigure.RobinBasicStrategy;
import kim.nzxy.robin.autoconfigure.ValidatorConfig;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

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
    public static RobinBasicStrategy getBasicConfig(String topic) {
        return VALIDATOR_CONFIG_MAP.get(topic).getBasic();
    }

}
