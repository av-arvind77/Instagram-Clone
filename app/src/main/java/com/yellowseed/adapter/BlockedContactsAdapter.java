package com.yellowseed.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.squareup.picasso.Picasso;
import com.yellowseed.R;
import com.yellowseed.databinding.LayoutBlockedProfilesItemBinding;

import com.yellowseed.imageUtils.CircleTransformation;
import com.yellowseed.listener.OnClickListener;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.listener.onBlockItemListener;
import com.yellowseed.model.BlockedContactsModel;
import com.yellowseed.model.UserListModel;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.Themes;

import java.util.ArrayList;
import java.util.List;

public class BlockedContactsAdapter extends RecyclerView.Adapter<BlockedContactsAdapter.ViewHolder> {

    private List<BlockedContactsModel> contacts;
    private Context context;
    private OnItemClickListener listener;




    public BlockedContactsAdapter(ArrayList<BlockedContactsModel> models, Context context, OnItemClickListener onItemClickListener) {

        this.contacts = models;
        this.context = context;
        this.listener=onItemClickListener;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_blocked_profiles_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {


        holder.binding.llContacts.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        holder.binding.tvContactUserName.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        //holder.binding.tvContactUserName.setText(contacts.get(position).getName());
        holder.binding.tvUserImage.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        holder.binding.tvContactUserName.setText(contacts.get(position).getUserName());
        Picasso.with(context).load(contacts.get(position).getUserPicture()).into(holder.binding.ivContactUserPic);

        //  Picasso.with(context).load(contacts.get(position).getImage()).transform(new CircleTransformation()).placeholder(R.drawable.user_placeholder).into(holder.binding.ivContactUserPic);
/*        CommonUtils.otherUserProfile(context, holder.binding.ivContactUserPic, contacts.get(position).getImage(), holder.binding.tvUserImage,
                contacts.get(position).getName());*/

    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public void updatedList(ArrayList<BlockedContactsModel> filteresNames){
        contacts = filteresNames;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private LayoutBlockedProfilesItemBinding binding;
        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);

/*
            if (binding != null) {
                binding.tbBlock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("Toggle button text",binding.tbBlock.getText().toString().trim());
                      //  listener.onBlockItemListener(binding.tbBlock,getAdapterPosition(),binding.tbBlock.getText().toString().trim());
                    }
                });*/

                /*binding.tbBlock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Log.e("Toggle button text",binding.tbBlock.getText().toString().trim());
                        listener.onBlockItemListener(binding.tbBlock,position,binding.tbBlock.getText().toString().trim());
                    }
                });*/
            }
        }
    }

