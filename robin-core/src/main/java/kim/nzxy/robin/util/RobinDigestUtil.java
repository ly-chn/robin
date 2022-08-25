package kim.nzxy.robin.util;

import kim.nzxy.robin.enums.RobinBuiltinErrEnum;
import kim.nzxy.robin.exception.RobinBuiltinException;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * 摘要工具类
 *
 * @author xuyf
 * @since 2022/8/25 17:11
 */
public class RobinDigestUtil {

    /**
     * 对消息进行摘要
     *
     * @param str 消息
     * @return 摘要结果
     */
    public String digest(String str) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            md.reset();
        } catch (NoSuchAlgorithmException e) {
            // will not throw
            throw new RobinBuiltinException(RobinBuiltinErrEnum.MODE_NOT_IMPLEMENTED_YET);
        }
        byte[] digest;
        digest = md.digest(str.getBytes(StandardCharsets.UTF_8));
        return String.format("%032x", new BigInteger(1, digest));
    }
    public static byte [] getBucketId(byte [] key, Integer bit) throws NoSuchAlgorithmException {

        MessageDigest mdInst = MessageDigest.getInstance("MD5");

        mdInst.update(key);

        byte [] md = mdInst.digest();

        byte [] r = new byte[(bit-1)/7 + 1];// 因为一个字节中只有7位能够表示成单字符，ascii码是7位

        int a = (int) Math.pow(2, bit%7)-2;

        md[r.length-1] = (byte) (md[r.length-1] & a);

        System.arraycopy(md, 0, r, 0, r.length);

        for(int i=0;i<r.length;i++) {

            if(r[i]<0) {
                r[i] &= 127;
            }
        }
        return r;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        byte[] id = getBucketId("123".getBytes(), 32);
        for (byte b : id) {
            System.out.println((char) b);
        }
        System.out.println("id = " + Arrays.toString(id));
    }
}
