package com.yellowseed.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.squareup.picasso.Picasso;
import com.yellowseed.R;
import com.yellowseed.databinding.ImagesItemBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.listener.OnItemLongClickListener;
import com.yellowseed.model.ImagesModelClass;

import java.util.ArrayList;
import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder>{

    private List<ImagesModelClass> items;
    private Context context;
    private OnItemLongClickListener listener;
    public ImagesAdapter(List<ImagesModelClass> items, Context context, OnItemLongClickListener listener) {
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

        if (items.get(position).isSelected()) {
            holder.binding.ivTick.setVisibility(View.VISIBLE);

        } else {
            holder.binding.ivTick.setVisibility(View.GONE);

        }

        holder.binding.image.setImageResource(items.get(position).getPicture());
        holder.binding.image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onItemClick(holder.binding.image, holder.getAdapterPosition());

                return true;
            }
        });
        holder.binding.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.image, holder.getAdapterPosition());
            }
        });
       // Picasso.with(context).load(items.get(position).getPicture()).into(holder.binding.image);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImagesItemBinding binding;
        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}