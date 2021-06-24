package kim.nzxy.robin.validator;

import kim.nzxy.robin.config.RobinManagement;
import kim.nzxy.robin.enums.RobinRuleEnum;
import kim.nzxy.robin.util.Assert;
import kim.nzxy.robin.util.MatcherUtil;
import lombok.val;

/**
 * Ip地址黑名单检测
 *
 * @author xy
 * @since 2021/6/6
 */
public class IpBlocklistValidator implements RobinValidator {
    @Override
    public void execute() {
        val ip = RobinManagement.getContextHandler().ip();
        Assert.assertRobinException(
                MatcherUtil.str(ip, RobinManagement.getRobinProperties().getBlackWhiteList().getIp().getBlocklist()),
                RobinRuleEnum.BLOCKLIST_IP_ADDRESS,
                ip
        );
    }
}
