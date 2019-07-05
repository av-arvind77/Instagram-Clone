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
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.google.gson.JsonObject;
import com.yellowseed.R;
import com.yellowseed.adapter.DirectUserAddAdapter;
import com.yellowseed.databinding.ActivityTagFollowingBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.DirectModel;
import com.yellowseed.model.UserListModel;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;
import com.yellowseed.webservices.ApiResponse;
import com.yellowseed.webservices.response.homepost.Post;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class TagFollowingActivity extends BaseActivity {

    private ActivityTagFollowingBinding binding;
    private Context mContext;
    private ArrayList<DirectModel> searchUserList = new ArrayList<>();
    private DirectUserAddAdapter adapter;
    private List<DirectModel> tagedFriendList = new ArrayList<>();
    private Post postList;
    private Themes themes;
    private String from;
    private String[] names = {"John Thomas", "Jenny Koul", "Mike Keel", "Julie Kite", "Tiny Tim", "Tiny Tim", "Tiny Tim", "Tiny Tim", "Tiny Tim"};
    private int[] images = {R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon2, R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon2, R.mipmap.profile_icon4, R.mipmap.profile_icon3, R.mipmap.profile_icon};


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        binding = DataBindingUtil.setContentView(this, R.layout.activity_tag_following);
        super.onCreate(savedInstanceState);
        mContext = TagFollowingActivity.this;
        getIntentValue();
        initializedControl();
        setOnClickListener();
        themes = new Themes(mContext);

        setToolBar();
       /* if (CommonUtils.isOnline(mContext)) {
            searchUserApi(false, "");
        } else {
            ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
        }*/
    }

    private void getIntentValue() {
      /*  if (getIntent() != null && getIntent().getExtras() != null) {
       //     tagedFriendList = (List<UserListModel>) getIntent().getSerializableExtra(AppConstants.TAG_FRIEND_LIST);
        }
        else {*/



    }

    @Override
    public void initializedControl()
    {
        setCurrentTheme();
        setTagFollowingRecycler();
    }

    private void setCurrentTheme()
    {
        binding.llMain.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkTheme()));
        binding.toolbarTagFollowing.toolbarMain.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkTheme()));
        binding.toolbarTagFollowing.tvRighttext.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setTolbarText()));
        binding.toolbarTagFollowing.tvHeader.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));

        binding.etSearchTag.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        binding.etSearchTag.setBackground(ContextCompat.getDrawable(mContext, Themes.getInstance(mContext).setDarkSearchDrawable()));
        Themes.getInstance(mContext).changeRightIcon(mContext,binding.toolbarTagFollowing.ivEdit);
        Themes.getInstance(mContext).changeIconColor(mContext,binding.toolbarTagFollowing.ivLeft);


        binding.viewLine.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setViewLineGrey()));

    }
    private void setTagFollowingRecycler() {
        binding.rvTagFollowing.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvTagFollowing.setLayoutManager(layoutManager);
        searchUserList = new ArrayList<>();
        searchUserList.addAll(prepareData());
        DirectUserAddAdapter adapter = new DirectUserAddAdapter(searchUserList, mContext, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        binding.rvTagFollowing.setAdapter(adapter);
    }

/*

    private void setTagFollowingRecycler() {
        binding.rvTagFollowing.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvTagFollowing.setLayoutManager(layoutManager);
        tagedFriendList.addAll(prepareData());
        adapter = new DirectUserAddAdapter(searchUserList, mContext, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position >= 0) {
                    if (searchUserList.get(position).isChecked()) {
                        searchUserList.get(position).setChecked(false);

                    } else {
                        searchUserList.get(position).setChecked(true);
                    }
                    adapter.notifyItemChanged(position);
                }

            }
        });
        binding.rvTagFollowing.setAdapter(adapter);
    }
*/


    private ArrayList<DirectModel> prepareData() {
        ArrayList<DirectModel> data = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            DirectModel model = new DirectModel();
            model.setImage_url(images[i]);
            model.setName_url(names[i]);
            data.add(model);
        }
        return data;
    }

    @Override
    public void setToolBar() {

        binding.toolbarTagFollowing.ivLeft.setVisibility(View.VISIBLE);
        binding.toolbarTagFollowing.ivLeft.setImageResource(R.mipmap.back_icon);
        binding.toolbarTagFollowing.tvHeader.setText(R.string.tagfollowing);
        binding.toolbarTagFollowing.tvHeader.setVisibility(View.VISIBLE);
        binding.toolbarTagFollowing.tvRighttext.setVisibility(View.VISIBLE);
        binding.toolbarTagFollowing.tvRighttext.setText(R.string.done);
    }

    @Override
    public void setOnClickListener() {
        binding.toolbarTagFollowing.tvRighttext.setOnClickListener(this);
        binding.toolbarTagFollowing.ivLeft.setOnClickListener(this);
        binding.etSearchTag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               /* if (CommonUtils.isOnline(mContext)) {
                    searchUserApi(true, s.toString());
                } else {
                    ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
                }*/
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tvRighttext:
            /*    Intent resultIntent = new Intent();
                resultIntent.putExtra("TagFriendList", (Serializable) adapter.getTaggedFriends());
                setResult(Activity.RESULT_OK, resultIntent);*/
                finish();
                break;

            case R.id.ivLeft:
                onBackPressed();
                break;

            default:
                break;
        }
    }

   /* private void searchUserApi(final boolean isSearching, String text) {
        if (CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            if (!isSearching) {
                progressDialog.show();
            }
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("name", text);
            Call<ApiResponse> call = ApiExecutor.getClient(mContext).apiSearchUser(jsonObject);
            call.enqueue(new retrofit2.Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    if (!isSearching) {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                    if (response != null && response.body() != null) {
                        if (response.body().getResponseCode() == AppConstants.SUCCESS) {
                            if (response.body().getUsers() != null && response.body().getUsers().size() > 0) {
                                searchUserList.clear();
                                searchUserList.addAll(response.body().getUsers());
                                if (tagedFriendList != null && tagedFriendList.size() > 0) {
                                    for (int i = 0; i < searchUserList.size(); i++) {
                                        for (int j = 0; j < tagedFriendList.size(); j++) {
                                            if (searchUserList.get(i).getId().equalsIgnoreCase(tagedFriendList.get(j).getId())) {
                                                searchUserList.get(i).setChecked(true);
                                                searchUserList.get(i).setServer_id(tagedFriendList.get(j).getServer_id());
                                            }
                                        }
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }
                    } else {
                        ToastUtils.showToastShort(mContext, getString(R.string.server_error_msg));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    if (!isSearching) {
                        progressDialog.dismiss();
                    }
                    t.printStackTrace();
                    ToastUtils.showToastShort(mContext, getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
        }
    }*/
}
