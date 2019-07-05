package com.yellowseed.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yellowseed.R;
import com.yellowseed.activity.ChatScreenActivity;
import com.yellowseed.activity.ShowPostItemsActivity;
import com.yellowseed.adapter.GridAdapter;
import com.yellowseed.adapter.GridFragmentAdapter;
import com.yellowseed.adapter.MyProfileImageAdapter;
import com.yellowseed.databinding.FragmentGridBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.HomeStoriesModel;
import com.yellowseed.utils.ActivityController;
import com.yellowseed.utils.BaseFragment;

import java.util.ArrayList;

public class GridFragment extends BaseFragment {

    private Context context;
    private FragmentGridBinding binding;
    private GridFragmentAdapter gridFragmentAdapter;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_grid,container,false);
        View view = binding.getRoot();
        initializedControl();
        setOnClickListener();
        return view;
    }


    @Override
    public void initializedControl() {
setPostRecyclerView();
    }

    public void setPostRecyclerView(){

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        binding.rvGridRecylerView.setLayoutManager(layoutManager);
        binding.rvGridRecylerView.setFocusable(false);
        binding.rvGridRecylerView.setNestedScrollingEnabled(false);
        gridFragmentAdapter = new GridFragmentAdapter(context, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.tvTextSearchStory:
                        ActivityController.startActivity(context, ShowPostItemsActivity.class);
                        break;
                }
            }
        });
        binding.rvGridRecylerView.setFocusable(false);
        binding.rvGridRecylerView.setAdapter(gridFragmentAdapter);
    }

    @Override
    public void setToolBar() {

    }

    @Override
    public void setOnClickListener() {

    }
}
