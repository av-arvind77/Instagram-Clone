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
import com.yellowseed.databinding.RowVoterListBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.MediaClickModel;
import com.yellowseed.model.VotesModel;
import com.yellowseed.utils.Themes;

import java.util.List;

public class VotesAdapter extends RecyclerView.Adapter<VotesAdapter.MyViewHolder>  {
    private Context context;
    private List<VotesModel>arVotesList;
    private OnItemClickListener listener;

    public VotesAdapter(Context context, List<VotesModel> arVotesList, OnItemClickListener onItemClickListener) {
      this.context=context;
      this.arVotesList=arVotesList;
      this.listener=onItemClickListener;

    }


    @NonNull
    @Override
    public VotesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_voter_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VotesAdapter.MyViewHolder holder, int position) {
        holder.binding.tvVotersName.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setToolbarWhiteText()));
        VotesModel model = arVotesList.get(position);
        if (model != null)
        {
            holder.binding.ivVoters.setImageResource(model.getImg());
            holder.binding.tvVotersName.setText(model.getName());

        }
    }


    @Override
    public int getItemCount() {
        return arVotesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private RowVoterListBinding binding;
        public MyViewHolder(View itemView) {

            super(itemView);
            binding=DataBindingUtil.bind(itemView);
            binding.llMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(binding.llMain,getAdapterPosition());
                }
            });
        }
    }
}
