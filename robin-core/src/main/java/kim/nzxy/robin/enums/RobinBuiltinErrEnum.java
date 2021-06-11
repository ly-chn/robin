package kim.nzxy.robin.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xy
 * @since 2021/6/4
 */
@AllArgsConstructor
@Getter
public enum RobinBuiltinErrEnum implements RobinEnum {

    METHOD_NOT_IMPLEMENTED_YET(1000, "方法尚未实现"),
    MODE_NOT_IMPLEMENTED_YET(1001, "模式尚未实现"),
    SOMETHING_NOT_REGISTER_YET(1002, "尚未注册的内容"),
    NOT_ORDERED_VALIDATOR(1003, "自定义验证器必须重写 getOrder 方法"),
    ERR_CONFIG_LOG(2001, "日志配置错误"),
    ;
    private final int code;
    private final String message;
}
