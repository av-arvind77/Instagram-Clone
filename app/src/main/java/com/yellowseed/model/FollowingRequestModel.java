package com.yellowseed.model;

public class FollowingRequestModel {

    private boolean isSuggestion;
    private String userFollowingName;
    private String userFollowers;
    private String type;
    private int userFollowersPicture;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public boolean isSuggestion() {
        return isSuggestion;
    }

    public void setSuggestion(boolean suggestion) {
        isSuggestion = suggestion;
    }

    public String getUserFollowingName() {
        return userFollowingName;
    }

    public void setUserFollowingName(String userFollowingName) {
        this.userFollowingName = userFollowingName;
    }

    public String getUserFollowers() {
        return userFollowers;
    }

    public void setUserFollowers(String userFollowers) {
        this.userFollowers = userFollowers;
    }

    public int getUserFollowersPicture() {
        return userFollowersPicture;
    }

    public void setUserFollowersPicture(int userFollowersPicture) {
        this.userFollowersPicture = userFollowersPicture;
    }
}
