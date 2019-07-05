package com.yellowseed.activity;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;

import com.google.gson.JsonObject;
import com.yellowseed.R;
import com.yellowseed.databinding.ActivityAutomaticMediaDownloadBinding;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;
import com.yellowseed.webservices.ApiResponse;

import retrofit2.Call;
import retrofit2.Response;

public class AutomaticMediaDownloadActivity extends BaseActivity {
    private ActivityAutomaticMediaDownloadBinding binding;
    private Context context;
    private boolean isMuteNotification, isShowAwater, isPrivateProfile, isAllowComment, isAllowSharing, isSaveGallery, isChatWallpaper, isMediaDownloadPrivate,
            isMediaDownladGroup;
    private int lastSeen, tagPost, profilePhoto;
    private Themes themes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_automatic_media_download);
        context = AutomaticMediaDownloadActivity.this;
        themes = new Themes(context);

     //   getIntentValue();
        initializedControl();
        setToolBar();
        setOnClickListener();
    }


    private void getIntentValue() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            if (getIntent().getExtras().get(AppConstants.IS_MUTE_NOTIFICATION) != null) {
                isMuteNotification = Boolean.parseBoolean(getIntent().getExtras().get(AppConstants.IS_MUTE_NOTIFICATION).toString());
            }
            if (getIntent().getExtras().get(AppConstants.IS_SHOW_AWATER) != null) {
                isShowAwater = Boolean.parseBoolean(getIntent().getExtras().get(AppConstants.IS_SHOW_AWATER).toString());
            }
            if (getIntent().getExtras().get(AppConstants.IS_PRIVATE_PROFILE) != null) {
                isPrivateProfile = Boolean.parseBoolean(getIntent().getExtras().get(AppConstants.IS_PRIVATE_PROFILE).toString());
            }
            if (getIntent().getExtras().get(AppConstants.IS_ALLOW_COMMENT) != null) {
                isAllowComment = Boolean.parseBoolean(getIntent().getExtras().get(AppConstants.IS_ALLOW_COMMENT).toString());
            }
            if (getIntent().getExtras().get(AppConstants.IS_ALLOW_SHARING) != null) {
                isAllowSharing = Boolean.parseBoolean(getIntent().getExtras().get(AppConstants.IS_ALLOW_SHARING).toString());
            }
            if (getIntent().getExtras().get(AppConstants.IS_SAVE_GALLERY) != null) {
                isSaveGallery = Boolean.parseBoolean(getIntent().getExtras().get(AppConstants.IS_SAVE_GALLERY).toString());
            }
            if (getIntent().getExtras().get(AppConstants.IS_MEDIA_DOWNLOAD_PRIVATE) != null) {
                isMediaDownloadPrivate = Boolean.parseBoolean(getIntent().getStringExtra(AppConstants.IS_MEDIA_DOWNLOAD_PRIVATE).toString());
            }
            if (getIntent().getExtras().get(AppConstants.IS_MEDIA_DOWNLOAD_GROUP) != null) {
                isMediaDownladGroup = Boolean.parseBoolean(getIntent().getStringExtra(AppConstants.IS_MEDIA_DOWNLOAD_GROUP).toString());
            }
            if (getIntent().getExtras().get(AppConstants.IS_CHAT_WALLPAPER) != null) {
                isChatWallpaper = Boolean.parseBoolean(getIntent().getStringExtra(AppConstants.IS_CHAT_WALLPAPER).toString());
            }
            if (getIntent().getExtras().get(AppConstants.LAST_SEEN) != null) {
                lastSeen = Integer.parseInt(getIntent().getExtras().get(AppConstants.LAST_SEEN).toString());
            }
            if (getIntent().getExtras().get(AppConstants.TAG_POST) != null) {
                tagPost = Integer.parseInt(getIntent().getExtras().get(AppConstants.TAG_POST).toString());
            }
            if (getIntent().getExtras().get(AppConstants.PROFILE_PHOTO) != null) {
                profilePhoto = Integer.parseInt(getIntent().getExtras().get(AppConstants.PROFILE_PHOTO).toString());
            }
        }
    }

    public void setCurrentTheme() {
        binding.toolbarAutoDownload.toolbarMain.setBackgroundColor(ContextCompat.getColor(context, themes.setDarkTheme()));
        binding.toolbarAutoDownload.tvHeader.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        themes.changeIconColor(context, binding.toolbarAutoDownload.ivLeft);
        binding.llMain.setBackgroundColor(ContextCompat.getColor(context, themes.setDarkTheme()));
        binding.tvMobileData.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.tvWifi.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.tvAudio.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.tvImages.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.tvVideo.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));

        binding.tvWifiAudio.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.tvWifiImages.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.tvWifiVideo.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));

        binding.viewLine.setBackgroundColor(ContextCompat.getColor(context,themes.setViewLineGrey()));
        binding.viewLine2.setBackgroundColor(ContextCompat.getColor(context,themes.setViewLineGrey()));


    }

    @Override
    public void initializedControl() {
        setCurrentTheme();




        if (isMediaDownloadPrivate) {
            binding.switchImages.setChecked(true);
        } else {
            binding.switchImages.setChecked(false);
        }

        if (isMediaDownladGroup) {
            binding.switchAudio.setChecked(true);
        } else {
            binding.switchAudio.setChecked(false);
        }

        if (isMediaDownladGroup) {
            binding.switchVideo.setChecked(true);
        } else {
            binding.switchVideo.setChecked(false);
        }

        if (isMediaDownladGroup) {
            binding.switchWifiImages.setChecked(true);
        } else {
            binding.switchWifiImages.setChecked(false);
        }

        if (isMediaDownladGroup) {
            binding.switchWifiAudio.setChecked(true);
        } else {
            binding.switchWifiAudio.setChecked(false);
        }

        if (isMediaDownladGroup) {
            binding.switchWifiVideo.setChecked(true);
        } else {
            binding.switchWifiVideo.setChecked(false);
        }


    }

    @Override
    public void setToolBar() {
        binding.toolbarAutoDownload.ivLeft.setVisibility(View.VISIBLE);
        binding.toolbarAutoDownload.tvHeader.setVisibility(View.VISIBLE);
        binding.toolbarAutoDownload.tvHeader.setText(R.string.automatic_media_download);
    }

    @Override
    public void setOnClickListener() {
        binding.toolbarAutoDownload.ivLeft.setOnClickListener(this);
       /* binding.switchGroups.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (CommonUtils.isOnline(context)) {
                    saveSettingApi(tagPost, lastSeen, profilePhoto, isMediaDownloadPrivate, isChecked, isShowAwater, isPrivateProfile, isAllowComment, isAllowSharing,
                            isSaveGallery, isChatWallpaper);
                } else {
                    ToastUtils.showToastShort(context, getString(R.string.internet_alert_msg));
                }
            }
        });*/

      /*  binding.switchPrivateChats.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (CommonUtils.isOnline(context)) {
                    saveSettingApi(tagPost, lastSeen, profilePhoto, isChecked, isMediaDownladGroup, isShowAwater, isPrivateProfile, isAllowComment, isAllowSharing,
                            isSaveGallery, isChatWallpaper);
                } else {
                    ToastUtils.showToastShort(context, getString(R.string.internet_alert_msg));
                }
            }
        });*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivLeft:
                onBackPressed();
                break;
            default:
                break;
        }
    }

   /* private void saveSettingApi(final int tag_post, int last_seen, int profile_photo, boolean media_download_private, boolean media_download_group,
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
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
