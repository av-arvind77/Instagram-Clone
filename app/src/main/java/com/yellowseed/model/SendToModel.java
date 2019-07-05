package com.yellowseed.model;

/**
 * Created by mobiloitte on 3/5/18.
 */

public class SendToModel {

    int image_url;
    String name_url;
    boolean isChecked;

    public boolean getChecked() {
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
}
