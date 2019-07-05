package com.yellowseed.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.yellowseed.R;
import com.yellowseed.databinding.ActivityStoriesViewBinding;
import com.yellowseed.utils.ActivityController;
import com.yellowseed.utils.BaseActivity;

import jp.shts.android.storiesprogressview.StoriesProgressView;

public class StoriesViewActivity extends BaseActivity implements StoriesProgressView.StoriesListener {

    private ActivityStoriesViewBinding binding;
    private Context context;
    private static final int PROGRESS_COUNT = 6;
    private ImageView image;
    private int counter = 0;
    private final int[] resources = new int[]{
            R.mipmap.peter_parker_image,
            R.mipmap.peter_parker_image,
            R.mipmap.peter_parker_image,
            R.mipmap.peter_parker_image,
            R.mipmap.peter_parker_image,
            R.mipmap.peter_parker_image,

    };
    private final long[] durations = new long[]{
            500L, 1000L, 1500L, 4000L, 5000L, 1000,
    };

    long pressTime = 0L;
    long limit = 500L;
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pressTime = System.currentTimeMillis();
                    binding.stories.pause();
                    return false;
                case MotionEvent.ACTION_UP:
                    long now = System.currentTimeMillis();
                    binding.stories.resume();
                    return limit < now - pressTime;
            }
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_stories_view);
        context = StoriesViewActivity.this;
        initializedControl();
        setOnClickListener();
        setToolBar();
    }

    @Override
    public void initializedControl() {
// storiesProgressView = (StoriesProgressView) findViewById(R.id.stories);
        binding.stories.setStoriesCount(PROGRESS_COUNT); // <- set stories
        binding.stories.setStoryDuration(1200L); // <- set a story duration
        binding.stories.setStoriesListener(this); // <- set listener
        binding.stories.startStories(); // <- start progress


        //  image = (ImageView) findViewById(R.id.image);
        binding.image.setImageResource(resources[counter]);
        //bind reverse view
        View reverse = findViewById(R.id.reverse);
        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.stories.reverse();
            }
        });
        reverse.setOnTouchListener(onTouchListener);

        //bind skip view
        View skip = findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.stories.skip();
            }
        });
        skip.setOnTouchListener(onTouchListener);
    }

    @Override
    public void setToolBar() {

    }

    @Override
    public void setOnClickListener() {
        binding.llViews.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llViews:
                ActivityController.startActivity(context, StoryViewActivity.class);
                break;
            case R.id.ivBack:
                onBackPressed();
                break;

            default:
                break;
        }
    }

    @Override
    public void onNext() {
        binding.image.setImageResource(resources[++counter]);

    }

    @Override
    public void onPrev() {
// Call when finished revserse animation.
        if ((counter - 1) < 0) return;
        binding.image.setImageResource(resources[--counter]);
    }

    @Override
    public void onComplete() {
        finish();

    }

    @Override
    protected void onDestroy() {
        // Very important !
        binding.stories.destroy();
        super.onDestroy();
    }

}













































/*
package com.yellowseed.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.yellowseed.R;
import com.yellowseed.databinding.ActivityStoriesViewBinding;
import com.yellowseed.model.CommentsModel;
import com.yellowseed.model.reqModel.ViewModel;
import com.yellowseed.model.reqModel.ViewerModel;
import com.yellowseed.model.resModel.StoryDetailResponseModel;
import com.yellowseed.utils.ActivityController;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.PrefManager;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;
import com.yellowseed.webservices.ApiRequest;
import com.yellowseed.webservices.response.StoriesResponse;
import com.yellowseed.webservices.response.homepost.File;

import java.io.Serializable;
import java.util.ArrayList;

import jp.shts.android.storiesprogressview.StoriesProgressView;
import retrofit2.Call;
import retrofit2.Response;

public class StoriesViewActivity extends BaseActivity implements StoriesProgressView.StoriesListener {

    private static int PROGRESS_COUNT = 0;
    long pressTime = 0L;
    long limit = 6000L;
    private ActivityStoriesViewBinding binding;
    private Context mContext;
    private int counter = 0;
    private ArrayList<StoryDetailResponseModel.StoriesBean> imageList = new ArrayList<>();
    private Uri uri;
    private double lat, lng;
    private String placeName;
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pressTime = System.currentTimeMillis();
                    binding.stories.pause();
                    return false;
                case MotionEvent.ACTION_UP:
                    long now = System.currentTimeMillis();
                    binding.stories.resume();
                    return limit < now - pressTime;
            }
            return false;
        }
    };
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_stories_view);
        mContext = StoriesViewActivity.this;
        getIntentValue();
        if (CommonUtils.isOnline(mContext)) {
            storiesApi(userId);
        } else {
            ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
        }
        setOnClickListener();
        setToolBar();
    }

    private void getIntentValue() {
        if (getIntent() != null && getIntent().getExtras() != null && getIntent().getStringExtra(AppConstants.USER_ID) != null) {
            userId = getIntent().getStringExtra(AppConstants.USER_ID);
        }
    }

    @Override
    public void initializedControl() {
// storiesProgressView = (StoriesProgressView) findViewById(R.id.stories);

        if (imageList.size() > 0) {
            binding.stories.setStoriesCount(PROGRESS_COUNT); // <- set stories
            binding.stories.setStoryDuration(6000L); // <- set a story duration
            binding.stories.setStoriesListener(this); // <- set listener
            binding.stories.startStories(); // <- start progress
            binding.tvStoriesView.setText("" + imageList.get(counter).getViewers_count() + " Views");

            if (imageList.get(counter).getStory().getStory_image().getImg() != null && imageList.get(counter).getStory().getStory_image().getImg().length() > 0) {
                if (imageList.get(counter).getStory().getStory_image().getImg().endsWith(".mp4")) {
                    Picasso.with(mContext).load(imageList.get(counter).getStory().getStory_image().getImg().replace(".mp4", ".jpg")).into(binding.image);
                } else {
                    Picasso.with(mContext).load(imageList.get(counter).getStory().getStory_image().getImg()).into(binding.image);
                }
            }
            if (imageList.get(counter).getUser().getId().equalsIgnoreCase(new PrefManager(mContext).getUserId())) {
                binding.llViews.setVisibility(View.VISIBLE);
                binding.llEditView.setVisibility(View.GONE);
            } else {
                binding.llViews.setVisibility(View.GONE);
                binding.llEditView.setVisibility(View.VISIBLE);
            }
            if (imageList.get(counter).getCheck_in() != null
                    && imageList.get(counter).getCheck_in().getX_axis() != null
                    && imageList.get(counter).getCheck_in().getY_axis() != null
                    && !imageList.get(counter).getCheck_in().getX_axis().equalsIgnoreCase("")
                    && !imageList.get(counter).getCheck_in().getY_axis().equalsIgnoreCase("")
                    && imageList.get(counter).getCheck_in().getLocation() != null && imageList.get(counter).getCheck_in().getLocation().length() > 0) {
                // uri=Uri.parse("geo:"+imageList.get(counter).getCheck_in().getLatitude()+","+imageList.get(counter).getCheck_in().getLongitude());
                lat = imageList.get(counter).getCheck_in().getLatitude();
                lng = imageList.get(counter).getCheck_in().getLongitude();
                placeName = imageList.get(counter).getCheck_in().getLocation();
                binding.llLocation.setVisibility(View.VISIBLE);
                FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) binding.llLocation.getLayoutParams();
                layoutParams1.leftMargin = (int) Float.parseFloat(imageList.get(counter).getCheck_in().getX_axis());
                layoutParams1.topMargin = (int) Float.parseFloat(imageList.get(counter).getCheck_in().getY_axis()) - (binding.llLocation.getHeight() / 2);
                binding.llLocation.setLayoutParams(layoutParams1);
                binding.tvLocation.setText(imageList.get(counter).getCheck_in().getLocation());

            } else {
                binding.llLocation.setVisibility(View.GONE);
            }

            if (imageList.get(counter).getPoll() != null
                    && imageList.get(counter).getPoll().getX_axis() != null
                    && imageList.get(counter).getPoll().getY_axis() != null
                    && !imageList.get(counter).getPoll().getX_axis().equalsIgnoreCase("")
                    && !imageList.get(counter).getPoll().getY_axis().equalsIgnoreCase("")
                    ) {

                binding.llRoot.setVisibility(View.VISIBLE);
                FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) binding.llRoot.getLayoutParams();
                layoutParams2.leftMargin = (int) Float.parseFloat(imageList.get(counter).getPoll().getX_axis()) - (binding.llRoot.getWidth() / 2);
                layoutParams2.topMargin = (int) Float.parseFloat(imageList.get(counter).getPoll().getY_axis()) - (binding.llRoot.getHeight() / 2);
                binding.llRoot.setLayoutParams(layoutParams2);
                binding.etStoryQuestion.setText(imageList.get(counter).getPoll().getPoll_title());
                try {
                    binding.etOption1.setText(imageList.get(counter).getPoll().getType1() + getPercentForType1(imageList.get(counter).getPoll()));
                    binding.etOption2.setText(imageList.get(counter).getPoll().getType2() + getPercentForType2(imageList.get(counter).getPoll()));

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                binding.llRoot.setVisibility(View.GONE);
            }
        } else {
            finish();
        }

        //bind reverse view
        View reverse = findViewById(R.id.reverse);
        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.stories.reverse();
            }
        });
        reverse.setOnTouchListener(onTouchListener);

        //bind skip view
        View skip = findViewById(R.id.skip);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.stories.skip();
            }
        });
        skip.setOnTouchListener(onTouchListener);
    }

    @Override
    public void setToolBar() {

    }

    @Override
    public void setOnClickListener() {
        binding.llViews.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);
        binding.llLocation.setOnClickListener(this);
        binding.etOption1.setOnClickListener(this);
        binding.etOption2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llViews:
                if (imageList.size() > 0) {
                    Intent intent = new Intent(mContext, StoryViewActivity.class);
                    intent.putExtra("story_id", imageList.get(counter).getStory().getStory_id());
                    intent.putExtra("pollDetail", (Serializable) imageList.get(counter).getPoll());
                    startActivity(intent);
                }
                break;

            case R.id.ivBack:
                onBackPressed();
                break;

            case R.id.llLocation:
                binding.stories.pause();
                CommonUtils.showMap(mContext, String.valueOf(lat), String.valueOf(lng), placeName);
                break;

            case R.id.etOption1:
                if (imageList.get(counter).getPoll() != null && !imageList.get(counter).getPoll().isCurrent_user_poll()) {
                    binding.stories.pause();
                    ViewModel viewModel = new ViewModel();
                    ViewerModel model = new ViewerModel();
                    model.setPoll_type(imageList.get(counter).getPoll().getType1());
                    model.setStory_id(imageList.get(counter).getPoll().getStory_id());
                    viewModel.viewer = model;
                    apiGivingVote(viewModel, imageList.get(counter).getPoll());
                }

                break;
            case R.id.etOption2:
                if (imageList.get(counter).getPoll() != null && !imageList.get(counter).getPoll().isCurrent_user_poll()) {
                    binding.stories.pause();
                    ViewModel viewModel2 = new ViewModel();
                    ViewerModel model2 = new ViewerModel();
                    model2.setPoll_type(imageList.get(counter).getPoll().getType1());
                    model2.setStory_id(imageList.get(counter).getPoll().getStory_id());
                    viewModel2.viewer = model2;
                    apiGivingVote(viewModel2, imageList.get(counter).getPoll());
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onNext() {
        if (imageList.size() > 0 && imageList.size() > counter) {
            if (imageList.get(counter).getUser().getId().equalsIgnoreCase(new PrefManager(mContext).getUserId())) {
                binding.llViews.setVisibility(View.VISIBLE);
                binding.llEditView.setVisibility(View.GONE);
            } else {
                binding.llViews.setVisibility(View.GONE);
                binding.llEditView.setVisibility(View.GONE);
            }
            binding.tvStoriesView.setText("" + imageList.get(counter).getViewers_count() + " Views");
            counter = ++counter;
            if (imageList.get(counter).getStory().getStory_image().getImg() != null && imageList.get(counter).getStory().getStory_image().getImg().length() > 0) {
                if (imageList.get(counter).getStory().getStory_image().getImg().endsWith(".mp4")) {
                    Picasso.with(mContext).load(imageList.get(counter).getStory().getStory_image().getImg().replace(".mp4", ".jpg")).into(binding.image);
                } else {
                    Picasso.with(mContext).load(imageList.get(counter).getStory().getStory_image().getImg()).into(binding.image);
                }
            }
            if (imageList.get(counter).getCheck_in() != null && imageList.get(counter).getCheck_in().getX_axis() != null && imageList.get(counter).getCheck_in().getX_axis().length() > 0 && imageList.get(counter).getCheck_in().getLocation() != null && imageList.get(counter).getCheck_in().getLocation().length() > 0) {
                lat = imageList.get(counter).getCheck_in().getLatitude();
                lng = imageList.get(counter).getCheck_in().getLongitude();
                placeName = imageList.get(counter).getCheck_in().getLocation();
                binding.llLocation.setVisibility(View.VISIBLE);
                FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) binding.llLocation.getLayoutParams();
                layoutParams1.leftMargin = (int) Float.parseFloat(imageList.get(counter).getCheck_in().getX_axis());
                try {
                    layoutParams1.topMargin = (int) Float.parseFloat(imageList.get(counter).getCheck_in().getY_axis()) - (binding.llLocation.getHeight() / 2);

                } catch (Exception e) {
                    layoutParams1.topMargin = 0;

                }
                binding.llLocation.setLayoutParams(layoutParams1);
                binding.tvLocation.setText(imageList.get(counter).getCheck_in().getLocation());

            } else {
                binding.llLocation.setVisibility(View.GONE);
            }
            if (imageList.get(counter).getPoll() != null && imageList.get(counter).getPoll().getX_axis() != null && imageList.get(counter).getPoll().getX_axis().length() > 0) {
                binding.llRoot.setVisibility(View.VISIBLE);

                FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) binding.llRoot.getLayoutParams();
                layoutParams2.leftMargin = (int) Float.parseFloat(imageList.get(counter).getPoll().getX_axis());
                layoutParams2.topMargin = (int) Float.parseFloat(imageList.get(counter).getPoll().getY_axis());
                binding.llRoot.setLayoutParams(layoutParams2);
                binding.etStoryQuestion.setText(imageList.get(counter).getPoll().getPoll_title());
                try {
                    binding.etOption1.setText(imageList.get(counter).getPoll().getType1() + getPercentForType1(imageList.get(counter).getPoll()));
                    binding.etOption2.setText(imageList.get(counter).getPoll().getType2() + getPercentForType2(imageList.get(counter).getPoll()));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                binding.llRoot.setVisibility(View.GONE);
            }
        }
    }

    private String getPercentForType1(StoryDetailResponseModel.StoriesBean.PollBean poll) {

        float percent = 0.0f;

        try {
            percent = (poll.getType_1_count() / (poll.getType_1_count() + poll.getType_2_count())) * 100;
        } catch (Exception e) {
            return "(0.0%)";
        }


        return "(" + percent + "%)";
    }

    private String getPercentForType2(StoryDetailResponseModel.StoriesBean.PollBean poll) {
        float percent = 0.0f;

        try {
            percent = (poll.getType_2_count() / (poll.getType_1_count() + poll.getType_2_count())) * 100;
        } catch (Exception e) {
            return "(0.0%)";
        }


        return "(" + percent + "%)";
    }

    @Override
    public void onPrev() {
// Call when finished revserse animation.
        if ((counter - 1) < 0) return;
        counter = --counter;
        if (imageList.get(counter).getStory().getStory_image().getImg() != null && imageList.get(counter).getStory().getStory_image().getImg().length() > 0) {
            if (imageList.get(counter).getStory().getStory_image().getImg().endsWith(".mp4")) {
                Picasso.with(mContext).load(imageList.get(counter).getStory().getStory_image().getImg().replace(".mp4", ".jpg")).into(binding.image);
            } else {
                Picasso.with(mContext).load(imageList.get(counter).getStory().getStory_image().getImg()).into(binding.image);
            }
        }
        if (imageList.get(counter).getUser().getId().equalsIgnoreCase(new PrefManager(mContext).getUserId())) {
            binding.llViews.setVisibility(View.VISIBLE);
            binding.llEditView.setVisibility(View.GONE);
        } else {
            binding.llViews.setVisibility(View.GONE);
            binding.llEditView.setVisibility(View.GONE);
        }

        if (imageList.get(counter).getCheck_in() != null && imageList.get(counter).getCheck_in().getLocation() != null && imageList.get(counter).getCheck_in().getLocation().length() > 0) {
            lat = imageList.get(counter).getCheck_in().getLatitude();
            lng = imageList.get(counter).getCheck_in().getLongitude();
            placeName = imageList.get(counter).getCheck_in().getLocation();
            binding.llLocation.setVisibility(View.VISIBLE);
            FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) binding.llLocation.getLayoutParams();
            layoutParams1.leftMargin = (int) Float.parseFloat(imageList.get(counter).getCheck_in().getX_axis());
            layoutParams1.topMargin = (int) Float.parseFloat(imageList.get(counter).getCheck_in().getY_axis());
            binding.llLocation.setLayoutParams(layoutParams1);
        } else {
            binding.llLocation.setVisibility(View.GONE);
        }
        binding.tvStoriesView.setText("" + imageList.get(counter).getViewers_count() + " Views");
        if (imageList.get(counter).getPoll() != null) {
            binding.llRoot.setVisibility(View.VISIBLE);
            FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) binding.llRoot.getLayoutParams();
            layoutParams2.leftMargin = (int) Float.parseFloat(imageList.get(counter).getPoll().getX_axis());
            layoutParams2.topMargin = (int) Float.parseFloat(imageList.get(counter).getPoll().getY_axis());
            binding.llRoot.setLayoutParams(layoutParams2);
        } else {
            binding.llRoot.setVisibility(View.GONE);
        }
    }

    @Override
    public void onComplete() {
        if (getIntent().hasExtra("all")) {
            binding.stories.destroy();
            storiesApi(userId);
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        // Very important !
        binding.stories.destroy();
        super.onDestroy();
    }

    //Stories Api===================================================================================

    private void storiesApi(String userId) {
        if (CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("user_id", userId);

            Call<StoryDetailResponseModel> call = ApiExecutor.getClient(mContext).apiGetStoryDetail(jsonObject);
            call.enqueue(new retrofit2.Callback<StoryDetailResponseModel>() {
                @Override
                public void onResponse(@NonNull Call<StoryDetailResponseModel> call, @NonNull final Response<StoryDetailResponseModel> response) {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                    if (response != null && response.body() != null) {
                        if (response.body().getResponseCode() == AppConstants.SUCCESS) {
                            imageList.addAll(response.body().getStories());
                            PROGRESS_COUNT = response.body().getStories().size();
                            initializedControl();
                        } else {

                        }
                    } else {
                        ToastUtils.showToastShort(mContext, getString(R.string.server_error_msg));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<StoryDetailResponseModel> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(mContext, getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
        }
    }


    private void apiGivingVote(ViewModel model, final StoryDetailResponseModel.StoriesBean.PollBean pollBean) {
        if (CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();
            Call<StoryDetailResponseModel> call = ApiExecutor.getClient(mContext).apiPollStory(model);
            call.enqueue(new retrofit2.Callback<StoryDetailResponseModel>() {
                @Override
                public void onResponse(@NonNull Call<StoryDetailResponseModel> call, @NonNull final Response<StoryDetailResponseModel> response) {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    if (response != null && response.body() != null) {
                        ToastUtils.showToastShort(mContext, "Voted successfully.");
                        binding.stories.resume();
                        pollBean.setCurrent_user_poll(true);
                        try {
                            binding.etOption1.setText(imageList.get(counter).getPoll().getType1() + getPercentForType1(imageList.get(counter).getPoll()));
                            binding.etOption2.setText(imageList.get(counter).getPoll().getType2() + getPercentForType2(imageList.get(counter).getPoll()));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        ToastUtils.showToastShort(mContext, getString(R.string.server_error_msg));
                        binding.stories.resume();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<StoryDetailResponseModel> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(mContext, getString(R.string.server_error_msg));
                    binding.stories.resume();
                }
            });
        } else {
            ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
        }
    }

}
*/
