package com.yellowseed.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.JsonObject;
import com.yellowseed.R;
import com.yellowseed.adapter.SavedUserAdapter;
import com.yellowseed.database.RoomsBackgroundTable;
import com.yellowseed.database.RoomsTable;
import com.yellowseed.database.UserTable;
import com.yellowseed.database.WallpaperModel;
import com.yellowseed.databinding.ActivitySettingsBinding;
import com.yellowseed.databinding.LayoutswitchaccountBinding;
import com.yellowseed.imageUtils.CropImage;
import com.yellowseed.imageUtils.TakePictureUtils;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.FollowingRequestModel;
import com.yellowseed.model.reqModel.UserModel;
import com.yellowseed.utils.ActivityController;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.PrefManager;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;
import com.yellowseed.webservices.ApiResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

import static com.yellowseed.imageUtils.TakePictureUtils.CROP_FROM_CAMERA;

public class SettingsActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private static final int TAKE_PICTURE = 1;
    private ActivitySettingsBinding binding;
    private Context context;
    private boolean isMuteNotification, isShowAwater, isPrivateProfile, isAllowComment, isAllowSharing, isSaveGallery, isChatWallpaper, isMediaDownloadPrivate,
            isMediaDownloadGroup;
    private int lastSeen, tagPost, profilePhoto;
    private boolean isFirstTime = true;
    private String image = "";
    private File imageFile;
    private Themes themes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        context = SettingsActivity.this;
        themes = new Themes(context);
        initializedControl();
        setToolBar();
        setOnClickListener();
    }


    @Override
    public void initializedControl() {
        setCurrentTheme();
        settingGender();
    }

    private void settingGender() {
        ArrayAdapter<CharSequence> arrayAdapterLanguage = ArrayAdapter.createFromResource(context, R.array.language, R.layout.simple_spinner_dropdown_item);
        binding.languageSpinner.setAdapter(arrayAdapterLanguage);
    }

    @Override
    public void setToolBar() {
        binding.toolbarSettings.ivLeft.setVisibility(View.VISIBLE);
        binding.toolbarSettings.ivLeft.setImageResource(R.mipmap.back_icon);
        binding.toolbarSettings.tvHeader.setVisibility(View.VISIBLE);
        binding.toolbarSettings.tvHeader.setText(R.string.settings);
    }

    public void setCurrentTheme() {
        binding.toolbarSettings.toolbarMain.setBackgroundColor(ContextCompat.getColor(context, themes.setDarkTheme()));
        binding.toolbarSettings.tvHeader.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        themes.changeIconColor(context, binding.toolbarSettings.ivLeft);
        binding.llMain.setBackgroundColor(ContextCompat.getColor(context, themes.setDarkTheme()));
        binding.tvHiddenSetting.setTextColor(ContextCompat.getColor(context, themes.setViewLine()));
        binding.tvVideoSetting.setTextColor(ContextCompat.getColor(context, themes.setViewLine()));
        binding.tvAccountSetting.setTextColor(ContextCompat.getColor(context, themes.setViewLine()));
        binding.tvProfileSetting.setTextColor(ContextCompat.getColor(context, themes.setViewLine()));
        binding.tvNotificationSetting.setTextColor(ContextCompat.getColor(context, themes.setViewLine()));
        binding.tvMute.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.tvStorySetting.setTextColor(ContextCompat.getColor(context, themes.setViewLine()));
        binding.tvChatSetting.setTextColor(ContextCompat.getColor(context, themes.setViewLine()));
        binding.tvResetHidden.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.tvChangePasscode.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));

        binding.tvChangePassword.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.tvHideChatExit.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.tvTouchID.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.rbOn.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.rbWifi.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.rbOff.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.tvChangeNumber.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.tvSelectLanguage.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.tvSwitchAccount.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.languageSpinner.setPopupBackgroundDrawable(ContextCompat.getDrawable(context, Themes.getInstance(context).setDarkThemeDialog()));
        binding.tvAddNewAccount.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.tvEditProfile.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.tvTaggingPost.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.tvTaggingPostValue.setTextColor(ContextCompat.getColor(context, themes.setLightThemeText()));
        binding.tvShowAvatar.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.tvPrivateProfile.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.tvAllowComment.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.tvAllowSharing.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.tvSaveToGallery.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.tvLastSeen.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.tvProfilePhoto.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.tvLastSeenEvery.setTextColor(ContextCompat.getColor(context, themes.setLightThemeText()));
        binding.tvProfilePhotoValue.setTextColor(ContextCompat.getColor(context, themes.setLightThemeText()));
        binding.tvProfilePhoto.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
       // binding.tvProfilePhotoValue.setTextColor(ContextCompat.getColor(context, themes.setLightThemeText()));
        binding.tvChatWallpaper.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.tvSaveIncoming.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.tvAutomatic.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.tvAutomaticMediaDownload.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.languageSpinner.setBackground(ContextCompat.getDrawable(context, themes.setSpinnerDrawable()));

        binding.tvUpdateLastSeen.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.tvTurnedOff.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.tvOnlineStatus.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.tvAllowUser.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));


        binding.viewLine.setBackgroundColor(ContextCompat.getColor(context, themes.setViewLineGrey()));
        binding.viewLine2.setBackgroundColor(ContextCompat.getColor(context, themes.setViewLineGrey()));
        binding.viewLine3.setBackgroundColor(ContextCompat.getColor(context, themes.setViewLineGrey()));
        binding.viewLine4.setBackgroundColor(ContextCompat.getColor(context, themes.setViewLineGrey()));
        binding.viewLine5.setBackgroundColor(ContextCompat.getColor(context, themes.setViewLineGrey()));
        binding.viewLine6.setBackgroundColor(ContextCompat.getColor(context, themes.setViewLineGrey()));
        binding.viewLine7.setBackgroundColor(ContextCompat.getColor(context, themes.setViewLineGrey()));

        binding.viewLine8.setBackgroundColor(ContextCompat.getColor(context, themes.setViewLineGrey()));


    }


    @Override
    public void setOnClickListener() {
        binding.tvAddNewAccount.setOnClickListener(this);
        binding.tvAutomaticMediaDownload.setOnClickListener(this);
        binding.tvBlocked.setOnClickListener(this);
        binding.tvChangeNumber.setOnClickListener(this);
        binding.tvChangePassword.setOnClickListener(this);
        binding.tvChatWallpaper.setOnClickListener(this);
        binding.tvDeleteAllChats.setOnClickListener(this);
        binding.tvEditProfile.setOnClickListener(this);
        binding.profilePhoto.setOnClickListener(this);
        binding.tvLastSeen.setOnClickListener(this);
        binding.taggingPost.setOnClickListener(this);
        binding.tvSwitchAccount.setOnClickListener(this);
        binding.tvReportProblem.setOnClickListener(this);
        binding.toolbarSettings.ivLeft.setOnClickListener(this);
        binding.tvAddNewAccount.setOnClickListener(this);
        binding.tvMute.setOnClickListener(this);
        binding.rbOff.setOnCheckedChangeListener(this);
        binding.rbOn.setOnCheckedChangeListener(this);
        binding.rbWifi.setOnCheckedChangeListener(this);


//        if (!isFirstTime){
        binding.switchSaveToGallery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               /* saveSettingApi(tagPost, lastSeen, profilePhoto, isMediaDownloadPrivate, isMediaDownloadGroup, isShowAwater, isPrivateProfile, isAllowComment,
                        isAllowSharing, isChecked, isChatWallpaper);*/
            }
        });

        binding.switchAllowSharing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
              /*  saveSettingApi(tagPost, lastSeen, profilePhoto, isMediaDownloadPrivate, isMediaDownloadGroup, isShowAwater, isPrivateProfile, isAllowComment,
                        isChecked, isSaveGallery, isChatWallpaper);*/
            }
        });

        binding.switchAllowComments.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            /*    saveSettingApi(tagPost, lastSeen, profilePhoto, isMediaDownloadPrivate, isMediaDownloadGroup, isShowAwater, isPrivateProfile, isChecked,
                        isAllowSharing, isSaveGallery, isChatWallpaper);*/
            }
        });

        binding.switchPrivateProfile.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               /* saveSettingApi(tagPost, lastSeen, profilePhoto, isMediaDownloadPrivate, isMediaDownloadGroup, isShowAwater, isChecked, isAllowComment,
                        isAllowSharing, isSaveGallery, isChatWallpaper);*/
            }
        });

        binding.switchShowAvatarOnProfile.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                /*saveSettingApi(tagPost, lastSeen, profilePhoto, isMediaDownloadPrivate, isMediaDownloadGroup, isChecked, isPrivateProfile, isAllowComment,
                        isAllowSharing, isSaveGallery, isChatWallpaper);*/
            }
        });
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ivLeft:
                finish();
                break;

            case R.id.tv_change_password:
                ActivityController.startActivity(context, ChangePasswordActivity.class);
                break;

            case R.id.tv_change_number:
                ActivityController.startActivity(context, ChangeNumberActivity.class);
                break;

            case R.id.tv_switch_account:
                onSwitchSelectedSettings();
                break;

            case R.id.tv_add_new_account:
                ActivityController.startActivity(context, LoginActivity.class);
                break;

            case R.id.tv_edit_profile:
                ActivityController.startActivity(context, EditProfileActivity.class);
                break;

            case R.id.tagging_post:
                dialogTaggingPost();
                break;

            case R.id.tv_last_seen:
                ActivityController.startActivity(context, LastSeenActivity.class);


/*
                startActivity(new Intent(context, LastSeenActivity.class).putExtra(AppConstants.IS_MUTE_NOTIFICATION, isMuteNotification).
                        putExtra(AppConstants.IS_SHOW_AWATER, String.valueOf(isShowAwater)).putExtra(AppConstants.IS_PRIVATE_PROFILE, String.valueOf(isPrivateProfile)).
                        putExtra(AppConstants.IS_ALLOW_COMMENT, String.valueOf(isAllowComment)).putExtra(AppConstants.IS_ALLOW_SHARING, String.valueOf(isAllowSharing)).
                        putExtra(AppConstants.IS_SAVE_GALLERY, String.valueOf(isSaveGallery)).putExtra(AppConstants.LAST_SEEN, String.valueOf(lastSeen)).
                        putExtra(AppConstants.TAG_POST, String.valueOf(tagPost)).putExtra(AppConstants.IS_CHAT_WALLPAPER, String.valueOf(isChatWallpaper)).
                        putExtra(AppConstants.PROFILE_PHOTO, String.valueOf(profilePhoto)).putExtra(AppConstants.IS_MEDIA_DOWNLOAD_PRIVATE, String.valueOf(isMediaDownloadPrivate)).
                        putExtra(AppConstants.IS_MEDIA_DOWNLOAD_GROUP, String.valueOf(isMediaDownloadGroup)));
      */          break;

            case R.id.profile_photo:
                dialogPhotoPermission();
                break;

            case R.id.tv_chat_wallpaper:
                dialogChatWallpaper();
                break;

            case R.id.tv_automatic_media_download:
                ActivityController.startActivity(context, AutomaticMediaDownloadActivity.class);

           /*     startActivity(new Intent(context, AutomaticMediaDownloadActivity.class).putExtra(AppConstants.IS_MUTE_NOTIFICATION, isMuteNotification).
                        putExtra(AppConstants.IS_SHOW_AWATER, String.valueOf(isShowAwater)).putExtra(AppConstants.IS_PRIVATE_PROFILE, String.valueOf(isPrivateProfile)).
                        putExtra(AppConstants.IS_ALLOW_COMMENT, String.valueOf(isAllowComment)).putExtra(AppConstants.IS_ALLOW_SHARING, String.valueOf(isAllowSharing)).
                        putExtra(AppConstants.IS_SAVE_GALLERY, String.valueOf(isSaveGallery)).putExtra(AppConstants.LAST_SEEN, String.valueOf(lastSeen)).
                        putExtra(AppConstants.TAG_POST, String.valueOf(tagPost)).putExtra(AppConstants.IS_CHAT_WALLPAPER, String.valueOf(isChatWallpaper)).
                        putExtra(AppConstants.PROFILE_PHOTO, String.valueOf(profilePhoto)).putExtra(AppConstants.IS_MEDIA_DOWNLOAD_PRIVATE, String.valueOf(isMediaDownloadPrivate)).
                        putExtra(AppConstants.IS_MEDIA_DOWNLOAD_GROUP, String.valueOf(isMediaDownloadGroup)));
       */         break;

            case R.id.tv_delete_all_chats:
                dialogDeleteChats();
                break;

            case R.id.tv_blocked:
                ActivityController.startActivity(context, Blocked.class);
                break;

            case R.id.tv_report_problem:
                ActivityController.startActivity(context, ReportProblemActivity.class);
                break;
            case R.id.tvMute:
                setPostShareDialoge();

            default:
                break;
        }
    }

    private void setPostShareDialoge() {


        final CharSequence[] items = {getString(R.string.eight_hrs), getString(R.string.one_week), getString(R.string.one_month),
                getString(R.string.one_year)};

        new MaterialDialog.Builder(context)
                .title(R.string.mute_notification_d).titleGravity(GravityEnum.CENTER)
                .items(items)
                .itemsColor(ContextCompat.getColor(context, themes.setDarkThemeText()))
                .backgroundColor(ContextCompat.getColor(context, themes.setDarkThemeStory()))
                .titleColor(ContextCompat.getColor(context, themes.setDarkThemeText()))
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {

                        if (items[position].equals(getString(R.string.eight_hrs))) {
                            ToastUtils.showToastShort(context, "Muted for 8hr");


                        } else if (items[position].equals(getString(R.string.one_week))) {
                            ToastUtils.showToastShort(context, "Muted for one week");

                        } else if (items[position].equals(getString(R.string.one_month))) {
                            ToastUtils.showToastShort(context, "Muted for one month");

                        } else if (items[position].equals(getString(R.string.one_year))) {
                            ToastUtils.showToastShort(context, "Muted for one year");

                        }

                    }
                }).negativeText(R.string.cancel).onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        }).show();

    }
    private void onSwitchSelectedSettings() {
        LayoutswitchaccountBinding layoutswitchaccountBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layoutswitchaccount, null, false);

        final Dialog dialog = DialogUtils.createDialog(context, layoutswitchaccountBinding.getRoot());
        layoutswitchaccountBinding.llMain.setBackground(ContextCompat.getDrawable(context, Themes.getInstance(context).setDarkStoryBackground()));
        layoutswitchaccountBinding.tvName1.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        layoutswitchaccountBinding.tvName2.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        layoutswitchaccountBinding.tvSwitchAccountss.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        layoutswitchaccountBinding.vieWLine.setBackgroundColor(ContextCompat.getColor(context,themes.setViewLineGrey()));
        layoutswitchaccountBinding.viewLine2.setBackgroundColor(ContextCompat.getColor(context,themes.setViewLineGrey()));
        layoutswitchaccountBinding.viewLine3.setBackgroundColor(ContextCompat.getColor(context,themes.setViewLineGrey()));
        layoutswitchaccountBinding.tvSwitchAccountss.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        layoutswitchaccountBinding.tvSwitchAccountssCancel.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setCancel()));

        layoutswitchaccountBinding.tvSwitchAccountssCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        layoutswitchaccountBinding.tvSwitchAccountssID1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        layoutswitchaccountBinding.tvSwitchAccountssID2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
    }




/*
    private void onSwitchSelectedSettings() {
        LayoutswitchaccountBinding layoutswitchaccountBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layoutswitchaccount, null, false);
        final ArrayList<UserModel> modelList = new ArrayList<>();
        UserTable table = new UserTable(SettingsActivity.this);
        modelList.addAll(table.getAllSavedUsers());
        if (modelList != null && modelList.size() > 0) {
            for (int i = 0; i < modelList.size(); i++) {
                if (PrefManager.getInstance(context).getUserId().equalsIgnoreCase(modelList.get(i).getId())) {
                    modelList.remove(i);
                    break;
                }
            }
        }
        table.closeDB();
        if (modelList != null && modelList.size() > 0) {
            final Dialog dialog = DialogUtils.createDialog(SettingsActivity.this, layoutswitchaccountBinding.getRoot());
            layoutswitchaccountBinding.rvSavedUsers.setLayoutManager(new LinearLayoutManager(SettingsActivity.this, LinearLayoutManager.VERTICAL, false));
            SavedUserAdapter adapter = new SavedUserAdapter(SettingsActivity.this, modelList, new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    PrefManager prefManager = new PrefManager(SettingsActivity.this);
                    prefManager.setUserId(modelList.get(position).getId());
                    prefManager.setQBid(modelList.get(position).getQb_id());
                    prefManager.saveUser(modelList.get(position));
                    prefManager.setUserProfilePic(modelList.get(position).getImage());
                    prefManager.setKeyAccessToken(modelList.get(position).getAccess_token());
                    Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
                    startActivity(intent);
                    dialog.dismiss();
                }
            });
            layoutswitchaccountBinding.rvSavedUsers.setAdapter(adapter);
            layoutswitchaccountBinding.tvSwitchAccountssCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
    }
*/
/*

    private void dialogTaggingPost() {

        final CharSequence[] items = {getString(R.string.everyone), getString(R.string.my_contact), getString(R.string.nobody), getString(R.string.following)};
        new MaterialDialog.Builder(context)
                .title(R.string.tagging).titleGravity(GravityEnum.CENTER)
                .items(items)
                .itemsColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()))
                .backgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeStory()))
                .titleColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()))
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        if (items[position].equals(getString(R.string.everyone))) {
                            if (CommonUtils.isOnline(context)) {
                                saveSettingApi(position, lastSeen, profilePhoto, isMediaDownloadPrivate, isMediaDownloadGroup, isShowAwater, isPrivateProfile, isAllowComment,
                                        isAllowSharing, isSaveGallery, isChatWallpaper);
                            } else {
                                ToastUtils.showToastShort(context, getString(R.string.internet_alert_msg));
                            }
                            binding.tvTaggingPostValue.setText(R.string.everyone);
                        } else if (items[position].equals(getString(R.string.my_contact))) {
                            if (CommonUtils.isOnline(context)) {
                                saveSettingApi(position, lastSeen, profilePhoto, isMediaDownloadPrivate, isMediaDownloadGroup, isShowAwater, isPrivateProfile, isAllowComment,
                                        isAllowSharing, isSaveGallery, isChatWallpaper);
                            } else {
                                ToastUtils.showToastShort(context, getString(R.string.internet_alert_msg));
                            }
                            binding.tvTaggingPostValue.setText(R.string.my_contact);
                        } else if (items[position].equals(getString(R.string.following))) {
                            if (CommonUtils.isOnline(context)) {
                                saveSettingApi(position, lastSeen, profilePhoto, isMediaDownloadPrivate, isMediaDownloadGroup, isShowAwater, isPrivateProfile, isAllowComment,
                                        isAllowSharing, isSaveGallery, isChatWallpaper);
                            } else {
                                ToastUtils.showToastShort(context, getString(R.string.internet_alert_msg));
                            }
                            binding.tvTaggingPostValue.setText(R.string.following);
                        } else if (items[position].equals(getString(R.string.nobody))) {
                            if (CommonUtils.isOnline(context)) {
                                saveSettingApi(position, lastSeen, profilePhoto, isMediaDownloadPrivate, isMediaDownloadGroup, isShowAwater, isPrivateProfile, isAllowComment,
                                        isAllowSharing, isSaveGallery, isChatWallpaper);
                            } else {
                                ToastUtils.showToastShort(context, getString(R.string.internet_alert_msg));
                            }
                            binding.tvTaggingPostValue.setText(R.string.nobody);
                        } else
                            dialog.dismiss();
                    }
                }).negativeText(R.string.cancel).onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        }).show();

    }
*/



    private void dialogTaggingPost() {



        final CharSequence[] items = {getString(R.string.everyone), getString(R.string.following_dialog), getString(R.string.nobody)};
        new MaterialDialog.Builder(context)
                .title(R.string.tagging).titleGravity(GravityEnum.CENTER)
                .items(items)
                .itemsColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()))
                .backgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeStory()))
                .titleColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()))

                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {

                        if (items[position].equals(getString(R.string.everyone)))
                            binding.tvTaggingPostValue.setText(R.string.everyone);
                        else if (items[position].equals(getString(R.string.following_dialog)))
                            binding.tvTaggingPostValue.setText(R.string.following_dialog);
                        else if (items[position].equals(getString(R.string.nobody)))
                            binding.tvTaggingPostValue.setText(R.string.nobody);
                        else
                            dialog.dismiss();


                    }
                }).negativeText(R.string.cancel).onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        }).show();




    }




    private void dialogPhotoPermission() {


        final CharSequence[] items = {getString(R.string.my_contacts), getString(R.string.everyone), getString(R.string.nobody)};
        new MaterialDialog.Builder(context)
                .title(R.string.profile_photo).titleGravity(GravityEnum.CENTER)
                .items(items)
                .backgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkChatBox()))
                .titleColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()))
                .itemsColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkGreyTextColor()))

                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {

                        if (items[position].equals(getString(R.string.everyone)))
                            binding.tvProfilePhotoValue.setText(R.string.everyone);
                        else if (items[position].equals(getString(R.string.following_dialog)))
                            binding.tvProfilePhotoValue.setText(R.string.following_dialog);
                        else if (items[position].equals(getString(R.string.nobody)))
                            binding.tvProfilePhotoValue.setText(R.string.nobody);
                        else
                            dialog.dismiss();


                    }
                }).negativeText(R.string.cancel).onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        }).show();


    }








   /*



    private void dialogPhotoPermission() {

        final CharSequence[] items = {getString(R.string.everyone), getString(R.string.my_contact), getString(R.string.nobody), getString(R.string.following)};
        new MaterialDialog.Builder(context)
                .title(R.string.profile_photo).titleGravity(GravityEnum.CENTER)
                .items(items)
                .backgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkChatBox()))
                .titleColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()))
                .itemsColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkGreyTextColor()))
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        if (items[position].equals(getString(R.string.everyone))) {
                            if (CommonUtils.isOnline(context)) {
                                saveSettingApi(tagPost, lastSeen, position, isMediaDownloadPrivate, isMediaDownloadGroup, isShowAwater, isPrivateProfile, isAllowComment,
                                        isAllowSharing, isSaveGallery, isChatWallpaper);
                            } else {
                                ToastUtils.showToastShort(context, getString(R.string.internet_alert_msg));
                            }
                            binding.tvProfilePhotoValue.setText(R.string.everyone);
                        } else if (items[position].equals(getString(R.string.my_contact))) {
                            if (CommonUtils.isOnline(context)) {
                                saveSettingApi(position, lastSeen, profilePhoto, isMediaDownloadPrivate, isMediaDownloadGroup, isShowAwater, isPrivateProfile, isAllowComment,
                                        isAllowSharing, isSaveGallery, isChatWallpaper);
                            } else {
                                ToastUtils.showToastShort(context, getString(R.string.internet_alert_msg));
                            }
                            binding.tvTaggingPostValue.setText(R.string.my_contact);
                        } else if (items[position].equals(getString(R.string.nobody))) {
                            if (CommonUtils.isOnline(context)) {
                                saveSettingApi(tagPost, lastSeen, position, isMediaDownloadPrivate, isMediaDownloadGroup, isShowAwater, isPrivateProfile, isAllowComment,
                                        isAllowSharing, isSaveGallery, isChatWallpaper);
                            } else {
                                ToastUtils.showToastShort(context, getString(R.string.internet_alert_msg));
                            }
                            binding.tvProfilePhotoValue.setText(R.string.nobody);
                        } else if (items[position].equals(getString(R.string.following))) {
                            if (CommonUtils.isOnline(context)) {
                                saveSettingApi(tagPost, lastSeen, position, isMediaDownloadPrivate, isMediaDownloadGroup, isShowAwater, isPrivateProfile, isAllowComment,
                                        isAllowSharing, isSaveGallery, isChatWallpaper);
                            } else {
                                ToastUtils.showToastShort(context, getString(R.string.internet_alert_msg));
                            }
                            binding.tvProfilePhotoValue.setText(R.string.following);
                        } else {
                            dialog.dismiss();
                        }


                    }
                }).negativeText(R.string.cancel).onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        }).show();
    }*/


  /*  private void dialogChatWallpaper() {

        final CharSequence[] items = {getString(R.string.default_wallpaper), getString(R.string.solid_color), getString(R.string.themes), getString(R.string.gallery), getString(R.string.no_wallpaper)};
        AlertDialog.Builder builder;
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {

            builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom));
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    RoomsTable roomsTable = new RoomsTable(context);
                    roomsTable.getAllRooms(context);
                    if (roomsTable.getAllRooms(context) != null && roomsTable.getAllRooms(context).size() > 0) {
                        RoomsBackgroundTable table = new RoomsBackgroundTable(context);
                        for (int i = 0; i < roomsTable.getAllRooms(context).size(); i++) {
                            WallpaperModel wallpaperModel = new WallpaperModel();
                            wallpaperModel.setType("color");
                            wallpaperModel.setColor_code("#f5fffefe");
                            wallpaperModel.setImage("");
                            if (!table.isExist(roomsTable.getAllRooms(context).get(i).getRoom_id())) {
                                table.saveBackground(wallpaperModel, roomsTable.getAllRooms(context).get(i).getRoom_id());
                            } else {
                                table.updateBackGround(wallpaperModel, roomsTable.getAllRooms(context).get(i).getRoom_id());
                            }
                        }
                        table.closeDB();
                    }
                    roomsTable.closeDB();
                    ToastUtils.showToastShort(context, "Default wallpaper applied");
                } else if (item == 1) {
                    Intent intent = new Intent(context, SolidColorSelectionActivity.class);
                    intent.putExtra(AppConstants.ROOM_ID, "");
                    intent.putExtra(AppConstants.FROM, "multiple");
                    startActivity(intent);
                } else if (item == 2) {
                    Intent intent = new Intent(context, SolidColorSelectionActivity.class);
                    intent.putExtra(AppConstants.ROOM_ID, "");
                    intent.putExtra(AppConstants.FROM, "multiple");
                    startActivity(intent);
                } else if (item == 3) {
                    image = System.currentTimeMillis() + "_photo.png";
                    openGallery();
                } else if (item == 4) {
                    ToastUtils.showToastShort(context, "Wallpaper Removed");
                    RoomsTable roomsTable = new RoomsTable(context);
                    roomsTable.getAllRooms(context);
                    if (roomsTable.getAllRooms(context) != null && roomsTable.getAllRooms(context).size() > 0) {
                        RoomsBackgroundTable table = new RoomsBackgroundTable(context);
                        for (int i = 0; i < roomsTable.getAllRooms(context).size(); i++) {
                            WallpaperModel wallpaperModel = new WallpaperModel();
                            wallpaperModel.setType("color");
                            wallpaperModel.setColor_code("#f5fffefe");
                            wallpaperModel.setImage("");
                            if (!table.isExist(roomsTable.getAllRooms(context).get(i).getRoom_id())) {
                                table.saveBackground(wallpaperModel, roomsTable.getAllRooms(context).get(i).getRoom_id());
                            } else {
                                table.updateBackGround(wallpaperModel, roomsTable.getAllRooms(context).get(i).getRoom_id());
                            }
                            table.closeDB();
                        }
                        table.closeDB();
                    }
                    roomsTable.closeDB();
                } else {
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
*/



    private void dialogChatWallpaper() {

        final CharSequence[] items = {getString(R.string.default_wallpaper), getString(R.string.solid_color), getString(R.string.themes), getString(R.string.gallery), getString(R.string.no_wallpaper)};
      //  android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        AlertDialog.Builder builder;

        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {

            builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom));
        } else {
            builder = new AlertDialog.Builder(context);
        } builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    ToastUtils.showToastShort(context, "Default wallpaper applied");
                } else if (item == 1) {
                    ActivityController.startActivity(context, SolidColorSelectionActivity.class);
                } else if (item == 2) {
                    ActivityController.startActivity(context, ThemeSelectionActivity.class);
                } else if (item == 3) {
                    openGallery();
                } else if (item == 4) {
                    ToastUtils.showToastShort(context, "Wallpaper Removed");
                } else {
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




   /* @Override
    protected void onResume() {
        super.onResume();
        if (CommonUtils.isOnline(context)) {
            getSettingApi();
        } else {
            ToastUtils.showToastShort(context, getString(R.string.internet_alert_msg));
        }
    }*/





    private void dialogDeleteChats() {

        new MaterialDialog.Builder(context)
                .title(R.string.sure_to_delete)
                .titleGravity(GravityEnum.CENTER)

        .itemsColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()))
                    .backgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeStory()))
                    .titleColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()))
                    .neutralText(R.string.delete_all_chats).onNeutral(new MaterialDialog.SingleButtonCallback() {

            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                ToastUtils.showToastShort(context, "Chats Deleted!");
                dialog.dismiss();
            }
        })
                .negativeText(R.string.cancel).onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        }).show();


    }



 /*
    private void dialogDeleteChats() {


        new MaterialDialog.Builder(context)
                .title(R.string.sure_to_delete)
                .titleGravity(GravityEnum.CENTER)
                .itemsColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()))
                .backgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeStory()))
                .titleColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()))
                .neutralText(R.string.delete_all_chats).onNeutral(new MaterialDialog.SingleButtonCallback() {

            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                ToastUtils.showToastShort(context, "Chats Deleted!");
                dialog.dismiss();
            }
        })
                .negativeText(R.string.cancel).onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        }).show();
    }*/

    public void openGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, TakePictureUtils.PICK_GALLERY);
    }

    /*private void saveSettingApi(final int tag_post, int last_seen, int profile_photo, boolean media_download_private, boolean media_download_group,
                                boolean show_avatar, boolean private_profile, boolean allow_comment, boolean allow_sharing, boolean save_gallery,
                                boolean chat_wallpaper) {
        final Dialog progressDialog = DialogUtils.customProgressDialog(context);
        progressDialog.show();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("tag_post", tag_post);
        jsonObject.addProperty("last_seen", last_seen);
        jsonObject.addProperty("profile_photo", profile_photo);
        jsonObject.addProperty("media_download_private", media_download_private);
        jsonObject.addProperty("media_download_group", media_download_group);
        jsonObject.addProperty("show_avatar", show_avatar);
        jsonObject.addProperty("private_profile", private_profile);
        jsonObject.addProperty("allow_comment", allow_comment);
        jsonObject.addProperty("allow_sharing", allow_sharing);
        jsonObject.addProperty("save_gallery", save_gallery);
        jsonObject.addProperty("chat_wallpaper", chat_wallpaper);

        retrofit2.Call<ApiResponse> call = ApiExecutor.getClient(context).saveSettingApi(jsonObject);
        call.enqueue(new retrofit2.Callback<ApiResponse>() {

            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (response.body() != null && response.body().getResponseCode() == 200) {
                    if (response.body().getResponseMessage() != null) {
                        CommonUtils.showToast(context, response.body().getResponseMessage());
                    }
                    isMuteNotification = response.body().getSetting().isMute_notification();
                    isShowAwater = response.body().getSetting().isShow_avatar();
                    isPrivateProfile = response.body().getSetting().isPrivate_profile();
                    isAllowComment = response.body().getSetting().isAllow_comment();
                    isAllowSharing = response.body().getSetting().isAllow_sharing();
                    isSaveGallery = response.body().getSetting().isSave_gallery();
                    lastSeen = response.body().getSetting().getLast_seen();
                    tagPost = response.body().getSetting().getTag_post();
                    isChatWallpaper = response.body().getSetting().isChat_wallpaper();
                    profilePhoto = response.body().getSetting().getProfile_photo();
                    isMediaDownloadPrivate = response.body().getSetting().isMedia_download_private();
                    isMediaDownloadGroup = response.body().getSetting().isMedia_download_group();

                    if (tagPost == 0) {
                        binding.tvTaggingPostValue.setText(R.string.everyone);
                    } else if (tagPost == 1) {
                        binding.tvTaggingPostValue.setText(R.string.my_contact);
                    } else if (tagPost == 2) {
                        binding.tvTaggingPostValue.setText(R.string.nobody);
                    } else if (tagPost == 3) {
                        binding.tvTaggingPostValue.setText(R.string.following);
                    }


                    if (isShowAwater) {
                        binding.switchShowAvatarOnProfile.setChecked(true);
                    } else {
                        binding.switchShowAvatarOnProfile.setChecked(false);
                    }

                    if (isPrivateProfile) {
                        binding.switchPrivateProfile.setChecked(true);
                    } else {
                        binding.switchPrivateProfile.setChecked(false);
                    }

                    if (isAllowComment) {
                        binding.switchAllowComments.setChecked(true);
                    } else {
                        binding.switchAllowComments.setChecked(false);
                    }

                    if (isAllowSharing) {
                        binding.switchAllowSharing.setChecked(true);
                    } else {
                        binding.switchAllowSharing.setChecked(false);
                    }

                    if (isSaveGallery) {
                        binding.switchSaveToGallery.setChecked(true);
                    } else {
                        binding.switchSaveToGallery.setChecked(false);
                    }

                    if (lastSeen == 0) {
                        binding.lastSeenValue.setText(R.string.everyone);
                    } else if (lastSeen == 1) {
                        binding.lastSeenValue.setText(R.string.my_contact);
                    } else if (lastSeen == 2) {
                        binding.lastSeenValue.setText(R.string.nobody);
                    } else if (lastSeen == 3) {
                        binding.lastSeenValue.setText(R.string.following);
                    }

                    if (profilePhoto == 0) {
                        binding.tvProfilePhotoValue.setText(R.string.everyone);
                    } else if (profilePhoto == 1) {
                        binding.tvProfilePhotoValue.setText(R.string.my_contact);
                    } else if (profilePhoto == 2) {
                        binding.tvProfilePhotoValue.setText(R.string.nobody);
                    } else if (profilePhoto == 3) {
                        binding.tvProfilePhotoValue.setText(R.string.following);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                t.printStackTrace();
            }
        });
    }


    private void getSettingApi() {
        final Dialog progressDialog = DialogUtils.customProgressDialog(context);
        progressDialog.show();

        JsonObject jsonObject = new JsonObject();

        retrofit2.Call<ApiResponse> call = ApiExecutor.getClient(context).saveSettingApi(jsonObject);
        call.enqueue(new retrofit2.Callback<ApiResponse>() {

            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (response.body() != null && response.body().getResponseCode() == 200) {
                    isMuteNotification = response.body().getSetting().isMute_notification();
                    isShowAwater = response.body().getSetting().isShow_avatar();
                    isPrivateProfile = response.body().getSetting().isPrivate_profile();
                    isAllowComment = response.body().getSetting().isAllow_comment();
                    isAllowSharing = response.body().getSetting().isAllow_sharing();
                    isSaveGallery = response.body().getSetting().isSave_gallery();
                    lastSeen = response.body().getSetting().getLast_seen();
                    tagPost = response.body().getSetting().getTag_post();
                    isChatWallpaper = response.body().getSetting().isChat_wallpaper();
                    profilePhoto = response.body().getSetting().getProfile_photo();
                    isMediaDownloadPrivate = response.body().getSetting().isMedia_download_private();
                    isMediaDownloadGroup = response.body().getSetting().isMedia_download_group();

                    if (tagPost == 0) {
                        binding.tvTaggingPostValue.setText(R.string.everyone);
                    } else if (tagPost == 1) {
                        binding.tvTaggingPostValue.setText(R.string.my_contact);
                    } else if (tagPost == 2) {
                        binding.tvTaggingPostValue.setText(R.string.nobody);
                    } else if (tagPost == 3) {
                        binding.tvTaggingPostValue.setText(R.string.following);
                    }


                    if (isShowAwater) {
                        binding.switchShowAvatarOnProfile.setChecked(true);
                    } else {
                        binding.switchShowAvatarOnProfile.setChecked(false);
                    }

                    if (isPrivateProfile) {
                        binding.switchPrivateProfile.setChecked(true);
                    } else {
                        binding.switchPrivateProfile.setChecked(false);
                    }

                    if (isAllowComment) {
                        binding.switchAllowComments.setChecked(true);
                    } else {
                        binding.switchAllowComments.setChecked(false);
                    }

                    if (isAllowSharing) {
                        binding.switchAllowSharing.setChecked(true);
                    } else {
                        binding.switchAllowSharing.setChecked(false);
                    }

                    if (isSaveGallery) {
                        binding.switchSaveToGallery.setChecked(true);
                    } else {
                        binding.switchSaveToGallery.setChecked(false);
                    }

                    if (lastSeen == 0) {
                        binding.lastSeenValue.setText(R.string.everyone);
                    } else if (lastSeen == 1) {
                        binding.lastSeenValue.setText(R.string.my_contact);
                    } else if (lastSeen == 2) {
                        binding.lastSeenValue.setText(R.string.nobody);
                    } else if (lastSeen == 3) {
                        binding.lastSeenValue.setText(R.string.following);
                    }

                    if (profilePhoto == 0) {
                        binding.tvProfilePhotoValue.setText(R.string.everyone);
                    } else if (profilePhoto == 1) {
                        binding.tvProfilePhotoValue.setText(R.string.my_contact);
                    } else if (profilePhoto == 2) {
                        binding.tvProfilePhotoValue.setText(R.string.nobody);
                    } else if (profilePhoto == 3) {
                        binding.tvProfilePhotoValue.setText(R.string.following);
                    }

                    isFirstTime = false;
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                t.printStackTrace();
            }
        });
    }
*/
    /**
     * this method is used for open crop image
     */
    public void startCropImage(Activity context, String fileName) {
        Intent intent = new Intent(context, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, new File(context.getExternalFilesDir("temp"), fileName).getPath());
        intent.putExtra(CropImage.SCALE, true);
        intent.putExtra(CropImage.ASPECT_X, 1);
        intent.putExtra(CropImage.ASPECT_Y, 1);
        intent.putExtra(CropImage.OUTPUT_X, 600);
        intent.putExtra(CropImage.OUTPUT_Y, 600);
        startActivityForResult(intent, CROP_FROM_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == TakePictureUtils.PICK_GALLERY) {
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        InputStream inputStream = context.getContentResolver().openInputStream(data.getData());
                        FileOutputStream fileOutputStream = new FileOutputStream(new File(this.context.getExternalFilesDir("temp"), image + ".jpg"));
                        TakePictureUtils.copyStream(inputStream, fileOutputStream);
                        fileOutputStream.close();
                        inputStream.close();
                        startCropImage(this, image + ".jpg");
                    } catch (Exception e) {
                        //CommonUtils.showToast(mContext, getString(R.string.error_in_picture));

                    }
                }
            } else if (requestCode == TakePictureUtils.TAKE_PICTURE) {
                if (resultCode == Activity.RESULT_OK) {
                    startCropImage(this, image + ".jpg");
                }
            } else if (requestCode == CROP_FROM_CAMERA) {
                //  imageName="picture";
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        String path = data.getStringExtra(CropImage.IMAGE_PATH);
                        RoomsTable roomsTable = new RoomsTable(context);
                        roomsTable.getAllRooms(context);
                        if (roomsTable.getAllRooms(context) != null && roomsTable.getAllRooms(context).size() > 0) {
                            RoomsBackgroundTable table = new RoomsBackgroundTable(context);
                            for (int i = 0; i < roomsTable.getAllRooms(context).size(); i++) {
                                WallpaperModel wallpaperModel = new WallpaperModel();
                                wallpaperModel.setColor_code("");
                                wallpaperModel.setType("image");
                                wallpaperModel.setImage(path);
                                if (!table.isExist(roomsTable.getAllRooms(context).get(i).getRoom_id())) {
                                    table.saveBackground(wallpaperModel, roomsTable.getAllRooms(context).get(i).getRoom_id());
                                } else {
                                    table.updateBackGround(wallpaperModel, roomsTable.getAllRooms(context).get(i).getRoom_id());
                                }
                            }
                            table.closeDB();
                            ToastUtils.showToastShort(context, "Wallpaper changed successfully.");
                        }
                        roomsTable.closeDB();
                    }
                }
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        if (b) {
            if (compoundButton.getId() == R.id.rbOn) {
                binding.rbOff.setChecked(false);
                binding.rbWifi.setChecked(false);
            }
            if (compoundButton.getId() == R.id.rbOff) {
                binding.rbOn.setChecked(false);
                binding.rbWifi.setChecked(false);
            }
            if (compoundButton.getId() == R.id.rbWifi) {
                binding.rbOn.setChecked(false);
                binding.rbOff.setChecked(false);
            }
        }
    }

}

