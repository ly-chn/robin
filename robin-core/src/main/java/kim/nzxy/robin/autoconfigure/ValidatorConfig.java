package kim.nzxy.robin.autoconfigure;

/**
 * robin 验证器-拓展配置基类，如有需要自行拓展
 *
 * @author xuyf
 * @since 2022/8/31 15:51
 */
public interface ValidatorConfig {

    /**
     * 读取基础配置
     *
     * @return 基础配置
     */
    RobinBasicStrategy getBasic();

    /**
     * 读取拓展配置
     *
     * @return 拓展配置
     */
    Object getConfig();
}
