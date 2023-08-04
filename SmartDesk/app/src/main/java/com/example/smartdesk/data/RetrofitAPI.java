package com.example.smartdesk.data;

import com.example.smartdesk.data.Model.Employee;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

}
