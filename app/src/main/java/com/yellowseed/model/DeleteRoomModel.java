package com.yellowseed.model;

import java.io.Serializable;

public class DeleteRoomModel implements Serializable{
    private String roomId;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
