package com.yellowseed.adapter;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.yellowseed.R;
import com.yellowseed.databinding.LayoutNewChatBinding;
import com.yellowseed.databinding.LayoutSearchFollowingBinding;
import com.yellowseed.databinding.UnfollowDialogBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.SearchFollowingModel;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.Themes;

import java.util.ArrayList;

public class NewChatAdapter extends RecyclerView.Adapter<NewChatAdapter.MyViewHolder> {

    private ArrayList<SearchFollowingModel> arlModel;
    private Context mContext;
    private boolean flag;
    private int pos = -1;
    private OnItemClickListener listener;
    private SearchFollowingModel model;
    private Themes themes;
    private Boolean darkThemeEnabled;
    private Dialog dialog;


    public NewChatAdapter(ArrayList<SearchFollowingModel> arlModel, Context mContext, OnItemClickListener listener) {


        this.arlModel = arlModel;
        this.mContext = mContext;
        this.listener = listener;
        themes = new Themes(mContext);
        darkThemeEnabled = CommonUtils.getPreferencesBoolean(mContext, AppConstants.DARK_THEME);
    }


    @NonNull
    @Override
    public NewChatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_new_chat, parent, false);
        return new NewChatAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.binding.tvUserName.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
        myViewHolder.binding.tvUserImage.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));

        myViewHolder.binding.tvUserName.setText(arlModel.get(i).getName_url());
        myViewHolder.binding.ivSearchFollowingUserPic.setImageResource(arlModel.get(i).getImage_url());
        myViewHolder.binding.llFollowingAnother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(myViewHolder.binding.llFollowingAnother, myViewHolder.getAdapterPosition());
            }
        });


    }


    @Override
    public int getItemCount() {
        return arlModel.size();
    }

    public void updatedList(ArrayList<SearchFollowingModel> filteresNames) {
        arlModel = filteresNames;
        notifyDataSetChanged();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private LayoutNewChatBinding binding;

        public MyViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
