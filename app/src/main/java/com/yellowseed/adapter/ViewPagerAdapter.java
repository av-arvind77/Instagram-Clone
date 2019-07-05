package com.yellowseed.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private String roomId;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle=new Bundle();
        bundle.putString("roomId",roomId);
        Fragment fragment=mFragmentList.get(position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount()
    {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title,String roomId) {
        this.roomId=roomId;
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
        mFragmentTitleList.add(title);
    }

    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }


}
