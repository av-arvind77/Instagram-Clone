package com.yellowseed.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.yellowseed.R;
import com.yellowseed.adapter.ExploreSearchAdapter;
import com.yellowseed.databinding.ActivityExploreSearchBinding;
import com.yellowseed.fragments.FragmentExploreSearch.ProfileExploreFragment;
import com.yellowseed.fragments.FragmentExploreSearch.TagsExploreFragment;
import com.yellowseed.model.SearchModel;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.Themes;

import org.greenrobot.eventbus.EventBus;

public class ExploreSearchActivity extends BaseActivity {
    private ActivityExploreSearchBinding binding;
    private Context mContext;
    private String text;
    private Themes themes;
    private Boolean darkThemeEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_explore_search);
        mContext = ExploreSearchActivity.this;
        darkThemeEnabled = CommonUtils.getPreferencesBoolean(mContext, AppConstants.DARK_THEME);
        themes = new Themes(mContext);
        initializedControl();
        setToolBar();
        setOnClickListener();
    }

    @Override
    public void onBackPressed()
    {
        finish();
    }

    @Override
    public void initializedControl()
    {
        viewPagerTab();
        setCurrentTheme();
    }

    private void setCurrentTheme()
    {
        binding.llMain.setBackgroundColor(ContextCompat.getColor(mContext, themes.setDarkTheme()));
        binding.etBroadcastSearch.setBackgroundResource(themes.setDarkSearchDrawable());
        binding.etBroadcastSearch.setHintTextColor(ContextCompat.getColor(mContext, themes.setGreyHint()));
        binding.etBroadcastSearch.setTextColor(ContextCompat.getColor(mContext, themes.setGreyHint()));
        binding.tablayoutExploreSearchItem.setTabTextColors(ContextCompat.getColor(mContext, R.color.lightgrey), ContextCompat.getColor(mContext, themes.setYellow()));
        binding.tablayoutExploreSearchItem.setSelectedTabIndicatorColor(ContextCompat.getColor(mContext, themes.setYellow()));
        binding.tablayoutExploreSearchItem.setBackgroundColor(ContextCompat.getColor(mContext, themes.setDarkTheme()));
        themes.changeIconColor(mContext, binding.expSchBack);

    }

    private void viewPagerTab() {
        setupViewPager(binding.pagerExploreSearch);
        binding.tablayoutExploreSearchItem.setupWithViewPager(binding.pagerExploreSearch);
        binding.etBroadcastSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (binding.etBroadcastSearch.length() > 0) {
                        text = binding.etBroadcastSearch.getText().toString().trim();
                    } else {
                        text = "";
                    }
                    if (binding.pagerExploreSearch.getCurrentItem() == 0) {
                        SearchModel searchModel = new SearchModel();
                        searchModel.setType("profile");
                        searchModel.setSearch(text);
                        EventBus.getDefault().post(searchModel);
                    } else {
                        SearchModel searchModel = new SearchModel();
                        searchModel.setType("post");
                        searchModel.setSearch(text);
                        EventBus.getDefault().post(searchModel);
                    }
                }
                return false;
            }
        });
    }

    private void setupViewPager(ViewPager pagerExploreSearch) {
        ExploreSearchAdapter adapter = new ExploreSearchAdapter(getSupportFragmentManager());

        adapter.addFragment(new ProfileExploreFragment(), "Profile");
        adapter.addFragment(new TagsExploreFragment(), "#Tags");

        pagerExploreSearch.setAdapter(adapter);
    }

    @Override
    public void setToolBar() {
    }

    @Override
    public void setOnClickListener() {
        binding.expSchBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.expSchBack:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            EventBus.getDefault().unregister(mContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
