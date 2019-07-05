package com.yellowseed.utils;

import android.app.Activity;
import android.support.v4.app.Fragment;


public abstract class BaseFragment extends Fragment {

    protected String TAG;

    public abstract void initializedControl();

    public abstract void setToolBar();

    public abstract void setOnClickListener();

    protected void setTag(Activity activity) {
        TAG = activity.getClass().getSimpleName();
    }

}
