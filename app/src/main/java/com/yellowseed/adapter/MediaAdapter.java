package com.yellowseed.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.yellowseed.R;
import com.yellowseed.databinding.ImagesItemBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.ImagesModelClass;
import com.yellowseed.model.resModel.GetChatResonse;

import java.util.ArrayList;
import java.util.List;

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.ViewHolder>{

    private List<GetChatResonse.UserInfoBean> items;
    private Context context;
    private OnItemClickListener listener;
    public MediaAdapter(List<GetChatResonse.UserInfoBean> items, Context context, OnItemClickListener listener) {
        this.items = items;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.images_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
       /* int displayWidth = context.getResources().getDisplayMetrics().widthPixels/3;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int)displayWidth, (int)displayWidth);
        holder.binding.llMain.setLayoutParams(layoutParams);
        if(items.get(position).getBody()!=null&&items.get(position).getBody().length()>0 ) {
            if(items.get(position).getBody().endsWith(".mp4")){
                Picasso.with(context).load(items.get(position).getBody().replace(".mp4",".jpg")).into(holder.binding.image);
            }else {
                Picasso.with(context).load(items.get(position).getBody()).into(holder.binding.image);
            }

        }*/

        holder.binding.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v,holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
       // return items.size();
        return 3;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImagesItemBinding binding;
        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}