package com.yellowseed.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.yellowseed.R;
import com.yellowseed.databinding.LayoutDirectAddBinding;
import com.yellowseed.imageUtils.CircleTransformation;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.DirectModel;
import com.yellowseed.model.UserListModel;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.Themes;

import java.util.ArrayList;
import java.util.List;

public class DirectUserAddAdapter extends RecyclerView.Adapter<DirectUserAddAdapter.ListViewHolder> {
    private Context mContext;
    private OnItemClickListener listener;
    private ArrayList<DirectModel> searchUserList;
    private Themes themes;

    public DirectUserAddAdapter(ArrayList<DirectModel> searchUserList, Context context, OnItemClickListener listener) {
        this.searchUserList = searchUserList;
        this.mContext = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DirectUserAddAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_direct_add, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DirectUserAddAdapter.ListViewHolder holder, final int position) {
        Boolean darkThemeEnabled = CommonUtils.getPreferencesBoolean(mContext, AppConstants.DARK_THEME);
        themes = new Themes(mContext);
        holder.binding.tvDirectUserNameadd.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
        holder.binding.tvUserImage.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        holder.binding.ivDirectUserPicadd.setImageResource(searchUserList.get(position).getImage_url());
       holder.binding.tvDirectUserNameadd.setText(searchUserList.get(position).getName_url());

     //   CommonUtils.otherUserProfile(mContext,holder.binding.ivDirectUserPicadd,searchUserList.get(position).getImage(),holder.binding.tvUserImage,
          //      searchUserList.get(position).getName());

      //  Picasso.with(mContext).load(searchUserList.get(position).getImage()).transform(new CircleTransformation()).placeholder(R.drawable.user_placeholder).memoryPolicy(MemoryPolicy.NO_CACHE).transform(new CircleTransformation()).into(holder.binding.ivDirectUserPicadd);
        holder.binding.chechBoxDirectadd.setChecked(searchUserList.get(position).isChecked());

        holder.binding.llDirectUseradd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, holder.getAdapterPosition());
            }
        });
        holder.binding.chechBoxDirectadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchUserList.size();
    }

    public void updatedList(ArrayList<DirectModel> filteresNames) {
        searchUserList = filteresNames;
        notifyDataSetChanged();
    }

    public List<DirectModel> getTaggedFriends() {
        List<DirectModel> arlTaggedFriends = new ArrayList<>();
        for (DirectModel model : searchUserList) {
            if (model.isChecked()) {
                arlTaggedFriends.add(model);
            }
        }
        return arlTaggedFriends;
    }


    public class ListViewHolder extends RecyclerView.ViewHolder {
        private LayoutDirectAddBinding binding;

        public ListViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }


}
