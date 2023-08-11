package com.example.smartdesk.data.Model;

import com.google.gson.annotations.SerializedName;

public class Seat {
    @SerializedName("seatId")
    private String seatId;

    @SerializedName("nickname")
    private String nickname;

    @SerializedName("teamName")
    private String teamName;

    @SerializedName("status")
    private int status;

    public String getSeatId() {
        return seatId;
    }

    public void setSeatId(String seatId) {
        this.seatId = seatId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
