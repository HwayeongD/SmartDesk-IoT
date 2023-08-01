package com.hjj.hjj_restful_server.controller;


import com.hjj.hjj_restful_server.dto.*;
import com.hjj.hjj_restful_server.handler.WebSocketChatHandler;
import com.hjj.hjj_restful_server.service.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.json.JSONObject;

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
    private final DeskService deskService;

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


    @GetMapping("/home/{empId}/first")
    public ResponseEntity<String> FirstInquiry(@PathVariable Long empId) {
        EmployeeDTO employeeDTO = employeeService.findByempId(empId);
        if (employeeDTO == null) {
            String json = "{ \"resultCode\": \" 400 \" }";
            return new ResponseEntity<>(json, HttpStatus.UNAUTHORIZED);
        }
        EMPSeatDTO empSeatDTO = empSeatService.findByempId(empId);

        // 전일 좌석 정보 가져옴.
        Long prevSeat = empSeatDTO.getPrevSeat();

        // 책상 정보 가져옴.
        DeskDTO byPrevSeat = deskService.findByseatId(prevSeat);
        if(byPrevSeat == null){ // 책상 존재 여부
            String json = "{ \"resultCode\": \" 400 \" }";
            return new ResponseEntity<>(json, HttpStatus.UNAUTHORIZED);
        }

        boolean reserveSuccess;

        if(byPrevSeat.getEmpId() == null){  // 자리가 빈거임.
            reserveSuccess = true;
        }
        else{
            reserveSuccess = false;
        }

        ResponseEntity<String> info = MainPageInquiry(empId);

        String json = info.getBody();
        System.out.println(json);

        JSONObject jsonObject = new JSONObject(json);
        jsonObject.put("reserveSuccess", reserveSuccess);

        json = jsonObject.toString();

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

    // 비밀번호 변경
    @PutMapping("/home/{empId}/password")
    public ResponseEntity<String> PasswordChange(@PathVariable Long empId, @RequestBody Map<String, Object> requestBody)
    {
        EmployeeDTO employeeDTO = employeeService.findByempId(empId);
        if (employeeDTO == null) {
            String json = "{ \"resultCode\": \" 400 \" }";
            return new ResponseEntity<>(json, HttpStatus.UNAUTHORIZED);
        }

        String password = (String) requestBody.get("password");
        employeeDTO.setPassword(password);
        employeeService.save(employeeDTO);

        String json = "{ \"resultCode\": \" 201 \" }";
        return new ResponseEntity<>(json, HttpStatus.OK);

    }

    // 자동 예약 설정 변경
    @PutMapping("/home/{empId}/auto")
    public ResponseEntity<String> AutoBookChange(@PathVariable Long empId, @RequestBody Map<String, Object> requestBody)
    {
        EMPSeatDTO empSeatDTO = empSeatService.findByempId(empId);
        if (empSeatDTO == null) {
            String json = "{ \"resultCode\": \" 400 \" }";
            return new ResponseEntity<>(json, HttpStatus.UNAUTHORIZED);
        }

        Boolean autoBook = (boolean) requestBody.get("autoBook");
        empSeatDTO.setAutoBook(autoBook);
        empSeatService.save(empSeatDTO);

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
