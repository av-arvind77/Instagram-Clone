package com.yellowseed.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import com.yellowseed.R;
import com.yellowseed.databinding.SearchStoryFragmentLayoutBinding;
import com.yellowseed.databinding.SearchStoryStaggeredLayoutBinding;

public class MyProfileImageTextAdapter extends RecyclerView.Adapter<MyProfileImageTextAdapter.ViewHolder> {

    private Context context;
    private MyProfileImageAdapter myProfileImageAdapter;

    public MyProfileImageTextAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.search_story_fragment_layout,parent,false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false);
        holder.binding.rvImageSearchStory.setLayoutManager(gridLayoutManager);
        myProfileImageAdapter = new MyProfileImageAdapter(context);
        holder.binding.rvImageSearchStory.setAdapter(myProfileImageAdapter);
        holder.binding.rvImageSearchStory.setNestedScrollingEnabled(false);

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private SearchStoryFragmentLayoutBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);

            binding = DataBindingUtil.bind(itemView);
        }
    }
}
