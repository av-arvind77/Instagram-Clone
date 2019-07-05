
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

import com.quickblox.core.exception.QBResponseException;
import com.quickblox.sample.video.VideoApp;
import com.quickblox.sample.video.util.QBResRequestExecutor;
import com.quickblox.sample.video.utils.QBEntityCallbackImpl;
import com.quickblox.users.model.QBUser;
import com.yellowseed.R;
import com.yellowseed.databinding.ActivityGenderBinding;
import com.yellowseed.model.reqModel.UserModel;
import com.yellowseed.utils.ActivityController;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.PrefManager;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;
import com.yellowseed.webservices.ApiRequest;
import com.yellowseed.webservices.ApiResponse;

import retrofit2.Call;
import retrofit2.Response;

public class GenderActivity extends BaseActivity {

    private ActivityGenderBinding binding;
   private Context mContext;
    private boolean isMale = false;
    private ApiResponse userData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding = DataBindingUtil.setContentView(this,R.layout.activity_gender);

       mContext = GenderActivity.this;
        initializedControl();
        setToolBar();
        setOnClickListener();
       // callSignUpQB(userData.getUser().getId()+"@yellowseed.com");
    }

    @Override
    public void initializedControl() {
       /* if(getIntent() != null)
        {
            if(getIntent().hasExtra(AppConstants.USER_DATA))
            {
                userData = (ApiResponse) getIntent().getSerializableExtra(AppConstants.USER_DATA);
            }

        }*/
    }

    @Override
    public void setToolBar() {

        binding.toolbarGender.ivHeaderImage.setImageResource(R.mipmap.logo_screen8);
        binding.toolbarGender.ivHeaderImage.setVisibility(View.VISIBLE);

    }

    @Override
    public void setOnClickListener() {


        binding.btnContinueGender.setOnClickListener(this);
        binding.ivMale.setOnClickListener(this);
        binding.ivFemale.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnContinueGender:
                ActivityController.startActivity(mContext, EditAvtarActivity.class);
/*
                AppConstants.clickAvatarSkip = false;
                String gender;
                if(isMale){
                    gender ="male";
                }
                else {
                    gender ="female";
                }*/

               // callUpdateGenderAPI(gender);

                break;

            case R.id.ivMale:
                unSelectGender();
                binding.ivMale.setImageResource(R.mipmap.user_icon_sel);
                binding.btnContinueGender.setBackground(getResources().getDrawable(R.drawable.bg_yellow));
                isMale = true;
                 break;

            case R.id.ivFemale:
                unSelectGender();
                binding.ivFemale.setImageResource(R.mipmap.female_icon_sel);
                binding.btnContinueGender.setBackground(getResources().getDrawable(R.drawable.bg_yellow));
                isMale =false;
                break;

                default:
                    break;



        }

    }

    private void unSelectGender() {
        binding.ivFemale.setImageResource(R.mipmap.female_icon);
        binding.ivMale.setImageResource(R.mipmap.user_icon);
        binding.btnContinueGender.setEnabled(true);
    }


    public void callUpdateGenderAPI(String gender) {
        if(CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();

            ApiRequest apiRequest = new ApiRequest();
            apiRequest.setUser(new UserModel());
            apiRequest.getUser().setGender(gender);
            apiRequest.getUser().setQb_id(new PrefManager(mContext).getQBId());


            Call<ApiResponse> call = ApiExecutor.getClient(mContext).apiUpdateProfile(apiRequest);
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
                            Intent intent = new Intent(mContext, EditAvtarActivity.class);
                            intent.putExtra("isMale",isMale);
                            intent.putExtra("from","signup");
                            startActivity(intent);
                        }
                        else if (response.body().getResponseCode() == AppConstants.USER_BLOCKED){
                            mContext.startActivity(new Intent(mContext, LoginActivity.class));
                            CommonUtils.clearPrefData(mContext);
                            ((Activity)mContext).finishAffinity();

                        }else {
                            ToastUtils.showToastShort(mContext, ""+response.body().getResponseMessage());
                        }
                    }  else {
                        ToastUtils.showToastShort(mContext,mContext.getString(R.string.server_error_msg));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(mContext,mContext.getString(R.string.server_error_msg));
                }
            });
        }
        else
        {
            ToastUtils.showToastShort(mContext,mContext.getString(R.string.internet_alert_msg));
        }
    }


    private void callSignUpQB(String email) {
        final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
        progressDialog.show();
        QBResRequestExecutor requestExecutor= VideoApp.getInstance().getQbResRequestExecutor();
        QBUser user = new QBUser();
        user.setPassword("yellowseed@123");
        user.setLogin(email);
        requestExecutor.signUpNewUser(user, new QBEntityCallbackImpl<QBUser>() {
            @Override
            public void onSuccess(QBUser result, Bundle params) {
                progressDialog.dismiss();
                new PrefManager(mContext).setQBid(String.valueOf(result.getId()));
            }

            @Override
            public void onError(QBResponseException responseException) {
                progressDialog.dismiss();
            }
        });
    }
}
