package kim.nzxy.robin.handler;


import kim.nzxy.robin.metadata.RobinMetadata;

import java.time.Duration;

/**
 * 缓存, 本接口命名尽可能与 redis 一致, 超时单位默认是秒级
 *
 * @author lyun-chn
 * @since 2021/6/4
 */
public interface RobinCacheHandler {

    /**
     * 持续访问记录
     *
     * @param metadata      元数据
     * @param timeFrameSize 时间窗口大小
     * @param maxTimes      最大连续窗口次数, 最小值为1
     * @return true表示校验通过
     */
    boolean sustainVisit(RobinMetadata metadata, Duration timeFrameSize, Integer maxTimes);

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
     * todo: 增加一个方法, 清理这个人的所有记录, 否则下次访问还可能被封禁
     *
     * @param metadata 元数据
     */
    void unlock(RobinMetadata metadata);

    /**
     * 清理缓存
     */
    void freshenUp();
}
