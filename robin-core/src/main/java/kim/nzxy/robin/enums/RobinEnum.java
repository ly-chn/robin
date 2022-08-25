package kim.nzxy.robin.enums;

/**
 * 规范部分enum命名
 *
 * @author xy
 * @since 2021/6/5
 */
public interface RobinEnum {
    /**
     * enum对应编码
     *
     * @return 编码
     */
    int getCode();

    /**
     * 用户提示信息，非语义化
     *
     * @return 非语义化信息
     */
    String getMessage();
}
