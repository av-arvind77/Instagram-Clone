package com.yellowseed.webservices.response.post;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentArr implements Parcelable
{

    @SerializedName("comment_id")
    @Expose
    private String commentId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("user_id")
    @Expose
    private UserId userId;
    public final static Parcelable.Creator<CommentArr> CREATOR = new Creator<CommentArr>() {


        @SuppressWarnings({
                "unchecked"
        })
        public CommentArr createFromParcel(Parcel in) {
            return new CommentArr(in);
        }

        public CommentArr[] newArray(int size) {
            return (new CommentArr[size]);
        }

    }
            ;

    protected CommentArr(Parcel in) {
        this.commentId = ((String) in.readValue((String.class.getClassLoader())));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.userId = ((UserId) in.readValue((UserId.class.getClassLoader())));
    }

    public CommentArr() {
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(commentId);
        dest.writeValue(title);
        dest.writeValue(userId);
    }

    public int describeContents() {
        return 0;
    }

}

