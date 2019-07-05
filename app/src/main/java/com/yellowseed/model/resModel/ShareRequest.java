package com.yellowseed.model.resModel;

import java.util.List;

/**
 * Created by rajat_mobiloitte on 3/7/18.
 */

public class ShareRequest {

    /**
     * share : {"post_id":"7ea23a84-ca09-4847-b1fa-76de0a6f3340"}
     */

    private ShareBean share;

    public ShareBean getShare() {
        return share;
    }

    public void setShare(ShareBean share) {
        this.share = share;
    }

    public static class ShareBean {
        /**
         * post_id : 7ea23a84-ca09-4847-b1fa-76de0a6f3340
         */

        private String post_id;
        private String title;
        private String description;
        private String check_in;
        private String latitude;
        private String longitude;
        private String share_type;
        private boolean status;
        private List<TagFriendModel> tag_friends_attributes;
        private List<TagNameModel> post_tags_attributes;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCheck_in() {
            return check_in;
        }

        public void setCheck_in(String check_in) {
            this.check_in = check_in;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getShare_type() {
            return share_type;
        }

        public void setShare_type(String share_type) {
            this.share_type = share_type;
        }

        public boolean getStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public List<TagFriendModel> getTag_friends_attributes() {
            return tag_friends_attributes;
        }

        public void setTag_friends_attributes(List<TagFriendModel> tag_friends_attributes) {
            this.tag_friends_attributes = tag_friends_attributes;
        }

        public List<TagNameModel> getPost_tags_attributes() {
            return post_tags_attributes;
        }

        public void setPost_tags_attributes(List<TagNameModel> post_tags_attributes) {
            this.post_tags_attributes = post_tags_attributes;
        }

        public String getPost_id() {
            return post_id;
        }

        public void setPost_id(String post_id) {
            this.post_id = post_id;
        }
    }

    public static class TagNameModel {


        private TagAttributes tag_attributes;

        public TagAttributes getTag_attributes() {
            return tag_attributes;
        }

        public void setTag_attributes(TagAttributes tag_attributes) {
            this.tag_attributes = tag_attributes;
        }
    }

    public static class TagAttributes {

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class TagFriendModel {
        private String user_id;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }
    }
}
