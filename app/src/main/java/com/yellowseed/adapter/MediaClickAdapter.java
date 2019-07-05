package com.yellowseed.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yellowseed.R;
import com.yellowseed.databinding.RowImageBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.MediaClickModel;

import java.util.List;

public class MediaClickAdapter extends RecyclerView.Adapter<MediaClickAdapter.MyViewHolder> {
    private Context context;
    private List<MediaClickModel> arImageList;
    private OnItemClickListener listener;

    public MediaClickAdapter(Context context, List<MediaClickModel> arImageList, OnItemClickListener onItemClickListener) {

        this.context = context;
        this.arImageList = arImageList;
        this.listener = onItemClickListener;


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_image, parent, false);
        return new MediaClickAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MediaClickModel model = arImageList.get(position);
        if (model != null)

            holder.binding.ivMediaImage.setImageResource(model.getImg());

    }

    @Override
    public int getItemCount() {
        return arImageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private RowImageBinding binding;

        public MyViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            binding.ivMediaImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(binding.ivMediaImage,getAdapterPosition());
                }
            });
        }

    }
}
