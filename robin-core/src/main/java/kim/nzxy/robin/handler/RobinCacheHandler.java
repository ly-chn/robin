package kim.nzxy.robin.handler;


import kim.nzxy.robin.config.RobinMetadata;

import java.time.Duration;

/**
 * 缓存, 本接口命名尽可能与 redis 一致, 超时单位默认是秒级
 * todo: 修改为redis工具类形式, 不再做详细封装
 *
 * @author xy
 * @since 2021/6/4
 */
public interface RobinCacheHandler {

    /**
     * 持续访问记录
     *
     * @param metadata      元数据
     * @param timeFrameSize 时间窗口大小
     * @return 已经连续访问的次数（含本次）
     */
    int sustainVisit(RobinMetadata metadata, Duration timeFrameSize);

    /**
     * 锁定
     *
     * @param metadata 元数据
     * @param lock     锁定时长
     */
    void lock(RobinMetadata metadata, Duration lock);

    /**
     * 判断是否封禁
     *
     * @param metadata 元数据
     * @return true表示已封禁
     */
    boolean locked(RobinMetadata metadata);

    /**
     * 主动解除封禁
     *
     * @param metadata 元数据
     */
    void unlock(RobinMetadata metadata);
}
