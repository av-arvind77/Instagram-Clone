package com.yellowseed.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.yellowseed.R;
import com.yellowseed.databinding.LayoutTagFollowingDoneBinding;
import com.yellowseed.imageUtils.CircleTransformation;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.resModel.GetRoomResonse;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.Themes;

import java.util.ArrayList;
import java.util.List;

public class TagFollowingAdapter extends RecyclerView.Adapter<TagFollowingAdapter.ListViewHolder> {

    private Context context;
    private ArrayList<GetRoomResonse.RoomsBean> roomList;
    private GetRoomResonse.RoomsBean model;
    private OnItemClickListener listener;

    public TagFollowingAdapter( Context context, ArrayList<GetRoomResonse.RoomsBean> roomList, OnItemClickListener listener) {
        this.context = context;
        this.roomList = roomList;
        this.listener = listener;
    }


    @NonNull
    @Override
    public TagFollowingAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_tag_following_done, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TagFollowingAdapter.ListViewHolder holder, int position) {
        Boolean darkThemeEnabled = CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME);
        Themes themes = new Themes(context);
        holder.binding.tvTagUserNameadd.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
       /* model = roomList.get(position);
        if (model.getName() != null) {
            holder.binding.tvTagUserNameadd.setText(model.getName());
        }*/
      /*  if (model.getImage() != null){
            Picasso.with(context).load(model.getImage()).placeholder(R.drawable.user_placeholder).transform(new CircleTransformation()).into(holder.binding.ivTagUserPicadd);
        }else {*/
           // Picasso.with(context).load(R.drawable.user_placeholder).placeholder(R.drawable.user_placeholder).transform(new CircleTransformation()).into(holder.binding.ivTagUserPicadd);

         /*   CommonUtils.otherUserProfile(context,holder.binding.ivTagUserPicadd,model.getImage(),holder.binding.tvUserImage,
                    model.getName());*/



       // holder.binding.cbChecked.setChecked(roomList.get(position).isIs_selected());

        holder.binding.llSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.llSelection,holder.getAdapterPosition());
            }
        });

        holder.binding.cbChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.llSelection,holder.getAdapterPosition());
            }
        });


    }
    @Override
    public int getItemCount() {
        return 5;
    }

    public List<String> getSelected() {
        List<String> arlRoomIds=new ArrayList<>();
        for (int i = 0; i < roomList.size(); i++) {
            if(roomList.get(i).isIs_selected()){
                arlRoomIds.add(roomList.get(i).getRoom_id());
            }
        }

        return arlRoomIds;
    }


    public class ListViewHolder extends RecyclerView.ViewHolder {

        private LayoutTagFollowingDoneBinding binding;

        public ListViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
