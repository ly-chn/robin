package kim.nzxy.robin;

import kim.nzxy.robin.config.RobinManagement;
import kim.nzxy.robin.config.RobinMetadata;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户工具类
 *
 * @author xy
 * @since 2021/6/4
 */
@SuppressWarnings("unused")
@Slf4j
public class Robin {

    /**
     * 解除对某限制的封禁
     *
     * @param metadata 元数据，为null表示解除所有封禁
     */
    public static void unlock(RobinMetadata metadata) {
        RobinManagement.getCacheHandler().unlock(metadata);
    }
}
