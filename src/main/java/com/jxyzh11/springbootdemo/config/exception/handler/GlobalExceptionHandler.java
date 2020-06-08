package com.jxyzh11.springbootdemo.config.exception.handler;

import com.alibaba.fastjson.JSONObject;
import com.jxyzh11.springbootdemo.common.entity.ApiResponseCode;
import com.jxyzh11.springbootdemo.common.utils.RequestUtil;
import com.jxyzh11.springbootdemo.config.exception.constants.AssertEnum;
import com.jxyzh11.springbootdemo.config.exception.constants.OpenapiAssertEnum;
import com.jxyzh11.springbootdemo.config.exception.entity.ExceptionResponse;
import com.jxyzh11.springbootdemo.config.exception.entity.GlobalException;
import com.jxyzh11.springbootdemo.config.exception.entity.OpenapiExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局统一异常处理
 *
 * @ClassName: GlobalExceptionHandler
 * @Description: TODO
 * @Author: JXYZH11
 * @Date: 2020/4/15 10:44
 * @Version: 1.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = GlobalException.class)
    public Object handleException(HttpServletRequest request, HttpServletResponse response, GlobalException e) {
        JSONObject requestObject = logRequest(request);
        log.error(e.getMessage(), e);

        if (e.getResponseV3Enum() != null) {
            response.setStatus(ApiResponseCode.param_validate_failed);
            return new OpenapiExceptionResponse(e.getResponseV3Enum().getError_no(), e.getMessage(), e.getResponseV3Enum().getError_code());
        }
        return new ExceptionResponse(e.getResponseEnum().getCode(), e.getMessage());
    }

    /**
     * 未定义异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(value = Exception.class)
    public Object handleException(HttpServletRequest request, HttpServletResponse response, Exception e) {
        JSONObject requestObject = logRequest(request);
        log.error(e.getMessage(), e);

        if (request.getRequestURI().contains("/openapi-fmxos/")) {
            response.setStatus(ApiResponseCode.unknown_server_error);
            return new OpenapiExceptionResponse(OpenapiAssertEnum.SERVER_ERROR.getError_no(), OpenapiAssertEnum.SERVER_ERROR.getError_desc(), OpenapiAssertEnum.SERVER_ERROR.getError_code());
        }
        return new ExceptionResponse(AssertEnum.SERVER_ERROR.getCode(), AssertEnum.SERVER_ERROR.getMessage());
    }

    /**
     * 获取请求信息
     *
     * @param request
     * @return 字符串
     */
    public static JSONObject logRequest(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String method = request.getMethod();
        String url = request.getRequestURI();
        String params = RequestUtil.createLinkString(RequestUtil.toVerifyMap(request.getParameterMap()));
        log.error("{}:{}?{}", method, url, params);
        jsonObject.put("method", method);
        jsonObject.put("url", url);
        jsonObject.put("params", params);
        return jsonObject;
    }
}
