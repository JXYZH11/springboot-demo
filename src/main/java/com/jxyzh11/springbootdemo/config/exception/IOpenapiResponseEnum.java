package com.jxyzh11.springbootdemo.config.exception;

/**
 * 异常常数方法接口
 */
public interface IOpenapiResponseEnum {

    int getError_no();

    String getError_code();

    String getError_desc();
}
