package kim.nzxy.robin.sample.web.common.exception;

import kim.nzxy.robin.exception.RobinException;
import kim.nzxy.robin.sample.web.common.res.Res;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一异常处理
 *
 * @author xy
 */
@Slf4j
@RestControllerAdvice
public class LyExceptionHandler {
    /**
     * robin异常拦截
     */
    @ExceptionHandler(RobinException.Panic.class)
    public Res<?> handler(RobinException.Panic e) {
        log.error("robin config has error: {}", e.getError());
        return Res.fail("系统配置有点问题, 问题原因我就不告诉你了");
    }

    /**
     * robin异常拦截
     */
    @ExceptionHandler(RobinException.Verify.class)
    public Res<?> handler(RobinException.Verify e) {
        log.error("robin reject: {}, {}", e.getError(), e.getTarget());
        return Res.fail("系统决定禁止你访问");
    }

    /**
     * robin异常拦截
     */
    @ExceptionHandler(LyException.class)
    public Res<?> handler(LyException e) {
        return Res.fail(e.getMessage());
    }
}
