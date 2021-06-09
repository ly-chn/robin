package kim.nzxy.robin.handler.impl;

import kim.nzxy.robin.config.RobinManagement;
import kim.nzxy.robin.enums.RobinRuleEnum;
import kim.nzxy.robin.exception.RobinException;
import kim.nzxy.robin.factory.RobinValidFactory;
import kim.nzxy.robin.handler.RobinValidator;
import kim.nzxy.robin.util.MatcherUtil;
import lombok.val;

/**
 * Ip地址黑名单检测
 *
 * @author xy
 * @since 2021/6/6
 */
public class IpBlacklistValidatorImpl extends RobinValidator {
    @Override
    public void execute() {
        val ipProp = RobinManagement.getRobinProperties().getIp();
        val contextHandler = RobinManagement.getContextHandler();
        val ip = contextHandler.ip();
        for (String s : ipProp.getBlacklist()) {
            if (MatcherUtil.str(ip, s)) {
                throw new RobinException(RobinRuleEnum.BLACKLIST_IP_ADDRESS, ip);
            }
        }

    }
}
