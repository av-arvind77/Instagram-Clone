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
import com.yellowseed.databinding.RowVotersBinding;
import com.yellowseed.imageUtils.CircleTransformation;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.SendToModel;
import com.yellowseed.model.resModel.StoryDetailResponseModel;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.Themes;

import java.util.ArrayList;
import java.util.List;


public class StoryViewVotersAdapter extends RecyclerView.Adapter<StoryViewVotersAdapter.MyViewHolder> {

    private List<SendToModel> arrayList;
    private Context mctx;
    private OnItemClickListener listener;

    public StoryViewVotersAdapter(List<SendToModel> arrayList, Context mctx, OnItemClickListener listener) {
        this.arrayList = arrayList;
        this.mctx = mctx;
        this.listener = listener;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_voters, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.tvVoted.setTextColor(ContextCompat.getColor(mctx, Themes.getInstance(mctx).setDarkThemeText()));
        holder.binding.tvVotersName.setTextColor(ContextCompat.getColor(mctx, Themes.getInstance(mctx).setDarkThemeText()));
        holder.binding.tvUserImage.setTextColor(ContextCompat.getColor(mctx, Themes.getInstance(mctx).setDarkThemeText()));
        holder.binding.tvVotersName.setText(arrayList.get(position).getName_url());
        holder.binding.ivVoters.setImageResource(arrayList.get(position).getImage_url());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RowVotersBinding binding;

        public MyViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
