package com.yellowseed.utils;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.yellowseed.R;


public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    protected String TAG;

    public abstract void initializedControl();

    public abstract void setToolBar();

    public abstract void setOnClickListener();

    protected void setTag(Activity activity) {
        TAG = activity.getClass().getSimpleName();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
