package com.yellowseed.model.reqModel;

import java.io.Serializable;

public class ApproveRequestList implements Serializable{

    /**
     * user : {"id":"1271bfa7-9a4a-45bf-ae77-5809de6b222e"}
     */

    private UserBean user;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public static class UserBean implements Serializable{
        /**
         * id : 1271bfa7-9a4a-45bf-ae77-5809de6b222e
         */

        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
