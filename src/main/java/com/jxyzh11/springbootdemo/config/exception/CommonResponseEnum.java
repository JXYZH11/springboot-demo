package com.jxyzh11.springbootdemo.config.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonResponseEnum {

    /**
     * Bad licence type
     */
    SERVER_ERROR(7001, "Bad licence type.");

    /**
     * 返回码
     */
    private int code;
    /**
     * 返回消息
     */
    private String message;
}
