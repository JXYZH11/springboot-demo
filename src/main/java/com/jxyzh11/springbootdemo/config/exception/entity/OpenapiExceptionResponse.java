package com.jxyzh11.springbootdemo.config.exception.entity;

/**
 * 异常返回实体类
 *
 * @ClassName: ResultBody
 * @Description: TODO
 * @Author: JXYZH11
 * @Date: 2020/4/15 14:59
 * @Version: 1.0
 */
public class OpenapiExceptionResponse {

    /**
     * 返回码
     */
    private int error_no;
    /**
     * 返回消息
     */
    private String error_desc;
    /**
     * 返回code
     */
    private String error_code;

    public OpenapiExceptionResponse(int error_no, String error_desc, String error_code) {
        this.error_no = error_no;
        this.error_desc = error_desc;
        this.error_code = error_code;
    }

    public int getError_no() {
        return error_no;
    }

    public void setError_no(int error_no) {
        this.error_no = error_no;
    }

    public String getError_desc() {
        return error_desc;
    }

    public void setError_desc(String error_desc) {
        this.error_desc = error_desc;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }
}
