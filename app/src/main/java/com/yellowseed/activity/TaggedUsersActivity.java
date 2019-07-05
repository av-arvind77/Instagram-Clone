package com.yellowseed.activity;


import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.google.gson.JsonObject;
import com.yellowseed.R;
import com.yellowseed.adapter.TagFriendsAdapter;
import com.yellowseed.databinding.ActivityTaggedUsersBinding;
import com.yellowseed.model.resModel.TagUserListResponse;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.webservices.ApiExecutor;
import com.yellowseed.webservices.ApiResponse;
import com.yellowseed.webservices.response.homepost.TagFriend;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class TaggedUsersActivity extends BaseActivity {
    private ActivityTaggedUsersBinding binding;
    private Context mContext;
    private TagFriendsAdapter tagFriendsAdapter;
    private ArrayList<TagUserListResponse.UsersBean> tagUserList=new ArrayList<>();
    private String postId;
    private String sharedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        binding= DataBindingUtil.setContentView(this,R.layout.activity_tagged_users);
        super.onCreate(savedInstanceState);
        mContext=TaggedUsersActivity.this;
        setCurrentTheme();
     //   getIntentData();
        setToolBar();
       /* if(CommonUtils.isOnline(mContext)) {
            callTagUserListApi();
        }else {
            CommonUtils.showToast(mContext,getString(R.string.internet_alert_msg));
        }
*/
        setOnClickListener();
    }

    private void callTagUserListApi() {
        JsonObject jsonObject = new JsonObject();
        if(sharedId!=null && !sharedId.equalsIgnoreCase("")){
            jsonObject.addProperty("shared_id",sharedId);
        }else{
            jsonObject.addProperty("post_id",postId);
        }

        final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
        progressDialog.show();

        Call<TagUserListResponse> call= ApiExecutor.getClient(mContext).tagUserListApi(jsonObject);
        call.enqueue(new retrofit2.Callback<TagUserListResponse>(){

            @Override
            public void onResponse(Call<TagUserListResponse> call, Response<TagUserListResponse> response) {
                progressDialog.dismiss();
                if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                    tagUserList.clear();
                    tagUserList.addAll(response.body().getUsers());
                    tagUserList.remove(0);
                    initializedControl();
                }

                }

            @Override
            public void onFailure(Call<TagUserListResponse> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }
    public void setCurrentTheme() {

        binding.llMain.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkTheme()));
        binding.toolbarTagFriends.tvHeader.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        binding.toolbarTagFriends.toolbarMain.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkTheme()));
        Themes.getInstance(mContext).changeIconColor(mContext,binding.toolbarTagFriends.ivLeft);

    }
    private void getIntentData() {
       /* userIdNames=(ArrayList<TagFriend>)getIntent().getSerializableExtra("tag_users_list");
        userIdNames.remove(0);*/
        postId= getIntent().getStringExtra(AppConstants.POST_ID);
        sharedId= getIntent().getStringExtra(AppConstants.SHARED_ID);
    }

    @Override
    public void initializedControl() {
        binding.rvTaggedFriends.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        binding.rvTaggedFriends.setLayoutManager(linearLayoutManager);
        tagFriendsAdapter = new TagFriendsAdapter(mContext,tagUserList);
        binding.rvTaggedFriends.setAdapter(tagFriendsAdapter);


    }

    @Override
    public void setToolBar() {
        binding.toolbarTagFriends.ivLeft.setVisibility(View.VISIBLE);
        binding.toolbarTagFriends.ivLeft.setImageResource(R.mipmap.back_icon);
        binding.toolbarTagFriends.tvHeader.setText(R.string.tagged_users);
        binding.toolbarTagFriends.tvHeader.setVisibility(View.VISIBLE);
        binding.toolbarTagFriends.tvRighttext.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setOnClickListener() {
        binding.toolbarTagFriends.ivLeft.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        binding.toolbarTagFriends.ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
