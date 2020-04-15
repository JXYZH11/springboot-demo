package com.jxyzh11.springbootdemo.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.jxyzh11.springbootdemo.UserErrorCodeEnum;
import com.jxyzh11.springbootdemo.config.exception.GlobalException;
import com.jxyzh11.springbootdemo.config.exception.SystemErrorCodeEnum;
import com.jxyzh11.springbootdemo.entity.User;
import com.jxyzh11.springbootdemo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.processing.SupportedAnnotationTypes;

/**
 * @ClassName: ApiController
 * @Description: TODO
 * @Author: JXYZH11
 * @Date: 2020/4/14 11:13
 * @Version: 1.0
 */
@Slf4j
@RestController
@RequestMapping(value = "api")
public class ApiController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "get")
    public User get(User user) throws GlobalException {
        if (user.getId() == null) {
            throw new GlobalException(UserErrorCodeEnum.id_not_be_null);
        }
        try {
            user = userService.get(user);
        } catch (Exception e) {
            log.error("e", e);
            throw new GlobalException(SystemErrorCodeEnum.EXCEPTION);
        }
        return user;
    }

    @PostMapping(value = "post")
    public JSONObject post(String id) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        return jsonObject;
    }

    @PostMapping(value = "postJson")
    public JSONObject postJson(@RequestBody JSONObject jsonObject) {
        return jsonObject;
    }
}
