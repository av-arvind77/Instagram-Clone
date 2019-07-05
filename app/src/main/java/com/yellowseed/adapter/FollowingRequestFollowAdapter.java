package com.yellowseed.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yellowseed.R;
import com.yellowseed.databinding.LayoutfollowingacceptBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.FollowingRequestModel;
import com.yellowseed.utils.Themes;

import java.util.ArrayList;

public class FollowingRequestFollowAdapter extends RecyclerView.Adapter<FollowingRequestFollowAdapter.ListViewHolder> {
    private ArrayList<FollowingRequestModel> arrayList;
    private Context context;
    private OnItemClickListener listener;
    private String type;

    public FollowingRequestFollowAdapter(ArrayList<FollowingRequestModel> arrayList, Context context, OnItemClickListener listener) {
        this.arrayList = arrayList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layoutfollowingaccept, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListViewHolder holder, int position) {
        holder.binding.tvFollowingAccept.setTextColor(ContextCompat.getColor(context,Themes.getInstance(context).setDarkThemeText()));
        holder.binding.tvMutualFollowers.setTextColor(ContextCompat.getColor(context,Themes.getInstance(context).setGreyHint()));
        holder.binding.btnFollowingReject.setBackground(ContextCompat.getDrawable(context,Themes.getInstance(context).setRejectDrawable()));
        holder.binding.btnFollowingReject.setTextColor(ContextCompat.getColor(context,Themes.getInstance(context).setDarkThemeText()));
        holder.binding.tvUserImage.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));

        Themes.getInstance(context).changeIconColorToWhite(context, holder.binding.btnFollowCross);


        holder.binding.tvFollowingAccept.setText(arrayList.get(position).getUserFollowingName());
        holder.binding.tvMutualFollowers.setText(arrayList.get(position).getUserFollowers());
        holder.binding.btnFollowingAccept.setText("Follow");
        holder.binding.btnFollowingReject.setVisibility(View.GONE);
        holder.binding.btnFollowCross.setVisibility(View.VISIBLE);

        holder.binding.llFollowUSer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.llFollowUSer, holder.getAdapterPosition());
            }
        });
        holder.binding.btnFollowCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.btnFollowCross, holder.getAdapterPosition());
            }
        });
        holder.binding.btnFollowingAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.btnFollowingAccept, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        private LayoutfollowingacceptBinding binding;

        public ListViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
