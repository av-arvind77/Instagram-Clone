
package com.yellowseed.model;


//Created by mobiloitte on 3/5/18.


public class DirectModel {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    private boolean isChecked;
    private int image_url;
    private String name_url;
    private String time_ago_url;
    private String last_message_url;
    private boolean is_mute;

    private boolean is_checked_for_delete;

    public boolean isIs_checked_for_delete() {
        return is_checked_for_delete;
    }

    public void setIs_checked_for_delete(boolean is_checked_for_delete) {
        this.is_checked_for_delete = is_checked_for_delete;
    }

    public boolean isIs_mute() {
        return is_mute;
    }

    public void setIs_mute(boolean is_mute) {
        this.is_mute = is_mute;
    }

    public int getIs_pinned() {
        return is_pinned;
    }

    public void setIs_pinned(int is_pinned) {
        this.is_pinned = is_pinned;
    }

    private int is_pinned;


    //  private boolean isSelected;
    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getImage_url() {
        return image_url;
    }

    public void setImage_url(int image_url) {
        this.image_url = image_url;
    }

    public String getName_url() {
        return name_url;
    }

    public void setName_url(String name_url) {
        this.name_url = name_url;
    }

    public String getTime_ago_url() {
        return time_ago_url;
    }

    public void setTime_ago_url(String time_ago_url) {
        this.time_ago_url = time_ago_url;
    }

    public String getLast_message_url() {
        return last_message_url;
    }

    public void setLast_message_url(String last_message_url) {
        this.last_message_url = last_message_url;
    }

    /*public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }*/
}

