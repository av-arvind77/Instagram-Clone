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

public class DirectShareAdapter extends RecyclerView.Adapter<DirectShareAdapter.MyViewHolder> {
    private Context context;

    public DirectShareAdapter(Context mContext) {
        this.context=mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_tag_following_done, parent, false);
        return new DirectShareAdapter.MyViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
      /*  holder.binding.tvTagUserNameadd.setText(arrayList.get(position).getName_url());
        holder.binding.ivTagUserPicadd.setImageResource(arrayList.get(position).getImage_url());

*/
    }

    @Override
    public int getItemCount() {
        return 7;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private LayoutTagFollowingDoneBinding binding;

        public MyViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
