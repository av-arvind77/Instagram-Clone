package com.yellowseed.application;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.bumptech.glide.request.target.ViewTarget;
import com.quickblox.sample.video.VideoApp;
import com.yellowseed.R;
import com.yellowseed.utils.Player;

/**
 * Created by ankit_mobiloitte on 5/6/18.
 */

public class YellowSeedApplication extends VideoApp {
    public static YellowSeedApplication yellowSeedApplication;

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ViewTarget.setTagId(R.id.glide_tag);
        yellowSeedApplication =YellowSeedApplication.this;
        Player.getInstance().setContext(yellowSeedApplication);
    }

    public static YellowSeedApplication getInstance() {
        return yellowSeedApplication;
    }
}