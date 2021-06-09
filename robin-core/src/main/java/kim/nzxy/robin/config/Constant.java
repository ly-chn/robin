package kim.nzxy.robin.config;

/**
 * 固定, 配置, 如公共前缀, 固定逻辑等
 *
 * @author xy
 * @since 2021/6/5
 */
@SuppressWarnings("unused")
public interface Constant {
    // 缓存公共前缀
    String recordPrefix = "robin:record:";
    String lockedPrefix = "robin:locked:";

    // ip 访问记录缓存前缀
    String ipPrefix = recordPrefix + "ip:";

    // ip 封禁记录缓存前缀
    String ipLockedPrefix = lockedPrefix + "ip:";
}
