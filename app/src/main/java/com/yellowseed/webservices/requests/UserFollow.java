package com.yellowseed.webservices.requests;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by pushpender.singh on 4/7/18.
 */

public class UserFollow implements Serializable{
    @SerializedName("id")
    private String id;
    @SerializedName("do")
    private String action;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
