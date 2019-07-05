package com.yellowseed.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.yellowseed.R;
import com.yellowseed.activity.MyProfileActivity;
import com.yellowseed.databinding.LayoutTaggedUserBinding;
import com.yellowseed.imageUtils.CircleTransformation;
import com.yellowseed.model.resModel.TagUserListResponse;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.webservices.response.homepost.TagFriend;

import java.util.ArrayList;

/**
 * Created by rajat_mobiloitte on 4/7/18.
 */

public class TagFriendsAdapter extends RecyclerView.Adapter<TagFriendsAdapter.ListViewHolder> {

    private Context context;
    private ArrayList<TagUserListResponse.UsersBean> tagUserList;



    public TagFriendsAdapter( Context context,ArrayList<TagUserListResponse.UsersBean> tagUserList) {
        this.context = context;
        this.tagUserList=tagUserList;
    }


    @NonNull
    @Override
    public TagFriendsAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_tagged_user, parent, false);
        return new TagFriendsAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TagFriendsAdapter.ListViewHolder holder, final int position) {
//        holder.binding.tvTagUserNameadd.setText(arrayList.get(position).getName_url());
//        holder.binding.ivTagUserPicadd.setImageResource(arrayList.get(position).getImage_url());
        holder.binding.tvTagUserName.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));

        holder.binding.tvTagUserName.setText(tagUserList.get(position).getName());

        if (tagUserList.get(position).getImage()!=null) {
            Picasso.with(context).load(tagUserList.get(position).getImage()).placeholder(R.drawable.user_placeholder).
                    transform(new CircleTransformation()).into(((ListViewHolder) holder).binding.ivContactUserPic);

            //.memoryPolicy(MemoryPolicy.NO_CACHE)
        }else{
          //  Picasso.with(context).load(R.mipmap.userprofile_icon).into(((ListViewHolder) holder).binding.ivContactUserPic);

            CommonUtils.otherUserProfile(context,holder.binding.ivContactUserPic,tagUserList.get(position).getImage(),holder.binding.tvUserImage,
                    tagUserList.get(position).getName());

        }
        holder.binding.llContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, MyProfileActivity.class).putExtra("user_id",tagUserList.get(position).getId()));
            }
        });



    }
    @Override
    public int getItemCount() {
        return tagUserList.size();
    }


    public class ListViewHolder extends RecyclerView.ViewHolder {

        private LayoutTaggedUserBinding binding;

        public ListViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
