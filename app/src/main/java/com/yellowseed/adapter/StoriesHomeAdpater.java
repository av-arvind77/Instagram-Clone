package com.yellowseed.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yellowseed.R;
import com.yellowseed.activity.StoriesViewActivity;
import com.yellowseed.activity.StoryRunningActivity;
import com.yellowseed.databinding.LayoutStoriesBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.HomeStoriesModel;
import com.yellowseed.utils.ActivityController;

import java.util.List;

public class StoriesHomeAdpater extends RecyclerView.Adapter<StoriesHomeAdpater.MyViewHolder> {
    private List<HomeStoriesModel> homeStoriesModels;
    private Context context;
    private boolean isShownMyProfile;

    /*public HomeStoriesAdapter(List<HomeStoriesModel> homeStoriesModels, Context context) {
        this.homeStoriesModels = homeStoriesModels;
        this.context = context;
    }*/


    public StoriesHomeAdpater(List<HomeStoriesModel> storyListModels, Context mContext, OnItemClickListener onItemClickListener) {

        this.context = mContext;
        this.homeStoriesModels = storyListModels;
        //this.isShownMyProfile = isShownMyProfile;
    }

    @NonNull
    @Override
    public StoriesHomeAdpater.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_stories, parent, false);
        return new StoriesHomeAdpater.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StoriesHomeAdpater.MyViewHolder holder, final int position) {

        HomeStoriesModel homeStoriesModel = homeStoriesModels.get(position);

        holder.binding.ivUserStories.setImageResource(homeStoriesModel.getStoriesImage());
        holder.binding.tvUserStoriesName.setText(homeStoriesModel.getStoriesUserName());

        holder.binding.llStoryHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isShownMyProfile && position == 0) {
                    ActivityController.startActivity(context, StoriesViewActivity.class);
                } else {
                    ActivityController.startActivity(context, StoryRunningActivity.class);
                }
            }
        });

    }

    @Override
    public int getItemCount() {

        return homeStoriesModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private LayoutStoriesBinding binding;

        public MyViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}

