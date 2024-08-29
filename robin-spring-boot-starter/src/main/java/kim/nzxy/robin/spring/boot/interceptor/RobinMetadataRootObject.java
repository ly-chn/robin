package kim.nzxy.robin.spring.boot.interceptor;

import lombok.Data;
import lombok.Getter;
import org.springframework.cache.Cache;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * @author ly-chn
 */
@Getter
public class RobinMetadataRootObject {
    private final Method method;
    private final Object[] args;
    private final Object target;
    private final Class<?> targetClass;

    public RobinMetadataRootObject( Method method, Object[] args, Object target, Class<?> targetClass) {
        this.method = method;
        this.target = target;
        this.targetClass = targetClass;
        this.args = args;
    }
}
