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
import com.yellowseed.databinding.LayoutcontactBinding;
import com.yellowseed.databinding.RowNewCallBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.NewCallModel;
import com.yellowseed.model.UserListModel;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.Themes;

import java.util.ArrayList;

public class NewCallAdapter extends RecyclerView.Adapter<NewCallAdapter.ListViewHolder> {
    private ArrayList<NewCallModel> newCallModels;
    private Context context;
    private Themes themes;
    private boolean darkThemeEnabled;



    public NewCallAdapter(Context context, ArrayList<NewCallModel> newCallModels) {
        this.newCallModels = newCallModels;
        this.context = context;
        themes = new Themes(context);
    }


    @NonNull
    @Override
    public NewCallAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_new_call, parent, false);
        return new NewCallAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        holder.binding.llNewCall.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        holder.binding.tvCallerName.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        holder.binding.viewCall.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setViewLineGrey()));
        Themes.getInstance(context).changeShareIcon(context, holder.binding.ivAudioCall);
        Themes.getInstance(context).changeShareIcon(context, holder.binding.ivVideoCall);
        holder.binding.tvCallerName.setText(newCallModels.get(position).getName() );
        holder.binding.ivUserProfile.setImageResource(newCallModels.get(position).getImg());

    }

    @Override
    public int getItemCount() {
        return newCallModels.size();    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        private RowNewCallBinding binding;
        public ListViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
