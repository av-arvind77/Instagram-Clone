package com.yellowseed.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.apradanas.simplelinkabletext.Link;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.gson.JsonObject;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.yellowseed.R;
import com.yellowseed.activity.ChatsScreenFrgActivity;
import com.yellowseed.activity.CommentsActivity;
import com.yellowseed.activity.EditProfileActivity;
import com.yellowseed.activity.MyProfileActivity;
import com.yellowseed.activity.ShowImageActivity;
import com.yellowseed.activity.ShowPostItemsActivity;
import com.yellowseed.activity.TaggedUsersActivity;
import com.yellowseed.databinding.HeaderProfileActivityBinding;
import com.yellowseed.databinding.LayoutUserPostBinding;
import com.yellowseed.imageUtils.CircleTransformation;
import com.yellowseed.imagezoom.ImageZoomHelper;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.reqModel.UserModel;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DateUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.PrefManager;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;
import com.yellowseed.webservices.ApiResponse;
import com.yellowseed.webservices.requests.UnFollowRequest;
import com.yellowseed.webservices.requests.UserFollow;
import com.yellowseed.webservices.response.homepost.Post;
import com.yellowseed.webservices.response.homepost.TagFriend;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Pattern;

import cn.jzvd.JZVideoPlayerStandard;
import retrofit2.Call;
import retrofit2.Response;

public class MyProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;
    protected static final int[] CONTENT2 = new int[]{R.mipmap.post_img, R.mipmap.post_img2, R.mipmap.post_img3};
    private ArrayList<Post> posts;
    private Context mContext;
    private OnItemClickListener listener;
    private int pos = -1;
    private CallbackManager callbackManager;
    private String imagePath = "";
    private Post post;
    private UserModel userModel;
    private String listType = "";
    private boolean likedStatus, isPinned;
    private String userIdSelected = "";
    private String postId;
    private String sharedId;
    private boolean isUserPost = false;
    private List<TagFriend> tagFriends = new ArrayList<>();
    private Link linkHashtag;
    private Themes themes;
    private Boolean darkThemeEnabled;
    private TestFragmentAdapter pagerAdapter;
    private String from="";
    private int temp=1;

    public MyProfileAdapter(ArrayList<Post> posts, String listType, UserModel userModel, Context context, OnItemClickListener listener,String from) {
        this.posts = posts;
        this.mContext = context;
        this.userModel = userModel;
        this.listener = listener;
        this.listType = listType;
        this.from=from;
        themes = new Themes(context);

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
        darkThemeEnabled = CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME);

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user_post, parent, false);
            return new ItemViewHolder(view);
        } else if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_profile_activity, parent, false);
            return new HeaderViewHolder(view);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    public boolean isPositionHeader(int position) {
        return position == 0;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        String checkInPlace = "";
        String postText = "";
        int userNameLength = 0;
        int firstTagLength = 0;
        int checkInLength = 0;
        int reducedCountValue = 0;


        if (holder instanceof ItemViewHolder) {
            int displayWidth = mContext.getResources().getDisplayMetrics().widthPixels / 3;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) displayWidth, (int) displayWidth);
            ((ItemViewHolder) holder).layoutUserPostBinding.llListGrid.setLayoutParams(layoutParams);

            if (listType.equalsIgnoreCase(AppConstants.GRID)) {
                ((ItemViewHolder) holder).layoutUserPostBinding.llListRow.setVisibility(View.GONE);
                ((ItemViewHolder) holder).layoutUserPostBinding.llListGrid.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).layoutUserPostBinding.llCommentView.setVisibility(View.GONE);
            } else {
                ((ItemViewHolder) holder).layoutUserPostBinding.llListRow.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).layoutUserPostBinding.llListGrid.setVisibility(View.GONE);
                ((ItemViewHolder) holder).layoutUserPostBinding.llCommentView.setVisibility(View.GONE);
            }
            post = posts.get(holder.getAdapterPosition() - 1);

            String strDate = "Not availbale";
            String setDateValue = "";
            String setHourValue = "";
            SimpleDateFormat readDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            readDate.setTimeZone(TimeZone.getTimeZone("GMT")); // missing line
            Date date = null;
            Date postDate = null;
            try {
                date = readDate.parse(post.getPost().getCreatedAt());
                String time = readDate.format(date);

                SimpleDateFormat formattedDate = new SimpleDateFormat(
                        "yyyy-MM-dd");
                postDate = formattedDate.parse(time);
                String postDateValue = formattedDate.format(postDate);

                formattedDate.setTimeZone(TimeZone.getTimeZone("GMT"));
                String gmtTime = formattedDate.format(new Date());

                Date currentDate = null;
                currentDate = formattedDate.parse(gmtTime);
                String currentDateValue = formattedDate.format(currentDate);


                if (postDateValue.equalsIgnoreCase(currentDateValue)) {
                    SimpleDateFormat setDateFormat = new SimpleDateFormat("hh:mm a");
                    setDateValue = setDateFormat.format(new Date(Long.parseLong(String.valueOf(date.getTime()))));
                    strDate = "Today at " + setDateValue;

                } else {

                    SimpleDateFormat setDateFormat = new SimpleDateFormat("dd MMM yyyy");
                    setDateValue = setDateFormat.format(new Date(Long.parseLong(String.valueOf(date.getTime()))));

                    SimpleDateFormat setHourFormat = new SimpleDateFormat("hh:mm a");
                    setHourValue = setHourFormat.format(new Date(Long.parseLong(String.valueOf(date.getTime()))));
                    strDate = setDateValue + " at " + setHourValue;
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

            ((ItemViewHolder) holder).layoutUserPostBinding.tvPostTime.setText(strDate);
            if (post.getPost().getStatus()) {
                ((ItemViewHolder) holder).layoutUserPostBinding.llComment.setVisibility(View.VISIBLE);
            } else {
                ((ItemViewHolder) holder).layoutUserPostBinding.llComment.setVisibility(View.GONE);
            }

            ((ItemViewHolder) holder).layoutUserPostBinding.tvPostLike.setText(String.valueOf(post.getLikesCount()));
            ((ItemViewHolder) holder).layoutUserPostBinding.tvPostComment.setText(String.valueOf(post.getComments()));


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
                setSpanableText(post, holder, tagFriends, postText, userNameLength, firstTagLength, checkInLength, reducedCountValue);

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

            if (post.getCommentArr().size() > 0)
            {
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
                ((ItemViewHolder) holder).layoutUserPostBinding.ivGrirdPostImage.setVisibility(View.VISIBLE);
                ((ItemViewHolder) holder).layoutUserPostBinding.tvGridPostTitle.setVisibility(View.GONE);

                ((MyProfileAdapter.ItemViewHolder) holder).layoutUserPostBinding.llFramePic.setVisibility(View.VISIBLE);
                if (post.getImages().get(0).getFile().getUrl() != null && !post.getImages().get(0).getFile().getUrl().equalsIgnoreCase("")) {
                    Glide.with(mContext) // Bind it with the context of the actual view used
                            .load(post.getImages().get(0).getFile().getUrl()) // Load the image
                            .asBitmap() // All our images are static, we want to display them as bitmaps
                            .format(DecodeFormat.PREFER_RGB_565) // the decode format - this will not use alpha at all
                            .centerCrop()// temporary holder displayed while the image loads// need to manually set the animation as bitmap cannot use cross fade
                            .thumbnail(0.2f)// make use of the thumbnail which can display a down-sized version of the image
                            .into(((ItemViewHolder) holder).layoutUserPostBinding.ivGrirdPostImage); // Voilla - the target view
                    setUpViewpager(((MyProfileAdapter.ItemViewHolder) holder));
                } else {
                    ((ItemViewHolder) holder).layoutUserPostBinding.ivGrirdPostImage.setVisibility(View.GONE);
                    ((ItemViewHolder) holder).layoutUserPostBinding.tvGridPostTitle.setVisibility(View.VISIBLE);
                    if (post.getPost().getDescription() != null && post.getPost().getDescription() != "") {
                        ((ItemViewHolder) holder).layoutUserPostBinding.tvGridPostTitle.setText(post.getPost().getDescription());
                        ((ItemViewHolder) holder).layoutUserPostBinding.tvGridPostTitle.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                ((ItemViewHolder) holder).layoutUserPostBinding.llFramePic.setVisibility(View.GONE);
                if (post.getPost().getDescription() != null && post.getPost().getDescription() != "") {
                    ((ItemViewHolder) holder).layoutUserPostBinding.tvGridPostTitle.setText(post.getPost().getDescription());
                    ((ItemViewHolder) holder).layoutUserPostBinding.tvGridPostTitle.setVisibility(View.VISIBLE);
                }
            }

            CommonUtils.otherUserProfile(mContext, ((ItemViewHolder) holder).layoutUserPostBinding.ivPostProfilePic, post.getUser().getImage(), ((ItemViewHolder) holder).layoutUserPostBinding.tvUserImage,
                    post.getUser().getName());


            // Picasso.with(mContext).load(R.mipmap.userprofile_icon).into(((ItemViewHolder) holder).layoutUserPostBinding.ivPostProfilePic);


            if (post.getCurrentUserLike()) {
                likedStatus = false;
                ((ItemViewHolder) holder).layoutUserPostBinding.ivHeart.setImageResource(R.mipmap.heart_icon_sel);
            } else {
                likedStatus = true;
                ((ItemViewHolder) holder).layoutUserPostBinding.ivHeart.setImageResource(R.mipmap.heart_icon);
            }

            if (post.getPinned()) {
                ((ItemViewHolder) holder).layoutUserPostBinding.ivPinned.setImageResource(R.drawable.unpin);
            } else {
                ((ItemViewHolder) holder).layoutUserPostBinding.ivPinned.setImageResource(R.drawable.pin);
            }

            ((ItemViewHolder) holder).layoutUserPostBinding.tvPostLike.setText(String.valueOf(post.getLikesCount()));
        } else {
            ((HeaderViewHolder) holder).setHeaderAdapter();
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
                // super.updateDrawState(ds);
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
                    Intent intent = new Intent(mContext, TaggedUsersActivity.class);
                    intent.putExtra(AppConstants.POST_ID, postId);
                    intent.putExtra(AppConstants.SHARED_ID, sharedId);
                    mContext.startActivity(intent);
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
    private void callLikePostAPI(final int position, String postId, final boolean isLiked) {
        if (CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("post_id", postId);
            jsonObject.addProperty("status", isLiked);
            Call<ApiResponse> call = ApiExecutor.getClient(mContext).apiLikePost(jsonObject);
            call.enqueue(new retrofit2.Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            if (isLiked) {
                                int count = (post.getLikesCount() + 1);
                                posts.get(position).setLikesCount(count);
                                posts.get(position).setCurrentUserLike(true);
                            } else {
                                if (post.getLikesCount() > 0) {
                                    int count = (post.getLikesCount() - 1);
                                    posts.get(position).setLikesCount(count);
                                    posts.get(position).setCurrentUserLike(false);
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

    private void apiFollowUser(UnFollowRequest model) {
        if (CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();
            Call<ApiResponse> call = ApiExecutor.getClient(mContext).apiFollowUser(model);
            call.enqueue(new retrofit2.Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            userModel.setIs_follow("2");
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

    private void apiUnfollowUser(UnFollowRequest model) {
        if (CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();

            Call<ApiResponse> call = ApiExecutor.getClient(mContext).apiUnFollowUser(model);
            call.enqueue(new retrofit2.Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            userModel.setIs_follow("0");
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

    private void setUpViewpager(MyProfileAdapter.ItemViewHolder viewHolder) {
        CustomPageAdapter mCustomPagerAdapter = new CustomPageAdapter(mContext, post.getImages());
        viewHolder.layoutUserPostBinding.viewPager.setAdapter(mCustomPagerAdapter);
       /* MyPagerAdapter adapter = new MyPagerAdapter(((AppCompatActivity)mContext).getSupportFragmentManager());
        viewHolder.layoutUserPostBinding.viewPager.setAdapter(adapter);*/


        if (post.getImages() != null && post.getImages().size() > 1) {
            viewHolder.layoutUserPostBinding.tabLayout.setVisibility(View.VISIBLE);
            viewHolder.layoutUserPostBinding.tabLayout.setupWithViewPager(viewHolder.layoutUserPostBinding.viewPager, true);
        } else {
            viewHolder.layoutUserPostBinding.tabLayout.setVisibility(View.GONE);
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
            Glide.with(galleryView.getContext()) // Bind it with the context of the actual view used
                    .load(galleryItem) // Load the image
                    .asBitmap() // All our images are static, we want to display them as bitmaps
                    .format(DecodeFormat.PREFER_RGB_565) // the decode format - this will not use alpha at all
                    .centerCrop()// temporary holder displayed while the image loads// need to manually set the animation as bitmap cannot use cross fade
                    .thumbnail(0.2f)// make use of the thumbnail which can display a down-sized version of the image
                    .into(galleryView); // Voilla - the target view
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private LayoutUserPostBinding layoutUserPostBinding;

        public ItemViewHolder(View itemView) {
            super(itemView);
            layoutUserPostBinding = DataBindingUtil.bind(itemView);
            layoutUserPostBinding.ivPostImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(layoutUserPostBinding.ivPostImage, getAdapterPosition() - 1);
                }
            });

            layoutUserPostBinding.tvPostUserName.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
            layoutUserPostBinding.llListRow.setBackgroundColor(ContextCompat.getColor(mContext, themes.setDarkTheme()));
            layoutUserPostBinding.tvPostLike.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
            layoutUserPostBinding.tvPostComment.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
            layoutUserPostBinding.tvPostShare.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
            layoutUserPostBinding.tvPostContent.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
            layoutUserPostBinding.tvPostContentSimple.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
            layoutUserPostBinding.tvComment.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
            layoutUserPostBinding.tvCommentDescription.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
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
        //    themes.changeIconColorToWhite(mContext, layoutUserPostBinding.ivPostDownloads);


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
            layoutUserPostBinding.llShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(layoutUserPostBinding.llShare, getAdapterPosition() - 1);
                }
            });
            layoutUserPostBinding.ivPostMenuItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(layoutUserPostBinding.ivPostMenuItem, getAdapterPosition() - 1);
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


                    if (posts.get(getAdapterPosition() - 1).isOpenPopup()) {
                        posts.get(getAdapterPosition() - 1).setOpenPopup(false);
                    } else {
                        posts.get(getAdapterPosition() - 1).setOpenPopup(true);
                    }


                }
            });
            layoutUserPostBinding.tvPostUserName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(layoutUserPostBinding.tvPostUserName, getAdapterPosition() - 1);
                }
            });
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


            layoutUserPostBinding.ivPostImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(new PrefManager(mContext).getUserId().equalsIgnoreCase(posts.get(getAdapterPosition() - 1).getCommentArr().get(0).getUser().getId()))) {
                        mContext.startActivity(new Intent(mContext, MyProfileActivity.class)
                                .putExtra("user_id", posts.get(getAdapterPosition() - 1).getCommentArr().get(0).getUser().getId())
                                .putExtra(AppConstants.IS_USER_POST, true));
                    }
                }
            });

            layoutUserPostBinding.ivPostDownloads.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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


                    listener.onItemClick(layoutUserPostBinding.ivPostDownloads, getAdapterPosition() - 1);

                }
            });

            layoutUserPostBinding.postHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, ShowPostItemsActivity.class)
                            .putExtra("post_id", posts.get(getAdapterPosition() - 1).getPost().getId())
                            .putExtra("shared_id", posts.get(getAdapterPosition() - 1).getSharedId())
                            .putExtra(AppConstants.IS_USER_POST, true));
                }
            });

            layoutUserPostBinding.llListGrid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, ShowPostItemsActivity.class)
                            .putExtra("post_id", posts.get(getAdapterPosition() - 1).getPost().getId())
                            .putExtra("shared_id", posts.get(getAdapterPosition() - 1).getSharedId())
                            .putExtra(AppConstants.IS_USER_POST, true));
                }
            });


            //==========================================================================================

            layoutUserPostBinding.tvPostTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(layoutUserPostBinding.tvPostTime, getAdapterPosition() - 1);
                }
            });

            layoutUserPostBinding.ivHeart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (!posts.get(getAdapterPosition() - 1).getCurrentUserLike()) {
                        int count = (post.getLikesCount() + 1);
                        posts.get(getAdapterPosition() - 1).setLikesCount(count);
                        posts.get(getAdapterPosition() - 1).setCurrentUserLike(true);
                    } else {
                        if (post.getLikesCount() > 0) {
                            int count = (post.getLikesCount() - 1);
                            posts.get(getAdapterPosition() - 1).setLikesCount(count);
                        } else {
                            posts.get(getAdapterPosition() - 1).setLikesCount(0);
                        }
                        posts.get(getAdapterPosition() - 1).setCurrentUserLike(false);
                    }
                    notifyDataSetChanged();


                   /* if (CommonUtils.isOnline(mContext)) {
                      //  callLikePostAPI(getAdapterPosition() - 1, posts.get(getAdapterPosition() - 1).getPost().getId(), likedStatus);
                    } else {
                        ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
                    }*/

                    listener.onItemClick(layoutUserPostBinding.ivHeart, getAdapterPosition() - 1);
                }
            });


            layoutUserPostBinding.ivPinned.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (posts.get(getAdapterPosition() - 1).getPinned()) {
                        posts.get(getAdapterPosition() - 1).setPinned(false);
                    } else {
                        posts.get(getAdapterPosition() - 1).setPinned(true);
                    }
                    notifyDataSetChanged();


               /*     if (CommonUtils.isOnline(mContext)) {
                        callPinnedApi(posts, getAdapterPosition() - 1);
                    } else {
                        ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
                    }
                }*/
                }
            });
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


        private void facebookSharing(int position) {
            if (CommonUtils.isOnline(mContext)) {
                callbackManager = CallbackManager.Factory.create();
                ShareDialog shareDialog = new ShareDialog((Activity) mContext);
                imagePath = posts.get(position).getImages().get(0).getFile().getUrl();

                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setQuote("New Post")
                            .setContentUrl(Uri.parse(imagePath))
                            .build();
                    shareDialog.show(linkContent, ShareDialog.Mode.FEED);
                }
                shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        if (CommonUtils.isOnline(mContext)) {
                            CommonUtils.showToast(mContext, "Posted successfully.");
                        } else {
                            CommonUtils.showToast(mContext, mContext.getString(R.string.internet_alert_msg));
                        }
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException error) {

                    }
                });

            } else {
                CommonUtils.showToast(mContext, mContext.getString(R.string.internet_alert_msg));
            }
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        private HeaderProfileActivityBinding binding;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            // binding.toolbarFollowerProfile.tvHeader.setText(TextUtils.isEmpty(userModel.getName()) ? "" : userModel.getName());
              binding.llFollowers.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      listener.onItemClick(binding.llFollowers, getAdapterPosition());

                  }
              });
              binding.llFollowing.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      listener.onItemClick(binding.llFollowing, getAdapterPosition());

                  }
              });
            binding.ivSettingProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(binding.ivSettingProfile, getAdapterPosition() - 1);
                }
            });
            pagerAdapter = new TestFragmentAdapter(((MyProfileActivity) mContext).getSupportFragmentManager(), mContext, CONTENT2);
            binding.kkpager.setAdapter(pagerAdapter);
            binding.kkpager.setAnimationEnabled(true);
            binding.kkpager.setFadeFactor(0.3f);
            binding.kkpager.setPageMargin(50);
            binding.kkpager.setPadding(45, 20, 45, 20);
            binding.kkpager.setCurrentItem(1);
            binding.llMain.setBackgroundColor(ContextCompat.getColor(mContext, themes.setDarkTheme()));
            binding.tvFollowers.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
            binding.tvFollowing.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
            binding.tvName.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
            binding.tvUserName.setTextColor(ContextCompat.getColor(mContext, themes.setGreyHint()));
            binding.tvBio.setTextColor(ContextCompat.getColor(mContext, themes.setGreyHint()));
            binding.tvPostCount.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
            binding.tvFollowProfile.setBackground(ContextCompat.getDrawable(mContext, themes.setRejectDrawable()));
            binding.tvMessageProfile.setBackground(ContextCompat.getDrawable(mContext, themes.setRejectDrawable()));
            binding.tvFollowProfile.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
            binding.tvMessageProfile.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
            binding.tvSeeMoreLess.setTextColor(ContextCompat.getColor(mContext, themes.setYellow()));
            binding.tvGenderDes.setTextColor(ContextCompat.getColor(mContext, themes.setDarkGreyTextColor()));
            binding.tvGender.setTextColor(ContextCompat.getColor(mContext, themes.setDarkGreyTextColor()));
            binding.tvGenderColon.setTextColor(ContextCompat.getColor(mContext, themes.setDarkGreyTextColor()));
            binding.tvDobDes.setTextColor(ContextCompat.getColor(mContext, themes.setDarkGreyTextColor()));
            binding.tvDobColon.setTextColor(ContextCompat.getColor(mContext, themes.setDarkGreyTextColor()));
            binding.tvDateOfBirth.setTextColor(ContextCompat.getColor(mContext, themes.setDarkGreyTextColor()));
            binding.tvPhoneNo.setTextColor(ContextCompat.getColor(mContext, themes.setDarkGreyTextColor()));
            binding.tvPhoneDes.setTextColor(ContextCompat.getColor(mContext, themes.setDarkGreyTextColor()));
            binding.tvPhoneColon.setTextColor(ContextCompat.getColor(mContext, themes.setDarkGreyTextColor()));
            binding.tvPinned.setTextColor(ContextCompat.getColor(mContext, themes.setPinnedText()));
            themes.changeIconColor(mContext, binding.ivDropDown);
            themes.changeIconWhite(mContext, binding.ivSettingProfile);
            themes.changeIconColor(mContext, binding.ivPinned);

            if (userModel.getIs_follow() != null && userModel.getIs_follow().length() > 0) {
                switch (userModel.getIs_follow()) {
                    case "0":
                        binding.tvFollowingProfile.setText("Follow");
                        binding.tvFollowingProfile.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_home_edittext));
                        break;
                    case "1":
                        binding.tvFollowingProfile.setText("Following");
                        binding.tvFollowingProfile.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_yellow_soft_corner));
                        break;
                    case "2":
                        binding.tvFollowingProfile.setText("Requested");
                        binding.tvFollowingProfile.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_home_edittext));
                        break;
                    default:
                        binding.tvFollowingProfile.setText("Follow");
                        binding.tvFollowingProfile.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_home_edittext));
                        break;
                }
            }


            binding.tvFollowingProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (binding.tvFollowingProfile.getText().toString()) {
                        case "Following":
                            if (darkThemeEnabled) {
                                final CharSequence[] items = {Html.fromHtml("<font color = '#FFFFFF'>Unfollow</font>"), Html.fromHtml("<font color = '#DD4040'>Cancel</font>")};
                                // final CharSequence[] items = {getString(R.string.themes), getString(R.string.gallery), getString(R.string.solid_color), getString(R.string.no_wallpaper), getString(R.string.default_wallpaper)};
                                new MaterialDialog.Builder(mContext)
                                        .title("Are you sure you want to Unfollow " + userModel.getName() + " ?").titleGravity(GravityEnum.CENTER)
                                        .items(items)
                                        .backgroundColor(mContext.getResources().getColor(R.color.dialogdark))
                                        .titleColor(Color.parseColor("#ffffff"))
                                        .itemsCallback(new MaterialDialog.ListCallback() {
                                            @Override
                                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                                switch (position) {
                                                    case 1:
                                                        dialog.dismiss();
                                                        break;
                                                    case 0:
                                                        UnFollowRequest userFollow = new UnFollowRequest();
                                                        UserFollow follow = new UserFollow();
                                                        follow.setAction("unfollow");
                                                        follow.setId(userModel.getId());
                                                        userFollow.setUser(follow);
                                                        // apiUnfollowUser(userFollow);
                                                        dialog.dismiss();
                                                        break;
                                                }

                                            }
                                        }).show();
                            } else {
                                final CharSequence[] items = {Html.fromHtml("<font color = '#FC2B2B'>Unfollow</font>"), Html.fromHtml("<font color = '#FC2B2B'>Cancel</font>")};
                                // final CharSequence[] items = {getString(R.string.themes), getString(R.string.gallery), getString(R.string.solid_color), getString(R.string.no_wallpaper), getString(R.string.default_wallpaper)};
                                new MaterialDialog.Builder(mContext)
                                        .title("Are you sure you want to Unfollow " + userModel.getName() + " ?").titleGravity(GravityEnum.CENTER)
                                        .items(items)
                                        .itemsColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()))
                                        .backgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeStory()))
                                        .titleColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()))
                                        .itemsCallback(new MaterialDialog.ListCallback() {
                                            @Override
                                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                                switch (position) {
                                                    case 1:
                                                        dialog.dismiss();
                                                        break;
                                                    case 0:
                                                        UnFollowRequest userFollow = new UnFollowRequest();
                                                        UserFollow follow = new UserFollow();
                                                        follow.setAction("unfollow");
                                                        follow.setId(userModel.getId());
                                                        userFollow.setUser(follow);
                                                        //apiUnfollowUser(userFollow);
                                                        dialog.dismiss();
                                                        break;
                                                }

                                            }
                                        }).show();
                            }
                            break;
                        case "Follow":
                            final CharSequence[] items1 = {Html.fromHtml("<font color = '#FC2B2B'>Follow</font>"), Html.fromHtml("<font color = '#FC2B2B'>Cancel</font>")};
                            new MaterialDialog.Builder(mContext)
                                    .title("Are you sure you want to Follow " + userModel.getName() + " ?").titleGravity(GravityEnum.CENTER)
                                    .items(items1)
                                    .itemsColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()))
                                    .backgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeStory()))
                                    .titleColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()))
                                    .itemsCallback(new MaterialDialog.ListCallback() {
                                        @Override
                                        public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                            switch (position) {
                                                case 1:
                                                    dialog.dismiss();
                                                    break;
                                                case 0:
                                                    UnFollowRequest userFollow = new UnFollowRequest();
                                                    UserFollow follow = new UserFollow();
                                                    follow.setId(userModel.getId());
                                                    userFollow.setUser(follow);
                                                    //  apiFollowUser(userFollow);
                                                    dialog.dismiss();
                                                    break;
                                            }


                                        }
                                    }).show();
                            break;
                        case "Requested":
                            CommonUtils.showToast(mContext, "You already sent the requested");
                            break;
                    }

                }
            });
            binding.llSeeMoreLess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!AppConstants.seeUSerDetail) {
                        binding.llUserDetils.setVisibility(View.VISIBLE);
                        binding.tvSeeMoreLess.setText(R.string.see_less_info);
                        BitmapDrawable drawable = (BitmapDrawable) CommonUtils.getDrawable(mContext, R.mipmap.down_icon);

                        Bitmap bInput, bOutput;
                        bInput = drawable.getBitmap();
                        Matrix matrix = new Matrix();
                        matrix.preScale(1.0f, -1.0f);
                        bOutput = Bitmap.createBitmap(bInput, 0, 0, bInput.getWidth(), bInput.getHeight(), matrix, true);
                        binding.ivDropDown.setImageBitmap(bOutput);
                        AppConstants.seeUSerDetail = true;
                    } else {
                        binding.llUserDetils.setVisibility(View.GONE);
                        binding.tvSeeMoreLess.setText(R.string.see_more_info);
                        BitmapDrawable drawable = (BitmapDrawable) CommonUtils.getDrawable(mContext, R.mipmap.down_icon);

                        Bitmap bInput, bOutput;
                        bInput = drawable.getBitmap();
                        Matrix matrix = new Matrix();
                        matrix.preScale(-1.0f, 1.0f);
                        bOutput = Bitmap.createBitmap(bInput, 0, 0, bInput.getWidth(), bInput.getHeight(), matrix, true);
                        binding.ivDropDown.setImageBitmap(bOutput);
                        AppConstants.seeUSerDetail = false;
                    }
                }
            });

            if (userModel.getId() != null && userModel.getId().equalsIgnoreCase(new PrefManager(mContext).getUserId())) {
                binding.tvMessageProfile.setVisibility(View.GONE);
            } else {
                binding.tvMessageProfile.setVisibility(View.VISIBLE);
            }

            binding.tvMessageProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, ChatsScreenFrgActivity.class).putExtra(AppConstants.CHAT_USER_ID, userModel.getId()).
                            putExtra(AppConstants.FROM, AppConstants.PROFILE).putExtra(AppConstants.NAME, userModel.getName()).putExtra(AppConstants.QB_ID, userModel.getQb_id()).
                            putExtra(AppConstants.SENDER_ID, PrefManager.getInstance(mContext).getUserId()).
                            putExtra(AppConstants.USER_IMAGE, userModel.getImage()));
                }
            });

            binding.tvName.setText(TextUtils.isEmpty(userModel.getName()) ? "" : userModel.getName());
            if (!TextUtils.isEmpty(userModel.getEmail())) {
                binding.tvUserName.setText(TextUtils.isEmpty(userModel.getEmail()) ? "" : userModel.getEmail());
            }
            binding.llSeeMoreLess.setVisibility(View.VISIBLE);
           // binding.tvFollowers.setText(String.valueOf(userModel.getFollower()));
            binding.tvFollowers.setText("429");
            binding.tvFollowing.setText("478");
           // binding.tvFollowing.setText(String.valueOf(userModel.getFollow()));
            if (posts != null && posts.size() > 0) {
                binding.tvPostCount.setVisibility(View.VISIBLE);
                binding.tvPostCount.setText("" + posts.size() + " Posts");
            } else {
                binding.tvPostCount.setVisibility(View.GONE);
            }
            if (userModel.getAvatar_img() != null && userModel.getAvatar_img().getAvatar_image() != null && !TextUtils.isEmpty(userModel.getAvatar_img().getAvatar_image())) {
                Picasso.with(mContext).load(userModel.getAvatar_img().getAvatar_image()).transform(new CircleTransformation()).memoryPolicy(MemoryPolicy.NO_CACHE).into(binding.ivAvatar);
            }
            // CommonUtils.userProfile(mContext, binding.ivUserPic, binding.tvUserImage);

            if (!TextUtils.isEmpty(userModel.getImage())) {
                /*Picasso.with(mContext).load(userModel.getImage()).placeholder(R.drawable.user_placeholder).
                        transform(new CircleTransformation()).memoryPolicy(MemoryPolicy.NO_CACHE).into(binding.ivUserPic);
          */

            }
            CommonUtils.otherUserProfile(mContext, binding.ivUserPic, userModel.getImage(), binding.tvUserImage,
                    userModel.getName());
            if (userModel.getSchool() != null && userModel.getSchool().size() > 0) {
                binding.rvSchool.setVisibility(View.VISIBLE);

            }

            if (userModel.getCollege() != null && userModel.getCollege().size() > 0) {
                binding.rvCollege.setVisibility(View.VISIBLE);
            }
            if (userModel.getWork() != null && userModel.getWork().size() > 0) {
                binding.rvWorkAt.setVisibility(View.VISIBLE);

            }
            if (!TextUtils.isEmpty(userModel.getCity())) {
                binding.tblLivesIn.setVisibility(View.VISIBLE);
                binding.tvLivesIn.setText(TextUtils.isEmpty(userModel.getCity()) ? "" : userModel.getCity() + ", " + userModel.getCountry());
            }
            if (!TextUtils.isEmpty(userModel.getHometown())) {
                binding.tblHomeTown.setVisibility(View.VISIBLE);
                binding.tvHomeTown.setText(TextUtils.isEmpty(userModel.getHometown()) ? "" : userModel.getHometown());
            }
            if (!TextUtils.isEmpty(userModel.getEmail())) {
                binding.tblEmail.setVisibility(View.VISIBLE);
                binding.tvEmail.setText(TextUtils.isEmpty(userModel.getEmail()) ? "" : userModel.getEmail());
            }
            if (!TextUtils.isEmpty(userModel.getWebsite())) {
                binding.tblWebsite.setVisibility(View.VISIBLE);
                binding.tvWebsite.setText(TextUtils.isEmpty(userModel.getWebsite()) ? "" : userModel.getWebsite());
            }
            if (!TextUtils.isEmpty(userModel.getMobile())) {
                binding.tblPhoneNo.setVisibility(View.VISIBLE);
                binding.tvPhoneNo.setText(TextUtils.isEmpty(userModel.getMobile()) ? "" : userModel.getMobile());
            }
            if (!TextUtils.isEmpty(userModel.getGender())) {
                binding.tblGneder.setVisibility(View.VISIBLE);
                binding.tvGender.setText(TextUtils.isEmpty(userModel.getGender()) ? "" : userModel.getGender());
            }

            if (!TextUtils.isEmpty(userModel.getDob()) && !userModel.getDob().equalsIgnoreCase("0")) {
                binding.tblDob.setVisibility(View.VISIBLE);
                try {
                    binding.tvDateOfBirth.setText(TextUtils.isEmpty(userModel.getDob()) ? "" : DateUtils.getDateOfTimestamp(Long.parseLong(userModel.getDob())));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            binding.tvBio.setText(TextUtils.isEmpty(userModel.getBio()) ? "" : userModel.getBio());
        }

        public void setHeaderAdapter() {
            binding.tvName.setText(TextUtils.isEmpty(userModel.getName()) ? "" : userModel.getName());
            if (userModel.getIs_follow() != null && userModel.getIs_follow().length() > 0) {
                switch (userModel.getIs_follow()) {
                    case "0":
                        binding.tvFollowingProfile.setText("Follow");
                        binding.tvFollowingProfile.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_home_edittext));
                        break;
                    case "1":
                        binding.tvFollowingProfile.setText("Following");
                        binding.tvFollowingProfile.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_yellow_soft_corner));
                        break;
                    case "2":
                        binding.tvFollowingProfile.setText("Requested");
                        binding.tvFollowingProfile.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_home_edittext));
                        break;
                    default:
                        binding.tvFollowingProfile.setText("Follow");
                        binding.tvFollowingProfile.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_home_edittext));
                        break;
                }
            }
         /*   if (userModel.getId() != null && userModel.getId().equalsIgnoreCase(new PrefManager(mContext).getUserId())) {
                binding.llOtherProfile.setVisibility(View.GONE);
                binding.tvEditMyProfile.setVisibility(View.VISIBLE);
            } else {
                binding.llOtherProfile.setVisibility(View.VISIBLE);
                binding.tvEditMyProfile.setVisibility(View.GONE);
            }*/

            if (from.equalsIgnoreCase("myprofile")) {
                binding.llOtherProfile.setVisibility(View.GONE);
                binding.tvEditMyProfile.setVisibility(View.VISIBLE);
            } else {
                binding.llOtherProfile.setVisibility(View.VISIBLE);
                binding.tvEditMyProfile.setVisibility(View.GONE);
            }


            if (!TextUtils.isEmpty(userModel.getEmail())) {
                binding.tvUserName.setText(TextUtils.isEmpty(userModel.getEmail()) ? "" : userModel.getEmail());
            }
            binding.llSeeMoreLess.setVisibility(View.VISIBLE);
            binding.tvFollowers.setText(String.valueOf(userModel.getFollower()));
            binding.tvFollowing.setText(String.valueOf(userModel.getFollow()));
            binding.tvEditMyProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, EditProfileActivity.class);
                    mContext.startActivity(intent);
                }
            });
            /*if (!TextUtils.isEmpty(userModel.getAvatar_img().getImage())) {
                Picasso.with(mContext).load(userModel.getAvatar_img().getImage())
                        .placeholder(R.mipmap.avaterimg).
                transform(new CircleTransformation()).memoryPolicy(MemoryPolicy.NO_CACHE).into(binding.ivAvatar);
            }*/
            //  CommonUtils.userProfile(mContext, binding.ivUserPic, binding.tvUserImage);

     /*       if (!TextUtils.isEmpty(userModel.getImage())) {
                Picasso.with(mContext).load(userModel.getImage()).placeholder(R.drawable.user_placeholder).
                        transform(new CircleTransformation()).memoryPolicy(MemoryPolicy.NO_CACHE).into(binding.ivUserPic);
            }*/

            CommonUtils.otherUserProfile(mContext, binding.ivUserPic, userModel.getImage(), binding.tvUserImage,
                    userModel.getName());

            if (userModel.getSchool() != null && userModel.getSchool().size() > 0) {
                binding.rvSchool.setVisibility(View.VISIBLE);
                // arlSchool.clear();
                // arlSchool.addAll(userModel.getSchool());
                // adapterSchool.notifyDataSetChanged();
            }
            if (posts != null && posts.size() > 0) {
                binding.tvPostCount.setVisibility(View.VISIBLE);
                binding.tvPostCount.setText("" + posts.size() + " Posts");
            } else {
                binding.tvPostCount.setVisibility(View.GONE);
            }
            if (userModel.getCollege() != null && userModel.getCollege().size() > 0) {
                binding.rvCollege.setVisibility(View.VISIBLE);
                //arlCollege.addAll(userModel.getCollege());
                // adapterCollege.notifyDataSetChanged();
            }
            if (userModel.getWork() != null && userModel.getWork().size() > 0) {
                binding.rvWorkAt.setVisibility(View.VISIBLE);
            }
            if (!TextUtils.isEmpty(userModel.getCity())) {
                binding.tblLivesIn.setVisibility(View.VISIBLE);
                binding.tvLivesIn.setText(TextUtils.isEmpty(userModel.getCity()) ? "" : userModel.getCity() + ", " + userModel.getCountry());
            }
            if (!TextUtils.isEmpty(userModel.getHometown())) {
                binding.tblHomeTown.setVisibility(View.VISIBLE);
                binding.tvHomeTown.setText(TextUtils.isEmpty(userModel.getHometown()) ? "" : userModel.getHometown());
            }
            if (!TextUtils.isEmpty(userModel.getEmail())) {
                binding.tblEmail.setVisibility(View.VISIBLE);
                binding.tvEmail.setText(TextUtils.isEmpty(userModel.getEmail()) ? "" : userModel.getEmail());
            }
            if (!TextUtils.isEmpty(userModel.getWebsite())) {
                binding.tblWebsite.setVisibility(View.VISIBLE);
                binding.tvWebsite.setText(TextUtils.isEmpty(userModel.getWebsite()) ? "" : userModel.getWebsite());
            }
            if (!TextUtils.isEmpty(userModel.getPhone_no())) {
                binding.tblPhoneNo.setVisibility(View.VISIBLE);
                binding.tvPhoneNo.setText(TextUtils.isEmpty(userModel.getPhone_no()) ? "" : userModel.getPhone_no());
            }
            if (!TextUtils.isEmpty(userModel.getGender())) {
                binding.tblGneder.setVisibility(View.VISIBLE);
                binding.tvGender.setText(TextUtils.isEmpty(userModel.getGender()) ? "" : userModel.getGender());
            }

            if (!TextUtils.isEmpty(userModel.getDob()) && !userModel.getDob().equalsIgnoreCase("0")) {
                binding.tblDob.setVisibility(View.VISIBLE);
                try {
                    binding.tvDateOfBirth.setText(TextUtils.isEmpty(userModel.getDob()) ? "" : DateUtils.getDateOfTimestamp(Long.parseLong(userModel.getDob())));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            binding.tvBio.setText(TextUtils.isEmpty(userModel.getBio()) ? "" : userModel.getBio());
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
            ImageView displayImage = (ImageView) view.findViewById(R.id.ivPostImage);
            JZVideoPlayerStandard jCVideoPlayer = (JZVideoPlayerStandard) view.findViewById(R.id.videocontroller);
            displayImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ShowPostItemsActivity.class);
                    intent.putExtra("url", dataObjectList.get(position).getFile().getUrl());
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

}
