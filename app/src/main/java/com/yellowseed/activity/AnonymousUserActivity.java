package com.yellowseed.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.daasuu.bl.BubbleLayout;
import com.daasuu.bl.BubblePopupHelper;
import com.yellowseed.R;
import com.yellowseed.adapter.ChatScreenAdapter;
import com.yellowseed.databinding.ActivityAnonymousUserBinding;
import com.yellowseed.databinding.LayoutattachementsiconsBinding;
import com.yellowseed.databinding.LayoutenableavatarBinding;
import com.yellowseed.imageUtils.CheckPermission;
import com.yellowseed.listener.ApiCallback;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.ChatScreenModel;
import com.yellowseed.model.resModel.GetChatResonse;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.TakePictureUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiResponse;
import com.yellowseed.webservices.CommonApi;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class AnonymousUserActivity extends BaseActivity {
    private ActivityAnonymousUserBinding binding;
    private Context context;
    private ArrayList<GetChatResonse.UserInfoBean> chatScreenModels = new ArrayList<>();
    private ChatScreenAdapter chatScreenAdapter;
    private Dialog revealDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_anonymous_user);
        context = AnonymousUserActivity.this;
        initializedControl();
        setToolBar();
        setOnClickListener();
    }

    @Override
    public void initializedControl() {
        setCurrentTheme();

        chatAnonymousRecyclerView();
    }

    private void chatAnonymousRecyclerView() {
        binding.rvAnonymousChatList.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvAnonymousChatList.setLayoutManager(layoutManager);


        /*chatScreenAdapter = new ChatScreenAdapter(AnonymousUserActivity.this, chatScreenModels, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        binding.rvAnonymousChatList.setAdapter(chatScreenAdapter);*/
    }
    private void setCurrentTheme() {
        binding.layoutAnonymousHeader.toolbarMain.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        binding.layoutAnonymousHeader.tvHeader.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.llAnonymous.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        binding.layoutAnonymousBottom.llChatBar.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        binding.layoutAnonymousBottom.llChatBar.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        binding.layoutAnonymousBottom.lleditText.setBackground(ContextCompat.getDrawable(context, Themes.getInstance(context).setDarkStoryBackground()));
        Themes.getInstance(context).changeIconWhite(context, binding.layoutAnonymousBottom.ivChatLeftIcon);
        Themes.getInstance(context).changeIconWhite(context, binding.layoutAnonymousBottom.btnSecondRight);
        Themes.getInstance(context).changeIconWhite(context, binding.layoutAnonymousBottom.tvSendButton);
        Themes.getInstance(context).changeIconWhite(context, binding.layoutAnonymousBottom.ivChatRightIcon);
        Themes.getInstance(context).changeIconWhite(context, binding.layoutAnonymousBottom.btnSecondRight);
        binding.viewLine.setBackgroundColor(ContextCompat.getColor(context,Themes.getInstance(context).setViewLineGrey()));
        binding.viewLine1.setBackgroundColor(ContextCompat.getColor(context,Themes.getInstance(context).setViewLineGrey()));
    }

    @Override
    public void setToolBar() {
        binding.layoutAnonymousHeader.ivLeft.setVisibility(View.VISIBLE);
        binding.layoutAnonymousHeader.ivLeft.setImageResource(R.mipmap.back_icon);
        binding.layoutAnonymousHeader.tvHeader.setVisibility(View.VISIBLE);
        binding.layoutAnonymousHeader.tvHeader.setText(R.string.anonymoususer);
        binding.layoutAnonymousHeader.ivRight.setVisibility(View.VISIBLE);
        binding.layoutAnonymousHeader.ivRight.setImageResource(R.mipmap.three_dot);
        binding.layoutAnonymousBottom.ivChatLeftIcon.setVisibility(View.VISIBLE);
        binding.layoutAnonymousBottom.ivChatLeftIcon.setImageResource(R.mipmap.samile_icon);
        binding.layoutAnonymousBottom.ivChatRightIcon.setVisibility(View.VISIBLE);
        binding.layoutAnonymousBottom.ivChatRightIcon.setImageResource(R.mipmap.photo_video_ico);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_anonymous_user, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }




    private void openMenuPopup( View targetView) {


        BubbleLayout customLayout = (BubbleLayout) LayoutInflater.from(context).inflate(R.layout.anonymous_popup, null);
        customLayout.setBubbleColor(ContextCompat.getColor(context, Themes.getInstance(context).setPopupBackground()));

        final PopupWindow quickAction = BubblePopupHelper.create(context, customLayout);
        LinearLayout llMain = (LinearLayout) customLayout.findViewById(R.id.llMain);
        llMain.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setPopupBackground()));
        TextView tvEnable = (TextView) customLayout.findViewById(R.id.tvEnable);
        TextView tvReveal = (TextView) customLayout.findViewById(R.id.tvReveal);
        TextView tvLogout = (TextView) customLayout.findViewById(R.id.tvLogout);
        ImageView ivEnable = customLayout.findViewById(R.id.ivEnable);
        ImageView ivReveal = customLayout.findViewById(R.id.ivReveal);
        tvEnable.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        tvLogout.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        tvReveal.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        Themes.getInstance(context).changeIconColorToWhite(context, ivEnable);
        Themes.getInstance(context).changeIconColorToWhite(context, ivReveal);



        tvEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToastShort(context, "Avatar is enabled");
                quickAction.dismiss();
            }
        });
        tvReveal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        revealDialogLayout();

quickAction.dismiss();
                    }


        });
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }


        });

        quickAction.showAsDropDown(targetView);
    }




    @Override
    public void setOnClickListener() {
        binding.layoutAnonymousHeader.ivLeft.setOnClickListener(this);
        binding.layoutAnonymousHeader.ivRight.setOnClickListener(this);
        binding.layoutAnonymousBottom.btnSecondRight.setOnClickListener(this);
        binding.layoutAnonymousBottom.ivChatLeftIcon.setOnClickListener(this);
        binding.layoutAnonymousBottom.ivChatRightIcon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivLeft:
                finish();
                break;
            case R.id.ivRight:
                openMenuPopup (v);
                break;

            case R.id.btnSecondRight:
                if (!CheckPermission.checkCameraPermission(context)) {
                    CheckPermission.requestCameraPermission((Activity) context, CheckPermission.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                } else {
                    Intent intent1 = new Intent("android.media.action.IMAGE_CAPTURE");
                    startActivity(intent1);
                }
                break;
            case R.id.ivChatLeftIcon:
                ToastUtils.showToastShort(context, "Work in progress");
                break;
            case R.id.ivChatRightIcon:
                TakePictureUtils.openGallery((Activity) context);
                break;
            case R.id.btnAnonEnableUser:
                revealDialog.dismiss();
                break;
            default:
                break;
        }
    }

    private void revealDialogLayout() {

        LayoutenableavatarBinding layoutenableavatarBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layoutenableavatar, null, false);
        revealDialog = DialogUtils.createDialog(context, layoutenableavatarBinding.getRoot());

        layoutenableavatarBinding.btnAnonEnableUser.setOnClickListener(this);

    }

    }

