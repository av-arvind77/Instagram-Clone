package com.yellowseed.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.yellowseed.R;
import com.yellowseed.databinding.LayoutThemesItemBinding;
import com.yellowseed.model.AvtarImageModel;
import com.yellowseed.utils.CommonUtils;

import java.util.ArrayList;

public class ThemesAdapter extends RecyclerView.Adapter<ThemesAdapter.ViewHolder>{

    private ArrayList<AvtarImageModel> images;
    private Context context;
    private int pos = -1;
    public ThemesAdapter(ArrayList<AvtarImageModel> images, Context context) {
        this.images = images;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_themes_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Picasso.with(context).load(images.get(position).getImage()).centerCrop().resize(200, 200).into(holder.binding.themeElement);

        if (pos == holder.getAdapterPosition()) {
            holder.binding.themeElement.setBackground(CommonUtils.getDrawable(context, R.drawable.bg_rect_black));
        } else {
            holder.binding.themeElement.setBackground(null);
        }
        holder.binding.themeElement.setOnClickListener(new View.OnClickListener() {
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LayoutThemesItemBinding binding;
        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
