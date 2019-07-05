package com.yellowseed.listener;

import com.yellowseed.model.resModel.GetChatResonse;

public class ChatModelObject extends ListObject {

        private GetChatResonse.UserInfoBean chatModel;

        public GetChatResonse.UserInfoBean getChatModel() {
            return chatModel;
        }

        public void setChatModel(GetChatResonse.UserInfoBean chatModel) {
            this.chatModel = chatModel;
        }

        @Override
        public int getType(String userId) {
            if(this.chatModel.getSender_id()!=null){
                if (this.chatModel.getSender_id().equalsIgnoreCase(userId)) {
                    return TYPE_GENERAL_RIGHT;
                } else
                    return TYPE_GENERAL_LEFT;
            }else {
                return TYPE_GENERAL_RIGHT;
            }
        }
    }