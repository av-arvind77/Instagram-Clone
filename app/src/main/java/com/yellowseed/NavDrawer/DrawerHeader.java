package com.yellowseed.NavDrawer;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.NonReusable;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.yellowseed.R;
import com.yellowseed.activity.MyProfileActivity;
import com.yellowseed.activity.SearchFollowersActivity;
import com.yellowseed.activity.SearchFollowingActivity;
import com.yellowseed.activity.TermsAndConditionActivity;
import com.yellowseed.customView.CustomRobotoBoldTextView;
import com.yellowseed.customView.CustomRobotoRegularTextView2;
import com.yellowseed.imageUtils.CircleTransformation;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.reqModel.UserModel;
import com.yellowseed.utils.ActivityController;

import java.util.logging.Handler;


/**
 * Created by mobiloitte_india on 7/4/18.
 */
@NonReusable
@Layout(R.layout.drawers_header)
public class DrawerHeader implements android.view.View.OnClickListener {

    private Context mContext;
    View view;
    private int mMenuPosition;
    private DrawerCallBack mCallBack;
    //    @View(R.id.ivCancel)
//    private ImageView ivCancel;
    @View(R.id.ivUserImageH1)
    private ImageView ivUser1;
    @View(R.id.ivUserImageH)
    private ImageView ivUser;
    @View(R.id.tvUserNameH)
    private CustomRobotoBoldTextView tvUserName;
    @View(R.id.tvUserEmailH)
    private CustomRobotoRegularTextView2 tvUserEmail;
    @View(R.id.llFollowersH)
    private LinearLayout llFollowers;
    @View(R.id.llFollowingH)
    private LinearLayout llFollowing;
    @View(R.id.tvUserNameH)
    private TextView tvUserNameH;
    @View(R.id.tv_NumberFollowers)
    private TextView tv_NumberFollowers;
    @View(R.id.tv_NumberFollowing)
    private TextView tv_NumberFollowing;
    private OnItemClickListener onItemClickListener;
    private long DRAWER_CLOSE_TIME = 160;

    public DrawerHeader(Context mContext, DrawerCallBack mCallBack,  OnItemClickListener onItemClickListener) {
        this.mContext = mContext;
        this.mCallBack = mCallBack;
        this.onItemClickListener = onItemClickListener;
    }


    @Resolve
    private void onResolved() {
        ivUser1.setOnClickListener(this);
        ivUser.setOnClickListener(this);
        tvUserName.setOnClickListener(this);
        tvUserEmail.setOnClickListener(this);
        llFollowers.setOnClickListener(this);
        llFollowing.setOnClickListener(this);


    }


    public void setDrawerCallBack(DrawerHeader.DrawerCallBack callBack) {
        mCallBack = callBack;
    }

    @Override
    public void onClick(android.view.View v) {
        onItemClickListener.onItemClick(null,0);
        switch (v.getId()) {
            case R.id.ivUserImageH1:
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        ActivityController.startActivity(mContext, MyProfileActivity.class);
                    }
                }, DRAWER_CLOSE_TIME);

                break;
            case R.id.ivUserImageH:
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ActivityController.startActivity(mContext, MyProfileActivity.class);
                    }
                }, DRAWER_CLOSE_TIME);

                break;
            case R.id.tvUserNameH:
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ActivityController.startActivity(mContext, MyProfileActivity.class);
                    }
                }, DRAWER_CLOSE_TIME);

                break;
            case R.id.tvUserEmailH:


                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ActivityController.startActivity(mContext, MyProfileActivity.class);
                    }
                }, DRAWER_CLOSE_TIME);

                break;
            case R.id.llFollowersH:

                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ActivityController.startActivity(mContext, SearchFollowersActivity.class);
                    }
                }, DRAWER_CLOSE_TIME);
                break;
            case R.id.llFollowingH:

                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ActivityController.startActivity(mContext, SearchFollowingActivity.class);
                    }
                }, DRAWER_CLOSE_TIME);
                break;
            default:
                break;
        }

    }

    public void setUserData(UserModel userData) {
        tvUserName.setText(userData.getUser_name());
        tvUserNameH.setText(userData.getName());
        tvUserEmail.setText(userData.getUser_name());

        if(!TextUtils.isEmpty(userData.getImage())) {
            Picasso.with(mContext).load(userData.getImage()).placeholder(R.drawable.user_placeholder).
                    transform(new CircleTransformation()).into(ivUser1);

        }

        if(userData.getAvatar_img()!=null&&userData.getAvatar_img().getAvatar_image()!=null&&!TextUtils.isEmpty(userData.getAvatar_img().getAvatar_image())) {
            Picasso.with(mContext).load(userData.getAvatar_img().getAvatar_image()).placeholder(R.drawable.user_placeholder).
                    transform(new CircleTransformation()).into(ivUser);

        }
        tv_NumberFollowing.setText(""+(userData.getFollow() == 0 ? 0 : userData.getFollow() ));
        tv_NumberFollowers.setText(""+(userData.getFollower() == 0 ? 0 : userData.getFollower() ));

    }

    public interface DrawerCallBack {


    }
}