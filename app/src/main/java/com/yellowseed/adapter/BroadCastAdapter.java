package com.yellowseed.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.yellowseed.R;
import com.yellowseed.databinding.LayoutSendtoBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.SendToModel;
import com.yellowseed.utils.ToastUtils;

import java.util.ArrayList;

public class BroadCastAdapter extends RecyclerView.Adapter<BroadCastAdapter.ListViewHolder> {
    private ArrayList<SendToModel> arrayList;
    private Context context;
    private OnItemClickListener listener;

    public BroadCastAdapter(ArrayList<SendToModel> arrayList, Context context, OnItemClickListener listener) {
        this.arrayList = arrayList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BroadCastAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_sendto, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BroadCastAdapter.ListViewHolder holder, final int position) {

        holder.binding.tvSendToUserName.setText(arrayList.get(position).getName_url());
        holder.binding.ivSendToUserPic.setImageResource(arrayList.get(position).getImage_url());
        holder.binding.llSendtoitems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.llSendtoitems, holder.getAdapterPosition());
            }
        });

        holder.binding.checkBoxSendTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.checkBoxSendTo,holder.getAdapterPosition());
            }
        });

    }
    public void updatedList(ArrayList<SendToModel> filteresNames){
        arrayList = filteresNames;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        LayoutSendtoBinding binding;

        public ListViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
