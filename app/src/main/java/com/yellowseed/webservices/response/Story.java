package com.yellowseed.webservices.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by rajat_mobiloitte on 27/6/18.
 */

public class Story {


    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("story")
    @Expose
    private Story_ story;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Story_ getStory() {
        return story;
    }

    public void setStory(Story_ story) {
        this.story = story;
    }
}
