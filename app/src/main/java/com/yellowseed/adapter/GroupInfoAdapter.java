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
import com.yellowseed.databinding.LayoutGroupInfoBinding;
import com.yellowseed.imageUtils.CircleTransformation;
import com.yellowseed.model.resModel.GroupMemberResonse;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.RecyclerItemClickListener;
import com.yellowseed.utils.Themes;

import java.util.ArrayList;

/**
 * Created by mobiloitte on 17/5/18.
 */

public class GroupInfoAdapter extends RecyclerView.Adapter<GroupInfoAdapter.ListViewHolder> {

    private ArrayList<GroupMemberResonse.MembersBean> arrayList;
    private Context context;
    private Themes themes;
    private boolean darkThemeEnabled;
    private GroupMemberResonse.MembersBean model;
    private RecyclerItemClickListener.OnItemClickListener listener;

    public GroupInfoAdapter(ArrayList<GroupMemberResonse.MembersBean> arrayList, Context context, RecyclerItemClickListener.OnItemClickListener listener) {
        this.arrayList = arrayList;
        this.context = context;
        themes = new Themes(context);
        darkThemeEnabled = CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME);
        this.listener = listener;
    }



    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_group_info, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListViewHolder holder, int position) {
        holder.binding.tvGroupInfoName.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        holder.binding.viewLine.setBackgroundColor(ContextCompat.getColor(context, themes.setViewLineGrey()));
        holder.binding.tvUserImage.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));

        model = arrayList.get(position);

       /* if (model.getImage() != null && model.getImage().length() > 0) {
            Picasso.with(context).load(model.getImage()).transform(new CircleTransformation()).into(holder.binding.ivGroupInfoImage);
        } else {
           // Picasso.with(context).load(R.mipmap.userprofile_icon).transform(new CircleTransformation()).into(holder.binding.ivGroupInfoImage);


            CommonUtils.otherUserProfile(context, holder.binding.ivGroupInfoImage, arrayList.get(position).getImage(), holder.binding.tvUserImage,
                    arrayList.get(position).getName());
        }

        if (model.getName() != null) {
            holder.binding.tvGroupInfoName.setText(model.getName());
        } else {
            holder.binding.tvGroupInfoName.setText("User " + position);
        }
        if (model.isIs_admin()) {
            holder.binding.btnGroupInfoAdmin.setVisibility(View.VISIBLE);
        } else {
            holder.binding.btnGroupInfoAdmin.setVisibility(View.GONE);
        }
*/
        holder.binding.llRow.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onItemClick(holder.binding.llRow, holder.getAdapterPosition());
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        private LayoutGroupInfoBinding binding;

        public ListViewHolder(View itemView) {
            super(itemView);

            binding = DataBindingUtil.bind(itemView);
        }
    }
}
