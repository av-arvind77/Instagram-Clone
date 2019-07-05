package com.yellowseed.model.resModel;

import java.io.Serializable;

public class SettingResponse implements Serializable{

    /**
     * user_id : c143ea68-64c2-40f0-8324-12709a2d81a2
     * id : 5bd87fbf-da47-4d18-8b01-dc7dcbee974c
     * mute_notification : false
     * show_avatar : false
     * private_profile : false
     * allow_comment : false
     * allow_sharing : false
     * save_gallery : false
     * last_seen : 0
     * tag_post : 0
     * chat_wallpaper : false
     * profile_photo : 0
     * media_download_private : false
     * media_download_group : false
     * created_at : 2018-07-13T08:59:42.373Z
     * updated_at : 2018-07-13T08:59:42.373Z
     */

    private String user_id;
    private String id;
    private boolean mute_notification;
    private boolean show_avatar;
    private boolean private_profile;
    private boolean allow_comment;
    private boolean allow_sharing;
    private boolean save_gallery;
    private int last_seen;
    private int tag_post;
    private boolean chat_wallpaper;
    private int profile_photo;
    private boolean media_download_private;
    private boolean media_download_group;
    private String created_at;
    private String updated_at;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isMute_notification() {
        return mute_notification;
    }

    public void setMute_notification(boolean mute_notification) {
        this.mute_notification = mute_notification;
    }

    public boolean isShow_avatar() {
        return show_avatar;
    }

    public void setShow_avatar(boolean show_avatar) {
        this.show_avatar = show_avatar;
    }

    public boolean isPrivate_profile() {
        return private_profile;
    }

    public void setPrivate_profile(boolean private_profile) {
        this.private_profile = private_profile;
    }

    public boolean isAllow_comment() {
        return allow_comment;
    }

    public void setAllow_comment(boolean allow_comment) {
        this.allow_comment = allow_comment;
    }

    public boolean isAllow_sharing() {
        return allow_sharing;
    }

    public void setAllow_sharing(boolean allow_sharing) {
        this.allow_sharing = allow_sharing;
    }

    public boolean isSave_gallery() {
        return save_gallery;
    }

    public void setSave_gallery(boolean save_gallery) {
        this.save_gallery = save_gallery;
    }

    public int getLast_seen() {
        return last_seen;
    }

    public void setLast_seen(int last_seen) {
        this.last_seen = last_seen;
    }

    public int getTag_post() {
        return tag_post;
    }

    public void setTag_post(int tag_post) {
        this.tag_post = tag_post;
    }

    public boolean isChat_wallpaper() {
        return chat_wallpaper;
    }

    public void setChat_wallpaper(boolean chat_wallpaper) {
        this.chat_wallpaper = chat_wallpaper;
    }

    public int getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(int profile_photo) {
        this.profile_photo = profile_photo;
    }

    public boolean isMedia_download_private() {
        return media_download_private;
    }

    public void setMedia_download_private(boolean media_download_private) {
        this.media_download_private = media_download_private;
    }

    public boolean isMedia_download_group() {
        return media_download_group;
    }

    public void setMedia_download_group(boolean media_download_group) {
        this.media_download_group = media_download_group;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
