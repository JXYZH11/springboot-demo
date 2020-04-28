package com.jxyzh11.springbootdemo.config.socket;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName: WebSocketServer
 * @Description: TODO
 * @Author: JXYZH11
 * @Date: 2020/4/21 14:30
 * @Version: 1.0
 */
@Slf4j
@Component
@ServerEndpoint("/mysocket/{deviceId}")
public class WebSocketServer {

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的
    private static long onlineCount = 0L;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象
    private static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    //接收deviceId
    private String deviceId;

    /**
     * 连接建立成功调用的方法
     *
     * @param session
     * @param deviceId
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("deviceId") String deviceId) {
        this.session = session;
        this.deviceId = deviceId;
        if (webSocketMap.containsKey(deviceId)) {
            webSocketMap.remove(deviceId);
            webSocketMap.put(deviceId, this);
        } else {
            webSocketMap.put(deviceId, this);
            addOnlineCount();
        }
        log.info("设备连接:{},当前在线设备为:{}", deviceId, getOnlineCount());

        try {
            sendMessage("连接成功");
        } catch (Exception e) {
            log.error("设备：{}，网络异常！！！", deviceId);
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if (webSocketMap.contains(deviceId)) {
            webSocketMap.remove(deviceId);
            //从set中删除
            subOnlineCount();
        }
        log.info("设备退出：{}，当前在线设备为：{}", deviceId, getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("设备消息：{}，报文：{}", deviceId, message);
        //可以群发消息
        //消息保存到数据库，redis
        if (StringUtils.isNotBlank(message)) {
            try {
                //解析发送的报文
                JSONObject jsonObject = JSONObject.parseObject(message);
                //追加发送人(防止串改)
                String fromDeviceId = jsonObject.getString("fromDeviceId");
                //传送给对应用户的websocket
                if (webSocketMap.containsKey(fromDeviceId)) {
                    webSocketMap.get(this.deviceId).sendMessage(jsonObject.toJSONString());
                } else {
                    log.error("请求的deviceId:{}不在该服务器上", deviceId);
                }
            } catch (Exception e) {
                log.error("onMessage Error", e);
            }
        }
    }

    /**
     * 异常
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:{},原因：{}", deviceId, error.getMessage());
        log.error("e", error);
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 发送自定义消息
     * */
    public static void sendInfo(String message,@PathParam("userId") String userId) throws IOException {
        log.info("发送消息到:"+userId+"，报文:"+message);
        if(StringUtils.isNotBlank(userId)&&webSocketMap.containsKey(userId)){
            webSocketMap.get(userId).sendMessage(message);
        }else{
            log.error("用户"+userId+",不在线！");
        }
    }

    public static synchronized long getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}
