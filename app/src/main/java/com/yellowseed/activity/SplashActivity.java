package com.yellowseed.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.yellowseed.R;
import com.yellowseed.databinding.ActivitySplashBinding;
import com.yellowseed.model.DeviceTokenEvent;
import com.yellowseed.utils.ActivityController;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.PrefManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashActivity extends BaseActivity {
    private static String TAG = SplashActivity.class.getSimpleName();
    ActivitySplashBinding binding;
    private Context context;
    private PrefManager prefManager;

    @Override
    public void onStart() {
        super.onStart();
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        context = SplashActivity.this;
        prefManager = PrefManager.getInstance(context);
        printHashKey();
        initializedControl();
        setToolBar();
        setOnClickListener();
        startFirstActivity();

        if (!TextUtils.isEmpty(prefManager.getKeyDeviceToken())) {
           // startFirstActivity();
        }
    }

    public void printHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }

    @Override
    public void initializedControl() {

    }

    @Override
    public void setToolBar() {

    }

    @Override
    public void setOnClickListener() {

    }

    @Override
    public void onClick(View v) {

    }

    public void startFirstActivity() {
        new Handler().postDelayed(() -> {
            if (!PrefManager.getInstance(context).isLogin()) {
                ActivityController.startActivity(context, LoginActivity.class);
            } else {
                ActivityController.startActivity(context, HomeActivity.class);
            }
            finish();
        }, 2000);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DeviceTokenEvent deviceTokenEvent) {
        if (deviceTokenEvent != null) {
            prefManager.setKeyDeviceToken(deviceTokenEvent.getToken());
            //startFirstActivity();
        }
    }
}
