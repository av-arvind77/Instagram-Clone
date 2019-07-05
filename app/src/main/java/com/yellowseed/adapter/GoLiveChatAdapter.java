package com.yellowseed.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


import com.bumptech.glide.Glide;
import com.yellowseed.R;
import com.yellowseed.databinding.RowGoLiveBinding;
import com.yellowseed.model.resModel.GetChatResonse;

import java.util.List;


/**
 * Created by Ashutosh Kumar on 2/12/2017.
 */

public class GoLiveChatAdapter extends RecyclerView.Adapter<GoLiveChatAdapter.ViewHolder> {
    Context context;
    List<GetChatResonse.UserInfoBean> mChatList;
    int lastPosition = -1;


    public GoLiveChatAdapter(Context context, List<GetChatResonse.UserInfoBean> mChatList) {
        this.context = context;
        this.mChatList = mChatList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_go_live, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        GetChatResonse.UserInfoBean chatMessage = mChatList .get(position);
        if(chatMessage!=null) {
            holder.binding.tvName.setText(chatMessage.getBody());
            holder.binding.tvMessage.setText(chatMessage.getBody());
            try {
                Glide.with(context)
                        .load(chatMessage.getSender_image())
                        .into(holder.binding.ivUserPic);
            } catch (Exception e) {
                e.printStackTrace();
            }

            holder.binding.ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mChatList.remove(position);
                    notifyDataSetChanged();
                }
            });

            if(position >lastPosition) {
                Animation animation = AnimationUtils.loadAnimation(context,
                        R.anim.up_from_bottom);
                holder.binding.llChat.startAnimation(animation);
                lastPosition = position;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mChatList.size();
    }

   public void add(GetChatResonse.UserInfoBean chatMessage) {
        mChatList.add(mChatList.size(), chatMessage);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final RowGoLiveBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }


}
