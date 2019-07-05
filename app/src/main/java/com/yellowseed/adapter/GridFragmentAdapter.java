package com.yellowseed.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yellowseed.R;
import com.yellowseed.activity.ChatScreenActivity;
import com.yellowseed.activity.ShowPostItemsActivity;
import com.yellowseed.databinding.RowGridFragmentBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.utils.ActivityController;

/**
 * Created by mobiloitte on 18/5/18.
 */

public class GridFragmentAdapter extends RecyclerView.Adapter<GridFragmentAdapter.MyViewHolder> {

    private Context context;
    private GridAdapter gridAdapter;
    private OnItemClickListener listener;
    public GridFragmentAdapter(Context context, OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_grid_fragment,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context,3);
        holder.binding.rvImageSearchStory.setLayoutManager(layoutManager);

      /*  gridAdapter = new GridAdapter(context, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.ivStaggeredImageSearch:
                        ActivityController.startActivity(context, ShowPostItemsActivity.class);
                        break;
                }
            }
        });*/
        holder.binding.tvTextSearchStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.tvTextSearchStory, holder.getAdapterPosition());
            }
        });
        holder.binding.rvImageSearchStory.setAdapter(gridAdapter);

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private RowGridFragmentBinding binding;

        public MyViewHolder(View itemView) {
            super(itemView);

            binding = DataBindingUtil.bind(itemView);
        }
    }
}
