package kim.nzxy.robin.handler;

import kim.nzxy.robin.config.RobinManagement;
import kim.nzxy.robin.enums.RobinBuiltinErrEnum;
import kim.nzxy.robin.enums.RobinRuleEnum;
import kim.nzxy.robin.exception.RobinBuiltinException;
import lombok.val;

/**
 * todo: interface or abstract class, will another method appear?
 *
 * @author xy
 * @since 2021/6/4
 */
public abstract class RobinValidator {
    /**
     * 执行验证
     */
    abstract public void execute();

    /**
     * 获取权重, 权重越小越靠前, 自定义内容重写时 rule 通通为 null
     * @return 排序/权重, 越小越靠前
     */
    public int getOrder() {
        return 0;
    }
}
