package kim.nzxy.robin.handler;


import kim.nzxy.robin.enums.RobinRuleEnum;

import java.time.Duration;
import java.util.List;

/**
 * 缓存, 本接口命名尽可能与 redis 一致, 超时单位默认是秒级
 *
 * @author xy
 * @since 2021/6/4
 */
public interface RobinCacheHandler {

    /**
     * 添加访问记录
     *
     * @param type   访问类型, 归属于保存记录的策略
     * @param target 访客
     * @param expire 缓存过期时间, 过期时间小于当前时间则可能被清理逻辑清理掉
     */
    void accessRecord(RobinRuleEnum type, String target, int expire);

    /**
     * 获取全部访问记录
     *
     * @param type   访问类型, 归属于保存记录的策略
     * @param target 访客
     * @return 最近访问时间戳
     */
    default List<Integer> getAccessRecord(RobinRuleEnum type, String target) {
        return getAccessRecord(type, target, 0);
    }

    /**
     * 获取全部访问记录
     *
     * @param type   访问类型, 归属于保存记录的策略
     * @param target 访客
     * @param length 获取最近多少条, 0表示获取全部
     * @return 最近访问时间戳
     */
    List<Integer> getAccessRecord(RobinRuleEnum type, String target, int length);
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
     * @param target 锁定类型, 为 null 则表示所有
     */
    void unlock(RobinRuleEnum type, String target);

}
