package com.yellowseed.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yellowseed.R;
import com.yellowseed.databinding.LayoutExploresearchBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.FollowingRequestModel;
import com.yellowseed.model.SendToModel;
import com.yellowseed.utils.Themes;
import com.yellowseed.webservices.response.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ProfileExploreFragmentAdapter extends RecyclerView.Adapter<ProfileExploreFragmentAdapter.ListViewHolder>{
    private ArrayList<FollowingRequestModel> arrayList;

    private Context context;
    private OnItemClickListener listener;
    private int pos = 0;
    boolean isLast  = false;

    public ProfileExploreFragmentAdapter(ArrayList<FollowingRequestModel> followingRequestModels, Context context, OnItemClickListener listener) {
        this.arrayList = followingRequestModels;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProfileExploreFragmentAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_exploresearch, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProfileExploreFragmentAdapter.ListViewHolder holder, int position) {
        holder.binding.tvExploreSearchName.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        holder.binding.tvSugestedRecent.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        holder.binding.tvExploreSearchFollowers.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkGreyStoryTextColor()));


        holder.binding.tvExploreSearchName.setText(arrayList.get(position).getUserFollowingName());
        holder.binding.ivExploreSearchUser.setImageResource(arrayList.get(position).getUserFollowersPicture());
        holder.binding.tvExploreSearchFollowers.setText(arrayList.get(position).getUserFollowers());
        holder.binding.llExploreSearchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.llExploreSearchUser, holder.getAdapterPosition());
            }
        });
        if (arrayList.get(position).getType().equalsIgnoreCase("Suggestion")) {
            holder.binding.tvExploreSearchFollowers.setVisibility(View.VISIBLE);
            pos = position;
            isLast = false;
        }
        else {
            isLast = true;
            holder.binding.tvSugestedRecent.setText("Recent");
            holder.binding.tvExploreSearchFollowers.setVisibility(View.GONE);
        }

        if ( position == 0 && !isLast) {

            holder.binding.tvSugestedRecent.setVisibility(View.VISIBLE);
            holder.binding.tvSugestedRecent.setText("Suggestion");
        }
        else if(position == pos + 1 && isLast || position == 0){

            holder.binding.tvSugestedRecent.setVisibility(View.VISIBLE);
            holder.binding.tvSugestedRecent.setText("Recent");
            holder.binding.tvSugestedRecent.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));

        }
        else {
            holder.binding.tvSugestedRecent.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        private LayoutExploresearchBinding binding;
        public ListViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
    public void updatedList(ArrayList<FollowingRequestModel> filteresNames){
        isLast  = false;
        pos = 0;
        arrayList.clear();
        arrayList.addAll(filteresNames);
        Collections.sort(arrayList, new Comparator<FollowingRequestModel>() {
            @Override
            public int compare(FollowingRequestModel o1, FollowingRequestModel o2) {

                return o2.getType().compareTo(o1.getType());
            }
        });
        notifyDataSetChanged();
    }

}
