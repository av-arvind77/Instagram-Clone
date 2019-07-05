package com.yellowseed.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.yellowseed.R;
import com.yellowseed.adapter.MediaClickAdapter;
import com.yellowseed.databinding.ActivityMediaClickViewBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.MediaClickModel;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MediaClickViewActivity extends BaseActivity {

    private ActivityMediaClickViewBinding binding;
    private Context context;
    private MediaClickAdapter adapter;
    private List<MediaClickModel> arImageList = new ArrayList<>();
    private int img[] = {R.mipmap.background_first, R.mipmap.background_second, R.mipmap.background_first, R.mipmap.background_second, R.mipmap.background_first, R.mipmap.background_first, R.mipmap.background_second, R.mipmap.background_first, R.mipmap.background_second, R.mipmap.background_first};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_media_click_view);
        context = MediaClickViewActivity.this;
        initializedControl();
        setOnClickListener();
    }

    @Override
    public void initializedControl() {
        setCurrentTheme();
        setRecyclerView();

    }

    private void setRecyclerView() {
        binding.rvImage.setHasFixedSize(true);
        arImageList.addAll(mediaData());
        binding.rvImage.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        adapter = new MediaClickAdapter(context, arImageList, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.ivMediaImage:
                        CommonUtils.showToast(context, "image clicked");
                }

            }
        });
        binding.rvImage.setAdapter(adapter);


    }


    private List<MediaClickModel> mediaData() {
        List<MediaClickModel> mediaClickModels = new ArrayList<>();
        for (int i = 0; i < img.length; i++) {
            MediaClickModel mediaModel = new MediaClickModel();
            mediaModel.setImg(img[i]);
            mediaClickModels.add(mediaModel);
        }
        return mediaClickModels;
    }

    private void setCurrentTheme() {

        Themes.getInstance(context).changeIconColor(context, binding.ivBack);
        binding.tvName.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.tvAllMedia.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setTolbarText()));
        binding.rlToolbar.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        binding.rlMain.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        binding.llBottom.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        binding.tvShare.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.tvStar.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.tvReply.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.tvCopy.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.tvForward.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        Themes.getInstance(context).changeIconColorToWhite(context, binding.ivStared);
        Themes.getInstance(context).changeIconColorToWhite(context, binding.ivBackward);
        Themes.getInstance(context).changeIconColorToWhite(context, binding.ivCopy);
        Themes.getInstance(context).changeIconColorToWhite(context, binding.ivForward);
        Themes.getInstance(context).changeShareIcon(context, binding.ivShare);
    }

    @Override
    public void setToolBar() {

    }

    @Override
    public void setOnClickListener() {

        binding.ivBackward.setOnClickListener(this);
        binding.ivForward.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);

        binding.ivCopy.setOnClickListener(this);
        binding.ivDelete.setOnClickListener(this);
        binding.ivShare.setOnClickListener(this);
        binding.ivStared.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ivBack:
                onBackPressed();
                break;


            case R.id.ivStarred:
                ToastUtils.showToastShort(context, "Work in progress.");
                break;

            case R.id.ivShare:
                ToastUtils.showToastShort(context, "Work in progress.");

                //  shareImage();
                break;
            case R.id.ivDelete:
                ToastUtils.showToastShort(context, "Work in progress.");
                break;
            case R.id.ivCopy:
                ToastUtils.showToastShort(context, "Work in progress.");
                break;

            case R.id.ivBackward:
                ToastUtils.showToastShort(context, "Work in progress.");
                break;
            default:
                break;
        }

    }

    // Method to share any image.
    private void shareImage() {
        Intent share = new Intent(Intent.ACTION_SEND);

        // If you want to share a png image only, you can do:
        // setType("image/png"); OR for jpeg: setType("image/jpeg");
        share.setType("image/*");

        // Make sure you put example png image named myImage.png in your
        // directory
        String imagePath = Environment.getExternalStorageDirectory()
                + "/myImage.png";

        File imageFileToShare = new File(imagePath);

        Uri uri = Uri.fromFile(imageFileToShare);
        share.putExtra(Intent.EXTRA_STREAM, uri);

        startActivity(Intent.createChooser(share, "Share Image!"));
    }
}
