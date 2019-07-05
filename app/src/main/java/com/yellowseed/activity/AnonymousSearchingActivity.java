package com.yellowseed.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.JsonObject;
import com.quickblox.sample.video.activities.CallActivity;
import com.quickblox.sample.video.utils.CollectionsUtils;
import com.quickblox.users.model.QBUser;
import com.quickblox.videochat.webrtc.QBRTCClient;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.QBRTCTypes;
import com.yellowseed.R;
import com.yellowseed.adapter.ChatScreenAdapter;
import com.yellowseed.databinding.ActivityAnonymousSearchingBinding;
import com.yellowseed.databinding.ActivityAnonymousUserBinding;
import com.yellowseed.databinding.LayoutenableavatarBinding;
import com.yellowseed.imageUtils.CheckPermission;
import com.yellowseed.model.DeleteRequestModel;
import com.yellowseed.model.resModel.GetChatResonse;
import com.yellowseed.quickblox.call.VideoCallSessionManager;
import com.yellowseed.utils.ActivityController;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.PrefManager;
import com.yellowseed.utils.TakePictureUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;
import com.yellowseed.webservices.ApiResponse;
import com.yellowseed.webservices.PushModel;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnonymousSearchingActivity extends BaseActivity{
    private ActivityAnonymousSearchingBinding binding;
    private Context context;
    private String type;
    private boolean isStopped;
    private Themes themes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_anonymous_searching);
        context = AnonymousSearchingActivity.this;
        themes = new Themes(context);
        initializedControl();
        setToolBar();
        setOnClickListener();
       // apiAnonymousChat(type);
    }
    @Override
    public void initializedControl() {
        type=getIntent().getStringExtra("type");
        setCurrentTheme();
    }

    @Override
    public void setToolBar() {
        binding.toolbarGender.ivLeft.setVisibility(View.VISIBLE);
        binding.toolbarGender.ivLeft.setImageResource(R.mipmap.back_icon);
        binding.toolbarGender.tvHeader.setVisibility(View.VISIBLE);
        binding.toolbarGender.tvHeader.setText(R.string.anonymous);
        binding.toolbarGender.ivRight.setVisibility(View.GONE);
    }



    private void setCurrentTheme() {
        binding.tvconnect.setTextColor(ContextCompat.getColor(context, themes.setDarkGreyTextColor()));
        binding.llMain.setBackgroundColor(ContextCompat.getColor(context, themes.setDarkTheme()));
        binding.tvconnecting.setTextColor(ContextCompat.getColor(context,themes.setYellow()));
        binding.toolbarGender.toolbarMain.setBackgroundColor(ContextCompat.getColor(context,themes.setDarkTheme()));
        binding.toolbarGender.tvHeader.setTextColor(ContextCompat.getColor(context,themes.setDarkThemeText()));
        themes.changeIconColor(context, binding.toolbarGender.ivLeft);
        themes.changeIconColor(context, binding.ivConnecting);
        binding.viewLine.setBackgroundColor(ContextCompat.getColor(context,themes.setViewLineGrey()));


    }


    @Override
    public void setOnClickListener() {
        binding.frConntecting.setOnClickListener(this);
        binding.toolbarGender.ivLeft.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivLeft:
                onBackPressed();
              //  apiDisconnectChat();
                break;
            case R.id.frConntecting:
                ActivityController.startActivity(context,AnonymousChatActivity.class);
                break;

            default:
                break;
        }
    }

    private void apiAnonymousChat(final String type) {
        if(CommonUtils.isOnline(context)) {
            JsonObject jsonObject = new JsonObject();
            if(type.equalsIgnoreCase("chat")) {
                jsonObject.addProperty("do", type);
            }
            Call<GetChatResonse> call = ApiExecutor.getClient(context).apiAnonymousChat(jsonObject);
            call.enqueue(new Callback<GetChatResonse>() {
                @Override
                public void onResponse(@NonNull Call<GetChatResonse> call, @NonNull final Response<GetChatResonse> response) {
                    try {
                        switch (type){
                            case "chat":
                                if(response.body().getRoom()!=null){
                                    isStopped=true;
                                    Intent intent =new Intent(context,AnonymousChatActivity.class);
                                    intent.putExtra(AppConstants.ASSOC_ID,response.body().getRoom().getAssoc_id());
                                    intent.putExtra(AppConstants.NAME,"Anonymous User");
                                    intent.putExtra(AppConstants.ROOM_ID,response.body().getRoom().getId());
                                    intent.putExtra(AppConstants.FROM,"Anonymous");
                                    startActivity(intent);
                                    finish();
                                }else {
                                    if(!isStopped) {
                                        try {
                                            Thread.sleep(5000);
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }
                                        apiAnonymousChat(type);
                                    }
                                }
                                break;
                            case "audio":

                                if(response.body().getUser_detail()!=null&&response.body().getUser_detail().size()>1){
                                    isStopped=true;
                                    String qIdUser="";
                                    for (int i = 0; i < response.body().getUser_detail().size(); i++) {
                                        if(!PrefManager.getInstance(context).getQBId().equalsIgnoreCase(response.body().getUser_detail().get(i).getQb_id())){
                                            qIdUser=  response.body().getUser_detail().get(i).getQb_id();
                                            break;
                                        }
                                    }
                                    if(qIdUser!=null&&qIdUser.length()>0){
                                        if (CommonUtils.checkPermissionCamera((Activity) context)) {
                                            try {
                                                Collection<QBUser> selectedUsers = new ArrayList<>();
                                                QBUser qbUser = new QBUser();
                                                qbUser.setFullName("Anonymous User");
                                                qbUser.setId(Integer.valueOf(qIdUser));
                                                selectedUsers.add(qbUser);
                                                ArrayList<Integer> opponentsList = CollectionsUtils.getIdsSelectedOpponents(selectedUsers);
                                                QBRTCTypes.QBConferenceType conferenceType = false
                                                        ? QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO
                                                        : QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_AUDIO;
                                                QBRTCClient qbrtcClient = QBRTCClient.getInstance(getApplicationContext());
                                                QBRTCSession newQbRtcSession = qbrtcClient.createNewSessionWithOpponents(opponentsList, conferenceType);
                                                VideoCallSessionManager.getInstance(context).setCurrentSession(newQbRtcSession);

                                                Map<String, String> userInfo = new HashMap<>();
                                                userInfo.put("call", "video");
                                                userInfo.put("type", "anonymous");
                                                newQbRtcSession.startCall(userInfo);

                                                CallActivity.start(context, false, "Anonymous User");
                                                Log.d(TAG, "conferenceType = " + conferenceType);


                                                PushModel pushModel=new PushModel();
                                                pushModel.setMessage(PrefManager.getInstance(context).getCurrentUser().getName()+ " is calling");
                                                List<String> arl=new ArrayList<>();
                                                arl.add(response.body().getRoom().getAssoc_id());
                                                pushModel.setUsers(arl);
                                                sendPushService(pushModel);
                                            } catch (IllegalStateException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            CommonUtils.requestPermissionCamera((Activity) context);
                                            CommonUtils.requestPermissionCallPhone((Activity) context);
                                        }
                                    }

                                }else {
                                    if(!isStopped) {
                                        try {
                                            Thread.sleep(5000);
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }
                                        apiAnonymousChat(type);
                                    }
                                }
                                break;
                            case "video":
                                if(response.body().getUser_detail()!=null&&response.body().getUser_detail().size()>1){
                                    isStopped=true;
                                    String qIdUser="";
                                    for (int i = 0; i < response.body().getUser_detail().size(); i++) {
                                        if(!PrefManager.getInstance(context).getQBId().equalsIgnoreCase(response.body().getUser_detail().get(i).getQb_id())){
                                            qIdUser=  response.body().getUser_detail().get(i).getQb_id();
                                            break;
                                        }
                                    }
                                    if(qIdUser!=null&&qIdUser.length()>0){
                                        if (CommonUtils.checkPermissionCamera((Activity) context)) {
                                            try {
                                                Collection<QBUser> selectedUsers = new ArrayList<>();
                                                QBUser qbUser = new QBUser();
                                                qbUser.setFullName("Anonymous User");
                                                qbUser.setId(Integer.valueOf(qIdUser));
                                                selectedUsers.add(qbUser);

                                                ArrayList<Integer> opponentsList = CollectionsUtils.getIdsSelectedOpponents(selectedUsers);
                                                QBRTCTypes.QBConferenceType conferenceType = true
                                                        ? QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO
                                                        : QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_AUDIO;
                                                QBRTCClient qbrtcClient = QBRTCClient.getInstance(getApplicationContext());
                                                QBRTCSession newQbRtcSession = qbrtcClient.createNewSessionWithOpponents(opponentsList, conferenceType);

                                                Map<String, String> userInfo = new HashMap<>();
                                                userInfo.put("call", "video");
                                                userInfo.put("type", "anonymous");
                                                newQbRtcSession.startCall(userInfo);

                                                VideoCallSessionManager.getInstance(context).setCurrentSession(newQbRtcSession);



                                                CallActivity.start(context, false, "Anonymous User");
                                                Log.d(TAG, "conferenceType = " + conferenceType);

                                                PushModel pushModel=new PushModel();
                                                pushModel.setMessage(PrefManager.getInstance(context).getCurrentUser().getName()+ " is calling");
                                                List<String> arl=new ArrayList<>();
                                                arl.add(response.body().getRoom().getAssoc_id());
                                                pushModel.setUsers(arl);
                                                sendPushService(pushModel);
                                            } catch (IllegalStateException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            CommonUtils.requestPermissionCamera((Activity) context);
                                            CommonUtils.requestPermissionCallPhone((Activity) context);
                                        }
                                    }
                                }else {
                                    if(!isStopped) {
                                        try {
                                            Thread.sleep(5000);
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }
                                        apiAnonymousChat(type);
                                    }
                                }
                                break;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(@NonNull Call<GetChatResonse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    if(!isStopped) {
                        try {
                            Thread.sleep(5000);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        apiAnonymousChat(type);
                    }
                }
            });
        }
        else
        {
            ToastUtils.showToastShort(context,context.getString(R.string.internet_alert_msg));
        }
    }

    private void sendPushService(PushModel pushModel) {
        if(CommonUtils.isOnline(context)) {
            Call<ApiResponse> call = ApiExecutor.getClient(context).apiCallNotification(pushModel);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                        }
                        else
                        {
                            ToastUtils.showToastShort(context,context.getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    ToastUtils.showToastShort(context,context.getString(R.string.server_error_msg));
                }
            });
        }
        else
        {
            ToastUtils.showToastShort(context,context.getString(R.string.internet_alert_msg));
        }
    }


    private void apiDisconnectChat() {
        if(CommonUtils.isOnline(context)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("do", "back");
            Call<GetChatResonse> call = ApiExecutor.getClient(context).apiDisconnectAnonymousChat(jsonObject);
            call.enqueue(new Callback<GetChatResonse>() {
                @Override
                public void onResponse(@NonNull Call<GetChatResonse> call, @NonNull final Response<GetChatResonse> response) {
                    try {
                        isStopped=true;
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(@NonNull Call<GetChatResonse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                }
            });
        }
        else
        {
            ToastUtils.showToastShort(context,context.getString(R.string.internet_alert_msg));
        }
    }

}
