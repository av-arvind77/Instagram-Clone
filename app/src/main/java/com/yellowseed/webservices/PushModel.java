package com.yellowseed.webservices;

import java.util.List;

/**
 * Created by pushpender.singh on 17/8/18.
 */

public class PushModel {
    private String message;
    private List<String> users;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
}
