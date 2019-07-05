package com.yellowseed.model;

import java.io.Serializable;

/**
 * Created by pushpender.singh on 9/8/18.
 */

public class PostNotificationResponse implements Serializable{
    private String id;
    private String image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
