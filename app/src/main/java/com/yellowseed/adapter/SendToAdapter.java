package com.yellowseed.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.squareup.picasso.Picasso;
import com.yellowseed.R;
import com.yellowseed.databinding.LayoutSendtoBinding;
import com.yellowseed.model.SendToModel;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.Themes;

import java.util.ArrayList;

/**
 * Created by mobiloitte on 3/5/18.
 */

public class SendToAdapter extends RecyclerView.Adapter<SendToAdapter.ListViewHolder>{

    private ArrayList<SendToModel> arrayList;
    private Context mctx;

    public SendToAdapter(ArrayList<SendToModel> arrayList, Context mctx) {
        this.arrayList = arrayList;
        this.mctx = mctx;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_sendto,parent,false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
      //  Picasso.with(mctx).load(arrayList.get(position).getImage_url()).resize(400,400).into(holder.binding.ivSendToUserPic);
        holder.binding.tvSendToUserName.setText(arrayList.get(position).getName_url());
        holder.binding.ivSendToUserPic.setImageResource(arrayList.get(position).getImage_url());
        holder.binding.tvSendToUserName.setTextColor(ContextCompat.getColor(mctx, Themes.getInstance(mctx).setDarkThemeText()));


        /*CommonUtils.otherUserProfile(mctx,holder.binding.ivSendToUserPic,arrayList.get(position).getImage_url(),holder.binding.tvUserImage,
                arrayList.get(position).getName_url());*/

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void updatedList(ArrayList<SendToModel> filteresNames){
        arrayList = filteresNames;
        notifyDataSetChanged();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder{
        LayoutSendtoBinding binding;
        public ListViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
