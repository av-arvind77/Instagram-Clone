package com.yellowseed.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.yellowseed.R;
import com.yellowseed.activity.RePostActivity;
import com.yellowseed.activity.SearchFollowersActivity;
import com.yellowseed.databinding.DialogShareBinding;
import com.yellowseed.databinding.LayoutSearchFollowingBinding;
import com.yellowseed.databinding.UnfollowDialogBinding;
import com.yellowseed.fragments.SearchFollowerFragment;
import com.yellowseed.imageUtils.CircleTransformation;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.SearchFollowingModel;
import com.yellowseed.model.UserListModel;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;
import com.yellowseed.webservices.ApiResponse;
import com.yellowseed.webservices.requests.UnFollowRequest;
import com.yellowseed.webservices.requests.UserFollow;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class SearchFollowingAdapter extends RecyclerView.Adapter<SearchFollowingAdapter.MyViewHolder> {

    private ArrayList<SearchFollowingModel> arlModel;
    private Context mContext;
    private boolean flag;
    private int pos = -1;
    private OnItemClickListener listener;
    private SearchFollowingModel model;
    private Themes themes;
    private Boolean darkThemeEnabled;
    private Dialog dialog;


    public SearchFollowingAdapter(ArrayList<SearchFollowingModel> arlModel, Context mContext, OnItemClickListener listener) {


        this.arlModel = arlModel;
        this.mContext = mContext;
        this.listener = listener;
        themes = new Themes(mContext);
        darkThemeEnabled = CommonUtils.getPreferencesBoolean(mContext, AppConstants.DARK_THEME);
    }




    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_search_following, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
      /*  holder.binding.tvUserName.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
        holder.binding.tvUserImage.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));

        model = arlModel.get(position);
        holder.binding.tvUserName.setText(TextUtils.isEmpty(model.getName()) ? "" : model.getName());
*//*
        if(!TextUtils.isEmpty(model.getImage()))
        {
            Picasso.with(mContext).load(model.getImage()).placeholder(R.drawable.user_placeholder).
                    transform(new CircleTransformation()).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.binding.ivSearchFollowingUserPic);

        }
        else
        {*//*
            CommonUtils.otherUserProfile(mContext,holder.binding.ivSearchFollowingUserPic,arlModel.get(position).getImage(),holder.binding.tvUserImage,
                    arlModel.get(position).getName());

        holder.binding.llFollowingAnother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.llFollowingAnother, holder.getAdapterPosition());
            }
        });

        if(model.getIs_follow()!=null&&model.getIs_follow().length()>0){
            switch (model.getIs_follow()){
                case "0":
                    holder.binding.btnSearchFollowing.setText("Follow");
                    holder.binding.btnSearchFollowing.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_home_edittext));
                    break;
                case "1":
                    holder.binding.btnSearchFollowing.setText("Following");
                    holder.binding.btnSearchFollowing.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_yellow_soft_corner));
                    break;
                case "2":
                    holder.binding.btnSearchFollowing.setText("Requested");
                    holder.binding.btnSearchFollowing.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_home_edittext));
                    break;
                default:
                    holder.binding.btnSearchFollowing.setText("Follow");
                    holder.binding.btnSearchFollowing.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_home_edittext));
                    break;
            }
        }

        holder.binding.btnSearchFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (holder.binding.btnSearchFollowing.getText().toString()){
                    case "Following":
                            final CharSequence[] items = {Html.fromHtml("<font color = '#FFFFFF'>Unfollow</font>"), Html.fromHtml("<font color = '#DD4040'>Cancel</font>")};
                            // final CharSequence[] items = {getString(R.string.themes), getString(R.string.gallery), getString(R.string.solid_color), getString(R.string.no_wallpaper), getString(R.string.default_wallpaper)};
                            new MaterialDialog.Builder(mContext)
                                    .title("Are you sure you want to Unfollow " + model.getName() + " ?").titleGravity(GravityEnum.CENTER)
                                    .items(items)
                                    .itemsColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()))
                                    .backgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeStory()))
                                    .titleColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()))
                                    .itemsCallback(new MaterialDialog.ListCallback() {
                                        @Override
                                        public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                            UnFollowRequest userFollow = new UnFollowRequest();
                                            UserFollow follow = new UserFollow();
                                            follow.setAction("unfollow");
                                            follow.setId(model.getId());
                                            userFollow.setUser(follow);
                                            apiUnfollowUser(userFollow);
                                            dialog.dismiss();
                                        }
                                    }).show();


                        break;
                    case "Follow":

                        final CharSequence[] items1 = {Html.fromHtml("<font color = '#FC2B2B'>Follow</font>"), Html.fromHtml("<font color = '#FC2B2B'>Cancel</font>")};
                        new MaterialDialog.Builder(mContext)
                                .title("Are you sure you want to Follow "+ model.getName()+" ?").titleGravity(GravityEnum.CENTER)
                                .items(items1)
                                .itemsColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()))
                                .backgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeStory()))
                                .titleColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()))
                                .itemsCallback(new MaterialDialog.ListCallback() {
                                    @Override
                                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                        UnFollowRequest userFollow=new UnFollowRequest();
                                        UserFollow  follow=new UserFollow();
                                        follow.setId(model.getId());
                                        userFollow.setUser(follow);
                                        apiFollowUser(userFollow);
                                        dialog.dismiss();
                                    }
                                }).show();
                        break;
                    case "Requested":
                        CommonUtils.showToast(mContext,"You already sent the requested");
                        break;
                }

            }
        });*/



        holder.binding.tvUserName.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
        holder.binding.tvUserImage.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));

        holder.binding.tvUserName.setText(arlModel.get(position).getName_url());
        holder.binding.ivSearchFollowingUserPic.setImageResource(arlModel.get(position).getImage_url());
        holder.binding.llFollowingAnother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.llFollowingAnother, holder.getAdapterPosition());
            }
        });

        holder.binding.btnSearchFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.binding.btnSearchFollowing.getText().toString().equals("Following")) {

                    UnfollowDialogBinding dialogShareBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.unfollow_dialog, null, false);
                    dialog = DialogUtils.createDialog(mContext, dialogShareBinding.getRoot());
                    WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                    dialogShareBinding.view.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setViewLineGrey()));
                    dialogShareBinding.viewCancel.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setViewLineGrey()));

                    dialogShareBinding.tvCancel.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setCancel()));
                    dialogShareBinding.llMain.setBackground(ContextCompat.getDrawable(mContext, Themes.getInstance(mContext).setDarkStoryBackground()));
                    dialogShareBinding.tvRemoveFollwer.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeGreyText()));
                    dialogShareBinding.tvRemove.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeGreyText()));

                    dialogShareBinding.tvTextappWont.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeGreyText()));
                    dialogShareBinding.tvCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialog.dismiss();
                        }
                    });
                    dialogShareBinding.tvRemove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            holder.binding.btnSearchFollowing.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_yellow_soft_corner));
                            holder.binding.btnSearchFollowing.setText(R.string.following);
                        dialog.dismiss();
                        }
                    });

                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.show();
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.background_dark);


                }



/*
                final CharSequence[] items = {Html.fromHtml("<font color = '#FC2B2B'>Unfollow</font>"), Html.fromHtml("<font color = '#FC2B2B'>Cancel</font>")};
                    // final CharSequence[] items = {getString(R.string.themes), getString(R.string.gallery), getString(R.string.solid_color), getString(R.string.no_wallpaper), getString(R.string.default_wallpaper)};
                    new MaterialDialog.Builder(mContext)
                            .title("Are you sure you want to Unfollow Jhon Thomas?").titleGravity(GravityEnum.CENTER)
                            .items(items)
                            .itemsColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()))
                            .backgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeStory()))
                            .titleColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()))

                            .itemsCallback(new MaterialDialog.ListCallback() {
                                @Override
                                public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {

                                    if (position == 0) {
                                        holder.binding.btnSearchFollowing.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_home_edittext));
                                        holder.binding.btnSearchFollowing.setText(R.string.follow);
                                    } else if (position == 1) {
                                        dialog.dismiss();
                                    } else {
                                        holder.binding.btnSearchFollowing.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_yellow_soft_corner));
                                        holder.binding.btnSearchFollowing.setText(R.string.following);
                                    }
                                }
                            }).show();


                } */else {
                    holder.binding.btnSearchFollowing.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_yellow_soft_corner));
                    holder.binding.btnSearchFollowing.setText(R.string.following);
                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return arlModel.size();
    }

    public void updatedList(ArrayList<SearchFollowingModel> filteresNames) {
        arlModel = filteresNames;
        notifyDataSetChanged();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private LayoutSearchFollowingBinding binding;

        public MyViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    /*private void apiFollowUser(final UnFollowRequest model1) {
        if(CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();
            Call<ApiResponse> call = ApiExecutor.getClient(mContext).apiFollowUser(model1);
            call.enqueue(new retrofit2.Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            model.setIs_follow("2");
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
    }*/
   /* private void apiUnfollowUser(final UnFollowRequest model1) {
        if(CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();

            Call<ApiResponse> call = ApiExecutor.getClient(mContext).apiUnFollowUser(model1);
            call.enqueue(new retrofit2.Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            model.setIs_follow("0");
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
    }*/
}
