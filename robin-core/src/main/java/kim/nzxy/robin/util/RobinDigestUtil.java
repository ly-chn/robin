package kim.nzxy.robin.util;

import kim.nzxy.robin.enums.RobinExceptionEnum;
import kim.nzxy.robin.exception.RobinException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * 摘要工具类
 *
 * @author lyun-chn
 * @since 2022/8/25 17:11
 */
public class RobinDigestUtil {
    @SuppressWarnings("SpellCheckingInspection")
    private static final String DIGITS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_/";

    private static final int BASE = DIGITS.length();

    /**
     * 对消息进行摘要
     *
     * @param str 目标字符串
     * @return 摘要结果
     */
    public static String md5(String str) {
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

    /**
     * 将数字转换为64进制字符串 url-safe
     *
     * @param num 目标数字, 仅支持正整数
     * @return 64进制字符串
     */
    public static String numTo64(Number num) {
        if (Objects.isNull(num)) {
            return "";
        }
        long n = num.longValue();
        if (n == 0) {
            return "0";
        }
        StringBuilder sb = new StringBuilder();
        while (n > 0) {
            sb.append(DIGITS.charAt((int) (n % BASE)));
            n /= BASE;
        }
        return sb.reverse().toString();
    }

    /**
     * 解析64位number
     *
     * @param num64 要解析的数字
     * @return 解析结果
     */
    public static long parse64(String num64) {
        long n = 0;
        for (int i = 0; i < num64.length(); i++) {
            char c = num64.charAt(i);
            n = n * BASE + DIGITS.indexOf(c);
        }
        return n;
    }


    public static String ipv4To64(String ipv4) {
        String[] ipAddressInArray = ipv4.split("\\.");
        long result = 0;
        for (int i = 0; i < ipAddressInArray.length; i++) {
            int power = 3 - i;
            int ip = Integer.parseInt(ipAddressInArray[i]);
            result += (long) (ip * Math.pow(256, power));
        }
        return numTo64(result);
    }

    public static String num64ToIpv4(String num64) {
        long n = parse64(num64);
        StringBuilder sb = new StringBuilder(15);
        for (int i = 0; i < 4; i++) {
            int shift = i * 8;
            sb.insert(0, n >> shift & 0xff);
            if (i < 3) {
                sb.insert(0, '.');
            }
        }
        return sb.toString();
    }
}
