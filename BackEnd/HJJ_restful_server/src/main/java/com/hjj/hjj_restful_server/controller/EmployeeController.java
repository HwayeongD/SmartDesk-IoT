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
    private final DepartmentService departmentService;

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
        DepartmentDTO departmentDTO = departmentService.findByTeamId(employeeDTO.getTeamId());
        EMPAttendanceDTO empAttendanceDTO = empAttendanceService.findByempId(empId);

        String nickname = employeeDTO.getNickname();
        Long personalDeskHeight = empSeatDTO.getPersonalDeskHeight();
        String teamName = departmentDTO.getTeamName();
        Byte status = empAttendanceDTO.getStatus();


        // 전일 좌석 정보 가져옴.
        Long prevSeat = empSeatDTO.getPrevSeat();

        // 책상 정보 가져옴.
        DeskDTO byPrevSeat = deskService.findByseatId(prevSeat);


        if(byPrevSeat == null){ // 책상 존재 여부
            String json = "{ \"resultCode\": \" 400 \" }";
            return new ResponseEntity<>(json, HttpStatus.UNAUTHORIZED);
        }

        boolean reserveSuccess;
        String seatIp = byPrevSeat.getSeatIp();

        if(byPrevSeat.getEmpId() == null){  // 자리가 빈거임.
            reserveSuccess = true;

            // 자동으로 자리 예약!

            // 모션데스킹 활동 요청 소켓 메세지
            String socketMsg = nickname +","+ personalDeskHeight +","+ teamName +","+ status;
            webSocketChatHandler.sendMessageToSpecificIP(seatIp, socketMsg);
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


    // 자리 선택 (예약)
    @PutMapping("seats")
    public ResponseEntity<String> SeatReservation(@RequestBody Map<String, Object> requestBody){
        Long empId = Long.valueOf(requestBody.get("empId").toString());
        Long seatId = Long.valueOf(requestBody.get("seatId").toString());

        DeskDTO deskDTO = deskService.findByseatId(seatId);
        if(deskDTO.getEmpId() != null){ // 이미 쓰고있는 좌석이면
            String json = "{ \"resultCode\": \" 400 \" }";
            return new ResponseEntity<>(json, HttpStatus.UNAUTHORIZED);
        }

        EMPAttendanceDTO empAttendanceDTO = empAttendanceService.findByempId(empId);
        if(empAttendanceDTO.getWorkAttTime() == null){  // 출근 x
            String json = "{ \"resultCode\": \" 400 \" }";
            return new ResponseEntity<>(json, HttpStatus.UNAUTHORIZED);
        }

        EMPSeatDTO empSeatDTO = empSeatService.findByempId(empId);
        if (empSeatDTO.getSeatId() != null) {   // 이미 예약함
            String json = "{ \"resultCode\": \" 400 \" }";
            return new ResponseEntity<>(json, HttpStatus.UNAUTHORIZED);
        }

        deskDTO.setEmpId(empId);
        empSeatDTO.setSeatId(seatId);
        empSeatDTO.setPrevSeat(seatId);

        deskService.save(deskDTO);
        empSeatService.save(empSeatDTO);

        String json = "{ \"resultCode\": \" 201 \" }";
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    // 자리 변경
    @PutMapping("seats/change")
    public ResponseEntity<String> SeatChange(@RequestBody Map<String, Object> requestBody) {
        Long empId = Long.valueOf(requestBody.get("empId").toString());
        Long seatId = Long.valueOf(requestBody.get("seatId").toString());

        EMPSeatDTO cancelSeat = empSeatService.findByempId(empId);
        DeskDTO cancelDesk = deskService.findByseatId(cancelSeat.getSeatId());



        String json = "{ \"resultCode\": \" 201 \" }";
        return new ResponseEntity<>(json, HttpStatus.OK);
    }
//    empId, seatId 받아옴
//    사람, 새로운 자리
//
//1. 이전 자리 정보 삭제
//    사람 -> 좌석 정보 확인
//    좌석 정보 -> 사람 정보 지우고
//    사람 -> 좌석 정보 갱신
//    새로운 좌석 -> 사람 정보 입력


//    private String toJson(EmployeeDTO employeeDTO) {
//        if (employeeDTO == null) {
//            return "{}"; // Return an empty JSON object or appropriate default response when the object is null.
//        }
//        return employeeDTO.toString();
//    }
}
