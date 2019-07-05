package com.yellowseed.model.resModel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PollDataResponce implements Serializable{

    @SerializedName("count1")
    private int count1;

    @SerializedName("type4")
    private String type4;

    @SerializedName("count2")
    private int count2;

    @SerializedName("type3")
    private String type3;

    @SerializedName("type2")
    private String type2;

    @SerializedName("poll_title")
    private String pollTitle;

    @SerializedName("count3")
    private int count3;

    @SerializedName("total_count")
    private int totalCount;

    @SerializedName("count4")
    private int count4;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("type1")
    private String type1;
    @SerializedName("poll_array")

    private List<String> pollArray;
    @SerializedName("poll_result")
    private List<PollResultData> pollResult;
    @SerializedName("valid_till")
    private String validTill;
    @SerializedName("post_id")
    private String postId;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("poll_users")
    private List<Object> pollUsers;
    @SerializedName("id")
    private String id;
    @SerializedName("valid_until")
    private double validUntil;
    @SerializedName("time")
    private double time;

    public double getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(double validUntil) {
        this.validUntil = validUntil;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public List<PollResultData> getPollResult() {
        return pollResult;
    }

    public void setPollResult(List<PollResultData> pollResult) {
        this.pollResult = pollResult;
    }

    public int getCount1() {
        return count1;
    }

    public void setCount1(int count1) {
        this.count1 = count1;
    }

    public String getType4() {
        return type4;
    }

    public void setType4(String type4) {
        this.type4 = type4;
    }

    public int getCount2() {
        return count2;
    }

    public void setCount2(int count2) {
        this.count2 = count2;
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

    public String getPollTitle() {
        return pollTitle;
    }

    public void setPollTitle(String pollTitle) {
        this.pollTitle = pollTitle;
    }

    public int getCount3() {
        return count3;
    }

    public void setCount3(int count3) {
        this.count3 = count3;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getCount4() {
        return count4;
    }

    public void setCount4(int count4) {
        this.count4 = count4;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public List<String> getPollArray() {
        return pollArray;
    }

    public void setPollArray(List<String> pollArray) {
        this.pollArray = pollArray;
    }

    public String getValidTill() {
        return validTill;
    }

    public void setValidTill(String validTill) {
        this.validTill = validTill;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Object> getPollUsers() {
        return pollUsers;
    }

    public void setPollUsers(List<Object> pollUsers) {
        this.pollUsers = pollUsers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return
                "PollDataResponce{" +
                        "count1 = '" + count1 + '\'' +
                        ",type4 = '" + type4 + '\'' +
                        ",count2 = '" + count2 + '\'' +
                        ",type3 = '" + type3 + '\'' +
                        ",type2 = '" + type2 + '\'' +
                        ",poll_title = '" + pollTitle + '\'' +
                        ",count3 = '" + count3 + '\'' +
                        ",total_count = '" + totalCount + '\'' +
                        ",count4 = '" + count4 + '\'' +
                        ",created_at = '" + createdAt + '\'' +
                        ",type1 = '" + type1 + '\'' +
                        ",poll_array = '" + pollArray + '\'' +
                        ",valid_till = '" + validTill + '\'' +
                        ",post_id = '" + postId + '\'' +
                        ",updated_at = '" + updatedAt + '\'' +
                        ",poll_users = '" + pollUsers + '\'' +
                        ",id = '" + id + '\'' +
                        "}";
    }
}