package com.yellowseed.model.reqModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by rajat_mobiloitte on 2/7/18.
 */


public class Share {

    @SerializedName("post_id")
    @Expose
    private String postId;
    private String share_id;


    public String getShare_id() {
        return share_id;
    }

    public void setShare_id(String share_id) {
        this.share_id = share_id;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

}
