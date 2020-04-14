package com.jxyzh11.springbootdemo.api.controller;

import com.alibaba.fastjson.JSONObject;
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

    @GetMapping(value = "get")
    public JSONObject get(String id) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        return jsonObject;
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
