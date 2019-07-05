package com.yellowseed.webservices.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by rajat_mobiloitte on 27/6/18.
 */

public class Story_ {

    @SerializedName("story_id")
    @Expose
    private String storyId;
    @SerializedName("story_image")
    @Expose
    private StoryImage storyImage;

    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    public StoryImage getStoryImage() {
        return storyImage;
    }

    public void setStoryImage(StoryImage storyImage) {
        this.storyImage = storyImage;
    }
}
