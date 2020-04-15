package com.jxyzh11.springbootdemo.config.exception;

/**
 * 系统级别错误码
 */
public enum SystemErrorCodeEnum implements ErrorCodeInterface {
    SUCCESS(0, "成功"),
    EXCEPTION(1,"服务器异常")
    ;

    private Integer code;
    private String message;

    SystemErrorCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
