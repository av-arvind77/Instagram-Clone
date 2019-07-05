package com.yellowseed.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.yellowseed.R;
import com.yellowseed.databinding.LayoutnewgroupdoneBinding;
import com.yellowseed.imageUtils.CircleTransformation;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.NewGroupDoneMOdel;
import com.yellowseed.model.UserListModel;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.Themes;

import java.util.ArrayList;

public class NewGroupDoneAdapter extends RecyclerView.Adapter<NewGroupDoneAdapter.ListViewHolder> {
    private ArrayList<NewGroupDoneMOdel> arrayList;
    private Context context;
    private OnItemClickListener listener;

    public NewGroupDoneAdapter(ArrayList<NewGroupDoneMOdel> arrayList, Context context, OnItemClickListener listener) {
        this.arrayList = arrayList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NewGroupDoneAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layoutnewgroupdone, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NewGroupDoneAdapter.ListViewHolder holder, int position) {

        holder.binding.tvGroupUserName.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        holder.binding.tvUserImage.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));

        holder.binding.tvGroupUserName.setText(arrayList.get(position).getGroupUserName());
        holder.binding.ivGroupUserImage.setImageResource(arrayList.get(position).getGroupUserImage());
        holder.binding.btnGroupUserRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.btnGroupUserRemove, holder.getAdapterPosition());
            }
        });


    /*    if (arrayList.get(position).getName() != null) {
            holder.binding.tvGroupUserName.setText(arrayList.get(position).getName());
        }

    *//*    if (arrayList.get(position) != null) {
            Picasso.with(context).load(arrayList.get(position).getImage()).placeholder(R.drawable.user_placeholder).
                    transform(new CircleTransformation()).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.binding.ivGroupUserImage);

        }*//*
        CommonUtils.otherUserProfile(context,holder.binding.ivGroupUserImage,arrayList.get(position).getImage(),holder.binding.tvUserImage,
                arrayList.get(position).getName());



        holder.binding.btnGroupUserRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.btnGroupUserRemove, holder.getAdapterPosition());
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        private LayoutnewgroupdoneBinding binding;

        public ListViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
