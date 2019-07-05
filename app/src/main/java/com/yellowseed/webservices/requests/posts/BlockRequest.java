package com.yellowseed.webservices.requests.posts;

/**
 * Created by rajat_mobiloitte on 13/7/18.
 */

public class BlockRequest {

    /**
     * block : {"user_id":"19b63a8a-158c-4ebb-85aa-eff82028c3b9"}
     */

    private BlockBean block;

    public BlockBean getBlock() {
        return block;
    }

    public void setBlock(BlockBean block) {
        this.block = block;
    }

    public static class BlockBean {
        /**
         * user_id : 19b63a8a-158c-4ebb-85aa-eff82028c3b9
         */

        private String user_id;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }
    }
}
