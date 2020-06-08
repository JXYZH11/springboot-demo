package com.jxyzh11.springbootdemo.common.entity;

public class ApiResponseCode {
    //业务参数缺少
    public final static Integer param_validate_failed = 202;
    //OAuth2签名验证失败
    public final static Integer oauth2_authenticate_failed = 208;
    //未知服务器错误
    public final static Integer unknown_server_error = 502;
}
