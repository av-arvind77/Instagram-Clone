package com.yellowseed.model.reqModel;

import java.io.Serializable;
import java.util.List;

public class PostModel implements Serializable{

    /**
     * title : story1
     * description : story post1
     * tag_friends_attributes : [{"user_id":"1271bfa7-9a4a-45bf-ae77-5809de6b222e"}]
     * poll_attributes : {"poll_title":"liked or not?","type1":"Yes","type2":"No","x_axis":"dfgpx","y_axis":"dfaspx"}
     * check_in_attributes : {"location":"delhi","latitude":"665east","longitude":"djfdwest","x_axis":"6px","y_axis":"25465px"}
     * image_attributes : {"file":"dfdsaf.jpg","width":"555px","height":"55px","file_type":"jpg"}
     */

    private String title;
    private String description;
    private PollAttributesBean poll_attributes;
    private CheckInAttributesBean check_in_attributes;
    private ImageAttributesBean image_attributes;
    private List<TagFriendsAttributes> tag_friends_attributes;

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

    public PollAttributesBean getPoll_attributes() {
        return poll_attributes;
    }

    public void setPoll_attributes(PollAttributesBean poll_attributes) {
        this.poll_attributes = poll_attributes;
    }

    public CheckInAttributesBean getCheck_in_attributes() {
        return check_in_attributes;
    }

    public void setCheck_in_attributes(CheckInAttributesBean check_in_attributes) {
        this.check_in_attributes = check_in_attributes;
    }

    public ImageAttributesBean getImage_attributes() {
        return image_attributes;
    }

    public void setImage_attributes(ImageAttributesBean image_attributes) {
        this.image_attributes = image_attributes;
    }

    public List<TagFriendsAttributes> getTag_friends_attributes() {
        return tag_friends_attributes;
    }

    public void setTag_friends_attributes(List<TagFriendsAttributes> tag_friends_attributes) {
        this.tag_friends_attributes = tag_friends_attributes;
    }

    public static class PollAttributesBean {
        /**
         * poll_title : liked or not?
         * type1 : Yes
         * type2 : No
         * x_axis : dfgpx
         * y_axis : dfaspx
         */

        private String poll_title;
        private String type1;
        private String type2;
        private String x_axis;
        private String y_axis;

        public String getPoll_title() {
            return poll_title;
        }

        public void setPoll_title(String poll_title) {
            this.poll_title = poll_title;
        }

        public String getType1() {
            return type1;
        }

        public void setType1(String type1) {
            this.type1 = type1;
        }

        public String getType2() {
            return type2;
        }

        public void setType2(String type2) {
            this.type2 = type2;
        }

        public String getX_axis() {
            return x_axis;
        }

        public void setX_axis(String x_axis) {
            this.x_axis = x_axis;
        }

        public String getY_axis() {
            return y_axis;
        }

        public void setY_axis(String y_axis) {
            this.y_axis = y_axis;
        }
    }

    public static class CheckInAttributesBean {
        /**
         * location : delhi
         * latitude : 665east
         * longitude : djfdwest
         * x_axis : 6px
         * y_axis : 25465px
         */

        private String location;
        private String latitude;
        private String longitude;
        private String x_axis;
        private String y_axis;

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
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

        public String getX_axis() {
            return x_axis;
        }

        public void setX_axis(String x_axis) {
            this.x_axis = x_axis;
        }

        public String getY_axis() {
            return y_axis;
        }

        public void setY_axis(String y_axis) {
            this.y_axis = y_axis;
        }
    }

    public static class ImageAttributesBean {
        /**
         * file : dfdsaf.jpg
         * width : 555px
         * height : 55px
         * file_type : jpg
         */

        private String file;
        private String width;
        private String height;
        private String file_type;

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getFile_type() {
            return file_type;
        }

        public void setFile_type(String file_type) {
            this.file_type = file_type;
        }
    }

   /* public static class TagFriendsAttributesBean implements Serializable {
        *//**
         * user_id : 1271bfa7-9a4a-45bf-ae77-5809de6b222e
         *//*

        private String user_id;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }
    }*/
}
