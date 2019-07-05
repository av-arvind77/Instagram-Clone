package com.yellowseed.fcm;

import android.app.Notification;

import com.yellowseed.model.PostNotificationResponse;
import com.yellowseed.webservices.response.post.PostResponse;

import java.io.Serializable;

/**
 * Created by ashutosh on 15/2/18.
 */

public class FCMResponse implements Serializable {


    private String body;
    private String alert_type;
    private String  notification_type;
    private String  username;
    private String  id;
    private String  image;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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

    public String getNotification_type() {
        return notification_type;
    }

    public void setNotification_type(String notification_type) {
        this.notification_type = notification_type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAlert_type() {
        return alert_type;
    }

    public void setAlert_type(String alert_type) {
        this.alert_type = alert_type;
    }

}
