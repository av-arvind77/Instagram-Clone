/*
package com.yellowseed.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.yellowseed.R;
import com.yellowseed.avatar.AvatarPart;
import com.yellowseed.avatar.AvatarSelectionListener;
import com.yellowseed.databinding.AvtarImageLayoutBinding;
import com.yellowseed.utils.Themes;

import java.util.List;

public class AvatarImageAdapter extends RecyclerView.Adapter<AvatarImageAdapter.ViewHolder> {

    private final AvatarSelectionListener avatarSelectionListener;
    private final boolean isMale;
    private Context context;
    private List<AvatarPart> avtarImageModelList;


    public AvatarImageAdapter(Context context, List<AvatarPart> avtarImageModelList, AvatarSelectionListener avatarSelectionListener, boolean isMale) {
        this.context = context;
        this.avtarImageModelList = avtarImageModelList;
        this.avatarSelectionListener = avatarSelectionListener;
        this.isMale = isMale;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.avtar_image_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.shadowView.setShadowColor(0);


//        String filePath = isMale?AvatarGenerator.AVATAR_PARTS_DIR_MALE:AvatarGenerator.AVATAR_PARTS_DIR_FEMALE + "/" + avtarImageModelList.get(holder.getAdapterPosition()).filename;
        String filePath = avtarImageModelList.get(position).filename;

        Bitmap partBitmap;
       */
/* try {
            InputStream stream;
            stream = context.getAssets().open(filePath);
            partBitmap = BitmapFactory.decodeStream(stream);
            stream.close();
            holder.binding.ivAvtarImage.setImageBitmap(partBitmap);
        } catch (IOException e) {
        }
*//*

        Picasso.with(context).load(filePath).into(holder.binding.ivAvtarImage);
        //holder.binding.ivAvtarImage.setImageResource(avtarImageModelList.get(holder.getAdapterPosition()).getImage());

    }

    @Override
    public int getItemCount() {
        return avtarImageModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private AvtarImageLayoutBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            binding.ivAvtarImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    avatarSelectionListener.onSubAccessoriesSelected(getAdapterPosition(), avtarImageModelList.get(getAdapterPosition()));
                    notifyDataSetChanged();
                }
            });
        }
    }
}
*/
package com.yellowseed.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yellowseed.R;
import com.yellowseed.databinding.AvtarImageLayoutBinding;
import com.yellowseed.model.AvtarImageModel;

import java.util.List;

public class AvatarImageAdapter extends RecyclerView.Adapter<AvatarImageAdapter.ViewHolder> {

    private Context context;
    private List<AvtarImageModel> avtarImageModelList;

    public AvatarImageAdapter(Context context, List<AvtarImageModel> avtarImageModelList) {
        this.context = context;
        this.avtarImageModelList = avtarImageModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.avtar_image_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.ivAvtarImage.setImageResource(avtarImageModelList.get(holder.getAdapterPosition()).getImage());

    }

    @Override
    public int getItemCount() {
        return avtarImageModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private AvtarImageLayoutBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding  = DataBindingUtil.bind(itemView);
        }
    }
}
