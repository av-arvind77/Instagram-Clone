package com.yellowseed.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.yellowseed.R;
import com.yellowseed.databinding.ActivityChangePasswordBinding;
import com.yellowseed.model.reqModel.UserModel;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;
import com.yellowseed.webservices.ApiRequest;
import com.yellowseed.webservices.ApiResponse;

import retrofit2.Call;
import retrofit2.Response;

public class ChangePasswordActivity extends BaseActivity implements TextWatcher {

    private ActivityChangePasswordBinding binding;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);
        mContext = ChangePasswordActivity.this;

        initializedControl();
        setToolBar();
        setOnClickListener();
    }

    @Override
    public void initializedControl() {
        setCurrentTheme();

    }

    private void setCurrentTheme() {
        binding.lllayout.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkTheme()));
        binding.toolbarForgetPass.toolbarMain.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkTheme()));
        binding.toolbarForgetPass.tvHeader.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        Themes.getInstance(mContext).changeIconColor(mContext, binding.toolbarForgetPass.ivLeft);
        binding.etOldPassword.setBackground(ContextCompat.getDrawable(mContext, Themes.getInstance(mContext).setDarkEditProfileBackground()));
        binding.etOldPassword.setHintTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkEditProfileHint()));
        binding.etOldPassword.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        binding.etNewPassword.setBackground(ContextCompat.getDrawable(mContext, Themes.getInstance(mContext).setDarkEditProfileBackground()));
        binding.etNewPassword.setHintTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkEditProfileHint()));
        binding.etNewPassword.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        binding.etConfirmPassword.setBackground(ContextCompat.getDrawable(mContext, Themes.getInstance(mContext).setDarkEditProfileBackground()));
        binding.etConfirmPassword.setHintTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkEditProfileHint()));
        binding.etConfirmPassword.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        binding.submitNewPassword.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkTheme()));

    }

    @Override
    public void setToolBar() {
        binding.toolbarForgetPass.tvHeader.setText(R.string.changepassword);
        binding.toolbarForgetPass.tvHeader.setVisibility(View.VISIBLE);
        binding.toolbarForgetPass.ivLeft.setVisibility(View.VISIBLE);

    }

    @Override
    public void setOnClickListener() {
        binding.submitNewPassword.setOnClickListener(this);
        binding.toolbarForgetPass.ivLeft.setOnClickListener(this);
        binding.lllayout.setOnClickListener(this);
        binding.etNewPassword.addTextChangedListener(this);
        binding.etConfirmPassword.addTextChangedListener(this);
        binding.etOldPassword.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivLeft:
                finish();
                break;
            case R.id.submit_new_password:
                if (validate()) {
                    ToastUtils.showToastShort(mContext,"Password has been changed successfully.");
                    finish();
                    //callChangePwdAPI();
                }
                break;

            case R.id.lllayout:
                CommonUtils.hideSoftKeyboard(this);
                break;

            default:
                break;
        }
    }

    private boolean validate() {
        String oldPass = binding.etOldPassword.getText().toString();
        String newPass = binding.etNewPassword.getText().toString();
        String newPassConfirm = binding.etConfirmPassword.getText().toString();
        if (TextUtils.isEmpty(oldPass)) {
            cleanError();
            binding.tvOldPasswordError.setVisibility(View.VISIBLE);
            binding.tvOldPasswordError.setText(R.string.oldpassworderoor);
            return false;
        } else if (oldPass.length() < 8) {
            cleanError();
            binding.tvOldPasswordError.setVisibility(View.VISIBLE);
            binding.tvOldPasswordError.setText(R.string.validpassworderror);
            return false;
        } else if (TextUtils.isEmpty(newPass)) {
            cleanError();
            binding.tvNewPasswordError.setVisibility(View.VISIBLE);
            binding.tvNewPasswordError.setText(R.string.newPassworderror);
            return false;
        } else if (newPass.length() < 8) {
            cleanError();
            binding.tvNewPasswordError.setVisibility(View.VISIBLE);
            binding.tvNewPasswordError.setText(R.string.validpassworderror);
            return false;
        } else if (TextUtils.isEmpty(newPassConfirm)) {
            cleanError();
            binding.tvConfirmPasswordError.setVisibility(View.VISIBLE);
            binding.tvConfirmPasswordError.setText(R.string.confirmpassworderror);
            return false;
        } else if (newPassConfirm.length() < 8) {
            cleanError();
            binding.tvConfirmPasswordError.setVisibility(View.VISIBLE);
            binding.tvConfirmPasswordError.setText(R.string.confirmpassworderror);
            return false;
        } else if (!newPass.equals(newPassConfirm)) {
            cleanError();
            binding.tvConfirmPasswordError.setVisibility(View.VISIBLE);
            binding.tvConfirmPasswordError.setText(R.string.confirmpassworderror);
            return false;
        } else {
            cleanError();
            return true;
        }
    }

    private void cleanError() {
        binding.tvConfirmPasswordError.setVisibility(View.GONE);
        binding.tvNewPasswordError.setVisibility(View.GONE);
        binding.tvOldPasswordError.setVisibility(View.GONE);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!TextUtils.isEmpty(binding.etOldPassword.getText().toString())) {
            binding.tvOldPasswordError.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(binding.etNewPassword.getText().toString())) {
            binding.tvNewPasswordError.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(binding.etConfirmPassword.getText().toString())) {
            binding.tvConfirmPasswordError.setVisibility(View.GONE);
        }
    }


    private void callChangePwdAPI() {
        if (CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();

            ApiRequest apiRequest = new ApiRequest();
            apiRequest.setUser(new UserModel());

            apiRequest.getUser().setOld_password(binding.etOldPassword.getText().toString().trim());
            apiRequest.getUser().setNew_password(binding.etNewPassword.getText().toString().trim());
            apiRequest.getUser().setConfirm_new_password(binding.etNewPassword.getText().toString().trim());

            Call<ApiResponse> call = ApiExecutor.getClient(mContext).apiChangePwd(apiRequest);
            call.enqueue(new retrofit2.Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    try {
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (response != null && response.body() != null) {
                        if (response.body().getResponseCode() == AppConstants.SUCCESS) {
                            ToastUtils.showToastShort(mContext, response.body().getResponseMessage());
                            finish();
                        } else if (response.body().getResponseCode() == AppConstants.USER_BLOCKED) {
                            startActivity(new Intent(mContext, LoginActivity.class));
                            finishAffinity();
                            CommonUtils.clearPrefData(mContext);
                        } else {
                            ToastUtils.showToastShort(mContext, response.body().getResponseMessage());
                        }
                    } else {
                        ToastUtils.showToastShort(mContext, getString(R.string.server_error_msg));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(mContext, getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
        }
    }


}
