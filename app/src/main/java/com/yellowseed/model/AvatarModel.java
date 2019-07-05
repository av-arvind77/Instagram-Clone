package com.yellowseed.model;

import java.io.Serializable;

public class AvatarModel implements Serializable {

    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
