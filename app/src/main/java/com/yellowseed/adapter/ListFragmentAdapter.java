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
import com.yellowseed.databinding.LayoutUserPostBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.HomeBottomViewModel;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.DateUtils;
import com.yellowseed.webservices.response.post.Post;

import java.util.ArrayList;

/**
 * Created by mobiloitte on 18/5/18.
 */

public class ListFragmentAdapter extends RecyclerView.Adapter<ListFragmentAdapter.MyViewHolder> {

    private ArrayList<Post> arrayList;
    private Context context;
    private boolean flag = false;
    private OnItemClickListener listener;
    private int pos = -1;
    private boolean isShownMenuItems;

    public ListFragmentAdapter(ArrayList<Post> arrayList, Context context, boolean isShownMenuItems, OnItemClickListener listener) {
        this.arrayList = arrayList;
        this.context = context;
        this.listener = listener;
        this.isShownMenuItems = isShownMenuItems;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_user_post, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        //holder.binding.ivPostProfilePic.setImageResource(arrayList.get(position).getProfileImage());
        //holder.binding.tvPostUserName.setText(arrayList.get(position).getPostUserName());
        String time = DateUtils.convertDateFormat(arrayList.get(position).getPost().getUpdatedAt(),DateUtils.DATE_FORMAT_IN_HH_MM_A);
        holder.binding.tvPostTime.setText("Today at " + time);
        holder.binding.tvPostContent.setText(arrayList.get(position).getPost().getDescription());

        Picasso.with(context).load(arrayList.get(position).getImages().get(0).getFile().getUrl()).into(holder.binding.ivPostImage);

        holder.binding.llRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.llRow, holder.getAdapterPosition());
                flag = false;
            }
        });
        holder.binding.ivPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.ivPostImage, holder.getAdapterPosition());
                flag = false;
            }
        });
        holder.binding.tvPostUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.tvPostUserName, holder.getAdapterPosition());
                flag = false;
            }
        });
        holder.binding.ivPostShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.ivPostShare, holder.getAdapterPosition());
                flag = false;
            }
        });
        holder.binding.ivPostProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.ivPostProfilePic, holder.getAdapterPosition());
                flag = false;
            }
        });
        holder.binding.ivPostDownloads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.ivPostDownloads, holder.getAdapterPosition());
                flag = false;
            }
        });
        holder.binding.ivHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.ivHeart, holder.getAdapterPosition());
                flag = false;
            }
        });
        holder.binding.ivPostMenuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos = holder.getAdapterPosition();
                listener.onItemClick(holder.binding.ivPostMenuItem, holder.getAdapterPosition());
                if (!flag) {
                  //  holder.binding.llCustomShare.setVisibility(View.VISIBLE);
                    flag = true;


                } else {
                    flag = false;
                }
                notifyDataSetChanged();

            }

        });
        holder.binding.ivPostComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.ivPostComment, holder.getAdapterPosition());
                flag = false;
            }
        });

    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private LayoutUserPostBinding binding;

        public MyViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);

           /* binding.llRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    binding.llCustomShare.setVisibility(View.GONE);
                    flag = false;
                }
            });*/


        }
    }
}
