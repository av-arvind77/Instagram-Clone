package com.yellowseed.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.yellowseed.R;
import com.yellowseed.activity.ShowPostItemsActivity;
import com.yellowseed.databinding.FooterExploreFragmentBinding;
import com.yellowseed.databinding.HeaderExploreFragmentBinding;
import com.yellowseed.databinding.LayoutTrendingPostBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.resModel.StoryListModel;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.webservices.response.User;
import com.yellowseed.webservices.response.homepost.Post;

import java.io.IOException;
import java.util.ArrayList;

public class ExploreViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;
    Themes themes;
    private HomeStoriesAdapter homeStoriesAdapter;
    private TrendingProfileAdapter trendingProfileAdapter;
    private ArrayList<Post> posts;
    private Post post;
    private ArrayList<StoryListModel.StoriesBean> storyList;
    private ArrayList<User> userList;
    private Context mContext;
    private OnItemClickListener listener;
    private int pos = -1;
    private HeaderExploreFragmentBinding bindingHeader;
    private FooterExploreFragmentBinding bindingFooter;
    private Boolean darkThemeEnabled;

    public ExploreViewAdapter(ArrayList<Post> posts, ArrayList<StoryListModel.StoriesBean> storyList, ArrayList<User> userList, Context context, OnItemClickListener listener) throws IOException {
        this.posts = posts;
        this.mContext = context;
        this.listener = listener;
        this.storyList = storyList;
        this.userList = userList;
        themes = new Themes(context);
        darkThemeEnabled = CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME);

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_trending_post, parent, false);
            return new ItemViewHolder(view);

        } else if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_explore_fragment, parent, false);
            return new HeaderViewHolder(view);

        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_explore_fragment, parent, false);
            return new FooterViewHolder(view);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        } else if (position == 15 + 1) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    public boolean isPositionHeader(int position) {
        return position == 0;
    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder) {
            {
                ((ItemViewHolder) holder).layoutUserPostBinding.clLayoutUser.setBackgroundColor(ContextCompat.getColor(mContext, themes.setDarkTheme()));
                ((ItemViewHolder) holder).layoutUserPostBinding.tvGridPostTitle.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
                // post = posts.get(holder.getAdapterPosition() - 1);
                int displayWidth = mContext.getResources().getDisplayMetrics().widthPixels / 3;
                ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams((int) displayWidth, (int) displayWidth);
                ((ExploreViewAdapter.ItemViewHolder) holder).layoutUserPostBinding.llListGrid.setLayoutParams(layoutParams);

                ((ExploreViewAdapter.ItemViewHolder) holder).layoutUserPostBinding.llListGrid.setVisibility(View.VISIBLE);


                if (position == 2 || position == 7 || position == 12) {
                    ((ItemViewHolder) holder).layoutUserPostBinding.ivGrirdPostImage.setVisibility(View.GONE);
                    ((ItemViewHolder) holder).layoutUserPostBinding.tvGridPostTitle.setVisibility(View.VISIBLE);

                } else {

                    ((ItemViewHolder) holder).layoutUserPostBinding.ivGrirdPostImage.setVisibility(View.VISIBLE);
                    ((ItemViewHolder) holder).layoutUserPostBinding.tvGridPostTitle.setVisibility(View.GONE);

                }



               /* if (post.getImages() != null && post.getImages().size() > 0) {
                    if (post.getImages().get(0).getFile().getUrl() != null && post.getImages().get(0).getFile().getUrl() != "") {
                        ((ExploreViewAdapter.ItemViewHolder) holder).layoutUserPostBinding.ivGrirdPostImage.setVisibility(View.VISIBLE);
                        Picasso.with(mContext).load(post.getImages().get(0).getFile().getUrl()).into(((ExploreViewAdapter.ItemViewHolder) holder).layoutUserPostBinding.ivGrirdPostImage);
                    } else {
                        if (post.getPost().getDescription() != null && post.getPost().getDescription() != "") {
                            ((ExploreViewAdapter.ItemViewHolder) holder).layoutUserPostBinding.tvGridPostTitle.setText(post.getPost().getDescription());
                            ((ExploreViewAdapter.ItemViewHolder) holder).layoutUserPostBinding.tvGridPostTitle.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    if (post.getPost().getDescription() != null && post.getPost().getDescription() != "") {
                        ((ExploreViewAdapter.ItemViewHolder) holder).layoutUserPostBinding.tvGridPostTitle.setText(post.getPost().getDescription());
                        ((ExploreViewAdapter.ItemViewHolder) holder).layoutUserPostBinding.tvGridPostTitle.setVisibility(View.VISIBLE);
                    }
                }*/


            }
        }
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).setHeaderAdapter();
        }
        if (holder instanceof FooterViewHolder) {
            ((FooterViewHolder) holder).setFooterAdapter();
        }
    }

    @Override
    public int getItemCount() {
        return 15 + 2;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public LayoutTrendingPostBinding layoutUserPostBinding;

        public ItemViewHolder(View itemView) {
            super(itemView);
            layoutUserPostBinding = DataBindingUtil.bind(itemView);
            layoutUserPostBinding.llListGrid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, ShowPostItemsActivity.class)
                            .putExtra("post_id", "")
                            .putExtra("shared_id", "")
                            .putExtra(AppConstants.IS_USER_POST, true));/*.putExtra("post_id", posts.get(getAdapterPosition() - 1).getPost().getId())
                            .putExtra("shared_id", posts.get(getAdapterPosition() - 1).getSharedId())
                            .putExtra(AppConstants.IS_USER_POST, true));*/

                }
            });
        }

    }


    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View itemView) {
            super(itemView);
            bindingHeader = DataBindingUtil.bind(itemView);
            bindingHeader.llMain.setBackgroundColor(ContextCompat.getColor(mContext, themes.setDarkThemeStory()));
            bindingHeader.tvTitle.setBackgroundColor(ContextCompat.getColor(mContext, themes.setDarkThemeStory()));
            bindingHeader.tvTitle.setTextColor(ContextCompat.getColor(mContext, themes.setViewLine()));
            bindingHeader.tvTrending.setBackgroundColor(ContextCompat.getColor(mContext, themes.setDarkThemeStory()));
            bindingHeader.tvTrending.setTextColor(ContextCompat.getColor(mContext, themes.setViewLine()));
            LinearLayoutManager manager1 = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);
            bindingHeader.rvStories.setLayoutManager(manager1);

        }

        public void setHeaderAdapter() {
            int displayWidth = mContext.getResources().getDisplayMetrics().widthPixels / 4;
            if (storyList != null && storyList.size() > 0) {
                homeStoriesAdapter = new HomeStoriesAdapter(itemView.getContext(), storyList, true);
                bindingHeader.rvStories.setAdapter(homeStoriesAdapter);
            } else {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, displayWidth);
                bindingHeader.rvStories.setLayoutParams(layoutParams);
                homeStoriesAdapter = new HomeStoriesAdapter(itemView.getContext(), storyList, true);
                bindingHeader.rvStories.setAdapter(homeStoriesAdapter);
            }

        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
            bindingFooter = DataBindingUtil.bind(itemView);
            bindingFooter.tvTitle.setBackgroundColor(ContextCompat.getColor(mContext, themes.setDarkThemeStory2()));
            bindingHeader.tvTitle.setTextColor(ContextCompat.getColor(mContext, themes.setViewLine()));
            bindingFooter.llMain.setBackgroundColor(ContextCompat.getColor(mContext, themes.setDarkTheme()));
            GridLayoutManager manager1 = new GridLayoutManager(itemView.getContext(), 4);
            bindingFooter.rvStories.setLayoutManager(manager1);


        }

        public void setFooterAdapter() {
            int displayWidth = mContext.getResources().getDisplayMetrics().widthPixels / 4;
            /* if (userList != null && userList.size() > 0) {*/
            trendingProfileAdapter = new TrendingProfileAdapter(itemView.getContext(), userList);
            bindingFooter.rvStories.setAdapter(trendingProfileAdapter);
            /*} else {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, displayWidth);
                bindingFooter.rvStories.setLayoutParams(layoutParams);
                trendingProfileAdapter = new TrendingProfileAdapter(itemView.getContext(), userList);
                bindingFooter.rvStories.setAdapter(trendingProfileAdapter);
           }*/
        }
    }
}
