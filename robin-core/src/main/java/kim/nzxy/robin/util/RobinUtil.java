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
}
