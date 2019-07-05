package com.yellowseed.model;

import java.io.Serializable;

public class AudioModel implements Serializable {

    private String url;
    private long recordTime;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(long recordTime) {
        this.recordTime = recordTime;
    }
}
