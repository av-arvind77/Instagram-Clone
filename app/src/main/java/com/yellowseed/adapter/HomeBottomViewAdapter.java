package com.yellowseed.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.apradanas.simplelinkabletext.Link;
import com.facebook.CallbackManager;
import com.google.gson.JsonObject;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.yellowseed.R;
import com.yellowseed.activity.CommentsActivity;
import com.yellowseed.activity.FollowerListActivity;
import com.yellowseed.activity.MyProfileActivity;
import com.yellowseed.activity.PollActivity;
import com.yellowseed.activity.SearchFollowingActivity;
import com.yellowseed.activity.ShowImageActivity;
import com.yellowseed.activity.ShowPostItemsActivity;
import com.yellowseed.activity.TagFollowingActivity;
import com.yellowseed.activity.TaggedUsersActivity;
import com.yellowseed.activity.VotesActivity;
import com.yellowseed.databinding.HeaderHomeFragmentBinding;
import com.yellowseed.databinding.LayoutSuggestionBinding;
import com.yellowseed.databinding.LayoutUserPostBinding;
import com.yellowseed.imageUtils.CircleTransformation;
import com.yellowseed.imagezoom.ImageZoomHelper;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.HomeStoriesModel;
import com.yellowseed.model.SearchFollowingModel;
import com.yellowseed.model.UserListModel;
import com.yellowseed.model.resModel.StoryListModel;
import com.yellowseed.utils.ActivityController;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.PrefManager;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;
import com.yellowseed.webservices.ApiResponse;
import com.yellowseed.webservices.response.homepost.Post;
import com.yellowseed.webservices.response.homepost.TagFriend;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import cn.jzvd.JZVideoPlayerStandard;
import retrofit2.Call;
import retrofit2.Response;

public class HomeBottomViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_SUGGESTION = 2;
    Themes themes;
    Boolean darkThemeEnabled;
    private HomeStoriesAdapter homeStoriesAdapter;
    private ArrayList<Post> posts;
    private ArrayList<StoryListModel.StoriesBean> storyList;
    private Context mContext;
    private OnItemClickListener listener;
    private int pos = -1;
    private Post post;
    private boolean isLiked, isPinned;
    private HeaderHomeFragmentBinding binding;
    private LayoutSuggestionBinding layoutSuggestionBinding;
    private RecyclerView.ViewHolder myHolder;
    private CallbackManager callbackManager;
    private String imagePath = "";
    private int postPos = 0;
    private String userIdSelected = "";
    private String postId;
    private String sharedId;
    private boolean isUserPost = false;
    private List<TagFriend> tagFriends = new ArrayList<>();
    private Link linkHashtag;
    private SuggestionsHomeAdapter adapter;

    private PollAdapter pollAdapter;
    private PollResultAdapter pollresultAdapter;
    private ArrayList<UserListModel> arlModel;
    private List<HomeStoriesModel>storyListModels;
    private List<String> alPoll = new ArrayList<>();
    private CountDownTimer countDownTimer;
    private String[] names = {"John Thomas", "Jenny Koul", "Mike Keel", "Julie Kite", "Tiny Tim", "Julie Kite", "Tiny Tim", "Tiny Tim", "Julie Kite", "Tiny Tim", "Julie Kite", "Tiny Tim", "Tiny Tim"};
    private int[] images = {R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon};
    private int temp=1;


    public HomeBottomViewAdapter(ArrayList<Post> posts, ArrayList<StoryListModel.StoriesBean> storyList, ArrayList<UserListModel> arlModel, Context context, OnItemClickListener listener) throws IOException {
        this.posts = posts;
        this.mContext = context;
        this.listener = listener;
        this.storyList = storyList;
        themes = new Themes(context);
        this.arlModel = arlModel;
        linkHashtag = new Link(Pattern.compile("(#\\w+)"))
                .setUnderlined(true)
                .setTextStyle(Link.TextStyle.ITALIC)
                .setTextColor(Color.BLUE)
                .setClickListener(new Link.OnClickListener() {
                    @Override
                    public void onClick(String text) {
                        // do something
                    }
                });
        darkThemeEnabled = CommonUtils.getPreferencesBoolean(mContext, AppConstants.DARK_THEME);

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user_post, parent, false);
            return new ItemViewHolder(view);
        } else if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_home_fragment, parent, false);
            return new HeaderViewHolder(view);

        } else if (viewType == TYPE_SUGGESTION) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_suggestion, parent, false);
            return new SuggestionViewHolder(view);

        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");

    }


    @Override
    public int getItemViewType(int position) {

        switch (position) {
            case 0:
                if (isPositionHeader(position)) {
                    return TYPE_HEADER;
                }
            case 1:
                return TYPE_ITEM;
            case 4:
                return TYPE_SUGGESTION;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        //  ((ItemViewHolder) holder).layoutUserPostBinding.viewBottom.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setViewLineGrey()));

        String checkInPlace = "";

        String postText = "";
        myHolder = holder;
        int userNameLength = 0;
        int firstTagLength = 0;
        int checkInLength = 0;
        int reducedCountValue = 0;

        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).layoutUserPostBinding.viewBottom.setVisibility(View.VISIBLE);
            post = posts.get(holder.getAdapterPosition() - 1);


            String strDate = "Not availbale";
            strDate = CommonUtils.getFormattedDate(post);

            ((ItemViewHolder) holder).layoutUserPostBinding.tvVoteCount.setTextColor(ContextCompat.getColor(mContext, themes.setSeeAll()));
            ((ItemViewHolder) holder).layoutUserPostBinding.tvPollEndsIn.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeGreyText()));
            ((ItemViewHolder) holder).layoutUserPostBinding.tvViews.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeGreyText()));

            ((ItemViewHolder) holder).layoutUserPostBinding.tvPostLike.setPaintFlags(((ItemViewHolder) holder).layoutUserPostBinding.tvPostLike.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
            ((ItemViewHolder) holder).layoutUserPostBinding.tvPostComment.setPaintFlags(((ItemViewHolder) holder).layoutUserPostBinding.tvPostComment.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
            ((ItemViewHolder) holder).layoutUserPostBinding.tvPostShare.setPaintFlags(((ItemViewHolder) holder).layoutUserPostBinding.tvPostShare.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

            ((ItemViewHolder) holder).layoutUserPostBinding.tvPostTime.setText(strDate);
            ((ItemViewHolder) holder).layoutUserPostBinding.postHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (post.getPostType().equalsIgnoreCase(AppConstants.POLL)) {
                        Intent intent = new Intent(mContext, PollActivity.class);
                        intent.putExtra(AppConstants.FROM, AppConstants.POLL);
                        mContext.startActivity(intent);


                    } else if (post.getPostType().equalsIgnoreCase(AppConstants.POLL_RESULT)) {
                        Intent intent = new Intent(mContext, PollActivity.class);
                        intent.putExtra(AppConstants.FROM, AppConstants.POLL_RESULT);
                        mContext.startActivity(intent);

                    } else {
                        isUserPost = posts.get(position - 1).getPost().getUserId().equalsIgnoreCase(CommonUtils.getPreferencesString(mContext, AppConstants.USER_ID));
                        mContext.startActivity(new Intent(mContext, ShowPostItemsActivity.class)
                                .putExtra("post_id", posts.get(position - 1).getPost().getId())
                                .putExtra("shared_id", posts.get(position - 1).getSharedId())
                                .putExtra(AppConstants.IS_USER_POST, isUserPost)
                                .putExtra(AppConstants.POSITION, position - 1));
                    }
                }
            });
            ((ItemViewHolder) holder).layoutUserPostBinding.tvVoteCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityController.startActivity(mContext, VotesActivity.class);
                }
            });
            ((ItemViewHolder)holder).layoutUserPostBinding.tvPostLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext,SearchFollowingActivity.class);
                    intent.putExtra(AppConstants.FROM,AppConstants.LIKED_NUMBER);
                    mContext.startActivity(intent);


                }
            });

            if (post.getPollData() != null && post.getPollData().getPollArray() != null
                    && post.getPollData().getPollArray().size() > 0) {
                posts.get(holder.getAdapterPosition() - 1).setPostType(AppConstants.POLL);

                ((ItemViewHolder) holder).layoutUserPostBinding.tvPollQuestion.setText(post.getPollData().getPollTitle());
                ((ItemViewHolder) holder).layoutUserPostBinding.llPollQuestions.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).layoutUserPostBinding.tvViews.setVisibility(View.GONE);
                ((ItemViewHolder) holder).layoutUserPostBinding.llFramePic.setVisibility(View.GONE);
                ((ItemViewHolder) holder).layoutUserPostBinding.tvPostLike.setPaintFlags(((ItemViewHolder) holder).layoutUserPostBinding.tvPostLike.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
                ((ItemViewHolder) holder).layoutUserPostBinding.tvPostComment.setPaintFlags(((ItemViewHolder) holder).layoutUserPostBinding.tvPostComment.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
                ((ItemViewHolder) holder).layoutUserPostBinding.tvPostShare.setPaintFlags(((ItemViewHolder) holder).layoutUserPostBinding.tvPostShare.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
               // ((ItemViewHolder) holder).layoutUserPostBinding.tvVoteCount.setPaintFlags(((ItemViewHolder) holder).layoutUserPostBinding.tvVoteCount.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

                ((ItemViewHolder) holder).layoutUserPostBinding.rvPollData.setHasFixedSize(true);
                LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
                ((ItemViewHolder) holder).layoutUserPostBinding.rvPollData.setLayoutManager(layoutManager);
                alPoll.clear();
                alPoll.addAll(post.getPollData().getPollArray());
                ((ItemViewHolder) holder).layoutUserPostBinding.tvVoteCount.setVisibility(View.GONE);
                pollAdapter = new PollAdapter(mContext, alPoll, post.getPollData().getId());
                ((ItemViewHolder) holder).layoutUserPostBinding.rvPollData.setAdapter(pollAdapter);

                //runTimer(holder.getAdapterPosition() - 1, ((ItemViewHolder) holder).layoutUserPostBinding.tvPollEndsIn);

            } else if (post.getPollData() != null && post.getPollData().getPollResult() != null
                    && post.getPollData().getPollResult().size() > 0) {
                posts.get(holder.getAdapterPosition() - 1).setPostType(AppConstants.POLL_RESULT);
                ((ItemViewHolder) holder).layoutUserPostBinding.tvPollQuestion.setText(post.getPollData().getPollTitle());
                ((ItemViewHolder) holder).layoutUserPostBinding.llPollQuestions.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).layoutUserPostBinding.llFramePic.setVisibility(View.GONE);
                ((ItemViewHolder) holder).layoutUserPostBinding.tvViews.setVisibility(View.GONE);
                ((ItemViewHolder) holder).layoutUserPostBinding.tvPostLike.setPaintFlags(((ItemViewHolder) holder).layoutUserPostBinding.tvPostLike.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
                ((ItemViewHolder) holder).layoutUserPostBinding.tvPostComment.setPaintFlags(((ItemViewHolder) holder).layoutUserPostBinding.tvPostComment.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
                ((ItemViewHolder) holder).layoutUserPostBinding.tvPostShare.setPaintFlags(((ItemViewHolder) holder).layoutUserPostBinding.tvPostShare.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

                ((ItemViewHolder) holder).layoutUserPostBinding.rvPollData.setHasFixedSize(true);
                LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
                ((ItemViewHolder) holder).layoutUserPostBinding.rvPollData.setLayoutManager(layoutManager);
                ((ItemViewHolder) holder).layoutUserPostBinding.tvVoteCount.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).layoutUserPostBinding.tvVoteCount.setText("" + post.getPollData().getTotalCount() + " Votes");
                ((ItemViewHolder) holder).layoutUserPostBinding.tvVoteCount.setPaintFlags(((ItemViewHolder) holder).layoutUserPostBinding.tvVoteCount.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);




                pollresultAdapter = new PollResultAdapter(mContext, post.getPollData().getPollResult());
                ((ItemViewHolder) holder).layoutUserPostBinding.rvPollData.setAdapter(pollresultAdapter);
                //runTimer(holder.getAdapterPosition() - 1, ((ItemViewHolder) holder).layoutUserPostBinding.tvPollEndsIn);

            } else {
                posts.get(holder.getAdapterPosition() - 1).setPostType(AppConstants.POST);
                ((ItemViewHolder) holder).layoutUserPostBinding.llPollQuestions.setVisibility(View.GONE);
                ((ItemViewHolder) holder).layoutUserPostBinding.llFramePic.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).layoutUserPostBinding.tvViews.setVisibility(View.VISIBLE);


            }


            ((ItemViewHolder) holder).layoutUserPostBinding.tvPostLike.setText(String.valueOf(post.getLikesCount()));
            /*((ItemViewHolder) holder).layoutUserPostBinding.tvPostLike.setPaintFlags(((ItemViewHolder) holder).layoutUserPostBinding.tvPostLike.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
            ((ItemViewHolder) holder).layoutUserPostBinding.tvPostComment.setPaintFlags(((ItemViewHolder) holder).layoutUserPostBinding.tvPostComment.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
            ((ItemViewHolder) holder).layoutUserPostBinding.tvPostShare.setPaintFlags(((ItemViewHolder) holder).layoutUserPostBinding.tvPostShare.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
         */   ((ItemViewHolder) holder).layoutUserPostBinding.tvPostComment.setText(String.valueOf(post.getComments()));
            ((ItemViewHolder) holder).layoutUserPostBinding.tvPostShare.setText("20");


            if (post.getPost().getStatus()) {
                ((ItemViewHolder) holder).layoutUserPostBinding.llComment.setVisibility(View.VISIBLE);
            } else {
                ((ItemViewHolder) holder).layoutUserPostBinding.llComment.setVisibility(View.GONE);
            }

            if (post.getUser().getName() != null && !post.getUser().getName().isEmpty()) {

                if (post.getPost().getCheckIn() != null && !post.getPost().getCheckIn().equalsIgnoreCase("")) {
                    checkInPlace = " at " + post.getPost().getCheckIn();
                } else {
                    checkInPlace = "";
                }
                int tagCount = 0;
                if (post.getTagFriends() != null && post.getTagFriends().size() > 0) {
                    tagCount = post.getTagFriends().size();
                }
                if (tagCount > 1) {
                    for (int i = 0; i < tagCount; i++) {
                        tagFriends.add(post.getTagFriends().get(i));
                    }
                }
                if (tagCount == 0) {
                    postText = post.getUser().getName();
                } else if (tagCount == 1) {
                    postText = post.getUser().getName() + " is with " + post.getTagFriends().get(0).getName() + checkInPlace;
                } else if (tagCount > 1) {
                    int reducedCount = post.getTagFriends().size() - 1;
                    reducedCountValue = String.valueOf(reducedCount).length();
                    postText = post.getUser().getName() + " is with " + post.getTagFriends().get(0).getName() + " and "
                            + reducedCount + " more" + checkInPlace;
                }

                userNameLength = post.getUser().getName().length();

                if (post.getTagFriends() != null && post.getTagFriends().size() >= 1)
                    firstTagLength = post.getTagFriends().get(0).getName().length();

                if (post.getPost().getCheckIn() != null)
                    checkInLength = post.getPost().getCheckIn().length();
                setSpanableText(post, myHolder, tagFriends, postText, userNameLength, firstTagLength, checkInLength, reducedCountValue);

            }
            if (post.getPost().getDescription() != null && !post.getPost().getDescription().trim().isEmpty()) {
                if (post.getPost().getDescription().contains("#")) {
                    ((ItemViewHolder) holder).layoutUserPostBinding.tvPostContent.setVisibility(View.VISIBLE);
                    ((ItemViewHolder) holder).layoutUserPostBinding.tvPostContentSimple.setVisibility(View.GONE);
                    ((ItemViewHolder) holder).layoutUserPostBinding.tvPostContent.setText(post.getPost().getDescription()).addLink(linkHashtag).build();

                } else {
                    ((ItemViewHolder) holder).layoutUserPostBinding.tvPostContent.setVisibility(View.GONE);
                    ((ItemViewHolder) holder).layoutUserPostBinding.tvPostContentSimple.setVisibility(View.VISIBLE);
                    ((ItemViewHolder) holder).layoutUserPostBinding.tvPostContentSimple.setText(post.getPost().getDescription());

                }
                // ((ItemViewHolder) holder).layoutUserPostBinding.tvPostContent.setText(post.getPost().getDescription());
            } else {
                ((ItemViewHolder) holder).layoutUserPostBinding.tvPostContent.setVisibility(View.GONE);
                ((ItemViewHolder) holder).layoutUserPostBinding.tvPostContentSimple.setVisibility(View.GONE);
            }

            if (post.getCommentArr().size() > 0) {
                ((ItemViewHolder) holder).layoutUserPostBinding.llCommentView.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).layoutUserPostBinding.tvComment.setText(post.getCommentArr().get(0).getUser().getName());
                ((ItemViewHolder) holder).layoutUserPostBinding.tvCommentDescription.setText(post.getCommentArr().get(0).getComment());
                Picasso.with(mContext).load(post.getCommentArr().get(0).getUser().getImage()).
                        transform(new CircleTransformation()).memoryPolicy(MemoryPolicy.NO_CACHE).into(((ItemViewHolder) holder).layoutUserPostBinding.ivComment);
            } else {
                ((ItemViewHolder) holder).layoutUserPostBinding.llCommentView.setVisibility(View.GONE);
                CommonUtils.otherUserProfile(mContext, ((ItemViewHolder) holder).layoutUserPostBinding.ivComment, post.getUser().getImage(), ((ItemViewHolder) holder).layoutUserPostBinding.tvCommentUserImage,
                        post.getUser().getName());
            }

            if (post.getImages() != null && post.getImages().size() > 0) {
                setUpViewpager(((ItemViewHolder) holder));
                ((ItemViewHolder) holder).layoutUserPostBinding.llFramePic.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).layoutUserPostBinding.viewPager.setVisibility(View.VISIBLE);
            } else {
                ((ItemViewHolder) holder).layoutUserPostBinding.llFramePic.setVisibility(View.GONE);
                ((ItemViewHolder) holder).layoutUserPostBinding.viewPager.setVisibility(View.GONE);
            }


            // Picasso.with(mContext).load(R.mipmap.userprofile_icon).into(((ItemViewHolder) holder).layoutUserPostBinding.ivPostProfilePic);

            CommonUtils.otherUserProfile(mContext, ((ItemViewHolder) holder).layoutUserPostBinding.ivPostProfilePic, post.getUser().getImage(), ((ItemViewHolder) holder).layoutUserPostBinding.tvUserImage,
                    post.getUser().getName());

            if (post.getCurrentUserLike()) {
                ((ItemViewHolder) holder).layoutUserPostBinding.ivHeart.setImageResource(R.mipmap.heart_icon_sel);
            } else {
                ((ItemViewHolder) holder).layoutUserPostBinding.ivHeart.setImageResource(R.mipmap.heart_icon);
            }
            if (post.getPinned()) {
                ((ItemViewHolder) holder).layoutUserPostBinding.ivPinned.setImageResource(R.drawable.unpin);
            } else {
                ((ItemViewHolder) holder).layoutUserPostBinding.ivPinned.setImageResource(R.drawable.pin);
            }
            ((ItemViewHolder) holder).layoutUserPostBinding.tvPostLike.setText(String.valueOf(post.getLikesCount()));
        }
        if (holder instanceof HeaderViewHolder) {


            ((HeaderViewHolder) holder).setHeaderAdapter();
        }
        if (holder instanceof SuggestionViewHolder) {


            ((SuggestionViewHolder) holder).setSuggestionAdapter();

        }
    }

    private void setUpViewpager(ItemViewHolder viewHolder) {
        CustomPageAdapter mCustomPagerAdapter = new CustomPageAdapter(mContext, post.getImages());
        viewHolder.layoutUserPostBinding.viewPager.setAdapter(mCustomPagerAdapter);
        if (post.getImages() != null && post.getImages().size() > 1) {
            viewHolder.layoutUserPostBinding.tabLayout.setVisibility(View.VISIBLE);
            viewHolder.layoutUserPostBinding.tabLayout.setupWithViewPager(viewHolder.layoutUserPostBinding.viewPager, true);
        } else {
            viewHolder.layoutUserPostBinding.tabLayout.setVisibility(View.GONE);
        }
    }

    /*
     * method is used to set spanable string
     * */
    public void setSpanableText(final Post post, final RecyclerView.ViewHolder holder, final List<TagFriend> tagFriends, String text, int userNameLength, int firstTagLength, int checkInLength, int reducedCountValue) {
        SpannableString spanString = new SpannableString(text);

        /* User's Post profile view */
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                userIdSelected = post.getUser().getId();
                Intent intent = new Intent(mContext, MyProfileActivity.class);
                intent.putExtra(AppConstants.USER_ID, userIdSelected);
                mContext.startActivity(intent);
            }

            @Override

            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        };
        if (darkThemeEnabled) {
            spanString.setSpan(clickableSpan, 0, userNameLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, userNameLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, userNameLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            spanString.setSpan(clickableSpan, 0, userNameLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spanString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, userNameLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, userNameLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        }

        /* First taged User's profile view */
        if (firstTagLength > 0) {
            ClickableSpan clickableSpan1 = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Intent intent = new Intent(mContext, MyProfileActivity.class);
                    intent.putExtra(AppConstants.USER_ID, post.getTagFriends().get(0).getUserId());
                    mContext.startActivity(intent);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setUnderlineText(false);
                }
            };


            if (darkThemeEnabled) {
                spanString.setSpan(clickableSpan1, userNameLength + 9, userNameLength + firstTagLength + 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanString.setSpan(new ForegroundColorSpan(Color.WHITE), userNameLength + 9, userNameLength + firstTagLength + 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanString.setSpan(new StyleSpan(Typeface.BOLD), userNameLength + 9, userNameLength + firstTagLength + 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {

                spanString.setSpan(clickableSpan1, userNameLength + 9, userNameLength + firstTagLength + 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanString.setSpan(new ForegroundColorSpan(Color.BLACK), userNameLength + 9, userNameLength + firstTagLength + 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanString.setSpan(new StyleSpan(Typeface.BOLD), userNameLength + 9, userNameLength + firstTagLength + 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }


        }
        /* View tag Users list */
        if (firstTagLength > 0 && reducedCountValue > 0) {
            ClickableSpan clickableSpan2 = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    postId = posts.get(holder.getAdapterPosition() - 1).getPost().getId();
                    sharedId = posts.get(holder.getAdapterPosition() - 1).getSharedId();
          /*          Intent intent = new Intent(mContext, TaggedUsersActivity.class);
                    intent.putExtra(AppConstants.POST_ID, postId);
                    intent.putExtra(AppConstants.SHARED_ID, sharedId);
                    mContext.startActivity(intent);*/

                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setUnderlineText(false);
                }
            };


            if (darkThemeEnabled) {

                spanString.setSpan(clickableSpan2, userNameLength + firstTagLength + 14
                        , userNameLength + firstTagLength + reducedCountValue + 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanString.setSpan(new ForegroundColorSpan(Color.WHITE), userNameLength + firstTagLength + 14
                        , userNameLength + firstTagLength + reducedCountValue + 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanString.setSpan(new StyleSpan(Typeface.BOLD), userNameLength + firstTagLength + 14
                        , userNameLength + firstTagLength + reducedCountValue + 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                spanString.setSpan(clickableSpan2, userNameLength + firstTagLength + 14
                        , userNameLength + firstTagLength + reducedCountValue + 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanString.setSpan(new ForegroundColorSpan(Color.BLACK), userNameLength + firstTagLength + 14
                        , userNameLength + firstTagLength + reducedCountValue + 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanString.setSpan(new StyleSpan(Typeface.BOLD), userNameLength + firstTagLength + 14
                        , userNameLength + firstTagLength + reducedCountValue + 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            }

        }

        /*Location Click*/
        if (firstTagLength > 0 && reducedCountValue > 0 && checkInLength > 0) {
            ClickableSpan clickableSpan3 = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    CommonUtils.showMap(mContext, post.getPost().getLatitude(), post.getPost().getLongitude(), post.getPost().getCheckIn());
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setUnderlineText(false);
                }
            };

            if (darkThemeEnabled) {
                spanString.setSpan(clickableSpan3, userNameLength + firstTagLength + reducedCountValue + 23, userNameLength + firstTagLength + reducedCountValue + checkInLength + 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanString.setSpan(new ForegroundColorSpan(Color.WHITE), userNameLength + firstTagLength + reducedCountValue + 23, userNameLength + firstTagLength + reducedCountValue + checkInLength + 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanString.setSpan(new StyleSpan(Typeface.BOLD), userNameLength + firstTagLength + reducedCountValue + 23, userNameLength + firstTagLength + reducedCountValue + checkInLength + 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                spanString.setSpan(clickableSpan3, userNameLength + firstTagLength + reducedCountValue + 23, userNameLength + firstTagLength + reducedCountValue + checkInLength + 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanString.setSpan(new ForegroundColorSpan(Color.BLACK), userNameLength + firstTagLength + reducedCountValue + 23, userNameLength + firstTagLength + reducedCountValue + checkInLength + 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanString.setSpan(new StyleSpan(Typeface.BOLD), userNameLength + firstTagLength + reducedCountValue + 23, userNameLength + firstTagLength + reducedCountValue + checkInLength + 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            }
        }


        ((ItemViewHolder) holder).layoutUserPostBinding.tvPostUserName.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
        ((ItemViewHolder) holder).layoutUserPostBinding.llListRow.setBackgroundColor(ContextCompat.getColor(mContext, themes.setDarkTheme()));
        ((ItemViewHolder) holder).layoutUserPostBinding.tvPostLike.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
        ((ItemViewHolder) holder).layoutUserPostBinding.tvPostComment.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
        ((ItemViewHolder) holder).layoutUserPostBinding.tvPostShare.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
        ((ItemViewHolder) holder).layoutUserPostBinding.tvPostContent.setTextColor(ContextCompat.getColor(mContext, themes.setToolbarWhiteText()));
        ((ItemViewHolder) holder).layoutUserPostBinding.tvPostContentSimple.setTextColor(ContextCompat.getColor(mContext, themes.setToolbarWhiteText()));
        ((ItemViewHolder) holder).layoutUserPostBinding.tvComment.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
        ((ItemViewHolder) holder).layoutUserPostBinding.tvCommentDescription.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
        ((ItemViewHolder) holder).layoutUserPostBinding.tvPostUserName.setText(spanString);
        ((ItemViewHolder) holder).layoutUserPostBinding.tvPostUserName.setMovementMethod(LinkMovementMethod.getInstance());

    }

    @Override
    public int getItemCount() {
        return posts.size() + 1;
    }

    /**
     * method for create story api
     */
    private void callLikePostAPI(final int position, String sharedId, String postId, final boolean isLiked) {
        if (CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();
            JsonObject jsonObject = new JsonObject();

            if (sharedId != null && !sharedId.equalsIgnoreCase("")) {
                jsonObject.addProperty("shared_id", sharedId);
            } else {
                jsonObject.addProperty("post_id", postId);
            }

            jsonObject.addProperty("status", isLiked);

            Call<ApiResponse> call = ApiExecutor.getClient(mContext).apiLikePost(jsonObject);
            call.enqueue(new retrofit2.Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            if (isLiked) {
                                int count = (posts.get(position).getLikesCount()) + 1;
                                posts.get(position).setLikesCount(count);
                            } else {
                                if (posts.get(position).getLikesCount() != 0) {
                                    int count = (posts.get(position).getLikesCount() - 1);
                                    posts.get(position).setLikesCount(count);
                                } else {
                                    posts.get(position).setLikesCount(0);
                                }
                            }
                            notifyDataSetChanged();
                        } else {
                            ToastUtils.showToastShort(mContext, mContext.getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(mContext, mContext.getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
        }
    }


    private void callPinnedApi(final ArrayList<Post> post, final int Pos) {
        if (CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("post_id", post.get(Pos).getPost().getId());
            jsonObject.addProperty("status", !post.get(Pos).getPinned());

            Call<ApiResponse> call = ApiExecutor.getClient(mContext).apiPinnedPost(jsonObject);
            call.enqueue(new retrofit2.Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            if (post.get(Pos).getPinned()) {
                                post.get(Pos).setPinned(false);
                            } else {
                                post.get(Pos).setPinned(true);
                            }
                            notifyDataSetChanged();
                        } else {
                            ToastUtils.showToastShort(mContext, mContext.getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(mContext, mContext.getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
        }
    }

    protected void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }

    private void displayGalleryItem(ImageView galleryView, String galleryItem) {
        if (null != galleryItem) {


            galleryView.setImageResource(R.mipmap.post_img);
           /* Glide.with(galleryView.getContext()) // Bind it with the context of the actual view used
                    .load(galleryItem) // Load the image
                    .asBitmap() // All our images are static, we want to display them as bitmaps
                    .format(DecodeFormat.PREFER_RGB_565) // the decode format - this will not use alpha at all
                    .centerCrop()// temporary holder displayed while the image loads// need to manually set the animation as bitmap cannot use cross fade
                    .thumbnail(0.2f)// make use of the thumbnail which can display a down-sized version of the image
                    .into(galleryView); // Voilla - the target view
   */
        }
    }

    public void runTimer(final int position, TextView textView) {
        final long[] diff = {(long) getTimeDifference((long) posts.get(position).getPollData().getValidUntil(), (long) posts.get(position).getPollData().getTime())};
        if (diff[0] <= 0) {
            textView.setText("This poll has been ended.");

        } else {
            cancelTimer();
            countDownTimer = new CountDownTimer((long) diff[0], 1000) {
                @SuppressLint("DefaultLocale")
                @Override
                public void onTick(long l) {
                    // Log.e("before", "" + (long) posts.get(position).getPollData().getTime());
                    posts.get(position).getPollData().setTime(((long) posts.get(position).getPollData().getTime() + 1));
                    diff[0] = getTimeDifference((long) posts.get(position).getPollData().getValidUntil()/*endTime*/, (long) posts.get(position).getPollData().getTime());
                    //   Log.e("after", "" + (long) posts.get(position).getPollData().getTime());

                    int diffSecond = (int) diff[0];
                    int diffDays = diffSecond / (24 * 60 * 60);
                    int diffHours = (diffSecond / 3600) % 24;
                    int diffMinutes = diffSecond / (60) % 60;
                    int diffSeconds = (diffSecond % 3600) % 60;


                    if (diffDays > 0) {
                        textView.setText("This poll ends in " + diffDays + " days " + diffHours + "Hours " + diffMinutes + "minutes " + diffSeconds + "sec.");


                    } else if (diffHours > 0) {

                        textView.setText("This poll ends in " + diffHours + "Hours " + diffMinutes + "minutes " + diffSeconds + "sec.");

                    } else if (diffMinutes > 0) {
                        textView.setText("This poll ends in " + diffMinutes + "minutes " + diffSeconds + "sec.");


                    } else if (diffSeconds > 0) {
                        textView.setText("This poll ends in " + diffSeconds + "sec.");


                    }


                }

                @Override
                public void onFinish() {
                    textView.setText("This poll has been ended.");

                }
            }.start();
        }
    }

    public long getTimeDifference(long time1, long time2) {
        return time1 - time2;
    }

    public void cancelTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public LayoutUserPostBinding layoutUserPostBinding;

        public ItemViewHolder(View itemView) {
            super(itemView);
            layoutUserPostBinding = DataBindingUtil.bind(itemView);


            layoutUserPostBinding.ivPostImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(layoutUserPostBinding.ivPostImage, getAdapterPosition() - 1);
                }
            });


            layoutUserPostBinding.tvPollQuestion.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
      //      layoutUserPostBinding.tvPollEndsIn.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
            layoutUserPostBinding.viewBottom.setBackgroundColor(ContextCompat.getColor(mContext, themes.setViewBottomColor()));
            themes.changeIconColorToWhite(mContext, layoutUserPostBinding.ivPostMenuItem);
            themes.changeIconColorToWhite(mContext, layoutUserPostBinding.ivPostComment);
            themes.changeIconColorToWhite(mContext, layoutUserPostBinding.ivPostShare);
            themes.changeIconColorToWhite(mContext, layoutUserPostBinding.ivPinned);
            if(darkThemeEnabled)
            {
                layoutUserPostBinding.ivPostDownloads.setImageResource(R.mipmap.save_icon_sel);
                //  themes.changeDownloadIcon(mContext, layoutUserPostBinding.ivPostDownloads);

            }
            else
            {
                layoutUserPostBinding.ivPostDownloads.setImageResource(R.mipmap.save_icon);

            }
         //  themes.changeDownloadIcon(mContext, layoutUserPostBinding.ivPostDownloads);



            layoutUserPostBinding.ivComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(new PrefManager(mContext).getUserId().equalsIgnoreCase(posts.get(getAdapterPosition() - 1).getCommentArr().get(0).getUser().getId()))) {
                        mContext.startActivity(new Intent(mContext, MyProfileActivity.class)
                                .putExtra("user_id", posts.get(getAdapterPosition() - 1).getCommentArr().get(0).getUser().getId())
                                .putExtra(AppConstants.IS_USER_POST, true));
                    }
                }
            });

            layoutUserPostBinding.llComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mContext, CommentsActivity.class);
                    if (posts.get(getAdapterPosition() - 1).getSharedId() != null) {
                        intent.putExtra(AppConstants.SHARED_ID, posts.get(getAdapterPosition() - 1).getSharedId());
                    } else {
                        intent.putExtra(AppConstants.SHARED_ID, "");
                    }
                    if (posts.get(getAdapterPosition() - 1).getPost().getId() != null) {
                        intent.putExtra(AppConstants.POST_ID, posts.get(getAdapterPosition() - 1).getPost().getId());
                    } else {
                        intent.putExtra(AppConstants.POST_ID, "");
                    }
                    mContext.startActivity(intent);
                }
            });


            layoutUserPostBinding.llCommentDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, CommentsActivity.class);
                    if (posts.get(getAdapterPosition() - 1).getSharedId() != null) {
                        intent.putExtra(AppConstants.SHARED_ID, posts.get(getAdapterPosition() - 1).getSharedId());
                    } else {
                        intent.putExtra(AppConstants.SHARED_ID, "");
                    }
                    if (posts.get(getAdapterPosition() - 1).getPost().getId() != null) {
                        intent.putExtra(AppConstants.POST_ID, posts.get(getAdapterPosition() - 1).getPost().getId());
                    } else {
                        intent.putExtra(AppConstants.POST_ID, "");
                    }

                    mContext.startActivity(intent);
                }
            });

            layoutUserPostBinding.ivPostMenuItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //openMenuPopup(posts.get(getAdapterPosition()-1),v);
                    listener.onItemClick(layoutUserPostBinding.ivPostMenuItem, getAdapterPosition() - 1);

                    if (posts.get(getAdapterPosition() - 1).isOpenPopup()) {
                        posts.get(getAdapterPosition() - 1).setOpenPopup(false);
                    } else {
                        posts.get(getAdapterPosition() - 1).setOpenPopup(true);
                    }
                }
            });

            layoutUserPostBinding.llShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(layoutUserPostBinding.llShare, getAdapterPosition() - 1);
                }
            });

            layoutUserPostBinding.ivPostProfilePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(layoutUserPostBinding.ivPostProfilePic, getAdapterPosition() - 1);
                }
            });


            layoutUserPostBinding.ivPostDownloads.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(layoutUserPostBinding.ivPostDownloads, getAdapterPosition() - 1);
                    if (temp==1) {
                        if (darkThemeEnabled) {
                            layoutUserPostBinding.ivPostDownloads.setImageResource(R.mipmap.download_home);

                        } else {
                            layoutUserPostBinding.ivPostDownloads.setImageResource(R.mipmap.download_white);

                        }
                        temp=0;

                    }
                    else
                        {
                            if(darkThemeEnabled)
                            {
                                layoutUserPostBinding.ivPostDownloads.setImageResource(R.mipmap.save_icon_sel);
                              //  themes.changeDownloadIcon(mContext, layoutUserPostBinding.ivPostDownloads);

                            }
                            else
                            {
                                layoutUserPostBinding.ivPostDownloads.setImageResource(R.mipmap.save_icon);

                            }
                            // themes.changeIconColorToWhite(mContext, layoutUserPostBinding.ivPostDownloads);


                            temp=1;
                        }

                }
            });


            layoutUserPostBinding.tvPostTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(layoutUserPostBinding.tvPostTime, getAdapterPosition() - 1);
                }
            });

            layoutUserPostBinding.postHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(layoutUserPostBinding.postHeader, getAdapterPosition() - 1);
                }
            });

            layoutUserPostBinding.ivHeart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClick(layoutUserPostBinding.ivHeart, getAdapterPosition() - 1);
                    if (!posts.get(getAdapterPosition() - 1).getCurrentUserLike()) {
                        posts.get(getAdapterPosition() - 1).setCurrentUserLike(true);
                        isLiked = true;
                    } else {
                        posts.get(getAdapterPosition() - 1).setCurrentUserLike(false);
                        isLiked = false;
                    }

                    if (isLiked) {
                        int count = (posts.get(getAdapterPosition() - 1).getLikesCount()) + 1;
                        posts.get(getAdapterPosition() - 1).setLikesCount(count);
                    } else {
                        if (posts.get(getAdapterPosition() - 1).getLikesCount() != 0) {
                            int count = (posts.get(getAdapterPosition() - 1).getLikesCount() - 1);
                            posts.get(getAdapterPosition() - 1).setLikesCount(count);
                        } else {
                            posts.get(getAdapterPosition() - 1).setLikesCount(0);
                        }
                    }
                    notifyDataSetChanged();
                    /*if (CommonUtils.isOnline(mContext)) {
                        callLikePostAPI(getAdapterPosition() - 1, posts.get(getAdapterPosition() - 1).getSharedId(), posts.get(getAdapterPosition() - 1).getPost().getId(), isLiked);
                    } else {
                        ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
                    }*/
                }
            });

            layoutUserPostBinding.ivPinned.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CommonUtils.isOnline(mContext)) {
                        //  callPinnedApi(posts, getAdapterPosition() - 1);
                    } else {
                        ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
                    }
                }
            });
        }

    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            binding.framelayout.setBackgroundColor(ContextCompat.getColor(mContext, themes.setDarkThemeStory()));
            binding.llStoryHome.setBackgroundColor(ContextCompat.getColor(mContext, themes.setDarkThemeStory()));
            binding.llMain2.setBackgroundColor(ContextCompat.getColor(mContext, themes.setDarkThemeStory()));
            binding.llStory.setBackgroundColor(ContextCompat.getColor(mContext, themes.setDarkTheme()));
            binding.llStory2.setBackgroundColor(ContextCompat.getColor(mContext, themes.setDarkThemeStory()));
            binding.rvStories.setBackgroundColor(ContextCompat.getColor(mContext, themes.setDarkThemeStory()));
            binding.etWriteSomething.setBackground(ContextCompat.getDrawable(mContext, themes.setDarkStoryBackground()));
            binding.etWriteSomething.setHintTextColor(ContextCompat.getColor(mContext, R.color.grey_hin_color));
            binding.etWriteSomething.setTextColor(ContextCompat.getColor(mContext, R.color.grey_hin_color));
            binding.tvStory.setTextColor(ContextCompat.getColor(mContext, themes.setDarkGreyTextColor()));
            binding.tvPostHome.setTextColor(ContextCompat.getColor(mContext, themes.setDarkGreyTextColor()));
            binding.tvPollHome.setTextColor(ContextCompat.getColor(mContext, themes.setDarkGreyTextColor()));
            binding.viewHeaderHome.setBackgroundColor(ContextCompat.getColor(mContext, themes.setViewLineGrey()));
            binding.viewLine.setBackgroundColor(ContextCompat.getColor(mContext, themes.setViewLineGrey()));

            binding.tvGoLive.setTextColor(ContextCompat.getColor(mContext, themes.setDarkGreyTextColor()));
            binding.tvHomeStories.setTextColor(ContextCompat.getColor(mContext, themes.setDarkGreyStoryTextColor()));
            binding.tvWatchAll.setTextColor(ContextCompat.getColor(mContext, themes.setYellow()));
            binding.tvUserStoriesName.setTextColor(ContextCompat.getColor(mContext, themes.setGreyHint()));
            themes.changeIconColorToWhite(mContext, binding.ivStory);
            themes.changeShareIcon(mContext, binding.ivPoll);
            themes.changeIconColorToWhite(mContext, binding.ivPost);
            themes.changeIconColorToWhite(mContext, binding.ivGoLive);
            themes.changeIconColor(mContext, binding.ivWatch);

            binding.llGoLive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(binding.llGoLive, getAdapterPosition() - 1);
                }
            }); binding.flImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(binding.flImage, getAdapterPosition() - 1);
                }
            });
            binding.tvWatchAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(binding.tvWatchAll, getAdapterPosition());
                }
            });
            binding.llPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(binding.llPost, getAdapterPosition());
                }
            });
            binding.llPoll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(binding.llPoll, getAdapterPosition());
                }
            });
            binding.llStory3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!binding.etWriteSomething.getText().toString().isEmpty()) {
                        listener.onItemClick(binding.llStory3, getAdapterPosition());
                    } else {
                        listener.onItemClick(binding.llStory3, getAdapterPosition());
                    }
                }
            });
            binding.tvHomeStories.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(binding.tvHomeStories, getAdapterPosition());
                }
            });

            binding.llStoryHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!binding.etWriteSomething.getText().toString().isEmpty()) {
                        listener.onItemClick(binding.tvStory, getAdapterPosition());
                    } else {
                        listener.onItemClick(binding.tvStory, getAdapterPosition());
                    }
                }
            });

            binding.etWriteSomething.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(binding.etWriteSomething, getAdapterPosition());
                }
            });
            LinearLayoutManager manager1 = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);
            binding.rvStories.setLayoutManager(manager1);


        }




      /*  private void setHeaderAdapter() {

            binding.rvStories.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
            binding.rvStories.setLayoutManager(layoutManager);
            storyListModels = new ArrayList<>();
            storyListModels.addAll(prepareData());
            homeStoriesAdapter = new StoriesHomeAdpater(storyListModels, mContext, new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    switch (view.getId()) {

                        default:
                            break;
                    }
                }
            });
            binding.rvStories.setAdapter(adapter);
        }

        private ArrayList<HomeStoriesModel> prepareData() {
            ArrayList<HomeStoriesModel> data = new ArrayList<>();
            for (int i = 0; i < names.length; i++) {
                HomeStoriesModel model = new HomeStoriesModel();
                model.setStoriesUserName(names[i]);
                model.setStoriesImage(images[i]);
                data.add(model);
            }
            return data;
        }*/





        public void setHeaderAdapter() {
            homeStoriesAdapter = new HomeStoriesAdapter(itemView.getContext(), storyList, true);
            binding.rvStories.setAdapter(homeStoriesAdapter);
            if (storyList != null && storyList.size() > 0) {
                binding.llStoryHome.setVisibility(View.GONE);
            } else {
                binding.llStoryHome.setVisibility(View.VISIBLE);
                //              binding.tvUserStoriesName.setText(PrefManager.getInstance(mContext).getCurrentUser().getName());
                if (PrefManager.getInstance(mContext).getUserPic() != null && PrefManager.getInstance(mContext).getUserPic().length() > 0) {
                    Picasso.with(mContext).load(PrefManager.getInstance(mContext).getUserPic()).
                            transform(new CircleTransformation()).memoryPolicy(MemoryPolicy.NO_CACHE).into(binding.ivUserStories);

                } else {
                    binding.ivUserStories.setVisibility(View.VISIBLE);
                    binding.tvUserImage.setVisibility(View.GONE);

                    CommonUtils.userProfile(mContext, binding.ivUserStories, binding.tvUserImage);

                    Picasso.with(mContext).load(R.drawable.user_placeholder).
                            transform(new CircleTransformation()).memoryPolicy(MemoryPolicy.NO_CACHE).into(binding.ivUserStories);

                }
            }
        }
    }

    public class CustomPageAdapter extends PagerAdapter {
        private Context context;
        private List<com.yellowseed.webservices.response.homepost.Image> dataObjectList;
        private LayoutInflater layoutInflater;

        public CustomPageAdapter(Context context, List<com.yellowseed.webservices.response.homepost.Image> dataObjectList) {
            this.context = context;
            this.layoutInflater = (LayoutInflater) this.context.getSystemService(this.context.LAYOUT_INFLATER_SERVICE);
            this.dataObjectList = dataObjectList;

        }

        @Override
        public int getCount() {
            return dataObjectList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = this.layoutInflater.inflate(R.layout.row_home_bottom, container, false);
            final ImageView displayImage = (ImageView) view.findViewById(R.id.ivPostImage);
            JZVideoPlayerStandard jCVideoPlayer = (JZVideoPlayerStandard) view.findViewById(R.id.videocontroller);
            displayImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ShowImageActivity.class);
                    intent.putExtra("imageList", (Serializable) dataObjectList);
                    //  intent.putExtras("imageList", dataObjectList);
                    context.startActivity(intent);
                }
            });

            if (dataObjectList.get(position).getFileType().length() > 0) {
                switch (dataObjectList.get(position).getFileType()) {
                    case "video":
                        displayImage.setVisibility(View.GONE);
                        jCVideoPlayer.setVisibility(View.VISIBLE);
                        jCVideoPlayer.setUp(dataObjectList.get(position).getFile().getUrl().replace(".mov", ".mp4"),
                                jCVideoPlayer.SCREEN_WINDOW_NORMAL,
                                "");
                        //   jCVideoPlayer.thumbImageView.setImage("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640");
                        break;
                    case "image":
                        displayImage.setVisibility(View.VISIBLE);
                        jCVideoPlayer.setVisibility(View.GONE);
                        ImageZoomHelper.setViewZoomable(displayImage);
                        displayImage.setVisibility(View.VISIBLE);
                        displayGalleryItem(displayImage, dataObjectList.get(position).getFile().getUrl());
                        break;
                    default:
                        displayImage.setVisibility(View.VISIBLE);
                        jCVideoPlayer.setVisibility(View.GONE);
                        ImageZoomHelper.setViewZoomable(displayImage);
                        displayGalleryItem(displayImage, dataObjectList.get(position).getFile().getUrl());
                        break;
                }
            } else {
                displayImage.setVisibility(View.VISIBLE);
                jCVideoPlayer.setVisibility(View.GONE);
                ImageZoomHelper.setViewZoomable(displayImage);
                displayGalleryItem(displayImage, dataObjectList.get(position).getFile().getUrl());
            }

            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // Removes the page from the container for the given position. We simply removed object using removeView()
            // but could’ve also used removeViewAt() by passing it the position.
            try {
                // Remove the view from the container
                container.removeView((View) object);

                // Try to clear resources used for displaying this view
//                Glide.clear(((View) object).findViewById(R.id.ivPostImage));
                // Remove any resources used by this view
                unbindDrawables((View) object);
                // Invalidate the object
                object = null;
            } catch (Exception e) {
                Log.w("", "destroyItem: failed to destroy item and clear it's used resources", e);
            }
        }
    }

    private class SuggestionViewHolder extends RecyclerView.ViewHolder {
        public SuggestionViewHolder(View itemView) {
            super(itemView);
            layoutSuggestionBinding = DataBindingUtil.bind(itemView);
            layoutSuggestionBinding.viewMain.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setSuggestionViewLine()));
            layoutSuggestionBinding.tvSuggestion.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setSuggestionText()));
            layoutSuggestionBinding.seeAll.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setSeeAll()));

            layoutSuggestionBinding.seeAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(layoutSuggestionBinding.seeAll, getAdapterPosition());
                }
            });

        }


        public void setSuggestionAdapter() {
            layoutSuggestionBinding.rvSuggestion.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            layoutSuggestionBinding.rvSuggestion.setLayoutManager(layoutManager);
            adapter = new SuggestionsHomeAdapter(arlModel, mContext);
            if (arlModel.size() == 0) {
                layoutSuggestionBinding.llMain.setVisibility(View.GONE);
            } else {
                layoutSuggestionBinding.llMain.setVisibility(View.VISIBLE);
            }
            layoutSuggestionBinding.rvSuggestion.setAdapter(adapter);

        }
    }


}

