package com.yellowseed.fragments;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yellowseed.R;
import com.yellowseed.activity.HomeActivity;
import com.yellowseed.activity.SocialPostActivity;
import com.yellowseed.adapter.SendToAdapter;
import com.yellowseed.adapter.TagFollowingAdapter;
import com.yellowseed.databinding.FragmentBottoSheetBinding;
import com.yellowseed.model.SendToModel;
import com.yellowseed.utils.ActivityController;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DirectShareFragment extends BottomSheetDialogFragment implements View.OnClickListener{
    private FragmentBottoSheetBinding binding;
    private Context context;
    private TagFollowingAdapter tagFollowingAdapter;
    private ArrayList<SendToModel> models;
    private SendToAdapter adapter;
    private String[] names = { "Jenny Koul", "Mike Keel", "Julie Kite", "Tiny Tim","Jullie Kite","Timy Tim","Mike Keel","Nick Thomas","Tiny Tim","Jullie Kite","Timy Tim","Mike Keel"};
    private int[] images = { R.mipmap.profile_icon3, R.mipmap.profile_icon4, R.mipmap.profile_icon2, R.mipmap.profile_icon,R.mipmap.profile_icon4, R.mipmap.profile_icon3, R.mipmap.profile_icon2, R.mipmap.profile_icon, R.mipmap.profile_icon3, R.mipmap.profile_icon4, R.mipmap.profile_icon2, R.mipmap.profile_icon};
    private boolean darkThemeEnabled;



    public DirectShareFragment() {
        // Required empty public constructor
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
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_botto_sheet, container, false);
        darkThemeEnabled = CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME);
        initializedControl();

        return binding.getRoot();


    }

    private void initializedControl() {
        setCurrentTheme();
        setOnClickListener();
        sendToRecyclerView();
    }

    private void setOnClickListener() {
        binding.ivSendRepost.setOnClickListener(this);
    }

    private void sendToRecyclerView() {
        binding.rvRepostHome.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        binding.rvRepostHome.setLayoutManager(layoutManager);
        models = new ArrayList<>();
        models.addAll(prepareData());
        adapter = new SendToAdapter(models, context);
        binding.rvRepostHome.setAdapter(adapter);
    }

    private ArrayList<SendToModel> prepareData() {

        ArrayList<SendToModel> data = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            SendToModel model = new SendToModel();
            model.setName_url(names[i]);
            model.setImage_url(images[i]);
            data.add(model);
        }
        return data;

    }

    private void setCurrentTheme() {


        binding.llRepost.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeDialog()));
        binding.etRepostSearch.setBackground(ContextCompat.getDrawable(context, Themes.getInstance(context).setDarkSearchDrawable()));
        binding.viewLine.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setViewLineGrey()));
        binding.etRepostSearch.setHintTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkHintColor()));
        binding.etRepostSearch.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.tvPrivateAccount.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.tvFollower.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setLightThemeText()));

        binding.etCaption.setBackground(ContextCompat.getDrawable(context, Themes.getInstance(context).setDarkSearchDrawable()));
        binding.etCaption.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));

        if (darkThemeEnabled) {
            binding.ivBlockUser.setImageResource(R.mipmap.private_lock);

        } else {
            binding.ivBlockUser.setImageResource(R.mipmap.private_lock_black);


        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.ivSendRepost:
                ToastUtils.showToastShort(context,"Post has been shared");
                ActivityController.startActivity(context, HomeActivity.class);
                break;

                default:break;

        }

    }
}
