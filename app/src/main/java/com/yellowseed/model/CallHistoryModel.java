package com.yellowseed.model;

public class CallHistoryModel {

    private int userImage;
    private String userName;
    private String hours;
    private String callType;
    private String day;
    private int calltypeImg;

    public int getCallStatus() {
        return callStatus;
    }

    public void setCallStatus(int callStatus) {
        this.callStatus = callStatus;
    }

    private int callStatus;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String time;

    private boolean isSelected = false;
    private boolean isLongClick=true;

    public int getUserImage() {
        return userImage;
    }

    public void setUserImage(int userImage) {
        this.userImage = userImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getCalltypeImg() {
        return calltypeImg;
    }

    public void setCalltypeImg(int calltypeImg) {
        this.calltypeImg = calltypeImg;
    }
    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public boolean isSelected() {
        return isSelected;
    }


    public boolean isLongClick() {
        return isLongClick;
    }

    public void setLongClick(boolean longClick) {
        isLongClick = longClick;
    }
}
