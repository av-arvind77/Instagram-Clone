package com.yellowseed.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.yellowseed.R;
import com.yellowseed.databinding.LayoutDirectAddBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.listener.OnRadioItemClickListener;
import com.yellowseed.model.DirectModel;
import com.yellowseed.model.SendToModel;
import com.yellowseed.utils.Themes;

import java.util.ArrayList;

public class DirectUserAddAdapterDemo extends RecyclerView.Adapter<DirectUserAddAdapterDemo.ListViewHolder> {
    private ArrayList<DirectModel> arrayList;
    private Context context;
    private OnRadioItemClickListener listener;

    public DirectUserAddAdapterDemo(ArrayList<DirectModel> arrayList, Context context, OnRadioItemClickListener listener) {
        this.arrayList = arrayList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DirectUserAddAdapterDemo.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_direct_add, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DirectUserAddAdapterDemo.ListViewHolder holder, int position) {

        holder.binding.tvDirectUserNameadd.setTextColor(ContextCompat.getColor(context,Themes.getInstance(context).setDarkThemeText()));


        holder.binding.tvDirectUserNameadd.setText(arrayList.get(position).getName_url());
        holder.binding.ivDirectUserPicadd.setImageResource(arrayList.get(position).getImage_url());

        holder.binding.chechBoxDirectadd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listener.onItemClick(holder.binding.llDirectChat, isChecked, holder.getAdapterPosition());
            }
        });

        /*holder.binding.llDirectChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.llDirectChat, holder.getAdapterPosition());
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void updatedList(ArrayList<DirectModel> filteresNames){
        arrayList = filteresNames;
        notifyDataSetChanged();
    }


    public class ListViewHolder extends RecyclerView.ViewHolder {
        private LayoutDirectAddBinding binding;

        public ListViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
