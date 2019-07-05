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
import com.yellowseed.databinding.LayoutColorsItemBinding;
import com.yellowseed.model.AvtarImageModel;
import com.yellowseed.utils.CommonUtils;

import java.util.ArrayList;

public class SolidColorsAdapter extends RecyclerView.Adapter<SolidColorsAdapter.ViewHolder> {

    private ArrayList<String> images;
    private Context context;
    private int pos = -1;

    public SolidColorsAdapter(ArrayList<String> images, Context context) {
        this.images = images;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_colors_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
     //   Picasso.with(context).load(images.get(position)).into(holder.binding.colorElement);

        holder.binding.colorElement.setBackgroundColor(Color.parseColor(images.get(position)));
        if (pos == holder.getAdapterPosition()) {
            holder.binding.llMain.setBackground(CommonUtils.getDrawable(context, R.drawable.bg_rect_black));
        } else {
            holder.binding.llMain.setBackground(null);
        }
        holder.binding.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos = holder.getAdapterPosition();
                notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public String getSelected() {
        if(pos!=-1) {
            return images.get(pos);
        }else {
            return "";
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LayoutColorsItemBinding binding;
        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
