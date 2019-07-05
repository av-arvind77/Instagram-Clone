package com.yellowseed.webservices;

import android.annotation.SuppressLint;

import com.google.gson.annotations.SerializedName;
import com.yellowseed.model.LiveModel;
import com.yellowseed.model.LiveUserModel;
import com.yellowseed.model.RoomModel;
import com.yellowseed.model.StreamsModel;
import com.yellowseed.model.UserListModel;
import com.yellowseed.model.reqModel.ContentModel;
import com.yellowseed.model.reqModel.SettingDetailModel;
import com.yellowseed.model.reqModel.UserModel;
import com.yellowseed.model.resModel.GetChatResonse;
import com.yellowseed.model.resModel.GetRoomResonse;
import com.yellowseed.model.resModel.SettingResponse;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("ALL")
@SuppressLint("ALL")

public class ApiResponse implements Serializable {

    private String status;
    private int responseCode;
    private String responseMessage;
    private String audio;
    private String video;
    private String image;
    private String thumbnail;
    private SettingDetailModel setting_detail;

    private LiveModel live;
    private RoomModel room;
    private List<RoomModel> live_users;

    public List<RoomModel> getLive_users() {
        return live_users;
    }

    public void setLive_users(List<RoomModel> live_users) {
        this.live_users = live_users;
    }

    public SettingDetailModel getSetting_detail() {
        return setting_detail;
    }

    public void setSetting_detail(SettingDetailModel setting_detail) {
        this.setting_detail = setting_detail;
    }

    public LiveModel getLive() {
        return live;
    }

    public void setLive(LiveModel live) {
        this.live = live;
    }

    public RoomModel getRoom() {
        return room;
    }

    public void setRoom(RoomModel room) {
        this.room = room;
    }



    private StreamsModel streams;

    public StreamsModel getStreams() {
        return streams;
    }

    public void setStreams(StreamsModel streams) {
        this.streams = streams;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getVideo() {
        return video;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    private ContentModel content;
    private UserModel user;
    private SettingResponse setting;
    private GroupsInfo groups;

    public GroupsInfo getGroups() {
        return groups;
    }

    public void setGroups(GroupsInfo groups) {
        this.groups = groups;
    }

    public SettingResponse getSetting() {
        return setting;
    }

    public void setSetting(SettingResponse setting) {
        this.setting = setting;
    }


    private PaginationBean pagination;
    private List<CommentArrBean> comment_arr;

    public List<UserListModel> getUsers()
    {
        return users;
    }

    public void setUsers(List<UserListModel> users) {
        this.users = users;
    }

    private List<UserListModel> users;

    public List<UserListModel> getContacts() {
        return contacts;
    }

    public void setContacts(List<UserListModel> contacts) {
        this.contacts = contacts;
    }

    private List<UserListModel> contacts;

    public ContentModel getContent() {
        return content;
    }

    public void setContent(ContentModel content) {
        this.content = content;
    }



    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public PaginationBean getPagination() {
        return pagination;
    }

    public void setPagination(PaginationBean pagination) {
        this.pagination = pagination;
    }

    public List<CommentArrBean> getComment_arr() {
        return comment_arr;
    }

    public void setComment_arr(List<CommentArrBean> comment_arr) {
        this.comment_arr = comment_arr;
    }


    public static class PaginationBean {
        /**
         * page_no : 1
         * per_page : 10
         * max_page_size : 1
         * total_records : 5
         */

        private String page_no;
        private int per_page;
        private int max_page_size;
        private int total_records;

        public String getPage_no() {
            return page_no;
        }

        public void setPage_no(String page_no) {
            this.page_no = page_no;
        }

        public int getPer_page() {
            return per_page;
        }

        public void setPer_page(int per_page) {
            this.per_page = per_page;
        }

        public int getMax_page_size() {
            return max_page_size;
        }

        public void setMax_page_size(int max_page_size) {
            this.max_page_size = max_page_size;
        }

        public int getTotal_records() {
            return total_records;
        }

        public void setTotal_records(int total_records) {
            this.total_records = total_records;
        }
    }

    public static class CommentArrBean {
        /**
         * comment_id : f565910d-eb07-4dae-b9f2-855b8970eecb
         * comment :
         * created_at : 2018-06-27T06:03:50.809Z
         * user : {"id":"075039d9-ac35-4dc2-b52d-d24a274aea66","email":"akshgaur001@gmail.com","name":"Sonu Gaur","image":"http://res.cloudinary.com/di8lsuqdb/image/upload/v1530009707/vgqqdxszsxxyleq0yu0x.jpg"}
         * likes_count : 0
         * dislikes_count : 0
         * current_user_like : false
         * current_user_dislike : false
         * nested_comment : []
         */

        private String name;
        private String comment_id;
        private String comment;
        private String created_at;
        @SerializedName("user")
        private UserListModel userX;
        private int likes_count;
        private int dislikes_count;
        private boolean current_user_like;
        private boolean current_user_dislike;
        private List<CommentArrBean> nested_comment;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getComment_id() {
            return comment_id;
        }

        public void setComment_id(String comment_id) {
            this.comment_id = comment_id;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public UserListModel getUserX() {
            return userX;
        }

        public void setUserX(UserListModel userX) {
            this.userX = userX;
        }

        public int getLikes_count() {
            return likes_count;
        }

        public void setLikes_count(int likes_count) {
            this.likes_count = likes_count;
        }

        public int getDislikes_count() {
            return dislikes_count;
        }

        public void setDislikes_count(int dislikes_count) {
            this.dislikes_count = dislikes_count;
        }

        public boolean isCurrent_user_like() {
            return current_user_like;
        }

        public void setCurrent_user_like(boolean current_user_like) {
            this.current_user_like = current_user_like;
        }

        public boolean isCurrent_user_dislike() {
            return current_user_dislike;
        }

        public void setCurrent_user_dislike(boolean current_user_dislike) {
            this.current_user_dislike = current_user_dislike;
        }

        public List<CommentArrBean> getNested_comment() {
            return nested_comment;
        }

        public void setNested_comment(List<CommentArrBean> nested_comment) {
            this.nested_comment = nested_comment;
        }
    }
}