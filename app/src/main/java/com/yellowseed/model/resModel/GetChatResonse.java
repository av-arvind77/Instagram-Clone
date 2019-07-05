package com.yellowseed.model.resModel;

import java.io.Serializable;
import java.util.List;

public class GetChatResonse implements Serializable{

    /**
     * responseCode : 200
     * responseMessage : Chats fetched Successfuly.
     * user_image : http://res.cloudinary.com/di8lsuqdb/image/upload/v1531300181/nxzpmrimr0evb0d919ci.jpg
     * name : 1a1d68gmail
     * room_id : 5f038588-0257-44e2-abd3-7230f0c81bac
     * user_info : [{"id":"292673dc-7be3-4994-abbe-d47a92829160","body":"sdaasd","read_status":true,"upload_type":"","reply_message":{"body":"xc","sender_id":"603e65e2-c455-4b6c-967e-1662fb92c72c","sender_name":"Sonu Gaur","message_id":"58dc48fc-b3f5-4f00-bba5-9ecde53faf8b","upload_type":""},"thumbnail":"","created_timestamp":"1531484512","created_at":"2018-07-13T12:21:52.354Z","sender_id":"603e65e2-c455-4b6c-967e-1662fb92c72c","is_user_sender":false,"sender_image":"http://res.cloudinary.com/di8lsuqdb/image/upload/v1531467105/uwhfvnuew9h6gxi4d00c.jpg","receiver_image":"http://res.cloudinary.com/di8lsuqdb/image/upload/v1531467277/pcqjeq7qsd7bdkdhdpfv.jpg","local_message_id":null,"is_stared":null},{"id":"0ba2938a-90b9-44f1-ae10-31b59f1e41d3","body":"http://res.cloudinary.com/di8lsuqdb/video/upload/v1531136055/bf4jtgtr1dkus2s8sel3.m4a","read_status":true,"upload_type":"audio","thumbnail":"","created_timestamp":"1531136061","created_at":"2018-07-09T11:34:21.957Z","sender_id":"a545a3a8-27f0-46e1-a18f-6e408be52bb5","is_user_sender":true,"sender_image":"http://res.cloudinary.com/di8lsuqdb/image/upload/v1531300181/nxzpmrimr0evb0d919ci.jpg","receiver_image":null,"local_message_id":null,"is_stared":null}]
     */

    private int responseCode;
    private String responseMessage;
    private String user_image;
    private String name;
    private String room_id;

    private List<GroupMemberResponse> group_members;

    public List<GroupMemberResponse> getGroup_members() {
        return group_members;
    }

    public void setGroup_members(List<GroupMemberResponse> group_members) {
        this.group_members = group_members;
    }

    private List<String> rooms;
    private GetRoomResonse.RoomsBean room;
    private List<UserInfoBean> user_detail;

    public List<String> getRooms() {
        return rooms;
    }

    public void setRooms(List<String> rooms) {
        this.rooms = rooms;
    }

    public GetRoomResonse.RoomsBean getRoom() {
        return room;
    }

    public void setRoom(GetRoomResonse.RoomsBean room) {
        this.room = room;
    }

    public List<UserInfoBean> getUser_detail() {
        return user_detail;
    }

    public void setUser_detail(List<UserInfoBean> user_detail) {
        this.user_detail = user_detail;
    }

    private List<GroupMemberResonse.MembersBean> broadcast_members;

    public List<GroupMemberResonse.MembersBean> getBroadcast_members() {
        return broadcast_members;
    }

    public void setBroadcast_members(List<GroupMemberResonse.MembersBean> broadcast_members) {
        this.broadcast_members = broadcast_members;
    }

    public List<String> getRoom_ids() {
        return rooms;
    }

    public void setRoom_ids(List<String> room_ids) {
        this.rooms = room_ids;
    }

    private List<UserInfoBean> user_info;

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

    public List<UserInfoBean> getUser_info() {
        return user_info;
    }

    public void setUser_info(List<UserInfoBean> user_info) {
        this.user_info = user_info;
    }

    public static class UserInfoBean implements Serializable{
        /**
         * id : 292673dc-7be3-4994-abbe-d47a92829160
         * body : sdaasd
         * read_status : true
         * upload_type :
         * reply_message : {"body":"xc","sender_id":"603e65e2-c455-4b6c-967e-1662fb92c72c","sender_name":"Sonu Gaur","message_id":"58dc48fc-b3f5-4f00-bba5-9ecde53faf8b","upload_type":""}
         * thumbnail :
         * created_timestamp : 1531484512
         * created_at : 2018-07-13T12:21:52.354Z
         * sender_id : 603e65e2-c455-4b6c-967e-1662fb92c72c
         * is_user_sender : false
         * sender_image : http://res.cloudinary.com/di8lsuqdb/image/upload/v1531467105/uwhfvnuew9h6gxi4d00c.jpg
         * receiver_image : http://res.cloudinary.com/di8lsuqdb/image/upload/v1531467277/pcqjeq7qsd7bdkdhdpfv.jpg
         * local_message_id : null
         * is_stared : null
         */

        private String msg_id;
        private String email;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
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

        public String getIs_admin() {
            return is_admin;
        }

        public void setIs_admin(String is_admin) {
            this.is_admin = is_admin;
        }

        private String name;

        private String image;
        private String is_admin;
        private String id;
        private String read_by;
        private boolean status;

        private String online_status;


        public String getOnline_status() {
            return online_status;
        }

        public void setOnline_status(String online_status) {
            this.online_status = online_status;
        }

        private String qb_id;

        public String getQb_id() {
            return qb_id;
        }

        public void setQb_id(String qb_id) {
            this.qb_id = qb_id;
        }

        public String getRoom_id() {
            return room_id;
        }

        public void setRoom_id(String room_id) {
            this.room_id = room_id;
        }

        private String room_id;
        private String assoc_id;

        public String getAssoc_id() {
            return assoc_id;
        }

        public void setAssoc_id(String assoc_id) {
            this.assoc_id = assoc_id;
        }

        private boolean isSelected;
        private String body;
        private boolean read_status;
        private String upload_type;
        private ReplyMessageBean reply_message;
        private String thumbnail;
        private String created_timestamp;
        private String created_at;
        private String sender_id;
        private String caption;

        public String getMsg_id() {
            return msg_id;
        }

        public void setMsg_id(String msg_id) {
            this.msg_id = msg_id;
        }

        public String getRead_by() {
            return read_by;
        }

        public void setRead_by(String read_by) {
            this.read_by = read_by;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public String getCaption() {
            return caption;
        }

        public void setCaption(String caption) {
            this.caption = caption;
        }

        private boolean is_user_sender;
        private boolean is_group;

        public boolean isIs_group() {
            return is_group;
        }

        public void setIs_group(boolean is_group) {
            this.is_group = is_group;
        }

        private String sender_image;
        private String avatar_image;

        public String getAvatar_image() {
            return avatar_image;
        }

        public void setAvatar_image(String avatar_image) {
            this.avatar_image = avatar_image;
        }

        private String receiver_image;
        private String local_message_id;
        private boolean is_stared;
        private boolean isPlaying;

        public boolean isIs_stared() {
            return is_stared;
        }

        public boolean isPlaying() {
            return isPlaying;
        }

        public void setPlaying(boolean playing) {
            isPlaying = playing;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public boolean isRead_status() {
            return read_status;
        }

        public void setRead_status(boolean read_status) {
            this.read_status = read_status;
        }

        public String getUpload_type() {
            return upload_type;
        }

        public void setUpload_type(String upload_type) {
            this.upload_type = upload_type;
        }

        public ReplyMessageBean getReply_message() {
            return reply_message;
        }

        public void setReply_message(ReplyMessageBean reply_message) {
            this.reply_message = reply_message;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getCreated_timestamp() {
            return created_timestamp;
        }

        public void setCreated_timestamp(String created_timestamp) {
            this.created_timestamp = created_timestamp;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getSender_id() {
            return sender_id;
        }

        public void setSender_id(String sender_id) {
            this.sender_id = sender_id;
        }

        public boolean isIs_user_sender() {
            return is_user_sender;
        }

        public void setIs_user_sender(boolean is_user_sender) {
            this.is_user_sender = is_user_sender;
        }

        public String getSender_image() {
            return sender_image;
        }

        public void setSender_image(String sender_image) {
            this.sender_image = sender_image;
        }

        public String getReceiver_image() {
            return receiver_image;
        }

        public void setReceiver_image(String receiver_image) {
            this.receiver_image = receiver_image;
        }

        public String getLocal_message_id() {
            return local_message_id;
        }

        public void setLocal_message_id(String local_message_id) {
            this.local_message_id = local_message_id;
        }

        public boolean getIs_stared() {
            return is_stared;
        }

        public void setIs_stared(boolean is_stared) {
            this.is_stared = is_stared;
        }

        public static class ReplyMessageBean implements Serializable{
            /**
             * body : xc
             * sender_id : 603e65e2-c455-4b6c-967e-1662fb92c72c
             * sender_name : Sonu Gaur
             * message_id : 58dc48fc-b3f5-4f00-bba5-9ecde53faf8b
             * upload_type :
             */

            private String body;
            private String sender_id;
            private String sender_name;
            private String message_id;
            private String upload_type;

            public String getBody() {
                return body;
            }

            public void setBody(String body) {
                this.body = body;
            }

            public String getSender_id() {
                return sender_id;
            }

            public void setSender_id(String sender_id) {
                this.sender_id = sender_id;
            }

            public String getSender_name() {
                return sender_name;
            }

            public void setSender_name(String sender_name) {
                this.sender_name = sender_name;
            }

            public String getMessage_id() {
                return message_id;
            }

            public void setMessage_id(String message_id) {
                this.message_id = message_id;
            }

            public String getUpload_type() {
                return upload_type;
            }

            public void setUpload_type(String upload_type) {
                this.upload_type = upload_type;
            }
        }
    }

    public static class GroupMemberResponse implements Serializable{
        private String id;
        private String name;
        private String user_name;
        private String qb_id;
        private boolean isSelected;

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getQb_id() {
            return qb_id;
        }

        public void setQb_id(String qb_id) {
            this.qb_id = qb_id;
        }
    }
}
