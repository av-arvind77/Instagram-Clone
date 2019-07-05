package com.yellowseed.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.apradanas.simplelinkabletext.Link;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.yellowseed.R;
import com.yellowseed.adapter.SocialPostPhotoAdapter;
import com.yellowseed.databinding.ActivityRepostBinding;
import com.yellowseed.imageUtils.CheckPermission;
import com.yellowseed.imageUtils.CropImage;
import com.yellowseed.imageeditor.EditImageActivity;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.SocialPostPhotoModel;
import com.yellowseed.model.UserListModel;
import com.yellowseed.model.resModel.ShareRequest;
import com.yellowseed.model.resModel.SharedResponse;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;
import com.yellowseed.webservices.response.homepost.Post;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Response;

import static com.yellowseed.imageUtils.TakePictureUtils.CROP_FROM_CAMERA;

public class RePostActivity extends BaseActivity implements PlaceSelectionListener {
    private static final int PLACE_PICKER_REQUEST = 110;
    private ActivityRepostBinding binding;
    private Context mContext;
    private boolean flag=true;
    private ArrayList<String> tagsNames=new ArrayList<>();
    private List<UserListModel> tagedFriendList = new ArrayList<>();
    private SocialPostPhotoAdapter addImageAdapter;
    private double placeLatitude, placeLongitude;
    private int TAG_FRIEND_IN_POST = 1005;
    private String postDiscription, postCheckIn;
    private boolean postIsAllowComment;
    private Post postList;
    private Intent intent;
    private Link linkHashtag;
    private ArrayList<SocialPostPhotoModel> imageList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_repost);
        mContext = RePostActivity.this;
        tagedFriendList.clear();
        initializedControl();
        getIntentValue();
        setToolBar();
        setOnClickListener();
        setRecyclerView();
        setNumberOfTaggedFriend();
    }

    private void getIntentValue() {
        if (getIntent() != null && getIntent().getExtras() != null){
                binding.btnPostSocial.setText("Share Post");

                if (getIntent().getSerializableExtra(AppConstants.POST_MODEL) != null){
                    postList = (Post) getIntent().getSerializableExtra(AppConstants.POST_MODEL);
                }
                //Descrition
                postDiscription = postList.getPost().getDescription();
                //setTags(binding.etWriteSomething,postDiscription);
                binding.etWriteSomething.setText(postDiscription);
                //Check In
                postCheckIn = postList.getPost().getCheckIn();
                binding.tvAddress.setText(postCheckIn);
                //Allow comment
                postIsAllowComment = postList.getPost().getStatus();
                if (postIsAllowComment) {
                    binding.ivSwitch.setImageResource(R.mipmap.yellow_switch_img);
                    flag = true;
                } else {
                    binding.ivSwitch.setImageResource(R.mipmap.white_switch_img);
                    flag = false;
                }
                //Tag friend list
            if(postList!=null&& postList.getTagFriends()!=null && postList.getTagFriends().size()>0){
                    for (int i = 0; i < postList.getTagFriends().size(); i++) {
                    UserListModel tagFriendsAttributes = new UserListModel();
                    tagFriendsAttributes.setId(postList.getTagFriends().get(i).getUserId());
                    tagedFriendList.add(tagFriendsAttributes);
                }
            }

                //Image list
                for (int i = 0; i < postList.getImages().size(); i++) {
                    SocialPostPhotoModel socialPostPhotoModel = new SocialPostPhotoModel();
                    socialPostPhotoModel.setUrl(postList.getImages().get(i).getFile().getUrl());
                    socialPostPhotoModel.setId(postList.getImages().get(i).getId());
                    socialPostPhotoModel.setType("server");
                    socialPostPhotoModel.setFile_type(postList.getImages().get(i).getFileType());
                    socialPostPhotoModel.setThumbServer(postList.getImages().get(i).getFile().getThumb().getUrl());
                    imageList.add(socialPostPhotoModel);
                }
            }
    }


    private void setCurrentTheme() {

        binding.llMain.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkTheme()));
        binding.toolbarPostSocial.toolbarMain.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkTheme()));
        binding.toolbarPostSocial.tvRighttext.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setTolbarText()));
        binding.toolbarPostSocial.tvHeader.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        binding.etWriteSomething.setBackground(ContextCompat.getDrawable(mContext,Themes.getInstance(mContext).setDarkSearchDrawable()));
        binding.etWriteSomething.setHintTextColor(ContextCompat.getColor(mContext,Themes.getInstance(mContext).setGreyHint()));
        binding.etWriteSomething.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        binding.tvCheckIn.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        binding.tvPeople.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        binding.tvTagPeoplePost.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        binding.tvAllowComments.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        binding.tvAddress.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        binding.viewLine1.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setViewLineGrey()));
        binding.viewLine2.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setViewLineGrey()));
        Themes.getInstance(mContext).changeIconColor(mContext, binding.ivCheckIn);
        Themes.getInstance(mContext).changeIconColor(mContext, binding.ivTag);
        Themes.getInstance(mContext).changeIconColor(mContext, binding.ivComment);
        Themes.getInstance(mContext).changeIconColor(mContext, binding.toolbarPostSocial.ivLeft);

        binding.viewLine.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setViewLineGrey()));

    }



    @Override
    public void initializedControl() {
        setCurrentTheme();
        linkHashtag = new Link(Pattern.compile("(#\\w+)"))
                .setUnderlined(true)
                .setTextStyle(Link.TextStyle.ITALIC)
                .setTextColor(Color.BLUE)
                .setClickListener(new Link.OnClickListener() {
                    @Override
                    public void onClick(String text) {
                        // do something
                    }
                });

        binding.etWriteSomething.addLink(linkHashtag);

    }

    private void setNumberOfTaggedFriend(){
        if (tagedFriendList != null) {
            binding.tvPeople.setText(tagedFriendList.size()+" People");
        }
        else
        {
            binding.tvPeople.setText("0"+" People");
        }
    }

    @Override
    public void setToolBar() {
        binding.toolbarPostSocial.ivLeft.setVisibility(View.VISIBLE);
        binding.toolbarPostSocial.tvHeader.setVisibility(View.VISIBLE);
        binding.toolbarPostSocial.tvHeader.setText(R.string.post);
    }
    @Override
    public void setOnClickListener() {
        binding.ivSwitch.setOnClickListener(this);
        binding.toolbarPostSocial.ivLeft.setOnClickListener(this);
        binding.tvCheckIn.setOnClickListener(this);
        binding.tvTagPeoplePost.setOnClickListener(this);
        binding.btnPostSocial.setOnClickListener(this);
        binding.lllayout.setOnClickListener(this);
        binding.etWriteSomething.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ivSwitch:
                if (flag) {
                    binding.ivSwitch.setImageResource(R.mipmap.white_switch_img);
                    flag = false;
                } else {
                    binding.ivSwitch.setImageResource(R.mipmap.yellow_switch_img);
                    flag = true;
                }
                break;

            case R.id.ivLeft:
                onBackPressed();
                break;



            case R.id.tvCheckIn:
                //Getting the place
                CommonUtils.getThePlace(this, PLACE_PICKER_REQUEST);
                break;

            case R.id.tvTagPeoplePost:
//                startActivityForResult(new Intent(this, TagFollowingActivity.class).putExtra(), TAG_FRIEND_IN_POST);

                intent = new Intent(new Intent(this, TagFollowingActivity.class));
                if (tagedFriendList != null && tagedFriendList.size() > 0) {
                    intent.putExtra(AppConstants.TAG_FRIEND_LIST, (Serializable) tagedFriendList);
                }
                startActivityForResult(intent, TAG_FRIEND_IN_POST);

                break;

            case R.id.etWriteSomething:
                binding.etWriteSomething.setCursorVisible(true);
                break;

            case R.id.btnPostSocial:
                if(CommonUtils.isOnline(mContext)) {
                    if(binding.etWriteSomething.getText().toString().trim().length()>0){
                        tagsNames=new ArrayList<>();
                        Pattern pattern = Pattern.compile("#\\w+");
                        Matcher matcher = pattern.matcher(binding.etWriteSomething.getText().toString().trim());
                        while (matcher.find())
                        {
                            tagsNames.add(matcher.group());
                        }
                    }
                    sharePostApi();
                }
                else
                {
                    ToastUtils.showToastShort(mContext,mContext.getString(R.string.internet_alert_msg));
                }
                break;

            case R.id.lllayout:
                binding.etWriteSomething.setCursorVisible(false);
                CommonUtils.hideSoftKeyboard(this);
                break;

            default:
                break;
        }
    }

    public void setRecyclerView() {

        binding.rvImageView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        binding.rvImageView.setLayoutManager(layoutManager);
        binding.rvImageView.setNestedScrollingEnabled(false);

        if (imageList.size() == 0){
            binding.rvImageView.setVisibility(View.GONE);
        }
        else
        {
            binding.rvImageView.setVisibility(View.VISIBLE);
        }

        addImageAdapter = new SocialPostPhotoAdapter(mContext, imageList, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.ivCross:
                        imageList.remove(position);
                        addImageAdapter.notifyItemRemoved(position);
                        if (imageList.size() == 0){
                            binding.rvImageView.setVisibility(View.GONE);
                        }
                        else
                        {
                            binding.rvImageView.setVisibility(View.VISIBLE);
                        }
                        break;

                    default:
                        break;
                }
            }
        });
        binding.rvImageView.setAdapter(addImageAdapter);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(mContext, intent);
                this.onPlaceSelected(place);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(mContext, intent);
                this.onError(status);
            }
        }

        else if (requestCode == TAG_FRIEND_IN_POST) {
            if (resultCode == Activity.RESULT_OK) {
                // TODO Extract the data returned from the child Activity.
                if (intent != null && intent.getExtras() != null){
                    tagedFriendList.clear();
                    tagedFriendList = (List<UserListModel>) intent.getSerializableExtra("TagFriendList");
                    binding.tvPeople.setText(tagedFriendList.size()+" People");
                }
            }
        }
        if (imageList.size() == 0){
            binding.rvImageView.setVisibility(View.GONE);
        }
        else
        {
            binding.rvImageView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CheckPermission.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    CommonUtils.showToast(this, "Permission denial");
                }
                break;
        }
    }

    /**
     * this method is used for open crop image
     */
    public void startCropImage(Activity context, String fileName) {
        Intent intent = new Intent(context, EditImageActivity.class);
        intent.putExtra(CropImage.IMAGE_PATH, new File(context.getExternalFilesDir("temp"), fileName).getPath());
        startActivityForResult(intent,CROP_FROM_CAMERA);

    }

    @Override
    public void onPlaceSelected(Place place) {
        binding.tvAddress.setText(place.getName());
        placeLatitude = place.getLatLng().latitude;
        placeLongitude = place.getLatLng().longitude;
    }

    @Override
    public void onError(Status status) {

    }


    private void sharePostApi() {
        if (CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();

            ShareRequest shareRequest = new ShareRequest();
            ShareRequest.ShareBean share = new ShareRequest.ShareBean();
            shareRequest.setShare(share);
            shareRequest.getShare().setPost_id(postList.getPost().getId());
            if (binding.tvAddress.getText().toString().trim() != null){
                shareRequest.getShare().setCheck_in(binding.tvAddress.getText().toString().trim());
            }
            if (!binding.etWriteSomething.getText().toString().isEmpty()) {
                String description = binding.etWriteSomething.getText().toString();
                shareRequest.getShare().setDescription(description);
            }
            shareRequest.getShare().setTitle("");
            shareRequest.getShare().setLatitude(String.valueOf(placeLatitude));
            shareRequest.getShare().setLongitude(String.valueOf(placeLongitude));
            shareRequest.getShare().setShare_type("Public");
            List<ShareRequest.TagFriendModel> friendModels=new ArrayList<>();
            if(tagedFriendList!=null&&tagedFriendList.size()>0){
                for (int i = 0; i < tagedFriendList.size(); i++) {
                    ShareRequest.TagFriendModel model=new ShareRequest.TagFriendModel();
                    model.setUser_id(tagedFriendList.get(i).getId());
                    friendModels.add(model);
                }
            }
            List<ShareRequest.TagNameModel> tagNameModels=new ArrayList<>();
            if(tagsNames!=null&&tagsNames.size()>0){
                for (int i = 0; i < tagsNames.size(); i++) {
                    ShareRequest.TagAttributes tagAttributes=new ShareRequest.TagAttributes();
                    tagAttributes.setName(tagsNames.get(i));
                    ShareRequest.TagNameModel tagNameModel=new ShareRequest.TagNameModel();
                    tagNameModel.setTag_attributes(tagAttributes);
                    tagNameModels.add(tagNameModel);
                }
            }
            shareRequest.getShare().setStatus(flag);
            shareRequest.getShare().setTag_friends_attributes(friendModels);
            shareRequest.getShare().setPost_tags_attributes(tagNameModels);
            Call<SharedResponse> call = ApiExecutor.getClient(mContext).apiSharePost(shareRequest);
            call.enqueue(new retrofit2.Callback<SharedResponse>() {
                @Override
                public void onResponse(@NonNull Call<SharedResponse> call, @NonNull final Response<SharedResponse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            tagedFriendList.clear();
                            startActivity(new Intent(RePostActivity.this,HomeActivity.class));
                        } else {
                            ToastUtils.showToastShort(mContext, mContext.getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(@NonNull Call<SharedResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(mContext, mContext.getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
        }
    }



}
