package com.yellowseed.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.halilibo.bettervideoplayer.BetterVideoCallback;
import com.halilibo.bettervideoplayer.BetterVideoPlayer;
import com.yellowseed.R;
import com.yellowseed.databinding.ActivityVideoDetailBinding;
import com.yellowseed.utils.Themes;

/**
 * Created by pushpender.singh on 30/7/18.
 */

public class VideoDetailActivity extends AppCompatActivity implements BetterVideoCallback {
    private Context mContext = VideoDetailActivity.this;
    private ActivityVideoDetailBinding binding;
    private String url;
    private Themes themes;
    private Boolean darkThemeEnabled;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video_detail);
        darkThemeEnabled=true;
        setCurrentTheme();
        themes = new Themes(mContext);
        binding.player.setCallback(this);
        setPlayer();
    }
    private void setCurrentTheme() {
        binding.llVideo.setBackgroundColor(ContextCompat.getColor(mContext, themes.setDarkTheme()));
     /*   binding.etName.setBackground(ContextCompat.getDrawable(context, themes.setDarkEditProfileBackground(darkThemeEnabled)));
        binding.etUserName.setBackground(ContextCompat.getDrawable(context, themes.setDarkEditProfileBackground(darkThemeEnabled)));
 */   }
    private void setPlayer() {

        if(getIntent()!=null && getIntent().hasExtra("url")) {
            binding.player.setSource(Uri.parse(getIntent().getStringExtra("url")));
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        binding.player.release();
    }

    @Override
    public void onStarted(BetterVideoPlayer player) {

    }

    @Override
    public void onPaused(BetterVideoPlayer player) {

    }

    @Override
    public void onPreparing(BetterVideoPlayer player) {

    }

    @Override
    public void onPrepared(BetterVideoPlayer player) {

    }

    @Override
    public void onBuffering(int percent) {

    }

    @Override
    public void onError(BetterVideoPlayer player, Exception e) {

    }

    @Override
    public void onCompletion(BetterVideoPlayer player) {

    }

    @Override
    public void onToggleControls(BetterVideoPlayer player, boolean isShowing) {

    }
}
