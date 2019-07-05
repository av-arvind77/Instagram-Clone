package com.yellowseed.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.adapters.SwitchBindingAdapter;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.yellowseed.R;
import com.yellowseed.databinding.LayoutSearchFollowingBinding;
import com.yellowseed.databinding.LayoutswitchaccountBinding;
import com.yellowseed.databinding.RowSwitchUserBinding;
import com.yellowseed.imageUtils.CircleTransformation;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.UserListModel;
import com.yellowseed.model.reqModel.UserModel;
import com.yellowseed.utils.Themes;

import java.util.ArrayList;

public class SavedUserAdapter extends RecyclerView.Adapter<SavedUserAdapter.MyViewHolder> {

    private ArrayList<UserModel> arlModel;
    private Context mContext;
    private OnItemClickListener listener;

    public SavedUserAdapter( Context mContext,ArrayList<UserModel> arlModel, OnItemClickListener listener) {
        this.arlModel = arlModel;
        this.mContext = mContext;
        this.listener = listener;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_switch_user, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        holder.binding.tvName.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkGreyTextColor()));

        UserModel model = arlModel.get(position);
        holder.binding.tvName.setText(TextUtils.isEmpty(model.getName()) ? "" : model.getName());
        if(!TextUtils.isEmpty(model.getImage()))
        {
            Picasso.with(mContext).load(model.getImage()).placeholder(R.drawable.user_placeholder).
                    transform(new CircleTransformation()).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.binding.ivUserPic);

        }else {
            Picasso.with(mContext).load(R.drawable.user_placeholder).placeholder(R.drawable.user_placeholder).
                    transform(new CircleTransformation()).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.binding.ivUserPic);

        }
        holder.binding.llMainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v,holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return arlModel.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private RowSwitchUserBinding binding;
        public MyViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
