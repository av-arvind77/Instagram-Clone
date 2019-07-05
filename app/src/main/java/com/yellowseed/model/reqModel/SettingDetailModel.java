package com.yellowseed.model.reqModel;

/**
 * Created by pushpender.singh on 16/8/18.
 */

public class SettingDetailModel {
    private boolean mute_notification;
    private boolean show_avatar;
    private boolean private_profile;
    private boolean allow_comment;
    private boolean allow_sharing;
    private boolean profile_visible;
    private boolean media_download_group;
    private boolean media_download_private;
    private int last_seen;
    private int tag_post;
    private int profile_photo;

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

    public boolean isProfile_visible() {
        return profile_visible;
    }

    public void setProfile_visible(boolean profile_visible) {
        this.profile_visible = profile_visible;
    }

    public boolean isMedia_download_group() {
        return media_download_group;
    }

    public void setMedia_download_group(boolean media_download_group) {
        this.media_download_group = media_download_group;
    }

    public boolean isMedia_download_private() {
        return media_download_private;
    }

    public void setMedia_download_private(boolean media_download_private) {
        this.media_download_private = media_download_private;
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

    public int getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(int profile_photo) {
        this.profile_photo = profile_photo;
    }
}

