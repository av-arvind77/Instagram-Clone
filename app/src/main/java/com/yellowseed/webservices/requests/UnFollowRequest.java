package com.yellowseed.webservices.requests;

import com.yellowseed.webservices.response.User;

/**
 * Created by pushpender.singh on 4/7/18.
 */

public class UnFollowRequest {
    private UserFollow user;

    public UserFollow getUser() {
        return user;
    }

    public void setUser(UserFollow user) {
        this.user = user;
    }
}
