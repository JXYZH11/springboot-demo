package com.jxyzh11.springbootdemo.config.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;

/**
 * 统一异常处理
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
    public ExceptionResponse handleException(HttpServletRequest request, GlobalException e) {
        logErrorRequest(request);
        log.error(e.getMessage(), e);
        return new ExceptionResponse(e.getResponseEnum().getCode(), e.getMessage());
    }

    /**
     * 未定义异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(value = Exception.class)
    public ExceptionResponse handleException(HttpServletRequest request, Exception e) {
        logErrorRequest(request);
        log.error(e.getMessage(), e);
        return new ExceptionResponse(CommonResponseEnum.SERVER_ERROR.getCode(), CommonResponseEnum.SERVER_ERROR.getMessage());
    }

    /**
     * 获取POST请求中Body参数
     *
     * @param request
     * @return 字符串
     */
    public static void logErrorRequest(HttpServletRequest request) {
        try {
            if ("GET".equals(request.getMethod())) {
                String paramsString = URLDecoder.decode(request.getQueryString(), "UTF-8");
                log.error(request.getRequestURI() + "?" + paramsString);
            }
            if ("POST".equals(request.getMethod())) {
                log.error(request.getRequestURI());
                InputStream is = request.getInputStream();
                String contentStr = IOUtils.toString(is, "utf-8");
                String[] params = contentStr.split("&");
                for (String param : params) {
                    if (param.split("=").length == 2) {
                        request.setAttribute(param.split("=")[0], param.split("=")[1]);
                        log.error(param.split("=")[0] + ":" + URLDecoder.decode(param.split("=")[1], "UTF-8"));
                    } else {
                        request.setAttribute(param.split("=")[0], "");
                        log.error(param.split("=")[0] + ":");
                    }
                }
            }
        } catch (IOException e) {
            log.error("error", e);
        }
    }
}
