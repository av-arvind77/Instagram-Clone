package com.yellowseed.model.resModel;

/**
 * Created by rajat_mobiloitte on 5/7/18.
 */

public class ReportPostResponse {


    /**
     * responseCode : 200
     * responseMessage : Reported successfully.
     * post : {"id":"aeaa8d48-f918-4d78-bf1e-e0a6f8c330e4","post_id":"7fc244c6-6080-4029-bb90-de64760be25e","user_id":"00e5fc12-bd3b-494f-b9c0-c7f196fdfe44","reason":"Its Spam","created_at":"2018-06-01T06:25:44.099Z","updated_at":"2018-06-01T06:25:44.099Z"}
     */

    private int responseCode;
    private String responseMessage;
    private PostBean post;

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

    public PostBean getPost() {
        return post;
    }

    public void setPost(PostBean post) {
        this.post = post;
    }

    public static class PostBean {
        /**
         * id : aeaa8d48-f918-4d78-bf1e-e0a6f8c330e4
         * post_id : 7fc244c6-6080-4029-bb90-de64760be25e
         * user_id : 00e5fc12-bd3b-494f-b9c0-c7f196fdfe44
         * reason : Its Spam
         * created_at : 2018-06-01T06:25:44.099Z
         * updated_at : 2018-06-01T06:25:44.099Z
         */

        private String id;
        private String post_id;
        private String user_id;
        private String reason;
        private String created_at;
        private String updated_at;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPost_id() {
            return post_id;
        }

        public void setPost_id(String post_id) {
            this.post_id = post_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }
    }
}
