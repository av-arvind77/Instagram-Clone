package com.yellowseed.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;
import com.yellowseed.R;
import com.yellowseed.activity.CallDetailsActivity;
import com.yellowseed.activity.MediaClickViewActivity;
import com.yellowseed.adapter.ImagesAdapter;
import com.yellowseed.database.RoomChatTable;
import com.yellowseed.databinding.FragmentMediaBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.listener.OnItemLongClickListener;
import com.yellowseed.model.CallHistoryModel;
import com.yellowseed.model.ImagesModelClass;
import com.yellowseed.utils.ActivityController;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseFragment;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;


public class MediaFragment extends BaseFragment {
    private FragmentMediaBinding binding;
    private String roomId;
    private Themes themes;
    private Boolean darkThemeEnabled;
    private Context context;
    public boolean isSingleSelection = false;
    private List<ImagesModelClass> arlist=new ArrayList<>();
    private ImagesAdapter adapter;
    private int[] image={R.mipmap.background_second,R.mipmap.background_second,R.mipmap.background_second,R.mipmap.background_second};

    public MediaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
        themes = new Themes(context);
        darkThemeEnabled = CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         //Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_media, container, false);
        View view = binding.getRoot();
        initializedControl();
      /*  roomId=getArguments().getString("roomId");
        RoomChatTable roomChatTable=new RoomChatTable(context);
        if(roomId!=null&&roomId.length()>0) {
            roomChatTable.getChatPerRoom(roomId);
        }*/

        setToolBar();
        setOnClickListener();
        initViews();
        return view;

    }

    private void initViews() {
      //  binding.todayGrid.setHasFixedSize(true);
        binding.yesterdayGrid.setHasFixedSize(true);


        RecyclerView.LayoutManager layoutManagerYesterday = new GridLayoutManager(getContext(), 3);
        RecyclerView.LayoutManager layoutManagerToday = new GridLayoutManager(getContext(), 3);
        binding.todayGrid.setLayoutManager(layoutManagerToday);
        arlist.addAll(prepareData());

        binding.yesterdayGrid.setLayoutManager(layoutManagerYesterday);
         adapter = new ImagesAdapter(arlist,getContext(), new OnItemLongClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.image:

                        ToastUtils.showToastShort(context,"ghh");

                        if (isSingleSelection) {
                            if (arlist.get(position).isSelected()) {

                                arlist.get(position).setSelected(false);

                            } else {
                                arlist.get(position).setSelected(true);

                            }
                            adapter.notifyItemChanged(position);

                            ActivityController.startActivity(getContext(), MediaClickViewActivity.class);
                            break;
                        }
                }
            }


            @Override
            public void onLongClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.image:

                        ToastUtils.showToastShort(context,"ghh");

                        if (arlist.get(position).isSelected()) {

                            arlist.get(position).setSelected(false);

                        } else {
                            arlist.get(position).setSelected(true);

                        }
                        adapter.notifyItemChanged(position);




                }
            }
        });

           
        binding.todayGrid.setAdapter(adapter);
        binding.yesterdayGrid.setAdapter(adapter);

    }


    private List<ImagesModelClass> prepareData() {
        List<ImagesModelClass> data = new ArrayList<>();
        for (int i = 0; i < image.length; i++) {
            ImagesModelClass model = new ImagesModelClass();
            model.setPicture(image[i]);

            data.add(model);
        }
        return data;
    }
    @Override
    public void initializedControl() {
        setCurrentTheme();

    }

    private void setCurrentTheme() {
        binding.llMain.setBackgroundColor(ContextCompat.getColor(context,themes.setDarkTheme()));
        binding.tvToday.setTextColor(ContextCompat.getColor(context,themes.setDarkThemeText()));
        binding.tvYesterday.setTextColor(ContextCompat.getColor(context,themes.setDarkThemeText()));

    }

    @Override
    public void setToolBar() {

    }

    @Override
    public void setOnClickListener() {

    }
}
