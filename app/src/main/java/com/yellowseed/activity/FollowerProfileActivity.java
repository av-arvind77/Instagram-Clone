package com.yellowseed.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.daasuu.bl.BubbleLayout;
import com.daasuu.bl.BubblePopupHelper;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.yellowseed.R;
import com.yellowseed.adapter.AddSchoolItemsAdapter;
import com.yellowseed.adapter.ThemesAdapter;
import com.yellowseed.databinding.ActivityFollowerProfileBinding;
import com.yellowseed.fragments.GridFragment;
import com.yellowseed.fragments.ListFragment;
import com.yellowseed.imageUtils.CircleTransformation;
import com.yellowseed.listener.ApiCallback;
import com.yellowseed.model.AvtarImageModel;
import com.yellowseed.model.reqModel.SchoolModel;
import com.yellowseed.model.reqModel.UserModel;
import com.yellowseed.utils.ActivityController;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DateUtils;
import com.yellowseed.utils.LogUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiResponse;
import com.yellowseed.webservices.CommonApi;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class FollowerProfileActivity extends BaseActivity {
    private ActivityFollowerProfileBinding binding;
    private Context mContext;
    private boolean dialog;
    private int[] posts = {R.mipmap.img, R.mipmap.img, R.mipmap.img,
            R.mipmap.img, R.mipmap.img, R.mipmap.img,
            R.mipmap.img, R.mipmap.img, R.mipmap.img};
    private String userId = "";
    private AddSchoolItemsAdapter adapterSchool;
    private AddSchoolItemsAdapter adapterCollege;
    private AddSchoolItemsAdapter adapterWorksAt;
    private List<SchoolModel> arlSchool = new ArrayList<>();
    private List<SchoolModel> arlCollege = new ArrayList<>();
    private List<SchoolModel> arlWorkAt = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_follower_profile);
        mContext = FollowerProfileActivity.this;
        initializedControl();
        setToolBar();
        setOnClickListener();
        setAdapters();
    }

    private void setAdapters() {
        binding.rvCollege.setLayoutManager(new LinearLayoutManager(mContext));
        binding.rvWorkAt.setLayoutManager(new LinearLayoutManager(mContext));
        binding.rvSchool.setLayoutManager(new LinearLayoutManager(mContext));
        adapterSchool = new AddSchoolItemsAdapter(mContext,AppConstants.SCHOOL_TYPE, arlSchool);
        adapterCollege = new AddSchoolItemsAdapter(mContext,AppConstants.COLLEGE_TYPE, arlCollege);
        adapterWorksAt = new AddSchoolItemsAdapter(mContext,AppConstants.WORK_AT_TYPE, arlWorkAt);
        binding.rvSchool.setAdapter(adapterSchool);
        binding.rvCollege.setAdapter(adapterCollege);
        binding.rvWorkAt.setAdapter(adapterWorksAt);
        binding.rvSchool.setNestedScrollingEnabled(false);
        binding.rvCollege.setNestedScrollingEnabled(false);
        binding.rvWorkAt.setNestedScrollingEnabled(false);

    }

    @Override
    public void initializedControl() {
        //initViews();
        setCurrentTheme();
        AppConstants.clickMenuItem = false;
        binding.tvEmail.setPaintFlags(binding.tvEmail.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        binding.tvWebsite.setPaintFlags(binding.tvWebsite.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        binding.tvPhoneNo.setPaintFlags(binding.tvPhoneNo.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        getDataIntent();
        setPopMenuDialogIcon();
        binding.ivListItem.setImageResource(R.mipmap.list_icon_sel);
        CommonUtils.setFragment(new ListFragment(), true, this, R.id.llContainer, "other profile list");

//        if(!TextUtils.isEmpty(userId)) {
        CommonApi.callGetOtherUserProfileAPI(mContext, userId, new ApiCallback() {
            @Override
            public void onSuccess(ApiResponse apiResponse) {
                setUserData(apiResponse.getUser());

            }

            @Override
            public void onFailure(ApiResponse apiResponse) {
            }
        });
      /*  }
        else {
            LogUtils.LOGE(TAG, getString(R.string.user_id_not_found));
        }*/

    }

    private void setCurrentTheme() {
        binding.tvUserImage.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));

    }

    private void getDataIntent() {
        if(getIntent() != null && getIntent().hasExtra(AppConstants.USER_ID))
        {

            userId = getIntent().getStringExtra(AppConstants.USER_ID);
        }
    }


    private void setPopMenuDialogIcon() {
        binding.ivListItem.setImageResource(R.mipmap.list_icon);
        binding.ivGridItem.setImageResource(R.mipmap.grid);
        binding.ivTaggedItem.setImageResource(R.mipmap.group_tag_icon);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item_another_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    private void initViews() {
        binding.followerProfilePost.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mContext, 3);
        binding.followerProfilePost.setLayoutManager(layoutManager);

        ArrayList<AvtarImageModel> images = prepareData();
        ThemesAdapter adapter = new ThemesAdapter(images, mContext);
        binding.followerProfilePost.setAdapter(adapter);
    }

    private ArrayList<AvtarImageModel> prepareData() {
        ArrayList<AvtarImageModel> data = new ArrayList<>();
        for (int i = 0; i < posts.length; i++) {
            AvtarImageModel item = new AvtarImageModel();
            item.setImage(posts[i]);
            data.add(item);
        }
        return data;
    }
/*
    private void showMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        try {
            Field[] fields = popup.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popup);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper
                            .getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod(
                            "setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        popup.inflate(R.menu.menu_item_another_user);
        popup.show();

    }*/


    private void openMenuPopup( View targetView) {


        BubbleLayout customLayout = (BubbleLayout) LayoutInflater.from(mContext).inflate(R.layout.profile_pop_up, null);
        customLayout.setBubbleColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setPopupBackground()));

        final PopupWindow quickAction = BubblePopupHelper.create(mContext, customLayout);
        LinearLayout llProfile = (LinearLayout) customLayout.findViewById(R.id.llProfile);
        View view=customLayout.findViewById(R.id.viewLine);
        llProfile.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setPopupBackground()));
        view.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setViewLineGrey()));


        TextView tvShare = (TextView) customLayout.findViewById(R.id.tvShareProfile);
        TextView tvTurnOn = (TextView) customLayout.findViewById(R.id.tvTurnOnNotifcation);
        TextView tvShareNotification = (TextView) customLayout.findViewById(R.id.tvStoryNotification);
        TextView tvBlock = (TextView) customLayout.findViewById(R.id.tvBlock);
        TextView tvReport = (TextView) customLayout.findViewById(R.id.tvReport);





        ImageView ivShare = customLayout.findViewById(R.id.ivShare);
        ImageView ivNotication = customLayout.findViewById(R.id.ivNotifiaction);
        ImageView ivStory = customLayout.findViewById(R.id.ivStory);

        tvShare.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        tvTurnOn.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        tvShareNotification.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        Themes.getInstance(mContext).changeIconColorToWhite(mContext, ivShare);
        Themes.getInstance(mContext).changeIconColorToWhite(mContext, ivNotication);
        Themes.getInstance(mContext).changeIconColorToWhite(mContext, ivStory);



        tvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToastShort(mContext,"Share Profile");
            }
        });
        tvTurnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToastShort(mContext, "Turn on Post Notification");

            }
        });
        tvShareNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToastShort(mContext, "Turn on Story Notification");

            }
        });
        tvBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonApi.callBlockUserAPI(mContext, "", new ApiCallback() {
                    @Override
                    public void onSuccess(ApiResponse apiResponse) {
                        ToastUtils.showToastShort(mContext, apiResponse.getResponseMessage());
                    }

                    @Override
                    public void onFailure(ApiResponse apiResponse) {

                    }
                });

            }
        });
        tvReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportUserDialog();


            }
        });

        quickAction.showAsDropDown(targetView);
    }



    @Override
    public void setToolBar() {
        binding.toolbarFollowerProfile.ivLeft.setVisibility(View.VISIBLE);
        binding.toolbarFollowerProfile.ivRight.setVisibility(View.VISIBLE);
        binding.toolbarFollowerProfile.tvHeader.setVisibility(View.VISIBLE);
        binding.toolbarFollowerProfile.ivRight.setImageResource(R.mipmap.back_icon);
        binding.toolbarFollowerProfile.ivRight.setImageResource(R.mipmap.three_dot);
    }

    @Override
    public void setOnClickListener() {

        binding.toolbarFollowerProfile.ivLeft.setOnClickListener(this);
        binding.toolbarFollowerProfile.ivRight.setOnClickListener(this);
        binding.tvMessageProfile.setOnClickListener(this);
        binding.llFollowers.setOnClickListener(this);
        binding.llFollowing.setOnClickListener(this);
        binding.llSeeMoreLess.setOnClickListener(this);
        binding.tvFollowingProfile.setOnClickListener(this);

        binding.ivSettingProfile.setOnClickListener(this);
        binding.ivListItem.setOnClickListener(this);
        binding.ivGridItem.setOnClickListener(this);
        binding.ivTaggedItem.setOnClickListener(this);
        binding.seeMUtualMoreFollwers.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ivSettingProfile:
                if (dialog) {
                    binding.llDialog.setVisibility(View.GONE);
                    dialog = false;
                } else {
                    binding.llDialog.setVisibility(View.VISIBLE);
                    dialog = true;
                }
                break;

            case R.id.ivLeft:
                finish();
                break;
            case R.id.ivRight:
                openMenuPopup(v) ;
                break;
            case R.id.seeMUtualMoreFollwers:
                ToastUtils.showToastShort(mContext, getString(R.string.work_in_progress));
                break;
            case R.id.tvMessageProfile:
                ActivityController.startActivity(mContext, ChatScreenActivity.class);
                break;
            case R.id.llFollowers:
                ActivityController.startActivity(mContext, SearchFollowersActivity.class);
                break;
            case R.id.llFollowing:
                ActivityController.startActivity(mContext, SearchFollowingActivity.class);
                break;
            case R.id.tvFollowingProfile:
                followUser();
                break;
            case R.id.llSeeMoreLess:
                setLessMoreTogel();
                break;
            case R.id.ivListItem:
                setPopMenuDialogIcon();
                binding.ivListItem.setImageResource(R.mipmap.list_icon_sel);
                CommonUtils.setFragment(new ListFragment(), true, this, R.id.llContainer, "other profile list");
                binding.llDialog.setVisibility(View.GONE);
                dialog = false;
                break;
            case R.id.ivGridItem:
                setPopMenuDialogIcon();
                binding.ivGridItem.setImageResource(R.mipmap.grid_sel);
                CommonUtils.setFragment(new GridFragment(), true, this, R.id.llContainer, "other profile grid");
                binding.llDialog.setVisibility(View.GONE);
                dialog = false;
                break;
            case R.id.ivTaggedItem:
                setPopMenuDialogIcon();
                binding.ivTaggedItem.setImageResource(R.mipmap.group_tag_icon_sel);
                CommonUtils.setFragment(new GridFragment(), true, this, R.id.llContainer, "other profile tagged item ");
                binding.llDialog.setVisibility(View.GONE);
                dialog = false;
                break;

            default:
                break;
        }
    }

    private void setLessMoreTogel() {
        if (!AppConstants.seeUSerDetail) {
            binding.llUserDetils.setVisibility(View.VISIBLE);
            binding.tvSeeMoreLess.setText(R.string.see_less_info);
            BitmapDrawable drawable = (BitmapDrawable) CommonUtils.getDrawable(mContext, R.mipmap.down_icon);

            Bitmap bInput, bOutput;
            bInput = drawable.getBitmap();
            Matrix matrix = new Matrix();
            matrix.preScale(1.0f, -1.0f);
            bOutput = Bitmap.createBitmap(bInput, 0, 0, bInput.getWidth(), bInput.getHeight(), matrix, true);
            binding.ivDropDown.setImageBitmap(bOutput);
            AppConstants.seeUSerDetail = true;
        } else {
            binding.llUserDetils.setVisibility(View.GONE);
            binding.tvSeeMoreLess.setText(R.string.see_more_info);
            BitmapDrawable drawable = (BitmapDrawable) CommonUtils.getDrawable(mContext, R.mipmap.down_icon);

            Bitmap bInput, bOutput;
            bInput = drawable.getBitmap();
            Matrix matrix = new Matrix();
            matrix.preScale(-1.0f, 1.0f);
            bOutput = Bitmap.createBitmap(bInput, 0, 0, bInput.getWidth(), bInput.getHeight(), matrix, true);
            binding.ivDropDown.setImageBitmap(bOutput);
            AppConstants.seeUSerDetail = false;
        }
    }

    private void followUser() {

        if (binding.tvFollowingProfile.getText().toString().equals("Following")) {


            final CharSequence[] items = {Html.fromHtml("<font color = '#FC2B2B'>Unfollow</font>"), Html.fromHtml("<font color = '#FC2B2B'>Cancel</font>")};
            // final CharSequence[] items = {getString(R.string.themes), getString(R.string.gallery), getString(R.string.solid_color), getString(R.string.no_wallpaper), getString(R.string.default_wallpaper)};
            new MaterialDialog.Builder(mContext)
                    .title(R.string.unfollow).titleGravity(GravityEnum.CENTER)
                    .items(items)
                    .itemsColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()))
                    .backgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeStory()))
                    .titleColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()))
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {

                            if (position == 0) {
                                binding.tvFollowingProfile.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_home_edittext));
                                binding.tvFollowingProfile.setText(R.string.follow);
                            } else if (position == 1) {
                                dialog.dismiss();
                            } else {
                                binding.tvFollowingProfile.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_yellow_soft_corner));
                                binding.tvFollowingProfile.setText(R.string.following);
                            }
                        }
                    }).show();


        } else {
            binding.tvFollowingProfile.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_yellow_soft_corner));
            binding.tvFollowingProfile.setText(R.string.following);
        }
    }


  /*  @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_ShareProfile:
                ToastUtils.shv owToastShort(mContext, "Share Profile");
                return true;
            case R.id.action_TurnOnPostNotification:
                ToastUtils.showToastShort(mContext, "Turn on Post Notification");
                return true;
            case R.id.action_TurnONStoryNotification:
                ToastUtils.showToastShort(mContext, "Turn on Story Notification");
                return true;
            case R.id.action_BlockAnotherUser:
                CommonApi.callBlockUserAPI(mContext, "", new ApiCallback() {
                    @Override
                    public void onSuccess(ApiResponse apiResponse) {
                        ToastUtils.showToastShort(mContext, apiResponse.getResponseMessage());
                    }

                    @Override
                    public void onFailure(ApiResponse apiResponse) {

                    }
                });
                return true;
            case R.id.action_ReportAnotherUSer:
                reportUserDialog();
                return true;
            default:
                return false;

        }
    }*/


    private void reportUserDialog() {
        final CharSequence[] items = {Html.fromHtml("<font color = '#FC2B2B'>Its Spam</font>"), Html.fromHtml("<font color = '#FC2B2B'>Report this Profile</font>"), Html.fromHtml("<font color = '#FC2B2B'>Inappropriate</font>")};

        new MaterialDialog.Builder(mContext)
                .title(R.string.understandthereason).titleGravity(GravityEnum.CENTER)
                .items(items)
                .itemsColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()))
                .backgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeStory()))
                .titleColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()))
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {

                        if (position == 0) {
                            reportUSerScreem();
                            dialog.dismiss();
                        } else if (position == 1) {
                            reportUSerScreem();
                            dialog.dismiss();
                        } else if (position == 2) {
                            reportUSerScreem();
                            dialog.dismiss();
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

    }

    private void reportUSerScreem() {
        if (!AppConstants.reportAnotherUSer) {
            binding.tvFollowingProfile.setVisibility(View.GONE);
            binding.tvFollowProfile.setVisibility(View.VISIBLE);


        }
    }


    private void setUserData(UserModel userModel) {

        binding.toolbarFollowerProfile.tvHeader.setText(TextUtils.isEmpty(userModel.getName()) ? "" : userModel.getName());

        binding.tvName.setText(TextUtils.isEmpty(userModel.getName()) ? "" : userModel.getName());
        if(!TextUtils.isEmpty(userModel.getUser_name())) {
            binding.tvUserName.setText(TextUtils.isEmpty(userModel.getUser_name()) ? "" : userModel.getUser_name());
            binding.llSeeMoreLess.setVisibility(View.VISIBLE);
        }
        binding.tvFollowers.setText(String.valueOf(userModel.getFollower()));
        binding.tvFollowing.setText(String.valueOf(userModel.getFollow()));

        if(!TextUtils.isEmpty(userModel.getAvatar_img().getAvatar_image()))
        {
            Picasso.with(mContext).load(userModel.getAvatar_img().getAvatar_image()).placeholder(R.mipmap.avaterimg).
                    transform(new CircleTransformation()).memoryPolicy(MemoryPolicy.NO_CACHE).into(binding.ivAvatar);
        }

            CommonUtils.otherUserProfile(mContext,binding.ivUserPic,userModel.getImage(),binding.tvUserImage,
                    userModel.getName());

        if(userModel.getSchool() != null && userModel.getSchool().size()>0)
        {
            binding.rvSchool.setVisibility(View.VISIBLE);
            arlSchool.clear();
            arlSchool.addAll(userModel.getSchool());
            adapterSchool.notifyDataSetChanged();
        }

        if(userModel.getCollege() != null && userModel.getCollege().size()>0)
        {
            binding.rvCollege.setVisibility(View.VISIBLE);
            arlCollege.clear();
            arlCollege.addAll(userModel.getCollege());
            adapterCollege.notifyDataSetChanged();
        }
        if(userModel.getWork() != null && userModel.getWork().size()>0)
        {
            binding.rvWorkAt.setVisibility(View.VISIBLE);
            arlWorkAt.clear();
            arlWorkAt.addAll(userModel.getWork());
            adapterWorksAt.notifyDataSetChanged();
        }
        if(!TextUtils.isEmpty(userModel.getCity())) {
            binding.tblLivesIn.setVisibility(View.VISIBLE);
            binding.tvLivesIn.setText(TextUtils.isEmpty(userModel.getCity()) ? "" : userModel.getCity() + ", " + userModel.getCountry());
        }
        if(!TextUtils.isEmpty(userModel.getHometown())) {
            binding.tblHomeTown.setVisibility(View.VISIBLE);
            binding.tvHomeTown.setText(TextUtils.isEmpty(userModel.getHometown()) ? "" : userModel.getHometown());
        }
        if(!TextUtils.isEmpty(userModel.getEmail())) {
            binding.tblEmail.setVisibility(View.VISIBLE);
            binding.tvEmail.setText(TextUtils.isEmpty(userModel.getEmail()) ? "" : userModel.getEmail());
        }
        if(!TextUtils.isEmpty(userModel.getWebsite())){
            binding.tblWebsite.setVisibility(View.VISIBLE);
            binding.tvWebsite.setText(TextUtils.isEmpty(userModel.getWebsite()) ? "" : userModel.getWebsite());}
        if(!TextUtils.isEmpty(userModel.getPhone_no())){
            binding.tblPhoneNo.setVisibility(View.VISIBLE);
            binding.tvPhoneNo.setText(TextUtils.isEmpty(userModel.getPhone_no()) ? "" : userModel.getPhone_no());
        }
        if(!TextUtils.isEmpty(userModel.getGender())) {
            binding.tblGneder.setVisibility(View.VISIBLE);
            binding.tvGender.setText(TextUtils.isEmpty(userModel.getGender()) ? "" : userModel.getGender());
        }

        if(!TextUtils.isEmpty(userModel.getDob()) && !userModel.getDob().equalsIgnoreCase("0")) {
            binding.tblDob.setVisibility(View.VISIBLE);
            binding.tvDateOfBirth.setText(TextUtils.isEmpty(userModel.getDob()) ? "" : DateUtils.getDateOfTimestamp(Long.parseLong(userModel.getDob())));
        }
        binding.tvBio.setText(TextUtils.isEmpty(userModel.getBio()) ? "" : userModel.getBio());

    }
}
