package kim.nzxy.robin.autoconfigure;

import lombok.Data;

/**
 * robin 验证器-拓展配置基类，如有需要自行拓展
 *
 * @author xuyf
 * @since 2022/8/31 15:51
 */
@SuppressWarnings("AlibabaAbstractClassShouldStartWithAbstractNaming")
@Data
public abstract class ValidatorConfig {
    /**
     * 基础配置
     */
    private RobinBasicStrategy basic;
    /**
     * 其他配置
     */
    private Object config;
}
