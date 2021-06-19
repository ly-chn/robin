package kim.nzxy.robin.util;

import kim.nzxy.robin.config.RobinManagement;
import lombok.val;

/**
 * 工具类的调用结果会保存在ThreadLocal之中
 *
 * @author xy
 * @since 2021/6/19
 */
public class CacheAbleUtil {
    private static final ThreadLocal<Boolean> ipInWhiteList = new ThreadLocal<>();

    /**
     * @return 当前ip是否在黑名单中
     */
    public static boolean ipInBlackList() {
        if (ipInWhiteList.get() != null) {
            return ipInWhiteList.get();
        }
        val ip = RobinManagement.getContextHandler().ip();
        val whitelist = RobinManagement.getRobinProperties().getBlackWhiteList().getIp().getWhitelist();
        val result = MatcherUtil.str(ip, whitelist);
        ipInWhiteList.set(result);
        return result;
    }
}
