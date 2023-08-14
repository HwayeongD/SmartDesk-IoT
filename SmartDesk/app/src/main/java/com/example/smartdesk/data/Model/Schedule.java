package com.example.smartdesk.data.Model;

import com.google.gson.annotations.SerializedName;

public class Schedule {
    @SerializedName("schId")
    private int schId;

    @SerializedName("head")
    private String head;

    @SerializedName("start")
    private String start;

    @SerializedName("end")
    private String end;

    @SerializedName("status")
    private int status;

    @SerializedName("detail")
    private String detail;

    @SerializedName("resultCode")
    private String resultCode;

    public int getSchId() {
        return schId;
    }

    public void setSchId(int schId) {
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

    public String getResultCode() {
        return resultCode;
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
