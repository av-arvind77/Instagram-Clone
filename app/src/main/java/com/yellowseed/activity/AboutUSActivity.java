package com.yellowseed.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.yellowseed.R;
import com.yellowseed.databinding.ActivityAboutUsBinding;
import com.yellowseed.listener.ApiCallback;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.webservices.ApiResponse;
import com.yellowseed.webservices.CommonApi;

public class AboutUSActivity extends BaseActivity {
    private ActivityAboutUsBinding binding;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about_us);
        context = AboutUSActivity.this;
        initializedControl();
        setToolBar();
        setOnClickListener();
/*        CommonApi.callStaticPageAPI(context, "About Us", new ApiCallback() {
            @Override
            public void onSuccess(ApiResponse apiResponse) {
                if (apiResponse.getContent() != null && apiResponse.getContent().getDescription() != null && apiResponse.getContent().getDescription().length() > 0) {
                    binding.tvAboutUs.setText(String.format("%s", apiResponse.getContent().getDescription()));
                }
            }

            @Override
            public void onFailure(ApiResponse apiResponse) {
            }
        });*/
    }

    @Override
    public void initializedControl() {
        setCurrentTheme();
    }

    @Override
    public void setToolBar() {
        binding.layoutAboutUsActivity.ivLeft.setVisibility(View.VISIBLE);
        binding.layoutAboutUsActivity.ivLeft.setImageResource(R.mipmap.back_icon);
        binding.layoutAboutUsActivity.tvHeader.setVisibility(View.VISIBLE);
        binding.layoutAboutUsActivity.tvHeader.setText(R.string.aboutus);
        binding.layoutAboutUsActivity.ivRight.setVisibility(View.GONE);

    }

    private void setCurrentTheme() {
        binding.layoutAboutUsActivity.toolbarMain.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        binding.layoutAboutUsActivity.tvHeader.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.tvAboutUs.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        Themes.getInstance(context).changeIconColor(context, binding.layoutAboutUsActivity.ivLeft);
        binding.llMain.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        binding.viewLine.setBackgroundColor(ContextCompat.getColor(context,Themes.getInstance(context).setViewLineGrey()));

    }

    @Override
    public void setOnClickListener() {
        binding.layoutAboutUsActivity.ivLeft.setOnClickListener(this);
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
