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
import com.yellowseed.databinding.RowPollResultDataBinding;
import com.yellowseed.model.resModel.PollResultData;
import com.yellowseed.utils.Themes;

import java.util.List;

public class PollResultAdapter extends RecyclerView.Adapter<PollResultAdapter.MyViewHolder> {

    private List<PollResultData> alPoll;
    private Context context;
    private Themes themes;

    public PollResultAdapter(Context context, List<PollResultData> alPoll) {
        this.alPoll = alPoll;
        this.context = context;
        themes = new Themes(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_poll_result_data, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.tvOptionName.setText(alPoll.get(position).getName());
        holder.binding.progressBar.setProgress(alPoll.get(position).getPer());
        holder.binding.tvPersentage.setText("" + alPoll.get(position).getPer() + "%");

        holder.binding.tvOptionName.setTextColor(ContextCompat.getColor(context, themes.setLightTheme()));
        holder.binding.tvPersentage.setTextColor(ContextCompat.getColor(context, themes.setLightTheme()));

        if (position == 0) {
            ViewGroup.LayoutParams params = holder.binding.tvResultData.getLayoutParams();
            params.width = 400;
            holder.binding.tvResultData.setLayoutParams(params);
            holder.binding.tvResultData.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_blue_light));
            //holder.binding.tvResultData.setLayoutParams(new LinearLayout.LayoutParams(130, LinearLayout.LayoutParams.WRAP_CONTENT));
        } else {
            ViewGroup.LayoutParams params = holder.binding.tvResultData.getLayoutParams();
            params.width = 200;
            holder.binding.tvResultData.setLayoutParams(params);
            holder.binding.tvResultData.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_gray_light));
            //  holder.binding.tvResultData.setLayoutParams(new LinearLayout.LayoutParams(70, LinearLayout.LayoutParams.WRAP_CONTENT));
        }
    }

    @Override
    public int getItemCount() {
        return alPoll.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        RowPollResultDataBinding binding;

        public MyViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
