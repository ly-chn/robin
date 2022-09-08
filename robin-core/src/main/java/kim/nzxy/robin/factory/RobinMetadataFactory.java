package kim.nzxy.robin.factory;

import kim.nzxy.robin.enums.RobinExceptionEnum;
import kim.nzxy.robin.exception.RobinException;
import kim.nzxy.robin.handler.RobinMetadataHandler;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 元数据方法工厂
 *
 * @author lyun-chn
 * @since 2022/8/29 12:12
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RobinMetadataFactory {
    /**
     * 校验策略
     */
    private static final Map<String, RobinMetadataHandler> METADATA_STRATEGY_MAP = new HashMap<>();


    /**
     * @return 所有策略(用户定义 + 内置)
     */
    public static RobinMetadataHandler getMetadataHandler(String topic) {
        if (!METADATA_STRATEGY_MAP.containsKey(topic)) {
            log.error("未找到对应topic的元数据处理器： {}", topic);
            throw new RobinException.Panic(RobinExceptionEnum.Panic.MetadataHandlerMissing);
        }
        return METADATA_STRATEGY_MAP.get(topic);
    }

    /**
     * 添加用户自定义验证策略
     *
     * @param handler 自定义策略
     */
    public static void register(String topic, RobinMetadataHandler handler) {
        if (log.isDebugEnabled()) {
            log.debug("register metadata：{}", handler.getClass());
        }
        METADATA_STRATEGY_MAP.put(topic, handler);
    }
}
