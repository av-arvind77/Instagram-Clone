package com.yellowseed.adapter;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.picasso.Picasso;
import com.yellowseed.R;
import com.yellowseed.databinding.RowSuggestionUsersBinding;
import com.yellowseed.model.UserListModel;
import com.yellowseed.utils.CircleTransform;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;
import com.yellowseed.webservices.ApiResponse;
import com.yellowseed.webservices.requests.UnFollowRequest;
import com.yellowseed.webservices.requests.UserFollow;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by mobiloitte on 17/5/18.
 */

public class SuggestionsHomeAdapter extends RecyclerView.Adapter<SuggestionsHomeAdapter.ListViewHolder> {

    private Context context;
    private ArrayList<UserListModel> arlModel;

    public SuggestionsHomeAdapter(ArrayList<UserListModel> arlModel, Context context) {
        this.arlModel = arlModel;
        this.context = context;
    }


    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_suggestion_users, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListViewHolder holder, final int position) {
//    holder.binding.ivSuggestions.setImageResource(arrayList.get(position).getImage());
        holder.binding.tvSuggestionsName.setText(arlModel.get(position).getName() != null ? arlModel.get(position).getName() : "");
        Themes.getInstance(context).changeIconColorToWhite(context, holder.binding.ivCross);
        holder.binding.llContacts.setBackground(ContextCompat.getDrawable(context, Themes.getInstance(context).setLiveUserDarwable()));
        holder.binding.tvSuggestionsName.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));

        holder.binding.tvUserImage.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));

      /*  if (arlModel.get(position).getImage() != null && !arlModel.get(position).getImage().equalsIgnoreCase("")) {
            Picasso.with(context).load(arlModel.get(position).getImage())
                    .transform(new CircleTransform()).error(R.drawable.user_placeholder)
                    .into(holder.binding.ivSuggestions);
        }
        else
            {
                CommonUtils.otherUserProfile(context, holder.binding.ivSuggestions, arlModel.get(position).getImage(), holder.binding.tvUserImage,
                        arlModel.get(position).getName());
            }*/


        if (arlModel != null) {
            if (arlModel.get(holder.getAdapterPosition()).getIs_follow() != null && arlModel.get(holder.getAdapterPosition()).getIs_follow().length() > 0) {
                switch (arlModel.get(position).getIs_follow()) {
                    case "0":
                        holder.binding.btnFollowSuggestions.setText("Follow");
                        holder.binding.btnFollowSuggestions.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_follow_button));
                        break;
                    case "1":
                        holder.binding.btnFollowSuggestions.setText("Following");
                        holder.binding.btnFollowSuggestions.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_yellow_soft_corner));
                        break;
                    case "2":
                        holder.binding.btnFollowSuggestions.setText("Requested");
                        holder.binding.btnFollowSuggestions.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_home_edittext));
                        break;
                    default:
                        holder.binding.btnFollowSuggestions.setText("Follow");
                        holder.binding.btnFollowSuggestions.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_follow_button));
                        break;
                }
            }
        }


        holder.binding.btnFollowSuggestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (holder.binding.btnFollowSuggestions.getText().toString()) {
                    case "Following":
                        final CharSequence[] items = {Html.fromHtml("<font color = '#FFFFFF'>Unfollow</font>"), Html.fromHtml("<font color = '#DD4040'>Cancel</font>")};
                        // final CharSequence[] items = {getString(R.string.themes), getString(R.string.gallery), getString(R.string.solid_color), getString(R.string.no_wallpaper), getString(R.string.default_wallpaper)};
                        new MaterialDialog.Builder(context)
                                .title("Are you sure you want to Unfollow " + arlModel.get(position).getName() + " ?").titleGravity(GravityEnum.CENTER)
                                .items(items)
                                .itemsColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()))
                                .backgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeStory()))
                                .titleColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()))
                                .itemsCallback(new MaterialDialog.ListCallback() {
                                    @Override
                                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {

                                        switch (position) {
                                            case 1:
                                                dialog.dismiss();
                                                break;
                                            case 0:
                                               /* UnFollowRequest userFollow = new UnFollowRequest();
                                                UserFollow follow = new UserFollow();
                                                follow.setAction("unfollow");
                                                follow.setId(arlModel.get(holder.getAdapterPosition()).getId());
                                                userFollow.setUser(follow);
                                                apiUnfollowUser(userFollow, holder.getAdapterPosition());*/
                                                arlModel.get(holder.getAdapterPosition()).setIs_follow("0");
                                                notifyItemChanged(holder.getAdapterPosition());
                                                dialog.dismiss();
                                                break;
                                        }

                                    }
                                }).show();


                        break;
                    case "Follow":
                        final CharSequence[] items1 = {Html.fromHtml("<font color = '#FC2B2B'>Follow</font>"), Html.fromHtml("<font color = '#FC2B2B'>Cancel</font>")};
                        new MaterialDialog.Builder(context)
                                .title("Are you sure you want to Follow " + arlModel.get(position).getName() + " ?").titleGravity(GravityEnum.CENTER)
                                .items(items1)
                                .itemsColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()))
                                .backgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeStory()))
                                .titleColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()))
                                .itemsCallback(new MaterialDialog.ListCallback() {
                                    @Override
                                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {

                                        switch (position) {
                                            case 1:
                                                dialog.dismiss();
                                                break;
                                            case 0:
                                                arlModel.get(holder.getAdapterPosition()).setIs_follow("2");
                                                notifyItemChanged(holder.getAdapterPosition());
                                                /*UnFollowRequest userFollow = new UnFollowRequest();
                                                UserFollow follow = new UserFollow();
                                                follow.setId(arlModel.get(holder.getAdapterPosition()).getId());
                                                userFollow.setUser(follow);
                                                apiFollowUser(userFollow, holder.getAdapterPosition());*/
                                                dialog.dismiss();
                                                break;
                                        }


                                    }
                                }).show();
                        break;
                    case "Requested":
                        CommonUtils.showToast(context, "You already sent the requested");
                        break;
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return arlModel.size();
    }

    private void apiFollowUser(final UnFollowRequest model1, final int position) {
        if (CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();
            Call<ApiResponse> call = ApiExecutor.getClient(context).apiFollowUser(model1);
            call.enqueue(new retrofit2.Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            arlModel.get(position).setIs_follow("2");
                            notifyItemChanged(position);
                        } else {
                            ToastUtils.showToastShort(context, context.getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(context, context.getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(context, context.getString(R.string.internet_alert_msg));
        }
    }

    private void apiUnfollowUser(final UnFollowRequest model1, final int pos) {
        if (CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();

            Call<ApiResponse> call = ApiExecutor.getClient(context).apiUnFollowUser(model1);
            call.enqueue(new retrofit2.Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            arlModel.get(pos).setIs_follow("0");
                            notifyItemChanged(pos);
                        } else {
                            ToastUtils.showToastShort(context, context.getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(context, context.getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(context, context.getString(R.string.internet_alert_msg));
        }
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {

        public RowSuggestionUsersBinding binding;

        public ListViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }


}
