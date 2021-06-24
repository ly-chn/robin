package kim.nzxy.robin.util;

import kim.nzxy.robin.enums.RobinBuiltinErrEnum;
import kim.nzxy.robin.exception.RobinBuiltinException;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;

/**
 * 普普通通的工具类
 *
 * @author xy
 * @since 2021/6/6
 */
public class RobinUtil {
    /**
     * @return 返回当前秒级时间戳
     */
    public static int now() {
        return (int) (System.currentTimeMillis() / 1000);
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

    public static String md5(String str) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            // will not throw
            throw new RobinBuiltinException(RobinBuiltinErrEnum.MODE_NOT_IMPLEMENTED_YET);
        }
        byte[] digest;
        digest = md.digest(str.getBytes(StandardCharsets.UTF_8));
        return String.format("%032x", new BigInteger(1, digest));
        // 这是网上找到的另一种方法, 开头的0会被忽略, 故而未采用
        // return new BigInteger(1,digest).toString(16);
    }

}
