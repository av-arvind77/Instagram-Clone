package com.yellowseed.model.resModel;

import java.io.Serializable;
import java.util.List;

public class GetRoomResonse implements Serializable{

    /**
     * responseCode : 200
     * responseMessage : Rooms find successfully.
     * rooms : [{"room_id":"d8d49e2a-dfbf-4987-bd8d-fcb63c44ee7d","last_update":"2018-07-06T13:25:22.891Z","sender_id":"0b2248bf-235e-4f78-a46f-8245161d1760","assoc_id":"1ccdaee6-07f7-48c6-b101-702b30e50e39","name":"New Group","image":"http://res.cloudinary.com/di8lsuqdb/image/upload/v1530870926/mdqnotsyjwdefsvkktea.jpg","is_group":true,"last_msg_type":"","last_message":"dsfdsfsfdsf","last_message_sender":"shah123","last_message_time":"1530878150","group_member_ids":["0b2248bf-235e-4f78-a46f-8245161d1760","4fcecbc5-25a1-41ee-bb49-9cb5b6d174df"]},{"sender_id":"0b2248bf-235e-4f78-a46f-8245161d1760","assoc_id":"4fcecbc5-25a1-41ee-bb49-9cb5b6d174df","last_update":"2018-07-06T12:34:15.856Z","room_id":"ae79f3fc-4c3b-4427-ba23-2fa1e7bc251a","name":"Chandra Adhikari","email":"chandra.adhikari@mobiloittegroup.com","image":"http://res.cloudinary.com/di8lsuqdb/image/upload/v1530790723/n1r1qg4oirppf40b1spp.png","is_group":false,"last_msg_type":"","last_message":"wefew","last_message_time":"1530873442"}]
     * total_pages : 1
     */

    private int responseCode;
    private String responseMessage;
    private int total_pages;
    private List<RoomsBean> rooms;

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

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public List<RoomsBean> getRooms() {
        return rooms;
    }

    public void setRooms(List<RoomsBean> rooms) {
        this.rooms = rooms;
    }

    public static class RoomsBean implements Serializable{


        private ChatStatusModel chat_status;

        public ChatStatusModel getChat_status() {
            return chat_status;
        }

        public void setChat_status(ChatStatusModel chat_status) {
            this.chat_status = chat_status;
        }

        private String room_id;
        private String last_update;
        private String sender_id;
        private String assoc_id;
        private String name;
        private String image;
        private int is_pinned;
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getIs_pinned() {
            return is_pinned;
        }

        public void setIs_pinned(int is_pinned) {
            this.is_pinned = is_pinned;
        }

        private boolean is_group;
        private boolean is_blocked;
        private boolean is_broadcast;
        private boolean is_mute;
        private boolean is_selected;

        public boolean isIs_selected() {
            return is_selected;
        }

        public void setIs_selected(boolean is_selected) {
            this.is_selected = is_selected;
        }

        private boolean is_enable_avatar;

        public boolean isIs_enable_avatar() {
            return is_enable_avatar;
        }

        public void setIs_enable_avatar(boolean is_enable_avatar) {
            this.is_enable_avatar = is_enable_avatar;
        }

        public boolean isIs_broadcast() {
            return is_broadcast;
        }

        public void setIs_broadcast(boolean is_broadcast) {
            this.is_broadcast = is_broadcast;
        }

        public boolean isIs_mute() {
            return is_mute;
        }

        public void setIs_mute(boolean is_mute) {
            this.is_mute = is_mute;
        }

        public boolean isIs_blocked() {
            return is_blocked;
        }

        public void setIs_blocked(boolean is_blocked) {
            this.is_blocked = is_blocked;
        }

        public boolean isIs_checked_for_delete() {
            return is_checked_for_delete;
        }

        public void setIs_checked_for_delete(boolean is_checked_for_delete) {
            this.is_checked_for_delete = is_checked_for_delete;
        }

        private boolean is_checked_for_delete;
        private String last_msg_type;
        private String last_message;
        private String last_message_sender;
        private String last_message_time;
        private String email;
        private String qb_id;

        public String getQb_id() {
            return qb_id;
        }

        public void setQb_id(String qb_id) {
            this.qb_id = qb_id;
        }

        private List<String> group_member_ids;

        public String getRoom_id() {
            return room_id;
        }

        public void setRoom_id(String room_id) {
            this.room_id = room_id;
        }

        public String getLast_update() {
            return last_update;
        }

        public void setLast_update(String last_update) {
            this.last_update = last_update;
        }

        public String getSender_id() {
            return sender_id;
        }

        public void setSender_id(String sender_id) {
            this.sender_id = sender_id;
        }

        public String getAssoc_id() {
            return assoc_id;
        }

        public void setAssoc_id(String assoc_id) {
            this.assoc_id = assoc_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public boolean isIs_group() {
            return is_group;
        }

        public void setIs_group(boolean is_group) {
            this.is_group = is_group;
        }

        public String getLast_msg_type() {
            return last_msg_type;
        }

        public void setLast_msg_type(String last_msg_type) {
            this.last_msg_type = last_msg_type;
        }

        public String getLast_message() {
            return last_message;
        }

        public void setLast_message(String last_message) {
            this.last_message = last_message;
        }

        public String getLast_message_sender() {
            return last_message_sender;
        }

        public void setLast_message_sender(String last_message_sender) {
            this.last_message_sender = last_message_sender;
        }

        public String getLast_message_time() {
            return last_message_time;
        }

        public void setLast_message_time(String last_message_time) {
            this.last_message_time = last_message_time;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public List<String> getGroup_member_ids() {
            return group_member_ids;
        }

        public void setGroup_member_ids(List<String> group_member_ids) {
            this.group_member_ids = group_member_ids;
        }
    }

    public static class ChatStatusModel implements Serializable{
        private String id;
        private String last_seen_timestamp;
        private String status;
        private String user_id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLast_seen_timestamp() {
            return last_seen_timestamp;
        }

        public void setLast_seen_timestamp(String last_seen_timestamp) {
            this.last_seen_timestamp = last_seen_timestamp;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }
    }
}
