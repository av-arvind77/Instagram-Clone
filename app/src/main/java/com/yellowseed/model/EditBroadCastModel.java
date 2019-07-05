package com.yellowseed.model;

import java.util.List;

/**
 * Created by pushpender.singh on 10/8/18.
 */

public class EditBroadCastModel {
    private String broadcast_id;
    private String name;
    private List<String> add_member_ids;
    private List<String> delete_member_ids;

    public String getBroadcast_id() {
        return broadcast_id;
    }

    public void setBroadcast_id(String broadcast_id) {
        this.broadcast_id = broadcast_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getAdd_member_ids() {
        return add_member_ids;
    }

    public void setAdd_member_ids(List<String> add_member_ids) {
        this.add_member_ids = add_member_ids;
    }

    public List<String> getDelete_member_ids() {
        return delete_member_ids;
    }

    public void setDelete_member_ids(List<String> delete_member_ids) {
        this.delete_member_ids = delete_member_ids;
    }
}
