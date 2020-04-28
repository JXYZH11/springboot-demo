package com.jxyzh11.springbootdemo.config.socket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * 开启WebSocket支持
 *
 * @ClassName: WebSocketConfig
 * @Description: TODO
 * @Author: JXYZH11
 * @Date: 2020/4/21 14:28
 * @Version: 1.0
 */
@Configuration
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
