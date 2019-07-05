package com.yellowseed.fragments;
/*
 *
 * Copyright (C) 2014 Krishna Kumar Sharma
 *
 *  */

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yellowseed.R;


public final class TestFragment extends Fragment {

    private int mContent;

    public static TestFragment newInstance(int content, Context c) {

        TestFragment fragment = new TestFragment();

        fragment.mContent = content;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup Rootview = (ViewGroup) inflater.inflate(R.layout.item,
                container, false);
        int bg = Color.rgb((int) Math.floor(Math.random() * 128) + 64,
                (int) Math.floor(Math.random() * 128) + 64,
                (int) Math.floor(Math.random() * 128) + 64);
        ImageView imageView = Rootview.findViewById(R.id.ivImage);
        imageView.setImageResource(mContent);
        return Rootview;
    }
}
