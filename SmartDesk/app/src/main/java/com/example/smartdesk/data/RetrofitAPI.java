package com.example.smartdesk.data;

import com.example.smartdesk.data.Model.Employee;
import com.example.smartdesk.data.Model.Schedule;
import com.example.smartdesk.data.Model.ReqEmployee;
import com.example.smartdesk.data.Model.Seat;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RetrofitAPI {
    // 로그인 요청
    @POST("login")
    @Headers("Content-type: application/json")
    Call<Employee> getLoginAccess(@Body Employee employee);

    // 메인페이지 접속 - 사원 데이터 요청
    @GET("home/{empId}")
    Call<Employee> getEmpData(@Path("empId") String empId);

    // 이전 좌석으로 자동 예약 요청
    @GET("home/{empId}/first")
    Call<Employee> reqAutoReserveSeat(@Path("empId") String empId);

    // 좌석 자동 예약 토글 변경 요청
    @PUT("home/{empId}/auto")
    Call<Employee> reqChangeAutoToggle(@Path("empId") String empId, @Body Employee employee);

    // 선호하는 책상 높이 변경
    @PUT("home/{empId}/mydesk")
    Call<Employee> reqChangeDeskHeight(@Path("empId") String empId);

    // 선호하는 책상 높이로 조절
    @PUT("home/{empId}/mydesk/move")
    Call<Employee> reqMoveDeskHeight(@Path("empId") String empId);

    // 퇴근 처리 요청
    @PUT("home/{empId}/leave")
    Call<Employee> reqLeave(@Path("empId") String empId);

    // 전체 좌석 현황 요청
    @GET("seats/{floor}")
    Call<List<Seat>> reqFloorSeat(@Path("floor") int floor);

    // 좌석 예약 요청
    @PUT("seats")
    Call<ReqEmployee> reqReserveSeat(@Body ReqEmployee employee);

    // 좌석 변경 요청
    @PUT("seats/change")
    Call<Employee> reqChangeSeat(@Body ReqEmployee employee);

    // 좌석 취소 요청
    @DELETE("seats/{empId}")
    Call<Employee> reqCancelSeat(@Path("empId") String empId);

    // 스케쥴 조회
    @GET("schedule/{empId}/{year}/{month}")
    Call<List<Schedule>> getSchedule(@Path("empId") String empId, @Path("year") int year, @Path("month") int month);

    @GET("schedule/{empId}/{year}/{month}/{day}")
    Call<List<Schedule>> getScheduleByDate(@Path("empId") String empId, @Path("year") int year, @Path("month") int month, @Path("day") int day);

    // 새로운 스케쥴 추가
    @POST("schedule/{empId}")
    Call<Schedule> reqCreateSchedule(@Path("empId") String empId, @Body Schedule newSchedule);

    // 스케쥴 수정
    @PUT("schedule/{empId}/{schId}")
    Call<Schedule> reqUpdateSchedule(@Path("empId") String empId, @Path("schId") String schId, @Body Schedule updatedSchedule);

    // 스케쥴 삭제
    @DELETE("schedule/{empId}/{schId}")
    Call<Schedule> reqDeleteSchedule(@Path("empId") String empId, @Path("schId") String schId);

    // 비밀번호 변경
    @PUT("home/{empId}/password")
    Call<Employee> reqChangePassword(@Path("empId") String empId, @Body Employee newPassword);
}
