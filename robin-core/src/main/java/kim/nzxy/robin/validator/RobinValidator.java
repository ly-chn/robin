package kim.nzxy.robin.validator;

/**
 * todo: interface or abstract class, will another method appear?
 *
 * @author xy
 * @since 2021/6/4
 */
public interface RobinValidator {
    /**
     * 执行验证
     */
    void execute();

    /**
     * 获取权重, 权重越小越靠前, 后续可能添加注解解析
     *
     * @return 排序/权重, 越小越靠前
     */
    default int getOrder() {
        return Integer.MIN_VALUE;
    }
}
