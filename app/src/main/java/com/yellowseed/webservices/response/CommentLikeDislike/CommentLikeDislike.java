package com.yellowseed.webservices.response.CommentLikeDislike;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by rajat_mobiloitte on 28/6/18.
 */

public class CommentLikeDislike {

    @Expose
    private Integer responseCode;
    @SerializedName("responseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("like")
    @Expose
    private Like like;

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

    public Like getLike() {
        return like;
    }

    public void setLike(Like like) {
        this.like = like;
    }
}
