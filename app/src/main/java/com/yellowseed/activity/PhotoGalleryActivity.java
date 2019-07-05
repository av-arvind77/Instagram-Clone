package com.yellowseed.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.yellowseed.R;
import com.yellowseed.adapter.CallhistoryViewPAgerAdapter;
import com.yellowseed.adapter.MediaAdapter;
import com.yellowseed.adapter.ViewPagerAdapter;
import com.yellowseed.database.RoomChatTable;
import com.yellowseed.databinding.ActivityPhotoGalleryBinding;
import com.yellowseed.fragments.CallHistoryFragment;
import com.yellowseed.fragments.DocumentsFragment;
import com.yellowseed.fragments.LinksFragment;
import com.yellowseed.fragments.MediaFragment;
import com.yellowseed.fragments.MissedCallFragment;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.resModel.GetChatResonse;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class PhotoGalleryActivity extends BaseActivity {
    private ActivityPhotoGalleryBinding binding;
    private Context context;
    private String room_id, chatName;
    private MediaAdapter imagesAdapter;
    private List<GetChatResonse.UserInfoBean> infoBeans = new ArrayList<>();
    private Themes themes;
    private boolean darkThemeEnabled;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_photo_gallery);
        context = PhotoGalleryActivity.this;
        themes = new Themes(context);
        darkThemeEnabled = CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME);
        getIntentValues();
        initializedControl();
        setToolBar();
        setOnClickListener();
        setHeaderRecylerView();
        RoomChatTable table = new RoomChatTable(context);
        if (table.getMediaChat(room_id) != null && table.getMediaChat(room_id).size() > 0) {
            infoBeans.addAll(table.getMediaChat(room_id));
            imagesAdapter.notifyDataSetChanged();
        }

        table.closeDB();
    }

    private void getIntentValues() {
        room_id = getIntent().getStringExtra(AppConstants.ROOM_ID);
        chatName = getIntent().getStringExtra(AppConstants.NAME);
    }


    @Override
    public void initializedControl() {
        viewPagerTab();
        setCurrentTheme();
    }

    private void setCurrentTheme() {
        binding.llMain.setBackgroundColor(ContextCompat.getColor(context, themes.setDarkTheme()));
        binding.tablayoutMediaItems.setTabTextColors(ContextCompat.getColor(context, R.color.lightgrey), ContextCompat.getColor(context, themes.setYellow()));
        binding.tablayoutMediaItems.setSelectedTabIndicatorColor(ContextCompat.getColor(context, themes.setYellow()));
        binding.tablayoutMediaItems.setBackgroundColor(ContextCompat.getColor(context, themes.setDarkTheme()));
        binding.mediaToolbar.toolbarMain.setBackgroundColor(ContextCompat.getColor(context, themes.setDarkTheme()));
        binding.mediaToolbar.tvHeader.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.mediaToolbar.tvRighttext.setTextColor(ContextCompat.getColor(context, themes.setTolbarText()));
        themes.changeIconColor(context, binding.mediaToolbar.ivLeft);

        binding.viewLine.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setViewLineGrey()));

    }

    private void viewPagerTab() {
        setupViewPager(binding.pagerMediaItems);
        binding.tablayoutMediaItems.setupWithViewPager(binding.pagerMediaItems);
    }

 /*   private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MediaFragment(), "Media", room_id);
        adapter.addFragment(new DocumentsFragment(), "Documents", room_id);
        adapter.addFragment(new LinksFragment(), "Links", room_id);
        viewPager.setAdapter(adapter);
    }*/


    private void setupViewPager(ViewPager callListContainer) {
        CallhistoryViewPAgerAdapter adapter = new CallhistoryViewPAgerAdapter(getSupportFragmentManager());

        adapter.addFragment(new MediaFragment(), "Media");
        adapter.addFragment(new DocumentsFragment(), "Documents");
        adapter.addFragment(new LinksFragment(), "Links");
        callListContainer.setAdapter(adapter);
    }


    @Override
    public void setToolBar() {
        binding.mediaToolbar.ivLeft.setVisibility(View.VISIBLE);
        binding.mediaToolbar.ivLeft.setImageResource(R.mipmap.back_icon);
        binding.mediaToolbar.tvHeader.setVisibility(View.VISIBLE);
        binding.mediaToolbar.tvHeader.setText("John Thomas");
        binding.mediaToolbar.ivRight.setVisibility(View.GONE);
        binding.mediaToolbar.tvRighttext.setVisibility(View.VISIBLE);
        binding.mediaToolbar.tvRighttext.setText("Select");



    }

    @Override
    public void setOnClickListener() {
        binding.mediaToolbar.ivLeft.setOnClickListener(this);
        binding.mediaToolbar.tvRighttext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivLeft:
                onBackPressed();
                break;
            case R.id.tvRighttext:
                ToastUtils.showToastShort(context,"Work in progress");
                break;
            default:
                break;
        }
    }

    private void setHeaderRecylerView() {
        final GridLayoutManager manager = new GridLayoutManager(context, 3);
        binding.rvMedia.setLayoutManager(manager);
        imagesAdapter = new MediaAdapter(infoBeans, context, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (infoBeans.get(position).getBody().endsWith(".mp4")) {
                    Intent intent = new Intent(context, VideoDetailActivity.class);
                    intent.putExtra("url", infoBeans.get(position).getBody());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(context, ShowImageActivity.class);
                    intent.putExtra("url", infoBeans.get(position).getBody());
                    startActivity(intent);
                }

            }
        });
        binding.rvMedia.setAdapter(imagesAdapter);

    }
}
