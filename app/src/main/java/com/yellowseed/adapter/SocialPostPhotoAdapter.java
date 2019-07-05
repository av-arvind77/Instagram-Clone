package com.yellowseed.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.yellowseed.R;
import com.yellowseed.databinding.RowSingleImageBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.SocialPostPhotoModel;
import com.yellowseed.utils.Themes;

import java.util.ArrayList;

/**
 * Created by mobiloitte on 22/5/18.
 */

public class SocialPostPhotoAdapter extends RecyclerView.Adapter<SocialPostPhotoAdapter.MyViewHolder> {

    private ArrayList<SocialPostPhotoModel> arrayList;
    private Context context;
    private OnItemClickListener listener;


    public SocialPostPhotoAdapter(Context context, ArrayList<SocialPostPhotoModel> arrayList, OnItemClickListener listener) {
        this.arrayList = arrayList;
        this.context = context;
        this.listener = listener;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_single_image, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder,  int position) {

        if (arrayList.size()>0){
            Themes.getInstance(context).changeIconColorToWhite(context,holder.binding.ivCross);
        }
        SocialPostPhotoModel currentItem = arrayList.get(position);
        if(arrayList.size() > 0 && position == arrayList.size()-1){
            holder.binding.ivAddImage.setVisibility(View.VISIBLE);
            Themes.getInstance(context).changeIconColorToWhite(context,holder.binding.ivCross);

        }else {
            holder.binding.ivAddImage.setVisibility(View.GONE);
        }
        switch (currentItem.getType()){
            case "local":
                if(currentItem.getThumbLocal()!=null){
                    holder.binding.ivSingleImage.setImageBitmap(currentItem.getThumbLocal());
                }else {
                    Picasso.with(context).load(currentItem.getFile()).placeholder(R.mipmap.img).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.binding.ivSingleImage);
                }
                break;
            case "server":
                if(currentItem.getThumbServer()!=null&&currentItem.getThumbServer().length()>0){
                    if(currentItem.getThumbServer().endsWith(".mp4")){
                        Picasso.with(context).load(currentItem.getThumbServer().replace(".mp4",".jpg")).placeholder(R.mipmap.img).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.binding.ivSingleImage);
                    }else if(currentItem.getThumbServer().endsWith(".mov")){
                        Picasso.with(context).load(currentItem.getThumbServer().replace(".mov",".jpg")).placeholder(R.mipmap.img).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.binding.ivSingleImage);
                    }else {
                        Picasso.with(context).load(currentItem.getThumbServer()).placeholder(R.mipmap.img).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.binding.ivSingleImage);
                    }
                }else {
                    Picasso.with(context).load(currentItem.getUrl()).placeholder(R.mipmap.img).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.binding.ivSingleImage);
                }
                break;
            default:
                Picasso.with(context).load(currentItem.getFile()).placeholder(R.mipmap.img).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.binding.ivSingleImage);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private RowSingleImageBinding binding;
        public MyViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            binding.ivAddImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(binding.ivAddImage,getAdapterPosition());
                }
            });
            binding.ivCross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(binding.ivCross, getAdapterPosition());
                }
            });
        }
    }
}
