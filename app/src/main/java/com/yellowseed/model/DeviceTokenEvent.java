package com.yellowseed.model;

/**
 * Created by pushpender.singh on 10/8/18.
 */

public class DeviceTokenEvent {
    private String token;
    public DeviceTokenEvent(String deviceToken) {
        this.token = deviceToken;
    }
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
