package kim.nzxy.robin.validator;

import kim.nzxy.robin.config.RobinManagement;
import kim.nzxy.robin.enums.RobinRuleEnum;
import kim.nzxy.robin.util.RobinUtil;
import kim.nzxy.robin.validator.util.ValidatorUtil;
import lombok.val;

/**
 * ua-IP 访问频率限制
 *
 * @author xy
 * @since 2021/6/4
 */
public class FrequentUaIpAccessValidator implements RobinValidator {
    @Override
    public void execute() {
        val contextHandler = RobinManagement.getContextHandler();
        val ip = contextHandler.ip();
        ValidatorUtil.validateFrequentAccess(
                RobinUtil.md5(ip + contextHandler.ua()),
                ip,
                RobinRuleEnum.FREQUENT_IP_ACCESS,
                RobinManagement.getRobinProperties().getIpFrequentAccess());
    }
}
