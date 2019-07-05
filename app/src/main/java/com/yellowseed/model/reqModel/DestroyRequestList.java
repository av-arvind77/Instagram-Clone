package com.yellowseed.model.reqModel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DestroyRequestList implements Serializable{

    /**
     * user : {"id":"2d9d6f34-d872-4e5c-8985-94473076b482","do":"reject"}
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
         * id : 2d9d6f34-d872-4e5c-8985-94473076b482
         * do : reject
         */

        private String id;
        @SerializedName("do")
        private String doX;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDoX() {
            return doX;
        }

        public void setDoX(String doX) {
            this.doX = doX;
        }
    }
}
