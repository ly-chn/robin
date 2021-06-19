package kim.nzxy.robin.validator;

import kim.nzxy.robin.util.CacheAbleUtil;
import kim.nzxy.robin.config.RobinManagement;
import kim.nzxy.robin.enums.RobinRuleEnum;
import kim.nzxy.robin.util.Assert;
import kim.nzxy.robin.util.RobinUtil;
import lombok.val;

/**
 * IP 访问频率限制
 *
 * @author xy
 * @since 2021/6/4
 */
public class FrequentIpAccessValidator implements RobinValidator {
    @Override
    public void execute() {
        if (CacheAbleUtil.ipInBlackList()) {
            return;
        }

        val cacheHandler = RobinManagement.getCacheHandler();
        val contextHandler = RobinManagement.getContextHandler();
        // 判断是否已被封禁
        val ipProp = RobinManagement.getRobinProperties().getIpFrequentAccess();
        val ip = contextHandler.ip();
        // 断言已被禁用
        Assert.assertRobinException(cacheHandler.lock(RobinRuleEnum.FREQUENT_IP_ACCESS, ip), RobinRuleEnum.FREQUENT_IP_ACCESS, ip);

        val now = RobinUtil.now();

        val recentVisitsCount = cacheHandler.getAccessRecord(RobinRuleEnum.FREQUENT_IP_ACCESS, ip)
                .stream()
                .filter(it -> it > now)
                .count();

        Assert.assertRobinException(
                recentVisitsCount >= ipProp.getFrequency(),
                RobinRuleEnum.FREQUENT_IP_ACCESS,
                ip,
                () -> cacheHandler.lock(RobinRuleEnum.FREQUENT_IP_ACCESS, ip, ipProp.getUnlock()));

        cacheHandler.accessRecord(RobinRuleEnum.FREQUENT_IP_ACCESS, ip, now + (int) ipProp.getDuration().getSeconds());
    }
}
