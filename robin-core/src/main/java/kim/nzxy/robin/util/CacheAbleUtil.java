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

    /**
     * @return 当前ip是否在黑名单中
     */
    public static boolean ipInWhitelist() {
        // todo: ip白名单
        // if (IP_IN_WHITE_LIST.get() != null) {
        //     return IP_IN_WHITE_LIST.get();
        // }
        // val ip = RobinManagement.getContextHandler().ip();
        // val whitelist = RobinManagement.getRobinProperties().getBlackWhiteList().getIp().getAllowlist();
        // val result = MatcherUtil.str(ip, whitelist);
        // IP_IN_WHITE_LIST.set(result);
        return true;
    }
}
