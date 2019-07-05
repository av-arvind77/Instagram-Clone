package com.yellowseed.webservices.response.homepost;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.yellowseed.model.resModel.PollDataResponce;

import java.io.Serializable;
import java.util.List;

public class Post implements Serializable {
    @SerializedName("shared_id")
    @Expose
    private String sharedId;
    @SerializedName("poll_data")
    @Expose

    private PollDataResponce pollData;
    @SerializedName("post")
    @Expose
    private Post_ post;
    @SerializedName(value = "images", alternate = {"post_images_attributes"})
    @Expose
    private List<Image> images = null;
    @SerializedName("tag_friends")
    @Expose
    private List<TagFriend> tagFriends = null;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("likes_count")
    @Expose
    private Integer likesCount;
    @SerializedName("current_user_like")
    @Expose
    private boolean currentUserLike;
    @SerializedName("isPinned")
    @Expose
    private boolean isPinned;
    @SerializedName("comments")
    @Expose
    private Integer comments;
    @SerializedName("comment_arr")
    @Expose
    private List<CommentArr> commentArr = null;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    private boolean isSaved;
    private boolean openPopup;
    private String postType;

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public Post() {
    }

    public PollDataResponce getPollData() {
        return pollData;
    }

    public void setPollData(PollDataResponce pollData) {
        this.pollData = pollData;
    }

    public boolean getPinned() {
        return isPinned;
    }

    public void setPinned(boolean pinned) {
        isPinned = pinned;
    }

    public boolean isOpenPopup() {
        return openPopup;
    }

    public void setOpenPopup(boolean openPopup) {
        this.openPopup = openPopup;
    }

    public boolean getSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    public String getSharedId() {
        return sharedId;
    }

    public void setSharedId(String sharedId) {
        this.sharedId = sharedId;
    }

    public Post_ getPost() {
        return post;
    }

    public void setPost(Post_ post) {
        this.post = post;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public List<TagFriend> getTagFriends() {
        return tagFriends;
    }

    public void setTagFriends(List<TagFriend> tagFriends) {
        this.tagFriends = tagFriends;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    public boolean getCurrentUserLike() {
        return currentUserLike;
    }

    public void setCurrentUserLike(boolean currentUserLike) {
        this.currentUserLike = currentUserLike;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    public List<CommentArr> getCommentArr() {
        return commentArr;
    }

    public void setCommentArr(List<CommentArr> commentArr) {
        this.commentArr = commentArr;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int describeContents() {
        return 0;
    }

}
