package com.yellowseed.model.reqModel;

import java.io.Serializable;

/**
 * Created by ankit_mobiloitte on 5/6/18.
 */

public  class DeviceModel implements Serializable{
    private String device_type;
    private String device_token;

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }
}
