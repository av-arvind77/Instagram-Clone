package com.yellowseed.model;

public class PollModel {

    private String options;
    private boolean selected;

    public String getAnswer() {
        return options;
    }

    public void setAnswer(String options) {
        this.options = options;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
