package com.yellowseed.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.yellowseed.R;
import com.yellowseed.adapter.CallhistoryViewPAgerAdapter;
import com.yellowseed.adapter.ExploreSearchAdapter;
import com.yellowseed.adapter.MediaAdapter;
import com.yellowseed.adapter.SearchFollowingAdapter;
import com.yellowseed.databinding.ActivityFollowerListBinding;
import com.yellowseed.fragments.DocumentsFragment;
import com.yellowseed.fragments.FragmentExploreSearch.ProfileExploreFragment;
import com.yellowseed.fragments.FragmentExploreSearch.TagsExploreFragment;
import com.yellowseed.fragments.LinksFragment;
import com.yellowseed.fragments.MediaFragment;
import com.yellowseed.fragments.SearchFollowerFragment;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.SearchFollowingModel;
import com.yellowseed.model.SearchModel;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.Themes;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class FollowerListActivity extends BaseActivity {
    private ActivityFollowerListBinding binding;
    private Context context;
    private ArrayList<SearchFollowingModel> arlModel=new ArrayList<>();
    private SearchFollowingAdapter adapter;
    private int page=1;
    private boolean isLastPage;
    private boolean isLoading;
    private Integer totalRecord;
    private Themes themes;
    private Boolean darkThemeEnabled;

    private String[] names = {"John Thomas", "Jenny Koul", "Mike Keel", "Julie Kite", "Tiny Tim", "Julie Kite", "Tiny Tim", "Tiny Tim", "Julie Kite", "Tiny Tim", "Julie Kite", "Tiny Tim", "Tiny Tim"};
    private int[] images = {R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon};
    private String tab_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding=DataBindingUtil.setContentView(this,R.layout.activity_follower_list);
        context = FollowerListActivity.this;
        themes = new Themes(context);
        initializedControl();
    }

    @Override
    public void initializedControl() {
        viewPagerTab();

        setCurrentTheme();
        setToolBar();
        setOnClickListener();
        setRecycler();

    }

    private void setRecycler() {
    }

    private void setCurrentTheme() {
        binding.toolbarCallDetail.toolbarMain.setBackgroundColor(ContextCompat.getColor(context, themes.setDarkTheme()));
        binding.toolbarCallDetail.tvHeader.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.viewLine.setBackgroundColor(ContextCompat.getColor(context, themes.setViewLineGrey()));

        themes.changeIconColor(context, binding.toolbarCallDetail.ivLeft);
        binding.followertabs.setSelectedTabIndicatorColor(ContextCompat.getColor(context, themes.setSelectedIndicatorColor()));
        binding.llMain.setBackgroundColor(ContextCompat.getColor(context, themes.setDarkTheme()));
        binding.followertabs.setTabTextColors(ContextCompat.getColor(context, R.color.lightgrey), ContextCompat.getColor(context, themes.setYellow()));
        binding.followertabs.setSelectedTabIndicatorColor(ContextCompat.getColor(context, themes.setYellow()));
        binding.followertabs.setBackgroundColor(ContextCompat.getColor(context, themes.setDarkTheme()));


    }

    @Override
    public void setToolBar() {
        binding.toolbarCallDetail.ivLeft.setVisibility(View.VISIBLE);
        binding.toolbarCallDetail.tvHeader.setVisibility(View.VISIBLE);
        binding.toolbarCallDetail.tvHeader.setText("Followers");




    }

    @Override
    public void setOnClickListener() {
        binding.toolbarCallDetail.ivLeft.setOnClickListener(this);


    }


    private void viewPagerTab() {
        setupViewPager(binding.followerListContainer);
        binding.followertabs.setupWithViewPager(binding.followerListContainer);
    }
    private void setupViewPager(ViewPager callListContainer) {
        CallhistoryViewPAgerAdapter adapter = new CallhistoryViewPAgerAdapter(getSupportFragmentManager());

        adapter.addFragment(new SearchFollowerFragment(), "Followers 429");
        adapter.addFragment(new SearchFollowerFragment(), "Following 478");
        callListContainer.setAdapter(adapter);
    }

    @Nullable
    @Override
    public Intent getParentActivityIntent() {
        final Bundle bundle = new Bundle();
        final Intent intent = new Intent(this, FollowerListActivity.class);
        bundle.putString("TabNumber", tab_num);
        intent.putExtras(bundle);
        return intent;
    }


/*
    private void setupViewPager( ) {
        CallhistoryViewPAgerAdapter adapter = new CallhistoryViewPAgerAdapter(getSupportFragmentManager());

        adapter.addFragment(new VotesFragment(), "");
        adapter.addFragment(new VotesFragment(), "");
        adapter.addFragment(new VotesFragment(), "");
        adapter.addFragment(new VotesFragment(), "");

        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
              //  onSwipe(position);

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }*/




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivLeft:
                onBackPressed();
                break;


            default:
                break;
        }
    }
}
