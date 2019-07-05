package com.yellowseed.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yellowseed.R;
import com.yellowseed.databinding.SearchStoryStaggeredLayoutBinding;

public class MyProfileImageAdapter extends RecyclerView.Adapter<MyProfileImageAdapter.ViewHolder> {

    private Context context;
    private int[] image = {R.mipmap.img, R.mipmap.img,R.mipmap.img, R.mipmap.img, R.mipmap.img, R.mipmap.img,R.mipmap.img, R.mipmap.img,R.mipmap.img, R.mipmap.img,R.mipmap.img, R.mipmap.img,R.mipmap.img, R.mipmap.img};

    public MyProfileImageAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.search_story_staggered_layout, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.ivStaggeredImageSearch.setImageResource(image[position]);
    }

    @Override
    public int getItemCount() {
        return image.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private SearchStoryStaggeredLayoutBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);

            binding = DataBindingUtil.bind(itemView);
        }
    }
}
