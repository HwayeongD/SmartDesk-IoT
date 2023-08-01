package com.example.smartdesk.data;

import com.example.smartdesk.data.Model.Employee;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RetrofitAPI {
    // 로그인 요청
    @POST("login")
    @Headers("Content-type: application/json")
    Call<Employee> getEmpData(@Body Employee employee);

}
