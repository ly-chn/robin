package kim.nzxy.robin.util;

import kim.nzxy.robin.config.RobinManagement;
import kim.nzxy.robin.enums.RobinExceptionEnum;
import kim.nzxy.robin.exception.RobinException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 摘要工具类
 *
 * @author lyun-chn
 * @since 2022/8/25 17:11
 */
public class RobinDigestUtil {

    /**
     * 对消息进行摘要
     *
     * @param str 目标字符串
     * @return 摘要结果
     */
    public static String digest(String str) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            // will not throw
            throw new RobinException.Panic(RobinExceptionEnum.Panic.DigestUtilInitError);
        }
        byte[] digest = md.digest(str.getBytes(StandardCharsets.UTF_8));
        return new String(digest);
    }
}
