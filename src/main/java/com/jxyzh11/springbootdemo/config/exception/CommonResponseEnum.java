package com.jxyzh11.springbootdemo.config.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonResponseEnum implements GlobalExceptionAssert{

    SERVER_ERROR(10002, "服务器异常")
    ;

    /**
     * 返回码
     */
    private int code;
    /**
     * 返回消息
     */
    private String message;
}
