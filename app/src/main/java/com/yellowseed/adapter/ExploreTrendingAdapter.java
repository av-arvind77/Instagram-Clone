package com.yellowseed.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yellowseed.R;
import com.yellowseed.databinding.RowExploreTrendingBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.HomeStoriesModel;

import java.util.ArrayList;

public class ExploreTrendingAdapter extends RecyclerView.Adapter<ExploreTrendingAdapter.MyViewHolder> {

    private ArrayList<HomeStoriesModel> storiesModels;
    private Context context;
    private OnItemClickListener listener;

    public ExploreTrendingAdapter(ArrayList<HomeStoriesModel> storiesModels, Context context, OnItemClickListener listener) {
        this.storiesModels = storiesModels;
        this.context = context;
        this.listener = listener;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_explore_trending, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        holder.binding.ivUserStories.setImageResource(storiesModels.get(position).getStoriesImage());
        holder.binding.tvUserStoriesName.setText(storiesModels.get(position).getStoriesUserName());
        holder.binding.llrowExploreTrending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.llrowExploreTrending, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return storiesModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public RowExploreTrendingBinding binding;

        public MyViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
