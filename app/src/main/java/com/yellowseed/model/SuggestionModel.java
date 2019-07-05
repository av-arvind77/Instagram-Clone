package com.yellowseed.model;

/**
 * Created by mobiloitte on 17/5/18.
 */

public class SuggestionModel {


    private String userFollowingName;
    private String userFollowers;
    private int userFollowersPicture;

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
