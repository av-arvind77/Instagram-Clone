package com.yellowseed.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.yellowseed.R;
import com.yellowseed.activity.MyProfileActivity;
import com.yellowseed.databinding.LayoutfollowingacceptBinding;
import com.yellowseed.imageUtils.CircleTransformation;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.FollowingRequestModel;
import com.yellowseed.model.UserListModel;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.webservices.response.User;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ListViewHolder> {

    private ArrayList<UserListModel> userList;
    private Context context;
    private OnItemClickListener listener;
    private Themes themes;
    private Boolean darkThemeEnabled;

    public UserListAdapter(Context context, ArrayList<UserListModel> arrayList, OnItemClickListener listener) {
        this.userList = arrayList;
        this.context = context;
        themes = new Themes(context);
        this.listener = listener;
        darkThemeEnabled = CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME);
    }

    @NonNull
    @Override
    public UserListAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layoutfollowingaccept, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserListAdapter.ListViewHolder holder, final int position) {

        holder.binding.tvFollowingAccept.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        holder.binding.tvUserImage.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));

        /*if (userList.get(position).getName() != null && userList.get(position).getName().length() > 0) {
            holder.binding.tvFollowingAccept.setText(userList.get(position).getName());
        }
        if (userList.get(position).getMutual_follower() > 0) {
            holder.binding.tvMutualFollowers.setVisibility(View.VISIBLE);
            holder.binding.tvMutualFollowers.setText(userList.get(position).getMutual_follower() + " mutual followers");
        } else {
            holder.binding.tvMutualFollowers.setVisibility(View.GONE);
        }*/
        holder.binding.llRejectAccept.setVisibility(View.GONE);
      /*  if (userList.get(position).getImage() != null && !userList.get(position).getImage().equalsIgnoreCase("")) {
            Picasso.with(context).load(userList.get(position).getImage()).transform(new CircleTransformation()).into(holder.binding.ivFollowingRequestAccept);
        } else {
            //  Picasso.with(context).load(R.drawable.user_placeholder).transform(new CircleTransformation()).into(holder.binding.ivFollowingRequestAccept);


            CommonUtils.otherUserProfile(context, holder.binding.ivFollowingRequestAccept, userList.get(position).getImage(), holder.binding.tvUserImage,
                    userList.get(position).getName());

        }*/
        holder.binding.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MyProfileActivity.class);
                intent.putExtra(AppConstants.USER_ID, "");
              //  intent.putExtra(AppConstants.USER_ID, userList.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        private LayoutfollowingacceptBinding binding;

        public ListViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
