package kim.nzxy.robin.handler;


import kim.nzxy.robin.enums.RobinRuleEnum;

import java.time.Duration;
import java.util.List;

/**
 * 缓存, 本接口命名尽可能与 redis 一致, 超时单位默认是秒级
 * ttlList 实则是redis中的 zset 方案, 定时删除过期 key
 *
 * @author xy
 * @since 2021/6/4
 */
public interface RobinCacheHandler {

    /**
     * 添加访问记录
     *
     * @param type      访问类型, 归属于保存记录的策略
     * @param target    访客
     * @param timestamp 访问时间, 秒级时间戳
     */
    void accessRecord(RobinRuleEnum type, String target, int timestamp);

    /**
     * 获取全部访问记录
     *
     * @param type   访问类型, 归属于保存记录的策略
     * @param target 访客
     */
    List<Integer> accessRecord(RobinRuleEnum type, String target);


    /**
     * 封禁ip
     *
     * @param ip     ip地址
     * @param unlock 多久后解封
     */
    void lockIp(String ip, Duration unlock);

    /**
     * 判断ip地址是否被封禁
     *
     * @param ip ip地址
     * @return 返回 true 表示 ip 已被封
     */
    boolean lockIp(String ip);

    /**
     * 设置锁定
     *
     * @param type   锁定原因
     * @param target 锁定目标
     * @param unlock 锁定时间
     */
    void lock(RobinRuleEnum type, String target, Duration unlock);

    /**
     * 判断是否已被锁定
     *
     * @param type   锁定原因
     * @param target 锁定内容
     * @return 为true表示已锁定
     */
    boolean lock(RobinRuleEnum type, String target);

    /**
     * 主动解除锁定
     *
     * @param type   锁定原因
     * @param target 锁定类型
     */
    void unlock(RobinRuleEnum type, String target);

    /**
     * 清理访问记录, 删除指定时间戳之前的日志记录
     *
     * @param type      访问类型, 归属于保存记录的策略
     * @param timestamp 访问时间, 秒级时间戳, 低于此时间的都将会被清理
     */
    void cleanAccessRecord(RobinRuleEnum type, int timestamp);


    /**
     * 清理锁定内容
     */
    void cleanLock();
}
