package com.yellowseed.model.resModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by rajat_mobiloitte on 2/7/18.
 */

public class SharePost {
    @SerializedName("shared_id")
    @Expose
    private String sharedId;
    @SerializedName("post")
    @Expose
    private Post post;
    @SerializedName("post_images")
    @Expose
    private List<Object> postImages = null;
    @SerializedName("tag_friends")
    @Expose
    private List<TagFriend> tagFriends = null;
    @SerializedName("shared_from_user")
    @Expose
    private SharedFromUser sharedFromUser;
    @SerializedName("shared_to_user")
    @Expose
    private SharedToUser sharedToUser;

    public String getSharedId() {
        return sharedId;
    }

    public void setSharedId(String sharedId) {
        this.sharedId = sharedId;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public List<Object> getPostImages() {
        return postImages;
    }

    public void setPostImages(List<Object> postImages) {
        this.postImages = postImages;
    }

    public List<TagFriend> getTagFriends() {
        return tagFriends;
    }

    public void setTagFriends(List<TagFriend> tagFriends) {
        this.tagFriends = tagFriends;
    }

    public SharedFromUser getSharedFromUser() {
        return sharedFromUser;
    }

    public void setSharedFromUser(SharedFromUser sharedFromUser) {
        this.sharedFromUser = sharedFromUser;
    }

    public SharedToUser getSharedToUser() {
        return sharedToUser;
    }

    public void setSharedToUser(SharedToUser sharedToUser) {
        this.sharedToUser = sharedToUser;
    }

}
