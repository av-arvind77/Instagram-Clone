package com.yellowseed.webservices.response;

import java.util.List;

/**
 * Created by rajat_mobiloitte on 13/7/18.
 */

public class BlockUserResponse {


    /**
     * responseCode : 200
     * responseMessage : Blocked successfully.
     * block : {"id":"981f7f41-eec3-454b-ad29-c828885dbe32","block_id":"19b63a8a-158c-4ebb-85aa-eff82028c3b9","user_id":"051fcf99-c70b-4731-89bc-4a894afa3bb8","reason":"","created_at":"2018-06-09T07:28:17.956Z","updated_at":"2018-06-09T07:28:17.956Z"}
     */

    private int responseCode;
    private String responseMessage;
    private BlockBean block;

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

    public BlockBean getBlock() {
        return block;
    }

    public void setBlock(BlockBean block) {
        this.block = block;
    }

    public static class BlockBean {
        /**
         * id : 981f7f41-eec3-454b-ad29-c828885dbe32
         * block_id : 19b63a8a-158c-4ebb-85aa-eff82028c3b9
         * user_id : 051fcf99-c70b-4731-89bc-4a894afa3bb8
         * reason :
         * created_at : 2018-06-09T07:28:17.956Z
         * updated_at : 2018-06-09T07:28:17.956Z
         */

        private String id;
        private String block_id;
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

        public String getBlock_id() {
            return block_id;
        }

        public void setBlock_id(String block_id) {
            this.block_id = block_id;
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
