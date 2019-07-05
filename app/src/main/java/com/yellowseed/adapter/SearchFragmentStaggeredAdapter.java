package com.yellowseed.adapter;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.squareup.picasso.Picasso;
import com.yellowseed.R;
import com.yellowseed.activity.ShowPostItemsActivity;
import com.yellowseed.databinding.SearchStoryStaggeredLayoutBinding;
import com.yellowseed.utils.ActivityController;

public class SearchFragmentStaggeredAdapter extends RecyclerView.Adapter<SearchFragmentStaggeredAdapter.ViewHolder> {

    private Context context;
    private int[] image = {  R.mipmap.image_6, R.mipmap.image_7,R.mipmap.image_6, R.mipmap.image_7,R.mipmap.image_6, R.mipmap.image_7,R.mipmap.image_6, R.mipmap.image_7,R.mipmap.image_6, R.mipmap.image_7};

    public SearchFragmentStaggeredAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.search_story_staggered_layout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.ivStaggeredImageSearch.setImageResource(image[position]);
        holder.binding.ivStaggeredImageSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityController.startActivity(context, ShowPostItemsActivity.class);
            }
        });
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
