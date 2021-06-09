package kim.nzxy.robin.handler;


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
     * ip访问记录
     *
     * @param ip        ip地址
     * @param timestamp 访问时间, 秒级时间戳
     */
    void ipAccessRecord(String ip, int timestamp);

    /**
     * @param ip ip地址
     * @return 该 ip 地址的最近访问时间集合
     */
    List<Integer> ipAccessRecord(String ip);

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
}
