package com.example.smartdesk.data.Model;

import com.google.gson.annotations.SerializedName;

public class Schedule {
    @SerializedName("schId")
    private String schId;

    @SerializedName("schHead")
    private String schHead;

    @SerializedName("schStart")
    private String schStart;

    @SerializedName("schEnd")
    private String schEnd;

    @SerializedName("status")
    private String status;

    @SerializedName("schDetail")
    private String schDetail;


    public String getSchId() { return schId; }

    public String getSchHead() { return schHead; }

    public void setSchHead(String schHead) { this.schHead = schHead; }

    public String getSchStart() { return schStart; }

    public void setSchStart(String schStart) { this.schStart = schStart; }

    public String getSchEnd() { return schEnd; }

    public void setSchEnd(String schEnd) { this.schEnd = schEnd; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public String getSchDetail() { return schDetail; }

    public void setSchDetail(String schDetail) { this.schDetail = schDetail; }

    @Override
    public String toString() {
        return "Id: " + getSchId() +
                ", Head: " + getSchHead() +
                ", Start: " + getSchStart() +
                ", End: " + getSchEnd() +
                ", Status: " + getStatus() +
                ", Detail: " + getSchDetail();
    }

}
