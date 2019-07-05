package com.yellowseed.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;


import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.yellowseed.R;
import com.yellowseed.databinding.ActivityPostTextBinding;
import com.yellowseed.utils.ActivityController;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.Themes;

public class PostTextActivity extends BaseActivity implements PlaceSelectionListener {

    private ActivityPostTextBinding binding;
    private static final int PLACE_PICKER_REQUEST = 110;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding = DataBindingUtil.setContentView(this,R.layout.activity_post_text);
       context = PostTextActivity.this;
       initializedControl();
       setOnClickListener();
       setToolBar();
    }

    @Override
    public void initializedControl() {
        setCurrentTheme();


    }

    private void setCurrentTheme() {
        binding.viewLine.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setViewLineGrey()));

    }


    @Override
    public void setToolBar() {

        binding.toolbarPostText.ivLeft.setVisibility(View.VISIBLE);
        binding.toolbarPostText.tvHeader.setText(R.string.post);
        binding.toolbarPostText.tvHeader.setVisibility(View.VISIBLE);
        binding.toolbarPostText.ivLeft.setOnClickListener(this);


    }

    @Override
    public void setOnClickListener() {
        binding.btnPostText.setOnClickListener(this);
        binding.llPostText.setOnClickListener(this);
        binding.tvCheckInPostText.setOnClickListener(this);
        binding.tvTagPeoplePostText.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnPostText:
                onBackPressed();
                break;

            case R.id.ivLeft:
                onBackPressed();
                break;
            case R.id.llPostText:

                CommonUtils.hideSoftKeyboard(this);
                break;
            case R.id.tvCheckInPostText:
                CommonUtils.getThePlace(this, PLACE_PICKER_REQUEST);
                break;
            case R.id.tvTagPeoplePostText:
                ActivityController.startActivity(this,TagFollowingActivity.class);
                break;

                default:
                    break;
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {


         if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(context, intent);
                this.onPlaceSelected(place);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(context, intent);
                this.onError(status);
            }
        }
    }

    @Override
    public void onPlaceSelected(Place place) {
        binding.tvCheckInPlace.setText(place.getName());
    }

    @Override
    public void onError(Status status) {

    }
}
