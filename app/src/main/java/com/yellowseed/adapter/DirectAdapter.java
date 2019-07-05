package com.yellowseed.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SectionIndexer;

import com.yellowseed.R;
import com.yellowseed.databinding.LayoutDirectScreenBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.DirectModel;
import com.yellowseed.model.SendToModel;
import com.yellowseed.utils.Themes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mobiloitte on 3/5/18.
 */

public class DirectAdapter extends RecyclerView.Adapter<DirectAdapter.ListViewHolder> implements SectionIndexer {

    private ArrayList<DirectModel> arrayList;
    private Context context;
    private OnItemClickListener listener;
    private ArrayList<Integer> mSectionPositions;

    public DirectAdapter(ArrayList<DirectModel> arrayList, Context context, OnItemClickListener listener) {
        this.arrayList = arrayList;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_direct_screen, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, int position) {
//        Picasso.with(mctx).load(arrayList.get(position).getImage_url()).resize(400,400).into(holder.binding.statusImage);
        holder.binding.tvDirectUserName.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        holder.binding.tvDirectUserMessage.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        holder.binding.viewLine.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setViewLineGrey()));

        holder.binding.tvDirectUserName.setText(arrayList.get(position).getName_url());
        holder.binding.ivDirectUserPic.setImageResource(arrayList.get(position).getImage_url());
        holder.binding.tvDirectUserTime.setText(arrayList.get(position).getTime_ago_url());
        holder.binding.tvDirectUserMessage.setText(arrayList.get(position).getLast_message_url());
        holder.binding.llDirectUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.llDirectUser, holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public Object[] getSections() {
        List<String> sections = new ArrayList<>(26);
        mSectionPositions = new ArrayList<>(26);
        for (int i = 0, size = arrayList.size(); i < size; i++) {
            String section = String.valueOf(arrayList.get(i).getName_url().charAt(0)).toUpperCase();
            if (!sections.contains(section)) {
                sections.add(section);
                mSectionPositions.add(i);
            }
        }
        return sections.toArray(new String[0]);
    }

    @Override
    public int getPositionForSection(int i) {
        return mSectionPositions.get(i);
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    public void updatedList(ArrayList<DirectModel> filteresNames){
        arrayList = filteresNames;
        notifyDataSetChanged();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        private LayoutDirectScreenBinding binding;

        public ListViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
