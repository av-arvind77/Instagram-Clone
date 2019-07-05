package com.yellowseed.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.quickblox.sample.video.activities.CallActivity;
import com.quickblox.sample.video.utils.CollectionsUtils;
import com.quickblox.users.model.QBUser;
import com.quickblox.videochat.webrtc.QBRTCClient;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.QBRTCTypes;
import com.yellowseed.R;
import com.yellowseed.adapter.ChatScreenAdapter;
import com.yellowseed.adapter.GridAdapter;
import com.yellowseed.adapter.VideoCallingUserListAdapter;
import com.yellowseed.database.RoomsBackgroundTable;
import com.yellowseed.database.RoomsTable;
import com.yellowseed.database.WallpaperModel;
import com.yellowseed.databinding.ActivityChatsScreenFrgBinding;
import com.yellowseed.databinding.DeleteDialogBinding;
import com.yellowseed.databinding.LayoutGroupuserListBinding;
import com.yellowseed.databinding.LayoutattachementsiconsBinding;
import com.yellowseed.imageUtils.CheckPermission;
import com.yellowseed.imageUtils.CropImage;
import com.yellowseed.listener.ChatModelObject;
import com.yellowseed.listener.DateObject;
import com.yellowseed.listener.ListObject;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.listener.OnItemLongClickListener;
import com.yellowseed.model.ChatScreenModel;
import com.yellowseed.model.ContactModel;
import com.yellowseed.model.DeleteRequestModel;
import com.yellowseed.model.LocationModel;
import com.yellowseed.model.resModel.GetChatResonse;
import com.yellowseed.model.resModel.GetRoomResonse;
import com.yellowseed.model.resModel.GroupMemberResonse;
import com.yellowseed.quickblox.call.VideoCallSessionManager;
import com.yellowseed.record_view.OnBasketAnimationEnd;
import com.yellowseed.record_view.OnRecordAudioResult;
import com.yellowseed.record_view.OnRecordClickListener;
import com.yellowseed.record_view.OnRecordListener;
import com.yellowseed.utils.ActivityController;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.PrefManager;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.TimeStampFormatter;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;
import com.yellowseed.webservices.ApiResponse;
import com.yellowseed.webservices.PushModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.gusavila92.websocketclient.WebSocketClient;

import static com.yellowseed.imageUtils.TakePictureUtils.CROP_FROM_CAMERA;
import static com.yellowseed.utils.TakePictureUtils.takePicture;

public class ChatsScreenFrgActivity extends BaseActivity implements PopupMenu.OnMenuItemClickListener {
    public static boolean isCheck = false;
    private Context context;
    private ActivityChatsScreenFrgBinding binding;
    //  private ArrayList<GetChatResonse.UserInfoBean> chatScreenModels = new ArrayList<>();
    private ArrayList<GetChatResonse.UserInfoBean> userInfoBeans = new ArrayList<>();
    private ArrayList<String> roomIds = new ArrayList<>();
    private ArrayList<String> arlGifs = new ArrayList<>();
    private ChatScreenAdapter chatScreenAdapter;
    private Dialog attachementDialog;
    //   private GetChatResonse.UserInfoBean chatScreenModel;
    private String chatUserId, senderId;
    private String roomId;
    private String from;
    private Themes themes;
    private String chatName;
    private boolean chatIsGroup;
    private String image = "", video = "", path = "", userImage, userType = "group";
    private boolean isBlocked, isMute, isBroadCast;
    private int PLACE_PICKER_REQUEST = 1;
    private int RQS_PICK_CONTACT = 311;
    private int CHOOSE_FILE_REQUESTCODE = 312;
    private WebSocketClient webSocketClient;
    private GetChatResonse.UserInfoBean.ReplyMessageBean replyMessage;
    private int lastPosition;
    private long record_time;
    private GetRoomResonse.RoomsBean roomsBean;
    private String headerName = "";
    private String qbId;
    private ArrayList<ChatScreenModel> chatScreenModels = new ArrayList<>();

    private Dialog dialog;
    private WallpaperModel wallpaperModel;
    private VideoCallingUserListAdapter videoCallingUserListAdapter;
    private GridAdapter adapter;
    private List<GetChatResonse.GroupMemberResponse> groupMemberResponseList = new ArrayList<>();
    private List<GroupMemberResonse.MembersBean> models = new ArrayList<>();
    private PopupWindow pwindo;
    private Boolean darkThemeEnabled;
    private PopupWindow popupWindow;
    private int flag = 0;
    private Animation slideUpAnimation, slideDownAnimation, slideTopToDown;
    private boolean isSingleSelection = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chats_screen_frg);

        context = ChatsScreenFrgActivity.this;
        themes = new Themes(context);
        darkThemeEnabled = CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME);
        if (darkThemeEnabled)
            binding.layoutCustomChatMenuFrg.tvSendButton.setImageResource(R.mipmap.mike);
        else
            binding.layoutCustomChatMenuFrg.tvSendButton.setImageResource(R.mipmap.mike_icon);


        getIntentValue();
        //  setUi();
        //  createWebSocketClient();
        initializedControl();
        setToolBar();
        setOnClickListener();
        setGirdRecylerView();
        setCurrentTheme();


    }


    private void setCurrentTheme() {
        binding.flContainer.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        binding.layoutCustomChatMenuFrg.llChatBar.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        binding.layoutCustomChatMenuFrg.lleditText.setBackground(ContextCompat.getDrawable(context, Themes.getInstance(context).setDarkStoryBackground()));
        binding.toolbarMain.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        binding.tvHeader.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        Themes.getInstance(context).changeIconWhite(context, binding.layoutCustomChatMenuFrg.ivChatLeftIcon);
        Themes.getInstance(context).changeIconWhite(context, binding.layoutCustomChatMenuFrg.btnSecondRight);
        //  Themes.getInstance(context).changeIconWhite(context, binding.layoutCustomChatMenuFrg.tvSendButton);
        binding.viewLine.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setViewLineGrey()));
        binding.viewLineBottom.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setViewLineGrey()));

        Themes.getInstance(context).changeIconWhite(context, binding.layoutCustomChatMenuFrg.ivChatRightIcon);
        Themes.getInstance(context).changeIconWhite(context, binding.layoutCustomChatMenuFrg.btnSecondRight);
        Themes.getInstance(context).changeIconColor(context, binding.ivLeft);
        Themes.getInstance(context).changeIconColor(context, binding.ivRight);
        Themes.getInstance(context).changeIconColor(context, binding.ivRightSecond);

        binding.layoutChatClickItem.llChatItems.setBackgroundColor(ContextCompat.getColor(context, themes.setDarkChatBox()));
        binding.layoutChatClickItem.tvShare.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.layoutChatClickItem.tvStar.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.layoutChatClickItem.tvReply.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.layoutChatClickItem.tvCopy.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.layoutChatClickItem.tvForward.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        themes.changeIconColorToWhite(context, binding.layoutChatClickItem.ivStared);
        themes.changeIconColorToWhite(context, binding.layoutChatClickItem.ivBackward);
        themes.changeIconColorToWhite(context, binding.layoutChatClickItem.ivCopy);
        themes.changeIconColorToWhite(context, binding.layoutChatClickItem.ivForward);
        themes.changeShareIcon(context, binding.layoutChatClickItem.ivShare);

        binding.layoutCustomChatMenuFrg.etChatMessage.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));


        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            binding.llAttachmentlayout.ivDocument.setImageResource(R.mipmap.doc_attach_black);
        } else {
            binding.llAttachmentlayout.ivDocument.setImageResource(R.mipmap.document);
        }
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            binding.llAttachmentlayout.ivCamera.setImageResource(R.mipmap.camera_attach_black);
        } else {
            binding.llAttachmentlayout.ivCamera.setImageResource(R.mipmap.camera);
        }
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            binding.llAttachmentlayout.ivGallery.setImageResource(R.mipmap.gallery_attach_black);
        } else {
            binding.llAttachmentlayout.ivGallery.setImageResource(R.mipmap.gallery);
        }
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            binding.llAttachmentlayout.ivLocation.setImageResource(R.mipmap.loc_attach_black);
        } else {
            binding.llAttachmentlayout.ivLocation.setImageResource(R.mipmap.location);
        }
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            binding.llAttachmentlayout.ivAudio.setImageResource(R.mipmap.audio_attach_black);
        } else {
            binding.llAttachmentlayout.ivAudio.setImageResource(R.mipmap.audio);
        }
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            binding.llAttachmentlayout.ivContact.setImageResource(R.mipmap.contact_attach_black);

        } else {
            binding.llAttachmentlayout.ivContact.setImageResource(R.mipmap.contact);
        }
        binding.llAttachmentlayout.llAttachment.setBackground(ContextCompat.getDrawable(context, Themes.getInstance(context).setRoundedBlackBackground()));
        binding.llAttachmentlayout.btnAttachementsDialog.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.llAttachmentlayout.tvattachDocuments.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.llAttachmentlayout.tvattachCamera.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.llAttachmentlayout.tvAttachGallery.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.llAttachmentlayout.tvAttachLocation.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.llAttachmentlayout.tvAttachContacts.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.llAttachmentlayout.tvAudio.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));


    }

    private void getIntentValue() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            if (getIntent().getStringExtra(AppConstants.FROM) != null) {
                from = getIntent().getStringExtra(AppConstants.FROM);
            }

        }
    }

    @Override
    public void initializedControl() {
        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_in_from_bottom);
        slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_out_to_bottom);
        slideTopToDown = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_in_from_top);

        chatRecyclerView();

        //IMPORTANT
        binding.layoutCustomChatMenuFrg.tvSendButton.setRecordView(binding.layoutCustomChatMenuFrg.recordView, binding.layoutCustomChatMenuFrg.llText);
        binding.layoutCustomChatMenuFrg.tvSendButton.setOnRecordClickListener(new OnRecordClickListener() {
            @Override
            public void onClick(View v) {
                binding.llAttachmentlayout.llAttachment.setVisibility(View.GONE);
                binding.layoutCustomChatMenuFrg.etChatMessage.setText("");
                if (binding.layoutCustomChatMenuFrg.etChatMessage.getText().toString().trim().length() == 0) {
                    return;
                }
            }
        });


        binding.layoutCustomChatMenuFrg.tvSendButton.setResultListner(new OnRecordAudioResult() {
            @Override
            public void onSuccess(final String filename) {

                binding.layoutCustomChatMenuFrg.etChatMessage.setText("");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //  appAudio(filename);
                    }
                }, 2000);
            }
        });


        //Cancel Bounds is when the Slide To Cancel text gets before the timer . default is 25
        binding.layoutCustomChatMenuFrg.recordView.setCancelBounds(30);


        binding.layoutCustomChatMenuFrg.recordView.setSmallMicColor(Color.parseColor("#c2185b"));

        //prevent recording under one Second
        binding.layoutCustomChatMenuFrg.recordView.setLessThanSecondAllowed(false);


        binding.layoutCustomChatMenuFrg.recordView.setSlideToCancelText("Slide To Cancel");


        binding.layoutCustomChatMenuFrg.recordView.setCustomSounds(R.raw.record_start, R.raw.record_finished, 0);


        binding.layoutCustomChatMenuFrg.recordView.setOnRecordListener(new OnRecordListener() {
            @Override
            public void onStart() {
                //  Log.d("RecordView", "onStart");
                Toast.makeText(context, "OnStartRecord", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                // Toast.makeText(context, "onCancel", Toast.LENGTH_SHORT).show();
                Log.d("RecordView", "onCancel");
                record_time = 0;
            }

            @Override
            public void onFinish(long recordTime) {
                record_time = recordTime;
                String time = CommonUtils.getHumanTimeText(recordTime);
                Toast.makeText(context, "onFinishRecord - Recorded Time is: " + time, Toast.LENGTH_SHORT).show();
                Log.d("RecordView", "onFinish");

                Log.d("RecordTime", time);
            }

            @Override
            public void onLessThanSecond() {
                record_time = 0;
                //   Toast.makeText(context, "OnLessThanSecond", Toast.LENGTH_SHORT).show();
                Log.d("RecordView", "onLessThanSecond");
            }
        });


        binding.layoutCustomChatMenuFrg.recordView.setOnBasketAnimationEndListener(new OnBasketAnimationEnd() {
            @Override
            public void onAnimationEnd() {
                Log.d("RecordView", "Basket Animation Finished");
            }
        });

    }

    private void chatRecyclerView() {
        binding.rvChatListFrg.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvChatListFrg.setLayoutManager(layoutManager);
        ChatScreenModel chatScreenModel = new ChatScreenModel();
        chatScreenModel.setMessages("Hi");
        chatScreenModels.add(chatScreenModel);

        chatScreenModel = new ChatScreenModel();
        chatScreenModel.setMessages("Hello");
        chatScreenModels.add(chatScreenModel);

        chatScreenAdapter = new ChatScreenAdapter(context, chatScreenModels, new OnItemLongClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.llReciver:

//                        if (isSingleSelection) {

                        if ( chatScreenModels.get(position).isSelected()) {
                            chatScreenModels.get(position).setSelected(false);
                        }
                        else
                        {
                            chatScreenModels.get(position).setSelected(true);
                        }


//                            adapter.notifyItemChanged(position);
                        adapter.notifyDataSetChanged();

//                        }
                        // binding.layoutChatclickItem.llChatItems.setVisibility(View.VISIBLE);
                        // binding.layoutCustomChatMenuFrg.llChatBar.setVisibility(View.GONE);
                        break;

                    case R.id.llSender:
                        ToastUtils.showToastShort(context, "hello");

//                        if (isSingleSelection) {
                        if (chatScreenModels.get(position).isSelected()) {
                            chatScreenModels.get(position).setSelected(false);
                        } else {
                            chatScreenModels.get(position).setSelected(true);
                        }
//                            adapter.notifyItemChanged(position);
                        adapter.notifyDataSetChanged();
//                        }

                        break;
//                    case R.id.ivDelete:
//                        chatScreenModels.remove(position);
//                        chatScreenAdapter.notifyItemRemoved(position);
//                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onLongClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.llReciver:
                        ToastUtils.showToastShort(context, "reciver");

                        if (chatScreenModels.get(position).isSelected()) {
                            chatScreenModels.get(position).setSelected(false);
                        } else {
                            chatScreenModels.get(position).setSelected(true);
                        }
                        adapter.notifyDataSetChanged();
                        break;

                    case R.id.llSender:
                        ToastUtils.showToastShort(context, "sender");
                        if (chatScreenModels.get(position).isSelected()) {
                            chatScreenModels.get(position).setSelected(false);
                        } else {
                            chatScreenModels.get(position).setSelected(true);
                        }
//                        adapter.notifyItemChanged(position);
                        adapter.notifyDataSetChanged();
                        // binding.layoutChatclickItem.llChatItems.setVisibility(View.VISIBLE);
                        // binding.layoutCustomChatMenuFrg.llChatBar.setVisibility(View.GONE);
                        break;

                }

            }

          /*  @Override
            public void onLongClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.llReciver:
                        ToastUtils.showToastShort(context, "gfgf");

                        if (chatScreenModels.get(position).isSelected()) {
                            chatScreenModels.get(position).setSelected(false);
                        } else {
                            chatScreenModels.get(position).setSelected(true);
                        }
                        adapter.notifyDataSetChanged();
//                        adapter.notifyItemChanged(position);
                        //   binding.chatSenderBinding.setVisibility(View.VISIBLE);
                        // binding.layoutCustomChatMenuFrg.llChatBar.setVisibility(View.GONE);
                        break;
                    case R.id.llSender:
                        ToastUtils.showToastShort(context, "gfgf");
                        if (chatScreenModels.get(position).isSelected()) {
                            chatScreenModels.get(position).setSelected(false);
                        } else {
                            chatScreenModels.get(position).setSelected(true);
                        }
//                        adapter.notifyItemChanged(position);
                        adapter.notifyDataSetChanged();
                        // binding.layoutChatclickItem.llChatItems.setVisibility(View.VISIBLE);
                        // binding.layoutCustomChatMenuFrg.llChatBar.setVisibility(View.GONE);
                        break;

//                    case R.id.ivDelete:
//                        chatScreenModels.remove(position);
//                        chatScreenAdapter.notifyItemRemoved(position);
//                        break;
                    default:
                        break;
                }

            }
        });*/
        });
        binding.rvChatListFrg.setAdapter(chatScreenAdapter);
        chatScreenAdapter.notifyItemRangeChanged(0, chatScreenAdapter.getItemCount());
    }


/*
    private void chatRecyclerView() {
        binding.rvChatListFrg.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvChatListFrg.setLayoutManager(layoutManager);
        chatScreenAdapter = new ChatScreenAdapter(ChatsScreenFrgActivity.this, groupDataIntoHashMap(chatScreenModels), new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.rlRow:
                        if (lastPosition == position) {
                            if (!((ChatModelObject) chatScreenAdapter.getItem(position)).getChatModel().isSelected()) {
                                lastPosition = position;
                                ((ChatModelObject) chatScreenAdapter.getItem(position)).getChatModel().setSelected(true);
                            } else {
                                ((ChatModelObject) chatScreenAdapter.getItem(position)).getChatModel().setSelected(false);
                            }
                        } else {
                            if (lastPosition != 0) {
                                ((ChatModelObject) chatScreenAdapter.getItem(lastPosition)).getChatModel().setSelected(false);
                            }
                            ((ChatModelObject) chatScreenAdapter.getItem(position)).getChatModel().setSelected(true);
                            lastPosition = position;
                        }
                        chatScreenAdapter.setDataChange(groupDataIntoHashMap(chatScreenModels));
                        showBottomBar(chatScreenModels);
                        break;
                    case R.id.llRowSender:
                        if (lastPosition == position) {
                            if (!((ChatModelObject) chatScreenAdapter.getItem(position)).getChatModel().isSelected()) {
                                lastPosition = position;
                                ((ChatModelObject) chatScreenAdapter.getItem(position)).getChatModel().setSelected(true);
                            } else {
                                ((ChatModelObject) chatScreenAdapter.getItem(position)).getChatModel().setSelected(false);
                            }
                        } else {
                            if (lastPosition != 0) {
                                ((ChatModelObject) chatScreenAdapter.getItem(lastPosition)).getChatModel().setSelected(false);

                            }
                            ((ChatModelObject) chatScreenAdapter.getItem(position)).getChatModel().setSelected(true);
                        }
                        chatScreenAdapter.setDataChange(groupDataIntoHashMap(chatScreenModels));
                        showBottomBar(chatScreenModels);
                        break;
                    case R.id.llChatReciever:

                        if (((ChatModelObject) chatScreenAdapter.getItem(position)).getChatModel().getUpload_type() != null && ((ChatModelObject) chatScreenAdapter.getItem(position)).getChatModel().getUpload_type().length() > 0) {
                            switch (((ChatModelObject) chatScreenAdapter.getItem(position)).getChatModel().getUpload_type()) {
                                case "audio":
                                    break;
                                case "video":
                                    context.startActivity(new Intent(context, VideoDetailActivity.class).putExtra("url", ((ChatModelObject) chatScreenAdapter.getItem(position)).getChatModel().getBody()));
                                    break;
                                case "location":
                                    Gson gson = new Gson();
                                    LocationModel locationModel = gson.fromJson(((ChatModelObject) chatScreenAdapter.getItem(position)).getChatModel().getBody(), LocationModel.class);
                                    CommonUtils.showMap(context, String.valueOf(locationModel.getLatitude()), String.valueOf(locationModel.getLongitude()), locationModel.getName());
                                    break;
                                case "contact":

                                    break;
                                case "image":

                                    break;

                                case "link":
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(((ChatModelObject) chatScreenAdapter.getItem(position)).getChatModel().getBody()));
                                    startActivity(i);

                                    break;

                                case "post":
                                    Gson gson1 = new Gson();
                                    PostChatBody postChatBody = gson1.fromJson(((ChatModelObject) chatScreenAdapter.getItem(position)).getChatModel().getBody(), PostChatBody.class);
                                    Intent intent = new Intent(context, ShowPostItemsActivity.class);
                                    intent.putExtra("post_id", postChatBody.getPost_id());
                                    startActivity(intent);

                                    break;
                            }
                        }
                        break;

                    case R.id.llAddView:
                        if (((ChatModelObject) chatScreenAdapter.getItem(position)).getChatModel().getUpload_type() != null && ((ChatModelObject) chatScreenAdapter.getItem(position)).getChatModel().getUpload_type().length() > 0) {
                            switch (chatScreenModels.get(position).getUpload_type()) {
                                case "audio":

                                    break;
                                case "video":
                                    context.startActivity(new Intent(context, VideoDetailActivity.class).putExtra("url", ((ChatModelObject) chatScreenAdapter.getItem(position)).getChatModel().getBody()));
                                    break;
                                case "location":
                                    Gson gson = new Gson();
                                    LocationModel locationModel = gson.fromJson(((ChatModelObject) chatScreenAdapter.getItem(position)).getChatModel().getBody(), LocationModel.class);
                                    CommonUtils.showMap(context, String.valueOf(locationModel.getLatitude()), String.valueOf(locationModel.getLongitude()), locationModel.getName());
                                    break;
                                case "contact":

                                    break;
                                case "image":

                                    break;
                                case "post":
                                    Gson gson1 = new Gson();
                                    PostChatBody postChatBody = gson1.fromJson(((ChatModelObject) chatScreenAdapter.getItem(position)).getChatModel().getBody(), PostChatBody.class);
                                    Intent intent = new Intent(context, ShowPostItemsActivity.class);
                                    intent.putExtra("post_id", postChatBody.getPost_id());
                                    startActivity(intent);
                                    break;

                                case "link":
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(((ChatModelObject) chatScreenAdapter.getItem(position)).getChatModel().getBody()));
                                    startActivity(i);
                                    break;
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        binding.rvChatListFrg.setAdapter(chatScreenAdapter);
        chatScreenAdapter.notifyItemRangeChanged(0, chatScreenAdapter.getItemCount());
        binding.rvChatListFrg.smoothScrollToPosition(chatScreenAdapter.getItemCount());
    }*/

    private void showBottomBar(ArrayList<GetChatResonse.UserInfoBean> chatScreenModels) {
        int count = 0;
        for (int i = 0; i < chatScreenModels.size(); i++) {
            if (chatScreenModels.get(i).isSelected()) {
                count = +1;
            }
        }

        if (count > 0) {
            binding.layoutChatClickItem.getRoot().setVisibility(View.VISIBLE);
        } else {
            binding.layoutChatClickItem.getRoot().setVisibility(View.GONE);
        }
    }

    @Override
    public void setToolBar() {
        // For toolbar items
        binding.ivLeft.setVisibility(View.VISIBLE);
        binding.ivLeft.setImageResource(R.mipmap.back_icon);


        binding.tvHeader.setVisibility(View.VISIBLE);
        binding.tvHeader.setText("John Thomas");

        switch (userType) {
            case "group":
                binding.tvHeader.setText("John Thomas");
                binding.ivRight.setImageResource(R.mipmap.three_dot);
                binding.ivRightSecond.setImageResource(R.mipmap.icon_call);
                binding.ivRightSecond.setVisibility(View.VISIBLE);
                binding.ivRight.setVisibility(View.VISIBLE);
                break;
            case "single":
                binding.ivRight.setVisibility(View.VISIBLE);
                binding.ivRight.setImageResource(R.mipmap.three_dot);
                binding.ivRightSecond.setVisibility(View.VISIBLE);
                binding.tvHeader.setText("John Thomas");

                binding.ivRightSecond.setImageResource(R.mipmap.icon_call);
                break;
            case "broadcast":
                binding.ivRight.setVisibility(View.GONE);
                binding.ivRight.setImageResource(R.mipmap.three_dot);
                binding.ivRightSecond.setVisibility(View.GONE);
                binding.tvHeader.setText("John Thomas");

                binding.ivRightSecond.setImageResource(R.mipmap.icon_call);
                break;

            case "Anonymous":
                binding.ivRight.setVisibility(View.GONE);
                binding.ivRight.setImageResource(R.mipmap.three_dot);
                binding.ivRightSecond.setVisibility(View.GONE);
                binding.tvHeader.setText("John Thomas");

                binding.ivRightSecond.setImageResource(R.mipmap.icon_call);
                break;
            default:
                break;
        }
        // For chat Bottom bar Item
        //  binding.layoutCustomChatMenuFrg.btnMidRight.setVisibility(View.VISIBL
        binding.layoutCustomChatMenuFrg.ivChatLeftIcon.setVisibility(View.VISIBLE);
        binding.layoutCustomChatMenuFrg.ivChatLeftIcon.setImageResource(R.mipmap.samile_icon);
        binding.layoutCustomChatMenuFrg.ivChatRightIcon.setVisibility(View.VISIBLE);
        binding.layoutCustomChatMenuFrg.ivChatRightIcon.setImageResource(R.mipmap.attachment_icon);


        // For set button on changed of text
        binding.layoutCustomChatMenuFrg.etChatMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 0) {
                    if (darkThemeEnabled)

                        binding.layoutCustomChatMenuFrg.tvSendButton.setImageResource(R.mipmap.send_icon);

                    else

                        binding.layoutCustomChatMenuFrg.tvSendButton.setImageResource(R.mipmap.up_send);

                    binding.layoutCustomChatMenuFrg.tvSendButton.setListenForRecord(false);
                } else {
                    if (darkThemeEnabled)
                        binding.layoutCustomChatMenuFrg.tvSendButton.setImageResource(R.mipmap.mike);
                    else
                        binding.layoutCustomChatMenuFrg.tvSendButton.setImageResource(R.mipmap.mike_icon);

                    binding.layoutCustomChatMenuFrg.tvSendButton.setListenForRecord(true);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void setOnClickListener() {
        binding.ivLeft.setOnClickListener(this);
        binding.tvHeader.setOnClickListener(this);
        binding.ivRight.setOnClickListener(this);
        binding.ivRightSecond.setOnClickListener(this);
        binding.layoutCustomChatMenuFrg.btnSecondRight.setOnClickListener(this);
        binding.layoutCustomChatMenuFrg.ivChatLeftIcon.setOnClickListener(this);
        binding.layoutCustomChatMenuFrg.ivChatRightIcon.setOnClickListener(this);
        binding.layoutCustomChatMenuFrg.ivCancelReply.setOnClickListener(this);
        binding.layoutCustomChatMenuFrg.llChatBar.setOnClickListener(this);
        binding.layoutCustomChatMenuFrg.llMsg.setOnClickListener(this);
        binding.layoutCustomChatMenuFrg.etChatMessage.setOnClickListener(this);


        binding.layoutChatClickItem.ivBackward.setOnClickListener(this);
        binding.layoutChatClickItem.ivForward.setOnClickListener(this);
        binding.layoutChatClickItem.ivCopy.setOnClickListener(this);
        binding.layoutChatClickItem.ivDelete.setOnClickListener(this);
        binding.layoutChatClickItem.ivShare.setOnClickListener(this);
        binding.layoutChatClickItem.ivStared.setOnClickListener(this);


        binding.llAttachmentlayout.ivDocument.setOnClickListener(this);
        binding.llAttachmentlayout.ivAudio.setOnClickListener(this);
        binding.llAttachmentlayout.ivCamera.setOnClickListener(this);
        binding.llAttachmentlayout.ivGallery.setOnClickListener(this);
        binding.llAttachmentlayout.ivLocation.setOnClickListener(this);
        binding.llAttachmentlayout.ivContact.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBackward:
                binding.llAttachmentlayout.llAttachment.setVisibility(View.GONE);

                break;

            case R.id.llMsg:
                binding.llAttachmentlayout.llAttachment.setVisibility(View.GONE);
                break;


            case R.id.ivForward:
                ActivityController.startActivity(context, NewGroupNextActivity.class);
                /*startActivity(new Intent(context, NewGroupNextActivity.class).putExtra(AppConstants.FROM, "forward").putExtra("forward", chatScreenModels.get(getSeletedMessage(chatScreenModels))));
                binding.layoutChatClickItem.getRoot().setVisibility(View.GONE);
                chatScreenModels.get(getSeletedMessage(chatScreenModels)).setSelected(false);
                chatScreenAdapter.notifyItemChanged(getSeletedMessage(chatScreenModels));
                binding.rvChatListFrg.smoothScrollToPosition(chatScreenAdapter.getItemCount());*/
                finish();

                break;

            case R.id.ivCopy:
              /*  if (chatScreenModels.get(getSeletedMessage(chatScreenModels)).getUpload_type() != null && chatScreenModels.get(getSeletedMessage(chatScreenModels)).getUpload_type().equalsIgnoreCase("")) {
                    ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(chatScreenModels.get(getSeletedMessage(chatScreenModels)).getBody());
                    Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();
                    chatScreenModels.get(getSeletedMessage(chatScreenModels)).setSelected(false);
                    chatScreenAdapter.notifyItemChanged(getSeletedMessage(chatScreenModels));
                    binding.rvChatListFrg.smoothScrollToPosition(chatScreenAdapter.getItemCount());
                }*/
                binding.layoutChatClickItem.getRoot().setVisibility(View.GONE);
                break;

            case R.id.ivDelete:
                binding.llAttachmentlayout.llAttachment.setVisibility(View.GONE);

                deleteDialog();

                break;
            case R.id.ivCancelReply:
                replyMessage = null;
                binding.layoutCustomChatMenuFrg.llReply.setVisibility(View.GONE);
                break;

            case R.id.ivShare:
                /*if (chatScreenModels.get(getSeletedMessage(chatScreenModels)).getUpload_type() != null && chatScreenModels.get(getSeletedMessage(chatScreenModels)).getUpload_type().length() > 0) {
                    if (chatScreenModels.get(getSeletedMessage(chatScreenModels)).getUpload_type().equalsIgnoreCase("image")) {
                        chatScreenModels.get(getSeletedMessage(chatScreenModels)).setSelected(false);
                        chatScreenAdapter.notifyItemChanged(getSeletedMessage(chatScreenModels));
                        binding.rvChatListFrg.smoothScrollToPosition(chatScreenAdapter.getItemCount());
                        try {
                            shareTextUrl(chatScreenModels.get(getSeletedMessage(chatScreenModels)).getBody());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }*/
                break;

            case R.id.etChatMessage:
                binding.llAttachmentlayout.llAttachment.setVisibility(View.GONE);
                break;

            case R.id.ivStared:
               /* staredUnstaredMessageapi(chatScreenModels, getSeletedMessage(chatScreenModels));
                binding.layoutChatClickItem.getRoot().setVisibility(View.GONE);
            */
                break;


            case R.id.btnSecondRight:
                binding.llAttachmentlayout.llAttachment.setVisibility(View.GONE);

                if (!CheckPermission.checkCameraPermission(context)) {
                    CheckPermission.requestCameraPermission((Activity) context, CheckPermission.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                } else {
                    image = System.currentTimeMillis() + "_photo.png";
                    takePicture(ChatsScreenFrgActivity.this, image);
                }
                break;
            case R.id.ivRight:
                binding.llAttachmentlayout.llAttachment.setVisibility(View.GONE);

                invalidateOptionsMenu();
                // avatarPopUp().showAsDropDown(binding.toolbar);

                showMenuDOt(view);
                //   initiatePopupWindow().showAsDropDown(binding.toolbar);
                break;
            case R.id.ivLeft:
                binding.llAttachmentlayout.llAttachment.setVisibility(View.GONE);

                finish();
                break;
            case R.id.tvHeader:
                binding.llAttachmentlayout.llAttachment.setVisibility(View.GONE);

                startActivity(new Intent(context, GroupInfoActivity.class).
                        putExtra(AppConstants.ASSOC_ID, chatUserId).
                        putExtra(AppConstants.NAME, chatName).
                        putExtra(AppConstants.IS_BROAD_CAST, isBroadCast).
                        putExtra(AppConstants.IS_GROUP, chatIsGroup).
                        putExtra(AppConstants.IS_MUTE, isMute).
                        putExtra(AppConstants.USER_IMAGE, userImage).
                        putExtra(AppConstants.ROOM_ID, roomId).
                        putExtra(AppConstants.IS_BLOCKED, isBlocked)
                );
                break;
            case R.id.ivRightSecond:
                binding.llAttachmentlayout.llAttachment.setVisibility(View.GONE);

                initiatePopupWindow().showAsDropDown(binding.toolbar);
                //   initiatePopupWindow().showAsDropDown(binding.toolbar);
                break;
            case R.id.ivChatLeftIcon:
                binding.llAttachmentlayout.llAttachment.setVisibility(View.GONE);

                if (binding.rvGif.isShown()) {
                    binding.rvGif.setVisibility(View.GONE);
                } else {
                    binding.rvGif.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.ivChatRightIcon:

                if (getIntent() != null && getIntent().hasExtra("direct")) {
                    openGallery();
                } else {


                    if (flag == 0) {
                        binding.llAttachmentlayout.llAttachment.startAnimation(slideUpAnimation);


                        binding.llAttachmentlayout.llAttachment.setVisibility(View.VISIBLE);
                        flag = 1;
                        //  attachmentDialog();
                    } else {
                        binding.llAttachmentlayout.llAttachment.startAnimation(slideDownAnimation);

                        binding.llAttachmentlayout.llAttachment.setVisibility(View.GONE);
                        flag = 0;

                    }
                }

                break;
            case R.id.ivAudio:
                binding.llAttachmentlayout.llAttachment.setVisibility(View.GONE);

                if (attachementDialog != null) {
                    attachementDialog.dismiss();
                }

                break;
            case R.id.ivDocument:
                if (attachementDialog != null) {
                    attachementDialog.dismiss();
                }
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                Intent i = Intent.createChooser(intent, "File");
                startActivityForResult(i, CHOOSE_FILE_REQUESTCODE);
                break;
            case R.id.ivCamera:
                binding.llAttachmentlayout.llAttachment.setVisibility(View.GONE);

                if (attachementDialog != null) {
                    attachementDialog.dismiss();
                }
                if (!CheckPermission.checkCameraPermission(context)) {
                    CheckPermission.requestCameraPermission((Activity) context, CheckPermission.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                } else {
                    image = System.currentTimeMillis() + "_photo.png";
                    takePicture((Activity) context, image);
                }
                break;
            case R.id.ivGallery:
                binding.llAttachmentlayout.llAttachment.setVisibility(View.GONE);

                if (attachementDialog != null) {
                    attachementDialog.dismiss();
                }
                if (!CheckPermission.checkCameraPermission(context)) {
                    CheckPermission.requestCameraPermission((Activity) context, CheckPermission.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                } else {
                    openGallery();
                }
                break;
            case R.id.ivLocation:
//                ToastUtils.showToastShort(context, "Work in progress");
                if (attachementDialog != null) {
                    attachementDialog.dismiss();
                }
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(ChatsScreenFrgActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ivContact:
                if (attachementDialog != null) {
                    attachementDialog.dismiss();
                }
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                startActivityForResult(intent, RQS_PICK_CONTACT);
                break;

            case R.id.llChatBar:
                binding.llAttachmentlayout.llAttachment.setVisibility(View.GONE);
                break;


            default:

                break;

        }
    }


    private void setGirdRecylerView() {
        final GridLayoutManager linearLayoutManager = new GridLayoutManager(context, 3);
        binding.rvGif.setLayoutManager(linearLayoutManager);
       /* if (PrefManager.getInstance(context).getCurrentUser().getAvatar_img() != null && PrefManager.getInstance(context).getCurrentUser().getAvatar_img().getAvatar_image() != null && PrefManager.getInstance(context).getCurrentUser().getAvatar_img().getAvatar_image().length() > 0) {
            arlGifs.add(PrefManager.getInstance(context).getCurrentUser().getAvatar_img().getAvatar_image());
            arlGifs.add(PrefManager.getInstance(context).getCurrentUser().getAvatar_img().getAvatar_image());
            arlGifs.add(PrefManager.getInstance(context).getCurrentUser().getAvatar_img().getAvatar_image());
            arlGifs.add(PrefManager.getInstance(context).getCurrentUser().getAvatar_img().getAvatar_image());
            arlGifs.add(PrefManager.getInstance(context).getCurrentUser().getAvatar_img().getAvatar_image());
            arlGifs.add(PrefManager.getInstance(context).getCurrentUser().getAvatar_img().getAvatar_image());
            arlGifs.add(PrefManager.getInstance(context).getCurrentUser().getAvatar_img().getAvatar_image());
            arlGifs.add(PrefManager.getInstance(context).getCurrentUser().getAvatar_img().getAvatar_image());
        }*/
        adapter = new GridAdapter(context, arlGifs, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
              /*  chatScreenModel = new GetChatResonse.UserInfoBean();
                chatScreenModel.setBody(arlGifs.get(position));
                chatScreenModel.setCreated_at(String.valueOf(System.currentTimeMillis() / 1000));
                chatScreenModel.setRoom_id(roomId);
                chatScreenModel.setCreated_timestamp("");
                chatScreenModel.setId("");
                chatScreenModel.setIs_user_sender(true);
                chatScreenModel.setIs_stared(false);
                chatScreenModel.setLocal_message_id("");
                chatScreenModel.setRead_status(false);
                chatScreenModel.setReceiver_image("");
                chatScreenModel.setSender_image(new PrefManager(context).getUserPic());
                chatScreenModel.setSender_id(new PrefManager(context).getUserId());
                chatScreenModel.setUpload_type("image");
                chatScreenModel.setThumbnail("");
                sendMessage(chatScreenModel);*/
                //      chatScreenAdapter.setDataChange(groupDataIntoHashMap(chatScreenModels));
                binding.layoutCustomChatMenuFrg.etChatMessage.setText("");
                binding.rvChatListFrg.post(new Runnable() {
                    @Override
                    public void run() {
                        // Call smooth scroll
                        binding.rvChatListFrg.smoothScrollToPosition(chatScreenAdapter.getItemCount());
                    }
                });
            }
        });
        binding.rvGif.setAdapter(adapter);
    }


    private void deleteDialog() {
        DeleteDialogBinding deleteDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.delete_dialog, null, false);
        dialog = DialogUtils.createDialog(context, deleteDialogBinding.getRoot());
        deleteDialogBinding.llMain.setBackground(ContextCompat.getDrawable(context, Themes.getInstance(context).setRoundedBlackBackground()));
        deleteDialogBinding.tvDeleteForMe.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        deleteDialogBinding.tvDelete.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        deleteDialogBinding.tvDeleteEveryone.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));


        deleteDialogBinding.tvDeleteForMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* DeleteRequestModel deleteRequestModel = new DeleteRequestModel();
                deleteRequestModel.set_do("me");
                ArrayList<String> strings = new ArrayList<>();
                strings.add(chatScreenModels.get(getSeletedMessage(chatScreenModels)).getId());
                deleteRequestModel.setMessage_ids(strings);
                deleteRequestModel.setRoom_id(roomId);
                deleteMessageapi(deleteRequestModel, chatScreenModels, getSeletedMessage(chatScreenModels));
             */
                binding.layoutChatClickItem.getRoot().setVisibility(View.GONE);

            }
        });

        deleteDialogBinding.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        deleteDialogBinding.tvDeleteEveryone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  DeleteRequestModel deleteRequestModel = new DeleteRequestModel();
                deleteRequestModel.set_do("everyone");
                ArrayList<String> strings = new ArrayList<>();
                strings.add(chatScreenModels.get(getSeletedMessage(chatScreenModels)).getId());
                deleteRequestModel.setMessage_ids(strings);
                deleteRequestModel.setRoom_id(roomId);
                deleteMessageapi(deleteRequestModel, chatScreenModels, getSeletedMessage(chatScreenModels));
         */
                binding.layoutChatClickItem.getRoot().setVisibility(View.GONE);
                dialog.dismiss();
            }
        });
        deleteDialogBinding.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.background_dark);


    }



   /* private void deletePopup() {
        AlertDialog.Builder builder;
        if (CommonUtils.getPreferencesBoolean(context,AppConstants.DARK_THEME)) {

            builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom));
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle(R.string.delete_msg);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DeleteRequestModel deleteRequestModel = new DeleteRequestModel();
                deleteRequestModel.set_do("me");
                ArrayList<String> strings = new ArrayList<>();
                strings.add(chatScreenModels.get(getSeletedMessage(chatScreenModels)).getId());
                deleteRequestModel.setMessage_ids(strings);
                deleteRequestModel.setRoom_id(roomId);
                deleteMessageapi(deleteRequestModel, chatScreenModels, getSeletedMessage(chatScreenModels));
                binding.layoutChatClickItem.getRoot().setVisibility(View.GONE);
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }*/

    private void shareTextUrl(String url) throws Exception {
        Uri imageUri = CommonUtils.getLocalBitmapUri(url, context);
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, "Yellow seed");
        share.putExtra(Intent.EXTRA_TEXT, "Yellow seed");
        share.putExtra(Intent.EXTRA_STREAM, imageUri);
        share.setType("image/jpeg");
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(Intent.createChooser(share, "Share link!"), 101);
    }

    private int getSeletedMessage(ArrayList<GetChatResonse.UserInfoBean> chatScreenModels) {
        for (int i = 0; i < chatScreenModels.size(); i++) {
            if (chatScreenModels.get(i).isSelected()) {
                return i;
            }
        }
        return 0;
    }

    private void addPhotoDialog() {
        final CharSequence[] items = {getString(R.string.take_a_photo), getString(R.string.from_gallery)};
        new MaterialDialog.Builder(context)
                .title(R.string.choose_photo).titleGravity(GravityEnum.CENTER)
                .items(items)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {

                        if (items[position].equals(getString(R.string.take_a_photo))) {
                            image = System.currentTimeMillis() + "_photo.png";
                            takePicture((Activity) context, image);

                        } else if (items[position].equals(getString(R.string.from_gallery))) {
                            image = System.currentTimeMillis() + "_photo.png";

                            openGallery();
                        } else {
                            dialog.dismiss();
                        }


                    }
                }).negativeText(R.string.cancel).onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        }).show();
    }

    public void openGallery() {

        if (Build.VERSION.SDK_INT < 19) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/* video/*");
            startActivityForResult(Intent.createChooser(intent, "Choose File"), com.yellowseed.imageUtils.TakePictureUtils.PICK_GALLERY);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*", "video/*"});
            startActivityForResult(intent, com.yellowseed.imageUtils.TakePictureUtils.PICK_GALLERY);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == com.yellowseed.imageUtils.TakePictureUtils.PICK_GALLERY) {
            if (resultCode == Activity.RESULT_OK) {
                if (intent.getData() != null && intent.getData().toString().contains("video")) {
                    video = String.valueOf(System.currentTimeMillis() / 1000);
                    try {
                        InputStream inputStream = context.getContentResolver().openInputStream(intent.getData());
                        FileOutputStream fileOutputStream = new FileOutputStream(new File(this.context.getExternalFilesDir("temp"), video + ".mp4"));
                        com.yellowseed.imageUtils.TakePictureUtils.copyStream(inputStream, fileOutputStream);
                        fileOutputStream.close();
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    File imageFile = new File(context.getExternalFilesDir("temp"), video + ".mp4");
                    // appVideo(imageFile);
                } else {
                    try {
                        InputStream inputStream = context.getContentResolver().openInputStream(intent.getData());
                        FileOutputStream fileOutputStream = new FileOutputStream(new File(this.context.getExternalFilesDir("temp"), image + ".jpg"));
                        com.yellowseed.imageUtils.TakePictureUtils.copyStream(inputStream, fileOutputStream);
                        fileOutputStream.close();
                        inputStream.close();
                        startCropImage(this, image + ".jpg");
                    } catch (Exception e) {

                    }
                }

            }
        } else if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
                startCropImage(this, image + ".jpg");
            }
        } else if (requestCode == CHOOSE_FILE_REQUESTCODE) {
            if (resultCode == Activity.RESULT_OK) {
                String document = String.valueOf(System.currentTimeMillis() / 1000);
                try {
                    InputStream inputStream = context.getContentResolver().openInputStream(intent.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(new File(this.context.getExternalFilesDir("temp"), video + ".pdf"));
                    com.yellowseed.imageUtils.TakePictureUtils.copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                File imageFile = new File(context.getExternalFilesDir("temp"), document + ".pdf");
                //  appDocument(imageFile);
            }
        } else if (requestCode == CROP_FROM_CAMERA) {
            //  imageName="picture";
            if (resultCode == Activity.RESULT_OK) {
                if (intent != null) {
                    path = intent.getStringExtra(CropImage.IMAGE_PATH);
                    File imageFile = new File(path);
                    if (imageFile.exists()) {
                        //  appImage(imageFile);
                    }
                }
                if (path == null) {
                    return;
                }
            }
        } else if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(intent, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();

                LocationModel locationModel = new LocationModel();
                locationModel.setName(place.getName().toString());
                locationModel.setImage(CommonUtils.local(String.valueOf(place.getLatLng().latitude), String.valueOf(place.getLatLng().longitude)));
                locationModel.setLatitude(place.getLatLng().latitude);
                locationModel.setLongitude(place.getLatLng().longitude);

              /*  chatScreenModel = new GetChatResonse.UserInfoBean();
                chatScreenModel.setBody(new Gson().toJson(locationModel));
                chatScreenModel.setCreated_at(String.valueOf(System.currentTimeMillis() / 1000));
                chatScreenModel.setRoom_id(roomId);
                chatScreenModel.setCreated_timestamp("");
                chatScreenModel.setId("");
                chatScreenModel.setIs_user_sender(true);
                chatScreenModel.setIs_stared(false);
                chatScreenModel.setLocal_message_id("");
                chatScreenModel.setRead_status(false);
                chatScreenModel.setReceiver_image("");
                chatScreenModel.setSender_image(new PrefManager(context).getUserPic());
                chatScreenModel.setSender_id(new PrefManager(context).getUserId());
                chatScreenModel.setUpload_type("location");
                chatScreenModel.setThumbnail("");
                sendMessage(chatScreenModel);
*/

                //      chatScreenAdapter.setDataChange(groupDataIntoHashMap(chatScreenModels));
                binding.layoutCustomChatMenuFrg.etChatMessage.setText("");
                binding.rvChatListFrg.post(new Runnable() {
                    @Override
                    public void run() {
                        // Call smooth scroll
                        binding.rvChatListFrg.smoothScrollToPosition(chatScreenAdapter.getItemCount());
                    }
                });

            }
        } else if (requestCode == RQS_PICK_CONTACT) {
            if (resultCode == RESULT_OK) {
                Uri contactData = intent.getData();
                Cursor cursor = managedQuery(contactData, null, null, null, null);
                cursor.moveToFirst();

                String number = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                ContactModel contactModel = new ContactModel();
                contactModel.setName(name);
                contactModel.setNumber(number);
/*
                chatScreenModel = new GetChatResonse.UserInfoBean();
                chatScreenModel.setBody(new Gson().toJson(contactModel));
                chatScreenModel.setCreated_at(String.valueOf(System.currentTimeMillis() / 1000));
                chatScreenModel.setRoom_id(roomId);
                chatScreenModel.setCreated_timestamp("");
                chatScreenModel.setId("");
                chatScreenModel.setIs_user_sender(true);
                chatScreenModel.setIs_stared(false);
                chatScreenModel.setLocal_message_id("");
                chatScreenModel.setRead_status(false);
                chatScreenModel.setReceiver_image("");
                chatScreenModel.setSender_image(new PrefManager(context).getUserPic());
                chatScreenModel.setSender_id(new PrefManager(context).getUserId());
                chatScreenModel.setUpload_type("contact");
                chatScreenModel.setThumbnail("");
                chatScreenModels.add(chatScreenModel);*/
                //    chatScreenAdapter.setDataChange(groupDataIntoHashMap(chatScreenModels));
                ;
                binding.layoutCustomChatMenuFrg.etChatMessage.setText("");
              /*  RoomChatTable roomChatTable = new RoomChatTable(context);
                roomChatTable.insertSingleRoomInformation(chatScreenModel, roomId);
                roomChatTable.closeDB();*/
                binding.rvChatListFrg.post(new Runnable() {
                    @Override
                    public void run() {
                        // Call smooth scroll
                        binding.rvChatListFrg.smoothScrollToPosition(chatScreenAdapter.getItemCount());
                    }
                });
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CheckPermission.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    //addPhotoDialog();
                } else {
                    CommonUtils.showToast(context, "Permission denial");
                }
                break;
        }
    }

    /**
     * this method is used for open crop image
     */
    public void startCropImage(Activity context, String fileName) {
        Intent intent = new Intent(context, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, new File(context.getExternalFilesDir("temp"), fileName).getPath());
        intent.putExtra(CropImage.SCALE, true);
        intent.putExtra(CropImage.ASPECT_X, 1);
        intent.putExtra(CropImage.ASPECT_Y, 1);
        intent.putExtra(CropImage.OUTPUT_X, 600);
        intent.putExtra(CropImage.OUTPUT_Y, 600);
        startActivityForResult(intent, CROP_FROM_CAMERA);
    }

    private void attachmentDialog() {

        LayoutattachementsiconsBinding layoutattachementsiconsBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layoutattachementsicons, null, false);
        attachementDialog = DialogUtils.createDialog(context, layoutattachementsiconsBinding.getRoot());


        Window window = attachementDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);


        WindowManager.LayoutParams params = attachementDialog.getWindow().getAttributes(); // change this to your dialog.

        //params.x = 10; // left margin
        params.y = 100;
        attachementDialog.getWindow().setAttributes(params);


        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            layoutattachementsiconsBinding.ivDocument.setImageResource(R.mipmap.doc);
        } else {
            layoutattachementsiconsBinding.ivDocument.setImageResource(R.mipmap.document_icon);
        }
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            layoutattachementsiconsBinding.ivCamera.setImageResource(R.mipmap.camera);
        } else {
            layoutattachementsiconsBinding.ivCamera.setImageResource(R.mipmap.camera_large_icon);
        }
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            layoutattachementsiconsBinding.ivGallery.setImageResource(R.mipmap.gallery);
        } else {
            layoutattachementsiconsBinding.ivGallery.setImageResource(R.mipmap.galler);
        }
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            layoutattachementsiconsBinding.ivLocation.setImageResource(R.mipmap.location);
        } else {
            layoutattachementsiconsBinding.ivLocation.setImageResource(R.mipmap.location_ico_large);
        }
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            layoutattachementsiconsBinding.ivAudio.setImageResource(R.mipmap.location);
        } else {
            layoutattachementsiconsBinding.ivAudio.setImageResource(R.mipmap.location_ico_large);
        }
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            layoutattachementsiconsBinding.ivContact.setImageResource(R.mipmap.contact);

        } else {
            layoutattachementsiconsBinding.ivContact.setImageResource(R.mipmap.contact_icon);
        }
        layoutattachementsiconsBinding.llAttachment.setBackground(ContextCompat.getDrawable(context, Themes.getInstance(context).setRoundedBlackBackground()));
        layoutattachementsiconsBinding.btnAttachementsDialog.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        layoutattachementsiconsBinding.tvattachDocuments.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        layoutattachementsiconsBinding.tvattachCamera.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        layoutattachementsiconsBinding.tvAttachGallery.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        layoutattachementsiconsBinding.tvAttachLocation.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        layoutattachementsiconsBinding.tvAttachContacts.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        layoutattachementsiconsBinding.tvAudio.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));

        layoutattachementsiconsBinding.ivDocument.setOnClickListener(this);
        layoutattachementsiconsBinding.ivAudio.setOnClickListener(this);
        layoutattachementsiconsBinding.ivCamera.setOnClickListener(this);
        layoutattachementsiconsBinding.ivGallery.setOnClickListener(this);
        layoutattachementsiconsBinding.ivLocation.setOnClickListener(this);
        layoutattachementsiconsBinding.ivContact.setOnClickListener(this);
    }

    private void showMenuDOt(View view) {
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
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.menu_chat_frg);
        popup.show();

    }

   /* private void showMenuDOt(View view) {
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
        popup.setOnMenuItemClickListener(this);

        if (roomsBean.isIs_enable_avatar()) {
            popup.inflate(R.menu.menu_chat_frg_disable);
        } else {
            popup.inflate(R.menu.menu_chat_frg);
        }
        popup.show();

    }*/


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionVoiceCall:
                if (CommonUtils.checkPermissionCamera((Activity) context)) {
                    if (userType.equalsIgnoreCase("group")) {
                        // groupCallingUserList(groupMemberResponseList, "Audio");
                        ToastUtils.showToastShort(context, "Voice call");
                        //  callGroupMemberListAPI(true, "Audio");
                    } else {
                        if (qbId != null && qbId.length() > 0) {
                            audioCall(false, null);
                        }
                    }
                } else {
                    CommonUtils.requestPermissionCamera((Activity) context);
                    CommonUtils.requestPermissionCallPhone((Activity) context);
                }
                return true;
            case R.id.actionVideoCall:
                if (CommonUtils.checkPermissionCamera((Activity) context)) {
                    if (userType.equalsIgnoreCase("group")) {
                        callGroupMemberListAPI(true, "Video");

                        //groupCallingUserList(groupMemberResponseList, "Video");
                    } else {
                        if (qbId != null && qbId.length() > 0) {
                            videoCall(false, null);
                        }
                    }
                } else {
                    CommonUtils.requestPermissionCamera((Activity) context);
                    CommonUtils.requestPermissionCallPhone((Activity) context);
                }

            case R.id.actionEnableAvatarFrggg:
                if (roomsBean != null) {
                    RoomsTable roomsTable = new RoomsTable(context);
                    if (roomsBean.isIs_enable_avatar()) {
                        roomsBean.setIs_enable_avatar(false);
                        roomsTable.updateAvtarEnableStatus(false, roomsBean.getRoom_id());
                    } else {
                        roomsBean.setIs_enable_avatar(true);
                        roomsTable.updateAvtarEnableStatus(true, roomsBean.getRoom_id());
                    }
                    roomsTable.closeDB();
                }


                return true;
            case R.id.actionEnableAvatarFrggg_disable:
                if (roomsBean != null) {
                    RoomsTable roomsTable = new RoomsTable(context);
                    if (roomsBean.isIs_enable_avatar()) {
                        roomsBean.setIs_enable_avatar(false);
                        roomsTable.updateAvtarEnableStatus(false, roomsBean.getRoom_id());
                    } else {
                        roomsBean.setIs_enable_avatar(true);
                        roomsTable.updateAvtarEnableStatus(true, roomsBean.getRoom_id());
                    }
                    roomsTable.closeDB();
                }


                return true;
            default:
                return false;
        }
    }

    private void audioCall(boolean isGroup, List<GroupMemberResonse.MembersBean> qBids) {
        if (CommonUtils.checkPermissionCamera((Activity) context)) {
            try {



               /* Collection<QBUser> selectedUsers = new ArrayList<>();
                QBUser qbUser = new QBUser();
                qbUser.setFullName(chatName);
                qbUser.setId(Integer.valueOf(qbId));
                selectedUsers.add(qbUser);

                ArrayList<Integer> opponentsList = CollectionsUtils.getIdsSelectedOpponents(selectedUsers);*/
                ArrayList<Integer> opponentsList = new ArrayList<>();
                List<String> arl = new ArrayList<>();
                if (isGroup) {
                    for (int i = 0; i < qBids.size(); i++) {
                        arl.add(qBids.get(i).getId());
                        opponentsList.add(Integer.parseInt(qBids.get(i).getQb_id()));
                    }
                } else {
                    Collection<QBUser> selectedUsers = new ArrayList<>();
                    QBUser qbUser = new QBUser();
                    qbUser.setFullName(chatName);
                    qbUser.setId(Integer.valueOf(qbId));
                    selectedUsers.add(qbUser);
                    opponentsList = CollectionsUtils.getIdsSelectedOpponents(selectedUsers);
                }


                QBRTCTypes.QBConferenceType conferenceType = false
                        ? QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO
                        : QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_AUDIO;
                QBRTCClient qbrtcClient = QBRTCClient.getInstance(getApplicationContext());
                QBRTCSession newQbRtcSession = qbrtcClient.createNewSessionWithOpponents(opponentsList, conferenceType);

                VideoCallSessionManager.getInstance(ChatsScreenFrgActivity.this).setCurrentSession(newQbRtcSession);
                Map<String, String> userInfo = new HashMap<>();
                userInfo.put("call", "audio");
                userInfo.put("type", "normal");
                newQbRtcSession.startCall(userInfo);
                CallActivity.start(ChatsScreenFrgActivity.this, false, chatName);
                Log.d(TAG, "conferenceType = " + conferenceType);
                PushModel pushModel = new PushModel();
                pushModel.setMessage(PrefManager.getInstance(context).getCurrentUser().getName() + " is calling");
               /* List<String> arl = new ArrayList<>();
                arl.add(chatUserId);*/
                if (!isGroup) {
                    arl.add(chatUserId);
                }
                pushModel.setUsers(arl);
                sendPushService(pushModel);
                // dialogType = "clr";
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        } else {
            CommonUtils.requestPermissionCamera((Activity) context);
            CommonUtils.requestPermissionCallPhone((Activity) context);
        }
    }

    private void videoCall(boolean isGroup, List<GroupMemberResonse.MembersBean> qBids) {
        if (CommonUtils.checkPermissionCamera((Activity) context)) {
            ArrayList<Integer> opponentsList = new ArrayList<>();
            List<String> arl = new ArrayList<>();
            if (isGroup) {
                for (int i = 0; i < qBids.size(); i++) {
                    arl.add(qBids.get(i).getId());
                    opponentsList.add(Integer.parseInt(qBids.get(i).getQb_id()));
                }
            } else {
                Collection<QBUser> selectedUsers = new ArrayList<>();
                QBUser qbUser = new QBUser();
                qbUser.setFullName(chatName);
                qbUser.setId(Integer.valueOf(qbId));
                selectedUsers.add(qbUser);
                opponentsList = CollectionsUtils.getIdsSelectedOpponents(selectedUsers);
            }
            try {
                QBRTCTypes.QBConferenceType conferenceType = true
                        ? QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO
                        : QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_AUDIO;
                QBRTCClient qbrtcClient = QBRTCClient.getInstance(getApplicationContext());
                QBRTCSession newQbRtcSession = qbrtcClient.createNewSessionWithOpponents(opponentsList, conferenceType);
                VideoCallSessionManager.getInstance(ChatsScreenFrgActivity.this).setCurrentSession(newQbRtcSession);

                Map<String, String> userInfo = new HashMap<>();
                userInfo.put("call", "video");
                userInfo.put("type", "normal");
                newQbRtcSession.startCall(userInfo);

                CallActivity.start(ChatsScreenFrgActivity.this, false, chatName);
                Log.d(TAG, "conferenceType = " + conferenceType);
                PushModel pushModel = new PushModel();
                pushModel.setMessage(PrefManager.getInstance(context).getCurrentUser().getName() + " is calling");
                if (!isGroup) {
                    arl.add(chatUserId);
                }
                pushModel.setUsers(arl);
                sendPushService(pushModel);
                // dialogType = "clr";
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        } else {
            CommonUtils.requestPermissionCamera((Activity) context);
            CommonUtils.requestPermissionCallPhone((Activity) context);
        }
    }

    /**
     * method to call group member api
     */
    private void callGroupMemberListAPI(final boolean isShowProgress, final String callingType) {
        if (CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            if (isShowProgress) {
                progressDialog.show();
            }

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("group_id", chatUserId);
            jsonObject.addProperty("do", "group");

            Call<GroupMemberResonse> call = ApiExecutor.getClient(context).apiGroupMemberList(jsonObject);
            call.enqueue(new Callback<GroupMemberResonse>() {
                @Override
                public void onResponse(@NonNull Call<GroupMemberResonse> call, @NonNull final Response<GroupMemberResonse> response) {
                    if (isShowProgress) {
                        progressDialog.dismiss();
                    }
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            if (response.body().getResponseMessage() != null) {
                                models.clear();
                                GroupMemberResonse.MembersBean userModel = null;
                                if (response.body().getMembers() != null && response.body().getMembers().size() > 0) {

                                    for (int i = 0; i < response.body().getMembers().size(); i++) {
                                        userModel = new GroupMemberResonse.MembersBean();
                                        if (!response.body().getMembers().get(i).getId().equalsIgnoreCase(new PrefManager(context).getUserId())) {
                                            userModel.setEmail(response.body().getMembers().get(i).getEmail());
                                            userModel.setId(response.body().getMembers().get(i).getId());
                                            userModel.setName(response.body().getMembers().get(i).getName());
                                            userModel.setQb_id(response.body().getMembers().get(i).getQb_id());
                                            models.add(userModel);
                                        }


                                    }


                                    groupCallingUserList(models, callingType);

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
                public void onFailure(@NonNull Call<GroupMemberResonse> call, @NonNull Throwable t) {
                    if (isShowProgress) {
                        progressDialog.dismiss();
                    }
                    t.printStackTrace();
                    ToastUtils.showToastShort(context, context.getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(context, context.getString(R.string.internet_alert_msg));
        }
    }

    private void groupCallingUserList(final List<GroupMemberResonse.MembersBean> models, final String type) {
        final LayoutGroupuserListBinding repostBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_groupuser_list, null, false);
        final Dialog dialog = DialogUtils.createDialog(context, repostBinding.getRoot());
        repostBinding.viewLine.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        repostBinding.llGroup.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkChatBox()));

        // repostBinding.llGroupMember.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkChatBox()));
        repostBinding.tvDone.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        repostBinding.etCaption.setBackground(ContextCompat.getDrawable(context, Themes.getInstance(context).setEditTextBackground()));
        repostBinding.tvDone.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setLightTheme()));
        Themes.getInstance(context).changePostIconColor(context, repostBinding.ivSendRepost);
        Themes.getInstance(context).changePostIconColor(context, repostBinding.ivCancelRepost);

        repostBinding.ivCancelRepost.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        repostBinding.tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //Setting the recycler view inside the dialog

        repostBinding.rvRepostHome.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        repostBinding.rvRepostHome.setLayoutManager(linearLayoutManager);
        videoCallingUserListAdapter = new VideoCallingUserListAdapter(context, models, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (models.get(position).isSelected()) {
                    models.get(position).setSelected(false);
                } else {
                    models.get(position).setSelected(true);
                }
                videoCallingUserListAdapter.notifyItemChanged(position);
            }
        });
        repostBinding.tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equalsIgnoreCase("Video")) {

                    videoCall(true, videoCallingUserListAdapter.getQBids());
                } else {
                    audioCall(true, videoCallingUserListAdapter.getQBids());
                }
                dialog.dismiss();
            }
        });
        repostBinding.rvRepostHome.setAdapter(videoCallingUserListAdapter);
    }

    private void sendPushService(PushModel pushModel) {
        if (CommonUtils.isOnline(context)) {
            Call<ApiResponse> call = ApiExecutor.getClient(context).apiCallNotification(pushModel);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                }
            });
        } else {
            ToastUtils.showToastShort(context, context.getString(R.string.internet_alert_msg));
        }
    }

    /**
     * method for create story api
     */
  /*  private void callGetChatAPI() {
        if (CommonUtils.isOnline(context)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("assoc_id", chatUserId);
            jsonObject.addProperty("sender_id", new PrefManager(context).getUserId());
            Call<GetChatResonse> call = ApiExecutor.getClient(context).apiGetChat(jsonObject);
            call.enqueue(new Callback<GetChatResonse>() {
                @Override
                public void onResponse(@NonNull Call<GetChatResonse> call, @NonNull final Response<GetChatResonse> response) {

                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            if (response.body().getRoom_id() != null) {
                                roomId = response.body().getRoom_id();
                                RoomChatTable roomChatTable = new RoomChatTable(context);
                                roomChatTable.deleteRoomChat(roomId);
                                roomChatTable.insertRoomInformation(response.body().getUser_info(), roomId);
                                roomChatTable.closeDB();
                                RoomChatTable roomChatTable1 = new RoomChatTable(context);
                                chatScreenModels.clear();
                                chatScreenModels.addAll(roomChatTable1.getChatPerRoom(roomId));
                        //        chatScreenAdapter.setDataChange(groupDataIntoHashMap(chatScreenModels));
                                binding.rvChatListFrg.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        binding.rvChatListFrg.smoothScrollToPosition(chatScreenAdapter.getItemCount());
                                    }
                                });
                                forwardMessage();
                                if (chatScreenModels != null && chatScreenModels.size() >= 1) {
                                    if (userType.equalsIgnoreCase("single")) {
                                        sendReadStatus(chatScreenModels.get(chatScreenModels.size() - 1).getId());
                                    }
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
                public void onFailure(@NonNull Call<GetChatResonse> call, @NonNull Throwable t) {

                    t.printStackTrace();
                    ToastUtils.showToastShort(context, context.getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(context, context.getString(R.string.internet_alert_msg));
        }
    }

    private void callBroadCastMessage() {
        if (CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("assoc_id", chatUserId);
            jsonObject.addProperty("sender_id", new PrefManager(context).getUserId());
            Call<GetChatResonse> call = ApiExecutor.getClient(context).apiGetBroadCast(jsonObject);
            call.enqueue(new Callback<GetChatResonse>() {
                @Override
                public void onResponse(@NonNull Call<GetChatResonse> call, @NonNull final Response<GetChatResonse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            if (response.body().getRoom_id() != null) {
                                roomId = response.body().getRoom_id();
                                roomIds.clear();
                                roomIds.addAll(response.body().getRoom_ids());
                                chatScreenModels.clear();
                                RoomChatTable roomChatTable = new RoomChatTable(context);
                                chatScreenModels.addAll(roomChatTable.getChatPerRoom(roomId));
                                roomChatTable.closeDB();
                         //       chatScreenAdapter.setDataChange(groupDataIntoHashMap(chatScreenModels));
                                ;
                                binding.rvChatListFrg.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        binding.rvChatListFrg.smoothScrollToPosition(chatScreenAdapter.getItemCount());
                                    }
                                });
                                forwardMessage();
                            }
                        } else {
                            ToastUtils.showToastShort(context, context.getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GetChatResonse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(context, context.getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(context, context.getString(R.string.internet_alert_msg));
        }
    }


    */

    /**
     * method for create story api
     *//*
    private void callGetGroupChatAPI() {
        if (CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("assoc_id", chatUserId);
            jsonObject.addProperty("sender_id", new PrefManager(context).getUserId());
            Call<GetChatResonse> call = ApiExecutor.getClient(context).apiGetGroupChat(jsonObject);
            call.enqueue(new Callback<GetChatResonse>() {
                @Override
                public void onResponse(@NonNull Call<GetChatResonse> call, @NonNull final Response<GetChatResonse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            if (response.body().getRoom_id() != null) {
                                roomId = response.body().getRoom_id();
                                Collections.sort(chatScreenModels, new Comparator<GetChatResonse.UserInfoBean>() {
                                    public int compare(GetChatResonse.UserInfoBean o1, GetChatResonse.UserInfoBean o2) {
                                        return o1.getCreated_at().compareTo(o2.getCreated_at());
                                    }
                                });
                                RoomChatTable roomChatTable = new RoomChatTable(context);
                                roomChatTable.deleteRoomChat(roomId);
                                roomChatTable.insertRoomInformation(response.body().getUser_info(), roomId);
                                roomChatTable.closeDB();
                                if (response.body().getGroup_members() != null && response.body().getGroup_members().size() > 0) {
                                    groupMemberResponseList.addAll(response.body().getGroup_members());
                                }
                                RoomChatTable roomChatTable1 = new RoomChatTable(context);
                                chatScreenModels.clear();
                                chatScreenModels.addAll(roomChatTable1.getChatPerRoom(roomId));
                     //           chatScreenAdapter.setDataChange(groupDataIntoHashMap(chatScreenModels));

                                forwardMessage();
                                binding.rvChatListFrg.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Call smooth scroll
                                        binding.rvChatListFrg.smoothScrollToPosition(chatScreenAdapter.getItemCount());
                                    }
                                });
                            }
                        } else {
                            ToastUtils.showToastShort(context, context.getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GetChatResonse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(context, context.getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(context, context.getString(R.string.internet_alert_msg));
        }
    }*/
    private void staredUnstaredMessageapi(final List<GetChatResonse.UserInfoBean> beanList, final int position) {
        if (CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("message_id", beanList.get(position).getId());
            if (beanList.get(position).getIs_stared()) {
                jsonObject.addProperty("stared", false);
            } else {
                jsonObject.addProperty("stared", true);
            }
            Call<GetChatResonse> call = ApiExecutor.getClient(context).apiStarMessage(jsonObject);
            call.enqueue(new Callback<GetChatResonse>() {
                @Override
                public void onResponse(@NonNull Call<GetChatResonse> call, @NonNull final Response<GetChatResonse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            if (beanList.get(position).getIs_stared()) {
                                beanList.get(position).setIs_stared(false);
                            } else {
                                beanList.get(position).setIs_stared(true);
                            }
                            beanList.get(position).setSelected(false);
                            binding.layoutChatClickItem.getRoot().setVisibility(View.GONE);
                            //     chatScreenAdapter.setDataChange(groupDataIntoHashMap(beanList));
                        } else {
                            ToastUtils.showToastShort(context, context.getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GetChatResonse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(context, context.getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(context, context.getString(R.string.internet_alert_msg));
        }
    }

    private void deleteMessageapi(DeleteRequestModel requestModel, final ArrayList<GetChatResonse.UserInfoBean> chatScreenModels, final int seletedMessage) {
        if (CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();
            Call<GetChatResonse> call = ApiExecutor.getClient(context).apiDeleteMessage(requestModel);
            call.enqueue(new Callback<GetChatResonse>() {
                @Override
                public void onResponse(@NonNull Call<GetChatResonse> call, @NonNull final Response<GetChatResonse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            chatScreenModels.remove(seletedMessage);
                            //       chatScreenAdapter.setDataChange(groupDataIntoHashMap(chatScreenModels));

                        } else {
                            ToastUtils.showToastShort(context, context.getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GetChatResonse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(context, context.getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(context, context.getString(R.string.internet_alert_msg));
        }
    }
/*
    private void createWebSocketClient() {
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

          *//*  @Override
            public void onTextReceived(final String message) {
                //  isSocketConneted = true;
                if (message != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (message != null && message.length() > 0) {
                                GetChatResonse.UserInfoBean chatScreenModel = new Gson().fromJson(message, GetChatResonse.UserInfoBean.class);

                                if (userType.equalsIgnoreCase("single")) {
                                    if (chatScreenModel.getOnline_status() != null && chatScreenModel.getOnline_status().equalsIgnoreCase("check_online_status")) {
                                        binding.tvLastSeen.setVisibility(View.VISIBLE);
                                        binding.tvLastSeen.setText("Online");
                                    } else {
                                        userOnlineStatus();
                                    }
                                }


                                if (chatScreenModel.getUpload_type() != null && chatScreenModel.getUpload_type().equalsIgnoreCase("Recieved")) {
                                    if (chatScreenModel.getRoom_id() != null && roomId != null && roomId.length() > 0 && chatScreenModel.getRoom_id().equalsIgnoreCase(roomId)) {
                                        RoomChatTable roomChatTable = new RoomChatTable(context);
                                        roomChatTable.updateReadStatus(chatScreenModel.getRoom_id());
                                        chatScreenModels.clear();
                                        chatScreenModels.addAll(roomChatTable.getChatPerRoom(chatScreenModel.getRoom_id()));
                       //                 chatScreenAdapter.setDataChange(groupDataIntoHashMap(chatScreenModels));
                                        roomChatTable.closeDB();
                                    }
                                } else {
                                    if (roomId != null && roomId.length() > 0 && chatScreenModel.getRoom_id().equalsIgnoreCase(roomId)) {
                                        RoomChatTable roomChatTable = new RoomChatTable(context);
                                        roomChatTable.insertSingleRoomInformation(chatScreenModel, roomId);
                                        roomChatTable.closeDB();
                                        chatScreenModels.add(chatScreenModel);
                         //               chatScreenAdapter.setDataChange(groupDataIntoHashMap(chatScreenModels));
                                        if (chatScreenModel.getSender_id() != null && !chatScreenModel.getSender_id().equalsIgnoreCase(PrefManager.getInstance(context).getUserId())) {
                                            if (userType.equalsIgnoreCase("single")) {
                                                sendReadStatus(chatScreenModel.getId());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    });
                }
            }*//*

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
    }*/

    private void sendReadStatus(String msg_id) {
        JsonObject object = new JsonObject();
        object.addProperty("read_by", "read_by_user");
        object.addProperty("room_id", roomId);
        object.addProperty("upload_type", "Recieved");
        object.addProperty("msg_id", msg_id);
        object.addProperty("assoc_id", chatUserId);
        if (webSocketClient != null)
            webSocketClient.send(new Gson().toJson(object).toString());
    }

    private void userOnlineStatus() {
        JsonObject object = new JsonObject();
        object.addProperty("online_status", "check_online_status");
        object.addProperty("id", new PrefManager(context).getUserId());
        object.addProperty("room_id", roomId);
        object.addProperty("assoc_id", chatUserId);
        object.addProperty("upload_type", "Recieved");

        if (webSocketClient != null)
            webSocketClient.send(new Gson().toJson(object).toString());
    }

    public void sendMessage(GetChatResonse.UserInfoBean chatScreenModel) {
        replyMessage = null;
        binding.layoutCustomChatMenuFrg.llReply.setVisibility(View.GONE);
        JsonObject object = new JsonObject();
        object.addProperty("local_message_id", "");
        object.addProperty("sender_id", new PrefManager(context).getUserId());
        object.addProperty("room_id", roomId);
        object.addProperty("is_group", "");
        object.addProperty("body", chatScreenModel.getBody());
        object.addProperty("assoc_id", chatUserId);
        object.addProperty("is_group", chatIsGroup);
        if (roomsBean != null && roomsBean.isIs_enable_avatar() && PrefManager.getInstance(context).getCurrentUser().getAvatar_img().getAvatar_image() != null && PrefManager.getInstance(context).getCurrentUser().getAvatar_img().getAvatar_image().length() > 0) {

            object.addProperty("avatar_image", PrefManager.getInstance(context).getCurrentUser().getAvatar_img().getAvatar_image());
        }
        if (isBroadCast) {
            object.addProperty("message_type", "broadcast");
            object.add("room_ids", new Gson().toJsonTree(roomIds).getAsJsonArray());
        } else {
            object.addProperty("message_type", "");
        }
        if (chatScreenModel.getReply_message() != null) {
            object.addProperty("reply_id", chatScreenModel.getReply_message().getMessage_id());
            JsonObject reply = new JsonObject();
            reply.addProperty("sender_id", chatScreenModel.getReply_message().getSender_id());
            reply.addProperty("message_id", chatScreenModel.getReply_message().getMessage_id());
            reply.addProperty("body", chatScreenModel.getReply_message().getBody());
            reply.addProperty("upload_type", chatScreenModel.getReply_message().getUpload_type());
            object.add("reply_message", reply);
        } else {
            object.addProperty("reply_id", "");
        }
        object.addProperty("created_at", chatScreenModel.getCreated_at());
        object.addProperty("upload_type", chatScreenModel.getUpload_type());
        object.addProperty("type", "Joined");
        if (webSocketClient != null)
            webSocketClient.send(new Gson().toJson(object).toString());
    }

/*    private void appAudio(String imageFile) {
        if (CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();
            Map<String, RequestBody> user = new HashMap<>();
            RequestBody fileBody = RequestBody.create(MediaType.parse("audio/*"), new File(imageFile));
            user.put("audio\"; filename=\"" + imageFile, fileBody);
            Call<ApiResponse> call = ApiExecutor.getClient(context).apiAddAudio(user);
            call.enqueue(new retrofit2.Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            ToastUtils.showToastShort(context, response.body().getResponseMessage());

                            chatScreenModel = new GetChatResonse.UserInfoBean();
                            AudioModel audioModel = new AudioModel();
                            audioModel.setUrl(response.body().getAudio());
                            audioModel.setRecordTime(record_time);

                            chatScreenModel.setBody(new Gson().toJson(audioModel));
                            chatScreenModel.setCreated_at(String.valueOf(System.currentTimeMillis() / 1000));
                            chatScreenModel.setRoom_id(roomId);
                            chatScreenModel.setCreated_timestamp("");
                            chatScreenModel.setId("");
                            chatScreenModel.setIs_user_sender(true);
                            chatScreenModel.setIs_stared(false);
                            chatScreenModel.setLocal_message_id("");
                            chatScreenModel.setRead_status(false);
                            chatScreenModel.setReceiver_image("");
                            chatScreenModel.setSender_image(new PrefManager(context).getUserPic());
                            chatScreenModel.setSender_id(new PrefManager(context).getUserId());
                            chatScreenModel.setUpload_type("audio");
                            chatScreenModel.setThumbnail("");
                            sendMessage(chatScreenModel);
                    //        chatScreenAdapter.setDataChange(groupDataIntoHashMap(chatScreenModels));
                            ;
                            binding.layoutCustomChatMenuFrg.etChatMessage.setText("");
                            binding.rvChatListFrg.post(new Runnable() {
                                @Override
                                public void run() {
                                    binding.rvChatListFrg.smoothScrollToPosition(chatScreenAdapter.getItemCount());
                                }
                            });
                            record_time = 0;
                        } else {
                            ToastUtils.showToastShort(context, getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    LogUtils.errorLog("Failure", "" + ((t != null && t.getMessage() != null) ? t.getMessage() : "---"));
                    ToastUtils.showToastShort(context, getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(context, context.getString(R.string.internet_alert_msg));
        }
    }*/
/*
    private void appVideo(File imageFile) {
        if (CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();

            Map<String, RequestBody> user = new HashMap<>();
            RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
            user.put("video\"; filename=\"" + imageFile.getName(), fileBody);
            Call<ApiResponse> call = ApiExecutor.getClient(context).apiAddVideo(user);
            call.enqueue(new retrofit2.Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            ToastUtils.showToastShort(context, response.body().getResponseMessage());

                            chatScreenModel = new GetChatResonse.UserInfoBean();
                            chatScreenModel.setBody(response.body().getVideo());
                            chatScreenModel.setCreated_at(String.valueOf(System.currentTimeMillis() / 1000));
                            chatScreenModel.setRoom_id(roomId);
                            chatScreenModel.setCreated_timestamp("");
                            chatScreenModel.setId("");
                            chatScreenModel.setIs_user_sender(true);
                            chatScreenModel.setIs_stared(false);
                            chatScreenModel.setLocal_message_id("");
                            chatScreenModel.setRead_status(false);
                            chatScreenModel.setReceiver_image("");
                            chatScreenModel.setSender_image(new PrefManager(context).getUserPic());
                            chatScreenModel.setSender_id(new PrefManager(context).getUserId());
                            chatScreenModel.setUpload_type("video");
                            chatScreenModel.setThumbnail(response.body().getThumbnail());
                            sendMessage(chatScreenModel);
                            binding.layoutCustomChatMenuFrg.etChatMessage.setText("");
                            binding.rvChatListFrg.post(new Runnable() {
                                @Override
                                public void run() {
                                    // Call smooth scroll
                                    binding.rvChatListFrg.smoothScrollToPosition(chatScreenAdapter.getItemCount());
                                }
                            });

                        } else {
                            ToastUtils.showToastShort(context, getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    LogUtils.errorLog("Failure", "" + ((t != null && t.getMessage() != null) ? t.getMessage() : "---"));
                    ToastUtils.showToastShort(context, getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(context, context.getString(R.string.internet_alert_msg));
        }
    }


    private void appDocument(File imageFile) {
        if (CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();
            Map<String, RequestBody> user = new HashMap<>();
            RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
            user.put("doc\"; filename=\"" + imageFile.getName(), fileBody);
            Call<ApiResponse> call = ApiExecutor.getClient(context).apiAddDocument(user);
            call.enqueue(new retrofit2.Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            ToastUtils.showToastShort(context, response.body().getResponseMessage());
                            chatScreenModel = new GetChatResonse.UserInfoBean();
                            chatScreenModel.setBody(response.body().getVideo());
                            chatScreenModel.setCreated_at(String.valueOf(System.currentTimeMillis() / 1000));
                            chatScreenModel.setRoom_id(roomId);
                            chatScreenModel.setCreated_timestamp("");
                            chatScreenModel.setId("");
                            chatScreenModel.setIs_user_sender(true);
                            chatScreenModel.setIs_stared(false);
                            chatScreenModel.setLocal_message_id("");
                            chatScreenModel.setRead_status(false);
                            chatScreenModel.setReceiver_image("");
                            chatScreenModel.setSender_image(new PrefManager(context).getUserPic());
                            chatScreenModel.setSender_id(new PrefManager(context).getUserId());
                            chatScreenModel.setUpload_type("doc");
                            chatScreenModel.setThumbnail(response.body().getThumbnail());
                            sendMessage(chatScreenModel);
                            binding.layoutCustomChatMenuFrg.etChatMessage.setText("");
                            binding.rvChatListFrg.post(new Runnable() {
                                @Override
                                public void run() {
                                    binding.rvChatListFrg.smoothScrollToPosition(chatScreenAdapter.getItemCount());
                                }
                            });

                        } else {
                            ToastUtils.showToastShort(context, getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    LogUtils.errorLog("Failure", "" + ((t != null && t.getMessage() != null) ? t.getMessage() : "---"));
                    ToastUtils.showToastShort(context, getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(context, context.getString(R.string.internet_alert_msg));
        }
    }

    private void appImage(File imageFile) {
        if (CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();

            Map<String, RequestBody> user = new HashMap<>();
            RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpg"), imageFile);
            user.put("image\"; filename=\"" + imageFile.getName(), fileBody);
            Call<ApiResponse> call = ApiExecutor.getClient(context).apiAddImage(user);
            call.enqueue(new retrofit2.Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            ToastUtils.showToastShort(context, response.body().getResponseMessage());

                            chatScreenModel = new GetChatResonse.UserInfoBean();
                            chatScreenModel.setBody(response.body().getImage());
                            chatScreenModel.setCreated_at(String.valueOf(System.currentTimeMillis() / 1000));
                            chatScreenModel.setRoom_id(roomId);
                            chatScreenModel.setCreated_timestamp("");
                            chatScreenModel.setId("");
                            chatScreenModel.setIs_user_sender(true);
                            chatScreenModel.setIs_stared(false);
                            chatScreenModel.setLocal_message_id("");
                            chatScreenModel.setRead_status(false);
                            chatScreenModel.setReceiver_image("");
                            chatScreenModel.setSender_image(new PrefManager(context).getUserPic());
                            chatScreenModel.setSender_id(new PrefManager(context).getUserId());
                            chatScreenModel.setUpload_type("image");
                            chatScreenModel.setThumbnail("");
                            sendMessage(chatScreenModel);
                 //           chatScreenAdapter.setDataChange(groupDataIntoHashMap(chatScreenModels));
                            ;
                            binding.layoutCustomChatMenuFrg.etChatMessage.setText("");
                            binding.rvChatListFrg.post(new Runnable() {
                                @Override
                                public void run() {
                                    // Call smooth scroll
                                    binding.rvChatListFrg.smoothScrollToPosition(chatScreenAdapter.getItemCount());
                                }
                            });

                        } else {
                            ToastUtils.showToastShort(context, getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    LogUtils.errorLog("Failure", "" + ((t != null && t.getMessage() != null) ? t.getMessage() : "---"));
                    ToastUtils.showToastShort(context, getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(context, context.getString(R.string.internet_alert_msg));
        }
    }*/

    @Override
    public void onResume() {
        super.onResume();
        // setUi();
        setBackGround();
    }

    private void setBackGround() {
        if (roomsBean != null) {
            if (roomsBean.getRoom_id() != null && roomsBean.getRoom_id().length() > 0) {
                RoomsBackgroundTable table = new RoomsBackgroundTable(context);
                wallpaperModel = table.getBackground(roomsBean.getRoom_id());
                table.closeDB();
            }

            if (wallpaperModel != null && wallpaperModel.getType() != null && wallpaperModel.getType().length() > 0) {
                binding.ivBackground.invalidate();
                switch (wallpaperModel.getType()) {
                    case "color":
                        binding.ivBackground.setBackgroundColor(Color.parseColor(wallpaperModel.getColor_code()));
                        Glide.with(context) // Bind it with the context of the actual view used
                                .load(Color.parseColor(wallpaperModel.getColor_code())) // Load the image
                                .centerCrop()// temporary holder displayed while the image loads// need to manually set the animation as bitmap cannot use cross fade
                                .thumbnail(0.2f)// make use of the thumbnail which can display a down-sized version of the image
                                .into(binding.ivBackground);
                        break;
                    case "image":
                        try {
                            Glide.with(context) // Bind it with the context of the actual view used
                                    .load(wallpaperModel.getImage()) // Load the image
                                    .asBitmap() // All our images are static, we want to display them as bitmaps
                                    .format(DecodeFormat.PREFER_RGB_565) // the decode format - this will not use alpha at all
                                    .centerCrop()// temporary holder displayed while the image loads// need to manually set the animation as bitmap cannot use cross fade
                                    .thumbnail(0.2f)//
                                    .into(binding.ivBackground);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Glide.with(context) // Bind it with the context of the actual view used
                                    .load(Themes.getInstance(context).setDarkTheme()) // Load the image
                                    .centerCrop()// temporary holder displayed while the image loads// need to manually set the animation as bitmap cannot use cross fade
                                    .thumbnail(0.2f)// make use of the thumbnail which can display a down-sized version of the image
                                    .into(binding.ivBackground);
                            binding.ivBackground.setBackgroundColor(Themes.getInstance(context).setDarkTheme());
                        }

                        break;

                }
            } else {

                Glide.with(context) // Bind it with the context of the actual view used
                        .load(Themes.getInstance(context).setDarkTheme()) // Load the image
                        .centerCrop()// temporary holder displayed while the image loads// need to manually set the animation as bitmap cannot use cross fade
                        .thumbnail(0.2f)// make use of the thumbnail which can display a down-sized version of the image
                        .into(binding.ivBackground);
                binding.ivBackground.setBackgroundColor(Themes.getInstance(context).setDarkTheme());

            }
        } else {
            Glide.with(context) // Bind it with the context of the actual view used
                    .load(Themes.getInstance(context).setDarkTheme()) // Load the image
                    .centerCrop()// temporary holder displayed while the image loads// need to manually set the animation as bitmap cannot use cross fade
                    .thumbnail(0.2f)// make use of the thumbnail which can display a down-sized version of the image
                    .into(binding.ivBackground);
            binding.ivBackground.setBackgroundColor(Themes.getInstance(context).setDarkTheme());
        }
    }

    private ArrayList<ListObject> groupDataIntoHashMap(List<GetChatResonse.UserInfoBean> chatModelList) {
        LinkedHashMap<String, Set<GetChatResonse.UserInfoBean>> groupedHashMap = new LinkedHashMap<>();
        Set<GetChatResonse.UserInfoBean> list = null;
        for (GetChatResonse.UserInfoBean chatModel : chatModelList) {
            //Log.d(TAG, travelActivityDTO.toString());
            String hashMapKey = TimeStampFormatter.getDayFromTS(chatModel.getCreated_timestamp(), "dd-MM-yyyy");
            //Log.d(TAG, "start date: " + DateParser.convertDateToString(travelActivityDTO.getStartDate()));
            if (groupedHashMap.containsKey(hashMapKey)) {
                // The key is already in the HashMap; add the pojo object
                // against the existing key.
                groupedHashMap.get(hashMapKey).add(chatModel);
            } else {
                // The key is not there in the HashMap; create a new key-value pair
                list = new LinkedHashSet<>();
                list.add(chatModel);
                groupedHashMap.put(hashMapKey, list);
            }
        }
        //Generate list from map
        ArrayList<ListObject> consolidatedList = new ArrayList<>();
        for (String date : groupedHashMap.keySet()) {
            DateObject dateItem = new DateObject();
            dateItem.setDate(date);
            consolidatedList.add(dateItem);
            for (GetChatResonse.UserInfoBean chatModel : groupedHashMap.get(date)) {
                ChatModelObject generalItem = new ChatModelObject();
                generalItem.setChatModel(chatModel);
                consolidatedList.add(generalItem);
            }
        }
        //chatScreenAdapter.setDataChange(consolidatedList);

        return consolidatedList;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webSocketClient != null) {
            webSocketClient.close();
        }
    }


    private PopupWindow avatarPopUp() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.enable_avtar_layout, null);

        LinearLayout.LayoutParams parm1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        parm1.setMargins(20, 20, 0, 0);
        view.setLayoutParams(parm1);
        popupWindow = new PopupWindow(context);
        popupWindow.setFocusable(true);
        popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setContentView(view);

        LinearLayout llMain = (LinearLayout) view.findViewById(R.id.llMain);
        TextView tvAvatarEnable = (TextView) view.findViewById(R.id.tvAvatarEnable);
        TextView tvDisableAvatar = (TextView) view.findViewById(R.id.tvDisableAvatar);
        ImageView ivAvtar = view.findViewById(R.id.ivAvatar);

        tvAvatarEnable.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setGreyHint()));
        tvAvatarEnable.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setGreyHint()));
        Themes.getInstance(context).changeIconColorToWhite(context, ivAvtar);
        llMain.setBackground(ContextCompat.getDrawable(context, Themes.getInstance(context).setDarkComment()));

        if (roomsBean.isIs_enable_avatar()) {
            inflater.inflate(R.layout.enable_avtar_layout, null);
        } else {
            inflater.inflate(R.layout.disable_avtar_layout, null);
        }
        return popupWindow;

    }


    private PopupWindow initiatePopupWindow() {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popup_call, null);
        view.startAnimation(slideTopToDown);


        LinearLayout.LayoutParams parm1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        parm1.setMargins(20, 20, 0, 0);
        view.setLayoutParams(parm1);

        pwindo = new PopupWindow(context);
        pwindo.setFocusable(true);
        pwindo.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        pwindo.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        pwindo.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pwindo.setContentView(view);
        LinearLayout llMain = (LinearLayout) view.findViewById(R.id.llMain);
        LinearLayout llAudio = (LinearLayout) view.findViewById(R.id.llAudio);
        LinearLayout llVideo = (LinearLayout) view.findViewById(R.id.llVideo);
        TextView tvAudio = (TextView) view.findViewById(R.id.tvAudio);
        TextView tvVideo = (TextView) view.findViewById(R.id.tvVideo);
        ImageView ivAudio = (ImageView) view.findViewById(R.id.ivAudio);
        ImageView ivVideo = (ImageView) view.findViewById(R.id.ivVideo);
        llMain.setBackground(ContextCompat.getDrawable(context, Themes.getInstance(context).setDarkComment()));
        tvAudio.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setGreyHint()));
        tvVideo.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setGreyHint()));
        Themes.getInstance(context).changeShareIcon(context, ivAudio);
        Themes.getInstance(context).changeShareIcon(context, ivVideo);
        llAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtils.checkPermissionCamera((Activity) context)) {
                    if (userType.equalsIgnoreCase("group")) {
                        ToastUtils.showToastShort(context, "Voice Call");
                        pwindo.dismiss();
                        // groupCallingUserList(groupMemberResponseList, "Audio");
                        //   callGroupMemberListAPI(true, "Audio");
                    } else {
                        if (qbId != null && qbId.length() > 0) {
                            audioCall(false, null);
                        }
                    }
                } else {
                    CommonUtils.requestPermissionCamera((Activity) context);
                    CommonUtils.requestPermissionCallPhone((Activity) context);
                }
            }
        });
        llVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtils.checkPermissionCamera((Activity) context)) {
                    if (userType.equalsIgnoreCase("group")) {
                        ToastUtils.showToastShort(context, "Video Call");
                        pwindo.dismiss();

                        //   callGroupMemberListAPI(true, "Video");
                        //groupCallingUserList(groupMemberResponseList, "Video");
                    } else {
                        if (qbId != null && qbId.length() > 0) {
                            videoCall(false, null);
                        }
                    }
                } else {
                    CommonUtils.requestPermissionCamera((Activity) context);
                    CommonUtils.requestPermissionCallPhone((Activity) context);
                }
            }
        });
        return pwindo;
    }


}

