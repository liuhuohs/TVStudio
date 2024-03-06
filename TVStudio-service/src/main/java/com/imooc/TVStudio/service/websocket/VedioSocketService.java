package com.imooc.TVStudio.service.websocket;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class VedioSocketService {
    public static AtomicInteger ONLINE_COUNT=new AtomicInteger(0);
    public static final ConcurrentHashMap<String,WebSocketService> WEBSOCKET_MAP = new ConcurrentHashMap<>();
}
