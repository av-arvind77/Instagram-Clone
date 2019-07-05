package com.yellowseed.model.reqModel;

import java.io.Serializable;
import java.util.List;

public class ClearRoomRequest implements Serializable{

    private List<String> room_ids;

    public List<String> getRoom_ids() {
        return room_ids;
    }

    public void setRoom_ids(List<String> room_ids) {
        this.room_ids = room_ids;
    }
}
