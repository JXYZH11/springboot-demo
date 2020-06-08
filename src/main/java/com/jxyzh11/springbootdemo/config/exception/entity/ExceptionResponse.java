package com.jxyzh11.springbootdemo.config.exception.entity;

import com.alibaba.fastjson.JSONObject;

/**
 * 异常返回实体类
 *
 * @ClassName: ResultBody
 * @Description: TODO
 * @Author: JXYZH11
 * @Date: 2020/4/15 14:59
 * @Version: 1.0
 */
public class ExceptionResponse {

    public Integer code;
    private String msg;
    private Object result = new JSONObject();

    public ExceptionResponse(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
