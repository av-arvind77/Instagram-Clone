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
import com.yellowseed.databinding.LayoutlinksgroupBinding;
import com.yellowseed.model.LinksModel;
import com.yellowseed.utils.Themes;

import java.util.ArrayList;

public class LinksAdapter extends RecyclerView.Adapter<LinksAdapter.MyViewHolder>{

    private ArrayList<LinksModel> models;
    private Context context;

    public LinksAdapter(ArrayList<LinksModel> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layoutlinksgroup,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

      /*  holder.binding.tvWeekDay.setText(models.get(position).getWeekDay());
        holder.binding.tvHeadingTittle.setText(models.get(position).getHeading());
        holder.binding.tvDetails.setText(models.get(position).getDetails());
*/
//        if(position == 0){
//            holder.binding.tvWeekDay.setVisibility(View.VISIBLE);
//        }

        holder.binding.tvHeadingTittle.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        holder.binding.tvDetails.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));


    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

       private LayoutlinksgroupBinding binding;

        public MyViewHolder(View itemView) {
            super(itemView);

            binding = DataBindingUtil.bind(itemView);
        }
    }
}
