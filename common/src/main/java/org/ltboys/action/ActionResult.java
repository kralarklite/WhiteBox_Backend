package org.ltboys.action;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

@Data
public class ActionResult<T> implements Serializable {

    /**
     * 返回码
     */
    private Integer code;

    /**
     * 返回说明
     */
    private String msg;

    private T data;

    public ActionResult(T data) {
        this.code = 1;
        this.msg = "操作成功";
        this.data = data;
    }

    public ActionResult(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
    }

    public ActionResult(ResultCode resultCode, String msg) {
        this.code = resultCode.getCode();
        this.msg = msg;
    }

    public ActionResult(ResultCode resultCode, T data) {
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
        this.data = data;
    }


    public static ActionResult success() {
        return new ActionResult(ResultCode.SUCCESS);
    }

    public static ActionResult success(Object data) {
        return new ActionResult(data);
    }

    public static ActionResult success(String msg) {
        return new ActionResult(ResultCode.SUCCESS, msg);
    }

    public static ActionResult success(String key, Object value) {
        JSONObject json = new JSONObject();
        json.put(key, value);
        return new ActionResult(ResultCode.SUCCESS, json);
    }

    public static ActionResult failure() {
        return new ActionResult(ResultCode.FAILURE);
    }

    public static ActionResult failure(String msg) {
        return new ActionResult(ResultCode.FAILURE, msg);
    }

    public static ActionResult no_token() {
        return new ActionResult(ResultCode.NO_TOKEN);
    }

    public static ActionResult no_token(String msg) {
        return new ActionResult(ResultCode.NO_TOKEN, msg);
    }
}
