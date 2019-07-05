package com.yellowseed.webservices;

import com.yellowseed.model.reqModel.BlockUserModel;
import com.yellowseed.model.reqModel.DeviceModel;
import com.yellowseed.model.reqModel.UserModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ankit Saini on 05/06/2018
 * for Mobiloitte
 *
 */

public class ApiRequest {
    private String title;

    public String getStory_id() {
        return story_id;
    }

    public void setStory_id(String story_id) {
        this.story_id = story_id;
    }

    private String story_id;
    /**
     * share : {"post_id":"7ea23a84-ca09-4847-b1fa-76de0a6f3340"}
     */

    private ShareBean share;


    public ArrayList<BlockUserModel> getTag_friends_attributes() {
        return tag_friends_attributes;
    }

    public void setTag_friends_attributes(ArrayList<BlockUserModel> tag_friends_attributes) {
        this.tag_friends_attributes = tag_friends_attributes;
    }

    private ArrayList<BlockUserModel> tag_friends_attributes;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    private UserModel user;
    private BlockUserModel block;

    public List<String> getMobile() {
        return mobile;
    }

    public void setMobile(List<String> mobile) {
        this.mobile = mobile;
    }

    private List<String> mobile;

    public BlockUserModel getBlock() {
        return block;
    }

    public void setBlock(BlockUserModel block) {
        this.block = block;
    }

    public DeviceModel getDevice() {
        return device;
    }

    public void setDevice(DeviceModel device) {
        this.device = device;
    }

    private DeviceModel device;

    public String getTyepe() {
        return tyepe;
    }

    public void setTyepe(String tyepe) {
        this.tyepe = tyepe;
    }

    private String tyepe;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    private int page;

    public ShareBean getShare() {
        return share;
    }

    public void setShare(ShareBean share) {
        this.share = share;
    }

    public static class ShareBean {
        /**
         * post_id : 7ea23a84-ca09-4847-b1fa-76de0a6f3340
         */

        private String post_id;

        public String getPost_id() {
            return post_id;
        }

        public void setPost_id(String post_id) {
            this.post_id = post_id;
        }
    }

}
