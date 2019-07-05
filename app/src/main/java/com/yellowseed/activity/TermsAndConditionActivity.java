package com.yellowseed.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.yellowseed.R;
import com.yellowseed.databinding.ActivityTermsAndConditionBinding;
import com.yellowseed.listener.ApiCallback;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.webservices.ApiResponse;
import com.yellowseed.webservices.CommonApi;

public class TermsAndConditionActivity extends BaseActivity {
    private ActivityTermsAndConditionBinding binding;
    private Context context;
    private Themes themes;
    private String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_terms_and_condition);
        context = TermsAndConditionActivity.this;
        themes = new Themes(context);
        from = getIntent().getStringExtra("from");
        initializedControl();
        setToolBar();
        setOnClickListener();
      /*  CommonApi.callStaticPageAPI(context, "Terms & Conditions", new ApiCallback() {
            @Override
            public void onSuccess(ApiResponse apiResponse) {
                if (apiResponse.getContent() != null && apiResponse.getContent().getDescription() != null && apiResponse.getContent().getDescription().length() > 0) {
                    binding.tvTermsandCondtn.setText(String.format("%s", apiResponse.getContent().getDescription()));
                }
            }

            @Override
            public void onFailure(ApiResponse apiResponse) {
            }
        });*/

    }

    @Override
    public void initializedControl() {
        if (from.equalsIgnoreCase(HomeActivity.class.getSimpleName())) {
            setCurrentTheme();
        }
    }

    private void setCurrentTheme() {
        binding.layoutTermsandCondtnActivity.toolbarMain.setBackgroundColor(ContextCompat.getColor(context, themes.setDarkTheme()));
        binding.layoutTermsandCondtnActivity.tvHeader.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.tvTermsandCondtn.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        themes.changeIconColor(context, binding.layoutTermsandCondtnActivity.ivLeft);
        binding.llMain.setBackgroundColor(ContextCompat.getColor(context, themes.setDarkTheme()));
        binding.viewLine.setBackgroundColor(ContextCompat.getColor(context,themes.setViewLineGrey()));

    }

    @Override
    public void setToolBar() {
        binding.layoutTermsandCondtnActivity.ivLeft.setVisibility(View.VISIBLE);
        binding.layoutTermsandCondtnActivity.ivLeft.setImageResource(R.mipmap.back_icon);
        binding.layoutTermsandCondtnActivity.tvHeader.setVisibility(View.VISIBLE);
        binding.layoutTermsandCondtnActivity.tvHeader.setText(R.string.termsandconditions);
    }

    @Override
    public void setOnClickListener() {
        binding.layoutTermsandCondtnActivity.ivLeft.setOnClickListener(this);
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
