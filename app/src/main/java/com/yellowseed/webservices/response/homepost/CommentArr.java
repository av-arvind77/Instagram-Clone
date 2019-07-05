
package com.yellowseed.webservices.response.homepost;

import java.io.Serializable;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentArr implements Serializable
{

    @SerializedName("comment_id")
    @Expose
    private String commentId;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("user")
    @Expose
    private User_ user;
    @SerializedName("likes_count")
    @Expose
    private Integer likesCount;
    @SerializedName("dislikes_count")
    @Expose
    private Integer dislikesCount;
    @SerializedName("current_user_like")
    @Expose
    private Boolean currentUserLike;
    @SerializedName("current_user_dislike")
    @Expose
    private Boolean currentUserDislike;
    @SerializedName("nested_comment")
    @Expose
    private List<NestedComment> nestedComment = null;

    public CommentArr() {
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User_ getUser() {
        return user;
    }

    public void setUser(User_ user) {
        this.user = user;
    }

    public Integer getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    public Integer getDislikesCount() {
        return dislikesCount;
    }

    public void setDislikesCount(Integer dislikesCount) {
        this.dislikesCount = dislikesCount;
    }

    public Boolean getCurrentUserLike() {
        return currentUserLike;
    }

    public void setCurrentUserLike(Boolean currentUserLike) {
        this.currentUserLike = currentUserLike;
    }

    public Boolean getCurrentUserDislike() {
        return currentUserDislike;
    }

    public void setCurrentUserDislike(Boolean currentUserDislike) {
        this.currentUserDislike = currentUserDislike;
    }

    public List<NestedComment> getNestedComment() {
        return nestedComment;
    }

    public void setNestedComment(List<NestedComment> nestedComment) {
        this.nestedComment = nestedComment;
    }

    public int describeContents() {
        return  0;
    }

}
