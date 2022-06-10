package kim.nzxy.robin.validator.util;

import kim.nzxy.robin.autoconfigure.RobinProperties;
import kim.nzxy.robin.config.RobinManagement;
import kim.nzxy.robin.enums.RobinRuleEnum;
import kim.nzxy.robin.util.RobinAssert;
import kim.nzxy.robin.util.CacheAbleUtil;
import kim.nzxy.robin.util.RobinUtil;
import lombok.val;

/**
 * @author xy
 * @since 2021/6/23
 */
public class ValidatorUtil {
    /**
     * 通用验证频繁访问
     * 通过判断mark是否被多次记录来决定是否封禁target
     *
     * @param mark    目标标志字符
     * @param target  目标
     * @param rule    校验所属规则
     * @param control 控制器
     */
    public static void validateFrequentAccess(String mark, String target, RobinRuleEnum rule, RobinProperties.FrequencyControl control) {
        if (CacheAbleUtil.ipInWhitelist()) {
            return;
        }

        val cacheHandler = RobinManagement.getCacheHandler();
        // 断言已被禁用
        RobinAssert.assertLocked(rule, target);

        val now = RobinUtil.now();

        val recentVisitsCount = cacheHandler.getAccessRecord(rule, mark)
                .stream()
                .filter(it -> it > now)
                .count();

        RobinAssert.assertRobinException(
                recentVisitsCount >= control.getFrequency(),
                rule,
                target,
                () -> cacheHandler.lock(rule, target, control.getUnlock()));

        cacheHandler.accessRecord(rule, mark, now + (int) control.getDuration().getSeconds());
    }
}
