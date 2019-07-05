package com.yellowseed.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;

import com.yellowseed.R;
import com.yellowseed.adapter.ChatScreenAdapter;
import com.yellowseed.databinding.ActivityChatScreenBinding;
import com.yellowseed.model.CallHistoryModel;
import com.yellowseed.model.resModel.GetChatResonse;
import com.yellowseed.utils.ActivityController;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.TakePictureUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ChatScreenActivity extends BaseActivity implements PopupMenu.OnMenuItemClickListener {


    private ActivityChatScreenBinding binding;
    private Activity context;
    private ArrayList<GetChatResonse.UserInfoBean> chatScreenModels = new ArrayList<>();
    private ChatScreenAdapter chatScreenAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_screen);
        context = ChatScreenActivity.this;

        initializedControl();
        setToolBar();
        setOnClickListener();
    }


    @Override
    public void initializedControl() {
        setCurrentTheme();
        chatRecyclerView();
        acceptReject();
    }


    private void setCurrentTheme() {
        binding.toolbarMain.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        binding.tvHeader.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.tvLastSeen.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.llMain.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeStory()));

        Themes.getInstance(context).changeIconColor(context, binding.ivLeft);
        Themes.getInstance(context).changeIconColor(context, binding.ivRight);
        Themes.getInstance(context).changeIconColor(context, binding.ivRightSecond);

        binding.layoutCustomChat.etChatMessage.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.layoutCustomChat.etChatMessage.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkChatBox()));

        binding.layoutCustomChat.mainLayout.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkChatBox()));
        Themes.getInstance(context).changeIconColorToWhite(context, binding.layoutCustomChat.btnSecondRight);
        Themes.getInstance(context).changeIconColorToWhite(context, binding.layoutCustomChat.ivChatLeftIcon);
        Themes.getInstance(context).changeIconColorToWhite(context, binding.layoutCustomChat.ivChatRightIcon);


       /* Themes.getInstance(context).changeIconColorToWhite(context, binding.layoutChatClickItem.ivBackward);
        Themes.getInstance(context).changeIconColorToWhite(context, binding.layoutChatClickItem.ivCopy);
        Themes.getInstance(context).changeIconColorToWhite(context, binding.layoutChatClickItem.ivForward);
        Themes.getInstance(context).changeShareIcon(context, binding.layoutChatClickItem.ivShare);
*/
    }

        private void acceptReject() {
        if (AppConstants.chatsaccept == true) {
            binding.viewChatBar.setVisibility(View.GONE);
            binding.layoutCustomChat.llRequestAccept.setVisibility(View.VISIBLE);
            binding.layoutCustomChat.llChatBar.setVisibility(View.GONE);
            AppConstants.chatsaccept = false;
        } else {
            binding.viewChatBar.setVisibility(View.VISIBLE);
            binding.layoutCustomChat.llRequestAccept.setVisibility(View.GONE);
            binding.layoutCustomChat.llChatBar.setVisibility(View.VISIBLE);
        }
    }

    private void chatRecyclerView() {
        binding.rvChatList.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvChatList.setLayoutManager(layoutManager);
       /* chatScreenAdapter = new ChatScreenAdapter(context, chatScreenModels, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });*/
        binding.rvChatList.setAdapter(chatScreenAdapter);
     //   chatScreenAdapter.notifyItemRangeChanged(0, chatScreenAdapter.getItemCount());


    }

    private void showMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        try {
            Field[] fields = popup.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popup);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper
                            .getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod(
                            "setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.menu_chat_screen);
        popup.show();

    }


//    @Override
//    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
//        if (menu != null) {
//            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
//                try {
//                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
//                    m.setAccessible(true);
//                    m.invoke(menu, true);
//                } catch (Exception e) {
//                    Log.e(getClass().getSimpleName(), "onMenuOpened...unable to set icons for overflow menu", e);
//                }
//            }
//        }
//        return super.onPrepareOptionsPanel(view, menu);
//    }

    @Override
    public void setToolBar() {

        binding.ivLeft.setVisibility(View.VISIBLE);
        binding.ivLeft.setImageResource(R.mipmap.back_icon);
        binding.tvHeader.setVisibility(View.VISIBLE);
        binding.tvHeader.setText(R.string.john_thomas);
        binding.ivRight.setVisibility(View.VISIBLE);
        binding.ivRight.setImageResource(R.mipmap.three_dot);
        binding.layoutCustomChat.ivChatLeftIcon.setVisibility(View.VISIBLE);
        binding.layoutCustomChat.ivChatRightIcon.setVisibility(View.VISIBLE);
        binding.layoutCustomChat.ivChatRightIcon.setImageResource(R.mipmap.photo_video_ico);
    }

    @Override
    public void setOnClickListener() {
        binding.ivLeft.setOnClickListener(this);
        binding.ivRight.setOnClickListener(this);
        binding.layoutCustomChat.btnSecondRight.setOnClickListener(this);
        binding.layoutCustomChat.btnChatsRequestAccept.setOnClickListener(this);
        binding.layoutCustomChat.btnChatsRequestReject.setOnClickListener(this);
        binding.layoutCustomChat.ivChatLeftIcon.setOnClickListener(this);
        binding.layoutCustomChat.ivChatRightIcon.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnSecondRight:
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivity(intent);
                break;

            case R.id.ivRight:
                showMenu(view);
                break;
            case R.id.ivLeft:
                onBackPressed();
                break;
            case R.id.btnChatsRequestAccept:
                binding.viewChatBar.setVisibility(View.VISIBLE);
                binding.layoutCustomChat.llRequestAccept.setVisibility(View.GONE);
                binding.layoutCustomChat.llChatBar.setVisibility(View.VISIBLE);
                break;
            case R.id.btnChatsRequestReject:
                ActivityController.startActivity(context, DirectScreenActivity.class);
                finish();
                break;
            case R.id.ivChatLeftIcon:
                ToastUtils.showToastShort(context, "Work in progress");
                break;
            case R.id.ivChatRightIcon:
                TakePictureUtils.openGallery(context);
                break;


            default:
                break;


        }

    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_EnableAvatar:
                ToastUtils.showToastShort(context, "Enable Avatar");

                return true;
            case R.id.action_Block:
                ToastUtils.showToastShort(context, "Block");
                return true;
            case R.id.action_ReportUser:
                enableUserDialog();
                return true;
            default:
                return false;

        }

    }

    private void enableUserDialog() {
        final CharSequence[] items = {Html.fromHtml("<font color = '#FC2B2B'>Its Spam</font>"), Html.fromHtml("<font color = '#FC2B2B'>Report this Profile</font>"), Html.fromHtml("<font color = '#FC2B2B'>Inappropriate</font>")};
        AlertDialog.Builder builder;
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {

            builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom));
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle(getString(R.string.understandthereason));
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {   //Its Spam
                    dialog.dismiss();
                    finish();
                } else if (item == 1) {    //Inappropiate
                    dialog.dismiss();
                    finish();
                } else if (item == 2) {       //Its False Story
                    dialog.dismiss();
                    finish();
                } else {       //Dialog Dismiss
                    dialog.dismiss();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
        builder.setCancelable(true);
    }

}