package com.yellowseed.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yellowseed.R;
import com.yellowseed.databinding.LayoutTagFollowingDoneBinding;

public class ShowPostItemAdapter extends RecyclerView.Adapter<ShowPostItemAdapter.ListViewHolder> {

    private Context context;

    public ShowPostItemAdapter( Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ShowPostItemAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_tag_following_done, parent, false);
        return new ShowPostItemAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return 7;
    }


    public class ListViewHolder extends RecyclerView.ViewHolder {

        private LayoutTagFollowingDoneBinding binding;

        public ListViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
