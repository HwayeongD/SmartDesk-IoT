package com.hjj.hjj_restful_server.handler;

import com.hjj.hjj_restful_server.dto.DeskDTO;
import com.hjj.hjj_restful_server.service.DeskService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableScheduling;
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
// @Scheduled 사용 가능하게 함
@EnableScheduling
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {

    private final static Logger LOG = Logger.getGlobal();
    private final Map<String, WebSocketSession> activeSessions = Collections.synchronizedMap(new ConcurrentHashMap<>());
    private final DeskService deskService;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        
        // 클라이언트에게 받은 메세지 출력
        String input = message.getPayload();
        String clientIP = session.getRemoteAddress().getHostName();
        System.out.println("[ " + clientIP + " ]: " + input);
        String[] item = input.split(" ");

        Long si = Long.valueOf(item[0]);
        Long dh = Long.valueOf(item[1]);
        DeskDTO deskDTO = deskService.findByseatId(si);
        deskDTO.setDeskHeightNow(dh);
        // 수신을 완료하면 클라이언트에게 답장 보내기
        // TextMessage textMessage = new TextMessage("서버에서 수신했습니다! [13:26분 수정용]");
        // session.sendMessage(textMessage);
//        if ("211.192.210.130".equals(clientIP)) {
//            TextMessage textMessage1 = new TextMessage("아주 나이스 서버에서 수신했습니다!");
//            session.sendMessage(textMessage1);
//        }
    }
//  일정한 시간이 되었을 때 전체 책상에게 보내는 신호
    @Scheduled(cron = "0 52 13 * * ?")
    //@Scheduled(fixedRate = 5000)
    public void sendDataToAllClients() {
        String message = "a,,,,";
        for (WebSocketSession session : activeSessions.values()) {
            if (session.isOpen()) {
                try{
                    TextMessage textMessage  = new TextMessage(message);
                    session.sendMessage(textMessage);
                } catch (IOException e) {
                    //
                }
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        
        // 웹소켓을 처음 연결했을 때!
        String clientIP = session.getRemoteAddress().getHostName();
        activeSessions.put(clientIP, session);
        System.out.println("[ " + clientIP + " ]가 웹소켓으로 접속했습니다!");


    }
    
    
    // 특정 IP로 메세지를 보냄
    public void sendMessageToSpecificIP(String ip, String message) {
        WebSocketSession session = activeSessions.get(ip);
        if (session != null && session.isOpen()) {
            try {
                TextMessage textMessage = new TextMessage(message);
                session.sendMessage(textMessage);
                System.out.println("성공적으로 원하는 주소에메 세지를 보냈다!");
            } catch (IOException e) {
                // Handle exception if needed
                System.out.println("하 개망했다!");
            }
        }
    }
}
