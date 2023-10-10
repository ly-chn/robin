package kim.nzxy.robin.util;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

class RobinDigestUtilTest {

    @Test
    void numTo64() {
        for (int i = 0; i < 1000000; i++) {
            String s = RobinDigestUtil.numTo64(i);
            long l = RobinDigestUtil.parse64(s);
            Assertions.assertEquals(i, l);
        }
    }

    @Test
    void ipv4ToLong() {
        Random random = new Random();
        for (int i = 0; i < 10000; i++) {
            String ipv4 = random.nextInt(256) + "." +
                    random.nextInt(256) + "." +
                    random.nextInt(256) + "." +
                    random.nextInt(256);
            String ipv4To64 = RobinDigestUtil.ipv4To64(ipv4);
            String result = RobinDigestUtil.num64ToIpv4(ipv4To64);
            Assertions.assertEquals(ipv4, result);
        }
    }
}