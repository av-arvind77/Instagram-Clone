package com.yellowseed.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.yellowseed.R;
import com.yellowseed.databinding.ActivityTextAppBinding;
import com.yellowseed.listener.ApiCallback;
import com.yellowseed.model.reqModel.UserModel;
import com.yellowseed.utils.ActivityController;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.ReqValue;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;
import com.yellowseed.webservices.ApiRequest;
import com.yellowseed.webservices.ApiResponse;
import com.yellowseed.webservices.CommonApi;

import retrofit2.Call;
import retrofit2.Response;

public class WelcomeActivity extends BaseActivity {

    private ActivityTextAppBinding binding;
    private Context mContext;
    private ApiResponse userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_text_app);
        mContext = WelcomeActivity.this;
      //  getIntentData();
        initializedControl();
        setToolBar();
        setOnClickListener();
    }

    @Override
    public void initializedControl() {

    }

    @Override
    public void setToolBar() {

        binding.toolbarText.ivHeaderImage.setVisibility(View.VISIBLE);
        binding.toolbarText.ivHeaderImage.setImageResource(R.mipmap.logo_screen8);


    }

    @Override
    public void setOnClickListener() {


        binding.btnContinueTextApp.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnContinueTextApp:
                ActivityController.startActivity(mContext, GenderActivity.class);
             /*   Intent intent=new Intent(mContext,GenderActivity.class);
                intent.putExtra(AppConstants.USER_DATA,userData);
                startActivity(intent);*/
                break;
            default:
                break;
        }

    }

    private void getIntentData() {
        if(getIntent() != null)
        {
            if(getIntent().hasExtra(AppConstants.USER_DATA))
            {
                userData = (ApiResponse) getIntent().getSerializableExtra(AppConstants.USER_DATA);
            }

            if(userData !=null && userData.getUser() != null && !TextUtils.isEmpty(userData.getUser().getName()))
            {
                binding.tvUsername.setText(userData.getUser().getName());
            }
        }
    }



}
