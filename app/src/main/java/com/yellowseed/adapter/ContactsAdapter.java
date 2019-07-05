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
import com.yellowseed.databinding.LayoutcontactBinding;
import com.yellowseed.imageUtils.CircleTransformation;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.SendToModel;
import com.yellowseed.model.UserListModel;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.Themes;

import java.util.ArrayList;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ListViewHolder> {

    //private ArrayList<UserListModel> arrayList;
    private Context mctx;
    private ArrayList<SendToModel> arrayList;

    private Themes themes;
    private boolean darkThemeEnabled;
    private OnItemClickListener listener;

    public ContactsAdapter(ArrayList<SendToModel> arrayList, Context mctx, OnItemClickListener listener) {
        this.arrayList = arrayList;
        this.mctx = mctx;
        themes = new Themes(mctx);
        darkThemeEnabled = CommonUtils.getPreferencesBoolean(mctx, AppConstants.DARK_THEME);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ContactsAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layoutcontact, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListViewHolder holder, int position) {
        holder.binding.tvContactUserName.setTextColor(ContextCompat.getColor(mctx,themes.setDarkThemeText()));
        holder.binding.tvUserImage.setTextColor(ContextCompat.getColor(mctx, Themes.getInstance(mctx).setDarkThemeText()));

       /* holder.binding.tvContactUserName.setText(arrayList.get(position).getName());

      *//*  if (arrayList.get(position).getImage() != null) {
            Picasso.with(mctx).load(arrayList.get(position).getImage()).placeholder(R.drawable.user_placeholder).
                    transform(new CircleTransformation()).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.binding.ivContactUserPic);
        }
*//*

        CommonUtils.otherUserProfile(mctx,holder.binding.ivContactUserPic,arrayList.get(position).getImage(),holder.binding.tvUserImage,
                arrayList.get(position).getName());


        holder.binding.llContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.llContacts, holder.getAdapterPosition());
            }
        });*/


        holder.binding.tvContactUserName.setText(arrayList.get(position).getName_url());
        holder.binding.ivContactUserPic.setImageResource(arrayList.get(position).getImage_url());

        holder.binding.llContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.llContacts, holder.getAdapterPosition());
            }
        });



    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void updatedList(ArrayList<SendToModel> filteresNames){
        arrayList = filteresNames;
        notifyDataSetChanged();
    }
    public class ListViewHolder extends RecyclerView.ViewHolder {
        private LayoutcontactBinding binding;
        public ListViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
