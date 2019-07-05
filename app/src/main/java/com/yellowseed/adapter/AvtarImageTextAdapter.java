/*
package com.yellowseed.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.yellowseed.R;
import com.yellowseed.avatar.AvatarSelectionListener;
import com.yellowseed.databinding.AvtarImageTextLayoutBinding;
import com.yellowseed.model.AvatarModel;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.webservices.response.avatar.AvatarCategory;

import java.util.List;

public class AvtarImageTextAdapter extends RecyclerView.Adapter<AvtarImageTextAdapter.ViewHolder> {

    private final AvatarSelectionListener avatarSelectionListener;
    private final List<AvatarCategory> avatarCategories;
    private Context context;
    private int pos = -1;
    private String isFrom;

    public AvtarImageTextAdapter(Context context, List<AvatarCategory> avatarCategories, AvatarSelectionListener avatarSelectionListener, String isFrom) {
        this.avatarSelectionListener = avatarSelectionListener;
        this.context = context;
        this.avatarCategories = avatarCategories;
        this.isFrom=isFrom;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.avtar_image_text_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        //holder.binding.ivAvtarImageText.setImageResource(image[position]);
        if (isFrom!=null) {
            holder.binding.cardView.setCardBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        }

        AvatarCategory avatarCategory = avatarCategories.get(position);
        if(avatarCategory.getAvatarImage().size()>0) {
            Glide.with(context).load(avatarCategory.getAvatarImage().get(0).getFile().getUrl()).into(holder.binding.ivAvtarImageText);
        }else{

        }
            holder.binding.tvAvtarText.setText(avatarCategory.getName());
        if(CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {

            if (pos == holder.getAdapterPosition()) {
                holder.binding.llHolder.setBackground(CommonUtils.getDrawable(context, R.mipmap.imgselected));
                holder.binding.tvAvtarText.setTextColor(ContextCompat.getColor(context, R.color.yellow));
            } else {
                holder.binding.llHolder.setBackground(null);
                holder.binding.tvAvtarText.setTextColor(ContextCompat.getColor(context, R.color.white));
            }
        }
        else {
            if (pos == holder.getAdapterPosition()) {
                holder.binding.llHolder.setBackground(CommonUtils.getDrawable(context, R.mipmap.frame_img));
                holder.binding.tvAvtarText.setTextColor(ContextCompat.getColor(context, R.color.red));
            } else {
                holder.binding.llHolder.setBackground(null);
                holder.binding.tvAvtarText.setTextColor(ContextCompat.getColor(context, R.color.black));
            }

        }
        holder.binding.llHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos = holder.getAdapterPosition();
                avatarSelectionListener.onAccessoriesSelected(position);
                notifyDataSetChanged();

            }
        });
    }

    @Override
    public int getItemCount() {
        return avatarCategories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private AvtarImageTextLayoutBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);

            binding = DataBindingUtil.bind(itemView);
        }
    }
}
*/
package com.yellowseed.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yellowseed.R;
import com.yellowseed.databinding.AvtarImageTextLayoutBinding;
import com.yellowseed.model.AvatarModel;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.Themes;

import java.util.List;

public class AvtarImageTextAdapter extends RecyclerView.Adapter<AvtarImageTextAdapter.ViewHolder> {

    private Context context;
    private int[] image = {R.mipmap.skin_type, R.mipmap.hair_color, R.mipmap.hair_style_img, R.mipmap.beard_img, R.mipmap.beared_color_one};
    private String[] nameType = {"Skin Type", "Hair Color", "Hair Style", "Beard", "Beard Color"};
    private int pos = -1;
    private String isFrom;

/*    public AvtarImageTextAdapter(Context context) {
        this.context = context;
    }*/

    public AvtarImageTextAdapter(Context context, String isFrom) {
        this.context = context;
        this.isFrom=isFrom;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.avtar_image_text_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        if (isFrom!=null) {
            holder.binding.cardView.setCardBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        }

        holder.binding.ivAvtarImageText.setImageResource(image[position]);
        holder.binding.tvAvtarText.setText(nameType[position]);


        if(CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {

            if (pos == holder.getAdapterPosition()) {
                holder.binding.llHolder.setBackground(CommonUtils.getDrawable(context, R.mipmap.imgselected));
                holder.binding.tvAvtarText.setTextColor(ContextCompat.getColor(context, R.color.yellow));
            } else {
                holder.binding.llHolder.setBackground(null);
                holder.binding.tvAvtarText.setTextColor(ContextCompat.getColor(context, R.color.white));
            }
        }
        else {
            if (pos == holder.getAdapterPosition()) {
                holder.binding.llHolder.setBackground(CommonUtils.getDrawable(context, R.mipmap.frame_img));
                holder.binding.tvAvtarText.setTextColor(ContextCompat.getColor(context, R.color.red));
            } else {
                holder.binding.llHolder.setBackground(null);
                holder.binding.tvAvtarText.setTextColor(ContextCompat.getColor(context, R.color.black));
            }

        }



        holder.binding.llHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos = holder.getAdapterPosition();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return image.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private AvtarImageTextLayoutBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);

            binding = DataBindingUtil.bind(itemView);
        }
    }
}
