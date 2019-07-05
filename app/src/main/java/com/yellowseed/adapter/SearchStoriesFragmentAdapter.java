package com.yellowseed.adapter;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.yellowseed.R;
import com.yellowseed.activity.BottomNavigationViewHelper;
import com.yellowseed.databinding.SearchStoryFragmentLayoutBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.ExploreModel;

import java.util.List;

public class SearchStoriesFragmentAdapter extends RecyclerView.Adapter<SearchStoriesFragmentAdapter.ViewHolder> {


    private Context context;
    private List<ExploreModel> exploreModels;
    private SearchFragmentStaggeredAdapter searchFragmentStaggeredAdapter;
    private OnItemClickListener listener;

    public SearchStoriesFragmentAdapter(Context context, List<ExploreModel> exploreModels, OnItemClickListener listener) {
        this.context = context;
        this.exploreModels = exploreModels;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_story_fragment_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        ExploreModel currentItem = exploreModels.get(position);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.HORIZONTAL);
        holder.binding.rvImageSearchStory.setLayoutManager(staggeredGridLayoutManager);
        searchFragmentStaggeredAdapter = new SearchFragmentStaggeredAdapter(context);
        holder.binding.rvImageSearchStory.setAdapter(searchFragmentStaggeredAdapter);
        holder.binding.llSearchStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.llSearchStory, holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return exploreModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private SearchStoryFragmentLayoutBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);

            binding = DataBindingUtil.bind(itemView);
        }
    }
}
