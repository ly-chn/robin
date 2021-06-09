package kim.nzxy.robin.handler.impl;

import kim.nzxy.robin.config.RobinManagement;
import kim.nzxy.robin.enums.RobinRuleEnum;
import kim.nzxy.robin.handler.RobinValidator;
import kim.nzxy.robin.util.Assert;
import kim.nzxy.robin.util.MatcherUtil;
import kim.nzxy.robin.util.RobinUtil;
import lombok.val;

/**
 * IP 访问频率限制
 *
 * @author xy
 * @since 2021/6/4
 */
public class FrequentIpAccessValidatorImpl extends RobinValidator {
    @Override
    public void execute() {
        val cacheHandler = RobinManagement.getCacheHandler();
        val contextHandler = RobinManagement.getContextHandler();
        // 判断是否已被封禁
        val ipProp = RobinManagement.getRobinProperties().getIp();
        val ip = contextHandler.ip();

        // 白名单
        for (String s : ipProp.getWhitelist()) {
            if (MatcherUtil.str(ip, s)) {
                return;
            }
        }

        // 断言已被禁用
        Assert.assertRobinException(cacheHandler.lockIp(ip), RobinRuleEnum.FREQUENT_IP_ACCESS, ip);

        val now = RobinUtil.now();

        val recentVisitsCount = cacheHandler.ipAccessRecord(ip)
                .stream()
                .filter(it -> it > now - ipProp.getDuration().getSeconds())
                .count();

        Assert.assertRobinException(
                recentVisitsCount > ipProp.getFrequency(),
                RobinRuleEnum.FREQUENT_IP_ACCESS,
                ip,
                () -> cacheHandler.lockIp(ip, ipProp.getUnlock()));

        cacheHandler.ipAccessRecord(ip, now);
    }
}
