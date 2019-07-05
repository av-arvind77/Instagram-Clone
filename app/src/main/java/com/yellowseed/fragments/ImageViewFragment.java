package com.yellowseed.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.yellowseed.databinding.RowHomeBottomBinding;
import com.yellowseed.webservices.response.homepost.Image;

import java.util.List;

/**
 * Created by pushpender.singh on 4/8/18.
 */

public class ImageViewFragment  extends Fragment {
    private Context context;
    private RowHomeBottomBinding binding;
    private List<Image> arlImages;
    private int position;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //final View view = inflater.inflate(R.layout.fragment_special_offer, container, false);
        binding = RowHomeBottomBinding.inflate(inflater, container, false);
        Picasso.with(context).load(arlImages.get(position).getFile().getUrl());

        return binding.getRoot();
    }
    /**
     * Instantiates a new Food fragment.
     */
    public ImageViewFragment() {

    }
    /**
     * Instantiates a new Food fragment.
     * @param position   the position
     */
    @SuppressLint("ValidFragment")
    public ImageViewFragment(int position, List<Image> arlImages) {
        this.arlImages = arlImages;
        this.position = position;

    }

    /**
     * New instance food fragment.
     *
     * @param position   the position
     * @return the food fragment
     */
    public static ImageViewFragment newInstance(int position, List<Image> arlImages) {
        return new ImageViewFragment(position, arlImages);
    }

}
