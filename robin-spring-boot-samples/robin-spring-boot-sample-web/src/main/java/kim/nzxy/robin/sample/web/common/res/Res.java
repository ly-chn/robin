package kim.nzxy.robin.sample.web.common.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Res<T> implements Serializable {

    private static final String INVALID_PARAM = "参数不合法";
    private static final String DELETE_OK = "已删除";
    private static final String DELETE_ERR = "删除失败, 可能原因: 已被删除";
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
    private List<T> list;

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
    public static <T> Res<T> success(List<T> list) {
        Res<T> msg = new Res<>();
        msg.setCode(200);
        msg.setList(list);
        return msg;
    }

    /**
     * 返回参数异常
     * 例如必填参数为空
     *
     * @return 统一封装返回参数不合法的提示
     */
    public static <T> Res<T> invalidParam() {
        Res<T> msg = new Res<>();
        msg.setCode(401);
        msg.setMessage(INVALID_PARAM);
        return msg;
    }

    /**
     * 返回参数异常
     * 建议用于请求参数校验(如编码不得重复)时发现的异常
     *
     * @param message 自定义返回信息
     * @return 统一封装返回参数不合法的提示
     */
    public static <T> Res<T> invalidParam(String message) {
        Res<T> msg = new Res<>();
        msg.setCode(401);
        msg.setMessage(message);
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