package com.hjj.hjj_restful_server.handler;

import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@Component
@Scope("singleton")
public class WebSocketChatHandler extends TextWebSocketHandler {

    private final static Logger LOG = Logger.getGlobal();
    private final Map<String, WebSocketSession> activeSessions = Collections.synchronizedMap(new ConcurrentHashMap<>());
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String input = message.getPayload();


        TextMessage textMessage = new TextMessage("서버에서 수신했습니다!");
        session.sendMessage(textMessage);
        String clientIP = session.getRemoteAddress().getHostString();

        if ("211.192.210.130".equals(clientIP)) {
            TextMessage textMessage1 = new TextMessage("아주 나이스 서버에서 수신했습니다!");
            session.sendMessage(textMessage1);
        }
    }

//    @Scheduled(fixedRate = 5000)
//    public void sendDataToAllClients() {
//        System.out.println("hellow`");
//        String message = "매분마다 보내는 데이터다 이것들아!!!!";
//        for (WebSocketSession session : activeSessions.values()) {
//            if (session.isOpen()) {
//                try{
//                    TextMessage textMessage  = new TextMessage(message);
//                    session.sendMessage(textMessage);
//                } catch (IOException e) {
//                    //
//                }
//
//            }
//        }
//    }

    @Scheduled(fixedRate = 5000)
    public void hi() {
        System.out.println("5초마다 하이!");
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        String clientIP = session.getRemoteAddress().getHostName();
        activeSessions.put(clientIP, session);
        System.out.println("[ " + clientIP + " ]가 웹소켓으로 접속했습니다!");
    }

    public void sendMessageToSpecificIP(String ip, String message) {
        WebSocketSession session = activeSessions.get(ip);
        System.out.println("일단 실행은 되는지 확인해보거라~" + session);
        if (session != null && session.isOpen()) {
            try {
                TextMessage textMessage = new TextMessage(message);
                session.sendMessage(textMessage);
                System.out.println("성공적으로 원하는 주소로 메세지를 보냈다!");
            } catch (IOException e) {
                // Handle exception if needed
                System.out.println("하 개망했다!");
            }
        }
    }
}
