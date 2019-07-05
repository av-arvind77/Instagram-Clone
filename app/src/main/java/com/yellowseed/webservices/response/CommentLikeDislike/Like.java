package com.yellowseed.webservices.response.CommentLikeDislike;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by rajat_mobiloitte on 28/6/18.
 */

public class Like {


    @SerializedName("dislikable_id")
    @Expose
    private String dislikableId;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("dislikable_type")
    @Expose
    private String dislikableType;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public String getDislikableId() {
        return dislikableId;
    }

    public void setDislikableId(String dislikableId) {
        this.dislikableId = dislikableId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDislikableType() {
        return dislikableType;
    }

    public void setDislikableType(String dislikableType) {
        this.dislikableType = dislikableType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
