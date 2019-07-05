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
import com.yellowseed.databinding.LayoutBlockedProfilesItemBinding;
import com.yellowseed.imageUtils.CircleTransformation;
import com.yellowseed.listener.ApiCallback;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.webservices.ApiResponse;
import com.yellowseed.webservices.CommonApi;
import com.yellowseed.webservices.response.BlockUserListResponse;


import java.util.List;

/**
 * Created by rajat_mobiloitte on 13/7/18.
 */

public class BlockContactListAdapter extends RecyclerView.Adapter<BlockContactListAdapter.ViewHolder>{

    private List<BlockUserListResponse.BlockBean> contacts;
    private Context context;


    public BlockContactListAdapter(List<BlockUserListResponse.BlockBean> contacts, Context context) {
        this.contacts = contacts;
        this.context = context;
    }

    @NonNull
    @Override
    public BlockContactListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_blocked_profiles_item, parent, false);
        return new BlockContactListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.llContacts.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        holder.binding.tvContactUserName.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        holder.binding.tvUserImage.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));

        holder.binding.tvContactUserName.setText(contacts.get(position).getName());
        holder.binding.tbBlock.setText(R.string.unblock);
   /*     if (contacts.get(position).getImage() != null && !contacts.get(position).getImage().equalsIgnoreCase("")) {

            Picasso.with(context).load(contacts.get(position).getImage()).placeholder(R.drawable.user_placeholder).transform(new CircleTransformation()).into(holder.binding.ivContactUserPic);

        } else
        {*/
            CommonUtils.otherUserProfile(context, holder.binding.ivContactUserPic, contacts.get(position).getImage(), holder.binding.tvUserImage,
                    contacts.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LayoutBlockedProfilesItemBinding binding;
        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);

            binding.tbBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonApi.callUnblockUserAPI(context, contacts.get(getAdapterPosition()).getId(), new ApiCallback() {
                        @Override
                        public void onSuccess(ApiResponse apiResponse) {
                            contacts.remove(getAdapterPosition());
                            notifyDataSetChanged();
                            CommonUtils.showToast(context,apiResponse.getResponseMessage());
                        }

                        @Override
                        public void onFailure(ApiResponse apiResponse) {
                            CommonUtils.showToast(context,apiResponse.getResponseMessage());
                        }
                    });
                }
            });
        }
    }
}
