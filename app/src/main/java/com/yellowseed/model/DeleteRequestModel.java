package com.yellowseed.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pushpender.singh on 25/7/18.
 */

public class DeleteRequestModel implements Serializable{
    @SerializedName("message_ids")
    @Expose
    private List<String> message_ids;
    @SerializedName("room_id")
    @Expose
    private String room_id;
    @SerializedName("do")
    @Expose
    private String _do;

    public List<String> getMessage_ids() {
        return message_ids;
    }

    public void setMessage_ids(List<String> message_ids) {
        this.message_ids = message_ids;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String get_do() {
        return _do;
    }

    public void set_do(String _do) {
        this._do = _do;
    }
}
