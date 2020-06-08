package com.jxyzh11.springbootdemo.config.exception.constants;

import com.jxyzh11.springbootdemo.config.exception.OpenapiExceptionAssert;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 服务级别异常常数
 */
@Getter
@AllArgsConstructor
public enum OpenapiAssertEnum implements OpenapiExceptionAssert {
    param_validate(100, "{0}", "ximalaya.common.request_param_check_failed"),
    SIG_ERROR(301, "sig is not a valid value", "ximalaya.oauth2.server_authenticate_failed"),
    SERVER_ERROR(502,"unknown server error","ximalaya.system.unknown_server_error"),
    ;

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
}
