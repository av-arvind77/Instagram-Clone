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

import com.yellowseed.R;
import com.yellowseed.activity.StoriesViewActivity;
import com.yellowseed.activity.StoryRunningActivity;
import com.yellowseed.databinding.LayoutStoriesBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.UserListModel;
import com.yellowseed.model.resModel.StoryListModel;
import com.yellowseed.utils.ActivityController;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.Themes;

import java.util.ArrayList;
import java.util.List;

public class HomeStoriesAdapter extends RecyclerView.Adapter<HomeStoriesAdapter.MyViewHolder> {
    private List<StoryListModel.StoriesBean> homeStoriesModels;
    private List<StoryListModel>storyListModels;
    private Context context;
    private boolean isShownMyProfile;
    private StoryListModel.StoriesBean homeStoriesModel;
    private Themes themes;
    private Boolean darkThemeEnabled;


    public HomeStoriesAdapter(Context context, List<StoryListModel.StoriesBean> storieImage, boolean isShownMyProfile) {
        this.context = context;
        this.homeStoriesModels = storieImage;
        this.isShownMyProfile = false;
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
        //   holder.binding.llStoryHome.setBackgroundColor(themes.setDarkThemeStory());
        holder.binding.tvUserStoriesName.setTextColor(themes.setGreyHint());


        /*homeStoriesModel = homeStoriesModels.get(position);
        if (homeStoriesModel.getStory() != null && homeStoriesModel.getStory().getStory_image().getImg() != null &&
                !homeStoriesModel.getStory().getStory_image().getImg().equalsIgnoreCase("")) {
            if (homeStoriesModel.getStory().getStory_image().getImg().endsWith(".mp4")) {
                Picasso.with(context).load(homeStoriesModel.getStory().getStory_image().getImg().replace(".mp4", "jpg")).transform(new CircleTransformation()).into(holder.binding.ivUserStories);
            } else {
                Picasso.with(context).load(homeStoriesModel.getStory().getStory_image().getImg()).transform(new CircleTransformation()).into(holder.binding.ivUserStories);

            }
        }*/


        holder.binding.tvUserStoriesName.setTextColor(ContextCompat.getColor(context, themes.setDarkGreyTextColor()));

//        holder.binding.ivUserStories.setImageResource(homeStoriesModel.getStory().getStory_image());
       /* if (homeStoriesModel.getUser() != null && homeStoriesModel.getUser().getName() != null) {
            holder.binding.tvUserStoriesName.setText(homeStoriesModel.getUser().getName());
        }*/

        holder.binding.ivUserStories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isShownMyProfile /*&& position == 0)*/) {
                    Intent intent = new Intent(context, StoriesViewActivity.class);
                    intent.putExtra(AppConstants.USER_ID, "");
                    //       intent.putExtra(AppConstants.USER_ID, homeStoriesModels.get(position).getUser().getId());
                    context.startActivity(intent);
//                    ActivityController.startActivity(context, StoriesViewActivity.class);
                } else {
                    ActivityController.startActivity(context, StoryRunningActivity.class);
                }
            }
        });
        holder.binding.tvUserStoriesName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isShownMyProfile /*&& position == 0)*/) {
                    Intent intent = new Intent(context, StoriesViewActivity.class);
                    intent.putExtra(AppConstants.USER_ID, "");
                    // intent.putExtra(AppConstants.USER_ID, homeStoriesModels.get(position).getUser().getId());
                    context.startActivity(intent);
//                    ActivityController.startActivity(context, StoriesViewActivity.class);
                } else {
                    ActivityController.startActivity(context, StoryRunningActivity.class);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if (homeStoriesModels != null && homeStoriesModels.size() > 0)
            return homeStoriesModels.size();
        else
            return 10;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private LayoutStoriesBinding binding;

        public MyViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
