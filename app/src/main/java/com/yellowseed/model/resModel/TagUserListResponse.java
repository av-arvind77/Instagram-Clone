package com.yellowseed.model.resModel;

import java.util.List;

/**
 * Created by rajat_mobiloitte on 5/7/18.
 */

public class TagUserListResponse {


    /**
     * responseCode : 200
     * responseMessage : Users fetched successfully.
     * users : [{"id":"dd3239c2-5a8b-4efa-b9dd-a163d95c689e","email":"akshgaur001@gmail.com","name":"Sonu Gaur","user_name":"","image":null}]
     */

    private int responseCode;
    private String responseMessage;
    private List<UsersBean> users;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public List<UsersBean> getUsers() {
        return users;
    }

    public void setUsers(List<UsersBean> users) {
        this.users = users;
    }

    public static class UsersBean {
        /**
         * id : dd3239c2-5a8b-4efa-b9dd-a163d95c689e
         * email : akshgaur001@gmail.com
         * name : Sonu Gaur
         * user_name :
         * image : null
         */

        private String id;
        private String email;
        private String name;
        private String user_name;
        private String image;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
