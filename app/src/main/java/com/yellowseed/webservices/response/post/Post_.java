package com.yellowseed.webservices.response.post;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Post_ implements Parcelable
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
    @SerializedName("post_type")
    @Expose
    private String postType;
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
    public final static Parcelable.Creator<Post_> CREATOR = new Creator<Post_>() {


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
        this.postType = ((String) in.readValue((String.class.getClassLoader())));
        this.shareType = ((String) in.readValue((String.class.getClassLoader())));
        this.status = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
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

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
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

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(title);
        dest.writeValue(description);
        dest.writeValue(checkIn);
        dest.writeValue(postType);
        dest.writeValue(shareType);
        dest.writeValue(status);
        dest.writeValue(userId);
        dest.writeValue(createdAt);
        dest.writeValue(updatedAt);
    }

    public int describeContents() {
        return 0;
    }

}