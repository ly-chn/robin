package kim.nzxy.robin.handler;

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
     * 获取权重, 权重越小越靠前
     *
     * @return 排序/权重, 越小越靠前
     */
    public int getOrder() {
        return -1;
    }
}
