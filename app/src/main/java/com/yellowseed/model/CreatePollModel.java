package com.yellowseed.model;

public class CreatePollModel {

    private String options;

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }

    private String hintText;
    private boolean isAdd;

    public String getOptions() {
        return options;
    }

    public String getHintText() {
        return hintText;
    }

    public void setHintText(String hintText) {
        this.hintText = hintText;
    }

    public void setOptions(String options) {
        this.options = options;
    }
}
