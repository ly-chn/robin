package kim.nzxy.robin;

import kim.nzxy.robin.config.RobinManagement;
import kim.nzxy.robin.config.RobinMetadata;
import kim.nzxy.robin.daily.RobinGetUp;
import kim.nzxy.robin.interceptor.RobinInterceptor;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * 用户工具类
 *
 * @author lyun-chn
 * @since 2021/6/4
 */
@SuppressWarnings("unused")
@Slf4j
public class Robin {

    /**
     * 解除对某限制的封禁
     *
     * @param metadata 元数据，todo: 为null表示解除所有封禁
     */
    public static void unlock(RobinMetadata metadata) {
        RobinManagement.getCacheHandler().unlock(metadata);
    }

    /**
     * 执行验证策略, 含通用策略
     * @see kim.nzxy.robin.annotations.RobinIgnore 调用前请自行判断
     * @see RobinInterceptor#beforeCatch() 调用前请自行判断
     * @param topics 拓展策略
     */
    public static void getUp(Set<String> topics) {
        RobinGetUp.getUp(topics);
    }
}
