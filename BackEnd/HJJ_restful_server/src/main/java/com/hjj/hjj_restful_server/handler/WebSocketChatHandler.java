package com.hjj.hjj_restful_server.handler;

import com.hjj.hjj_restful_server.dto.DailyScheduleDTO;
import com.hjj.hjj_restful_server.dto.DeskDTO;
import com.hjj.hjj_restful_server.dto.EMPAttendanceDTO;
import com.hjj.hjj_restful_server.repository.*;
import com.hjj.hjj_restful_server.service.DailyScheduleService;
import com.hjj.hjj_restful_server.service.DeskService;
import com.hjj.hjj_restful_server.service.EMPAttendanceService;
import com.hjj.hjj_restful_server.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;
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
    private final ScheduleService scheduleService;
    private final DailyScheduleService dailyScheduleService;
    private final EMPAttendanceService empAttendanceService;
    private final EMPAttendanceRepository empAttendanceRepository;
    private final DeskRepository deskRepository;
    private final EMPSeatRepository empSeatRepository;
    private final DailyScheduleRepository dailyScheduleRepository;



    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        
        // 클라이언트에게 받은 메세지 출력
        String input = message.getPayload();
        String clientIP = session.getRemoteAddress().getHostName();
        System.out.println("[ " + clientIP + " ]: " + input);
        String[] item = input.split(" ");
        if (item.length == 2) {
            Long si = Long.valueOf(item[0]);
            Long dh = Long.valueOf(item[1]);
            DeskDTO deskDTO = deskService.findByseatId(si);
            deskDTO.setDeskHeightNow(dh);
            deskService.save(deskDTO);
        }
//         수신을 완료하면 클라이언트에게 답장 보내기
//         TextMessage textMessage = new TextMessage("서버에서 수신했습니다! [13:26분 수정용]");
//         session.sendMessage(textMessage);
//        if ("211.192.210.130".equals(clientIP)) {
//            TextMessage textMessage1 = new TextMessage("아주 나이스 서버에서 수신했습니다!");
//            session.sendMessage(textMessage1);
//        }

    }
//  일정한 시간이 되었을 때 전체 책상에게 보내는 신호
    @Scheduled(cron = "0 00 00 * * ?")
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
        empAttendanceRepository.resetTable();
        empSeatRepository.resetTable();
        deskRepository.resetTable();
        dailyScheduleRepository.truncateTable();
        scheduleService.transferToDailySchedule();

        System.out.println("[자정 전체 초기화]");
    }

    // 1분 마다 체크!
    @Scheduled(cron = "0 * 6-23 * * ?")
    public void CheckAFK(){
        List<DailyScheduleDTO> EndList = dailyScheduleService.findNowEndTime();
        if(EndList != null){
            for(DailyScheduleDTO dailyScheduleDTO : EndList){
                Long empId = dailyScheduleDTO.getEmpId();
                EMPAttendanceDTO empAttendanceDTO = empAttendanceService.findByempId(empId);
                empAttendanceDTO.setStatus(Byte.valueOf("1"));
                empAttendanceService.save(empAttendanceDTO);
            }
        }


        List<DailyScheduleDTO> StartList = dailyScheduleService.findNowSchedule();
        if(StartList != null){
            for(DailyScheduleDTO dailyScheduleDTO : StartList){
                Long empId = dailyScheduleDTO.getEmpId();
                EMPAttendanceDTO empAttendanceDTO = empAttendanceService.findByempId(empId);
                empAttendanceDTO.setStatus(Byte.valueOf("2"));
                empAttendanceService.save(empAttendanceDTO);
            }
        }
        System.out.println("[자리비움 체크]");
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        
        // 웹소켓을 처음 연결했을 때!
        String clientIP = session.getRemoteAddress().getHostName();
        activeSessions.put(clientIP, session);
        System.out.println("[ " + clientIP + " ]가 웹소켓으로 접속했습니다!");
        Long si = Long.valueOf("301");
        DeskDTO deskDTO = deskService.findByseatId(si);
        deskDTO.setSeatIp(clientIP);
        deskService.save(deskDTO);
    }

    // 특정 IP로 메세지를 보냄
    public void sendMessageToSpecificIP(String ip, String message) {
        WebSocketSession session = activeSessions.get(ip);
        if (session != null && session.isOpen()) {
            try {
                TextMessage textMessage = new TextMessage(message);
                session.sendMessage(textMessage);
                System.out.println("성공적으로 [" + ip + "] 메세지를 보냈다!");
            } catch (IOException e) {
                // Handle exception if needed
                System.out.println("하,, ["+ ip + "]에게 못보내서 개망했다!");
            }
        }

    }
}
