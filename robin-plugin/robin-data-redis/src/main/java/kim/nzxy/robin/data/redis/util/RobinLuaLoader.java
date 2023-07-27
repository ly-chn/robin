package kim.nzxy.robin.data.redis.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 * 加载lua脚本
 * @author ly-chn
 */
public class RobinLuaLoader {
    /**
     * 脚本位置: classpath: /robin-lua/*.lua
     *
     * @param filename 无需后缀, 只要文件名即可
     */
    public static DefaultRedisScript<Boolean> file(String filename) {
        DefaultRedisScript<Boolean> script = new DefaultRedisScript<>();
        script.setScriptSource(new ResourceScriptSource(new ClassPathResource("robin-lua/" + filename + ".lua")));
        script.setResultType(Boolean.class);
        return script;
    }
}
