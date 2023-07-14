package kim.nzxy.robin.factory;

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
     * 校验策略, key为元数据标识， value为元数据处理器
     */
    private static final Map<String, RobinMetadataHandler> METADATA_STRATEGY_MAP = new HashMap<>();
    /**
     * 默认元数据处理器
     */
    private static final RobinMetadataHandler DEFAULT_METADATA_HANDLER = () -> null;


    /**
     * @return 所有策略(用户定义 + 内置)
     */
    public static RobinMetadataHandler getMetadataHandler(String name) {
        if (!METADATA_STRATEGY_MAP.containsKey(name)) {
            log.error("未找到对应topic的元数据处理器： {}, 将使用默认处理器", name);
            return DEFAULT_METADATA_HANDLER;
        }
        return METADATA_STRATEGY_MAP.get(name);
    }

    /**
     * 添加用户自定义验证策略
     *
     * @param handler 自定义策略
     */
    public static void register(String name, RobinMetadataHandler handler) {
        if (log.isDebugEnabled()) {
            log.debug("register metadata：{}", handler.getClass());
        }
        METADATA_STRATEGY_MAP.put(name, handler);
    }
}
