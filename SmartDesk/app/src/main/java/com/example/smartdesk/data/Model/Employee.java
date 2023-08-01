package com.example.smartdesk.data.Model;

import com.google.gson.annotations.SerializedName;

public class Employee {
    @SerializedName("resultCode")
    private String resultCode;

    @SerializedName("nickname")
    private String nickname;

    @SerializedName("empId")
    private Long empId;

    @SerializedName("password")
    private String password;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Long getEmpId() {
        return empId;
    }

    public void setEmpId(Long empId) {
        this.empId = empId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
