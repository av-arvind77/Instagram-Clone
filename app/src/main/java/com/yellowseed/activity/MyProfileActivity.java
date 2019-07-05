package com.yellowseed.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.daasuu.bl.BubbleLayout;
import com.daasuu.bl.BubblePopupHelper;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yellowseed.R;
import com.yellowseed.adapter.MyProfileAdapter;
import com.yellowseed.adapter.TagFollowingAdapter;
import com.yellowseed.customView.QuickAction;
import com.yellowseed.database.RoomsTable;
import com.yellowseed.databinding.ActivityMyProfileBinding;
import com.yellowseed.databinding.DialogShareBinding;
import com.yellowseed.databinding.LayoutRepostBinding;
import com.yellowseed.databinding.SpamdialogBinding;
import com.yellowseed.fragments.DirectShareFragment;
import com.yellowseed.listener.ApiCallback;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.reqModel.UserModel;
import com.yellowseed.model.resModel.GetChatResonse;
import com.yellowseed.model.resModel.GetRoomResonse;
import com.yellowseed.model.resModel.ReportPostResponse;
import com.yellowseed.utils.ActivityController;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.PrefManager;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;
import com.yellowseed.webservices.ApiResponse;
import com.yellowseed.webservices.CommonApi;
import com.yellowseed.webservices.ServiceConstant;
import com.yellowseed.webservices.requests.PostChatBody;
import com.yellowseed.webservices.requests.posts.PostRequest;
import com.yellowseed.webservices.response.homepost.Post;
import com.yellowseed.webservices.response.homepost.Post_;
import com.yellowseed.webservices.response.post.PostResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.gusavila92.websocketclient.WebSocketClient;


public class MyProfileActivity extends BaseActivity implements PopupMenu.OnMenuItemClickListener {
    int page = 1;
    private ActivityMyProfileBinding binding;
    private Context mContext;
    private UserModel userModel=new UserModel();
    private MyProfileAdapter adapter;
    private ArrayList<com.yellowseed.webservices.response.homepost.Post> models = new ArrayList<>();
    private boolean isLastPage;
    private boolean isLoading;
    private Integer totalRecord;
    private String userId = "";
    private String report;
    private TagFollowingAdapter tagFollowingAdapter;
    private ArrayList<GetRoomResonse.RoomsBean> roomList = new ArrayList<>();
    private WebSocketClient webSocketClient;
    private GetChatResonse.UserInfoBean chatScreenModel;
    private boolean statusPost = true, statusStory = true;
    private Menu menu;
    private Boolean darkThemeEnabled;
    private PopupWindow quickAction;
    private Dialog dialog;
    private String from = "";
    private boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_profile);
        mContext = MyProfileActivity.this;
        models.clear();
        getBundledata();
        models.addAll(getPostData());
        userModel.setId("jfbgxbgfbgf");
        userModel.setAddress("Mobiloitte");

        userModel.setName("John Thomas");
        postData(userModel);
        initializedControl();
        darkThemeEnabled = CommonUtils.getPreferencesBoolean(mContext, AppConstants.DARK_THEME);
        setToolBar();
        setOnClickListener();

    }

    private void getBundledata() {

        if (getIntent() != null && getIntent().getStringExtra(AppConstants.FROM) != null) {
            from = getIntent().getStringExtra(AppConstants.FROM);


        }


    }

    private void createWebSocketClient() {
        URI uri;
        try {
            uri = new URI(ServiceConstant.WEB_SOCKET_URL + "?id=" + new PrefManager(mContext).getUserId());
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
        object.addProperty("sender_id", new PrefManager(mContext).getUserId());
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

    private void postData(UserModel user) {
        userModel = user;
        userId = user.getId();
        if (user.getId() != null && user.getId().equalsIgnoreCase(new PrefManager(mContext).getUserId())) {
            PrefManager.getInstance(MyProfileActivity.this).saveUser(user);
            binding.toolbarMyprofile.ivRight.setVisibility(View.GONE);
            binding.toolbarMyprofile.tvHeader.setText(R.string.my_profile);
        } else {
            binding.toolbarMyprofile.ivRight.setVisibility(View.VISIBLE);
            binding.toolbarMyprofile.ivRight.setImageResource(R.mipmap.three_dot);
            binding.toolbarMyprofile.tvHeader.setText(userModel.getName());
        }
        setHeaderRecylerView();

    }

    @Override
    public void initializedControl() {
        setCurrentTheme();
        if (getIntent() != null && getIntent().hasExtra(AppConstants.USER_ID)) {
            userId = getIntent().getStringExtra(AppConstants.USER_ID);
        }
    }

    private void setCurrentTheme() {
        binding.toolbarMyprofile.toolbarMain.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).getInstance(mContext).setDarkTheme()));
        binding.toolbarMyprofile.tvHeader.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).getInstance(mContext).setDarkThemeText()));
        Themes.getInstance(mContext).getInstance(mContext).changeIconColor(mContext, binding.toolbarMyprofile.ivLeft);
        Themes.getInstance(mContext).getInstance(mContext).changeIconColor(mContext, binding.toolbarMyprofile.ivRight);
        binding.llMyProfile.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).getInstance(mContext).setDarkTheme()));
        binding.toolbarMyprofile.toolbarMain.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).getInstance(mContext).setDarkTheme()));
        binding.toolbarMyprofile.tvRighttext.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).getInstance(mContext).setTolbarText()));
        binding.toolbarMyprofile.tvHeader.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).getInstance(mContext).setDarkThemeText()));

    }

    @Override
    public void setToolBar() {
        binding.toolbarMyprofile.ivLeft.setVisibility(View.VISIBLE);
        binding.toolbarMyprofile.tvHeader.setVisibility(View.VISIBLE);
//        binding.toolbarMyprofile.tvHeader.setText(R.string.my_profile);
    }


    @Override
    public void setOnClickListener() {
        binding.llMyProfile.setOnClickListener(this);
        binding.toolbarMyprofile.ivRight.setOnClickListener(this);
        binding.toolbarMyprofile.ivLeft.setOnClickListener(this);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivLeft:
                finish();
                break;
            case R.id.ivRight:
                if (isOpen) {
                    if (quickAction != null) {
                        isOpen = false;
                        quickAction.dismiss();
                    }

                } else {
                    isOpen = true;
                    showMenu(v);
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        UserModel userModel = new UserModel();
        userModel.setAddress("Mobiloitte");
        userModel.setId("jfbgxbgfbgf");
        userModel.setName("John Thomas");
        //postData(userModel);
        /*if (userId != null && userId.length() > 0) {
            CommonApi.callGetOtherUserProfileAPI(mContext, userId, new ApiCallback() {
                @Override
                public void onSuccess(ApiResponse apiResponse) {
                    if (apiResponse.getUser().getRoom_info() != null) {
                        RoomsTable roomsTable = new RoomsTable(mContext);
                        roomsTable.insertSingleRoom(apiResponse.getUser().getRoom_info(), apiResponse.getUser().getName());
                        roomsTable.closeDB();
                    }
                    postData(apiResponse.getUser());
                }

                @Override
                public void onFailure(ApiResponse apiResponse) {

                }
            });
        } else {
            CommonApi.callGetProfileAPI(mContext, true, new ApiCallback() {
                @Override
                public void onSuccess(ApiResponse apiResponse) {
                    postData(apiResponse.getUser());
                }

                @Override
                public void onFailure(ApiResponse apiResponse) {
                }
            });
        }*/
    }

    private void setHeaderRecylerView() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        binding.rvPost.setLayoutManager(linearLayoutManager);
        adapter = new MyProfileAdapter(models, AppConstants.LIST, userModel, mContext, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.seeMUtualMoreFollwers:
                        ToastUtils.showToastShort(mContext, getString(R.string.work_in_progress));
                        break;
                    case R.id.tvMessageProfile:
                        ActivityController.startActivity(mContext, ChatScreenActivity.class);
                        break;
                    case R.id.ivSettingProfile:
                        initiatePopupWindow(view);
                        break;
                    case R.id.llFollowers:
                        ActivityController.startActivity(mContext, FollowerListActivity.class);
                break;
                case R.id.llFollowing:
                        ActivityController.startActivity(mContext, FollowerListActivity.class);
                break;
                    case R.id.ivPostMenuItem:
                        openMenuPopup(models.get(position), view, position);
                        break;
                    case R.id.llShare:
                        if (models.get(position).getPost().getId() != null)
                            setPostShareDialoge(models.get(position));
                        CommonUtils.errorLog(TAG, models.get(position).getPost().getId());

                        break;

                    case R.id.ivPostDownloads:
                        ToastUtils.showToastShort(mContext,"Post saved successfully.");
                        /*if (models.get(position).getSaved()) {
                            callSavePostListApi(models.get(position).getPost(), models.get(position).getSharedId(), false);
                        } else {
                            callSavePostListApi(models.get(position).getPost(), models.get(position).getSharedId(), true);
                        }*/
                        break;
                    default:
                        break;
                }
            }
        }, from);
        binding.rvPost.setAdapter(adapter);

/*
        binding.rvPost.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastvisibleitemposition = linearLayoutManager.findLastVisibleItemPosition();
                if (models != null && models.size() > 0) {
                    if (adapter != null && adapter.getItemCount() - 1 == totalRecord) {
                        isLastPage = true;
                    }
                    if (lastvisibleitemposition == adapter.getItemCount() - 1) {

                        if (!isLoading && !isLastPage) {
                            isLoading = true;
                            callPostListAPI(++page, true);
                        }
                    }

                }
            }
        });*/
        binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.swipeContainer.setRefreshing(false);
                /*page = 1;
                isLoading = true;
                callPostListAPI(page, true);*/
            }
        });

    }

    /**
     * Save Post List Api
     */
    private void callSavePostListApi(final Post_ post, final String sharedId, final boolean isSaved) {
        final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
        progressDialog.show();

        JsonObject jsonObject = new JsonObject();

        if (sharedId != null && !sharedId.equalsIgnoreCase("")) {
            jsonObject.addProperty("shared_id", sharedId);
        } else {
            jsonObject.addProperty("post_id", post.getId());
        }

        jsonObject.addProperty("status", isSaved);
        Call<ApiResponse> call = ApiExecutor.getClient(mContext).apiSavePost(jsonObject);
        call.enqueue(new retrofit2.Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (response.body() != null && response.body().getResponseCode() == 200) {

                    if (response.body().getResponseMessage() != null && !response.body().getResponseMessage().equalsIgnoreCase("")) {
                        CommonUtils.showToast(mContext, response.body().getResponseMessage());
                        if (isSaved) {
                            post.setSaved(true);
                        } else {
                            post.setSaved(false);
                        }
                    }
                    adapter.notifyDataSetChanged();
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


    private void setPostShareDialoge(final Post post) {


        DialogShareBinding dialogShareBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dialog_share, null, false);
        dialog = DialogUtils.createDialog(mContext, dialogShareBinding.getRoot());
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialogShareBinding.llMain.setBackground(ContextCompat.getDrawable(mContext, Themes.getInstance(mContext).setDarkStoryBackground()));
        dialogShareBinding.tvRepost.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        dialogShareBinding.tvDirectShare.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        dialogShareBinding.tvRepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RePostActivity.class);
                intent.putExtra(AppConstants.POST_MODEL, post);
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

                DirectShareFragment directShareFragment =
                        new DirectShareFragment();
                directShareFragment.show(getSupportFragmentManager(),
                        "directShare");


               // setPostShareDialogeRecyclerView(post);

                /*if (CommonUtils.isOnline(mContext)) {
                     callGetRoomAPI(post);
                } else {
                    CommonUtils.showToast(mContext, getString(R.string.internet_alert_msg));
                }*/
                dialog.dismiss();

            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.background_dark);


    }








    private void setPostShareDialogeRecyclerView(final Post post) {


        final LayoutRepostBinding repostBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.layout_repost, null, false);

        final Dialog dialog = DialogUtils.createDialog(mContext, repostBinding.getRoot());
        repostBinding.llRepost.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeDialog()));
        repostBinding.etRepostSearch.setBackground(ContextCompat.getDrawable(mContext, Themes.getInstance(mContext).setDarkSearchDrawable()));
        repostBinding.viewLine.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setViewLineGrey()));
        repostBinding.etRepostSearch.setHintTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkHintColor()));
        repostBinding.etRepostSearch.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        repostBinding.etCaption.setBackground(ContextCompat.getDrawable(mContext, Themes.getInstance(mContext).setDarkSearchDrawable()));
        repostBinding.etCaption.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
       /* Themes.getInstance(mContext).changeCrossIconColor(mContext, repostBinding.ivCancelRepost);

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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        repostBinding.rvRepostHome.setLayoutManager(linearLayoutManager);
        tagFollowingAdapter = new TagFollowingAdapter(mContext, roomList, new OnItemClickListener() {
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
                ToastUtils.showToastShort(mContext,"Post has been shared.");

                dialog.dismiss();
            /*    if (tagFollowingAdapter.getSelected().size() > 0) {
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
                    postChatBody.setPost_id(post.getPost().getId());
                    postChatBody.setUser_name(post.getUser().getName());
                    postChatBody.setUser_image(post.getUser().getImage());
                    postChatBody.setDescription(post.getPost().getDescription());
                    if (post.getImages() != null && post.getImages().size() > 0) {
                        postChatBody.setImage(post.getImages().get(0).getFile().getUrl());
                    }
                    chatScreenModel.setBody(new Gson().toJson(postChatBody));
                    chatScreenModel.setBody(new Gson().toJson(postChatBody));
                    chatScreenModel.setSender_id(new PrefManager(mContext).getUserId());
                    chatScreenModel.setUpload_type("post");
                    sendMessage(chatScreenModel, tagFollowingAdapter.getSelected());
                    repostBinding.etCaption.setText("");
                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                }*/

            }
        });
    }

    /**
     * PostShareDialoge
     */

/*
    private void setPostShareDialoge(final Post post) {




        DialogShareBinding dialogShareBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dialog_share, null, false);
        dialog = DialogUtils.createDialog(mContext, dialogShareBinding.getRoot());
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialogShareBinding.llMain.setBackground(ContextCompat.getDrawable(mContext, Themes.getInstance(mContext).setDarkStoryBackground()));
        dialogShareBinding.tvRepost.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        dialogShareBinding.tvDirectShare.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        dialogShareBinding.tvRepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RePostActivity.class);
                intent.putExtra(AppConstants.POST_MODEL, post);
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
                setPostShareDialogeRecyclerView(post);

                *//*if (CommonUtils.isOnline(mContext)) {
                     callGetRoomAPI(post);
                } else {
                    CommonUtils.showToast(mContext, getString(R.string.internet_alert_msg));
                }*//*
                dialog.dismiss();

            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.background_dark);


    }








    private void setPostShareDialogeRecyclerView(final Post post) {


        final LayoutRepostBinding repostBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.layout_repost, null, false);

        final Dialog dialog = DialogUtils.createDialog(mContext, repostBinding.getRoot());
        repostBinding.llRepost.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeDialog()));
        repostBinding.etRepostSearch.setBackground(ContextCompat.getDrawable(mContext, Themes.getInstance(mContext).setDarkSearchDrawable()));
        repostBinding.viewLine.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setViewLineGrey()));
        repostBinding.etRepostSearch.setHintTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkHintColor()));
        repostBinding.etRepostSearch.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        repostBinding.etCaption.setBackground(ContextCompat.getDrawable(mContext, Themes.getInstance(mContext).setDarkSearchDrawable()));
        repostBinding.etCaption.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        Themes.getInstance(mContext).changeCrossIconColor(mContext, repostBinding.ivCancelRepost);

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

        repostBinding.rvRepostHome.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        repostBinding.rvRepostHome.setLayoutManager(linearLayoutManager);
        tagFollowingAdapter = new TagFollowingAdapter(mContext, roomList, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
              *//*  if (roomList.get(position).isIs_selected()) {
                    roomList.get(position).setIs_selected(false);
                } else {
                    roomList.get(position).setIs_selected(true);
                }*//*
                tagFollowingAdapter.notifyItemChanged(position);
            }
        });
        repostBinding.rvRepostHome.setAdapter(tagFollowingAdapter);

        repostBinding.ivSendRepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToastShort(mContext,"Post has been shared.");

                dialog.dismiss();


            }
        });
    }*/












    private void setGirdRecylerView() {
        final GridLayoutManager linearLayoutManager = new GridLayoutManager(mContext, 3);
        binding.rvPost.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (adapter.getItemViewType(position)) {
                    case 0:
                        return linearLayoutManager.getSpanCount();
                    default:
                        return 1;

                }
            }
        });
        adapter = new MyProfileAdapter(models, AppConstants.GRID, userModel, mContext, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.seeMUtualMoreFollwers:
                        // ToastUtils.showToastShort(mContext, getString(R.string.work_in_progress));
                        break;
                    case R.id.tvMessageProfile:
                        mContext.startActivity(new Intent(mContext, ChatsScreenFrgActivity.class).putExtra(AppConstants.CHAT_USER_ID, userModel.getId()).
                                putExtra(AppConstants.FROM, AppConstants.PROFILE).putExtra(AppConstants.NAME, userModel.getName())
                                .putExtra(AppConstants.QB_ID, userModel.getQb_id()).putExtra(AppConstants.SENDER_ID, PrefManager.getInstance(mContext).getUserId()).
                                        putExtra(AppConstants.USER_IMAGE, userModel.getImage()));

                        break;
                    case R.id.ivSettingProfile:
                        initiatePopupWindow(view);
                        break;
                    case R.id.ivPostMenuItem:
                        openMenuPopup(models.get(position), view, position);
                        break;
                    default:
                        break;
                }
            }
        }, from);
        binding.rvPost.setAdapter(adapter);
        /*binding.rvPost.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastvisibleitemposition = linearLayoutManager.findLastVisibleItemPosition();
                if (models != null && models.size() > 0) {
                    if (adapter != null && adapter.getItemCount() - 1 == totalRecord) {
                        isLastPage = true;
                    }
                    if (lastvisibleitemposition == adapter.getItemCount() - 1) {

                        if (!isLoading && !isLastPage) {
                            isLoading = true;
                            callPostListAPI(++page, true);
                        }
                    }
                }
            }
        });*/
        binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.swipeContainer.setRefreshing(false);
                /*page = 1;
                isLoading = true;
                callPostListAPI(page, true);
         */
            }
        });
    }

    private void callPostListAPI(final int page, final boolean isProgress) {
        if (CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            if (isProgress) {
                progressDialog.show();
            }
            PostRequest postRequest = new PostRequest();
            postRequest.setPage(page);
            postRequest.setId(userId);
            Call<PostResponse> call = ApiExecutor.getClient(mContext).apiUserPostList(postRequest);
            call.enqueue(new retrofit2.Callback<PostResponse>() {
                @Override
                public void onResponse(@NonNull Call<PostResponse> call, @NonNull Response<PostResponse> response) {
                    binding.swipeContainer.setRefreshing(false);
                    if (isProgress) {
                        progressDialog.dismiss();
                    }
                    try {
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (response != null && response.body() != null) {
                        if (response.body().getResponseCode() == AppConstants.SUCCESS) {
                            if (page == 1) {
                                models.clear();
                                binding.swipeContainer.setRefreshing(false);
                            }
                            models.addAll(response.body().getPost());
                            totalRecord = response.body().getPagination().getTotalRecords();
                            adapter.notifyDataSetChanged();
                        } else if (response.body().getResponseCode() == AppConstants.USER_BLOCKED) {
                            ToastUtils.showToastShort(mContext, response.body().getResponseMessage());
                        } else {
                            ToastUtils.showToastShort(mContext, response.body().getResponseMessage());
                        }
                    } else {
                        ToastUtils.showToastShort(mContext, getString(R.string.server_error_msg));
                    }
                }


                @Override
                public void onFailure(@NonNull Call<PostResponse> call, @NonNull Throwable t) {
                    if (page == 1) {
                        binding.swipeContainer.setRefreshing(false);
                    }
                    if (isProgress) {
                        progressDialog.dismiss();
                    }
                    t.printStackTrace();
                    ToastUtils.showToastShort(mContext, getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
        }
    }

    private void callTaggedPostListAPI(final int page) {
        if (CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();
            PostRequest postRequest = new PostRequest();
            postRequest.setPage(page);
            postRequest.setId(userId);
            Call<PostResponse> call = ApiExecutor.getClient(mContext).apiTaggedPostList(postRequest);
            call.enqueue(new retrofit2.Callback<PostResponse>() {
                @Override
                public void onResponse(@NonNull Call<PostResponse> call, @NonNull Response<PostResponse> response) {
                    binding.swipeContainer.setRefreshing(false);
                    progressDialog.dismiss();
                    try {
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (response != null && response.body() != null) {
                        if (response.body().getResponseCode() == AppConstants.SUCCESS) {
                            if (page == 1) {
                                models.clear();
                                binding.swipeContainer.setRefreshing(false);
                            }
                            models.addAll(response.body().getPost());
                            totalRecord = response.body().getPagination().getTotalRecords();
                            adapter.notifyDataSetChanged();
                        } else if (response.body().getResponseCode() == AppConstants.USER_BLOCKED) {
                            ToastUtils.showToastShort(mContext, response.body().getResponseMessage());
                        } else {
                            ToastUtils.showToastShort(mContext, response.body().getResponseMessage());
                        }
                    } else {
                        ToastUtils.showToastShort(mContext, getString(R.string.server_error_msg));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<PostResponse> call, @NonNull Throwable t) {
                    if (page == 1) {
                        binding.swipeContainer.setRefreshing(false);
                    }
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(mContext, getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
        }
    }

    private void callSavedPostListAPI(final int page) {
        if (CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();
            PostRequest postRequest = new PostRequest();
            postRequest.setPage(page);
            Call<PostResponse> call = ApiExecutor.getClient(mContext).apiSavedPostList(postRequest);
            call.enqueue(new retrofit2.Callback<PostResponse>() {
                @Override
                public void onResponse(@NonNull Call<PostResponse> call, @NonNull Response<PostResponse> response) {
                    binding.swipeContainer.setRefreshing(false);
                    progressDialog.dismiss();
                    try {
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (response != null && response.body() != null) {
                        if (response.body().getResponseCode() == AppConstants.SUCCESS) {
                            if (page == 1) {
                                models.clear();
                                binding.swipeContainer.setRefreshing(false);
                            }
                            models.addAll(response.body().getPost());
                            totalRecord = response.body().getPagination().getTotalRecords();
                            adapter.notifyDataSetChanged();
                        } else if (response.body().getResponseCode() == AppConstants.USER_BLOCKED) {
                            ToastUtils.showToastShort(mContext, response.body().getResponseMessage());
                        } else {
                            ToastUtils.showToastShort(mContext, response.body().getResponseMessage());
                        }
                    } else {
                        ToastUtils.showToastShort(mContext, getString(R.string.server_error_msg));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<PostResponse> call, @NonNull Throwable t) {
                    if (page == 1) {
                        binding.swipeContainer.setRefreshing(false);
                    }
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(mContext, getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
        }
    }

    private void callPinnedPost() {
        if (CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();
            PostRequest postRequest = new PostRequest();
            Call<PostResponse> call = ApiExecutor.getClient(mContext).apiPinnedList(postRequest);
            call.enqueue(new retrofit2.Callback<PostResponse>() {
                @Override
                public void onResponse(@NonNull Call<PostResponse> call, @NonNull Response<PostResponse> response) {
                    binding.swipeContainer.setRefreshing(false);
                    progressDialog.dismiss();
                    try {
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (response != null && response.body() != null) {
                        if (response.body().getResponseCode() == AppConstants.SUCCESS) {
                            models.clear();
                            binding.swipeContainer.setRefreshing(false);
                            models.addAll(response.body().getPost());
                            adapter.notifyDataSetChanged();
                        } else if (response.body().getResponseCode() == AppConstants.USER_BLOCKED) {
                            ToastUtils.showToastShort(mContext, response.body().getResponseMessage());
                        } else {
                            ToastUtils.showToastShort(mContext, response.body().getResponseMessage());
                        }
                    } else {
                        ToastUtils.showToastShort(mContext, getString(R.string.server_error_msg));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<PostResponse> call, @NonNull Throwable t) {
                    if (page == 1) {
                        binding.swipeContainer.setRefreshing(false);
                    }
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(mContext, getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
        }
    }


  /*  private void showMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        try {
            Field[] fields = popup.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popup);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper
                            .getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod(
                            "setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        popup.setOnMenuItemClickListener((PopupMenu.OnMenuItemClickListener) this);
        popup.inflate(R.menu.menu_item_another_user);

        popup.show();

    }*/

    private void showMenu(View targetView) {


        BubbleLayout customLayout = (BubbleLayout) LayoutInflater.from(mContext).inflate(R.layout.profile_pop_up, null);
        customLayout.setBubbleColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setPopupBackground()));

        quickAction = BubblePopupHelper.create(mContext, customLayout);
        LinearLayout llProfile = (LinearLayout) customLayout.findViewById(R.id.llProfile);
        View view = customLayout.findViewById(R.id.viewLine);
        llProfile.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setPopupBackground()));
        view.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setViewLineGrey()));


        TextView tvShare = (TextView) customLayout.findViewById(R.id.tvShareProfile);
        TextView tvTurnOn = (TextView) customLayout.findViewById(R.id.tvTurnOnNotifcation);
        TextView tvShareNotification = (TextView) customLayout.findViewById(R.id.tvStoryNotification);
        TextView tvBlock = (TextView) customLayout.findViewById(R.id.tvBlock);
        TextView tvReport = (TextView) customLayout.findViewById(R.id.tvReport);


        ImageView ivShare = customLayout.findViewById(R.id.ivShare);
        ImageView ivNotication = customLayout.findViewById(R.id.ivNotifiaction);
        ImageView ivStory = customLayout.findViewById(R.id.ivStory);

        tvShare.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        tvTurnOn.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        tvShareNotification.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        Themes.getInstance(mContext).changeIconColorToWhite(mContext, ivShare);
        Themes.getInstance(mContext).changeIconColorToWhite(mContext, ivNotication);
        Themes.getInstance(mContext).changeIconColorToWhite(mContext, ivStory);


        tvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quickAction.dismiss();
                ToastUtils.showToastShort(mContext, "Share Profile");
            }
        });
        tvTurnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quickAction.dismiss();
                ToastUtils.showToastShort(mContext, "Turn on Post Notification");
            }
        });
        tvShareNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quickAction.dismiss();
                ToastUtils.showToastShort(mContext, "Turn on Story Notification");
            }
        });
        tvBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToastShort(mContext, "Work on progress");
                quickAction.dismiss();
             /*   CommonApi.callBlockUserAPI(mContext, "", new ApiCallback() {
                    @Override
                    public void onSuccess(ApiResponse apiResponse) {
                        ToastUtils.showToastShort(mContext, apiResponse.getResponseMessage());
                    }

                    @Override
                    public void onFailure(ApiResponse apiResponse) {

                    }
                });*/

            }
        });
        tvReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportUserDialog();
                quickAction.dismiss();

            }
        });

        quickAction.showAsDropDown(targetView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item_another_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_ShareProfile:
                ToastUtils.showToastShort(mContext, "Share Profile");
                return true;
            case R.id.action_TurnOnPostNotification:
                CommonApi.turnOnPushPost(mContext, userModel.getId(), statusPost, new ApiCallback() {
                    @Override
                    public void onSuccess(ApiResponse apiResponse) {
                        ToastUtils.showToastShort(mContext, apiResponse.getResponseMessage());
                        statusPost = false;
                        // updateMenuTitles();
                    }

                    @Override
                    public void onFailure(ApiResponse apiResponse) {
                        ToastUtils.showToastShort(mContext, apiResponse.getResponseMessage());
                    }
                });

                return true;
            case R.id.action_TurnONStoryNotification:


                CommonApi.turnOnPushStory(mContext, userModel.getId(), statusStory, new ApiCallback() {
                    @Override
                    public void onSuccess(ApiResponse apiResponse) {
                        //ToastUtils.showToastShort(mContext, "Turn on Story Notification");
                        ToastUtils.showToastShort(mContext, apiResponse.getResponseMessage());
                        statusStory = false;
                    }

                    @Override
                    public void onFailure(ApiResponse apiResponse) {
                        ToastUtils.showToastShort(mContext, apiResponse.getResponseMessage());
                    }
                });


                return true;
            case R.id.action_BlockAnotherUser:
                CommonApi.callBlockUserAPI(mContext, userModel.getId(), new ApiCallback() {
                    @Override
                    public void onSuccess(ApiResponse apiResponse) {
                        ToastUtils.showToastShort(mContext, apiResponse.getResponseMessage());
                    }

                    @Override
                    public void onFailure(ApiResponse apiResponse) {
                        ToastUtils.showToastShort(mContext, apiResponse.getResponseMessage());
                    }
                });
                return true;
            case R.id.action_ReportAnotherUSer:
                reportUserDialog();
                return true;
            default:
                return false;

        }
    }

    /*private void updateMenuTitles() {
        MenuItem bedMenuItem = menu.findItem(R.id.action_TurnOnPostNotification);
        if (statusPost) {
            bedMenuItem.setTitle("hai");
        } else {
            bedMenuItem.setTitle("Bye");
        }
    }*/
    private void reportUserDialog() {
        final CharSequence[] items = {Html.fromHtml("<font color = '#FC2B2B'>Its Spam</font>"), Html.fromHtml("<font color = '#FC2B2B'>Report this Profile</font>"), Html.fromHtml("<font color = '#FC2B2B'>Inappropriate</font>")};

        new MaterialDialog.Builder(mContext)
                .title(R.string.understandthereason).titleGravity(GravityEnum.CENTER)
                .items(items)
                .itemsColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()))
                .backgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeStory()))

                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {

                      /*  if (position == 0) {
                            // reportUSerScreem();
                            if (CommonUtils.isOnline(mContext)) {
                                apiReportUser("Its Spam");
                            } else {
                                ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
                            }
                            dialog.dismiss();
                        } else if (position == 1) {
                            // reportUSerScreem();
                            if (CommonUtils.isOnline(mContext)) {
                                apiReportUser("Report This Profile");
                            } else {
                                ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
                            }
                            dialog.dismiss();
                        } else if (position == 2) {
                            // reportUSerScreem();
                            if (CommonUtils.isOnline(mContext)) {
                                apiReportUser("Inappropriate");
                            } else {
                                ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
                            }
                            dialog.dismiss();
                        } else {
                            dialog.dismiss();
                        }
                    }*/
                        dialog.dismiss();
                    }
                }).negativeText(R.string.cancel).onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        }).show();

    }

    private void initiatePopupWindow(final View targetView) {
        //RelativeLayout customLayout = (RelativeLayout) ((Activity) mContext).getLayoutInflater().inflate(R.layout.popup, null);
        BubbleLayout customLayout;
        customLayout = (BubbleLayout) LayoutInflater.from(this).inflate(R.layout.popup, null);
        final PopupWindow quickAction = BubblePopupHelper.create(this, customLayout);
        LinearLayout llMain = (LinearLayout) customLayout.findViewById(R.id.llMain);
        llMain.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).getInstance(mContext).setPopupBackground()));
        customLayout.setBubbleColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).getInstance(mContext).setPopupBackground()));

      //  final QuickAction quickAction = new QuickAction(mContext, R.style.PopupAnimation, customLayout, customLayout);
        final ImageView ivListItem = (ImageView) customLayout.findViewById(R.id.ivListItem);
        final ImageView ivGridItem = (ImageView) customLayout.findViewById(R.id.ivGridItem);
        final ImageView ivTaggedItem = (ImageView) customLayout.findViewById(R.id.ivTaggedItem);
        final ImageView ivSavedItem = (ImageView) customLayout.findViewById(R.id.ivSavedItem);
        final ImageView ivPinned = (ImageView) customLayout.findViewById(R.id.ivPinned);
        if (userModel.getId().equalsIgnoreCase(new PrefManager(mContext).getUserId())) {
            ivSavedItem.setVisibility(View.VISIBLE);
            ivPinned.setVisibility(View.VISIBLE);

        } else {
            ivSavedItem.setVisibility(View.VISIBLE);
            ivPinned.setVisibility(View.GONE);
        }
        ivGridItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = 1;
                ivGridItem.setImageResource(R.mipmap.grid_sel);
                quickAction.dismiss();
                binding.rvPost.invalidate();
                setGirdRecylerView();
                /*if (CommonUtils.isOnline(mContext)) {
                    callPostListAPI(page, false);
                } else {
                    ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
                }*/
            }
        });

        ivListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = 1;
                ivListItem.setImageResource(R.mipmap.list_icon_sel);
                quickAction.dismiss();
                binding.rvPost.invalidate();
                setHeaderRecylerView();
                /*if (CommonUtils.isOnline(mContext)) {
                    callPostListAPI(page, false);
                } else {
                    ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
                }*/
            }
        });

        ivTaggedItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = 1;
                ivTaggedItem.setImageResource(R.mipmap.group_tag_icon_sel);
                quickAction.dismiss();
                binding.rvPost.invalidate();
                setHeaderRecylerView();
                ToastUtils.showToastShort(mContext, "Work in progress.....");
              /*  if (CommonUtils.isOnline(mContext)) {
                    callTaggedPostListAPI(page);
                } else {
                    ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
                }*/
                quickAction.dismiss();
            }
        });
        ivSavedItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = 1;
                ivTaggedItem.setImageResource(R.mipmap.group_tag_icon_sel);
                quickAction.dismiss();
                binding.rvPost.invalidate();
                setHeaderRecylerView();
               /* if (CommonUtils.isOnline(mContext)) {
                    callSavedPostListAPI(page);
                } else {
                    ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
                }*/
                quickAction.dismiss();
            }
        });
        ivPinned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = 1;
                ivTaggedItem.setImageResource(R.drawable.unpin);
                quickAction.dismiss();
                binding.rvPost.invalidate();
                setHeaderRecylerView();
                if (CommonUtils.isOnline(mContext)) {
                    callPinnedPost();
                } else {
                    ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
                }
                quickAction.dismiss();
            }
        });
        quickAction.showAsDropDown(targetView);
    }


    private void apiReportUser(String reason) {
        if (CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("user_id", userModel.getId());
            jsonObject.addProperty("reason", reason);

            Call<ReportPostResponse> call = ApiExecutor.getClient(mContext).apiReportUser(jsonObject);
            call.enqueue(new retrofit2.Callback<ReportPostResponse>() {
                @Override
                public void onResponse(@NonNull Call<ReportPostResponse> call, @NonNull final Response<ReportPostResponse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            ToastUtils.showToastShort(mContext, response.body().getResponseMessage());
                        } else {
                            ToastUtils.showToastShort(mContext, mContext.getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ReportPostResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(mContext, mContext.getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
        }
    }

    private void openMenuPopup(final com.yellowseed.webservices.response.homepost.Post post, View targetView, final int position) {

        BubbleLayout customLayout = (BubbleLayout) LayoutInflater.from(mContext).inflate(R.layout.popup_menu, null);
        customLayout.setBubbleColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).getInstance(mContext).setPopupBackground()));

        final PopupWindow quickAction = BubblePopupHelper.create(mContext, customLayout);
        LinearLayout otherProfile = (LinearLayout) customLayout.findViewById(R.id.llOtherProfile);
        LinearLayout llMyProfile = (LinearLayout) customLayout.findViewById(R.id.llMyProfile);
        llMyProfile.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).getInstance(mContext).setPopupBackground()));
        otherProfile.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).getInstance(mContext).setPopupBackground()));
        if (new PrefManager(mContext).getUserId().equalsIgnoreCase(post.getUser().getId())) {
            llMyProfile.setVisibility(View.VISIBLE);
            otherProfile.setVisibility(View.GONE);
        } else {
            llMyProfile.setVisibility(View.GONE);
            otherProfile.setVisibility(View.VISIBLE);
        }
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

        tvShareFb.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        tvSendToChat.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        tvEditPost.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        tvShareFb1.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        tvSendToChat1.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        tvHide.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        tvHide1.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        tvRemove.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        Themes.getInstance(mContext).changeRightIcon(mContext, ivEdit);
        Themes.getInstance(mContext).changeIconColorToWhite(mContext, ivShare);
        Themes.getInstance(mContext).changeIconColorToWhite(mContext, ivSend2);
        Themes.getInstance(mContext).changeIconColorToWhite(mContext, ivShare1);
        Themes.getInstance(mContext).changeIconColorToWhite(mContext, ivSend);
        Themes.getInstance(mContext).changeIconColorToWhite(mContext, ivHide);
        Themes.getInstance(mContext).changeIconColorToWhite(mContext, ivHide1);
        Themes.getInstance(mContext).changeIconColorToWhite(mContext, ivRemove);

        tvHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quickAction.dismiss();
            }
        });

        tvHide1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quickAction.dismiss();
            }
        });


        tvRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quickAction.dismiss();
            }
        });
        tvDeletePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePostPopup(position, models.get(position).getPost().getId(), models.get(position).getSharedId());
                quickAction.dismiss();
            }
        });
        tvShareFb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //facebookSharing(post);
                CommonUtils.showToast(mContext, "Work in progress");

                quickAction.dismiss();
            }
        });
        tvShareFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // facebookSharing(post);
                CommonUtils.showToast(mContext, "Work in progress");

                quickAction.dismiss();
            }
        });
        tvSendToChat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.showToast(mContext, "Work in progress...");
                /*if (CommonUtils.isOnline(mContext)) {
                    callGetRoomAPI(post);
                } else {
                    CommonUtils.showToast(mContext, getString(R.string.internet_alert_msg));
                }*/
                quickAction.dismiss();
            }
        });
        tvSendToChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.showToast(mContext, "Work in progress...");
                /*if (CommonUtils.isOnline(mContext)) {
                    callGetRoomAPI(post);
                } else {
                    CommonUtils.showToast(mContext, getString(R.string.internet_alert_msg));
                }*/
                quickAction.dismiss();
            }
        });
        tvReportUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // addReportDialog(position);
                setSpamDialog(position);
                quickAction.dismiss();
            }
        });
        tvEditPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quickAction.dismiss();

                startActivity(new Intent(mContext, SocialPostActivity.class)
                        .putExtra(AppConstants.FROM, AppConstants.FROM_EDIT_POST)
                        .putExtra(AppConstants.POSITION, position)
                        .putExtra(AppConstants.POST_MODEL, models.get(position)));
            }
        });


        quickAction.showAsDropDown(targetView);
    }

    private void facebookSharing(com.yellowseed.webservices.response.homepost.Post post) {
        if (CommonUtils.isOnline(mContext)) {
            if (post.getImages() != null && post.getImages().size() > 0 && post.getImages().get(0).getFile().getUrl() != null) {
                CallbackManager callbackManager = CallbackManager.Factory.create();
                ShareDialog shareDialog = new ShareDialog((Activity) mContext);
                String imagePath = post.getImages().get(0).getFile().getUrl();
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
                CommonUtils.showToast(mContext, "You can only share image via facebook.");
            }


        } else {
            CommonUtils.showToast(mContext, mContext.getString(R.string.internet_alert_msg));
        }
    }
    private void setSpamDialog(final int postPos) {


        final SpamdialogBinding spamdialogBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.spamdialog, null, false);

        final Dialog dialog = DialogUtils.createDialog(mContext, spamdialogBinding.getRoot());
        spamdialogBinding.llMain.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeDialog()));
        spamdialogBinding.viewHate.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setViewLineGrey()));
        spamdialogBinding.viewLineFalse.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setViewLineGrey()));
        spamdialogBinding.viwLine2.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setViewLineGrey()));
        spamdialogBinding.viewLineSpam.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setViewLineGrey()));
        spamdialogBinding.tvCancel.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setCancel()));
        spamdialogBinding.tvhelpUs.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        spamdialogBinding.tvFalseStory.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setSpamText()));
        spamdialogBinding.tvSpam.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setSpamText()));
        spamdialogBinding.tvHate.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setSpamText()));
        spamdialogBinding.tvInappropriate.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setCancel()));

        spamdialogBinding.tvCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        spamdialogBinding.tvInappropriate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        spamdialogBinding.tvHate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        spamdialogBinding.tvSpam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        spamdialogBinding.tvFalseStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //Setting the recycler view inside the dialog

    }

    private void addReportDialog(final int postPos) {

        final CharSequence[] items = {Html.fromHtml("<font color = '#FC2B2B'>Its Spam</font>"), Html.fromHtml("<font color = '#FC2B2B'>Inappropriate</font>"), Html.fromHtml("<font color = '#FC2B2B'>Its False Story</font>"), Html.fromHtml("<font color = '#FC2B2B'>Hate Speech</font>")};
        new MaterialDialog.Builder(mContext)
                .title(R.string.helpusunderstand).titleGravity(GravityEnum.CENTER)
                .items(items)
                .itemsColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()))
                .backgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeStory()))
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
                        CommonUtils.showToast(mContext, "Work in progress...");
                     /*   if (CommonUtils.isOnline(mContext)) {
                            callReportPostApi(report, models.get(postPos).getPost().getId(), models.get(postPos).getSharedId());
                        } else {
                            CommonUtils.showToast(mContext, getString(R.string.internet_alert_msg));
                        }*/

                    }
                }).show();

    }


    private void callReportPostApi(String report, String postId, String sharedId) {
        if (CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();
            JsonObject jsonObject = new JsonObject();
            if (sharedId != null && !sharedId.equalsIgnoreCase("")) {
                jsonObject.addProperty(AppConstants.SHARED_ID, sharedId);
            } else {
                jsonObject.addProperty(AppConstants.POST_ID, postId);
            }
            jsonObject.addProperty(AppConstants.REASON, report);
            Call<ReportPostResponse> call = ApiExecutor.getClient(mContext).apiReportUser(jsonObject);
            call.enqueue(new retrofit2.Callback<ReportPostResponse>() {
                @Override
                public void onResponse(@NonNull Call<ReportPostResponse> call, @NonNull final Response<ReportPostResponse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            ToastUtils.showToastShort(mContext, response.body().getResponseMessage());
                        } else {
                            ToastUtils.showToastShort(mContext, mContext.getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ReportPostResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(mContext, mContext.getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
        }
    }

    private void deletePostPopup(final int position, final String postId, final String sharedId) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.delete_cnfm);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                CommonUtils.showToast(mContext, "Work in progress...");


                /*if (CommonUtils.isOnline(mContext)) {
                    deletePostApi(position, postId, sharedId);
                } else {
                    CommonUtils.showToast(mContext, getString(R.string.internet_alert_msg));
                }*/
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


    private void deletePostApi(final int position, String postId, String sharedId) {
        final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
        progressDialog.show();

        JsonObject jsonObject = new JsonObject();

        if (sharedId != null && !sharedId.equalsIgnoreCase("")) {
            jsonObject.addProperty("shared_id", sharedId);
        } else {
            jsonObject.addProperty("post_id", postId);
        }

        Call<ApiResponse> call = ApiExecutor.getClient(mContext).apiDeletePost(jsonObject);
        call.enqueue(new retrofit2.Callback<ApiResponse>() {


            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (response.body() != null && response.body().getResponseCode() == 200) {

                    models.remove(position);
                    if (response.body().getResponseMessage() != null && !response.body().getResponseMessage().equalsIgnoreCase("")) {
                        CommonUtils.showToast(mContext, response.body().getResponseMessage());
                    }
                    adapter.notifyDataSetChanged();
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

    /**
     * method for create story api
     */
    private void callGetRoomAPI(final Post post) {
        if (CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();
            JsonObject jsonObject = new JsonObject();
            Call<GetRoomResonse> call = ApiExecutor.getClient(mContext).apiGetRoom(jsonObject);
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
                            adapter.notifyDataSetChanged();
                            setPostShareDialogeRecyclerView(post);
                        } else {
                            ToastUtils.showToastShort(mContext, mContext.getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GetRoomResonse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(mContext, mContext.getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
        }
    }


    private List<Post> getPostData() {

        // builds country data from json
        InputStream is = getResources().openRawResource(R.raw.post_profile_data);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String jsonString = writer.toString();
        com.yellowseed.webservices.response.homepost.PostResponse data = new Gson().fromJson(jsonString, com.yellowseed.webservices.response.homepost.PostResponse.class);

        return data.getPost();
    }


}