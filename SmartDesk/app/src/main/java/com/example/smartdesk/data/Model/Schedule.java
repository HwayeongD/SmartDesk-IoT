package com.example.smartdesk.data.Model;

import com.google.gson.annotations.SerializedName;

public class Schedule {

    @SerializedName("resultCode")
    private String resultCode;

    @SerializedName("schId")
    private Long schId;

    @SerializedName("head")
    private String head;

    @SerializedName("start")
    private String start;

    @SerializedName("end")
    private String end;

    @SerializedName("status")
    private int status; // 0:Blank, 1:Present(Online), 2:Absence(Offline)

    @SerializedName("detail")
    private String detail;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public Long getSchId() {
        return schId;
    }

    public void setSchId(Long schId) {
        this.schId = schId;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "Id: " + getSchId() +
                ", Head: " + getHead() +
                ", Start: " + getStart() +
                ", End: " + getEnd() +
                ", Status: " + getStatus() +
                ", Detail: " + getDetail() +
                ", ResultCode: " + getResultCode();
    }

}
