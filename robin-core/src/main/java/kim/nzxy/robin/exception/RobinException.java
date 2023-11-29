package kim.nzxy.robin.exception;

import kim.nzxy.robin.metadata.RobinMetadata;
import kim.nzxy.robin.enums.RobinExceptionEnum;
import lombok.Getter;

/**
 * @author ly-chn
 * @since 2021/6/4
 */
@Getter
public class RobinException extends RuntimeException {
    /**
     * 异常类型
     */
    private final RobinExceptionEnum type;
    /**
     * 异常附加信息
     */
    private final RobinMetadata metadata;

    private RobinException(RobinExceptionEnum type, RobinMetadata metadata, Throwable error) {
        super(type.toString(), error);
        this.type = type;
        this.metadata = metadata;
    }

    /**
     * 严重异常，将导致robin无法正常运行
     */
    public static class Panic extends RobinException {
        public Panic(RobinExceptionEnum.Panic type) {
            super(type, null, null);
        }
        public Panic(RobinExceptionEnum.Panic type, Throwable error) {
            super(type, null, error);
        }
    }

    /**
     * 校验异常, 校验过程中的异常
     */
    public static class Verify extends RobinException {
        public Verify(RobinExceptionEnum.Verify type, RobinMetadata target) {
            super(type, target, null);
        }
        public Verify(RobinExceptionEnum.Verify type, RobinMetadata target, Throwable error) {
            super(type, target, error);
        }
    }
}
