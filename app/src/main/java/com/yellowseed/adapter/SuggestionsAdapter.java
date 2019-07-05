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
import com.yellowseed.databinding.CustomSuggetionsLayoutBinding;
import com.yellowseed.model.MediaClickModel;
import com.yellowseed.model.UserListModel;
import com.yellowseed.model.resModel.SuggestionListModel;
import com.yellowseed.utils.AppConstants;
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

public class SuggestionsAdapter extends RecyclerView.Adapter<SuggestionsAdapter.ListViewHolder> {


    ArrayList<SuggestionListModel> userListModels;
    private Context context;
    private String isFrom;

    public SuggestionsAdapter(ArrayList<SuggestionListModel> userListModels, Context context, String isFrom) {
        this.userListModels = userListModels;
        this.context = context;
        this.isFrom = isFrom;
    }


    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_suggetions_layout, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListViewHolder holder, final int position) {
//    holder.binding.ivSuggestions.setImageResource(arrayList.get(position).getImage());


        SuggestionListModel model = userListModels.get(position);
        if (model != null) {
            holder.binding.ivSuggestions.setImageResource(model.getImg());
            holder.binding.tvSuggestionsName.setText(model.getName());
            holder.binding.tvSuggestionsFollowers.setText(model.getMutual()+" Mutual Friend");

        }


        //    holder.binding.tvSuggestionsName.setText(userListModels.get(position).getName()!=null?userListModels.get(position).getName():"");
        //  holder.binding.tvSuggestionsFollowers.setText(String.valueOf(userListModels.get(position).getMutual_follower()));
        holder.binding.tvUserImage.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));

        /*    CommonUtils.otherUserProfile(context, holder.binding.ivSuggestions, userListModels.get(position).getImage(), holder.binding.tvUserImage,
                    userListModels.get(position).getName());
*/

        if (isFrom != null) {
            holder.binding.llMain.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
            holder.binding.tvSuggestionsName.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
            holder.binding.tvSuggestionsFollowers.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setGreyHint()));
            holder.binding.btnFollowSuggestions.setBackground(ContextCompat.getDrawable(context, Themes.getInstance(context).setFollowButtonDrawble()));


        }

        if (userListModels.get(position).getMutual() != null && userListModels.get(position).getMutual().length() > 0) {
            switch (userListModels.get(position).getMutual()) {
                case "0":
                    holder.binding.btnFollowSuggestions.setText("Follow");
                    if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
                        holder.binding.btnFollowSuggestions.setBackground(ContextCompat.getDrawable(context, Themes.getInstance(context).setFollowButtonDrawble()));
                    }

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
                    holder.binding.btnFollowSuggestions.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_home_edittext));

                    break;
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
                                .title("Are you sure you want to Unfollow " + userListModels.get(position).getName() + " ?").titleGravity(GravityEnum.CENTER)
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
                                                UnFollowRequest userFollow = new UnFollowRequest();
                                                UserFollow follow = new UserFollow();
                                                follow.setAction("unfollow");
                                                // follow.setId(userListModels.get(holder.getAdapterPosition()).getId());
                                                userFollow.setUser(follow);
                                                //    apiUnfollowUser(userFollow, holder.getAdapterPosition());
                                                dialog.dismiss();
                                                break;
                                        }
                                    }
                                }).show();


                        break;
                    case "Follow":
                        final CharSequence[] items1 = {Html.fromHtml("<font color = '#FC2B2B'>Follow</font>"), Html.fromHtml("<font color = '#FC2B2B'>Cancel</font>")};
                        new MaterialDialog.Builder(context)
                                .title("Are you sure you want to Follow " + userListModels.get(position).getName() + " ?").titleGravity(GravityEnum.CENTER)
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
                                                userListModels.get(holder.getAdapterPosition()).setMutual("2");
                                                notifyItemChanged(holder.getAdapterPosition());
                                                // UnFollowRequest userFollow = new UnFollowRequest();
                                                // UserFollow follow = new UserFollow();
                                                //  follow.setId(userListModels.get(holder.getAdapterPosition()).getId());
                                                // userFollow.setUser(follow);
                                                //  apiFollowUser(userFollow, holder.getAdapterPosition());
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
        return userListModels.size();
    }

  /*  private void apiFollowUser(final UnFollowRequest model1, final int position) {
        if (CommonUtils.isOnline(context)) {
            if (isFrom != null) {

            }
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();
            Call<ApiResponse> call = ApiExecutor.getClient(context).apiFollowUser(model1);
            call.enqueue(new retrofit2.Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            userListModels.get(position).setIs_follow("2");
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
                            userListModels.get(pos).setIs_follow("0");
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
    }*/

    public class ListViewHolder extends RecyclerView.ViewHolder {

        public CustomSuggetionsLayoutBinding binding;

        public ListViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }


}
