
package com.yellowseed.webservices.response.homepost;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TagFriend implements Serializable
{

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
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public final static Creator<TagFriend> CREATOR = new Creator<TagFriend>() {


        @SuppressWarnings({
            "unchecked"
        })
        public TagFriend createFromParcel(Parcel in) {
            return new TagFriend(in);
        }

        public TagFriend[] newArray(int size) {
            return (new TagFriend[size]);
        }

    }
    ;

    protected TagFriend(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.userId = ((String) in.readValue((String.class.getClassLoader())));
        this.tagableId = ((String) in.readValue((String.class.getClassLoader())));
        this.tagableType = ((String) in.readValue((String.class.getClassLoader())));
        this.createdAt = ((String) in.readValue((String.class.getClassLoader())));
        this.updatedAt = ((String) in.readValue((String.class.getClassLoader())));
    }

    public TagFriend() {
    }

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

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(userId);
        dest.writeValue(tagableId);
        dest.writeValue(tagableType);
        dest.writeValue(createdAt);
        dest.writeValue(updatedAt);
    }

    public int describeContents() {
        return  0;
    }

}
