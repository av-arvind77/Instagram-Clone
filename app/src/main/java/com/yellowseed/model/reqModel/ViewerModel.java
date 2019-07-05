package com.yellowseed.model.reqModel;

/**
 * Created by ankit_mobiloitte on 9/6/18.
 */

public class ViewerModel {

    private String story_id;
    private String poll_type;

    private String id;
    private String email;
    private String name;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStory_id() {
        return story_id;
    }

    public void setStory_id(String story_id) {
        this.story_id = story_id;
    }

    public String getPoll_type() {
        return poll_type;
    }

    public void setPoll_type(String poll_type) {
        this.poll_type = poll_type;
    }
}
