package kim.nzxy.robin.sample.web.common.res;

import kim.nzxy.robin.exception.RobinException;
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
     * 计算异常, 如将long转为int时, 超出int范围
     */
    @ExceptionHandler(RobinException.class)
    public Res<?> handlerArithmeticException(RobinException e) {
        log.error(e.getError().toString());
        log.error(e.getTarget());
        return Res.fail(e.getError().getMessage() + ": " + e.getTarget());
    }
}
