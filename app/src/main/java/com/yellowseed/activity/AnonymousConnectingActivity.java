package com.yellowseed.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.yellowseed.R;
import com.yellowseed.databinding.ActivityAnonymousConnectingBinding;
import com.yellowseed.utils.ActivityController;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;

public class AnonymousConnectingActivity extends BaseActivity {
    private Context context;
    private ActivityAnonymousConnectingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_anonymous_connecting);
        context = AnonymousConnectingActivity.this;
        initializedControl();
        setToolBar();
        setOnClickListener();

    }

    @Override
    public void initializedControl() {
        setCurrentTheme();

    }
    private void setCurrentTheme() {
          binding.viewLine.setBackgroundColor(ContextCompat.getColor(context,Themes.getInstance(context).setViewLineGrey()));

    }
    @Override
    public void setToolBar() {
        binding.toolbarAnonymousConnecting.ivRight.setVisibility(View.GONE);
        binding.toolbarAnonymousConnecting.ivLeft.setVisibility(View.VISIBLE);
        binding.toolbarAnonymousConnecting.ivLeft.setImageResource(R.mipmap.back_icon);
        binding.toolbarAnonymousConnecting.tvHeader.setVisibility(View.VISIBLE);
        binding.toolbarAnonymousConnecting.tvHeader.setText(R.string.anonymous);
    }

    @Override
    public void setOnClickListener() {
        binding.toolbarAnonymousConnecting.ivLeft.setOnClickListener(this);
        binding.ivAnonymousConnecting.setOnClickListener(this);
        binding.toolbarAnonymousConnecting.ivLeft.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_AnonymousConnecting:
                ToastUtils.showToastShort(context,"kg");
                ActivityController.startActivity(context, AnonymousUserActivity.class);
                finish();
                break;
            case R.id.ivLeft:

                onBackPressed();
                break;
            default:
                break;
        }
    }
}
