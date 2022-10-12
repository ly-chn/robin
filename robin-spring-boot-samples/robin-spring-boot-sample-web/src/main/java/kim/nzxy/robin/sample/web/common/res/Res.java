package kim.nzxy.robin.sample.web.common.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Res<T> implements Serializable {

    private static final String INVALID_PARAM = "参数不合法";
    /**
     * 返回处理消息
     */
    private String message = "操作成功！";

    /**
     * 返回代码
     */
    private Integer code = 200;

    /**
     * 返回数据对象 data
     */
    private T data;

    /**
     * 返回列表信息
     */
    private Collection<T> list;

    /**
     * 成功响应并返回信息
     *
     * @param message 提示信息
     * @param data    返回的data数据
     * @return 统一封装返回单条成功数据
     */
    public static <T> Res<T> success(T data, String message) {
        Res<T> msg = new Res<T>();
        msg.setCode(200);
        msg.setMessage(message);
        msg.setData(data);
        return msg;
    }

    /**
     * 成功响应并返回信息
     *
     * @param data 返回的data数据
     * @return 统一封装返回单条成功数据
     */
    public static <T> Res<T> success(T data) {
        Res<T> msg = new Res<>();
        msg.setCode(200);
        msg.setMessage("操作成功");
        msg.setData(data);
        return msg;
    }

    /**
     * 成功响应并返回指定提示信息
     *
     * @param message 提示信息
     * @return 统一封装返回提示信息
     */
    public static <T> Res<T> success(String message) {
        Res<T> msg = new Res<>();
        msg.setCode(200);
        msg.setMessage(message);
        msg.setData(null);
        return msg;
    }

    /**
     * 成功响应并返回指定提示信息
     *
     * @return 统一封装返回操作成功
     */
    public static <T> Res<T> success() {
        return new Res<>();
    }

    /**
     * 成功响应并返回信息
     *
     * @param list 返回的data数据
     * @return 统一封装返回单条成功数据
     */
    public static <T> Res<T> success(Collection<T> list) {
        Res<T> msg = new Res<>();
        msg.setCode(200);
        msg.setList(list);
        return msg;
    }

    /**
     * 操作失败的提示
     *
     * @param message 自定义返回信息
     * @return 统一封装操作失败的提示
     */
    public static <T> Res<T> fail(String message) {
        Res<T> msg = new Res<>();
        msg.setCode(500);
        msg.setMessage(message);
        return msg;
    }

    /**
     * 登录验证失败
     *
     * @return 统一封装操作失败的提示
     */
    public static <T> Res<T> failLogin(String message) {
        Res<T> msg = new Res<>();
        msg.setCode(501);
        msg.setMessage(message);
        return msg;
    }
}