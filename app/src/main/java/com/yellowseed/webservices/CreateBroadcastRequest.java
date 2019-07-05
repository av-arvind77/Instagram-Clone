package com.yellowseed.webservices;

import java.io.Serializable;
import java.util.List;

public class CreateBroadcastRequest implements Serializable{

    /**
     * name : first broadcast
     * member_ids : ["c5a60247-a45e-4405-9653-50c2a6a09e27","8a45e655-71d4-4a2a-9933-b1f6aae0fe29"]
     */

    private String name;
    private List<String> member_ids;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getMember_ids() {
        return member_ids;
    }

    public void setMember_ids(List<String> member_ids) {
        this.member_ids = member_ids;
    }
}
