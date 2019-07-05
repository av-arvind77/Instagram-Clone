package com.yellowseed.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.apradanas.simplelinkabletext.Link;
import com.facebook.CallbackManager;
import com.yellowseed.R;
import com.yellowseed.adapter.CommentsAdapter;
import com.yellowseed.adapter.HomeBottomViewAdapter;
import com.yellowseed.adapter.ShowPostItemAdapter;
import com.yellowseed.adapter.TagFollowingAdapter;
import com.yellowseed.databinding.ActivityShowPostItemsBinding;
import com.yellowseed.databinding.DialogShareBinding;
import com.yellowseed.databinding.LayoutRepostBinding;
import com.yellowseed.fragments.DirectShareFragment;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.resModel.GetChatResonse;
import com.yellowseed.model.resModel.GetRoomResonse;
import com.yellowseed.utils.ActivityController;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiResponse;
import com.yellowseed.webservices.response.homepost.Post;
import com.yellowseed.webservices.response.homepost.TagFriend;

import java.util.ArrayList;
import java.util.List;

import tech.gusavila92.websocketclient.WebSocketClient;

public class ShowPostItemsActivity extends BaseActivity {

    private ActivityShowPostItemsBinding binding;
    private Context context;
    private ShowPostItemAdapter showPostItemAdapter;
    private TagFollowingAdapter tagFollowingAdapter;
    private boolean flag = false;
    private String postId = "";
    private String userIdSelected = "";
    private String sharedId = "";
    private boolean isUserPost;
    private String postText;
    private List<TagFriend> tagFriends = new ArrayList<>();
    private String checkInPlace;
    private int reducedCountValue;
    private int userNameLength;
    private int firstTagLength;
    private boolean isLiked, isPinned;
    private int checkInLength;
    private String report;
    private Post postClick;
    private List<Post> postDetailList = new ArrayList<>();
    private ArrayList<ApiResponse.CommentArrBean> comments = new ArrayList<>();
    private CommentsAdapter commentsAdapter;
    private CallbackManager callbackManager;
    private String imagePath = "";
    private ArrayList<GetRoomResonse.RoomsBean> roomList = new ArrayList<>();
    private Link linkHashtag;
    private WebSocketClient webSocketClient;
    private GetChatResonse.UserInfoBean chatScreenModel;
    private boolean darkThemeEnabled;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_post_items);
        context = ShowPostItemsActivity.this;
        darkThemeEnabled = CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME);
        context = ShowPostItemsActivity.this;
        initializedControl();
        setOnClickListener();
        setToolBar();
        setCurrentTheme();

    }

    private void setCurrentTheme() {
        binding.toolbarShowPost.toolbarMain.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));

        binding.customShareDialogShowPost.llMain.setBackground(ContextCompat.getDrawable(context, Themes.getInstance(context).setRoundedBlackBackground()));
        binding.llMain.setBackground(ContextCompat.getDrawable(context, Themes.getInstance(context).setDarkTheme()));
        binding.customShareDialogShowPost.viewLine1.setBackgroundColor(ContextCompat.getColor(context,Themes.getInstance(context).setViewLineGrey()));
        binding.customShareDialogShowPost.viewLine2.setBackgroundColor(ContextCompat.getColor(context,Themes.getInstance(context).setViewLineGrey()));
        binding.tvPostUserNameShowPost.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.tvComment.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        Themes.getInstance(context).changeIconColor(context, binding.toolbarShowPost.ivLeft);
        binding.toolbarShowPost.tvHeader.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.customShareDialogShowPost.tvShareFb.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.customShareDialogShowPost.tvDelete.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.customShareDialogShowPost.tvEdit.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));

        binding.tvViews.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeGreyText()));

        Themes.getInstance(context).changeIconColorToWhite(context, binding.customShareDialogShowPost.ivArrow);
        Themes.getInstance(context).changeIconColorToWhite(context, binding.customShareDialogShowPost.ivShare1);
        Themes.getInstance(context).changeRightIcon(context, binding.customShareDialogShowPost.ivEdit);
       // Themes.getInstance(context).changeIconColorToWhite(context, binding.ivPostDownloads);
        Themes.getInstance(context).changeIconColorToWhite(context, binding.ivPostComment);
        if(darkThemeEnabled)
        {
            binding.ivPostDownloads.setImageResource(R.mipmap.download_home);

        }
        else {
            binding.ivPostDownloads.setImageResource(R.mipmap.download_white);

        }
        Themes.getInstance(context).changeIconColorToWhite(context, binding.ivPostShareShowPost);
        Themes.getInstance(context).changeIconColorToWhite(context, binding.ivPostMenuItemShowPost);

    }

    @Override
    public void initializedControl() {

    }

    @Override
    public void setToolBar() {
        binding.toolbarShowPost.ivLeft.setVisibility(View.VISIBLE);
        binding.toolbarShowPost.ivLeft.setImageResource(R.mipmap.back_icon);
        binding.toolbarShowPost.tvHeader.setVisibility(View.VISIBLE);
        binding.toolbarShowPost.tvHeader.setText(R.string.photo);
    }

    @Override
    public void setOnClickListener() {
        binding.toolbarShowPost.ivLeft.setOnClickListener(this);
        binding.ivPostMenuItemShowPost.setOnClickListener(this);
        binding.ivPostShareShowPost.setOnClickListener(this);
        binding.ivPostComment.setOnClickListener(this);
        binding.llMain.setOnClickListener(this);
        binding.customShareDialogShowPost.tvShareFb.setOnClickListener(this);
        binding.customShareDialogShowPost.tvDelete.setOnClickListener(this);
        binding.customShareDialogShowPost.tvEdit.setOnClickListener(this);

        binding.ivHeartShowPost.setOnClickListener(this);
        binding.ivPostDownloads.setOnClickListener(this);
        binding.tvPostUserNameShowPost.setOnClickListener(this);
        binding.ivPostProfilePicShowPost.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ivPostDownloads:
                ToastUtils.showToastShort(context, "Work in Progress!");
                hideShareShowPostDialog();
                break;
            case R.id.tvPostUserNameShowPost:
                ActivityController.startActivity(context, FollowerProfileActivity.class);
                hideShareShowPostDialog();
                break;
            case R.id.ivPostProfilePicShowPost:
                ActivityController.startActivity(context, FollowerProfileActivity.class);
                hideShareShowPostDialog();
                break;
            case R.id.ivLeft:
                onBackPressed();
                break;
            case R.id.ivHeartShowPost:
                hideShareShowPostDialog();
                break;

            case R.id.tvShareFb:
                CommonUtils.showToast(context, "Work in progress");
                break;
            case R.id.tvDelete:
                CommonUtils.showToast(context, "Work in progress");
                break;
            case R.id.tvEdit:
                CommonUtils.showToast(context, "Work in progress");
                break;
            case R.id.ivPostMenuItemShowPost:
                if (!flag) {
                    binding.llCustomShareShowPost.setVisibility(View.VISIBLE);
                    flag = true;
                } else
                    hideShareShowPostDialog();
                break;
            case R.id.ivPostShareShowPost:
                setPostShareDialoge();
                hideShareShowPostDialog();
                break;
            case R.id.ivPostComment:
                ActivityController.startActivity(context, CommentsActivity.class);
                hideShareShowPostDialog();
                break;
            case R.id.llMain:
                hideShareShowPostDialog();
                break;
            default:
                break;
        }
    }


    private void hideShareShowPostDialog() {
        binding.llCustomShareShowPost.setVisibility(View.GONE);
        flag = false;
    }

    private void setPostShareDialoge() {


        DialogShareBinding dialogShareBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_share, null, false);
        dialog = DialogUtils.createDialog(context, dialogShareBinding.getRoot());
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialogShareBinding.llMain.setBackground(ContextCompat.getDrawable(context, Themes.getInstance(context).setDarkStoryBackground()));
        dialogShareBinding.tvRepost.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        dialogShareBinding.tvDirectShare.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        dialogShareBinding.tvRepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RePostActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        dialogShareBinding.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialogShareBinding.tvDirectShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  setPostShareDialogeRecyclerView();
                DirectShareFragment directShareFragment =
                        new DirectShareFragment();
                directShareFragment.show(getSupportFragmentManager(),
                        "directShare");

                /*if (CommonUtils.isOnline(context)) {
                     callGetRoomAPI(post);
                } else {
                    CommonUtils.showToast(context, getString(R.string.internet_alert_msg));
                }*/
                dialog.dismiss();

            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.background_dark);


    }


    private void setPostShareDialogeRecyclerView() {

        final LayoutRepostBinding repostBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_repost, null, false);

        final Dialog dialog = DialogUtils.createDialog(context, repostBinding.getRoot());
        repostBinding.llRepost.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeDialog()));
        repostBinding.etRepostSearch.setBackground(ContextCompat.getDrawable(context, Themes.getInstance(context).setDarkSearchDrawable()));
        repostBinding.viewLine.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setViewLineGrey()));
        repostBinding.etRepostSearch.setHintTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkHintColor()));
        repostBinding.etRepostSearch.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        repostBinding.etCaption.setBackground(ContextCompat.getDrawable(context, Themes.getInstance(context).setDarkSearchDrawable()));
        repostBinding.etCaption.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
       /* Themes.getInstance(context).changeCrossIconColor(context, repostBinding.ivCancelRepost);

        repostBinding.ivCancelRepost.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });*/
        repostBinding.ivSendRepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //Setting the recycler view inside the dialog

        repostBinding.rvRepostHome.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        repostBinding.rvRepostHome.setLayoutManager(linearLayoutManager);
        tagFollowingAdapter = new TagFollowingAdapter(context, roomList, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
              /*  if (roomList.get(position).isIs_selected()) {
                    roomList.get(position).setIs_selected(false);
                } else {
                    roomList.get(position).setIs_selected(true);
                }*/
                tagFollowingAdapter.notifyItemChanged(position);
            }
        });
        repostBinding.rvRepostHome.setAdapter(tagFollowingAdapter);

        repostBinding.ivSendRepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToastShort(context, "Post has been shared.");

                dialog.dismiss();


            }
        });
    }
}

























 /*   private void setPostShareDialoge() {
        final CharSequence[] items = {getString(R.string.Repost), getString(R.string.directshare)};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals(getString(R.string.Repost))) {

                    ActivityController.startActivity(context, PostTextActivity.class);
                } else if (items[item].equals(getString(R.string.directshare))) {

                    setPostShareDialogeRecyclerView();
                }


            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

    }

    private void setPostShareDialogeRecyclerView() {
        LayoutRepostBinding repostBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_repost, null, false);


        final Dialog dialog = DialogUtils.createDialog(context, repostBinding.getRoot());

        repostBinding.ivCancelRepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });
        repostBinding.ivSendRepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });

        //Setting the recycler view inside the dialog
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        repostBinding.rvRepostHome.setLayoutManager(linearLayoutManager);
        showPostItemAdapter = new ShowPostItemAdapter(context);
        repostBinding.rvRepostHome.setAdapter(showPostItemAdapter);
        repostBinding.rvRepostHome.setNestedScrollingEnabled(false);

    }
}
*/
   /* private void createWebSocketClient() {
        URI uri;

        try {
            uri = new URI(ServiceConstant.WEB_SOCKET_URL + "?id=" + new PrefManager(context).getUserId());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {
                System.out.println("onOpen");
            }

            @Override
            public void onTextReceived(final String message) {

            }

            @Override
            public void onBinaryReceived(byte[] data) {
                // isSocketConneted = true;
                System.out.println("onBinaryReceived");
            }

            @Override
            public void onPingReceived(byte[] data) {
                //isSocketConneted = true;
                System.out.println("onPingReceived");
            }

            @Override
            public void onPongReceived(byte[] data) {
                // isSocketConneted = true;
                System.out.println("onPongReceived");
            }

            @Override
            public void onException(Exception e) {
                //  isSocketConneted = false;

                System.out.println("onException");
                e.printStackTrace();
            }

            @Override
            public void onCloseReceived() {
                // isSocketConneted = false;
                System.out.println("onCloseReceived");
            }
        };

        webSocketClient.setConnectTimeout(10000);
        // webSocketClient.setReadTimeout(60000);
        webSocketClient.enableAutomaticReconnection(5000);
        webSocketClient.connect();
    }

    public void sendMessage(GetChatResonse.UserInfoBean chatScreenModel, List<String> roomIds) {
        JsonObject object = new JsonObject();
        object.addProperty("local_message_id", "");
        object.addProperty("sender_id", new PrefManager(context).getUserId());
        object.addProperty("room_id", chatScreenModel.getRoom_id());
        object.addProperty("is_group", "");
        object.addProperty("body", chatScreenModel.getBody());
        object.addProperty("is_group", false);
        object.addProperty("created_at", chatScreenModel.getCreated_at());
        object.addProperty("upload_type", chatScreenModel.getUpload_type());
        object.addProperty("type", "Joined");
        object.addProperty("message_type", "broadcast");
        object.addProperty("is_broadcast", true);
        object.add("room_ids", new Gson().toJsonTree(roomIds).getAsJsonArray());
        if (webSocketClient != null)
            webSocketClient.send(new Gson().toJson(object).toString());
    }

    private void getIntentData() {
        if (getIntent() != null) {


            if (getIntent().getStringExtra("post_id") != null && !getIntent().getStringExtra("post_id").equalsIgnoreCase(""))
                postId = getIntent().getStringExtra("post_id");

            if (getIntent().getStringExtra("shared_id") != null && !getIntent().getStringExtra("shared_id").equalsIgnoreCase(""))
                sharedId = getIntent().getStringExtra("shared_id");
        }
    }

    *//**
 * method for All Comment api
 * <p>
 * Post Detail Api
 * <p>
 * Save Post List Api
 * <p>
 * method for create story api
 * <p>
 * PostShareDialoge
 *
 * @param post
 * <p>
 * LikeCommentApi
 * <p>
 * method for create story api
 *//*
    private void callAllCommentUserAPI() {
        if (CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();

            JsonObject jsonObject = new JsonObject();
            if (sharedId != null && !sharedId.equalsIgnoreCase("")) {
                jsonObject.addProperty("shared_id", sharedId);
            } else {
                jsonObject.addProperty("post_id", postId);
            }
            jsonObject.addProperty("page", "1");

            retrofit2.Call<ApiResponse> call = ApiExecutor.getClient(context).apiAllCommentUser(jsonObject);
            call.enqueue(new retrofit2.Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull retrofit2.Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            comments.clear();
                            comments.addAll(response.body().getComment_arr());
                            initializedControl();
                            commentsAdapter.notifyDataSetChanged();
                        } else {
                            ToastUtils.showToastShort(context, context.getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull retrofit2.Call<ApiResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(context, context.getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(context, context.getString(R.string.internet_alert_msg));
        }
    }

    *//**
 * Post Detail Api
 *//*
    private void callPostDetailApi() {
        JsonObject jsonObject = new JsonObject();
        if (sharedId != null && !sharedId.equalsIgnoreCase("")) {
            jsonObject.addProperty("shared_id", sharedId);
        } else {
            jsonObject.addProperty("post_id", postId);
        }
        final Dialog progressDialog = DialogUtils.customProgressDialog(context);
        progressDialog.show();

        retrofit2.Call<PostDetailResponse> call = ApiExecutor.getClient(context).postDetailApi(jsonObject);
        call.enqueue(new retrofit2.Callback<PostDetailResponse>() {

            @Override
            public void onResponse(retrofit2.Call<PostDetailResponse> call, Response<PostDetailResponse> response) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (response.body() != null && response.body().getResponseCode() == 200) {
                    postDetailList.addAll(response.body().getPosts());
                    if (response.body().getPosts() != null && response.body().getPosts().size() > 0) {
                        if (response.body().getPosts().get(0).getUser()!=null&&new PrefManager(context).getUserId().equalsIgnoreCase(response.body().getPosts().get(0).getUser().getId())) {
                            isUserPost = true;
                        } else {
                            isUserPost = false;
                        }
                    }
                    setPostData();
                } else {
                    CommonUtils.showToast(context, getString(R.string.server_error_msg));
                }
            }

            @Override
            public void onFailure(retrofit2.Call<PostDetailResponse> call, Throwable t) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                t.printStackTrace();
                CommonUtils.showToast(context, getString(R.string.server_error_msg));
            }
        });
    }


    @Override
    public void initializedControl() {
        //Comments Adapter==========================================================================
        binding.rvComments.setNestedScrollingEnabled(false);
        binding.rvComments.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvComments.setLayoutManager(layoutManager);
        commentsAdapter = new CommentsAdapter(ShowPostItemsActivity.class.getSimpleName(), postId, this, comments, context, new inlineReplyClickListener() {
            @Override
            public void onItemClick(View view, int position, String commentId) {
                switch (view.getId()) {

                    case R.id.tvReplyComments:
                        CommonUtils.showSoftKeyboard(ShowPostItemsActivity.this);
                        CommonUtils.savePreferencesString(context, AppConstants.COMMENT_ID, comments.get(position).getComment_id());
                        ToastUtils.showToastShort(context, getString(R.string.reply));
                        break;

                    case R.id.tvLikeComments:
                        if (CommonUtils.isOnline(context)) {
                            callLikeCommentApi(comments.get(position).getComment_id(), "like", true);
                        } else {
                            ToastUtils.showToastShort(context, getString(R.string.internet_alert_msg));
                        }
                        break;
                    case R.id.tvDislikeComments:
                        if (CommonUtils.isOnline(context)) {
                            callLikeCommentApi(comments.get(position).getComment_id(), "dislike", true);
                        } else {
                            ToastUtils.showToastShort(context, getString(R.string.internet_alert_msg));
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        binding.rvComments.setAdapter(commentsAdapter);

    }

    private String setPostTime(String created_at) {


        String strDate = "Not availbale";
        String setDateValue = "";
        String setHourValue = "";
        SimpleDateFormat readDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        readDate.setTimeZone(TimeZone.getTimeZone("GMT")); // missing line
        Date date = null;
        Date postDate = null;
        try {
            date = readDate.parse(created_at);
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
        return strDate;
    }


    @Override
    public void setToolBar() {
        binding.toolbarShowPost.ivLeft.setVisibility(View.VISIBLE);
        binding.toolbarShowPost.ivLeft.setImageResource(R.mipmap.back_icon);
        binding.toolbarShowPost.tvHeader.setVisibility(View.VISIBLE);
        binding.toolbarShowPost.tvHeader.setText(R.string.post);
    }

    @Override
    public void setOnClickListener() {
        binding.toolbarShowPost.ivLeft.setOnClickListener(this);
        binding.llPostItem.ivPostProfilePic.setOnClickListener(this);
        binding.llPostItem.tvPostUserName.setOnClickListener(this);
        binding.llPostItem.tvPostTime.setOnClickListener(this);
        binding.llPostItem.ivHeart.setOnClickListener(this);
        binding.llPostItem.llShare.setOnClickListener(this);
        binding.llPostItem.ivPostDownloads.setOnClickListener(this);
        binding.llPostItem.llComment.setOnClickListener(this);
        binding.llPostItem.ivPostMenuItem.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (postDetailList != null && postDetailList.size() > 1) {
            postClick = postDetailList.get(1);
        }
        switch (v.getId()) {
            case R.id.ivPostProfilePic:
            case R.id.tvPostUserName:
            case R.id.tv_PostTime:
                Intent intent = new Intent(context, MyProfileActivity.class);
                intent.putExtra(AppConstants.USER_ID, postClick.getUser().getId());
                Log.e(AppConstants.USER_ID, postClick.getUser().getId());
                startActivity(intent);
                break;

            case R.id.ivPostDownloads:
                callSavePostListApi(postId, sharedId, true);
                break;

            case R.id.ivHeart:
                if (!postClick.getCurrentUserLike()) {
                    postClick.setCurrentUserLike(true);
                    binding.llPostItem.ivHeart.setImageResource(R.mipmap.heart_icon_sel);
                    int count = (postClick.getLikesCount() + 1);
                    binding.llPostItem.tvPostLike.setText(String.valueOf(count));
                    isLiked = true;
                } else {
                    postClick.setCurrentUserLike(false);
                    binding.llPostItem.ivHeart.setImageResource(R.mipmap.heart_icon);
                    int count = (postClick.getLikesCount() - 1);
                    binding.llPostItem.tvPostLike.setText(String.valueOf(count));

                    isLiked = false;
                }
                if (CommonUtils.isOnline(context)) {
                    callLikePostAPI(postClick.getPost().getId(), isLiked);
                } else {
                    ToastUtils.showToastShort(context, context.getString(R.string.internet_alert_msg));
                }
                break;

            case R.id.llShare:
                if (postClick.getPost()!=null&&postClick.getPost().getId() != null)
                    setPostShareDialoge(postClick);

                break;

            case R.id.ivPinned:

                if (CommonUtils.isOnline(context)) {
                    callPinnedApi(postClick.getPost().getId(), postClick.getPinned());
                } else {
                    ToastUtils.showToastShort(context, context.getString(R.string.internet_alert_msg));
                }

                break;

            case R.id.ivLeft:
                onBackPressed();
                break;


            case R.id.llComment:
                startActivity(new Intent(ShowPostItemsActivity.this, CommentsActivity.class).putExtra("post_id", postId));
                break;


            case R.id.ivPostMenuItem:
                openMenuPopup(isUserPost, binding.llPostItem.ivPostMenuItem);
                break;
            case R.id.tvReportUser:
                addReportDialog();
                break;


            default:
                break;
        }
    }


    *//**
 * Save Post List Api
 *//*
    private void callSavePostListApi(final String postId, final String sharedId, final boolean isSaved) {
        final Dialog progressDialog = DialogUtils.customProgressDialog(context);
        progressDialog.show();

        JsonObject jsonObject = new JsonObject();

        if (sharedId != null && !sharedId.equalsIgnoreCase("")) {
            jsonObject.addProperty("shared_id", sharedId);
        } else {
            jsonObject.addProperty("post_id", postId);
        }

        if (isSaved) {
            jsonObject.addProperty("status", true);
        } else {
            jsonObject.addProperty("status", false);
        }
        Call<ApiResponse> call = ApiExecutor.getClient(context).apiSavePost(jsonObject);
        call.enqueue(new retrofit2.Callback<ApiResponse>() {


            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (response.body() != null && response.body().getResponseCode() == 200) {

                    if (response.body().getResponseMessage() != null && !response.body().getResponseMessage().equalsIgnoreCase("")) {
                        CommonUtils.showToast(context, response.body().getResponseMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                t.printStackTrace();
            }
        });
    }


    private void deletePostPopup(final String postId, final String sharedId) {

        AlertDialog.Builder builder;
        if (darkThemeEnabled) {

            builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom));
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle(R.string.delete_cnfm);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (CommonUtils.isOnline(context)) {
                    deletePostApi(postId, sharedId);
                } else {
                    CommonUtils.showToast(context, getString(R.string.internet_alert_msg));
                }
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


    private void deletePostApi(String postId, String sharedId) {
        final Dialog progressDialog = DialogUtils.customProgressDialog(context);
        progressDialog.show();

        JsonObject jsonObject = new JsonObject();

        if (sharedId != null && !sharedId.equalsIgnoreCase("")) {
            jsonObject.addProperty("shared_id", sharedId);
        } else {
            jsonObject.addProperty("post_id", postId);
        }

        Call<ApiResponse> call = ApiExecutor.getClient(context).apiDeletePost(jsonObject);
        call.enqueue(new retrofit2.Callback<ApiResponse>() {


            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (response.body() != null && response.body().getResponseCode() == 200) {
                    if (response.body().getResponseMessage() != null && !response.body().getResponseMessage().equalsIgnoreCase("")) {
                        CommonUtils.showToast(context, response.body().getResponseMessage());
                    }
                    startActivity(new Intent(ShowPostItemsActivity.this, HomeActivity.class));

                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                t.printStackTrace();
            }
        });
    }


    private void facebookSharing() {
        if (CommonUtils.isOnline(context)) {

            if (postClick.getImages() != null && postClick.getImages().size() > 0 && postClick.getImages().get(0).getFile().getUrl() != null) {
                callbackManager = CallbackManager.Factory.create();
                ShareDialog shareDialog = new ShareDialog((Activity) context);
                imagePath = postClick.getImages().get(0).getFile().getUrl();

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
                        if (CommonUtils.isOnline(context)) {
                            CommonUtils.showToast(context, "Posted successfully.");
                        } else {
                            CommonUtils.showToast(context, context.getString(R.string.internet_alert_msg));
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
                CommonUtils.showToast(context, "You can only share image with facebook.");
            }

        } else {
            CommonUtils.showToast(context, context.getString(R.string.internet_alert_msg));
        }
    }


    private void addReportDialog() {

        final CharSequence[] items = {Html.fromHtml("<font color = '#FC2B2B'>Its Spam</font>"), Html.fromHtml("<font color = '#FC2B2B'>Inappropriate</font>"), Html.fromHtml("<font color = '#FC2B2B'>Its False Story</font>"), Html.fromHtml("<font color = '#FC2B2B'>Hate Speech</font>")};
        new MaterialDialog.Builder(context)
                .title(R.string.helpusunderstand).titleGravity(GravityEnum.CENTER)
                .items(items)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        CharSequence data = items[position];
                        if (String.valueOf(data).equalsIgnoreCase(getString(R.string.itsspam))) {
                            report = getString(R.string.itsspam);
                            dialog.dismiss();
                        } else if (String.valueOf(data).equalsIgnoreCase(getString(R.string.inappropriate))) {
                            report = getString(R.string.inappropriate);
                            dialog.dismiss();
                        } else if (String.valueOf(data).equalsIgnoreCase(getString(R.string.itsfalsestory))) {
                            report = getString(R.string.itsfalsestory);
                            dialog.dismiss();
                        } else if (String.valueOf(data).equalsIgnoreCase(getString(R.string.hatespeech))) {
                            dialog.dismiss();
                            report = getString(R.string.hatespeech);
                        } else {
                            report = "";
                        }
                        if (CommonUtils.isOnline(context)) {

                            callReportPostApi(report, postId, sharedId);
                        } else {
                            CommonUtils.showToast(context, getString(R.string.internet_alert_msg));
                        }

                    }
                }).show();

    }


    private void callReportPostApi(String report, String postId, String sharedId) {
        if (CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();
            JsonObject jsonObject = new JsonObject();
            if (sharedId != null && !sharedId.equalsIgnoreCase("")) {
                jsonObject.addProperty(AppConstants.SHARED_ID, sharedId);
            } else {
                jsonObject.addProperty(AppConstants.POST_ID, postId);
            }
            jsonObject.addProperty(AppConstants.REASON, report);
            Call<ReportPostResponse> call = ApiExecutor.getClient(context).apiReportUser(jsonObject);
            call.enqueue(new retrofit2.Callback<ReportPostResponse>() {
                @Override
                public void onResponse(@NonNull Call<ReportPostResponse> call, @NonNull final Response<ReportPostResponse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            ToastUtils.showToastShort(context, response.body().getResponseMessage());
                        } else {
                            ToastUtils.showToastShort(context, getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ReportPostResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(context, getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(context, getString(R.string.internet_alert_msg));
        }
    }


    *//**
 * method for create story api
 *//*
    private void callLikePostAPI(String postId, final boolean isLiked) {
        if (CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("post_id", postId);
            jsonObject.addProperty("status", isLiked);

            retrofit2.Call<ApiResponse> call = ApiExecutor.getClient(context).apiLikePost(jsonObject);
            call.enqueue(new retrofit2.Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull retrofit2.Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            if (isLiked) {
                                int count = (postClick.getLikesCount() + 1);
                                postClick.setLikesCount(count);
                            } else {
                                if (postClick.getLikesCount() > 0) {
                                    int count = (postClick.getLikesCount() - 1);
                                    postClick.setLikesCount(count);
                                } else {
                                    postClick.setLikesCount(0);
                                }
                            }

                        } else {
                            ToastUtils.showToastShort(context, context.getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull retrofit2.Call<ApiResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(context, context.getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(context, context.getString(R.string.internet_alert_msg));
        }
    }


    private void callPinnedApi(String postId, final boolean isPinned) {
        if (CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("post_id", postId);
            jsonObject.addProperty("status", isPinned);

            Call<ApiResponse> call = ApiExecutor.getClient(context).apiPinnedPost(jsonObject);
            call.enqueue(new retrofit2.Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            if (!postClick.getPinned()) {
                                postClick.setPinned(true);
                                binding.llPostItem.ivPinned.setImageResource(R.drawable.unpin);
                            } else {
                                postClick.setPinned(false);
                                binding.llPostItem.ivHeart.setImageResource(R.drawable.pin);
                            }
                        } else {
                            ToastUtils.showToastShort(context, context.getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(context, context.getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(context, context.getString(R.string.internet_alert_msg));
        }
    }


    *//**
 * PostShareDialoge
 *
 * @param post
 *//*
    private void setPostShareDialoge(final Post post) {

        final CharSequence[] items = {getString(R.string.Repost), getString(R.string.directshare)};
        AlertDialog.Builder builder;
        if (darkThemeEnabled) {

            builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom));
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getString(R.string.Repost))) {
                    Intent intent = new Intent(context, RePostActivity.class);
                    intent.putExtra(AppConstants.POST_MODEL, post);
                    startActivity(intent);
                } else if (items[item].equals(getString(R.string.directshare))) {
                    if (CommonUtils.isOnline(context)) {
                        callGetRoomAPI(post);
                    } else {
                        CommonUtils.showToast(context, getString(R.string.internet_alert_msg));
                    }
//                    setPostShareDialogeRecyclerView();
                }

            }


        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

    }


    private void setPostShareDialogeRecyclerView(final Post postClick) {
        final LayoutRepostBinding repostBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_repost, null, false);
        final Dialog dialog = DialogUtils.createDialog(context, repostBinding.getRoot());

        repostBinding.ivCancelRepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });
        repostBinding.ivSendRepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });

        //Setting the recycler view inside the dialog
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        repostBinding.rvRepostHome.setLayoutManager(linearLayoutManager);
        tagFollowingAdapter = new TagFollowingAdapter(context, roomList, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (roomList.get(position).isIs_selected()) {
                    roomList.get(position).setIs_selected(false);
                } else {
                    roomList.get(position).setIs_selected(true);
                }
                tagFollowingAdapter.notifyItemChanged(position);
            }
        });
        repostBinding.rvRepostHome.setAdapter(tagFollowingAdapter);
        repostBinding.rvRepostHome.setNestedScrollingEnabled(false);

        repostBinding.ivSendRepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tagFollowingAdapter.getSelected().size() > 0) {
                    chatScreenModel = new GetChatResonse.UserInfoBean();
                    chatScreenModel.setCaption(repostBinding.etCaption.getText().toString().trim());
                    chatScreenModel.setCreated_at(String.valueOf(System.currentTimeMillis() / 1000));
                    chatScreenModel.setCreated_timestamp(String.valueOf(System.currentTimeMillis() / 1000));
                    chatScreenModel.setId("");
                    chatScreenModel.setIs_user_sender(true);
                    chatScreenModel.setIs_stared(false);
                    chatScreenModel.setLocal_message_id(String.valueOf(System.currentTimeMillis() / 1000));
                    chatScreenModel.setRead_status(false);
                    chatScreenModel.setReceiver_image("");
                    PostChatBody postChatBody = new PostChatBody();
                    postChatBody.setPost_id(postClick.getPost().getId());
                    postChatBody.setUser_name(postClick.getUser().getName());
                    postChatBody.setUser_image(postClick.getUser().getImage());
                    postChatBody.setDescription(postClick.getPost().getDescription());
                    if (postClick.getImages() != null && postClick.getImages().size() > 0) {
                        postChatBody.setImage(postClick.getImages().get(0).getFile().getUrl());
                    }
                    chatScreenModel.setBody(new Gson().toJson(postChatBody));
                    chatScreenModel.setSender_id(new PrefManager(context).getUserId());
                    chatScreenModel.setUpload_type("post");
                    sendMessage(chatScreenModel, tagFollowingAdapter.getSelected());
                    repostBinding.etCaption.setText("");
                    dialog.dismiss();
                } else {
                    CommonUtils.showToast(context, "Please choose friends.");
                }

            }
        });

    }


    public void setPostData() {

        postClick = postDetailList.get(1);
        String strDate = "Not availbale";
        strDate = CommonUtils.getFormattedDate(postClick);
        binding.llPostItem.tvPostTime.setText(strDate);
        binding.llPostItem.tvPostLike.setText(String.valueOf(postClick.getLikesCount()));
        binding.llPostItem.tvPostComment.setText(String.valueOf(postClick.getComments()));
        binding.llPostItem.tvUserImage.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));

        Themes.getInstance(context).getInstance(context).changeIconColorToWhite(context, binding.llPostItem.ivPostMenuItem);
        Themes.getInstance(context).getInstance(context).changeIconColorToWhite(context, binding.llPostItem.ivPostComment);
        Themes.getInstance(context).getInstance(context).changeIconColorToWhite(context, binding.llPostItem.ivPostShare);
        Themes.getInstance(context).getInstance(context).changeIconColorToWhite(context, binding.llPostItem.ivPinned);
        Themes.getInstance(context).getInstance(context).changeIconColorToWhite(context, binding.llPostItem.ivPostDownloads);

        if (postClick.getPost().getStatus()) {
            binding.llPostItem.llComment.setVisibility(View.VISIBLE);
        } else {
            binding.llPostItem.llComment.setVisibility(View.GONE);
        }
        if (postClick.getUser().getName() != null && !postClick.getUser().getName().isEmpty()) {

            if (postClick.getPost().getCheckIn() != null && !postClick.getPost().getCheckIn().equalsIgnoreCase("")) {
                checkInPlace = " at " + postClick.getPost().getCheckIn();
            } else {
                checkInPlace = "";
            }
            int tagCount = 0;
            if (postClick.getTagFriends() != null && postClick.getTagFriends().size() > 0) {

                tagCount = postClick.getTagFriends().size();
            }
            if (tagCount > 1) {
                for (int i = 0; i < tagCount; i++) {
                    tagFriends.add(postClick.getTagFriends().get(i));
                }
            }
            if (tagCount == 0) {
                postText = postClick.getUser().getName();
            } else if (tagCount == 1) {
                postText = postClick.getUser().getName() + "is with" + postClick.getTagFriends().get(0).getName() + checkInPlace;
            } else if (tagCount > 1) {
                int reducedCount = postClick.getTagFriends().size() - 1;
                reducedCountValue = String.valueOf(reducedCount).length();
                postText = postClick.getUser().getName() + " is with " + postClick.getTagFriends().get(0).getName() + " and "
                        + reducedCount + " more" + checkInPlace;
            }

            userNameLength = postClick.getUser().getName().length();

            if (postClick.getTagFriends() != null && postClick.getTagFriends().size() >= 1)
                firstTagLength = postClick.getTagFriends().get(0).getName().length();

            if (postClick.getPost().getCheckIn() != null)
                checkInLength = postClick.getPost().getCheckIn().length();


            setSpanableText(postClick, tagFriends, postText, userNameLength, firstTagLength, checkInLength, reducedCountValue);

        }
        if (postClick.getPost().getDescription() != null && postClick.getPost().getDescription().length() > 0 && postClick.getPost().getDescription().contains("#")) {
            binding.llPostItem.tvPostContent.setVisibility(View.VISIBLE);
            binding.llPostItem.tvPostContentSimple.setVisibility(View.GONE);
            binding.llPostItem.tvPostContent.setText(postClick.getPost().getDescription()).addLink(linkHashtag).build();
        } else {
            binding.llPostItem.tvPostContent.setVisibility(View.GONE);
            binding.llPostItem.tvPostContentSimple.setVisibility(View.VISIBLE);
            binding.llPostItem.tvPostContentSimple.setText(postClick.getPost().getDescription());
        }


        if (postClick.getCommentArr().size() > 0) {
            binding.llPostItem.tvComment.setText(postClick.getCommentArr().get(0).getUser().getName());
            binding.llPostItem.tvCommentDescription.setText(postClick.getCommentArr().get(0).getComment());
            Picasso.with(context).load(postClick.getCommentArr().get(0).getUser().getName()).into(binding.llPostItem.ivPostImage);
        } else {
//                ((ItemViewHolder) holder).layoutUserPostBinding.llCommentView.setVisibility(View.GONE);
        }
        if (postClick.getImages() != null && postClick.getImages().size() > 0) {
            ImageZoomHelper.setViewZoomable(binding.llPostItem.ivPostImage);
            binding.llPostItem.llFramePic.setVisibility(View.VISIBLE);
            binding.llPostItem.ivPostImage.setVisibility(View.VISIBLE);
//                ((ItemViewHolder) holder).layoutUserPostBinding.llFramePic.setVisibility(View.VISIBLE);
            binding.llPostItem.ivPostImage.setMinimumHeight(400);
            setUpViewpager();
        } else {
            binding.llPostItem.ivPostImage.setVisibility(View.GONE);
            binding.llPostItem.llFramePic.setVisibility(View.GONE);
//                ((ItemViewHolder) holder).layoutUserPostBinding.llFramePic.setVisibility(View.GONE);
            //Picasso.with(context).load(R.mipmap.post_img).into(((ItemViewHolder) holder).layoutUserPostBinding.ivPostImage);
        }
        if (postClick.getUser().getImage() != null) {
            Picasso.with(context).load(postClick.getUser().getImage()).placeholder(R.drawable.user_placeholder).
                    transform(new CircleTransformation()).into(binding.llPostItem.ivPostProfilePic);
        } else {
     //       Picasso.with(context).load(R.mipmap.userprofile_icon).into(binding.llPostItem.ivPostProfilePic);


            CommonUtils.otherUserProfile(context,binding.llPostItem.ivPostProfilePic,postClick.getUser().getImage(),binding.llPostItem.tvUserImage,
                    postClick.getUser().getName());
        }

        if (postClick.getCurrentUserLike()) {
            binding.llPostItem.ivHeart.setImageResource(R.mipmap.heart_icon_sel);
        } else {
            binding.llPostItem.ivHeart.setImageResource(R.mipmap.heart_icon);
        }


        if (postClick.getPinned()) {
            binding.llPostItem.ivPinned.setImageResource(R.drawable.unpin);
        } else {
            binding.llPostItem.ivPinned.setImageResource(R.drawable.pin);
        }
        binding.llPostItem.tvPostLike.setText(String.valueOf(postClick.getLikesCount()));
    }


    private void setUpViewpager() {
        CustomPageAdapter mCustomPagerAdapter = new CustomPageAdapter(context, postClick.getImages());
        binding.llPostItem.viewPager.setAdapter(mCustomPagerAdapter);
        if (postClick.getImages() != null && postClick.getImages().size() > 1) {
            binding.llPostItem.tabLayout.setVisibility(View.VISIBLE);
            binding.llPostItem.tabLayout.setupWithViewPager(binding.llPostItem.viewPager, true);
        } else {
            binding.llPostItem.tabLayout.setVisibility(View.GONE);
        }
    }

    *//*
 * method is used to set spanable string
 * *//*
    public void setSpanableText(final Post post, final List<TagFriend> tagFriends, String text, int userNameLength, int firstTagLength, int checkInLength, int reducedCountValue) {
        SpannableString spanString = new SpannableString(text);

        *//* User's Post profile view *//*
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                userIdSelected = post.getUser().getId();
                Intent intent = new Intent(context, MyProfileActivity.class);
                intent.putExtra(AppConstants.USER_ID, userIdSelected);
                context.startActivity(intent);
            }

            @Override

            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        };
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            spanString.setSpan(clickableSpan, 0, userNameLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, userNameLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, userNameLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            spanString.setSpan(clickableSpan, 0, userNameLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spanString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, userNameLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, userNameLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        }

        *//* First taged User's profile view *//*
        if (firstTagLength > 0) {
            ClickableSpan clickableSpan1 = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Intent intent = new Intent(context, MyProfileActivity.class);
                    intent.putExtra(AppConstants.USER_ID, post.getTagFriends().get(0).getUserId());
                    context.startActivity(intent);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setUnderlineText(false);
                }
            };


            if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
                spanString.setSpan(clickableSpan1, userNameLength + 9, userNameLength + firstTagLength + 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanString.setSpan(new ForegroundColorSpan(Color.WHITE), userNameLength + 9, userNameLength + firstTagLength + 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanString.setSpan(new StyleSpan(Typeface.BOLD), userNameLength + 9, userNameLength + firstTagLength + 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {

                spanString.setSpan(clickableSpan1, userNameLength + 9, userNameLength + firstTagLength + 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanString.setSpan(new ForegroundColorSpan(Color.BLACK), userNameLength + 9, userNameLength + firstTagLength + 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanString.setSpan(new StyleSpan(Typeface.BOLD), userNameLength + 9, userNameLength + firstTagLength + 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }


        }
        *//* View tag Users list *//*
        if (firstTagLength > 0 && reducedCountValue > 0) {
            ClickableSpan clickableSpan2 = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Intent intent = new Intent(context, TaggedUsersActivity.class);
                    intent.putExtra(AppConstants.POST_ID, postId);
                    intent.putExtra(AppConstants.SHARED_ID, sharedId);
                    context.startActivity(intent);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setUnderlineText(false);
                }
            };


            if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {

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

        *//*Location Click*//*
        if (firstTagLength > 0 && reducedCountValue > 0 && checkInLength > 0) {
            ClickableSpan clickableSpan3 = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    CommonUtils.showMap(context, post.getPost().getLatitude(), post.getPost().getLongitude(), post.getPost().getCheckIn());
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setUnderlineText(false);
                }
            };

            if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
                spanString.setSpan(clickableSpan3, userNameLength + firstTagLength + reducedCountValue + 23, userNameLength + firstTagLength + reducedCountValue + checkInLength + 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanString.setSpan(new ForegroundColorSpan(Color.WHITE), userNameLength + firstTagLength + reducedCountValue + 23, userNameLength + firstTagLength + reducedCountValue + checkInLength + 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanString.setSpan(new StyleSpan(Typeface.BOLD), userNameLength + firstTagLength + reducedCountValue + 23, userNameLength + firstTagLength + reducedCountValue + checkInLength + 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                spanString.setSpan(clickableSpan3, userNameLength + firstTagLength + reducedCountValue + 23, userNameLength + firstTagLength + reducedCountValue + checkInLength + 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanString.setSpan(new ForegroundColorSpan(Color.BLACK), userNameLength + firstTagLength + reducedCountValue + 23, userNameLength + firstTagLength + reducedCountValue + checkInLength + 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanString.setSpan(new StyleSpan(Typeface.BOLD), userNameLength + firstTagLength + reducedCountValue + 23, userNameLength + firstTagLength + reducedCountValue + checkInLength + 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            }
        }

        binding.llPostItem.tvPostUserName.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).getInstance(context).setDarkThemeText()));
        binding.llPostItem.llListRow.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        binding.llPostItem.tvPostLike.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.llPostItem.tvPostComment.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.llPostItem.tvPostShare.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.llPostItem.tvPostContent.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.llPostItem.tvPostContentSimple.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.llPostItem.tvComment.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.llPostItem.tvCommentDescription.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.llPostItem.tvPostUserName.setText(spanString);
        binding.llPostItem.tvPostUserName.setMovementMethod(LinkMovementMethod.getInstance());

    }

    private void setCurrentTheme() {
        binding.testLayout.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        binding.toolbarShowPost.toolbarMain.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).getInstance(context).setDarkTheme()));
        binding.toolbarShowPost.tvHeader.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).getInstance(context).setDarkThemeText()));
        Themes.getInstance(context).getInstance(context).changeIconColor(context, binding.toolbarShowPost.ivLeft);
        binding.toolbarShowPost.toolbarMain.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).getInstance(context).setDarkTheme()));
        binding.toolbarShowPost.tvRighttext.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).getInstance(context).setTolbarText()));
        binding.toolbarShowPost.tvHeader.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).getInstance(context).setDarkThemeText()));
    }


    private void openMenuPopup(final boolean isMyPost, View targetView) {

        BubbleLayout customLayout = (BubbleLayout) LayoutInflater.from(context).inflate(R.layout.popup_menu, null);
        final PopupWindow quickAction = BubblePopupHelper.create(context, customLayout);
        LinearLayout otherProfile = (LinearLayout) customLayout.findViewById(R.id.llOtherProfile);
        LinearLayout llMyProfile = (LinearLayout) customLayout.findViewById(R.id.llMyProfile);
        if (isMyPost) {
            llMyProfile.setVisibility(View.VISIBLE);
            otherProfile.setVisibility(View.GONE);
        } else {
            llMyProfile.setVisibility(View.GONE);
            otherProfile.setVisibility(View.VISIBLE);
        }
        llMyProfile.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).getInstance(context).setPopupBackground()));
        otherProfile.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).getInstance(context).setPopupBackground()));

        TextView tvSendToChat = (TextView) customLayout.findViewById(R.id.tvSendToChat);
        TextView tvSendToChat1 = (TextView) customLayout.findViewById(R.id.tvSendToChat1);
        TextView tvShareFb1 = (TextView) customLayout.findViewById(R.id.tvShareFb1);
        TextView tvShareFb = (TextView) customLayout.findViewById(R.id.tvShareFb);
        TextView tvDeletePost = (TextView) customLayout.findViewById(R.id.tvDeletePost);
        TextView tvEditPost = (TextView) customLayout.findViewById(R.id.tvEditPost);
        TextView tvReportUser = (TextView) customLayout.findViewById(R.id.tvReportUser);
        ImageView ivHide = (ImageView) customLayout.findViewById(R.id.ivHide);
        ImageView ivHide1 = (ImageView) customLayout.findViewById(R.id.ivHide1);
        ImageView ivRemove = (ImageView) customLayout.findViewById(R.id.ivRemove);
        TextView tvHide = (TextView) customLayout.findViewById(R.id.tvHide);
        TextView tvHide1 = (TextView) customLayout.findViewById(R.id.tvHide1);
        TextView tvRemove = (TextView) customLayout.findViewById(R.id.tvRemove);
        ImageView ivEdit = (ImageView) customLayout.findViewById(R.id.ivEdit);
        ImageView ivShare = (ImageView) customLayout.findViewById(R.id.ivShare);
        ImageView ivSend2 = (ImageView) customLayout.findViewById(R.id.ivSend2);
        ImageView ivShare1 = (ImageView) customLayout.findViewById(R.id.ivShare1);
        ImageView ivSend = (ImageView) customLayout.findViewById(R.id.ivSend);


        tvShareFb.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        tvSendToChat.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        tvEditPost.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        tvShareFb1.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        tvSendToChat1.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        tvHide.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        tvHide1.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        tvRemove.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));

        Themes.getInstance(context).changeIconColorToWhite(context, ivEdit);
        Themes.getInstance(context).changeIconColorToWhite(context, ivShare);
        Themes.getInstance(context).changeIconColorToWhite(context, ivSend2);
        Themes.getInstance(context).changeIconColorToWhite(context, ivShare1);
        Themes.getInstance(context).changeIconColorToWhite(context, ivSend);
        Themes.getInstance(context).changeShareIcon(context, ivHide);
        Themes.getInstance(context).changeShareIcon(context, ivHide1);
        Themes.getInstance(context).changeShareIcon(context, ivRemove);

        tvDeletePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePostPopup(postId, sharedId);
                quickAction.dismiss();
            }
        });
        tvShareFb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebookSharing();
                quickAction.dismiss();
            }
        });
        tvShareFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebookSharing();
                quickAction.dismiss();
            }
        });
        tvSendToChat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CommonUtils.isOnline(context)) {
                    callGetRoomAPI(postClick);
                } else {
                    CommonUtils.showToast(context, getString(R.string.internet_alert_msg));
                }
                quickAction.dismiss();
            }
        });
        tvSendToChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CommonUtils.isOnline(context)) {
                    callGetRoomAPI(postClick);
                } else {
                    CommonUtils.showToast(context, getString(R.string.internet_alert_msg));
                }
                quickAction.dismiss();
            }
        });
        tvReportUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addReportDialog();
                quickAction.dismiss();
            }
        });
        tvEditPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quickAction.dismiss();

                startActivity(new Intent(context, SocialPostActivity.class)
                        .putExtra(AppConstants.FROM, AppConstants.FROM_EDIT_POST)
                        .putExtra(AppConstants.POSITION, 0)
                        .putExtra(AppConstants.POST_MODEL, (Serializable) postDetailList.get(0)));
            }
        });
        quickAction.showAsDropDown(targetView);
    }

    *//**
 * LikeCommentApi
 *//*
    private void callLikeCommentApi(String comment_id, String like_dislike_status, boolean status) {
        if (CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("comment_id", comment_id);
            jsonObject.addProperty("status", status);
            jsonObject.addProperty("do", like_dislike_status);

            Call<CommentLikeDislike> call = ApiExecutor.getClient(context).commentLikeDislike(jsonObject);
            call.enqueue(new Callback<CommentLikeDislike>() {
                @Override
                public void onResponse(@NonNull Call<CommentLikeDislike> call, @NonNull final Response<CommentLikeDislike> response) {
                    progressDialog.dismiss();
                    CommonUtils.errorLog("response", "" + response.message());
                    try {
                        if (response.body() != null && response.body().getResponseCode() == 200) {
                        *//*    likeDislikeList.clear();
                            likeDislikeList.addAll(response.body().getLike());
                            commentsAdapter.notifyDataSetChanged();*//*
                            callAllCommentUserAPI();

                        } else {
                            ToastUtils.showToastShort(context, context.getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CommentLikeDislike> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    LogUtils.errorLog(TAG, "on failure -> " + ((t != null && t.getMessage() != null) ? t.getMessage() : "---"));
                    ToastUtils.showToastShort(context, context.getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(context, context.getString(R.string.internet_alert_msg));
        }
    }

    *//**
 * method for create story api
 *//*
    private void callGetRoomAPI(final Post post) {
        if (CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();
            JsonObject jsonObject = new JsonObject();

            Call<GetRoomResonse> call = ApiExecutor.getClient(context).apiGetRoom(jsonObject);
            call.enqueue(new Callback<GetRoomResonse>() {
                @Override
                public void onResponse(@NonNull Call<GetRoomResonse> call, @NonNull final Response<GetRoomResonse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            roomList.clear();
                            if (response.body().getRooms() != null && response.body().getRooms().size() > 0) {
                                roomList.addAll(response.body().getRooms());
                            }
                            tagFollowingAdapter.notifyDataSetChanged();
                            setPostShareDialogeRecyclerView(post);
                        } else {
                            ToastUtils.showToastShort(context, context.getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GetRoomResonse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(context, context.getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(context, context.getString(R.string.internet_alert_msg));
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
                    Intent intent = new Intent(context, ShowImageActivity.class);
                    intent.putExtra("url", dataObjectList.get(position).getFile().getUrl());
                    context.startActivity(intent);
                }
            });
            if (dataObjectList.get(position).getFileType().length() > 0) {
                switch (dataObjectList.get(position).getFileType()) {
                    case "video":
                        displayImage.setVisibility(View.GONE);
                        jCVideoPlayer.setVisibility(View.VISIBLE);
                        jCVideoPlayer.setMinimumHeight(400);
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
                        displayImage.setVisibility(View.VISIBLE);
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
*/