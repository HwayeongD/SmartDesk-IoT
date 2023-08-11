package com.example.smartdesk.ui.seat;

import com.example.smartdesk.data.Model.Seat;

import java.util.ArrayList;

public class SeatItem {
    String seatId;
    String nickname;
    String teamName;
    int status; // 0:Blank, 1:Present(Online), 2:Absence(Offline)

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
