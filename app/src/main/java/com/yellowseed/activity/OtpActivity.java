package com.yellowseed.activity;

import android.app.Activity;
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
import com.yellowseed.databinding.ActivityOtpBinding;
import com.yellowseed.model.reqModel.DeviceModel;
import com.yellowseed.model.reqModel.UserModel;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.PrefManager;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;
import com.yellowseed.webservices.ApiRequest;
import com.yellowseed.webservices.ApiResponse;

import retrofit2.Call;
import retrofit2.Response;

public class OtpActivity extends BaseActivity implements TextWatcher {

    private ActivityOtpBinding binding;
    private Context mContext = OtpActivity.this;
    private String from = "";
    private String mobile_num;
    private String user_id;
    private String code;
    private UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otp);
        mContext = OtpActivity.this;
        userModel = PrefManager.getInstance(mContext).getCurrentUser();
        getIntentData();
        setOnClickListener();
        initializedControl();
        setToolBar();
        setOnClickListener();
    }

    private void getIntentData() {
        if (getIntent() != null) {
            from = getIntent().getStringExtra(AppConstants.FROM);
            if (from.equalsIgnoreCase(AppConstants.FROM_LOGIN)) {
                if (userModel != null && !(TextUtils.isEmpty(userModel.getId()))) {
                    user_id = userModel.getId();
                }

                if (userModel != null && !(TextUtils.isEmpty(userModel.getCode()))) {
                    code = userModel.getCode();
                }

                if (userModel != null && !(TextUtils.isEmpty(userModel.getMobile()))) {
                    mobile_num = userModel.getMobile();
                }
            } else if (from.equalsIgnoreCase(AppConstants.CHANGE_NUMBER)) {
                if (getIntent().hasExtra(AppConstants.USER_NUM)) {
                    mobile_num = getIntent().getStringExtra(AppConstants.USER_NUM);
                    user_id = userModel.getId();

                }
                if (getIntent().hasExtra(AppConstants.MOBILE_COUNTRY_CODE)) {
                    code = getIntent().getStringExtra(AppConstants.MOBILE_COUNTRY_CODE);
                }

            } else {
                if (getIntent().hasExtra(AppConstants.USER_DATA)) {
                    if (userModel != null && !(TextUtils.isEmpty(userModel.getId()))) {
                        user_id = userModel.getId();
                    }

                    if (userModel != null && !(TextUtils.isEmpty(userModel.getCode()))) {
                        code = userModel.getCode();
                    }

                    if (userModel != null && !(TextUtils.isEmpty(userModel.getMobile()))) {
                        mobile_num = userModel.getMobile();
                    }

                }

            }
        }
    }

    @Override
    public void initializedControl() {
        if (getIntent() != null) {
            from = getIntent().getStringExtra(AppConstants.FROM);
            if (from.equalsIgnoreCase(AppConstants.CHANGE_NUMBER)) {
                setCurrentTheme();
            } else {
//TODO
            }
        }
    }

    private void setCurrentTheme() {
        Themes.getInstance(mContext).changeIconColorToWhite(mContext, binding.ivOtp);
        binding.toolbarOtp.toolbarMain.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkTheme()));
        binding.toolbarOtp.tvHeader.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        binding.llOtp.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkTheme()));
        binding.etOtp.setBackground(ContextCompat.getDrawable(mContext, Themes.getInstance(mContext).setDarkEditProfileBackground()));
        binding.etOtp.setHintTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkEditProfileHint()));
        binding.etOtp.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        Themes.getInstance(mContext).changeIconColor(mContext, binding.toolbarOtp.ivLeft);
        binding.tvResendOtp.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setTolbarText()));

        binding.viewLine.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setViewLineGrey()));


    }

    @Override
    public void setToolBar() {


        binding.toolbarOtp.ivLeft.setVisibility(View.VISIBLE);
        binding.toolbarOtp.tvHeader.setVisibility(View.VISIBLE);
        if (from != null && from.equalsIgnoreCase(AppConstants.SIGNUP)) {
            binding.toolbarOtp.tvHeader.setText(R.string.otptext);
        } else {
            binding.toolbarOtp.tvHeader.setText(R.string.change_number);
        }


    }

    @Override
    public void setOnClickListener() {

        binding.btnOtpSubmit.setOnClickListener(this);
        binding.etOtp.addTextChangedListener(this);
        binding.toolbarOtp.ivLeft.setOnClickListener(this);
        binding.llOtp.setOnClickListener(this);
        binding.tvResendOtp.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOtpSubmit:
                if (checkValidation())
                    if (from != null && from.equalsIgnoreCase(AppConstants.CHANGE_NUMBER)) {
                //    callOtpVerifyChangeNumberAPI(mobile_num);
                      startActivity(new Intent(this, LoginActivity.class));

                    } else {
                        startActivity(new Intent(this, SuggestionsActivity.class));

                    }


                break;

            case R.id.ivLeft:
                startActivity(new Intent(this, LoginActivity.class));
                break;

            case R.id.llOtp:
                CommonUtils.hideSoftKeyboard(this);
                break;

            case R.id.tv_resendOtp:


                    ToastUtils.showToastShort(mContext,"OTP has been resend to registered Mobile Number.");
                   // callResendOtpAPI(mobile_num, user_id, code);

                break;

            default:
                break;
        }

    }

    private boolean checkValidation() {
        if (TextUtils.isEmpty(binding.etOtp.getText().toString().trim())) {
            binding.tvOtpError.setVisibility(View.VISIBLE);
            binding.tvOtpError.setText(R.string.please_enter_otp);
            return false;
        } else if (CommonUtils.lengthFieldValidation(binding.etOtp.getText().toString(), 4)) {
            binding.tvOtpError.setVisibility(View.VISIBLE);
            binding.tvOtpError.setText(R.string.enter_valid_otp);
            return false;
        }

        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        if (!TextUtils.isEmpty(binding.etOtp.getText().toString())) {
            binding.tvOtpError.setVisibility(View.GONE);
        }
    }

    private void callOtpVerifyAPI() {
        if (CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();

            ApiRequest apiRequest = new ApiRequest();
            apiRequest.setUser(userModel);
            DeviceModel deviceModel = new DeviceModel();
            deviceModel.setDevice_type(AppConstants.DEVICE_TYPE);
            deviceModel.setDevice_token(PrefManager.getInstance(mContext).getKeyDeviceToken());
            apiRequest.setDevice(deviceModel);

            apiRequest.getUser().setOtp(binding.etOtp.getText().toString().trim());

            Call<ApiResponse> call = ApiExecutor.getClient(mContext).apiOTPVerify(apiRequest);
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
                            callSuggestionActivity(response.body());
                        } else {
                            ToastUtils.showToastShort(mContext, response.body().getResponseMessage());

                        }
                    } else if (response != null && response.errorBody() != null) {
                        String errorResponse = null;
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

    private void callSuggestionActivity(ApiResponse response) {
        CommonUtils.saveUserData(mContext, response.getUser()); // save user data

        Intent intent = new Intent(mContext, SuggestionsActivity.class);
        intent.putExtra(AppConstants.USER_DATA, response);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


    private void callResendOtpAPI(String mobile_num, String user_id, String code) {
        if (CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();

            ApiRequest apiRequest = new ApiRequest();
            UserModel model = new UserModel();
            model.setMobile(mobile_num);
            model.setId(user_id);
            model.setCode(code);
            apiRequest.setUser(model);

            Call<ApiResponse> call = ApiExecutor.getClient(mContext).apiResendOTP(apiRequest);
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

    public void callOtpVerifyChangeNumberAPI(String mobile) {
        if (CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();

            ApiRequest apiRequest = new ApiRequest();
            apiRequest.setUser(new UserModel());
            apiRequest.getUser().setMobile(mobile);
            apiRequest.getUser().setOtp(binding.etOtp.getText().toString().trim());

            Call<ApiResponse> call = ApiExecutor.getClient(mContext).apiOtpVerifyChangeMobileNum(apiRequest);
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
                            ToastUtils.showToastShort(mContext, response.body().getResponseMessage());
                            finish();
                        } else if (response.body().getResponseCode() == AppConstants.USER_BLOCKED) {
                            mContext.startActivity(new Intent(mContext, LoginActivity.class));
                            CommonUtils.clearPrefData(mContext);
                            ((Activity) mContext).finishAffinity();

                        } else {
                            ToastUtils.showToastShort(mContext, "" + response.body().getResponseMessage());
                        }
                    } else {
                        ToastUtils.showToastShort(mContext, mContext.getString(R.string.server_error_msg));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(mContext, mContext.getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
        }
    }


}
