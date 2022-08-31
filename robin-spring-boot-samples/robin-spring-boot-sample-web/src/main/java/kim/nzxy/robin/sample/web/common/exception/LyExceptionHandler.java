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
    @ExceptionHandler(RobinException.class)
    public Res<?> handler(RobinException e) {
        log.error(e.getError().toString());
        log.error("{}",e.getTarget());
        return Res.fail(e.getError() + ": " + e.getTarget());
    }

    /**
     * robin异常拦截
     */
    @ExceptionHandler(LyException.class)
    public Res<?> handler(LyException e) {
        return Res.fail(e.getMessage());
    }
}
