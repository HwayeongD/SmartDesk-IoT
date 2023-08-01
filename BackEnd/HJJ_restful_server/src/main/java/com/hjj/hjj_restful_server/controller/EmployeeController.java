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
import java.util.Map;

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
    public ResponseEntity<String> login(@RequestBody Map<String, Object> requestBody) {
        Long empId = Long.valueOf(requestBody.get("empId").toString());
        String password = (String) requestBody.get("password");
        EmployeeDTO loginResult = employeeService.login(empId, password);
        if (loginResult != null) {
            // 211.192.210.157 준섭 아이피
            //webSocketChatHandler.sendMessageToSpecificIP("211.192.210.130", "여기에 메시지를 입력하세요.");

            String json =
                    "{ \"empId\": \"" + loginResult.getEmpId() +
                            "\", \"name\": \"" + loginResult.getName() +
                            "\", \"nickname\": \"" + loginResult.getNickname() +
                            "\", \"password\": \"" + loginResult.getPassword() +
                            "\", \"teamId\": \"" + loginResult.getTeamId() +
                            "\", \"image\": \"" + loginResult.getImage() +
                            "\" }";
            return new ResponseEntity<>(json, HttpStatus.OK);
        } else {

            String json =  "{ \"resultCode\": \" 400 \" }";

            return new ResponseEntity<>(json, HttpStatus.UNAUTHORIZED);
        }
    }
    
    // 매인 페이지 조회
    @GetMapping("/home/{empId}")
    public ResponseEntity<String> MainPageInquiry(@PathVariable Long empId){
        EmployeeDTO employeeDTO = employeeService.findByempId(empId);
        if (employeeDTO == null) {
            String json =  "{ \"resultCode\": \" 400 \" }";

            return new ResponseEntity<>(json, HttpStatus.UNAUTHORIZED);
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
            // 시간이 null 일때 어떻게 할지 생각. 일단 쓰레기값 넣어둠.
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

        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    // 프로필 사진 변경
    @PutMapping("/home/{empId}/profile")
    public ResponseEntity<String> ProfileChange(@PathVariable Long empId, @RequestBody Map<String, Object> requestBody) {
        EmployeeDTO employeeDTO = employeeService.findByempId(empId);
        if (employeeDTO == null) {
            String json =  "{ \"resultCode\": \" 400 \" }";

            return new ResponseEntity<>(json, HttpStatus.UNAUTHORIZED);
        }
        String image = (String) requestBody.get("image");
        employeeDTO.setImage(image);
        employeeService.save(employeeDTO);


        String json = "{ \"resultCode\": \" 201 \" }";
        return new ResponseEntity<>(json, HttpStatus.OK);
    }








//    private String toJson(EmployeeDTO employeeDTO) {
//        if (employeeDTO == null) {
//            return "{}"; // Return an empty JSON object or appropriate default response when the object is null.
//        }
//        return employeeDTO.toString();
//    }
}
