package com.yellowseed.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yellowseed.R;
import com.yellowseed.adapter.SendToAdapter;
import com.yellowseed.adapter.TagFollowingAdapter;
import com.yellowseed.databinding.ActivityBottomSheetBinding;
import com.yellowseed.databinding.FragmentDocumentsBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.SendToModel;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;

import java.util.ArrayList;

import butterknife.OnClick;

public class BottomSheetActivity extends BaseActivity {
    private ActivityBottomSheetBinding binding;
    BottomSheetBehavior sheetBehavior;
    private Context context;
    private TagFollowingAdapter tagFollowingAdapter;
    private ArrayList<SendToModel> models;
    private SendToAdapter adapter;
    private String[] names = {"John Thomas", "Jenny Koul", "Mike Keel", "Julie Kite", "Tiny Tim"};
    private int[] images = {R.mipmap.profile_icon, R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon3, R.mipmap.profile_icon};
    private boolean darkThemeEnabled;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bottom_sheet);
        context=BottomSheetActivity.this;
        darkThemeEnabled = CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME);


        sheetBehavior = BottomSheetBehavior.from(binding.botomSheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        initializedControl();

    }
    /**
     * manually opening / closing bottom sheet on button click
     */
    public void toggleBottomSheet() {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    @Override
    public void initializedControl() {
        setCurrentTheme();
        sendToRecyclerView();

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
            binding.ivBlockUser.setImageResource(R.mipmap.private_lock_black);

        } else {
            binding.ivBlockUser.setImageResource(R.mipmap.private_lock);


        }
    }
    
    
    
    
    private void sendToRecyclerView() {

        binding.rvRepostHome.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvRepostHome.setLayoutManager(layoutManager);
        models = new ArrayList<>();
        models.addAll(prepareData());
        adapter = new SendToAdapter(models, this);
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

  /*  private void setRecyclerView() {

        binding.rvRepostHome.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvRepostHome.setLayoutManager(linearLayoutManager);
        tagFollowingAdapter = new TagFollowingAdapter(context, models, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
              *//*  if (roomList.get(position).isIs_selected()) {
                    roomList.get(position).setIs_selected(false);
                } else {
                    roomList.get(position).setIs_selected(true);
                }*//*
                tagFollowingAdapter.notifyItemChanged(position);
            }
        });
        binding.rvRepostHome.setAdapter(tagFollowingAdapter);

        binding.ivSendRepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToastShort(context, "Post has been shared.");

                dialog.dismiss();
            *//*    if (tagFollowingAdapter.getSelected().size() > 0) {
                    chatScreenModel = new GetChatResonse.UserInfoBean();
                    chatScreenModel.setCaption(binding.etCaption.getText().toString().trim());
                    chatScreenModel.setCreated_at(String.valueOf(System.currentTimeMillis() / 1000));
                    chatScreenModel.setCreated_timestamp(String.valueOf(System.currentTimeMillis() / 1000));
                    chatScreenModel.setId("");
                    chatScreenModel.setIs_user_sender(true);
                    chatScreenModel.setIs_stared(false);
                    chatScreenModel.setLocal_message_id(String.valueOf(System.currentTimeMillis() / 1000));
                    chatScreenModel.setRead_status(false);
                    chatScreenModel.setReceiver_image("");
                    PostChatBody postChatBody = new PostChatBody();
                    postChatBody.setPost_id(post.getPost().getId());
                    postChatBody.setUser_name(post.getUser().getName());
                    postChatBody.setUser_image(post.getUser().getImage());
                    postChatBody.setDescription(post.getPost().getDescription());
                    if (post.getImages() != null && post.getImages().size() > 0) {
                        postChatBody.setImage(post.getImages().get(0).getFile().getUrl());
                    }
                    chatScreenModel.setBody(new Gson().toJson(postChatBody));
                    chatScreenModel.setBody(new Gson().toJson(postChatBody));
                    chatScreenModel.setSender_id(new PrefManager(context).getUserId());
                    chatScreenModel.setUpload_type("post");
                    sendMessage(chatScreenModel, tagFollowingAdapter.getSelected());
                    binding.etCaption.setText("");
                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                }*//*

            }
        });
    }


}*/

    @Override
    public void setToolBar() {

    }

    @Override
    public void setOnClickListener() {

    }

    @Override
    public void onClick(View view) {

    }
}
