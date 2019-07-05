package com.yellowseed.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.google.gson.JsonObject;
import com.yellowseed.R;
import com.yellowseed.adapter.HomeBottomViewAdapter;
import com.yellowseed.databinding.ActivityImageBinding;
import com.yellowseed.imagezoom.ImageZoomHelper;
import com.yellowseed.model.resModel.GetRoomResonse;
import com.yellowseed.utils.ActivityController;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;
import com.yellowseed.webservices.ApiResponse;
import com.yellowseed.webservices.response.homepost.Image;
import com.yellowseed.webservices.response.homepost.Post;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JZVideoPlayerStandard;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by pushpender.singh on 30/7/18.
 */

public class ShowImageActivity extends BaseActivity {
    //private String url;
    List<Image> dataObjectList;
    private Context mContext = ShowImageActivity.this;
    private ActivityImageBinding binding;
    private int flag = 1;
    private ArrayList<Post> posts;
    private boolean isLiked;
    private Post post;
    private boolean darkThemeEnabled;
    private ArrayList<GetRoomResonse.RoomsBean> roomList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image);
        dataObjectList = (List<Image>) getIntent().getExtras().getSerializable("imageList");
        darkThemeEnabled = CommonUtils.getPreferencesBoolean(mContext, AppConstants.DARK_THEME);

        initializedControl();
        setToolBar();
        setOnClickListener();
       /* Glide.with(mContext) // Bind it with the context of the actual view used
                .load(url) // Load the image
                .asBitmap() // All our images are static, we want to display them as bitmaps
                .format(DecodeFormat.PREFER_RGB_565) // the decode format - this will not use alpha at all
                .thumbnail(0.2f)// make use of the thumbnail which can display a down-sized version of the image
                .into(binding.ivPostImage); // Voilla - the target view*/
    }

    @Override
    public void initializedControl() {
        binding.tvViews.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeGreyText()));

        setUpViewpager(dataObjectList);
    }

    private void setUpViewpager(List<Image> imageList) {
        CustomPageAdapter mCustomPagerAdapter = new CustomPageAdapter(mContext, dataObjectList);
        binding.ivPostComment.setColorFilter(ContextCompat.getColor(mContext, R.color.white), PorterDuff.Mode.SRC_ATOP);
        binding.ivPostDownloads.setColorFilter(ContextCompat.getColor(mContext, R.color.white), PorterDuff.Mode.SRC_ATOP);
        binding.ivPostShare.setColorFilter(ContextCompat.getColor(mContext, R.color.white), PorterDuff.Mode.SRC_ATOP);

        binding.viewPager.setAdapter(mCustomPagerAdapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager, true);


    }

    @Override
    public void setToolBar() {
        /*binding.toolbarMyprofile.ivLeft.setVisibility(View.VISIBLE);
        binding.toolbarMyprofile.ivRight.setVisibility(View.GONE);
  */      //binding.toolbarMyprofile.tvHeader.setVisibility(View.VISIBLE);
    }

    @Override
    public void setOnClickListener() {
   /*     binding.toolbarMyprofile.ivRight.setOnClickListener(this);
        binding.toolbarMyprofile.ivLeft.setOnClickListener(this);*/
        binding.ivHeart.setOnClickListener(this);
        binding.llShare.setOnClickListener(this);
        binding.llDownload.setOnClickListener(this);
        binding.llComment.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
          /*  case R.id.ivLeft:
                finish();
                break;
            case R.id.ivRight:

                break;*/
            case R.id.ivHeart:

                break;

            case R.id.llComment:
                ActivityController.startActivity(mContext,CommentsActivity.class);
                break;
            case R.id.llShare:
                setPostShareDialoge();
                break;
            case R.id.llDownload:
                ToastUtils.showToastShort(mContext,"Post saved successfully");
        }


    }

    private void setPostShareDialoge() {


        final CharSequence[] items = {getString(R.string.Repost), getString(R.string.directshare)};
        AlertDialog.Builder builder;
        if (darkThemeEnabled) {

            builder = new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AlertDialogCustom));
        } else {
            builder = new AlertDialog.Builder(mContext);
        }
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals(getString(R.string.Repost))) {
              /*      Intent intent = new Intent(mContext, RePostActivity.class);
                    intent.putExtra(AppConstants.POST_MODEL, post);
                    startActivity(intent);*/
                   // ActivityController.startActivity(mContext, RePostActivity.class);
                    ToastUtils.showToastShort(mContext,"Post has been Reposted");

                } else if (items[item].equals(getString(R.string.directshare))) {
                   /* if (CommonUtils.isOnline(mContext)) {
                        callGetRoomAPI(post);
                    } else {
                        CommonUtils.showToast(mContext, getString(R.string.internet_alert_msg));
                    }*/
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

        builder.create().getWindow().setBackgroundDrawableResource(android.R.color.background_dark);


    }

    private void callGetRoomAPI(final Post post) {
        if (CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();
            JsonObject jsonObject = new JsonObject();

            Call<GetRoomResonse> call = ApiExecutor.getClient(mContext).apiGetRoom(jsonObject);
            call.enqueue(new Callback<GetRoomResonse>() {
                @Override
                public void onResponse(@NonNull Call<GetRoomResonse> call, @NonNull final Response<GetRoomResonse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            roomList.clear();
                            if (response.body().getRooms() != null && response.body().getRooms().size() > 0) {
                                roomList.addAll(response.body().getRooms());
                            }

                            //totalPage = response.body().getTotal_pages();
                 /*           PostShareBottomSheetFragment postShareBottomSheetFragment =
                                    new PostShareBottomSheetFragment();
                            postShareBottomSheetFragment.show(getChildFragmentManager(),
                                    "add_photo_dialog_fragment");*/
                        } else {
                            ToastUtils.showToastShort(mContext, mContext.getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GetRoomResonse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(mContext, mContext.getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
        }
    }

    private void callLikePostAPI(final int position, String sharedId, String postId, final boolean isLiked) {
        if (CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();
            JsonObject jsonObject = new JsonObject();

            if (sharedId != null && !sharedId.equalsIgnoreCase("")) {
                jsonObject.addProperty("shared_id", sharedId);
            } else {
                jsonObject.addProperty("post_id", postId);
            }

            jsonObject.addProperty("status", isLiked);

            Call<ApiResponse> call = ApiExecutor.getClient(mContext).apiLikePost(jsonObject);
            call.enqueue(new retrofit2.Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            if (isLiked) {
                                int count = (posts.get(position).getLikesCount()) + 1;
                                posts.get(position).setLikesCount(count);
                            } else {
                                if (posts.get(position).getLikesCount() != 0) {
                                    int count = (posts.get(position).getLikesCount() - 1);
                                    posts.get(position).setLikesCount(count);
                                } else {
                                    posts.get(position).setLikesCount(0);
                                }
                            }
                        } else {
                            ToastUtils.showToastShort(mContext, mContext.getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(mContext, mContext.getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
        }
    }

    protected void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }

    private void displayGalleryItem(ImageView galleryView, String galleryItem) {
        if (null != galleryItem) {
            Glide.with(galleryView.getContext()) // Bind it with the context of the actual view used
                    .load(galleryItem) // Load the image
                    .asBitmap() // All our images are static, we want to display them as bitmaps
                    .format(DecodeFormat.PREFER_RGB_565) // the decode format - this will not use alpha at all
                    .centerCrop()// temporary holder displayed while the image loads// need to manually set the animation as bitmap cannot use cross fade
                    .thumbnail(0.2f)// make use of the thumbnail which can display a down-sized version of the image
                    .into(galleryView); // Voilla - the target view
        }
    }

    public class CustomPageAdapter extends PagerAdapter {
        private Context context;
        private List<Image> dataObjectList;

        private LayoutInflater layoutInflater;

        public CustomPageAdapter(Context context, List<Image> dataObjectList) {
            this.context = context;
            this.layoutInflater = (LayoutInflater) this.context.getSystemService(this.context.LAYOUT_INFLATER_SERVICE);
            this.dataObjectList = dataObjectList;

        }

        @Override
        public int getCount() {
            return dataObjectList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == ((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = this.layoutInflater.inflate(R.layout.row_home_bottom, container, false);
            final ImageView displayImage = (ImageView) view.findViewById(R.id.ivPostImage);
            JZVideoPlayerStandard jCVideoPlayer = (JZVideoPlayerStandard) view.findViewById(R.id.videocontroller);
          /*  displayImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, ShowImageActivity.class);
                    intent.putExtra("imageList", (Serializable) dataObjectList);
                    //  intent.putExtras("imageList", dataObjectList);
                    context.startActivity(intent);
                }
            });*/

            if (dataObjectList.get(position).getFileType().length() > 0) {
                switch (dataObjectList.get(position).getFileType()) {
                    case "video":
                        displayImage.setVisibility(View.GONE);
                        jCVideoPlayer.setVisibility(View.VISIBLE);
                        jCVideoPlayer.setUp(dataObjectList.get(position).getFile().getUrl().replace(".mov", ".mp4"),
                                jCVideoPlayer.SCREEN_WINDOW_NORMAL,
                                "");
                        //   jCVideoPlayer.thumbImageView.setImage("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640");
                        break;
                    case "image":
                        displayImage.setVisibility(View.VISIBLE);
                        jCVideoPlayer.setVisibility(View.GONE);
                        ImageZoomHelper.setViewZoomable(displayImage);
                        displayImage.setVisibility(View.VISIBLE);
                        displayGalleryItem(displayImage, dataObjectList.get(position).getFile().getUrl());
                        break;
                    default:
                        displayImage.setVisibility(View.VISIBLE);
                        jCVideoPlayer.setVisibility(View.GONE);
                        ImageZoomHelper.setViewZoomable(displayImage);
                        displayGalleryItem(displayImage, dataObjectList.get(position).getFile().getUrl());
                        break;
                }
            } else {
                displayImage.setVisibility(View.VISIBLE);
                jCVideoPlayer.setVisibility(View.GONE);
                ImageZoomHelper.setViewZoomable(displayImage);
                displayGalleryItem(displayImage, dataObjectList.get(position).getFile().getUrl());
            }

            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // Removes the page from the container for the given position. We simply removed object using removeView()
            // but could’ve also used removeViewAt() by passing it the position.
            try {
                // Remove the view from the container
                container.removeView((View) object);

                // Try to clear resources used for displaying this view
//                Glide.clear(((View) object).findViewById(R.id.ivPostImage));
                // Remove any resources used by this view
                unbindDrawables((View) object);
                // Invalidate the object
                object = null;
            } catch (Exception e) {
                Log.w("", "destroyItem: failed to destroy item and clear it's used resources", e);
            }
        }


    }
}
