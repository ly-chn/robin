package kim.nzxy.robin.filter;

import kim.nzxy.robin.exception.RobinException;

/**
 * Validator验证钩子
 *
 * @author xy
 * @since 2021/6/16
 */
public interface RobinInterceptor {
    /**
     * @return 返回 false 将不会执行拦截策略, 可以针对指定用户指定角色进行信任
     */
    default boolean beforeValidate() {
        return true;
    }

    /**
     * 验证失败时的钩子, 可以在此实现自己的拦截逻辑, 比如羞辱爬虫一番, 返回错乱的结果, 或者记录一下容易出现异常的IP地址/用户进行分析
     *
     * @param e Robin验证异常
     * @return 返回 false 表示忽略此捕获, 并不再执行其它策略. 返回 true 则抛出此异常, 等待异常拦截器处理
     */
    default boolean onCache(RobinException e) {
        return true;
    }
}
