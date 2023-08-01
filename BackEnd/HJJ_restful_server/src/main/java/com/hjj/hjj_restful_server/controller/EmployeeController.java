package com.hjj.hjj_restful_server.controller;


import com.hjj.hjj_restful_server.dto.EMPAttendanceDTO;
import com.hjj.hjj_restful_server.dto.EMPSeatDTO;
import com.hjj.hjj_restful_server.dto.EmployeeDTO;
import com.hjj.hjj_restful_server.dto.ScheduleDTO;
import com.hjj.hjj_restful_server.handler.WebSocketChatHandler;
import com.hjj.hjj_restful_server.service.EMPAttendanceService;
import com.hjj.hjj_restful_server.service.EMPSeatService;
import com.hjj.hjj_restful_server.service.EmployeeService;
import com.hjj.hjj_restful_server.service.ScheduleService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EMPAttendanceService empAttendanceService;
    private final ScheduleService scheduleService;
    private final EMPSeatService empSeatService;

    // 웹소켓 주입
    private final WebSocketChatHandler webSocketChatHandler;

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<String> login(@ModelAttribute EmployeeDTO employeeDTO, HttpSession session) {
        EmployeeDTO loginResult = employeeService.login(employeeDTO);
        if (loginResult != null) {
            session.setAttribute("loginId", loginResult.getEmpId());
            // 여기서 loginResult를 JSON으로 변환해서 반환
            // 211.192.210.157 준섭 아이피
            webSocketChatHandler.sendMessageToSpecificIP("211.192.210.130", "여기에 메시지를 입력하세요.");
            return new ResponseEntity<>(toJson(loginResult), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Login failed", HttpStatus.UNAUTHORIZED);
        }
    }
    
    // 매인 페이지 조회
    @GetMapping("/home/{empId}")
    public String MainPageInquiry(@PathVariable Long empId){
        EmployeeDTO employeeDTO = employeeService.findByempId(empId);
        if (employeeDTO == null) {
            return "Employee not found";
        }
        EMPAttendanceDTO empAttendanceDTO = empAttendanceService.findByempId(empId);
        ScheduleDTO scheduleDTO = scheduleService.findRecentByEmpId(empId);
        EMPSeatDTO empSeatDTO = empSeatService.findByempId(empId);
        String image;
        String nickname = employeeDTO.getNickname();
        if(employeeDTO.getImage() == null){
            image = "";
        }
        else{
            image = employeeDTO.getImage();
        }
        java.sql.Timestamp calTime;
        String calDetail;

        Time workAttTime = empAttendanceDTO.getWorkAttTime();
        if(scheduleDTO == null){
            calTime = Timestamp.valueOf("2000-01-01 00:00:00.000");
            calDetail = "";
        }
        else{
             calTime = scheduleDTO.getStart();
            calDetail = scheduleDTO.getDetail();
        }
        Long seatId = empSeatDTO.getSeatId();
        Long personalDeskHeight = empSeatDTO.getPersonalDeskHeight();

        String json =
                "{ \"nickname\": \"" + nickname +
                        "\", \"image\": \"" + image +
                        "\", \"workAttTime\": \"" + workAttTime +
                        "\", \"calTime\": \"" + calTime +
                        "\", \"calDetail\": \"" + calDetail +
                        "\", \"seatId\": \"" + seatId +
                        "\", \"personalDeskHeight\": \"" + personalDeskHeight + "\" }";


        return json;

    }

    // 프로필 사진 변경
    @PutMapping("/home/{empId}/profile")
    public void ProfileChange(@PathVariable Long empId, @RequestBody String _image) {
        EmployeeDTO employeeDTO = employeeService.findByempId(empId);

    }



    private String toJson(EmployeeDTO employeeDTO) {
        if (employeeDTO == null) {
            return "{}"; // Return an empty JSON object or appropriate default response when the object is null.
        }

        // Here, you can implement the logic to convert EmployeeDTO to JSON manually,
        // or use libraries like Jackson, Gson, etc., to convert the object to JSON.

        // Example manual conversion:
//        String json = "{\"emp_id\": \"" + employeeDTO.getEmpId() + "\", "
//                + "\"image\": \"" + employeeDTO.getImage() + "\", "
//                + "\"name\": \"" + employeeDTO.getName() + "\", "
//                + "\"nickname\": \"" + employeeDTO.getNickname() + "\", "
//                + "\"password\": \"" + employeeDTO.getPassword() + "\", "
//                + "\"team_id\": \"" + employeeDTO.getTeamId() + "\"}";

        return employeeDTO.toString();
    }
}