package com.yellowseed.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
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
import com.yellowseed.adapter.BlockedContactsAdapter;
import com.yellowseed.databinding.ActivityAddContactToBlockBinding;
import com.yellowseed.listener.ApiCallback;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.listener.onBlockItemListener;
import com.yellowseed.model.BlockedContactsModel;
import com.yellowseed.model.SendToModel;
import com.yellowseed.model.UserListModel;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;
import com.yellowseed.webservices.ApiRequest;
import com.yellowseed.webservices.ApiResponse;
import com.yellowseed.webservices.CommonApi;
import com.yellowseed.webservices.requests.BlockUserRequest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class AddContactToBlockActivity extends BaseActivity {
    private ActivityAddContactToBlockBinding binding;
    private Context context;
    private  BlockedContactsAdapter adapter;
    private ArrayList<BlockedContactsModel> arlModel=new ArrayList<>();
    private String[] names = {"John Thomas", "Jenny Koul", "Mike Keel", "Julie Kite", "Tiny Tim", "Julie Kite", "Tiny Tim", "Julie Kite", "Tiny Tim", "Julie Kite", "Tiny Tim", "Tiny Tim", "Julie Kite", "Tiny Tim", "Julie Kite", "Tiny Tim"};
    private int[] images = {R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon3, R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon3, R.mipmap.profile_icon3, R.mipmap.profile_icon3, R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon3, R.mipmap.profile_icon3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_contact_to_block);
        context = AddContactToBlockActivity.this;
        arlModel=new ArrayList<>();
        setCurrentTheme();
     //   callFollowersListAPI();
        initializedControl();
        setOnClickListener();
        setToolBar();
    }
    private void setCurrentTheme() {
        binding.llAddContact.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        binding.toolbarAddToBlock.tvHeader.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.toolbarAddToBlock.toolbarMain.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        binding.viewLine.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setViewLineGrey()));


         Themes.getInstance(context).changeIconColor(context,binding.toolbarAddToBlock.ivLeft);
        binding.svAddToBlock.setBackground(ContextCompat.getDrawable(context, Themes.getInstance(context).setEditTextBackground()));
        binding.svAddToBlock.setHintTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setGreyHint()));
        binding.svAddToBlock.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));

    }

    @Override
    public void initializedControl() {
        initView();

        binding.svAddToBlock.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }



    public void filter(String text){

        ArrayList<BlockedContactsModel> filteredName = new ArrayList<>();

        for (BlockedContactsModel model : arlModel) {

            if (model.getUserName().toLowerCase().contains(text.toLowerCase())) {
                filteredName.add(model);
            }
        }
        adapter.updatedList(filteredName);

    }


    private void initView() {
        binding.addToBlockListContainer.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        binding.addToBlockListContainer.setLayoutManager(layoutManager);
       // models = getData();
        arlModel.addAll(prepareData());

        adapter = new BlockedContactsAdapter(arlModel, context, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {

                    case R.id.tbBlock:
                        /**
                         * Call Block User Api
                         */

                        break;
                    default:
                        break;

                }
            }
        });


        binding.addToBlockListContainer.setAdapter(adapter);
    }

    private ArrayList<BlockedContactsModel> prepareData() {
        ArrayList<BlockedContactsModel> data = new ArrayList<>();
        for (int i = 0; i < images.length; i++) {
            BlockedContactsModel item = new BlockedContactsModel();
            item.setUserName(names[i]);
            item.setUserPicture(images[i]);
            data.add(item);
        }
        return data;
    }


/*    public  void callFollowersListAPI() {
        if(CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();

            ApiRequest apiRequest = new ApiRequest();
            apiRequest.setPage(1);


            Call<ApiResponse> call = ApiExecutor.getClient(context).apiFollowerList(apiRequest);
            call.enqueue(new retrofit2.Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    try {
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (response != null && response.body() != null) {
                        if (response.body().getResponseCode() == AppConstants.SUCCESS) {
                            if(response.body().getUsers() != null){
                                arlModel.clear();
                                arlModel.addAll(response.body().getUsers());
                                adapter.notifyDataSetChanged();

                            }
                        }
                        else if (response.body().getResponseCode() == AppConstants.USER_BLOCKED){
                            context.startActivity(new Intent(context, LoginActivity.class));
                            CommonUtils.clearPrefData(context);
                            ((Activity)context).finishAffinity();

                        }else {
                            ToastUtils.showToastShort(context, ""+response.body().getResponseMessage());
                        }
                    }  else {
                        ToastUtils.showToastShort(context,context.getString(R.string.server_error_msg));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
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
    }*/

    @Override
    public void setToolBar() {
        binding.toolbarAddToBlock.ivLeft.setVisibility(View.VISIBLE);
        binding.toolbarAddToBlock.tvHeader.setVisibility(View.VISIBLE);
        binding.toolbarAddToBlock.tvHeader.setText(R.string.add_contact_to_block);
    }

    @Override
    public void setOnClickListener() {
        binding.toolbarAddToBlock.ivLeft.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivLeft:
                finish();
                break;
            default:
                break;
        }
    }
}
