package com.yellowseed.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.yellowseed.R;
import com.yellowseed.databinding.ActivityPrivacyPolicyBinding;
import com.yellowseed.listener.ApiCallback;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.webservices.ApiResponse;
import com.yellowseed.webservices.CommonApi;

public class PrivacyPolicyActivity extends BaseActivity {
    private ActivityPrivacyPolicyBinding binding;
    private Context context;
    private Themes themes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_privacy_policy);
        context = PrivacyPolicyActivity.this;
        themes = new Themes(context);
        initializedControl();
        setToolBar();
        setOnClickListener();

   /*     CommonApi.callStaticPageAPI(context, "Privacy Policy", new ApiCallback() {
            @Override
            public void onSuccess(ApiResponse apiResponse) {
                if (apiResponse.getContent() != null && apiResponse.getContent().getDescription() != null && apiResponse.getContent().getDescription().length() > 0) {
                    binding.tvPrivacyPolicy.setText(String.format("%s", apiResponse.getContent().getDescription()));
                }
            }

            @Override
            public void onFailure(ApiResponse apiResponse) {
            }
        });*/

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void initializedControl() {
        setCurrentTheme();
    }

    @Override
    public void setToolBar() {
        binding.layoutPrivacyPolicyActivity.ivLeft.setVisibility(View.VISIBLE);
        binding.layoutPrivacyPolicyActivity.ivLeft.setImageResource(R.mipmap.back_icon);
        binding.layoutPrivacyPolicyActivity.tvHeader.setVisibility(View.VISIBLE);
        binding.layoutPrivacyPolicyActivity.tvHeader.setText(R.string.privacypolicy);

    }


    private void setCurrentTheme() {
        binding.layoutPrivacyPolicyActivity.toolbarMain.setBackgroundColor(ContextCompat.getColor(context, themes.setDarkTheme()));
        binding.layoutPrivacyPolicyActivity.tvHeader.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.tvPrivacyPolicy.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        themes.changeIconColor(context, binding.layoutPrivacyPolicyActivity.ivLeft);
        binding.llMain.setBackgroundColor(ContextCompat.getColor(context, themes.setDarkTheme()));
        binding.viewLine.setBackgroundColor(ContextCompat.getColor(context,themes.setViewLineGrey()));


    }

    @Override
    public void setOnClickListener() {
        binding.layoutPrivacyPolicyActivity.ivLeft.setOnClickListener(this);
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
