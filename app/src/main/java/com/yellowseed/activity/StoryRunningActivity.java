package com.yellowseed.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.yellowseed.R;
import com.yellowseed.databinding.ActivityStoryRunningBinding;
import com.yellowseed.model.resModel.StoryDetailResponseModel;
import com.yellowseed.model.resModel.StoryListModel;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;

import java.util.ArrayList;
import java.util.Collection;

import jp.shts.android.storiesprogressview.StoriesProgressView;

public class StoryRunningActivity extends BaseActivity implements StoriesProgressView.StoriesListener {
    private static int PROGRESS_COUNT = 1;
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
    private ActivityStoryRunningBinding binding;
    private Context context;
    private StoriesProgressView storiesProgressView;
    private ImageView image;
    private int counter = 0;
    private ArrayList<StoryListModel.StoriesBean> imageList = new ArrayList<>();

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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_story_running);
        context = StoryRunningActivity.this;
        getIntentValue();
        initializedControl();
        setToolBar();
        setOnClickListener();
    }

    private void getIntentValue() {
        if (getIntent() != null && getIntent().getExtras() != null && getIntent().getSerializableExtra(AppConstants.ALL_STORY_LIST) != null) {
         //   allStoryList = getIntent().getSerializableExtra(AppConstants.ALL_STORY_LIST)
           // imageList.addAll((Collection<? extends StoryListModel.StoriesBean>) getIntent().getSerializableExtra(AppConstants.ALL_STORY_LIST));
            PROGRESS_COUNT = resources.length;
        }
    }

    @Override
    public void onNext() {
        binding.image.setImageResource(resources[++counter]);
       // Picasso.with(context).load(imageList.get(++counter).getStory().getStory_image().getImg()).into(binding.image);
    }

    @Override
    public void onPrev() {
        // Call when finished revserse animation.
        if ((counter - 1) < 0) return;
       binding.image.setImageResource(resources[--counter]);
      //  Picasso.with(context).load(imageList.get(--counter).getStory().getStory_image().getImg()).into(binding.image);
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

    @Override
    public void initializedControl() {
        // storiesProgressView = (StoriesProgressView) findViewById(R.id.stories);
        binding.stories.setStoriesCount(PROGRESS_COUNT); // <- set stories
        binding.stories.setStoryDuration(1200L); // <- set a story duration
        binding.stories.setStoriesListener(this); // <- set listener
        binding.stories.startStories(); // <- start progress


        //  image = (ImageView) findViewById(R.id.image);
        for (int i = 0; i < resources.length; i++) {
            binding.image.setImageResource(resources[counter]);
           // Picasso.with(context).load(imageList.get(counter).getStory().getStory_image().getImg()).into(binding.image);
        }
        // bind reverse view
        View reverse = findViewById(R.id.reverse);
        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.stories.reverse();
            }
        });
        reverse.setOnTouchListener(onTouchListener);

        // bind skip view
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
        binding.ivBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
            default:
                break;
        }
    }
}
