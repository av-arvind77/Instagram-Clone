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
import com.yellowseed.databinding.RowStoriesDemoBinding;
import com.yellowseed.model.HomeStoriesModel;
import com.yellowseed.utils.ActivityController;

import java.util.List;

public class HomeStoriesAdapterDemo extends RecyclerView.Adapter<HomeStoriesAdapterDemo.MyViewHolder> {

    private List<HomeStoriesModel> homeStoriesModels;
    private Context context;
    private boolean isShownMyProfile;

    /*public HomeStoriesAdapter(List<HomeStoriesModel> homeStoriesModels, Context context) {
        this.homeStoriesModels = homeStoriesModels;
        this.context = context;
    }*/

    public HomeStoriesAdapterDemo(Context context, List<HomeStoriesModel> storieImage, boolean isShownMyProfile) {
        this.context = context;
        this.homeStoriesModels = storieImage;
        this.isShownMyProfile = isShownMyProfile;
    }

    @NonNull
    @Override
    public HomeStoriesAdapterDemo.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_stories_demo, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeStoriesAdapterDemo.MyViewHolder holder, final int position) {
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
        if (homeStoriesModels != null && homeStoriesModels.size() > 0)
            return homeStoriesModels.size();
        else
            return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private RowStoriesDemoBinding binding;

        public MyViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
