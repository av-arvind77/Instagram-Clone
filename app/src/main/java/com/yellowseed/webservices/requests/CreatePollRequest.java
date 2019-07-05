package com.yellowseed.webservices.requests;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

public class CreatePollRequest  {

    @SerializedName("type4")
    private String type4;

    @SerializedName("type3")
    private String type3;

    @SerializedName("type2")
    private String type2;

    @SerializedName("valid_till")
    private String validTill;

    @SerializedName("poll_title")
    private String pollTitle;

    @SerializedName("type1")
    private String type1;

    public String getType4() {
        return type4;
    }

    public void setType4(String type4) {
        this.type4 = type4;
    }

    public String getType3() {
        return type3;
    }

    public void setType3(String type3) {
        this.type3 = type3;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public String getValidTill() {
        return validTill;
    }

    public void setValidTill(String validTill) {
        this.validTill = validTill;
    }

    public String getPollTitle() {
        return pollTitle;
    }

    public void setPollTitle(String pollTitle) {
        this.pollTitle = pollTitle;
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    @Override
    public String toString() {
        return
                "CreatePollRequest{" +
                        "type4 = '" + type4 + '\'' +
                        ",type3 = '" + type3 + '\'' +
                        ",type2 = '" + type2 + '\'' +
                        ",valid_till = '" + validTill + '\'' +
                        ",poll_title = '" + pollTitle + '\'' +
                        ",type1 = '" + type1 + '\'' +
                        "}";
    }
}