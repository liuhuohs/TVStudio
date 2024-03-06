package com.HITwh.TVStudio.service.websocket;

import com.alibaba.fastjson.JSONObject;
import com.HITwh.TVStudio.domain.Danmu;
import com.HITwh.TVStudio.domain.constant.UserMomentsConstant;
import com.HITwh.TVStudio.service.DanmuService;
import com.HITwh.TVStudio.service.util.RocketMQUtil;
import com.HITwh.TVStudio.service.util.TokenUtil;
import io.netty.util.internal.StringUtil;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@ServerEndpoint("/imserver/{token}/{videoId}")
public class WebSocketService {
    private final Logger logger=LoggerFactory.getLogger(this.getClass());
    private static AtomicInteger ONLINE_COUNT=new AtomicInteger(0);
    public static final HashMap<String,VedioSocketService> VEDIOSOCKET_MAP = new HashMap<>();

    private Session session;
    private String sessionId;
    private Long userId;
    private String videoId;
    private static ApplicationContext APPLICATION_CONTEXT;
    public static void setApplicationContext(ApplicationContext applicationContext){
        WebSocketService.APPLICATION_CONTEXT = applicationContext;
    }
    @OnOpen
    public void openConnection(Session session, @PathVariable("token")String token,@PathVariable("videoId") String videoId1){
        try{
            this.userId = TokenUtil.verifyToken(token);
        }catch (Exception ignored){}
        this.sessionId=session.getId();
        this.session=session;
        VedioSocketService vedioSocketService = null;
        videoId=videoId1;
        if(!VEDIOSOCKET_MAP.containsKey(videoId)){
            vedioSocketService= new VedioSocketService();
            VEDIOSOCKET_MAP. put(videoId,vedioSocketService);
        }
        if(vedioSocketService.WEBSOCKET_MAP.containsKey(sessionId)){
            vedioSocketService.WEBSOCKET_MAP.remove(sessionId);
            vedioSocketService.WEBSOCKET_MAP.put(sessionId,this);
        }else{
            vedioSocketService.WEBSOCKET_MAP.put(sessionId,this);
            vedioSocketService.ONLINE_COUNT.getAndIncrement();
        }
        logger.info("用户连接成功：" + sessionId + "，当前在线人数为：" + ONLINE_COUNT.get());
        try{
            this.sendMessage("0");
        }catch (Exception e){
            logger.error("连接异常");
        }
    }
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }
    @OnClose
    public void closeConnection(){
        VedioSocketService vedioSocketService = VEDIOSOCKET_MAP.get(videoId);

        if(vedioSocketService.WEBSOCKET_MAP.containsKey(sessionId)){
            vedioSocketService.WEBSOCKET_MAP.remove(sessionId);
            ONLINE_COUNT.getAndDecrement();
        }
        logger.info("用户退出：" + sessionId + "当前在线人数为：" + ONLINE_COUNT.get());
    }
    @OnMessage
    public void onMessage(String message){
        logger.info("用户信息：" + sessionId + "，报文：" + message);
        VedioSocketService vedioSocketService = VEDIOSOCKET_MAP.get(videoId);
        if(!StringUtil.isNullOrEmpty(message)){
            try{
                //群发消息
                for(Map.Entry<String, WebSocketService> entry : vedioSocketService.WEBSOCKET_MAP.entrySet()){
                    WebSocketService webSocketService = entry.getValue();
                    DefaultMQProducer danmusProducer = (DefaultMQProducer)APPLICATION_CONTEXT.getBean("danmusProducer");
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("message", message);
                    jsonObject.put("sessionId", webSocketService.getSessionId());
                    jsonObject.put("videoId",videoId);
                    Message msg = new Message(UserMomentsConstant.TOPIC_DANMUS, jsonObject.toJSONString().getBytes(StandardCharsets.UTF_8));
                    RocketMQUtil.asyncSendMsg(danmusProducer, msg);
                }
                if(this.userId != null){
                    //保存弹幕到数据库
                    Danmu danmu = JSONObject.parseObject(message, Danmu.class);
                    danmu.setUserId(userId);
                    danmu.setCreateTime(new Date());
                    DanmuService danmuService = (DanmuService)APPLICATION_CONTEXT.getBean("danmuService");
                    danmuService.asyncAddDanmu(danmu);
                    //保存弹幕到redis
                    danmuService.addDanmusToRedis(danmu);
                }
            }catch (Exception e){
                logger.error("弹幕接收出现问题");
                e.printStackTrace();
            }
        }
    }
    //或直接指定时间间隔，例如：5秒
    @Scheduled(fixedRate=5000)
    private void noticeOnlineCount() throws IOException {
        for(Map.Entry<String, WebSocketService> entry : WebSocketService.VEDIOSOCKET_MAP.get(videoId).WEBSOCKET_MAP.entrySet()){
            WebSocketService webSocketService = entry.getValue();
            if(webSocketService.session.isOpen()){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("onlineCount", ONLINE_COUNT.get());
                jsonObject.put("msg", "当前在线人数为" + ONLINE_COUNT.get());
                webSocketService.sendMessage(jsonObject.toJSONString());
            }
        }
    }
    public Session getSession() {
        return session;
    }

    public String getSessionId() {
        return sessionId;
    }
    
}
