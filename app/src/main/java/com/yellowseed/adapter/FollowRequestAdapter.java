package com.yellowseed.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.yellowseed.R;
import com.yellowseed.databinding.LayoutfollowingacceptBinding;
import com.yellowseed.imageUtils.CircleTransformation;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.FollowingRequestModel;
import com.yellowseed.model.resModel.RequestListResonse;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.Themes;

import java.util.ArrayList;

public class FollowRequestAdapter extends RecyclerView.Adapter<FollowRequestAdapter.ListViewHolder> {

 //   private ArrayList<RequestListResonse.UsersBean> arrayList;
    private ArrayList<FollowingRequestModel> arrayList;

    private Context context;
    private OnItemClickListener listener;
    private RequestListResonse.UsersBean model;
    Themes themes;
    private Boolean darkThemeEnabled;

    public FollowRequestAdapter(ArrayList<FollowingRequestModel> arrayList, Context context, OnItemClickListener listener) {
        this.arrayList = arrayList;
        this.context = context;
        this.listener = listener;
        themes = new Themes(context);
        darkThemeEnabled = CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME);
    }

    @NonNull
    @Override
    public FollowRequestAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layoutfollowingaccept, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FollowRequestAdapter.ListViewHolder holder, int position) {
        holder.binding.tvFollowingAccept.setTextColor(ContextCompat.getColor(context,themes.setDarkThemeText()));
        holder.binding.tvMutualFollowers.setTextColor(ContextCompat.getColor(context,themes.setGreyHint()));
        holder.binding.btnFollowingReject.setBackground(ContextCompat.getDrawable(context,themes.setRejectDrawable()));
        holder.binding.btnFollowingReject.setTextColor(ContextCompat.getColor(context,themes.setDarkThemeText()));
        holder.binding.tvUserImage.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));

      /*  model = arrayList.get(position);
        if (model.getName() != null) {
            holder.binding.tvFollowingAccept.setText(model.getName());
        }

        if (model.getImage() != null && !model.getImage().equalsIgnoreCase("")) {
            Picasso.with(context).load(model.getImage()).transform(new CircleTransformation()).into(holder.binding.ivFollowingRequestAccept);
        }
        else
        {

            CommonUtils.otherUserProfile(context,holder.binding.ivFollowingRequestAccept,model.getImage(),holder.binding.tvUserImage,
                    model.getName());
        }

        holder.binding.tvMutualFollowers.setText(model.getMutual_follower() + " "+context.getString(R.string.mutual_followers_count));

        holder.binding.llFollowUSer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.llFollowUSer, holder.getAdapterPosition());
            }
        });
        holder.binding.btnFollowingAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.btnFollowingAccept, holder.getAdapterPosition());
            }
        });
        holder.binding.btnFollowingReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.btnFollowingReject, holder.getAdapterPosition());
            }
        });
*/


        holder.binding.tvFollowingAccept.setText(arrayList.get(position).getUserFollowingName());
        holder.binding.ivFollowingRequestAccept.setImageResource(arrayList.get(position).getUserFollowersPicture());
        holder.binding.tvMutualFollowers.setText(arrayList.get(position).getUserFollowers());

        holder.binding.llFollowUSer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.llFollowUSer, holder.getAdapterPosition());
            }
        });
        holder.binding.btnFollowingAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.btnFollowingAccept, holder.getAdapterPosition());
            }
        });
        holder.binding.btnFollowingReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.btnFollowingReject, holder.getAdapterPosition());
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
