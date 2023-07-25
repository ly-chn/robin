package kim.nzxy.robin.interceptor;

import kim.nzxy.robin.config.RobinMetadata;

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
     * @param robinMetadata 元数据详情
     * @return 返回 false 表示忽略此捕获. 返回 true 则抛出此异常, 等待异常拦截器处理
     */
    default boolean onCatch(RobinMetadata robinMetadata) {
        return true;
    }

    /**
     * 全部校验逻辑走完之后执行
     */
    default void afterCache() {
    }
}
