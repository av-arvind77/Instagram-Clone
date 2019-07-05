package com.yellowseed.model;

/**
 * Created by pushpender.singh on 17/8/18.
 */

public class RoomModel {
    private String id;
    private String user_id;
    private String live_user_id;
    private String created_at;
    private String updated_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getLive_user_id() {
        return live_user_id;
    }

    public void setLive_user_id(String live_user_id) {
        this.live_user_id = live_user_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
