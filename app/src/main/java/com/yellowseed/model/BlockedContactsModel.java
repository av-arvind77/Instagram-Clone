package com.yellowseed.model;

public class BlockedContactsModel {
    private String userName;
    private int userPicture;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserPicture() {
        return userPicture;
    }

    public void setUserPicture(int userPicture) {
        this.userPicture = userPicture;
    }
    private boolean isBlocked = false;

    public void setSelected(boolean selected) {
        isBlocked = selected;
    }


    public boolean isSelected() {
        return isBlocked;
    }
}
