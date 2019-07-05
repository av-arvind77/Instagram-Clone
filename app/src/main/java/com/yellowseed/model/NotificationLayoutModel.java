package com.yellowseed.model;

public class NotificationLayoutModel {

   private String notificationData;
   private int notificationUserImage;
   private int notificationUserPostImage;

    public String getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(String notificationTime) {
        this.notificationTime = notificationTime;
    }

    private String notificationTime;

    public String getNotificationData() {
        return notificationData;
    }

    public void setNotificationData(String notificationData) {
        this.notificationData = notificationData;
    }

    public int getNotificationUserImage() {
        return notificationUserImage;
    }

    public void setNotificationUserImage(int notificationUserImage) {
        this.notificationUserImage = notificationUserImage;
    }

    public int getNotificationUserPostImage() {
        return notificationUserPostImage;
    }

    public void setNotificationUserPostImage(int notificationUserPostImage) {
        this.notificationUserPostImage = notificationUserPostImage;
    }
}
