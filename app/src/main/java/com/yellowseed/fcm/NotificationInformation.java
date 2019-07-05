package com.yellowseed.fcm;

import com.yellowseed.webservices.response.post.Image;

/**
 * Created by pushpender.singh on 14/8/18.
 */

class NotificationInformation {
    private String id;
    private Image image;
    private String username;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
