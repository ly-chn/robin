package kim.nzxy.robin.util;

import kim.nzxy.robin.enums.RobinBuiltinErrEnum;
import kim.nzxy.robin.exception.RobinBuiltinException;

import java.util.Map;

/**
 * 普普通通的工具类
 *
 * @author xy
 * @since 2021/6/6
 */
public class RobinUtil {
    public static int now() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    public static void TODO() {
        throw new RobinBuiltinException(RobinBuiltinErrEnum.METHOD_NOT_IMPLEMENTED_YET);
    }

    /**
     * 通过 map 的 value 反查 key (如果有多个匹配, 返回第一个)
     *
     * @return key
     */
    public static <K, V> K getMapKey(Map<K, V> target, V value) {
        for (K k : target.keySet()) {
            if (target.get(k).equals(value)) {
                return k;
            }
        }
        return null;
    }
}
