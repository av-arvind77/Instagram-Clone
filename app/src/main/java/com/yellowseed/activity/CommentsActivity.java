package com.yellowseed.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.JsonObject;
import com.yellowseed.R;
import com.yellowseed.adapter.CommentsAdapter;

import com.yellowseed.databinding.ActivityCommentsBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.listener.inlineReplyClickListener;
import com.yellowseed.model.CommentsModel;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.LogUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;
import com.yellowseed.webservices.ApiResponse;
import com.yellowseed.webservices.response.CommentLikeDislike.CommentLikeDislike;
import com.yellowseed.webservices.response.CommentLikeDislike.Like;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class CommentsActivity extends BaseActivity  {
    private ActivityCommentsBinding binding;
    private Context mContext;
    private Activity activity;
 //   private ArrayList<ApiResponse.CommentArrBean> comments = new ArrayList<>();
    private CommentsAdapter commentsAdapter;
    private String postId;
    private String sharedId;
    private boolean inlineCommentStatus=false;

    private ArrayList<CommentsModel> comments = new ArrayList<>();
    private String[] userNames = {"John Thomas", "Jenny Koul", "Mike Keel", "Tiny Tim", "Julie Kite", "Mike Keel", "Tiny Time"};
    private String[] commentTimes = {"1h", "2h", "1h", "2h", "1h", "2h", "3h"};
    private String[] userCommmentss = {"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do.", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do.", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do.", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do.", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do.", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do.", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do."};
    private int userPictures = R.mipmap.profile_icon3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_comments);
        mContext = CommentsActivity.this;
        getIntentData();
        initializedControl();
        setToolBar();
        setOnClickListener();
        activity=CommentsActivity.this;
/*        if(CommonUtils.isOnline(mContext))
            callAllCommentUserAPI(postId,sharedId);
        else
            ToastUtils.showToastShort(mContext,mContext.getString(R.string.internet_alert_msg));*/
    }

    private void getIntentData() {
        if (getIntent() != null && getIntent().getExtras() != null && getIntent().getStringExtra(AppConstants.POST_ID) != null){
            postId = getIntent().getStringExtra(AppConstants.POST_ID);
            sharedId = getIntent().getStringExtra(AppConstants.SHARED_ID);
            CommonUtils.savePreferencesString(mContext,AppConstants.POST_ID,postId);
        }
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CommonUtils.hideSoftKeyboard(activity);
        inlineCommentStatus=false;
        CommonUtils.savePreferencesString(mContext,AppConstants.POST_ID,postId);
    }

    @Override
    public void initializedControl() {
        setCurrentTheme();

        //Comments Adapter==========================================================================
     /*   binding.rvCommentsContainer.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvCommentsContainer.setLayoutManager(layoutManager);
        commentsAdapter = new CommentsAdapter("", postId,activity,comments, this, new inlineReplyClickListener() {
            @Override
            public void onItemClick(View view, int position,String commentId) {
                switch (view.getId()) {

                    case R.id.tvReplyComments:
                        CommonUtils.showSoftKeyboard(activity);
                        binding.commentText.setFocusable(true);
                        inlineCommentStatus=true;
                        CommonUtils.savePreferencesString(mContext,AppConstants.COMMENT_ID,comments.get(position).getComment_id());
                        ToastUtils.showToastShort(mContext, getString(R.string.reply));
                        break;
                    default:
                        break;
                }
            }
        });*/
       /* binding.rvCommentsContainer.setAdapter(commentsAdapter);
        commentsAdapter.notifyItemRangeChanged(0, commentsAdapter.getItemCount());*/



        binding.rvCommentsContainer.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvCommentsContainer.setLayoutManager(layoutManager);

        comments.addAll(prepareData());

        commentsAdapter = new CommentsAdapter(comments, mContext, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.tvReplyComments:

                        ToastUtils.showToastShort(mContext, "Reply");
                        break;
                    case R.id.tvLikeComments:

                        ToastUtils.showToastShort(mContext, "Like");
                        break;
                    case R.id.tvDislikeComments:
                        ToastUtils.showToastShort(mContext, "Dislike");
                        break;
                    default:
                        break;
                }
            }
        });


        binding.rvCommentsContainer.setAdapter(commentsAdapter);
        commentsAdapter.notifyItemRangeChanged(0, commentsAdapter.getItemCount());


    }



    private ArrayList<CommentsModel> prepareData() {
        ArrayList<CommentsModel> comments = new ArrayList<>();
        for (int i = 0; i < userNames.length; i++) {
            CommentsModel comment = new CommentsModel();
            comment.setUserName(userNames[i]);
            comment.setCommentTime(commentTimes[i]);
            comment.setUserComment(String.valueOf(userCommmentss[i]));
            comment.setUserPicture(userPictures);
            comments.add(comment);
        }
        return comments;

    }




    private void setCurrentTheme() {
        Themes themes = new Themes(mContext);
        themes.changeIconColor(mContext, binding.toolbarComments.ivLeft);
        binding.viewLine.setBackgroundColor(ContextCompat.getColor(mContext,Themes.getInstance(mContext).setViewLineGrey()));

        binding.llCommentMain.setBackgroundColor(ContextCompat.getColor(mContext,Themes.getInstance(mContext).setDarkTheme()));
        binding.toolbarComments.toolbarMain.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkTheme()));
        binding.toolbarComments.tvHeader.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
      //  binding.llCommentView.setBackgroundColor(ContextCompat.getColor(mContext,Themes.getInstance(mContext).setDarkGreyBackground()));
        binding.commentText.setBackground(ContextCompat.getDrawable(mContext,Themes.getInstance(mContext).setDarkRoundDrawable()));
        binding.commentText.setTextColor(ContextCompat.getColor(mContext,Themes.getInstance(mContext).setDarkThemeText()));
        binding.commentText.setHintTextColor(ContextCompat.getColor(mContext,themes.setDarkEditProfileHint()));
        themes.changeIconWhite(mContext, binding.ivMike);
        binding.llCommentView.setBackground(ContextCompat.getDrawable(mContext,Themes.getInstance(mContext).setDarkComment()));



    }


    /**
     * LikeCommentApi
     */
  /*  private void callLikeCommentApi(String comment_id, String like_dislike_status,boolean status) {
        if(CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("comment_id",comment_id);
            jsonObject.addProperty("status",status);
            jsonObject.addProperty("do",like_dislike_status);

            Call<CommentLikeDislike> call = ApiExecutor.getClient(mContext).commentLikeDislike(jsonObject);
            call.enqueue(new retrofit2.Callback<CommentLikeDislike>() {
                @Override
                public void onResponse(@NonNull Call<CommentLikeDislike> call, @NonNull final Response<CommentLikeDislike> response) {
                    progressDialog.dismiss();
                    CommonUtils.errorLog("response",""+response.message());
                    try {
                        if (response.body()!= null && response.body().getResponseCode() == 200) {
                        *//*    likeDislikeList.clear();
                            likeDislikeList.addAll(response.body().getLike());
                            commentsAdapter.notifyDataSetChanged();*//*
                                callAllCommentUserAPI(postId,sharedId);

                        } 
                        else
                        {
                            ToastUtils.showToastShort(mContext,mContext.getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CommentLikeDislike> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    LogUtils.errorLog(TAG, "on failure -> " + ((t != null && t.getMessage() != null) ? t.getMessage() : "---"));
                    ToastUtils.showToastShort(mContext,mContext.getString(R.string.server_error_msg));
                }
            });
        }
        else
        {
            ToastUtils.showToastShort(mContext,mContext.getString(R.string.internet_alert_msg));
        }
    }*/

    @Override
    public void setToolBar() {
        binding.toolbarComments.ivLeft.setVisibility(View.VISIBLE);
        binding.toolbarComments.ivLeft.setImageResource(R.mipmap.back_icon);
        binding.toolbarComments.tvHeader.setVisibility(View.VISIBLE);
        binding.toolbarComments.tvHeader.setText(R.string.comments);
        binding.toolbarComments.ivRight.setVisibility(View.GONE);
    }

    @Override
    public void setOnClickListener() {
        binding.toolbarComments.ivLeft.setOnClickListener(this);
        binding.commentButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
          /*  case R.id.ivLeft:
                finish();
                break;
            case R.id.comment_button:
                if (binding.commentText.getText().toString().trim().isEmpty()) {
                    return;
                } else {

                    if(CommonUtils.isOnline(mContext)) {
                        callCreateCommentAPI(postId,sharedId, binding.commentText.getText().toString().trim());
                    }
                    else
                    {
                        ToastUtils.showToastShort(mContext,mContext.getString(R.string.internet_alert_msg));
                    }

                }
                break;*/





            case R.id.ivLeft:
                finish();
                break;
            case R.id.comment_button:
                if (binding.commentText.getText().toString().trim().isEmpty()) {

                    return;
                } else {

                    CommentsModel commentsModel = new CommentsModel();
                    commentsModel.setUserName("James Peter");
                    commentsModel.setUserPicture(R.mipmap.profile_icon2);
                    commentsModel.setCommentTime("1 min");
                    commentsModel.setUserComment(binding.commentText.getText().toString().trim());
                    comments.add(commentsModel);
                    commentsAdapter.notifyDataSetChanged();
                    binding.commentText.setText("");
                    binding.rvCommentsContainer.post(new Runnable() {
                        @Override
                        public void run() {
                            // Call smooth scroll
                            binding.rvCommentsContainer.smoothScrollToPosition(commentsAdapter.getItemCount());
                        }
                    });
                }
                break;


            default:
                break;
        }
    }

    /**
     * method for All Comment api
     */
/*    private void callAllCommentUserAPI(String postId,String sharedId) {
        if(CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();

            JsonObject jsonObject = new JsonObject();
            if(sharedId!=null && !sharedId.equalsIgnoreCase("")){
                jsonObject.addProperty("shared_id",sharedId);
            }else {
                jsonObject.addProperty("post_id",postId);
            }

            jsonObject.addProperty("page","1");

            Call<ApiResponse> call = ApiExecutor.getClient(mContext).apiAllCommentUser(jsonObject);
            call.enqueue(new retrofit2.Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            comments.clear();
                            comments.addAll(response.body().getComment_arr());
                            initializedControl();
                            commentsAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            ToastUtils.showToastShort(mContext,mContext.getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(mContext,mContext.getString(R.string.server_error_msg));
                }
            });
        }
        else
        {
            ToastUtils.showToastShort(mContext,mContext.getString(R.string.internet_alert_msg));
        }
    }*/

    /**
     * method for create story api
     */
 /*   private void callCreateCommentAPI(final String postId,final String sharedId, String comment) {
        if(CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();

            JsonObject jsonObject = new JsonObject();

            if(inlineCommentStatus) {
                jsonObject.addProperty("comment_id", CommonUtils.getPreferencesString(mContext, AppConstants.COMMENT_ID));
                inlineCommentStatus=false;
            }else {
                if(sharedId!=null && !sharedId.equalsIgnoreCase("")){
                    jsonObject.addProperty("shared_id",sharedId);

                }else {
                  //  jsonObject.addProperty("post_id", CommonUtils.getPreferencesString(mContext,AppConstants.POST_ID));
                    jsonObject.addProperty("post_id",postId );
                }

            }
            jsonObject.addProperty("comment",comment);

            Call<ApiResponse> call = ApiExecutor.getClient(mContext).apiCommentCreate(jsonObject);
            call.enqueue(new retrofit2.Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            callAllCommentUserAPI(postId,sharedId);
                            binding.commentText.setText("");
                            binding.rvCommentsContainer.post(new Runnable() {
                                @Override
                                public void run() {
                                    // Call smooth scroll
                                    binding.rvCommentsContainer.smoothScrollToPosition(commentsAdapter.getItemCount());
                                }
                            });
                        }
                        else
                        {
                            ToastUtils.showToastShort(mContext,mContext.getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(mContext,mContext.getString(R.string.server_error_msg));
                }
            });
        }
        else
        {
            ToastUtils.showToastShort(mContext,mContext.getString(R.string.internet_alert_msg));
        }
    }*/
/*
    @Override
    public void onItemClick(View view, int position, String commentId) {
        inlineCommentStatus=true;
        CommonUtils.savePreferencesString(mContext,AppConstants.COMMENT_ID,commentId);
    }*/
}
