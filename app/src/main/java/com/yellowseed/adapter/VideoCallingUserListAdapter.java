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
import com.yellowseed.databinding.RowGroupuserListBinding;
import com.yellowseed.imageUtils.CircleTransformation;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.resModel.GroupMemberResonse;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.Themes;

import java.util.ArrayList;
import java.util.List;

public class VideoCallingUserListAdapter extends RecyclerView.Adapter<VideoCallingUserListAdapter.ListViewHolder> {

    Themes themes;
    private Context context;
    //private  List<GetChatResonse.GroupMemberResponse> roomList;
    private List<GroupMemberResonse.MembersBean> roomList;
    private OnItemClickListener listener;
    private Boolean darkThemeEnabled;

    public VideoCallingUserListAdapter(Context context, List<GroupMemberResonse.MembersBean> roomList, OnItemClickListener listener) {
        this.context = context;
        this.roomList = roomList;
        this.listener = listener;
        themes = new Themes(context);
        darkThemeEnabled = CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME);
    }

    @NonNull
    @Override
    public VideoCallingUserListAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_groupuser_list, parent, false);
        return new VideoCallingUserListAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final VideoCallingUserListAdapter.ListViewHolder holder, int position) {
        holder.binding.llTagFollowing.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        holder.binding.tvTagUserNameadd.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        holder.binding.tvUserImage.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));

        if (roomList.get(position).getName() != null) {
            holder.binding.tvTagUserNameadd.setText(roomList.get(position).getName());
        }
      /*  if (roomList.get(position).getImage() != null) {
            Picasso.with(context).load(roomList.get(position).getImage()).placeholder(R.drawable.user_placeholder).transform(new CircleTransformation()).into(holder.binding.ivTagUserPicadd);
        } else {
          //  Picasso.with(context).load(R.drawable.user_placeholder).placeholder(R.drawable.user_placeholder).transform(new CircleTransformation()).into(holder.binding.ivTagUserPicadd);
        }*/
        CommonUtils.otherUserProfile(context, holder.binding.ivTagUserPicadd,roomList.get(position).getImage(), holder.binding.tvUserImage,
                roomList.get(position).getName());


        holder.binding.cbChecked.setChecked(roomList.get(position).isSelected());

        holder.binding.llSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.llSelection, holder.getAdapterPosition());
            }
        });

        holder.binding.cbChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.llSelection, holder.getAdapterPosition());
            }
        });
      /*  for (int i = 0; i < response.body().getMembers().size(); i++) {
            if(models.get(i).getId().equalsIgnoreCase(new PrefManager(context).getUserId())){
                models.remove(i);
            }
        }*/


    }


    @Override
    public int getItemCount() {
        return roomList.size();
    }

    /*   public ArrayList<Integer> getQBids() {
           ArrayList<Integer> arlRoomIds=new ArrayList<>();
           for (int i = 0; i < roomList.size(); i++) {
               if(roomList.get(i).isSelected()){
                   arlRoomIds.add(Integer.valueOf(roomList.get(i).getQb_id()));
               }
           }
           return arlRoomIds;
       }*/
    public List<GroupMemberResonse.MembersBean> getQBids() {
        List<GroupMemberResonse.MembersBean> arlRoomIds = new ArrayList<>();
        for (int i = 0; i < roomList.size(); i++) {

            if (roomList.get(i).isSelected()) {
                arlRoomIds.add(roomList.get(i));
            }
        }
        return arlRoomIds;
    }


    public class ListViewHolder extends RecyclerView.ViewHolder {

        private RowGroupuserListBinding binding;

        public ListViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
