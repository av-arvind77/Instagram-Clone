package com.yellowseed.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.squareup.picasso.Picasso;
import com.yellowseed.R;
import com.yellowseed.activity.CallDetailsActivity;
import com.yellowseed.databinding.RowCallHistoryBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.listener.OnItemLongClickListener;
import com.yellowseed.model.CallHistoryModel;
import com.yellowseed.utils.CircleTransform;
import com.yellowseed.utils.Themes;

import java.util.List;

public class CallHistoryAdapter extends RecyclerSwipeAdapter<CallHistoryAdapter.MyViewHolder> {

    private List<CallHistoryModel> alCallHistory;
    private Context context;
    private OnItemLongClickListener listener;
    private Themes themes;
    public boolean longClick=true;

 /*   public CallHistoryAdapter(Context context, List<CallHistoryModel> alCallHistory, OnItemClickListener listener) {
        this.alCallHistory = alCallHistory;
        this.context = context;
        this.listener = listener;
        themes = new Themes(context);
    }*/

    public CallHistoryAdapter(Context context, List<CallHistoryModel> alCallHistory, OnItemLongClickListener onItemLongClickListener) {

        this.alCallHistory = alCallHistory;
        this.context = context;
        this.listener = onItemLongClickListener;
        themes = new Themes(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_call_history, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        if (alCallHistory.get(position).isSelected()) {
            holder.binding.ivTick.setVisibility(View.VISIBLE);

            holder.binding.llParent.setBackgroundColor(ContextCompat.getColor(context,R.color.grey_call));
        } else {
            holder.binding.ivTick.setVisibility(View.GONE);
            holder.binding.llParent.setBackgroundColor(Color.TRANSPARENT);

        }


        holder.binding.llMain.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        holder.binding.tvCallerName.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        // holder.binding.tvCallType.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setGreyHint()));
      //  holder.binding.tvDay.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setGreyHint()));
        holder.binding.viewLine.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setViewLineGrey()));

        Themes.getInstance(context).changeCallType(context, holder.binding.ivCallType);
       // Themes.getInstance(context).changeInfoColor(context, holder.binding.ivCallType);

        holder.binding.tvTime.setText(alCallHistory.get(position).getTime());

        holder.binding.tvCallerName.setText(alCallHistory.get(position).getUserName());
        holder.binding.tvCallType.setText(alCallHistory.get(position).getCallType());
        holder.binding.ivOutgoing.setImageResource(alCallHistory.get(position).getCallStatus());
       // holder.binding.tvDay.setText(alCallHistory.get(position).getDay());
        holder.binding.ivCallType.setImageResource(alCallHistory.get(position).getCalltypeImg());

        String status = alCallHistory.get(position).getCallType();
       /* if (status.equalsIgnoreCase("Missed")) {
            holder.binding.ivOutgoing.set(ContextCompat.getColor(context, R.color.red));
        }*/

        //   holder.binding.tvTime.setText(alCallHistory.get(position).getHours());

        Picasso.with(context).load(alCallHistory.get(position).getUserImage()).transform(new CircleTransform()).into(holder.binding.ivUserProfile);


        mItemManger.bindView(holder.binding.swipe, position);

/*
        holder.binding.ivCallType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.ivInformation, holder.getAdapterPosition());
            }
        });*/


        holder.binding.llParent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
               listener.onLongClick(holder.binding.llParent, holder.getAdapterPosition());
                return true;
            }
        });
        holder.binding.llParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.llParent, holder.getAdapterPosition());
            }
        });
        holder.binding.llSwipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.llSwipe, holder.getAdapterPosition());
            }
        });
        holder.binding.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.ivDelete, holder.getAdapterPosition());
            }
        });
        holder.binding.ivCallType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.ivCallType, holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return alCallHistory.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        RowCallHistoryBinding binding;

        public MyViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

}
