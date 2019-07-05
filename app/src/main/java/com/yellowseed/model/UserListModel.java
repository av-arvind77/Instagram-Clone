package com.yellowseed.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import java.io.Serializable;

/**
 * Created by ankit_mobiloitte on 8/6/18.
 */

public class UserListModel implements Serializable {
    private String id;
    private String tagedFriendId;
    private String email;
    private String name;
    private String is_follow;
    private String qb_id;
    private boolean isChecked;
    private String image;
    private int mutual_follower;
    @SerializedName("is_destory")
    @Expose
    private String is_destory;
    @SerializedName("server_id")
    @Expose

    private String server_id;

    public String getQb_id() {
        return qb_id;
    }

    public void setQb_id(String qb_id) {
        this.qb_id = qb_id;
    }

    public String getIs_destory() {
        return is_destory;
    }

    public void setIs_destory(String is_destory) {
        this.is_destory = is_destory;
    }

    public String getServer_id() {
        return server_id;
    }

    public void setServer_id(String server_id) {
        this.server_id = server_id;
    }

    public String getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(String is_follow) {
        this.is_follow = is_follow;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getTagedFriendId() {
        return tagedFriendId;
    }

    public void setTagedFriendId(String tagedFriendId) {
        this.tagedFriendId = tagedFriendId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getMutual_follower() {
        return mutual_follower;
    }

    public void setMutual_follower(int mutual_follower) {
        this.mutual_follower = mutual_follower;
    }


}
