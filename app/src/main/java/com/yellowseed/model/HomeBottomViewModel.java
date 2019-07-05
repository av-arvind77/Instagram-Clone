package com.yellowseed.model;

public class HomeBottomViewModel {

    private int profileImage;
    private int postImage;
    private String postUserName, postTime, postContent, postLike, postComment, postShare;


    public int getPostImage() {
        return postImage;
    }

    public void setPostImage(int postImage) {
        this.postImage = postImage;
    }

    public int getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(int profileImage) {
        this.profileImage = profileImage;
    }

    public String getPostUserName() {
        return postUserName;
    }

    public void setPostUserName(String postUserName) {
        this.postUserName = postUserName;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getPostLike() {
        return postLike;
    }

    public void setPostLike(String postLike) {
        this.postLike = postLike;
    }

    public String getPostComment() {
        return postComment;
    }

    public void setPostComment(String postComment) {
        this.postComment = postComment;
    }

    public String getPostShare() {
        return postShare;
    }

    public void setPostShare(String postShare) {
        this.postShare = postShare;
    }
}
