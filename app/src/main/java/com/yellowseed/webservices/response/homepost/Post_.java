
package com.yellowseed.webservices.response.homepost;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Post_ implements Serializable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("check_in")
    @Expose
    private String checkIn;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("share_type")
    @Expose
    private String shareType;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    @SerializedName("is_saved")
    @Expose
    private boolean isSaved;
    @SerializedName("isPinned")
    @Expose
    private boolean isPinned;

    public boolean isPinned() {
        return isPinned;
    }

    public void setPinned(boolean pinned) {
        isPinned = pinned;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    public final static Creator<Post_> CREATOR = new Creator<Post_>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Post_ createFromParcel(Parcel in) {
            return new Post_(in);
        }

        public Post_[] newArray(int size) {
            return (new Post_[size]);
        }

    }
    ;

    protected Post_(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.description = ((String) in.readValue((String.class.getClassLoader())));
        this.checkIn = ((String) in.readValue((String.class.getClassLoader())));
        this.latitude = ((String) in.readValue((Object.class.getClassLoader())));
        this.longitude = ((String) in.readValue((Object.class.getClassLoader())));
        this.shareType = ((String) in.readValue((String.class.getClassLoader())));
        this.status = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.isSaved = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.isPinned = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.userId = ((String) in.readValue((String.class.getClassLoader())));
        this.createdAt = ((String) in.readValue((String.class.getClassLoader())));
        this.updatedAt = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Post_() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
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

    public int describeContents() {
        return  0;
    }

}
