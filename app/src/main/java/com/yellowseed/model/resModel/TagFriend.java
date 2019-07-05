package com.yellowseed.model.resModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by rajat_mobiloitte on 2/7/18.
 */

public class TagFriend {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("tagable_id")
    @Expose
    private String tagableId;
    @SerializedName("tagable_type")
    @Expose
    private String tagableType;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTagableId() {
        return tagableId;
    }

    public void setTagableId(String tagableId) {
        this.tagableId = tagableId;
    }

    public String getTagableType() {
        return tagableType;
    }

    public void setTagableType(String tagableType) {
        this.tagableType = tagableType;
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
