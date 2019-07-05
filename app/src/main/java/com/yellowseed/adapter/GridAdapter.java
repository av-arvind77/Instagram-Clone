package com.yellowseed.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.yellowseed.R;
import com.yellowseed.databinding.SearchStoryStaggeredLayoutBinding;
import com.yellowseed.listener.OnItemClickListener;

import java.util.List;

/**
 * Created by mobiloitte on 18/5/18.
 */

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.MyviewHolder> {

    private Context context;
    private OnItemClickListener listener;
    private List<String> arlGif;

    public GridAdapter(Context context,List<String> arlGif, OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.arlGif = arlGif;
    }


    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_story_staggered_layout,parent,false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyviewHolder holder, int position) {

        int displayWidth = context.getResources().getDisplayMetrics().widthPixels/3;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int)displayWidth, (int)displayWidth);
        holder.binding.llListGrid.setLayoutParams(layoutParams);
        if(arlGif.get(position)!=null&&arlGif.get(position).length()>0){
            Glide.with(context).load(arlGif.get(position)).into(holder.binding.ivStaggeredImageSearch);
        }else {
            Glide.with(context).load(R.drawable.user_placeholder).into(holder.binding.ivStaggeredImageSearch);
        }

        holder.binding.llListGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.llListGrid,holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return arlGif.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder{

        private SearchStoryStaggeredLayoutBinding binding;
        public MyviewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
