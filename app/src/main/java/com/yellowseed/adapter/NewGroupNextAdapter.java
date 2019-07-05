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
import com.yellowseed.databinding.LayoutDirectAddBinding;
import com.yellowseed.imageUtils.CircleTransformation;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.DirectModel;
import com.yellowseed.model.SendToModel;
import com.yellowseed.model.UserListModel;
import com.yellowseed.model.resModel.GroupMemberResonse;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.Themes;

import java.util.ArrayList;
import java.util.List;

import de.measite.minidns.record.A;

public class NewGroupNextAdapter extends RecyclerView.Adapter<NewGroupNextAdapter.ListViewHolder> {
    private ArrayList<DirectModel> arrayList;
   // private List<GroupMemberResonse.MembersBean> arlMembersList;
    private Context context;
    private OnItemClickListener listener;
    private Themes themes;
    private boolean darkThemeEnabled;
    public NewGroupNextAdapter(ArrayList<DirectModel> arrayList, Context context, OnItemClickListener listener) {
        this.arrayList = arrayList;
        themes = new Themes(context);
        darkThemeEnabled = CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME);
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NewGroupNextAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_direct_add, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NewGroupNextAdapter.ListViewHolder holder, int position) {
        holder.binding.tvDirectUserNameadd.setTextColor(ContextCompat.getColor(context,themes.setDarkThemeText()));
     /*   if (arrayList.get(position).getName() != null) {
            holder.binding.tvDirectUserNameadd.setText(arrayList.get(position).getName());
        }

    *//*    if (arrayList.get(position).getImage() != null){
            Picasso.with(context).load(arrayList.get(position).getImage()).placeholder(R.drawable.user_placeholder).
                    transform(new CircleTransformation()).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.binding.ivDirectUserPicadd);

        }*//*
        CommonUtils.otherUserProfile(context,holder.binding.ivDirectUserPicadd,arrayList.get(position).getImage(),holder.binding.tvUserImage,
                arrayList.get(position).getName());


        holder.binding.chechBoxDirectadd.setChecked(arrayList.get(position).isChecked());

        holder.binding.chechBoxDirectadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.chechBoxDirectadd,holder.getAdapterPosition());
            }
        });*/


        holder.binding.tvDirectUserNameadd.setText(arrayList.get(position).getName_url());
        holder.binding.ivDirectUserPicadd.setImageResource(arrayList.get(position).getImage_url());
        holder.binding.chechBoxDirectadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.chechBoxDirectadd,holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

 /*   public void updatedList(ArrayList<UserListModel> filteresNames){
        arrayList = filteresNames;
        notifyDataSetChanged();
    }

    public List<String> getSelectedIds() {
    List<String> strings=new ArrayList<>();
        for (int i = 0; i < arrayList.size(); i++) {
            if(arrayList.get(i).isChecked()){
                strings.add(arrayList.get(i).getId());
            }
        }

        return strings;
    }*/



    public List<String> getSelectedIds() {
        List<String> strings=new ArrayList<>();
        for (int i = 0; i < arrayList.size(); i++) {
            if(arrayList.get(i).isChecked()){
                strings.add(arrayList.get(i).getId());
            }
        }

        return strings;
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
