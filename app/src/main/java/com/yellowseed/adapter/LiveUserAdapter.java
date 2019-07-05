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
import com.yellowseed.databinding.LayoutLiveUsersBinding;
import com.yellowseed.databinding.LayoutTaggedUserBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.InstanceListModel;
import com.yellowseed.utils.Themes;

import java.util.List;

public class LiveUserAdapter extends RecyclerView.Adapter<LiveUserAdapter.MyViewHolder> {

    private List<InstanceListModel> liveUsers;
    private Context mContext;
    private OnItemClickListener listener;

    public LiveUserAdapter(Context context, List<InstanceListModel> liveUsers, OnItemClickListener listener) {
        this.liveUsers = liveUsers;
        this.mContext = context;
        this.listener = listener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_live_users, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
       /* holder.binding.llContacts.setBackground(ContextCompat.getDrawable(mContext, Themes.getInstance(mContext).setLiveUserDarwable()));
        holder.binding.tvTagUserName.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));


        if (liveUsers.get(position).getApplicationInstance() != null && liveUsers.get(position).getApplicationInstance().length() > 0) {
            holder.binding.tvTagUserName.setText(liveUsers.get(position).getApplicationInstance());
        }*/

        holder.binding.llContacts.setBackground(ContextCompat.getDrawable(mContext, Themes.getInstance(mContext).setLiveUserDrawble()));
        holder.binding.tvTagUserName.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkGreyTextColor()));


        holder.binding.llContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.llContacts, holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount()
    {
        return /*liveUsers.size()*/10;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private LayoutLiveUsersBinding binding;

        public MyViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);


        }

    }
}
