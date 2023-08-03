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

    @GET("home/{empId}")
    Call<Employee> getEmpData(@Path("empId") String empId);

    @GET("home/{empId}/first")
    Call<Employee> reqAutoReserveSeat(@Path("empId") String empId);
}
