package com.yellowseed.webservices.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by rajat_mobiloitte on 27/6/18.
 */

public class StoryImage {
    @SerializedName("img")
    @Expose
    private String img;
    @SerializedName("width")
    @Expose
    private String width;
    @SerializedName("height")
    @Expose
    private String height;
    @SerializedName("file_type")
    @Expose
    private String fileType;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
