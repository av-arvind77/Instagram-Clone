package com.yellowseed.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.yellowseed.R;
import com.yellowseed.databinding.ActivityAvtarStyleSocialShareBinding;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.PrefManager;

public class AvtarStyleSocialShareActivity extends BaseActivity {


    private ActivityAvtarStyleSocialShareBinding binding;
    private Context context;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_avtar_style_social_share);
        context = AvtarStyleSocialShareActivity.this;

        initializedControl();
        setToolBar();
        setOnClickListener();

    }

    @Override
    public void initializedControl() {

    }

    @Override
    public void setToolBar() {

        binding.toolbarAvtarSocial.ivLeft.setVisibility(View.VISIBLE);
        binding.toolbarAvtarSocial.tvHeader.setVisibility(View.VISIBLE);
        binding.toolbarAvtarSocial.tvHeader.setText(R.string.avtarstyle);
        binding.toolbarAvtarSocial.tvRighttext.setVisibility(View.GONE);


    }

    @Override
    public void setOnClickListener() {

        binding.btnShareOnFacebook.setOnClickListener(this);
       binding.tvCancelAvtarSocial.setOnClickListener(this);
       binding.toolbarAvtarSocial.ivLeft.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnShareOnFacebook:

            /*    SharePhoto photo = new SharePhoto.Builder()
                        .setBitmap(CommonUtils.getImageBitmap(binding.ivAvtarImage,context))
                        .build();
                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(photo)
                        .build();
                ShareDialog shareDialog = new ShareDialog(AvtarStyleSocialShareActivity.this);
                shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);*/

                break;
            case R.id.tvCancelAvtarSocial:
                if (AppConstants.clickAvatarSkip) {
                    Intent intent = new Intent(context, EditProfileActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {

                    Intent intent = new Intent(context, HomeActivity.class);

                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finishAffinity();
                }
                break;
            case R.id.ivLeft:
                finish();
                default:
                    break;

        }

    }
}
