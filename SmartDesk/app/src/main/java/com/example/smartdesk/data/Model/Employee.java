package com.example.smartdesk.data.Model;

import com.google.gson.annotations.SerializedName;

public class Employee {
    private static Employee employee;

    public static Employee getInstance() {
        if(employee == null) {
            employee = new Employee();
        }
        if(employee.getPersonalAutoReserve() == null) {
            employee.setPersonalAutoReserve(true);
        }

        return employee;
    }

    @SerializedName("resultCode")
    private String resultCode;

    @SerializedName("empId")
    private Long empId;

    @SerializedName("password")
    private String password;

    @SerializedName("nickname")
    private String nickname;

    @SerializedName("image")
    private String image;

    @SerializedName("workAttTime")
    private String workAttTime;

    @SerializedName("calTime")
    private String calTime;

    @SerializedName("calDetail")
    private String calDetail;

    @SerializedName("seatId")
    private String seatId;

    @SerializedName("reserveSuccess")
    private Boolean reserveSuccess;

    @SerializedName("personalDeskHeight")
    private String personalDeskHeight;

    @SerializedName("personalAutoReserve")
    private Boolean personalAutoReserve;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getWorkAttTime() {
        return workAttTime;
    }

    public void setWorkAttTime(String workAttTime) {
        this.workAttTime = workAttTime;
    }

    public String getCalTime() {
        return calTime;
    }

    public void setCalTime(String calTime) {
        this.calTime = calTime;
    }

    public String getCalDetail() {
        return calDetail;
    }

    public void setCalDetail(String calDetail) {
        this.calDetail = calDetail;
    }

    public String getSeatId() {
        return seatId;
    }

    public void setSeatId(String seatId) {
        this.seatId = seatId;
    }

    public Boolean getReserveSuccess() {
        return reserveSuccess;
    }

    public void setReserveSuccess(Boolean reserveSuccess) {
        this.reserveSuccess = reserveSuccess;
    }

    public String getPersonalDeskHeight() {
        return personalDeskHeight;
    }

    public void setPersonalDeskHeight(String personalDeskHeight) {
        this.personalDeskHeight = personalDeskHeight;
    }

    public Boolean getPersonalAutoReserve() {
        return personalAutoReserve;
    }

    public void setPersonalAutoReserve(Boolean personalAutoReserve) {
        this.personalAutoReserve = personalAutoReserve;
    }

    public String printEmpData() {
        return "empId: " + getEmpId()
                + ", nickname: " + getNickname()
                + ", workAttTime: " + getWorkAttTime()
                + ", calTime: " + getCalTime()
                + ", calDetail: " + getCalDetail()
                + ", seatId: " + getSeatId()
                + ", personalDeskHeight" + getPersonalDeskHeight();
    }
}
