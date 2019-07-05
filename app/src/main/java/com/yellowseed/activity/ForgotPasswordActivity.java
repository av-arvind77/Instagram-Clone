package com.yellowseed.activity;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.yellowseed.R;
import com.yellowseed.databinding.ActivityForgotPasswordBinding;
import com.yellowseed.listener.OkCancelInterface;
import com.yellowseed.model.reqModel.UserModel;
import com.yellowseed.utils.ActivityController;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;
import com.yellowseed.webservices.ApiRequest;
import com.yellowseed.webservices.ApiResponse;

import retrofit2.Call;
import retrofit2.Response;

public class ForgotPasswordActivity extends BaseActivity implements TextWatcher {

    private ActivityForgotPasswordBinding binding;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);
        mContext = ForgotPasswordActivity.this;
        initializedControl();
        setToolBar();
        setOnClickListener();

    }

    @Override
    public void initializedControl() {


    }

    @Override
    public void setToolBar() {
        binding.toolbarForgotPas.tvHeader.setText(R.string.forotpass);
        binding.toolbarForgotPas.tvHeader.setVisibility(View.VISIBLE);
        binding.toolbarForgotPas.ivLeft.setVisibility(View.VISIBLE);
    }

    @Override
    public void setOnClickListener() {

        binding.btForgotpasSubmit.setOnClickListener(this);
        binding.toolbarForgotPas.ivLeft.setOnClickListener(this);
        binding.etForgotpassMobilenumber.addTextChangedListener(this);
        binding.lllinearlayout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btForgotpasSubmit:
                if (checkValidation()) {
                    // callForgotAPI();
                    ToastUtils.showToastShort(mContext, "New password has been sent on your mobile number.");
                    ActivityController.startActivity(mContext, LoginActivity.class);
                }
                break;

            case R.id.ivLeft:
                finish();
                break;
            case R.id.lllinearlayout:
                CommonUtils.hideSoftKeyboard(this);
                break;
            default:
                break;
        }

    }

    private boolean checkValidation() {

        if (TextUtils.isEmpty(binding.etForgotpassMobilenumber.getText().toString().trim())) {

            binding.tvForgotpassError.setVisibility(View.VISIBLE);
            binding.tvForgotpassError.setText(R.string.entermobilenoerror);
            return false;
        } else if (CommonUtils.lengthFieldValidation(binding.etForgotpassMobilenumber.getText().toString(), 7)) {

            binding.tvForgotpassError.setVisibility(View.VISIBLE);
            binding.tvForgotpassError.setText(R.string.validmobilenoerror);
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

        if (!TextUtils.isEmpty(binding.etForgotpassMobilenumber.getText().toString())) {
            binding.tvForgotpassError.setVisibility(View.GONE);
        }
    }

    private void callForgotAPI() {
        if (CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();
            ApiRequest apiRequest = new ApiRequest();
            UserModel model = new UserModel();
            model.setMobile(binding.etForgotpassMobilenumber.getText().toString().trim());
            apiRequest.setUser(model);

            Call<ApiResponse> call = ApiExecutor.getClient(mContext).apiForgotPwd(apiRequest);
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
                            DialogUtils.dilaogOkWithInterFace(mContext, getString(R.string.new_pwd_sent), new OkCancelInterface() {
                                @Override
                                public void onSuccess() {
                                    finish();
                                }

                                @Override
                                public void onCancel() {

                                }
                            });
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
