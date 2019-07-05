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
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.yellowseed.R;
import com.yellowseed.activity.MyProfileActivity;
import com.yellowseed.databinding.LayoutStoriesBinding;
import com.yellowseed.imageUtils.CircleTransformation;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.webservices.response.User;

import java.util.ArrayList;

public class TrendingProfileAdapter extends RecyclerView.Adapter<TrendingProfileAdapter.MyViewHolder> {
    private ArrayList<User> arlUserList;
    private Context context;
    private Themes themes;
    private Boolean darkThemeEnabled;

    public TrendingProfileAdapter(Context context, ArrayList<User> storieImage) {
        this.context = context;
        this.arlUserList = storieImage;
        themes = new Themes(context);
        darkThemeEnabled = CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_stories, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.binding.tvUserImage.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));

        holder.binding.tvUserStoriesName.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        int displayWidth = context.getResources().getDisplayMetrics().widthPixels / 4;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(displayWidth, displayWidth);
        holder.binding.llStoryHome.setLayoutParams(layoutParams);
        /*final User user = arlUserList.get(position);
        if (user.getImage() != null &&
                !user.getImage().equalsIgnoreCase("")) {
            Picasso.with(context).load(user.getImage()).transform(new CircleTransformation()).into(holder.binding.ivUserStories);
        }
        else
        {
            CommonUtils.otherUserProfile(context,holder.binding.ivUserStories,arlUserList.get(position).getImage(),holder.binding.tvUserImage,
                    arlUserList.get(position).getName());
        }
        if (user.getName() != null && !user.getName().equalsIgnoreCase("")) {
            holder.binding.tvUserStoriesName.setText(user.getName());
        }*/
        holder.binding.flImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MyProfileActivity.class);
                intent.putExtra(AppConstants.USER_ID, "");
                //intent.putExtra(AppConstants.USER_ID, user.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (arlUserList != null && arlUserList.size() > 0)
            return arlUserList.size();
        else
            return 5;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private LayoutStoriesBinding binding;

        public MyViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
