package kim.nzxy.robin.validator;

import kim.nzxy.robin.config.RobinManagement;
import kim.nzxy.robin.enums.RobinRuleEnum;
import kim.nzxy.robin.validator.util.ValidatorUtil;
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
        val ip = RobinManagement.getContextHandler().ip();
        ValidatorUtil.validateFrequentAccess(
                ip,
                ip,
                RobinRuleEnum.FREQUENT_IP_ACCESS,
                RobinManagement.getRobinProperties().getIpFrequentAccess());
    }
}
