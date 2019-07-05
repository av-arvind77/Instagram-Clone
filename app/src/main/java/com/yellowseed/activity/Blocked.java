package com.yellowseed.activity;

import android.content.Context;
import android.content.res.ColorStateList;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.google.gson.JsonObject;
import com.yellowseed.R;
import com.yellowseed.adapter.CallHistoryAdapter;
import com.yellowseed.adapter.CallhistoryViewPAgerAdapter;
import com.yellowseed.adapter.ViewPagerAdapter;
import com.yellowseed.databinding.ActivityBlockedBinding;
import com.yellowseed.fragments.BlockedContactsFragment;
import com.yellowseed.fragments.BlockedProfilesFragment;
import com.yellowseed.model.resModel.GetChatResonse;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Blocked extends BaseActivity {
    private ActivityBlockedBinding binding;
    private Themes themes;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_blocked);
        context=Blocked.this;
        themes = new Themes(context);
        initializedControl();
        setOnClickListener();
        setToolBar();
    }
    private void setCurrentTheme()
    {
        binding.llBlock.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        binding.toolbarBlocked.tvHeader.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.toolbarBlocked.toolbarMain.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        binding.viewLine.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setViewLineGrey()));
        binding.blockListTabs.setSelectedTabIndicatorColor(ContextCompat.getColor(context, themes.setYellow()));
        Themes.getInstance(context).changeIconColor(context,binding.toolbarBlocked.ivLeft);
        binding.blockListTabs.setTabTextColors(ContextCompat.getColor(context, R.color.lightgrey), ContextCompat.getColor(context, themes.setYellow()));
    }

    @Override
    public void initializedControl() {
        viewPagerTab();
        setCurrentTheme();

    }

    private void viewPagerTab() {
        setUpViewPager(binding.blockListContainer);
        binding.blockListTabs.setupWithViewPager(binding.blockListContainer);
    }

    @Override
    public void setToolBar() {
        binding.toolbarBlocked.ivLeft.setVisibility(View.VISIBLE);
        binding.toolbarBlocked.tvHeader.setVisibility(View.VISIBLE);
        binding.toolbarBlocked.tvHeader.setText(R.string.blocked);
    }
    private void setUpViewPager(ViewPager viewPager) {
        CallhistoryViewPAgerAdapter adapter = new CallhistoryViewPAgerAdapter(getSupportFragmentManager());
        adapter.addFragment(new BlockedProfilesFragment(), "Blocked Profiles");
        adapter.addFragment(new BlockedContactsFragment(), "Blocked Contacts");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void setOnClickListener() {
        binding.toolbarBlocked.ivLeft.setOnClickListener(this);
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
