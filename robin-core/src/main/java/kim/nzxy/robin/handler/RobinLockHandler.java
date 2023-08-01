package kim.nzxy.robin.handler;

import kim.nzxy.robin.metadata.RobinMetadata;

import java.time.Duration;

/**
 * robin 统一处理锁定信息
 * @author ly-chn
 */
public interface RobinLockHandler {
    /**
     * 锁定
     *
     * @param metadata 元数据
     * @param lock     锁定时长, 为0表示不锁定
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
     * @param metadata 元数据, 为null表示清空所有,
     *                 topic为null表示清空所有topic,
     *                 metadata为null表示清空topic下所有metadata
     */
    void unlock(RobinMetadata metadata);

    /**
     * 梳理缓存
     */
    void freshenUp();
}
