package com.yellowseed.model.resModel;

import java.io.Serializable;
import java.util.List;

public class StartedMessageListResponse implements Serializable{

    /**
     * responseCode : 200
     * responseMessage : Chats fetched Successfuly.
     * user_image : http://res.cloudinary.com/di8lsuqdb/image/upload/v1531300181/nxzpmrimr0evb0d919ci.jpg
     * name : 1a1d68gmail
     * room_id : 5f038588-0257-44e2-abd3-7230f0c81bac
     * user_info : [{"id":"e6c801d0-06fa-4012-8db6-a282d080611f","body":"http://res.cloudinary.com/di8lsuqdb/image/upload/v1531120772/mpzjkx0opj011xo4sdjr.jpg","read_status":true,"upload_type":"image","thumbnail":"","created_timestamp":"1531120778","created_at":"2018-07-09T07:19:38.209Z","sender_id":"a545a3a8-27f0-46e1-a18f-6e408be52bb5","is_user_sender":true,"sender_image":"http://res.cloudinary.com/di8lsuqdb/image/upload/v1531300181/nxzpmrimr0evb0d919ci.jpg","receiver_image":null,"local_message_id":null},{"id":"e6c801d0-06fa-4012-8db6-a282d080611f","body":"http://res.cloudinary.com/di8lsuqdb/image/upload/v1531120772/mpzjkx0opj011xo4sdjr.jpg","read_status":true,"upload_type":"image","thumbnail":"","created_timestamp":"1531120778","created_at":"2018-07-09T07:19:38.209Z","sender_id":"a545a3a8-27f0-46e1-a18f-6e408be52bb5","is_user_sender":true,"sender_image":"http://res.cloudinary.com/di8lsuqdb/image/upload/v1531300181/nxzpmrimr0evb0d919ci.jpg","receiver_image":null,"local_message_id":null}]
     */

    private int responseCode;
    private String responseMessage;
    private String user_image;
    private String name;
    private String room_id;
    private List<GetChatResonse.UserInfoBean> user_info;

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

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public List<GetChatResonse.UserInfoBean> getUser_info() {
        return user_info;
    }

    public void setUser_info(List<GetChatResonse.UserInfoBean> user_info) {
        this.user_info = user_info;
    }

}
