package com.yellowseed.fragments;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yellowseed.R;
import com.yellowseed.activity.AddContactToBlockActivity;
import com.yellowseed.adapter.BlockedContactsAdapter;
import com.yellowseed.databinding.FragmentBlockedContactsBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.BlockedContactsModel;
import com.yellowseed.utils.ActivityController;
import com.yellowseed.utils.BaseFragment;
import com.yellowseed.utils.Themes;

import java.util.ArrayList;


public class BlockedContactsFragment extends BaseFragment implements View.OnClickListener {

    private FragmentBlockedContactsBinding binding;
    private String[] names = {"John Thomas", "Jenny Koul", "Mike Keel", "Julie Kite", "Tiny Tim", "Julie Kite", "Tiny Tim", "Julie Kite", "Tiny Tim", "Julie Kite", "Tiny Tim", "Tiny Tim", "Julie Kite", "Tiny Tim", "Julie Kite", "Tiny Tim"};
    private int[] images = {R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon3, R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon3, R.mipmap.profile_icon3, R.mipmap.profile_icon3, R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon3, R.mipmap.profile_icon3};
    private Context context;
    private  ArrayList<BlockedContactsModel> models=new ArrayList<>();
    public BlockedContactsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_blocked_contacts, container, false);


        initializedControl();
        setToolBar();
        setOnClickListener();
        return binding.getRoot();
    }

    @Override
    public void initializedControl() {
        initView();
    }



    private void initView() {
        binding.blockedContactsContainer.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.blockedContactsContainer.setLayoutManager(layoutManager);
        models.addAll(prepareData());
        BlockedContactsAdapter adapter = new BlockedContactsAdapter(models, context, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        binding.blockedContactsContainer.setAdapter(adapter);
    }

    private ArrayList<BlockedContactsModel> prepareData() {
        ArrayList<BlockedContactsModel> data = new ArrayList<>();
        for (int i = 0; i < images.length; i++) {
            BlockedContactsModel item = new BlockedContactsModel();
            item.setUserName(names[i]);
            item.setUserPicture(images[i]);
            data.add(item);
        }
        return data;
    }


    private void setTheme() {
        binding.tvAddNewBlockedContacts.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setTolbarText()));
    }


    @Override
    public void setToolBar() {

    }

    @Override
    public void setOnClickListener() {
        setTheme();
        binding.tvAddNewBlockedContacts.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add_new_blocked_contacts:
                ActivityController.startActivity(context, AddContactToBlockActivity.class);
                break;
            default:
                break;
        }
    }
}
