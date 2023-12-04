package kim.nzxy.robin.enums;

import kim.nzxy.robin.exception.RobinException;
import kim.nzxy.robin.util.RobinDigestUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 摘要策略
 *
 * @author ly-chn
 */
@AllArgsConstructor
@Getter
public enum RobinStrCodecEnum {
    /**
     * md5摘要
     */
    MD5(false) {
        @Override
        public String encode(String input) {
            return RobinDigestUtil.md5(input);
        }
    },
    /**
     * 64进制, 支持的最大值为{@link java.lang.Long#MAX_VALUE}, 最小值为{@link java.lang.Long#MIN_VALUE}
     * 返回值为{@code 0-9a-zA-Z}
     */
    Num64(true) {
        @Override
        public String encode(String input) {
            long l = Long.parseLong(input.trim());
            return RobinDigestUtil.numTo64(l);
        }
    },
    /**
     * String hashCode后转64进制 方法
     */
    HashCode64(false) {
        @Override
        public String encode(String input) {
            long l = input.hashCode();
            return RobinDigestUtil.numTo64(l);
        }
    },
    /**
     * IPv4地址转64进制
     */
    IPv4Num64(true) {
        @Override
        public String encode(String input) {
            return RobinDigestUtil.ipv4To64(input);
        }

        @Override
        public String decode(String digest) {
            return RobinDigestUtil.num64ToIpv4(digest);
        }
    };

    /**
     * 是否可逆
     */
    private final Boolean reversible;

    /**
     * 编码
     *
     * @param input 要编码的值
     * @return 编码结果
     */
    public abstract String encode(String input);

    /**
     * 解码
     *
     * @param digest 编码后的内容
     * @return 解码结果
     */
    public String decode(String digest) {
        throw new RobinException.Panic(RobinExceptionEnum.Panic.StrCodecIrreversible);
    }
}
