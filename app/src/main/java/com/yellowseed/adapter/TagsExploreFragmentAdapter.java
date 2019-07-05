package com.yellowseed.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.yellowseed.R;
import com.yellowseed.activity.ShowPostItemsActivity;
import com.yellowseed.databinding.LayoutTrendingPostBinding;
import com.yellowseed.databinding.LayoutcontactBinding;
import com.yellowseed.fragments.FragmentExploreSearch.TagsExploreFragment;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.SearchFollowingModel;
import com.yellowseed.model.SendToModel;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.webservices.response.post.Post;

import java.util.ArrayList;

public class TagsExploreFragmentAdapter extends RecyclerView.Adapter<TagsExploreFragmentAdapter.ListViewHolder> {

    Themes themes;
    private Boolean darkThemeEnabled;
    private ArrayList<SendToModel> arrayList;
    private Context context;
    private OnItemClickListener listener;




    public TagsExploreFragmentAdapter(ArrayList<SendToModel> arrayList, Context context, OnItemClickListener listener) {
        this.arrayList = arrayList;
        this.context = context;
        this.listener = listener;
        darkThemeEnabled = CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME);
    }


    /*public TagsExploreFragmentAdapter(Context context, ArrayList<com.yellowseed.webservices.response.homepost.Post> arrayList, OnItemClickListener listener) {
        this.posts = arrayList;
        this.mContext = context;
        this.listener = listener;
        darkThemeEnabled = CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME);
        themes = new Themes(mContext);
    }*/


    @NonNull
    @Override
    public TagsExploreFragmentAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layoutcontact, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TagsExploreFragmentAdapter.ListViewHolder holder, final int position) {

        /*holder.binding.tvContactUserName.setText(arrayList.get(position).getName_url());
        holder.binding.ivContactUserPic.setImageResource(arrayList.get(position).getImage_url());
        holder.binding.llContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.llContacts, holder.getAdapterPosition());
            }
        });*/
        holder.binding.tvContactUserName.setText(arrayList.get(position).getName_url());
        holder.binding.ivContactUserPic.setImageResource(arrayList.get(position).getImage_url());
        holder.binding.llContacts.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        holder.binding.tvContactUserName.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        holder.binding.llContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.binding.llContacts, holder.getAdapterPosition());
            }
        });


        //post = posts.get(holder.getAdapterPosition());
       /* int displayWidth = mContext.getResources().getDisplayMetrics().widthPixels / 3;
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams((int) displayWidth, (int) displayWidth);
        holder.binding.llListGrid.setLayoutParams(layoutParams);
        *//*if (post.getImages() != null && post.getImages().size() > 0) {
            if (post.getImages().get(0).getFile().getUrl() != null && post.getImages().get(0).getFile().getUrl() != "") {
                holder.binding.ivGrirdPostImage.setVisibility(View.VISIBLE);
                holder.binding.tvGridPostTitle.setVisibility(View.GONE);
                // Picasso.with(mContext).load(post.getImages().get(0).getFile().getUrl()).into(holder.binding.ivGrirdPostImage);
            } else {
                holder.binding.ivGrirdPostImage.setVisibility(View.GONE);
                if (post.getPost().getDescription() != null && post.getPost().getDescription() != "") {
                    holder.binding.tvGridPostTitle.setText(post.getPost().getDescription());
                    holder.binding.tvGridPostTitle.setVisibility(View.VISIBLE);
                }
            }
        } else {
            if (post.getPost().getDescription() != null && post.getPost().getDescription() != "") {
                holder.binding.tvGridPostTitle.setText(post.getPost().getDescription());
                holder.binding.tvGridPostTitle.setVisibility(View.VISIBLE);
                holder.binding.ivGrirdPostImage.setVisibility(View.GONE);
            }
        }*/

       /* if (position == 1 || position == 5 || position == 8) {
            holder.binding.tvGridPostTitle.setVisibility(View.VISIBLE);
            holder.binding.ivGrirdPostImage.setVisibility(View.GONE);
        } else {
            holder.binding.tvGridPostTitle.setVisibility(View.GONE);
            holder.binding.ivGrirdPostImage.setVisibility(View.VISIBLE);

        }

        holder.binding.llListGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, ShowPostItemsActivity.class)
                        .putExtra("post_id", "")
                        //  .putExtra("post_id", posts.get(position).getPost().getId())
                        .putExtra("shared_id", "")
                        //.putExtra("shared_id", posts.get(position).getSharedId())
                        .putExtra(AppConstants.IS_USER_POST, true));

            }
        });*/


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        private LayoutcontactBinding binding;
        public ListViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
    public void updatedList(ArrayList<SendToModel> filteresNames){
        arrayList = filteresNames;
        notifyDataSetChanged();
    }

}
