package com.hjj.hjj_restful_server.controller;


import com.hjj.hjj_restful_server.dto.*;
import com.hjj.hjj_restful_server.handler.WebSocketChatHandler;
import com.hjj.hjj_restful_server.service.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.json.JSONObject;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
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
        if (loginResult.getPassword() != null) {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("empId",loginResult.getEmpId());
            jsonObject.put("name",loginResult.getName());
            jsonObject.put("nickname",loginResult.getNickname());
            jsonObject.put("password",loginResult.getPassword());
            jsonObject.put("teamId",loginResult.getTeamId());
            jsonObject.put("result","L101");

            String json = jsonObject.toString();
            return new ResponseEntity<>(json, HttpStatus.OK);
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("result",loginResult.getName());

            String json = jsonObject.toString();
            return new ResponseEntity<>(json, HttpStatus.UNAUTHORIZED);
        }
    }

    // 매인 페이지 조회
    @GetMapping("/home/{empId}")
    public ResponseEntity<String> MainPageInquiry(@PathVariable Long empId) {
        EmployeeDTO employeeDTO = employeeService.findByempId(empId);
        if (employeeDTO == null) {
            String json = "{ \"resultCode\": \" 400 \" }";

            return new ResponseEntity<>(json, HttpStatus.UNAUTHORIZED);
        }
        EMPAttendanceDTO empAttendanceDTO = empAttendanceService.findByempId(empId);
        EMPSeatDTO empSeatDTO = empSeatService.findByempId(empId);


        JSONObject json = new JSONObject();
        json.put("nickname", employeeDTO.getNickname());
        if (empAttendanceDTO.getWorkAttTime() != null)
            json.put("workAttTime", empAttendanceDTO.getWorkAttTime());
        else
            json.put("workAttTime", "");
        if (empAttendanceDTO.getWorkEndTime() != null)
            json.put("workEndTime", empAttendanceDTO.getWorkEndTime());
        else
            json.put("workEndTime", "");
        json.put("status", empAttendanceDTO.getStatus());
        if (empSeatDTO.getSeatId() != null)
            json.put("seatId", empSeatDTO.getSeatId());
        else
            json.put("seatId", "");
        if (empSeatDTO.getPersonalDeskHeight() != null)
            json.put("personalDeskHeight", empSeatDTO.getPersonalDeskHeight());
        else
            json.put("personalDeskHeight", "");
        json.put("autoBook",empSeatDTO.isAutoBook());

        String jsonString = json.toString();

        return new ResponseEntity<>(jsonString, HttpStatus.OK);
    }


    // 자동 예약 요청
    @GetMapping("/home/{empId}/first")
    public ResponseEntity<String> FirstInquiry(@PathVariable Long empId) {
        EmployeeDTO employeeDTO = employeeService.findByempId(empId);

        EMPSeatDTO empSeatDTO = empSeatService.findByempId(empId);
        DepartmentDTO departmentDTO = departmentService.findByTeamId(employeeDTO.getTeamId());
        EMPAttendanceDTO empAttendanceDTO = empAttendanceService.findByempId(empId);

        // 전일 좌석 정보 가져옴.
        Long prevSeat = empSeatDTO.getPrevSeat();

        // 책상 정보 가져옴.
        DeskDTO byPrevSeat = deskService.findByseatId(prevSeat);

        boolean reserveSuccess;

        if (byPrevSeat.getEmpId() == null) {  // 자리가 빈거임.
            reserveSuccess = true;
            // 자동으로 자리 예약!
            Map<String, Object> reqbody = new HashMap<>();
            reqbody.put("empId", empId.toString());
            reqbody.put("seatId", prevSeat.toString());
            SeatReservation(reqbody);
        } else {
            reserveSuccess = false;
        }

        JSONObject jsonObject = new JSONObject();
        empSeatDTO = empSeatService.findByempId(empId);
        if(empSeatDTO.getSeatId() != null)
            jsonObject.put("seatId", empSeatDTO.getSeatId());
        else
            jsonObject.put("seatId", "");

        jsonObject.put("reserveSuccess", reserveSuccess);

        String json = jsonObject.toString();
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    // 프로필 사진 변경
    @PutMapping("/home/{empId}/profile")
    public ResponseEntity<String> ProfileChange(@PathVariable Long empId, @RequestBody Map<String, Object> requestBody) {
        EmployeeDTO employeeDTO = employeeService.findByempId(empId);
        if (employeeDTO == null) {
            String json = "{ \"resultCode\": \" 400 \" }";
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
    public ResponseEntity<String> PasswordChange(@PathVariable Long empId, @RequestBody Map<String, Object> requestBody) {
        EmployeeDTO employeeDTO = employeeService.findByempId(empId);
        
        String password = (String) requestBody.get("password");

        if(!employeeDTO.getPassword().equals(password)){  // 비밀번호 틀릴 경우
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("result","P201");
            String json = jsonObject.toString();
            return new ResponseEntity<>(json, HttpStatus.UNAUTHORIZED);
        }
        
        String newpassword = requestBody.get("newpassword").toString();
        
        employeeDTO.setPassword(newpassword);
        employeeService.save(employeeDTO);

        String json = "{ \"result\": \" P101 \" }";
        return new ResponseEntity<>(json, HttpStatus.OK);

    }

    // 자동 예약 설정 변경
    @PutMapping("/home/{empId}/auto")
    public ResponseEntity<String> AutoBookChange(@PathVariable Long empId, @RequestBody Map<String, Object> requestBody) {
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

    // 자리 비움 토글
    @PutMapping("/home/{empId}/away")
    public ResponseEntity<String> AwayToggle(@PathVariable Long empId, @RequestBody Map<String, Object> requestBody) {
        Byte Status = Byte.valueOf(requestBody.get("status").toString());
        // 자리비움 status = 2
        // 자리비움 off status = 1
        EMPAttendanceDTO empAttendanceDTO = empAttendanceService.findByempId(empId);
        empAttendanceDTO.setStatus(Status);
        empAttendanceService.save(empAttendanceDTO);

        EmployeeDTO employeeDTO = employeeService.findByempId(empId);
        DepartmentDTO departmentDTO = departmentService.findByTeamId(employeeDTO.getTeamId());
        EMPSeatDTO empSeatDTO = empSeatService.findByempId(empId);

        String nickname = employeeDTO.getNickname();
        Long personalDeskHeight = empSeatDTO.getPersonalDeskHeight();
        String teamName = departmentDTO.getTeamName();

        Byte status = empAttendanceDTO.getStatus();

        Long prevSeat = empSeatDTO.getPrevSeat();
        DeskDTO byPrevSeat = deskService.findByseatId(prevSeat);
        String seatIp = byPrevSeat.getSeatIp();

        // 모션데스킹 활동 요청 소켓 메세지
        String socketMsg = "x,"+ nickname +","+ personalDeskHeight +","+ teamName +","+ status;
        webSocketChatHandler.sendMessageToSpecificIP(seatIp, socketMsg);

        String json = "{ \"resultCode\": \" 201 \" }";
        return new ResponseEntity<>(json, HttpStatus.OK);
    }


    // 출근
    @PutMapping("home/att")
    public ResponseEntity<String> AttRequest(@RequestBody Map<String, Object> requestBody){
        String empIdCard = requestBody.get("empIdCard").toString();

        // 카드로 사용자 정보 가져옴.
        EmployeeDTO employeeDTO = employeeService.findByEmpIdCard(empIdCard);
        EMPAttendanceDTO empAttendanceDTO = empAttendanceService.findByempId(employeeDTO.getEmpId());

        // 현재 시간 출근 시간에 저장
        LocalTime currentTime;
        currentTime =LocalTime.now();
        Time time = Time.valueOf(currentTime);

        System.out.println(time.toString());
        empAttendanceDTO.setWorkAttTime(time);
        empAttendanceDTO.setStatus(Byte.valueOf("1"));
        empAttendanceService.save(empAttendanceDTO);

        String json = "{ \"resultCode\": \" 201 \" }";
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    // 퇴근 요청
    @PutMapping("home/{empId}/leave")
    public ResponseEntity<String> ExitRequest(@PathVariable Long empId){
        //현재 시간 저장
        LocalTime currentTime;

        currentTime = LocalTime.now();
        Time time = Time.valueOf(currentTime);

        EMPAttendanceDTO empAttendanceDTO = empAttendanceService.findByempId(empId);
        empAttendanceDTO.setWorkEndTime(time);
        empAttendanceDTO.setStatus(Byte.valueOf("0"));
        empAttendanceService.save(empAttendanceDTO);
        
        // 예약 취소
        SeatCancel(empId);

        // 여기 지워도 될듯 확인하기.
        EmployeeDTO employeeDTO = employeeService.findByempId(empId);
        DepartmentDTO departmentDTO = departmentService.findByTeamId(employeeDTO.getTeamId());
        EMPSeatDTO empSeatDTO = empSeatService.findByempId(empId);

        String nickname = employeeDTO.getNickname();
        Long personalDeskHeight = empSeatDTO.getPersonalDeskHeight();
        String teamName = departmentDTO.getTeamName();
        Byte status = empAttendanceDTO.getStatus();
        Long prevSeat = empSeatDTO.getPrevSeat();
        DeskDTO byPrevSeat = deskService.findByseatId(prevSeat);
        String seatIp = byPrevSeat.getSeatIp();

        // 모션데스킹 활동 요청 소켓 메세지
        String socketMsg = "c,"+ nickname +","+ personalDeskHeight +","+ teamName +","+ status;
        webSocketChatHandler.sendMessageToSpecificIP(seatIp, socketMsg);


        String json = "{ \"workEndTime\": \"" + time + "\" }";
        return new ResponseEntity<>(json, HttpStatus.OK);
    }


    // 선호 책상 높이 변경
    @PutMapping("home/{empId}/mydesk")
    public ResponseEntity<String> ChangeDeskHeight(@PathVariable Long empId, Map<String, Object> requestBody){
        Long personalDeskHeight = Long.valueOf(requestBody.get("personalDeskHeight").toString());

        EMPSeatDTO empSeatDTO = empSeatService.findByempId(empId);
        empSeatDTO.setPersonalDeskHeight(personalDeskHeight);
        empSeatService.save(empSeatDTO);

        String json = "{ \"resultCode\": \" 201 \" }";
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    // 아두이노로 책상 높이 조절 명령
    @PutMapping("home/{empId}/mydesk/move")
    public ResponseEntity<String> MoveDeskHeight(@PathVariable Long empId){



        String json = "{ \"resultCode\": \" 201 \" }";
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    // 층별로 자리 불러오기
    @GetMapping("seats/{floor}")
    public ResponseEntity<String> SeatsByFloor(@PathVariable Long floor){
        List<DeskDTO> deskDTOList = deskService.findByFloor(floor);

        JSONArray jsonArray = new JSONArray();
        for(DeskDTO deskDTO : deskDTOList){

            JSONObject json = new JSONObject();

            // 1. empId 확인
            //Long empId = deskDTO.getEmpId();

            // 2-1. 있으면 값 넣어주기.
            if(deskDTO.getEmpId() != null){
                Long empId = deskDTO.getEmpId();

                // 3. empId 기준으로 nickname, teamname, status 가져와서 넣어주기.
                EmployeeDTO employeeDTO = employeeService.findByempId(empId);
                String nickname = employeeDTO.getNickname();

                DepartmentDTO departmentDTO = departmentService.findByTeamId(employeeDTO.getTeamId());
                String teamName = departmentDTO.getTeamName();

                EMPAttendanceDTO empAttendanceDTO = empAttendanceService.findByempId(empId);
                Byte status = empAttendanceDTO.getStatus();

                json.put("seatId", deskDTO.getSeatId());
                json.put("nickname", nickname);
                json.put("teamName", teamName);
                json.put("status", status);
                jsonArray.put(json);
            }
            else{
                // 2-2. 없으면 seatId만 넣어주기.
                json.put("seatId", deskDTO.getSeatId());
                json.put("nickname", "");
                json.put("teamName", "");
                json.put("status", "");
                jsonArray.put(json);
            }
        }
        String jsonString = jsonArray.toString();

        return new ResponseEntity<>(jsonString, HttpStatus.OK);
    }

    // 자리 선택 (예약)
    @PutMapping("seats")
    public ResponseEntity<String> SeatReservation(@RequestBody Map<String, Object> requestBody){
        Long empId = Long.valueOf(requestBody.get("empId").toString());
        Long seatId = Long.valueOf(requestBody.get("seatId").toString());

        DeskDTO deskDTO = deskService.findByseatId(seatId);
        if(deskDTO.getEmpId() != null){ // 이미 쓰고있는 좌석이면
            String json = "{ \"resultCode\": \" 401 \" }";
            return new ResponseEntity<>(json, HttpStatus.UNAUTHORIZED);
        }

        EMPAttendanceDTO empAttendanceDTO = empAttendanceService.findByempId(empId);
        if(empAttendanceDTO.getWorkAttTime() == null){  // 출근 x
            String json = "{ \"resultCode\": \" 402 \" }";
            return new ResponseEntity<>(json, HttpStatus.UNAUTHORIZED);
        }

        EMPSeatDTO empSeatDTO = empSeatService.findByempId(empId);
        if (empSeatDTO.getSeatId() != null) {   // 이미 예약함
            String json = "{ \"resultCode\": \" 403 \" }";
            return new ResponseEntity<>(json, HttpStatus.UNAUTHORIZED);
        }

        deskDTO.setEmpId(empId);
        empSeatDTO.setSeatId(seatId);
        empSeatDTO.setPrevSeat(seatId);

        deskService.save(deskDTO);
        empSeatService.save(empSeatDTO);


        EmployeeDTO employeeDTO = employeeService.findByempId(empId);
        DepartmentDTO departmentDTO = departmentService.findByTeamId(employeeDTO.getTeamId());


        String nickname = employeeDTO.getNickname();
        Long personalDeskHeight = empSeatDTO.getPersonalDeskHeight();
        String teamName = departmentDTO.getTeamName();
        Byte status = empAttendanceDTO.getStatus();
        Long prevSeat = empSeatDTO.getPrevSeat();
        DeskDTO byPrevSeat = deskService.findByseatId(prevSeat);
        String seatIp = byPrevSeat.getSeatIp();
        String socketMsg = "";
        if (personalDeskHeight == null) {
            socketMsg = "g,"+ nickname +","+ "-1" +","+ teamName +","+ status;
        }
        else {
            socketMsg = "g,"+ nickname +","+ personalDeskHeight +","+ teamName +","+ status;
        }
        // 모션데스킹 활동 요청 소켓 메세지
        webSocketChatHandler.sendMessageToSpecificIP(seatIp, socketMsg);


        String json = "{ \"resultCode\": \" 201 \" }";
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    // 자리 변경
    @PutMapping("seats/change")
    public ResponseEntity<String> SeatChange(@RequestBody Map<String, Object> requestBody) {
        Long empId = Long.valueOf(requestBody.get("empId").toString());
        Long seatId = Long.valueOf(requestBody.get("seatId").toString());

        // 기존 자리 취소
        EMPSeatDTO empSeat = empSeatService.findByempId(empId);
        DeskDTO cancelDesk = deskService.findByseatId(empSeat.getSeatId());
        cancelDesk.setEmpId(null);
        deskService.save(cancelDesk);

        // 자리 정보 갱신
        empSeat.setSeatId(seatId);
        empSeat.setPrevSeat(seatId);
        empSeatService.save(empSeat);

        // 책상에도 정보 입력
        DeskDTO newDesk = deskService.findByseatId(seatId);
        newDesk.setEmpId(empId);
        deskService.save(newDesk);

        String json = "{ \"resultCode\": \" 201 \" }";
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    // 자리 예약 취소
    @DeleteMapping("seats/{empId}")
    public ResponseEntity<String> SeatCancel(@PathVariable Long empId) {

        // 자리 취소
        EMPSeatDTO empSeat = empSeatService.findByempId(empId);
        DeskDTO cancelDesk = deskService.findByseatId(empSeat.getSeatId());
        cancelDesk.setEmpId(null);
        deskService.save(cancelDesk);

        // 자리 정보 갱신
        empSeat.setSeatId(null);
        empSeatService.save(empSeat);

        // 자리 취소
        EmployeeDTO employeeDTO = employeeService.findByempId(empId);
        DepartmentDTO departmentDTO = departmentService.findByTeamId(employeeDTO.getTeamId());
        EMPSeatDTO empSeatDTO = empSeatService.findByempId(empId);
        EMPAttendanceDTO empAttendanceDTO = empAttendanceService.findByempId(empId);

        String nickname = employeeDTO.getNickname();
        Long personalDeskHeight = empSeatDTO.getPersonalDeskHeight();
        String teamName = departmentDTO.getTeamName();
        Byte status = empAttendanceDTO.getStatus();
        Long prevSeat = empSeatDTO.getPrevSeat();
        DeskDTO byPrevSeat = deskService.findByseatId(prevSeat);
        String seatIp = byPrevSeat.getSeatIp();

        // 모션데스킹 활동 요청 소켓 메세지
        String socketMsg = "c,"+ nickname +","+ personalDeskHeight +","+ teamName +","+ status;
        webSocketChatHandler.sendMessageToSpecificIP(seatIp, socketMsg);

        String json = "{ \"resultCode\": \" 201 \" }";
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    // 개인 스케쥴 확인하기 (월별로)
    @GetMapping("schedule/{empId}/{month}")
    public ResponseEntity<String> GetScheduleMonth(@PathVariable Long month){
        List<ScheduleDTO> scheduleDTOList = scheduleService.findByMonth(month);

        JSONArray jsonArray = new JSONArray();
        for(ScheduleDTO scheduleDTO : scheduleDTOList){

            JSONObject json = new JSONObject();

            json.put("schId", scheduleDTO.getSchId());
            json.put("start", scheduleDTO.getStart());
            json.put("end", scheduleDTO.getEnd());
            json.put("status", scheduleDTO.getStatus());
            json.put("detail", scheduleDTO.getDetail());
            jsonArray.put(json);
        }
        String jsonString = jsonArray.toString();

        return new ResponseEntity<>(jsonString,HttpStatus.OK);
    }

    // 스케쥴 등록하기
    @PostMapping("schedule/{empId}")
    public ResponseEntity<String> RegistSchedule(@PathVariable Long empId, @RequestBody Map<String,Object> requestBody){

        java.sql.Timestamp start = Timestamp.valueOf(requestBody.get("start").toString());
        java.sql.Timestamp end = Timestamp.valueOf(requestBody.get("end").toString());
        Byte status = Byte.valueOf(requestBody.get("status").toString());
        String detail = requestBody.get("detail").toString();

        ScheduleDTO NewscheduleDTO =  new ScheduleDTO();
        NewscheduleDTO.setEmpId(empId);
        NewscheduleDTO.setStart(start);
        NewscheduleDTO.setEnd(end);
        NewscheduleDTO.setStatus(status);
        NewscheduleDTO.setDetail(detail);
        scheduleService.save(NewscheduleDTO);

        String json = "{ \"resultCode\": \" 201 \" }";
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

}
