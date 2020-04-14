package com.jxyzh11.springbootdemo.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.jxyzh11.springbootdemo.entity.User;
import com.jxyzh11.springbootdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: ApiController
 * @Description: TODO
 * @Author: JXYZH11
 * @Date: 2020/4/14 11:13
 * @Version: 1.0
 */
@RestController
@RequestMapping(value = "api")
public class ApiController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "get")
    public User get(User user) {
        user = userService.get(user);
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
