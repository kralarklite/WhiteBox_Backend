package org.ltboys.action;

public enum ResultCode {
    SUCCESS(1, "操作成功"),
    FAILURE(-1, "操作失败"),

    NO_AUTH(5559, "凭证过期或不存在");

    private Integer code;
    private String msg;

    private ResultCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }
}
