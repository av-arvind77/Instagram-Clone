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
import com.yellowseed.databinding.LayoutcontactBinding;
import com.yellowseed.imageUtils.CircleTransformation;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.SendToModel;
import com.yellowseed.model.reqModel.ViewerModel;
import com.yellowseed.utils.PrefManager;
import com.yellowseed.utils.Themes;

import java.util.ArrayList;
import java.util.List;

public class DirectUserPendingAdapter extends RecyclerView.Adapter<DirectUserPendingAdapter.ListViewHolder> {
    private List<SendToModel> arrayList;
    private Context mctx;
    private OnItemClickListener listener;

    public DirectUserPendingAdapter(List<SendToModel> arrayList, Context mctx, OnItemClickListener listener) {
        this.arrayList = arrayList;
        this.mctx = mctx;
        this.listener = listener;
    }



    @NonNull
    @Override
    public DirectUserPendingAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layoutcontact, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DirectUserPendingAdapter.ListViewHolder holder, int position) {
        holder.binding.tvContactUserName.setTextColor(ContextCompat.getColor(mctx, Themes.getInstance(mctx).setDarkThemeText()));

        holder.binding.tvContactUserName.setText(arrayList.get(position).getName_url());
        holder.binding.ivContactUserPic.setImageResource(arrayList.get(position).getImage_url());

        holder.binding.llContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.llContacts, holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        private LayoutcontactBinding binding;

        public ListViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
