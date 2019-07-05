package com.yellowseed.activity;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.JsonObject;
import com.yellowseed.R;
import com.yellowseed.adapter.ChatScreenAdapter;
import com.yellowseed.adapter.CommentsAdapter;
import com.yellowseed.adapter.StarredMessageAdapter;
import com.yellowseed.database.RoomChatTable;
import com.yellowseed.databinding.ActivityDirectScreenBinding;
import com.yellowseed.databinding.ActivityStarredMessageBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.ChatScreenModel;
import com.yellowseed.model.resModel.GetChatResonse;
import com.yellowseed.model.resModel.GetRoomResonse;
import com.yellowseed.model.resModel.StartedMessageListResponse;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StarredMessageActivity extends BaseActivity {
    private ActivityStarredMessageBinding binding;
    private Context context;
    private String assocID,roomId;
    private List<GetChatResonse.UserInfoBean> infoBeans=new ArrayList<>();
    private StarredMessageAdapter  adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_starred_message);
        context = StarredMessageActivity.this;
        initializedControl();
        setToolBar();
        setOnClickListener();

        /*if(CommonUtils.isOnline(context)) {
            callStaredMessageListAPI();
        }
        else
        {
            RoomChatTable roomChatTable=new RoomChatTable(context);
            infoBeans.addAll(roomChatTable.getStarredMessages(roomId));
            adapter.notifyDataSetChanged();
        }*/
    }


        /*adapter = new CommentsAdapter(comments, mContext, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.tvReplyComments:
                        ToastUtils.showToastShort(mContext, "Reply");
                        break;
                    case R.id.tvLikeComments:
                        ToastUtils.showToastShort(mContext, "Like");
                        break;
                    case R.id.tvDislikeComments:
                        ToastUtils.showToastShort(mContext, "Dislike");
                        break;
                    default:
                        break;
                }
            }
        });*/




    private void getIntentValue() {
        if (getIntent() != null && getIntent().getExtras() != null){
            if (getIntent().getStringExtra(AppConstants.ASSOC_ID) != null) {
                assocID = getIntent().getStringExtra(AppConstants.ASSOC_ID);
            }
            if (getIntent().getStringExtra(AppConstants.ROOM_ID) != null) {
                roomId = getIntent().getStringExtra(AppConstants.ROOM_ID);
            }
        }
    }

    @Override
    public void initializedControl() {
        setCurrentTheme();

    }
    private void setCurrentTheme() {

        binding.llMain.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
         binding.toolbarStarredMessage.toolbarMain.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        binding.toolbarStarredMessage.tvHeader.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.toolbarStarredMessage.tvRighttext.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setTolbarText()));
     //   binding.toolbarStarredMessage.tvCancel.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setTolbarText()));
        Themes.getInstance(context).changeIconColor(context, binding.toolbarStarredMessage.ivLeft);

        binding.viewLine.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setViewLineGrey()));

    }

    @Override
    public void setToolBar() {
        binding.toolbarStarredMessage.ivLeft.setVisibility(View.VISIBLE);
        binding.toolbarStarredMessage.ivLeft.setImageResource(R.mipmap.back_icon);
        binding.toolbarStarredMessage.tvHeader.setVisibility(View.VISIBLE);
        binding.toolbarStarredMessage.tvHeader.setText(R.string.starred_message);
        binding.toolbarStarredMessage.ivRight.setVisibility(View.GONE);

    }

    @Override
    public void setOnClickListener() {
        binding.toolbarStarredMessage.ivLeft.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivLeft:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    /**
     * method for block contact
     */
    private void callStaredMessageListAPI() {
        if(CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("assoc_id", assocID);

            Call<StartedMessageListResponse> call = ApiExecutor.getClient(context).apiStaredMessageList(jsonObject);
            call.enqueue(new Callback<StartedMessageListResponse>() {
                @Override
                public void onResponse(@NonNull Call<StartedMessageListResponse> call, @NonNull final Response<StartedMessageListResponse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            if (response.body().getResponseMessage() != null) {
                                CommonUtils.showToast(context, response.body().getResponseMessage());
                                if(response.body().getUser_info()!=null&&response.body().getUser_info().size()>0){
                                    infoBeans.addAll(response.body().getUser_info());
                                    adapter.notifyDataSetChanged();
                                }
                            }
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
                public void onFailure(@NonNull Call<StartedMessageListResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
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
}
