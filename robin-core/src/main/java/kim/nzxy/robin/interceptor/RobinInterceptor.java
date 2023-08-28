package kim.nzxy.robin.interceptor;

import com.sun.istack.internal.Nullable;
import kim.nzxy.robin.config.RobinManagement;
import kim.nzxy.robin.enums.RobinExceptionEnum;
import kim.nzxy.robin.exception.RobinException;
import kim.nzxy.robin.factory.RobinEffortFactory;
import kim.nzxy.robin.metadata.RobinMetadata;

import java.util.Objects;
import java.util.function.Function;

/**
 * Robin验证钩子
 *
 * @author lyun-chn
 * @since 2021/6/16
 */
public interface RobinInterceptor {
    /**
     * 全部验证逻辑执行之前执行
     *
     * @return 返回 false 将不会执行任何拦截策略, 可以针对指定用户指定角色进行信任
     */
    default boolean beforeCatch() {
        return true;
    }

    /**
     * 验证失败时的钩子, 可以在此实现自己的拦截逻辑, 比如羞辱爬虫一番, 返回错乱的结果, 或者记录一下容易出现异常的IP地址/用户进行分析
     *
     * @param type          异常类型
     * @param robinMetadata 元数据详情
     * @throws RobinException.Verify 自动输出异常
     */
    default void onCatch(RobinExceptionEnum.Verify type, @Nullable RobinMetadata robinMetadata) throws RobinException.Verify {
        // 验证失败则锁定
        if (Objects.equals(type, RobinExceptionEnum.Verify.VerifyFailed)) {
            RobinManagement.getRobinLockHandler()
                    .lock(robinMetadata, RobinEffortFactory.getEffort(robinMetadata.getTopic()).getLockDuration());

        }
        throw new RobinException.Verify(type, robinMetadata);
    }

    /**
     * 全部校验逻辑走完之后执行
     */
    default void afterCache() {
    }
}
