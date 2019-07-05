package com.yellowseed.fragments.FragmentHomeBottomView;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yellowseed.R;
import com.yellowseed.adapter.TagFollowingAdapter;
import com.yellowseed.databinding.FragmentPostShareBottomSheetBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.resModel.GetChatResonse;
import com.yellowseed.model.resModel.GetRoomResonse;
import com.yellowseed.model.resModel.Post;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.PrefManager;
import com.yellowseed.webservices.ServiceConstant;
import com.yellowseed.webservices.requests.PostChatBody;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import tech.gusavila92.websocketclient.WebSocketClient;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostShareBottomSheetFragment extends BottomSheetDialogFragment {

    private FragmentPostShareBottomSheetBinding mBinding;
    private Context mContext;
    private TagFollowingAdapter tagFollowingAdapter;
    private ArrayList<GetRoomResonse.RoomsBean> roomList = new ArrayList<>();
    private GetChatResonse.UserInfoBean chatScreenModel;
    private WebSocketClient webSocketClient;


    public PostShareBottomSheetFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_post_share_bottom_sheet, container, false);
        mBinding.rvRepostHome.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mBinding.rvRepostHome.setLayoutManager(linearLayoutManager);
        tagFollowingAdapter = new TagFollowingAdapter(mContext, roomList, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (roomList.get(position).isIs_selected()) {
                    roomList.get(position).setIs_selected(false);
                } else {
                    roomList.get(position).setIs_selected(true);
                }
                tagFollowingAdapter.notifyItemChanged(position);
            }
        });
        mBinding.rvRepostHome.setAdapter(tagFollowingAdapter);
        mBinding.ivSendRepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tagFollowingAdapter.getSelected().size() > 0) {
                    chatScreenModel = new GetChatResonse.UserInfoBean();
                    chatScreenModel.setCaption(mBinding.etCaption.getText().toString().trim());
                    chatScreenModel.setCreated_at(String.valueOf(System.currentTimeMillis() / 1000));
                    chatScreenModel.setCreated_timestamp(String.valueOf(System.currentTimeMillis() / 1000));
                    chatScreenModel.setId("");
                    chatScreenModel.setIs_user_sender(true);
                    chatScreenModel.setIs_stared(false);
                    chatScreenModel.setLocal_message_id(String.valueOf(System.currentTimeMillis() / 1000));
                    chatScreenModel.setRead_status(false);
                    chatScreenModel.setReceiver_image("");
                  /*  PostChatBody postChatBody = new PostChatBody();
                    postChatBody.setPost_id(post.getPost().getId());
                    postChatBody.setUser_name(post.getUser().getName());
                    postChatBody.setUser_image(post.getUser().getImage());
                    postChatBody.setDescription(post.getPost().getDescription());
                    if (post.getImages() != null && post.getImages().size() > 0) {
                        postChatBody.setImage(post.getImages().get(0).getFile().getUrl());
                    }
                    chatScreenModel.setBody(new Gson().toJson(postChatBody));
                    chatScreenModel.setBody(new Gson().toJson(postChatBody));*/
                    chatScreenModel.setSender_id(new PrefManager(mContext).getUserId());
                    chatScreenModel.setUpload_type("post");
                    sendMessage(chatScreenModel, tagFollowingAdapter.getSelected());
                    mBinding.etCaption.setText("");
                    dismiss();
                } else {
                    CommonUtils.showToast(mContext, "Please choose friends.");
                }

            }
        });

        return mBinding.getRoot();
    }

    private void createWebSocketClient() {
        URI uri;

        try {
            uri = new URI(ServiceConstant.WEB_SOCKET_URL + "?id=" + new PrefManager(mContext).getUserId());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {
                System.out.println("onOpen");
            }

            @Override
            public void onTextReceived(final String message) {

            }

            @Override
            public void onBinaryReceived(byte[] data) {
                // isSocketConneted = true;
                System.out.println("onBinaryReceived");
            }

            @Override
            public void onPingReceived(byte[] data) {
                //isSocketConneted = true;
                System.out.println("onPingReceived");
            }

            @Override
            public void onPongReceived(byte[] data) {
                // isSocketConneted = true;
                System.out.println("onPongReceived");
            }

            @Override
            public void onException(Exception e) {
                //  isSocketConneted = false;

                System.out.println("onException");
                e.printStackTrace();
            }

            @Override
            public void onCloseReceived() {
                // isSocketConneted = false;
                System.out.println("onCloseReceived");
            }
        };

        webSocketClient.setConnectTimeout(10000);
        // webSocketClient.setReadTimeout(60000);
        webSocketClient.enableAutomaticReconnection(5000);
        webSocketClient.connect();
    }

    public void sendMessage(GetChatResonse.UserInfoBean chatScreenModel, List<String> roomIds) {
        JsonObject object = new JsonObject();
        object.addProperty("local_message_id", "");
        object.addProperty("sender_id", new PrefManager(mContext).getUserId());
        object.addProperty("room_id", chatScreenModel.getRoom_id());
        object.addProperty("is_group", "");
        object.addProperty("body", chatScreenModel.getBody());
        object.addProperty("is_group", false);
        object.addProperty("created_at", chatScreenModel.getCreated_at());
        object.addProperty("upload_type", chatScreenModel.getUpload_type());
        object.addProperty("type", "Joined");
        object.addProperty("message_type", "broadcast");
        object.addProperty("is_broadcast", true);
        object.add("room_ids", new Gson().toJsonTree(roomIds).getAsJsonArray());
        if (webSocketClient != null)
            webSocketClient.send(new Gson().toJson(object).toString());
    }
}
