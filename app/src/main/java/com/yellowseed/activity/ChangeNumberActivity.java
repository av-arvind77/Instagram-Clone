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
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import com.yellowseed.R;
import com.yellowseed.countrypicker.CountryPicker;
import com.yellowseed.countrypicker.CountryPickerListener;
import com.yellowseed.databinding.ActivityChangeNumberBinding;
import com.yellowseed.model.reqModel.UserModel;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.Iso2Phone;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;
import com.yellowseed.webservices.ApiRequest;
import com.yellowseed.webservices.ApiResponse;

import retrofit2.Call;
import retrofit2.Response;

public class ChangeNumberActivity extends BaseActivity implements TextWatcher {

    private ActivityChangeNumberBinding binding;
    private Context mContext;
    private String countryCodeStr = "+91";
    private boolean darkThemeEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_number);
        mContext = ChangeNumberActivity.this;
        darkThemeEnabled = CommonUtils.getPreferencesBoolean(mContext, AppConstants.DARK_THEME);
        initializedControl();
        setToolBar();
        setOnClickListener();
    }

    @Override
    public void initializedControl() {
        setCurrentTheme();
        settingCountryCode();
    }

    private void settingCountryCode() {
        ArrayAdapter<String> arrayAdapterLanguage;
        arrayAdapterLanguage = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, Iso2Phone.getaCountryCodeNew());

//        ArrayAdapter<CharSequence> arrayAdapterLanguage = ArrayAdapter.createFromResource(mContext, R.array.country_code, android.R.layout.simple_spinner_dropdown_item);
        binding.spCountryCode.setAdapter(arrayAdapterLanguage);
    }

    @Override
    public void setToolBar() {
        binding.toolbarChangeNubmer.ivLeft.setVisibility(View.VISIBLE);
        binding.toolbarChangeNubmer.tvHeader.setVisibility(View.VISIBLE);
        binding.toolbarChangeNubmer.tvHeader.setText(R.string.change_number);
    }

    private void setCurrentTheme() {
        binding.lllayout.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkTheme()));
        binding.toolbarChangeNubmer.toolbarMain.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkTheme()));
        binding.toolbarChangeNubmer.tvHeader.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        Themes.getInstance(mContext).changeIconColor(mContext, binding.toolbarChangeNubmer.ivLeft);
        binding.etMobileNumber.setBackground(ContextCompat.getDrawable(mContext, Themes.getInstance(mContext).setDarkEditProfileBackground()));
        binding.etMobileNumber.setHintTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkEditProfileHint()));
        binding.etMobileNumber.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        binding.tvCountryCode.setBackground(ContextCompat.getDrawable(mContext, Themes.getInstance(mContext).setSpinnerDarwable()));
        binding.tvCountryCode.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        binding.submitChangeNumber.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkTheme()));
    }

    @Override
    public void setOnClickListener() {
        binding.toolbarChangeNubmer.ivLeft.setOnClickListener(this);
        binding.submitChangeNumber.setOnClickListener(this);
        binding.etMobileNumber.addTextChangedListener(this);
        binding.lllayout.setOnClickListener(this);
        binding.tvCountryCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivLeft:
                finish();
                break;
            case R.id.submit_change_number:
                if (validate()) {
                    ToastUtils.showToastShort(mContext,"Number has been changed successfully");
                    finish();
                   // callChangeNumberAPI();
                }
                break;
            case R.id.lllayout:
                CommonUtils.hideSoftKeyboard(this);
                break;

            case R.id.tvCountryCode:
                final CountryPicker picker = CountryPicker.newInstance("Select Country Code");
                picker.setListener(new CountryPickerListener() {
                    @Override
                    public void onSelectCountry(String name, String code) {
                        countryCodeStr = code;
                        Log.d(TAG, "countryCodeStr : " + countryCodeStr);
                        binding.tvCountryCode.setText("+" + countryCodeStr);
                        picker.dismiss();
                    }
                });
                picker.show(getSupportFragmentManager(), "COUNTRY_CODE_PICKER");
                break;
            default:
                break;
        }
    }

    private boolean validate() {
        final String mobile_no = binding.etMobileNumber.getText().toString();
        if (TextUtils.isEmpty(mobile_no)) {
            binding.tvMobileError.setText(R.string.entermobilenoerror);
            binding.tvMobileError.setVisibility(View.VISIBLE);
            return false;
        }else if (CommonUtils.lengthFieldValidation(binding.etMobileNumber.getText().toString(), 8)) {
            binding.tvMobileError.setText(R.string.validmobilenoerror);
            binding.tvMobileError.setVisibility(View.VISIBLE);
            return false;
        } else
            return true;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (TextUtils.isDigitsOnly(binding.etMobileNumber.getText().toString().trim())) {
            binding.etMobileNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

        if (!TextUtils.isEmpty(binding.etMobileNumber.getText().toString())) {
            binding.tvMobileError.setVisibility(View.GONE);
        }


    }

    public void callChangeNumberAPI() {
        if (CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();

            ApiRequest apiRequest = new ApiRequest();
            apiRequest.setUser(new UserModel());
            //final String[] code = binding.spCountryCode.getSelectedItem().toString().trim().split(" ");
            apiRequest.getUser().setCode(countryCodeStr);
            apiRequest.getUser().setMobile(binding.etMobileNumber.getText().toString().trim());

            Call<ApiResponse> call = ApiExecutor.getClient(mContext).apiChangeMobileNum(apiRequest);
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
                            if (response.body().getUser() != null) {
                                Intent intent = new Intent(mContext, OtpActivity.class);
                                intent.putExtra(AppConstants.FROM, AppConstants.CHANGE_NUMBER);
                                intent.putExtra(AppConstants.USER_NUM, binding.etMobileNumber.getText().toString().trim());
                                intent.putExtra(AppConstants.MOBILE_COUNTRY_CODE, countryCodeStr);
                                startActivity(intent);
                                finish();
                            }
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
