package com.example.smartdesk.data.Model;

import com.google.gson.annotations.SerializedName;

public class ReqEmployee {
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

    @SerializedName("workEndTime")
    private String workEndTime;

    @SerializedName("schStart")
    private String schStart;

    @SerializedName("schHead")
    private String schHead;

    @SerializedName("seatId")
    private String seatId;

    @SerializedName("reserveSuccess")
    private Boolean reserveSuccess;

    @SerializedName("personalDeskHeight")
    private String personalDeskHeight;

    @SerializedName("autoBook")
    private Boolean autoBook;

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

    public String getWorkEndTime() {
        return workEndTime;
    }

    public void setWorkEndTime(String workEndTime) {
        this.workEndTime = workEndTime;
    }


    public String getSchStart() {
        return schStart;
    }

    public void setSchStart(String schStart) {
        this.schStart = schStart;
    }

    public String getSchHead() {
        return schHead;
    }

    public void setSchHead(String schHead) {
        this.schHead = schHead;
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

    public Boolean getAutoBook() {
        return autoBook;
    }

    public void setAutoBook(Boolean autoBook) {
        this.autoBook = autoBook;
    }

    public String printEmpData() {
        return "empId: " + getEmpId()
                + ", nickname: " + getNickname()
                + ", workAttTime: " + getWorkAttTime()
                + ", schStart: " + getSchStart()
                + ", schHead: " + getSchHead()
                + ", seatId: " + getSeatId()
                + ", personalDeskHeight: " + getPersonalDeskHeight()
                + ", autoBook: " + getAutoBook();
    }
}
