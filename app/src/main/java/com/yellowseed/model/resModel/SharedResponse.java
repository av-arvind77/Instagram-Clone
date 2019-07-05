package com.yellowseed.model.resModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by rajat_mobiloitte on 2/7/18.
 */

public class SharedResponse {

    @SerializedName("responseCode")
    @Expose
    private Integer responseCode;
    @SerializedName("responseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("share_post")
    @Expose
    private SharePost sharePost;

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public SharePost getSharePost() {
        return sharePost;
    }

    public void setSharePost(SharePost sharePost) {
        this.sharePost = sharePost;
    }

}


