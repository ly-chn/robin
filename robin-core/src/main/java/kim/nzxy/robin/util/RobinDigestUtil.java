package kim.nzxy.robin.util;

import kim.nzxy.robin.config.RobinManagement;
import kim.nzxy.robin.enums.RobinBuiltinErrEnum;
import kim.nzxy.robin.exception.RobinBuiltinException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
     * @param str 目标字符串
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
        byte[] digest = md.digest(str.getBytes(StandardCharsets.UTF_8));
        String result = new String(digest);
        RobinManagement.getCacheHandler();
        return result;
    }

    /**
     * 将摘要消息还原为原本的消息
     *
     * @return 还原消息
     */
    public String nurture(String digest) {
        // todo: 还原消息

        return RobinManagement.getCacheHandler().toString();
    }
}
