package com.jxyzh11.springbootdemo.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.jxyzh11.springbootdemo.config.exception.constants.ResponseEnum;
import com.jxyzh11.springbootdemo.config.redis.RedisService;
import com.jxyzh11.springbootdemo.entity.User;
import com.jxyzh11.springbootdemo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private RedisService redisService;

    @RequestMapping(value = "get")
    public User get(User user) throws Exception {
        ResponseEnum.ID_IS_NULL.assertNotNull(user.getId());
        user = userService.get(user);
        redisService.setObject("user." + user.getId(), user.getName(), 7200L);
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
