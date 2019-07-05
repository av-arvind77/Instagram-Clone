package com.yellowseed.model.reqModel;

import java.io.Serializable;
import java.util.List;

public class SocialPostModel implements Serializable {

    /**
     * title : nested
     * description : nested attributes
     * check_in : delhi
     * latitude : 0.0
     * longitude : 0.0
     * share_type : public
     * status : true
     * tag_friends_attributes : [{"user_id":"435143e0-f27a-4213-8317-74f07896f950"}]
     * image : []
     */

    private String title;
    private String description;
    private String check_in;
    private double latitude;
    private double longitude;
    private String share_type;
    private boolean status;
    private List<TagFriendsAttributes> tag_friends_attributes;
    private List<?> image;

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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getShare_type() {
        return share_type;
    }

    public void setShare_type(String share_type) {
        this.share_type = share_type;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<TagFriendsAttributes> getTag_friends_attributes() {
        return tag_friends_attributes;
    }

    public void setTag_friends_attributes(List<TagFriendsAttributes> tag_friends_attributes) {
        this.tag_friends_attributes = tag_friends_attributes;
    }

    public List<?> getImage() {
        return image;
    }

    public void setImage(List<?> image) {
        this.image = image;
    }


}