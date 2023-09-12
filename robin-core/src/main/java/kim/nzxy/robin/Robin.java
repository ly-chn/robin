package kim.nzxy.robin;

import kim.nzxy.robin.config.RobinManagement;
import kim.nzxy.robin.daily.RobinGetUp;
import kim.nzxy.robin.interceptor.RobinInterceptor;
import kim.nzxy.robin.metadata.RobinMetadata;
import lombok.CustomLog;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * 用户工具类
 *
 * @author lyun-chn
 * @since 2021/6/4
 */
@SuppressWarnings("unused")
@CustomLog
public class Robin {

    /**
     * 解除对某限制的封禁
     *
     * @param metadata 元数据，为null表示解除所有封禁
     */
    public static void unlock(RobinMetadata metadata) {
        RobinManagement.getRobinLockHandler().unlock(metadata);
    }

    /**
     * 将指定的topic暂存, 以便后续统一处理
     *
     * @param topics 拓展策略, key为topic key, value 为topic的默认metadata
     */
    public static void crop(@Nullable Map<String, String> topics) {
        RobinGetUp.crop(topics);
    }

    /**
     * 直接执行验证策略, 含通用策略, 执行完成后清空暂存
     *
     * @param topics 拓展策略, key为topic key, value 为topic的默认metadata
     * @see kim.nzxy.robin.annotations.RobinIgnore 调用前请自行判断
     * @see RobinInterceptor#beforeCatch() 调用前请自行判断
     */
    public static void hunger(@Nullable Map<String, String> topics) {
        RobinGetUp.hunger(topics);
    }
}
