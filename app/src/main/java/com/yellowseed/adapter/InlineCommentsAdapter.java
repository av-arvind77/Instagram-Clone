package com.yellowseed.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.gson.JsonObject;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.yellowseed.R;
import com.yellowseed.databinding.RowInlineCommentsBinding;
import com.yellowseed.imageUtils.CircleTransformation;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.listener.inlineReplyClickListener;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;
import com.yellowseed.webservices.ApiResponse;
import com.yellowseed.webservices.response.CommentLikeDislike.CommentLikeDislike;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by rajat_mobiloitte on 29/6/18.
 */

public class InlineCommentsAdapter extends RecyclerView.Adapter<InlineCommentsAdapter.InlineCommentViewHolder> {

    private List<ApiResponse.CommentArrBean> comments;
    private Context mContext;
    private inlineReplyClickListener listener;
    private String postId,mainId;


    public InlineCommentsAdapter(String postId, List<ApiResponse.CommentArrBean> comments, String id, Context context, inlineReplyClickListener listener) {
        this.comments = comments;
        this.mContext = context;
        this.listener = listener;
        this.postId=postId;
        this.mainId=id;

    }

    @NonNull
    @Override
    public InlineCommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_inline_comments, parent, false);
        return new InlineCommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final InlineCommentsAdapter.InlineCommentViewHolder holder, final int position) {

        String strDate = "Not availbale";
        SimpleDateFormat readDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        readDate.setTimeZone(TimeZone.getTimeZone("GMT")); // missing line
        Date date = null;

        try {
            date = readDate.parse(comments.get(position).getCreated_at());
            long commentDate = date.getTime();
            long timeElapsed = timeElapsedFromDate(commentDate);
            if (timeElapsed == commentDate) {
                SimpleDateFormat writeDate = new SimpleDateFormat("hh:mm a");
                writeDate.setTimeZone(TimeZone.getDefault());
                strDate = writeDate.format(date);
            } else {
                strDate = "" + String.valueOf(timeElapsed) + CommonUtils.getPreferencesString(mContext, AppConstants.TIME_ELAPSED_STRING);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (comments.get(position).getUserX().getImage() != null && !comments.get(position).getUserX().getImage().equalsIgnoreCase("")){
            Picasso.with(mContext).load(comments.get(position).getUserX().getImage()).
                    transform(new CircleTransformation()).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.binding.ivInlineCommentorProfilePic);
        }else {
            /*Picasso.with(mContext).load(R.drawable.user_placeholder).
                    transform(new CircleTransformation()).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.binding.ivInlineCommentorProfilePic);
*/

            CommonUtils.otherUserProfile(mContext,holder.binding.ivInlineCommentorProfilePic,comments.get(position).getUserX().getImage(),holder.binding.tvUserImage,
                    comments.get(position).getUserX().getName());
        }
        holder.binding.tvInlineCommentorCommentTime.setText(strDate);
        holder.binding.tvInlineComment.setText(comments.get(position).getComment());
        holder.binding.tvInlineCommentorName.setTextColor(ContextCompat.getColor(mContext,Themes.getInstance(mContext).setDarkThemeText()));
        holder.binding.tvInlineComment.setTextColor(ContextCompat.getColor(mContext,Themes.getInstance(mContext).setDarkThemeText()));
        holder.binding.tvUserImage.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));

        holder.binding.tvInlineCommentorName.setText(comments.get(position).getUserX().getName());
        if (comments.get(position).getLikes_count() > 0){
          //  holder.binding.tvLikeComments.setTextColor(mContext.getResources().getColor(R.color.green_color_picker));
            holder.binding.tvLikeComments.setText(""+comments.get(position).getLikes_count());
            holder.binding.tvLikeMsg.setText(R.string.like_underline);
         //  holder.binding.tvLikeComments.setPaintFlags(holder.binding.tvLikeComments.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        }
        else {
         //   holder.binding.tvLikeComments.setTextColor(mContext.getResources().getColor(R.color.grey));
            holder.binding.tvLikeComments.setText(""+comments.get(position).getLikes_count());
            holder.binding.tvLikeMsg.setText(R.string.like_underline);


        }

        if (comments.get(position).getDislikes_count() > 0){
         //   holder.binding.tvDislikeComments.setTextColor(mContext.getResources().getColor(R.color.red));
            holder.binding.tvDislikeComments.setText(""+ comments.get(position).getDislikes_count());
            holder.binding.tvDislike.setText(R.string.dislike_underLine);

            // holder.binding.tvDislikeComments.setPaintFlags(holder.binding.tvDislikeComments.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        }else {
         //   holder.binding.tvDislikeComments.setTextColor(mContext.getResources().getColor(R.color.grey));
            holder.binding.tvDislikeComments.setText(  ""+comments.get(position).getDislikes_count());
            holder.binding.tvDislike.setText(R.string.dislike_underLine);

        }

        if (comments.get(position).getUserX().getImage() != null && !comments.get(position).getUserX().getImage().equalsIgnoreCase("")) {
            Picasso.with(mContext).load(comments.get(position).getUserX().getImage()).transform(new CircleTransformation()).
                    into(holder.binding.ivInlineCommentorProfilePic);
        }


        holder.binding.tvReplyComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.tvReplyComments, holder.getAdapterPosition(),postId);
            }
        });
        holder.binding.llLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(comments.get(position).getLikes_count()>0){
                    if(CommonUtils.isOnline(mContext)){
                        callLikeCommentApi(comments.get(position),"like",false);
                    }else {
                        ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
                    }
                }else {
                    if(CommonUtils.isOnline(mContext)){
                        callLikeCommentApi(comments.get(position),"like",true);
                    }else {
                        ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
                    }
                }

            }
        });
        holder.binding.llDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(comments.get(position).getDislikes_count()>0){
                    if(CommonUtils.isOnline(mContext)){
                        callLikeCommentApi(comments.get(position),"dislike",false);
                    }else {
                        ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
                    }
                }else {
                    if(CommonUtils.isOnline(mContext)){
                        callLikeCommentApi(comments.get(position),"dislike",true);
                    }else {
                        ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
                    }
                }
            }
        });







    }

    /**
     * LikeCommentApi
     */
    private void callLikeCommentApi(final ApiResponse.CommentArrBean commentArrBean, final String like_dislike_status, final boolean status) {
        if(CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("comment_id",commentArrBean.getComment_id());
            jsonObject.addProperty("status",status);
            jsonObject.addProperty("do",like_dislike_status);

            Call<CommentLikeDislike> call = ApiExecutor.getClient(mContext).commentLikeDislike(jsonObject);
            call.enqueue(new retrofit2.Callback<CommentLikeDislike>() {
                @Override
                public void onResponse(@NonNull Call<CommentLikeDislike> call, @NonNull final Response<CommentLikeDislike> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            ToastUtils.showToastShort(mContext,response.message());
                            switch (like_dislike_status){
                                case "like":
                                    if(status){
                                        commentArrBean.setLikes_count(1);
                                        commentArrBean.setDislikes_count(0);
                                    }else {
                                        commentArrBean.setLikes_count(0);
                                    }
                                    break;
                                case "dislike":
                                    if(status){
                                        commentArrBean.setLikes_count(0);
                                        commentArrBean.setDislikes_count(1);
                                    }else {
                                        commentArrBean.setDislikes_count(0);
                                    }
                                    break;
                            }
                            notifyDataSetChanged();
                            //callAllCommentUserAPI(mainId);
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
                    t.printStackTrace();
                    ToastUtils.showToastShort(mContext,mContext.getString(R.string.server_error_msg));
                }
            });
        }
        else
        {
            ToastUtils.showToastShort(mContext,mContext.getString(R.string.internet_alert_msg));
        }
    }


    private void spannable() {


    }





    /**
     * method for create story api
     */
    private void callAllCommentUserAPI(String postId) {
        if(CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("post_id",postId);
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
                            notifyDataSetChanged();
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
    }



    public long timeElapsedFromDate(long commentDate) {
        long timeElapsed = 0L;
        long cdLongValue = 0L;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        String gmtTime = df.format(new Date());

        Date currentDate = null;
        try {
            currentDate = df.parse(gmtTime);
            cdLongValue = currentDate.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }


        long diff = cdLongValue - commentDate;
        long diffSeconds = diff / 1000;
        long diffMinutes = diffSeconds / 60;
        long diffHours = diffMinutes / 60;
        long diffDays = diffHours / 24;
        long diffWeeks = diffDays / 7;
        long diffMonth = diffDays / 30;
        long diffYear = diffDays / 365;


        if (diffYear > 0) {
            timeElapsed = diffYear;
            CommonUtils.savePreferencesString(mContext, AppConstants.TIME_ELAPSED_STRING, "y ago");
        } else if (diffYear < 1 && diffMonth > 0) {
            timeElapsed = diffMonth;
            CommonUtils.savePreferencesString(mContext, AppConstants.TIME_ELAPSED_STRING, "m ago");
        } else if (diffYear < 1 && diffMonth < 1 && diffWeeks > 0) {
            timeElapsed = diffWeeks;
            CommonUtils.savePreferencesString(mContext, AppConstants.TIME_ELAPSED_STRING, "w ago");
        } else if (diffYear < 1 && diffMonth < 1 && diffWeeks < 1 && diffDays > 0) {
            timeElapsed = diffDays;
            CommonUtils.savePreferencesString(mContext, AppConstants.TIME_ELAPSED_STRING, "d ago");
        } else if (diffYear < 1 && diffMonth < 1 && diffWeeks < 1 && diffDays < 1 && diffHours > 0) {
            timeElapsed = diffHours;
            CommonUtils.savePreferencesString(mContext, AppConstants.TIME_ELAPSED_STRING, "h ago");
        } else if (diffYear < 1 && diffMonth < 1 && diffWeeks < 1 && diffDays < 1 && diffHours < 1) {
            timeElapsed = commentDate;
        }
        return timeElapsed;

    }


    @Override
    public int getItemCount() {
        return comments.size();
    }


    public class InlineCommentViewHolder extends RecyclerView.ViewHolder {
        private RowInlineCommentsBinding binding;

        public InlineCommentViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}







