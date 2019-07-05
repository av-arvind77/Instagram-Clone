package com.yellowseed.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.yellowseed.R;
import com.yellowseed.databinding.ActivityShowStoriesBinding;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.BaseFragment;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.ToastUtils;

public class ShowStoriesActivity extends BaseActivity {
    private ActivityShowStoriesBinding binding;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_stories);
        context = ShowStoriesActivity.this;
        initializedControl();
        setOnClickListener();
    }

    @Override
    public void initializedControl() {

    }

    @Override
    public void setToolBar() {

    }

    @Override
    public void setOnClickListener() {
        binding.ivBack.setOnClickListener(this);
        binding.ivSendStoryMessage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;

            case R.id.ivSendStoryMessage:
                //onBackPressed();
                ToastUtils.showToastShort(context, "Work in Progress");
                break;


            case R.id.rlMainStories:
                //onBackPressed();
                CommonUtils.hideSoftKeyboard(ShowStoriesActivity.this);
                break;

            default:
                break;
        }
    }
}
