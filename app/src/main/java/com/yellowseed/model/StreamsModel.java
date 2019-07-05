package com.yellowseed.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pushpender.singh on 3/8/18.
 */

public class StreamsModel implements Serializable{
    private String serverName;
    private List<InstanceListModel> instanceList;

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public List<InstanceListModel> getInstanceList() {
        return instanceList;
    }

    public void setInstanceList(List<InstanceListModel> instanceList) {
        this.instanceList = instanceList;
    }
}
