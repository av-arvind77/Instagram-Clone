package com.yellowseed.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pushpender.singh on 3/8/18.
 */

public class InstanceListModel implements Serializable{
    private List<InstanceListModel> incomingStreams;

    public List<InstanceListModel> getIncomingStreams() {
        return incomingStreams;
    }

    public void setIncomingStreams(List<InstanceListModel> incomingStreams) {
        this.incomingStreams = incomingStreams;
    }

    private String applicationInstance;
    private boolean isConnected;
    private boolean isPTZEnabled;
    private boolean isPublishedToVOD;
    private boolean isRecordingSet;
    private String name;
    private String ptzPollingInterval;
    private String ptzPollingIntervalMinimum;
    private String sourceIp;

    public String getApplicationInstance() {
        return applicationInstance;
    }

    public void setApplicationInstance(String applicationInstance) {
        this.applicationInstance = applicationInstance;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public boolean isPTZEnabled() {
        return isPTZEnabled;
    }

    public void setPTZEnabled(boolean PTZEnabled) {
        isPTZEnabled = PTZEnabled;
    }

    public boolean isPublishedToVOD() {
        return isPublishedToVOD;
    }

    public void setPublishedToVOD(boolean publishedToVOD) {
        isPublishedToVOD = publishedToVOD;
    }

    public boolean isRecordingSet() {
        return isRecordingSet;
    }

    public void setRecordingSet(boolean recordingSet) {
        isRecordingSet = recordingSet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPtzPollingInterval() {
        return ptzPollingInterval;
    }

    public void setPtzPollingInterval(String ptzPollingInterval) {
        this.ptzPollingInterval = ptzPollingInterval;
    }

    public String getPtzPollingIntervalMinimum() {
        return ptzPollingIntervalMinimum;
    }

    public void setPtzPollingIntervalMinimum(String ptzPollingIntervalMinimum) {
        this.ptzPollingIntervalMinimum = ptzPollingIntervalMinimum;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }
}