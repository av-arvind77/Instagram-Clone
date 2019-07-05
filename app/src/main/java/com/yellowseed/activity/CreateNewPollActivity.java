package com.yellowseed.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;

import android.widget.FrameLayout;
import android.widget.Toast;


import com.yellowseed.R;
import com.yellowseed.databinding.ActivityCreateNewStoryBinding;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.Themes;

public class CreateNewPollActivity extends BaseActivity {

    private ActivityCreateNewStoryBinding binding;
    private Context context;
    private String path;
    public static final int POLL_INTENT = 9;
    private String location;
    private int initialX,initialXLocation;
    private int initialY,initialYLocation;
    private int locationX,locationY,pollX,pollY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_new_story);
        context = CreateNewPollActivity.this;
        setCurrentTheme();
        getIntentData();
        setOnClickListener();
        initializedControl();

        binding.llLocation.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
               movingLayoutlocation(v,event);
                return true;
            }
        });

        binding.llPoll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                movingPollingLayout(v,event);
                return true;
            }
        });

        binding.llStory.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                movingPollingLayout(v,event);
                return true;
            }
        });
        binding.llYes.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                movingPollingLayout(v,event);
                return true;
            }
        });
        binding.llNo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                movingPollingLayout(v,event);
                return true;
            }
        });
    }

    private void movingPollingLayout(View v, MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) binding.llPoll.getLayoutParams();
                initialX = X - params.leftMargin;
                initialY = Y - params.topMargin;

                break;
            case MotionEvent.ACTION_UP:
                v.performClick();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) binding.llPoll.getLayoutParams();
                int leftMargin = X - initialX;
                int topMargin = Y - initialY;
                int width = binding.llPoll.getWidth();
                int height = binding.llPoll.getHeight();
                if (leftMargin < 0) {
                    leftMargin=0;
                    layoutParams.leftMargin = leftMargin;
                } else if ((leftMargin + width) > binding.llMainLayout.getWidth()) {
                    if (leftMargin >= binding.llMainLayout.getWidth() - width) {
                        leftMargin = binding.llMainLayout.getWidth() - width;
                    }
                    //layoutParams.rightMargin = binding.llMainLayout.getWidth() - width - leftMargin;
                }
                if (topMargin < 0) {
                    topMargin=0;
                    layoutParams.topMargin = topMargin;
                } else if ((topMargin + height) >= binding.llMainLayout.getHeight()) {
                    if (topMargin >= binding.llMainLayout.getHeight() - height) {
                        topMargin = binding.llMainLayout.getHeight() - height;
                    }
                    //layoutParams.bottomMargin = binding.llMainLayout.getHeight() - height - topMargin;
                }
                // initialX = leftMargin+(width/2);
                // initialY = topMargin+(height/2);
                layoutParams.leftMargin = leftMargin;
                layoutParams.topMargin = topMargin;
                pollX=leftMargin;
                pollY=topMargin;
                Log.e("left top","left "+leftMargin+"top margin"+topMargin);
                binding.llPoll.setLayoutParams(layoutParams);
                break;
        }
    }

    private void movingLayoutlocation(View v, MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) binding.llLocation.getLayoutParams();
                initialXLocation = X - params.leftMargin;
                initialYLocation = Y - params.topMargin;

                break;
            case MotionEvent.ACTION_UP:
                binding.etOption1.setFocusable(true);
                binding.etOption2.setFocusable(true);
                binding.etStoryQuestion.setFocusable(true);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) binding.llLocation.getLayoutParams();
                int leftMargin = X - initialXLocation;
                int topMargin = Y - initialYLocation;
                int width = binding.llLocation.getWidth();
                int height = binding.llLocation.getHeight();
                if (leftMargin < 0) {
                    leftMargin=0;
                    layoutParams.leftMargin = leftMargin;
                } else if ((leftMargin + width) > binding.llMainLayout.getWidth()) {
                    if (leftMargin >= binding.llMainLayout.getWidth() - width) {
                        leftMargin = binding.llMainLayout.getWidth() - width;
                    }
                }
                if (topMargin < 0) {
                    topMargin=0;
                    layoutParams.topMargin = topMargin;
                } else if ((topMargin + height) >= binding.llMainLayout.getHeight()) {
                    if (topMargin >= binding.llMainLayout.getHeight() - height) {
                        topMargin = binding.llMainLayout.getHeight() - height;
                    }
                }
                //  initialXLocation = leftMargin+(width/2);
                //  initialYLocation = topMargin+(height/2);
                layoutParams.leftMargin = leftMargin;
                layoutParams.topMargin = topMargin;
                locationX=leftMargin;
                locationY=topMargin;
                Log.e("left top","left "+leftMargin+"top margin"+topMargin);
                binding.llLocation.setLayoutParams(layoutParams);
                break;
        }
    }

    private void getIntentData() {
        if(getIntent().getExtras().get("location")!=null){
             // imageFile= (File) getIntent().getExtras().get("image");
               location= (String)getIntent().getExtras().get("location");
        }

        if(getIntent().getStringExtra("image")!=null && !getIntent().getStringExtra("image").equalsIgnoreCase("")){
            path= getIntent().getStringExtra("image");
            Bitmap bm = BitmapFactory.decodeFile(path);
            BitmapDrawable background = new BitmapDrawable(getResources(), bm);
            binding.llMain.setBackground(background);
        }
    }

    @Override
    public void initializedControl() {

        if(location!=null && !location.equalsIgnoreCase("")){
            binding.llLocation.setVisibility(View.VISIBLE);
            binding.tvLocation.setText(location);
        }else {
            binding.llLocation.setVisibility(View.GONE);
        }
    }
    private void setCurrentTheme() {
        binding.llMain.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        binding.etStoryQuestion.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.etOption1.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.etOption2.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));

    }

    @Override
    public void setToolBar() {

    }

    @Override
    public void setOnClickListener() {
        binding.tvNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tvNext:

                if(checkValidation()) {
                    Intent intent =new Intent();
                    intent.putExtra(AppConstants.QUESTION, binding.etStoryQuestion.getText().toString().trim());
                    intent.putExtra(AppConstants.OPTION1, binding.etOption1.getText().toString().trim());
                    intent.putExtra(AppConstants.OPTION2, binding.etOption2.getText().toString().trim());
                    intent.putExtra(AppConstants.INITIAL_X_LOCATION, locationX);
                    intent.putExtra(AppConstants.INITIAL_Y_LOCATION, locationY);
                    intent.putExtra(AppConstants.INITIAL_X,pollX );
                    intent.putExtra(AppConstants.INITIAL_Y,pollY );
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;

            default:

                break;
        }
    }

    private boolean checkValidation() {
        if(TextUtils.isEmpty(binding.etStoryQuestion.getText().toString().trim())){
            CommonUtils.showToast(context,"Please enter poll question.");
            return false;
        }else if(TextUtils.isEmpty(binding.etOption1.getText().toString().trim())){
            CommonUtils.showToast(context,"Please enter first option.");
            return false;
        }else if(TextUtils.isEmpty(binding.etOption2.getText().toString().trim())){
            CommonUtils.showToast(context,"Please enter second option.");
            return false;
        }else {
            return true;
        }
    }
}
