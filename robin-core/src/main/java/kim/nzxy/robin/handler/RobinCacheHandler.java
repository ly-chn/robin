package kim.nzxy.robin.handler;


import kim.nzxy.robin.config.RobinMetaData;

import java.time.Duration;

/**
 * 缓存, 本接口命名尽可能与 redis 一致, 超时单位默认是秒级
 *
 * @author xy
 * @since 2021/6/4
 */
public interface RobinCacheHandler {

    /**
     * 持续访问记录
     *
     * @param metaData      元数据
     * @param timeFrameSize 时间窗口大小
     * @return 已经连续访问的次数（含本次）
     */
    int sustainVisit(RobinMetaData metaData, Duration timeFrameSize);

    /**
     * 锁定
     *
     * @param metaData 元数据
     * @param lock     锁定时长
     */
    void lock(RobinMetaData metaData, Duration lock);

    /**
     * 判断是否封禁
     *
     * @param metaData 元数据
     * @return true表示已封禁
     */
    boolean locked(RobinMetaData metaData);

    /**
     * 主动解除封禁
     *
     * @param metaData 元数据
     */
    void unlock(RobinMetaData metaData);
}
