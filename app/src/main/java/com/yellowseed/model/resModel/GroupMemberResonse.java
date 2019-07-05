package com.yellowseed.model.resModel;

import java.io.Serializable;
import java.util.List;

public class GroupMemberResonse implements Serializable{

    /**
     * responseCode : 200
     * responseMessage : Group fetched successfully.
     * members : [{"id":"a545a3a8-27f0-46e1-a18f-6e408be52bb5","email":"chandra.prakash@mobiloittegroup.com","name":"Chandra","image":null,"is_admin":false},{"id":"2cdf7da8-9d04-4bb0-9a28-decb5ab46974","email":"aa@ss.sss","name":"sss","image":null,"is_admin":false},{"id":"ba28e5b0-b8a3-47ec-9359-994549abd833","email":"henant.singh@mobiloitte.in","name":"Hemantlocal","image":null,"is_admin":false},{"id":"64f5052c-d72d-4890-8089-43ebb191ddcd","email":"raj.jha@mobiloitte.in","name":"Rajjsisisoenidjahuw Jhajwiejdbsjiwjsjej","image":"http://res.cloudinary.com/di8lsuqdb/image/upload/v1530531467/uyssublucrjne33k2p5t.jpg","is_admin":true}]
     */

    private int responseCode;
    private String responseMessage;
    private List<MembersBean> members;

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

    public List<MembersBean> getMembers() {
        return members;
    }

    public void setMembers(List<MembersBean> members) {
        this.members = members;
    }

    public static class MembersBean implements Serializable{
        /**
         * id : a545a3a8-27f0-46e1-a18f-6e408be52bb5
         * email : chandra.prakash@mobiloittegroup.com
         * name : Chandra
         * image : null
         * is_admin : false
         */

        private String id;
        private String email;
        private String name;
        private String image;
        private String qb_id;
        private boolean isSelected;

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
        public String getQb_id() {
            return qb_id;
        }

        public void setQb_id(String qb_id) {
            this.qb_id = qb_id;
        }

        private boolean is_admin;

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

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public boolean isIs_admin() {
            return is_admin;
        }

        public void setIs_admin(boolean is_admin) {
            this.is_admin = is_admin;
        }
    }
}
