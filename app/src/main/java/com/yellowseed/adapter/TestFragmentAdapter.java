package com.yellowseed.adapter;
/*
 *
 * Copyright (C) 2014 Krishna Kumar Sharma
 *
 *  */

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.yellowseed.fragments.TestFragment;

class TestFragmentAdapter extends FragmentPagerAdapter {

    private Context context;
    private int[] content;

    TestFragmentAdapter(FragmentManager fm, Context context, int[] data) {
        super(fm);
        this.context = context;
        content = data;
    }

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        Object obj = super.instantiateItem(container, position);
        return obj;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        if (object != null) {
            return ((Fragment) object).getView() == view;
        } else {
            return false;
        }
    }

    @Override
    public int getItemPosition(Object object) {
        // Causes adapter to reload all Fragments when
        // notifyDataSetChanged is called
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        return TestFragment.newInstance(content[position], context);
    }

    @Override
    public int getCount() {
        return content == null ? 0 : content.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "ASDd";
    }

}