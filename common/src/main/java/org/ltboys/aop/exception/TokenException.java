package org.ltboys.aop.exception;

import java.util.ArrayList;
import java.util.List;

public class TokenException extends RuntimeException {
    private int code;

    private String message;

    private Object[] args;
    public TokenException(int messageCode) {
        this.code = messageCode;
    }
    public TokenException(String message) {
        this.message = message;
    }
    public TokenException(int messageCode,Object... args) {
        this.code = messageCode;
        this.args = args;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
