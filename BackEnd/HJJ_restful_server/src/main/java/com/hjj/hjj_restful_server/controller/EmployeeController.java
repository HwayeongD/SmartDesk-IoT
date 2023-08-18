package com.hjj.hjj_restful_server.controller;


import com.hjj.hjj_restful_server.dto.*;
import com.hjj.hjj_restful_server.handler.WebSocketChatHandler;
import com.hjj.hjj_restful_server.service.*;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.json.JSONObject;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

//import java.sql.Date;
import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;
import java.util.Date;
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
    private final DailyScheduleService dailyScheduleService;

    // 웹소켓 주입
    private final WebSocketChatHandler webSocketChatHandler;

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, Object> requestBody) {
        if(requestBody.get("empId") == null || requestBody.get("empId") == ""){
            System.out.println("[로그인] 아이디를 입력해주세요.");
            String json = "{ \"resultCode\": \" L201 \" }";
            return new ResponseEntity<>(json, HttpStatus.OK);
        }
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
            jsonObject.put("resultCode","L101");

            String json = jsonObject.toString();
            return new ResponseEntity<>(json, HttpStatus.OK);
        } else {
            JSONObject jsonObject = new JSONObject();
            if(loginResult.getName() == "L201"){
                System.out.println("[로그인] 사용자 없음");
            }
            else if (loginResult.getName() == "L202"){
                System.out.println("[로그인] 비밀번호가 틀렸습니다.");
            }
            jsonObject.put("resultCode",loginResult.getName());

            String json = jsonObject.toString();
            return new ResponseEntity<>(json, HttpStatus.OK);
        }
    }

    // 매인 페이지 조회
    @GetMapping("/home/{empId}")
    public ResponseEntity<String> MainPageInquiry(@PathVariable Long empId) {
        EmployeeDTO employeeDTO = employeeService.findByempId(empId);
        if (employeeDTO == null) {
            String json = "{ \"resultCode\": \"400\" }";
            return new ResponseEntity<>(json, HttpStatus.OK);
        }
        EMPAttendanceDTO empAttendanceDTO = empAttendanceService.findByempId(empId);
        EMPSeatDTO empSeatDTO = empSeatService.findByempId(empId);
        ScheduleDTO scheduleDTO = scheduleService.findRecentByEmpId(empId);

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
        if (scheduleDTO != null){
            json.put("schStart", scheduleDTO.getStart());
            json.put("schHead", scheduleDTO.getHead());
        }
        else{
            json.put("schStart", "");
            json.put("schHead", "");
        }

        String jsonString = json.toString();
        System.out.println("[매인 페이지 조희] 성공");
        return new ResponseEntity<>(jsonString, HttpStatus.OK);
    }


    // 자동 예약 요청
    @GetMapping("/home/{empId}/first")
    public ResponseEntity<String> FirstInquiry(@PathVariable Long empId) {
          EMPSeatDTO empSeatDTO = empSeatService.findByempId(empId);

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
        if(empSeatDTO.getSeatId() != null) {
            jsonObject.put("seatId", empSeatDTO.getSeatId());
            System.out.println("[자동 예약 요청] 성공");
        }
        else {
            jsonObject.put("seatId", "");
            System.out.println("[자동 예약 요청] 실패");
        }

        jsonObject.put("reserveSuccess", reserveSuccess);

        String json = jsonObject.toString();
        return new ResponseEntity<>(json, HttpStatus.OK);
    }


    // 비밀번호 변경
    @PutMapping("/home/{empId}/password")
    public ResponseEntity<String> PasswordChange(@PathVariable Long empId, @RequestBody Map<String, Object> requestBody) {
        EmployeeDTO employeeDTO = employeeService.findByempId(empId);
        
        String password = (String) requestBody.get("password");

        if(!employeeDTO.getPassword().equals(password)){  // 비밀번호 틀릴 경우
            JSONObject jsonObject = new JSONObject();
            System.out.println("[비밀번호 변경] 비밀번호 틀림");
            jsonObject.put("resultCode","P201");
            String json = jsonObject.toString();
            return new ResponseEntity<>(json, HttpStatus.OK);
        }
        
        String newpassword = requestBody.get("newpassword").toString();
        
        employeeDTO.setPassword(newpassword);
        employeeService.save(employeeDTO);

        JSONObject jsonObject = new JSONObject();
        System.out.println("[비밀번호 변경] 성공");
        jsonObject.put("resultCode","P101");
        String json = jsonObject.toString();
        return new ResponseEntity<>(json, HttpStatus.OK);

    }

    // 자동 예약 설정 변경
    @PutMapping("/home/{empId}/auto")
    public ResponseEntity<String> AutoBookChange(@PathVariable Long empId, @RequestBody Map<String, Object> requestBody) {
        EMPSeatDTO empSeatDTO = empSeatService.findByempId(empId);

        Boolean autoBook = (boolean) requestBody.get("autoBook");
        empSeatDTO.setAutoBook(autoBook);
        empSeatService.save(empSeatDTO);

        String json;

        if(autoBook) {
            System.out.println("[자동 예약 토글] ON");
            json = "{ \"resultCode\": \"P101\" }";
        }
        else{
            System.out.println("[자동 예약 토글] OFF");
            json = "{ \"resultCode\": \"P102\" }";
        }
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

//    // 자리 비움 토글
//    @PutMapping("/home/{empId}/away")
//    public ResponseEntity<String> AwayToggle(@PathVariable Long empId, @RequestBody Map<String, Object> requestBody) {
//        Byte Status = Byte.valueOf(requestBody.get("status").toString());
//        // 자리비움 status = 2
//        // 자리비움 off status = 1
//        EMPAttendanceDTO empAttendanceDTO = empAttendanceService.findByempId(empId);
//        empAttendanceDTO.setStatus(Status);
//        empAttendanceService.save(empAttendanceDTO);
//
//        EmployeeDTO employeeDTO = employeeService.findByempId(empId);
//        DepartmentDTO departmentDTO = departmentService.findByTeamId(employeeDTO.getTeamId());
//        EMPSeatDTO empSeatDTO = empSeatService.findByempId(empId);
//
//        String nickname = employeeDTO.getNickname();
//        Long personalDeskHeight = empSeatDTO.getPersonalDeskHeight();
//        String teamName = departmentDTO.getTeamName();
//
//        Byte status = empAttendanceDTO.getStatus();
//
//        Long prevSeat = empSeatDTO.getPrevSeat();
//        DeskDTO byPrevSeat = deskService.findByseatId(prevSeat);
//        String seatIp = byPrevSeat.getSeatIp();
//
//        // 모션데스킹 활동 요청 소켓 메세지
//        String socketMsg = "x,"+ nickname +","+ personalDeskHeight +","+ teamName +","+ status;
//        webSocketChatHandler.sendMessageToSpecificIP(seatIp, socketMsg);
//
//        String json = "{ \"resultCode\": \"201\" }";
//        return new ResponseEntity<>(json, HttpStatus.OK);
//    }


    // 출근
    @PutMapping("home/att")
    public ResponseEntity<String> AttRequest(@RequestBody Map<String, Object> requestBody){
        String empIdCard = requestBody.get("empIdCard").toString();

        // 카드로 사용자 정보 가져옴.
        EmployeeDTO employeeDTO = employeeService.findByEmpIdCard(empIdCard);
        if(employeeDTO == null){
            System.out.println("[출근] 없는 사용자 입니다.");
            String json = "{ \"resultCode\": \"P201\" }";
            return new ResponseEntity<>(json, HttpStatus.OK);
        }
        EMPAttendanceDTO empAttendanceDTO = empAttendanceService.findByempId(employeeDTO.getEmpId());

        // 현재 시간 출근 시간에 저장
        LocalTime currentTime;
        currentTime =LocalTime.now();
        Time time = Time.valueOf(currentTime);

        empAttendanceDTO.setWorkAttTime(time);
        empAttendanceDTO.setStatus(Byte.valueOf("1"));
        empAttendanceService.save(empAttendanceDTO);

        String timeString = time.toString();
        System.out.println(String.format("[출근] 성공 시간 : %s ",timeString));
        String json = "{ \"resultCode\": \"P101\" }";
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
        if(empAttendanceDTO.getWorkAttTime() == null){
            System.out.println("[퇴근] 출근 안한 상태");
            String json = "{ \"resultCode\": \"P201\" }";
            return new ResponseEntity<>(json, HttpStatus.OK);
        }

        empAttendanceDTO.setWorkEndTime(time);
        empAttendanceDTO.setStatus(Byte.valueOf("0"));
        empAttendanceService.save(empAttendanceDTO);
        
        // 예약 취소
        EMPSeatDTO empSeatDTO = empSeatService.findByempId(empId);
        if(empSeatDTO.getSeatId() != null)
            SeatCancel(empId);


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("workEndTime",time);
        jsonObject.put("resultCode","P101");
        String json = jsonObject.toString();
        System.out.println("[퇴근] 성공");
        return new ResponseEntity<>(json, HttpStatus.OK);
    }


    // 선호 책상 높이 변경
    @PutMapping("home/{empId}/mydesk")
    public ResponseEntity<String> ChangeDeskHeight(@PathVariable Long empId){

        DeskDTO deskDTO = deskService.findByEmpId(empId);
        if(deskDTO == null){
            System.out.println("[책상 높이 변경] 연결된 책상이 없음");
            String json = "{ \"resultCode\": \"D201\" }";
            return new ResponseEntity<>(json, HttpStatus.OK);
        }

        Long personalDeskHeight = deskDTO.getDeskHeightNow();

        EMPSeatDTO empSeatDTO = empSeatService.findByempId(empId);
        empSeatDTO.setPersonalDeskHeight(personalDeskHeight);
        empSeatService.save(empSeatDTO);

        System.out.println("[책상 높이 변경] 성공");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("resultCode","D101");
        jsonObject.put("personalDeskHeight",personalDeskHeight);
        String json = jsonObject.toString();
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    // 아두이노로 책상 높이 조절 명령
    @PutMapping("home/{empId}/mydesk/move")
    public ResponseEntity<String> MoveDeskHeight(@PathVariable Long empId){

        EmployeeDTO employeeDTO = employeeService.findByempId(empId);
        EMPAttendanceDTO empAttendanceDTO = empAttendanceService.findByempId(empId);
        EMPSeatDTO empSeatDTO = empSeatService.findByempId(empId);
        DepartmentDTO departmentDTO = departmentService.findByTeamId(employeeDTO.getTeamId());

        String nickname = employeeDTO.getNickname();
        Long personalDeskHeight = empSeatDTO.getPersonalDeskHeight();
        String teamName = departmentDTO.getTeamName();
        Byte status = empAttendanceDTO.getStatus();

        if(empSeatDTO.getSeatId() == null){
            System.out.println("[높이 조절 요청] 연결된 책상이 없음");
            String json = "{ \"resultCode\": \"D201\" }";
            return new ResponseEntity<>(json, HttpStatus.OK);
        }

        Long seatId = empSeatDTO.getSeatId();
        DeskDTO deskDTO = deskService.findByseatId(seatId);
        String seatIp = deskDTO.getSeatIp();

        String socketMsg;
        if (personalDeskHeight == null) {
            System.out.println("[높이 조절 요청] 선호 높이가 없음");
            String json = "{ \"resultCode\": \"D202\" }";
            return new ResponseEntity<>(json, HttpStatus.OK);
        }
        else {
            socketMsg = "g,"+ nickname +","+ personalDeskHeight +","+ teamName +","+ status;
        }
        // 모션데스킹 활동 요청 소켓 메세지
        webSocketChatHandler.sendMessageToSpecificIP(seatIp, socketMsg);

        System.out.println("[높이 조절 요청] 성공");
        String json = "{ \"resultCode\": \"D101\" }";
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    // 층별로 자리 불러오기
    @GetMapping("seats/{floor}")
    public ResponseEntity<String> SeatsByFloor(@PathVariable Long floor){
        List<DeskDTO> deskDTOList = deskService.findByFloor(floor);

        JSONArray jsonArray = new JSONArray();
        for(DeskDTO deskDTO : deskDTOList){

            JSONObject json = new JSONObject();

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
                json.put("status", 0);
                jsonArray.put(json);
            }
        }
        String jsonString = jsonArray.toString();
        System.out.println("[전체 자리 현황] 성공");
        return new ResponseEntity<>(jsonString, HttpStatus.OK);
    }

    // 자리 선택 (예약)
    @PutMapping("seats")
    public ResponseEntity<String> SeatReservation(@RequestBody Map<String, Object> requestBody){
        Long empId = Long.valueOf(requestBody.get("empId").toString());
        Long seatId = Long.valueOf(requestBody.get("seatId").toString());

        DeskDTO deskDTO = deskService.findByseatId(seatId);
        if(deskDTO.getEmpId() != null){ // 이미 쓰고있는 좌석이면
            System.out.println("[좌석 예약] 이미 예약된 좌석");
            String json = "{ \"resultCode\": \"S201\" }";
            return new ResponseEntity<>(json, HttpStatus.OK);
        }
        EMPAttendanceDTO empAttendanceDTO = empAttendanceService.findByempId(empId);
        if(empAttendanceDTO.getWorkAttTime() == null){  // 출근 x
            System.out.println("[좌석 예약] 출근 안한 상태");
            String json = "{ \"resultCode\": \"S202\" }";
            return new ResponseEntity<>(json, HttpStatus.OK);
        }
        if(empAttendanceDTO.getWorkEndTime() != null){  // 퇴근 상태
            System.out.println("[좌석 예약] 퇴근 상태");
            String json = "{ \"resultCode\": \"S203\" }";
            return new ResponseEntity<>(json, HttpStatus.OK);
        }

        EMPSeatDTO empSeatDTO = empSeatService.findByempId(empId);

        deskDTO.setEmpId(empId);
        empSeatDTO.setSeatId(seatId);
        empSeatDTO.setPrevSeat(seatId);
        empAttendanceDTO.setStatus(Byte.valueOf("1"));

        deskService.save(deskDTO);
        empSeatService.save(empSeatDTO);
        empAttendanceService.save(empAttendanceDTO);

        EmployeeDTO employeeDTO = employeeService.findByempId(empId);
        DepartmentDTO departmentDTO = departmentService.findByTeamId(employeeDTO.getTeamId());

        String nickname = employeeDTO.getNickname();
        Long personalDeskHeight = empSeatDTO.getPersonalDeskHeight();
        String teamName = departmentDTO.getTeamName();
        Byte status = empAttendanceDTO.getStatus();
        Long prevSeat = empSeatDTO.getPrevSeat();
        DeskDTO byPrevSeat = deskService.findByseatId(prevSeat);
        String seatIp = byPrevSeat.getSeatIp();
        String socketMsg;
        if (personalDeskHeight == null) {
            socketMsg = "g,"+ nickname +","+ "-1" +","+ teamName +","+ status;
        }
        else {
            socketMsg = "g,"+ nickname +","+ personalDeskHeight +","+ teamName +","+ status;
        }
        // 모션데스킹 활동 요청 소켓 메세지
        webSocketChatHandler.sendMessageToSpecificIP(seatIp, socketMsg);


        System.out.println("[좌석 예약] 예약 성공");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("resultCode","S101");
        jsonObject.put("empId",empId);
        jsonObject.put("seatId",seatId);
        String json = jsonObject.toString();
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    // 자리 변경
    @PutMapping("seats/change")
    public ResponseEntity<String> SeatChange(@RequestBody Map<String, Object> requestBody) {
        Long empId = Long.valueOf(requestBody.get("empId").toString());
        Long NewseatId = Long.valueOf(requestBody.get("seatId").toString());

        DeskDTO NewdeskDTO = deskService.findByseatId(NewseatId);
        if(NewdeskDTO.getEmpId() != null){ // 이미 쓰고있는 좌석이면
            System.out.println("[좌석 변경] 이미 쓰고 있는 좌석");
            String json = "{ \"resultCode\": \"S201\" }";
            return new ResponseEntity<>(json, HttpStatus.OK);
        }
        // 새로운 자리 저장
        NewdeskDTO.setEmpId(empId);
        deskService.save(NewdeskDTO);

        // 기존 자리 취소
        EMPSeatDTO empSeat = empSeatService.findByempId(empId);
        DeskDTO cancelDesk = deskService.findByseatId(empSeat.getSeatId());
        cancelDesk.setEmpId(null);
        deskService.save(cancelDesk);

        // 자리 정보 갱신
        empSeat.setSeatId(NewseatId);
        empSeat.setPrevSeat(NewseatId);
        empSeatService.save(empSeat);

        System.out.println("[좌석 변경] 변경 성공");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("resultCode","S101");
        jsonObject.put("empId",empId);
        jsonObject.put("seatId",NewseatId);
        String json = jsonObject.toString();
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    // 자리 예약 취소
    @DeleteMapping("seats/{empId}")
    public ResponseEntity<String> SeatCancel(@PathVariable Long empId) {

        // 자리 취소
        EMPSeatDTO empSeat = empSeatService.findByempId(empId);
        Long CancelSeatId = empSeat.getSeatId();
        DeskDTO cancelDesk = deskService.findByseatId(CancelSeatId);
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

        System.out.println("[좌석 취소] 성공");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("resultCode","S101");
        jsonObject.put("empId",empId);
        jsonObject.put("seatId",CancelSeatId);
        String json = jsonObject.toString();
        return new ResponseEntity<>(json, HttpStatus.OK);
    }


    // 개인 스케줄 확인 (일별로)
    @GetMapping("schedule/{empId}/{year}/{month}/{day}")
    public ResponseEntity<String> GetScheduleDay(@PathVariable Long empId, @PathVariable Long year, @PathVariable Long month, @PathVariable Long day){

        LocalDate localDate = LocalDate.of(year.intValue(), month.intValue(), day.intValue());
        List<ScheduleDTO> scheduleDTOList = scheduleService.findByDate(localDate, empId);
        JSONArray jsonArray = new JSONArray();

        if(scheduleDTOList == null){
            JSONObject json = new JSONObject();
            json.put("resultCode","S201");
            jsonArray.put(json);
            String jsonstring = jsonArray.toString();
            System.out.println("[스케줄 일 조회] 스케줄 없음");
            return new ResponseEntity<>(jsonstring,HttpStatus.OK);
        }
        

        for(ScheduleDTO scheduleDTO : scheduleDTOList){

            JSONObject json = new JSONObject();

            json.put("schId", scheduleDTO.getSchId());
            json.put("head", scheduleDTO.getHead());
            json.put("start", scheduleDTO.getStart());
            json.put("end", scheduleDTO.getEnd());
            json.put("status", scheduleDTO.getStatus());
            if(scheduleDTO.getDetail() != null)
                json.put("detail", scheduleDTO.getDetail());
            else
                json.put("detail","");

            jsonArray.put(json);
        }
        String jsonString = jsonArray.toString();
        System.out.println("[스케줄 일 조회] 성공");
        return new ResponseEntity<>(jsonString,HttpStatus.OK);

    }


    // 개인 스케줄 확인하기 (월별로)
    @GetMapping("schedule/{empId}/{year}/{month}")
    public ResponseEntity<String> GetScheduleMonth(@PathVariable Long empId,@PathVariable Long year, @PathVariable Long month){
        List<ScheduleDTO> scheduleDTOList = scheduleService.findByMonth(year, month, empId);


        JSONArray jsonArray = new JSONArray();

        if(scheduleDTOList == null){
            JSONObject json = new JSONObject();
            json.put("resultCode","S201");
            String jsonstring = json.toString();
            System.out.println("[스케줄 월 조회] 스케줄 없음");
            return new ResponseEntity<>(jsonstring,HttpStatus.OK);
        }
        for(ScheduleDTO scheduleDTO : scheduleDTOList){

            JSONObject json = new JSONObject();

            json.put("schId", scheduleDTO.getSchId());
            json.put("head", scheduleDTO.getHead());
            json.put("start", scheduleDTO.getStart());
            json.put("end", scheduleDTO.getEnd());
            json.put("status", scheduleDTO.getStatus());
            if(scheduleDTO.getDetail() != null)
                json.put("detail", scheduleDTO.getDetail());
            else
                json.put("detail","");

            jsonArray.put(json);
        }
        String jsonString = jsonArray.toString();
        System.out.println("[스케줄 월 조회] 성공");
        return new ResponseEntity<>(jsonString,HttpStatus.OK);
    }

    // 스케줄 등록하기
    @PostMapping("schedule/{empId}")
    public ResponseEntity<String> RegistSchedule(@PathVariable Long empId, @RequestBody Map<String,Object> requestBody){

        if(requestBody.get("head") == null || requestBody.get("head")==""){
            System.out.println("[스케줄 등록] 제목이 없습니다.");
            String json = "{ \"resultCode\": \"S201\" }";
            return new ResponseEntity<>(json, HttpStatus.OK);
        }
        String head = requestBody.get("head").toString();
        java.sql.Timestamp start = Timestamp.valueOf(requestBody.get("start").toString());
        java.sql.Timestamp end = Timestamp.valueOf(requestBody.get("end").toString());
        if(start.after(end)){
            System.out.println("[스케줄 등록] 종료 시간이 시작 시간보다 빠릅니다.");
            String json = "{ \"resultCode\": \"S203\" }";
            return new ResponseEntity<>(json, HttpStatus.OK);
        }

        Byte status = Byte.valueOf(requestBody.get("status").toString());
        String detail ="";
        if(requestBody.get("detail")!=null)
             detail = requestBody.get("detail").toString();

        ScheduleDTO NewscheduleDTO =  new ScheduleDTO();
        NewscheduleDTO.setHead(head);
        NewscheduleDTO.setEmpId(empId);
        NewscheduleDTO.setStart(start);
        NewscheduleDTO.setEnd(end);
        NewscheduleDTO.setStatus(status);
        NewscheduleDTO.setDetail(detail);
        scheduleService.save(NewscheduleDTO);

        LocalDateTime now = LocalDateTime.now();
        if(start.toLocalDateTime().toLocalDate().equals(now.toLocalDate()) && status == 2){
            DailyScheduleDTO dailyScheduleDTO = new DailyScheduleDTO();
            dailyScheduleDTO.setEmpId(empId);
            dailyScheduleDTO.setStartTime(start);
            dailyScheduleDTO.setEndTime(end);
            dailyScheduleService.save(dailyScheduleDTO);
        }
        webSocketChatHandler.CheckAFK();
        webSocketChatHandler.SendChangeStatus(empId);
        
        System.out.println("[스케줄 등록] 성공");
        String json = "{ \"resultCode\": \"S101\" }";
        return new ResponseEntity<>(json, HttpStatus.OK);
    }
    
    // 스케줄 수정하기
    @PutMapping("schedule/{empId}/{schId}")
    public ResponseEntity<String> EditSchedule(@PathVariable Long empId, @PathVariable Long schId, @RequestBody Map<String,Object> requestBody){
        if(requestBody.get("head") == null || requestBody.get("head")==""){
            System.out.println("[스케줄 수정] 제목이 없습니다.");
            String json = "{ \"resultCode\": \"S201\" }";
            return new ResponseEntity<>(json, HttpStatus.OK);
        }
        String head = requestBody.get("head").toString();
        java.sql.Timestamp start = Timestamp.valueOf(requestBody.get("start").toString());
        java.sql.Timestamp end = Timestamp.valueOf(requestBody.get("end").toString());
        if(start.after(end)){
            System.out.println("[스케줄 수정] 종료 시간이 시작 시간보다 빠릅니다.");
            String json = "{ \"resultCode\": \"S203\" }";
            return new ResponseEntity<>(json, HttpStatus.OK);
        }
        
        Byte status = Byte.valueOf(requestBody.get("status").toString());
        String detail ="";
        if(requestBody.get("detail")!=null)
            detail = requestBody.get("detail").toString();


        ScheduleDTO scheduleDTO = scheduleService.findBySchId(schId);

        dailyScheduleService.DeleteBySchId(empId, scheduleDTO.getStart(), scheduleDTO.getEnd());

        scheduleDTO.setHead(head);
        scheduleDTO.setEmpId(empId);
        scheduleDTO.setStart(start);
        scheduleDTO.setEnd(end);
        scheduleDTO.setStatus(status);
        scheduleDTO.setDetail(detail);
        scheduleService.save(scheduleDTO);

        LocalDateTime now = LocalDateTime.now();
        if(start.toLocalDateTime().toLocalDate().equals(now.toLocalDate()) && status == 2){
            DailyScheduleDTO dailyScheduleDTO = new DailyScheduleDTO();
            dailyScheduleDTO.setEmpId(empId);
            dailyScheduleDTO.setStartTime(start);
            dailyScheduleDTO.setEndTime(end);
            dailyScheduleService.save(dailyScheduleDTO);
        }
        webSocketChatHandler.CheckAFK();

        webSocketChatHandler.SendChangeStatus(empId);

        System.out.println("[스케줄 수정] 성공");
        String json = "{ \"resultCode\": \"S101\" }";
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    // 스케줄 삭제하기
    @DeleteMapping("schedule/{empId}/{schId}")
    public ResponseEntity<String> DeleteSchedule(@PathVariable Long empId, @PathVariable Long schId){
        if(scheduleService.findBySchId(schId) == null){
            System.out.println("[스케줄 삭제] 없는 사용자");
            String json = "{ \"resultCode\": \"S201\" }";
            return new ResponseEntity<>(json, HttpStatus.OK);
        }
        ScheduleDTO scheduleDTO = scheduleService.findBySchId(schId);

        dailyScheduleService.DeleteBySchId(scheduleDTO.getEmpId(),scheduleDTO.getStart(),scheduleDTO.getEnd());
        webSocketChatHandler.CheckAFK();

        webSocketChatHandler.SendChangeStatus(empId);

        scheduleService.deleteSchedule(schId);
        System.out.println("[스케줄 삭제] 성공");
        String json = "{ \"resultCode\": \"S101\" }";
        return new ResponseEntity<>(json, HttpStatus.OK);
    }
    
    // 책상 높이 초기화
    @GetMapping("reset/{empId}")
    public ResponseEntity<String> ResetHeight(@PathVariable long empId){
        DeskDTO deskDTO = deskService.findByseatId(201l);

        String message = "a,,,,";
        webSocketChatHandler.sendMessageToSpecificIP(deskDTO.getSeatIp(), message);

        System.out.println("[책상 높이 초기화] 성공");
        String json = "{ \"resultCode\": \"S101\" }";
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

}
