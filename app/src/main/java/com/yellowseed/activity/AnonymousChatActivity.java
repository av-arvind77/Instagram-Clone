package com.yellowseed.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
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
import com.daasuu.bl.BubbleLayout;
import com.daasuu.bl.BubblePopupHelper;
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
import com.yellowseed.database.RoomChatTable;
import com.yellowseed.database.RoomsBackgroundTable;
import com.yellowseed.database.RoomsTable;
import com.yellowseed.database.WallpaperModel;
import com.yellowseed.databinding.ActivityChatsScreenAnonymousBinding;
import com.yellowseed.databinding.ActivityChatsScreenFrgBinding;
import com.yellowseed.databinding.LayoutattachementsiconsBinding;
import com.yellowseed.databinding.LayoutenableavatarBinding;
import com.yellowseed.imageUtils.CheckPermission;
import com.yellowseed.imageUtils.CropImage;
import com.yellowseed.listener.ChatModelObject;
import com.yellowseed.listener.DateObject;
import com.yellowseed.listener.ListObject;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.listener.OnItemLongClickListener;
import com.yellowseed.model.AudioModel;
import com.yellowseed.model.ChatScreenModel;
import com.yellowseed.model.ContactModel;
import com.yellowseed.model.DeleteRequestModel;
import com.yellowseed.model.LocationModel;
import com.yellowseed.model.resModel.GetChatResonse;
import com.yellowseed.model.resModel.GetRoomResonse;
import com.yellowseed.quickblox.call.VideoCallSessionManager;
import com.yellowseed.record_view.OnBasketAnimationEnd;
import com.yellowseed.record_view.OnRecordAudioResult;
import com.yellowseed.record_view.OnRecordClickListener;
import com.yellowseed.record_view.OnRecordListener;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.LogUtils;
import com.yellowseed.utils.PrefManager;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.TimeStampFormatter;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;
import com.yellowseed.webservices.ApiResponse;
import com.yellowseed.webservices.ServiceConstant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.gusavila92.websocketclient.WebSocketClient;

import static com.yellowseed.imageUtils.TakePictureUtils.CROP_FROM_CAMERA;
import static com.yellowseed.utils.TakePictureUtils.takePicture;

public class AnonymousChatActivity extends BaseActivity {
    private Context context;
    private ActivityChatsScreenAnonymousBinding binding;
    private ArrayList<ChatScreenModel> chatScreenModels = new ArrayList<>();
    private AnonymousChatAdapter chatScreenAdapter;
    private Dialog attachementDialog;
    private GetChatResonse.UserInfoBean chatScreenModel;
    private String chatUserId;
    private String roomId;
    private String from;
    private String image = "",video="", path = "";
    private int PLACE_PICKER_REQUEST = 1;
    private int RQS_PICK_CONTACT = 311;
    private int CHOOSE_FILE_REQUESTCODE = 312;
    private WebSocketClient webSocketClient;
    private GetChatResonse.UserInfoBean.ReplyMessageBean replyMessage;
    private long record_time;
    private Dialog revealDialog;
    private Themes themes;
    private Boolean darkThemeEnabled;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chats_screen_anonymous);
        context = AnonymousChatActivity.this;
        themes = new Themes(context);
        darkThemeEnabled = CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME);

        if (darkThemeEnabled)
            binding.layoutCustomChatMenuFrg.tvSendButton.setImageResource(R.mipmap.mike);
        else
            binding.layoutCustomChatMenuFrg.tvSendButton.setImageResource(R.mipmap.mike_icon);


        getIntentValue();
        initializedControl();
        setToolBar();
        setOnClickListener();
    }
    private void getIntentValue() {
        if (getIntent() != null && getIntent().getExtras() != null){
            if (getIntent().getStringExtra(AppConstants.FROM) != null) {
                from = getIntent().getStringExtra(AppConstants.FROM);
                chatUserId = getIntent().getStringExtra(AppConstants.ASSOC_ID);
                roomId = getIntent().getStringExtra(AppConstants.ROOM_ID);
            }
        }
    }

    @Override
    public void initializedControl() {
        setCurrentTheme();
        chatAnonymous();
        //IMPORTANT
        binding.layoutCustomChatMenuFrg.tvSendButton.setRecordView(binding.layoutCustomChatMenuFrg.recordView,binding.layoutCustomChatMenuFrg.llText);
        binding.layoutCustomChatMenuFrg.tvSendButton.setOnRecordClickListener(new OnRecordClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.layoutCustomChatMenuFrg.etChatMessage.getText().toString().trim().length() == 0) {
                    return;
                } else {
                    chatScreenModel = new GetChatResonse.UserInfoBean();
                    chatScreenModel.setBody(binding.layoutCustomChatMenuFrg.etChatMessage.getText().toString().trim());
                    chatScreenModel.setCreated_at(String.valueOf(System.currentTimeMillis()/1000));
                    chatScreenModel.setRoom_id(roomId);
                    chatScreenModel.setCreated_timestamp(String.valueOf(System.currentTimeMillis()/1000));
                    chatScreenModel.setId("");
                    chatScreenModel.setIs_user_sender(true);
                    chatScreenModel.setIs_stared(false);
                    chatScreenModel.setLocal_message_id(String.valueOf(System.currentTimeMillis()/1000));
                    chatScreenModel.setRead_status(false);
                    chatScreenModel.setReceiver_image("");
                    chatScreenModel.setSender_image(new PrefManager(context).getUserPic());
                    chatScreenModel.setSender_id(new PrefManager(context).getUserId());
                    chatScreenModel.setUpload_type("");
                    if(replyMessage!=null){
                        chatScreenModel.setReply_message(replyMessage);
                    }
                    sendMessage(chatScreenModel);
                    binding.layoutCustomChatMenuFrg.etChatMessage.setText("");
                    binding.rvChatListFrg.post(new Runnable() {
                        @Override
                        public void run() {
                            binding.rvChatListFrg.smoothScrollToPosition(chatScreenAdapter.getItemCount());
                        }
                    });
                }
            }
        });


        binding.layoutCustomChatMenuFrg.tvSendButton.setResultListner(new OnRecordAudioResult(){
            @Override
            public void onSuccess(final String filename) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
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
                record_time=0;
            }

            @Override
            public void onFinish(long recordTime) {
                record_time=recordTime;
                String time = CommonUtils.getHumanTimeText(recordTime);
                Toast.makeText(context, "onFinishRecord - Recorded Time is: " + time, Toast.LENGTH_SHORT).show();
                Log.d("RecordView", "onFinish");

                Log.d("RecordTime", time);
            }

            @Override
            public void onLessThanSecond() {
                record_time=0;
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
    private void setCurrentTheme() {
        binding.toolbarChatScreenFrg.toolbarMain.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        binding.toolbarChatScreenFrg.tvHeader.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.mainLayout.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        binding.frameAvatar.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        binding.viewLine3.setBackgroundColor(ContextCompat.getColor(context,Themes.getInstance(context).setViewLineGrey()));
        binding.viewLine2.setBackgroundColor(ContextCompat.getColor(context,Themes.getInstance(context).setViewLineGrey()));

        binding.layoutCustomChatMenuFrg.llChatBar.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        binding.layoutCustomChatMenuFrg.lleditText.setBackground(ContextCompat.getDrawable(context, Themes.getInstance(context).setDarkStoryBackground()));
        binding.layoutCustomChatMenuFrg.etChatMessage.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.llCallOption.setBackground(ContextCompat.getDrawable(context, Themes.getInstance(context).setChatOptionDrawble()));


        binding.viewLine.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setViewLineGrey()));

        Themes.getInstance(context).changeIconWhite(context, binding.layoutCustomChatMenuFrg.ivChatLeftIcon);
        Themes.getInstance(context).changeIconWhite(context, binding.layoutCustomChatMenuFrg.btnSecondRight);
        //Themes.getInstance(context).changeIconWhite(context, binding.layoutCustomChatMenuFrg.tvSendButton);

        Themes.getInstance(context).changeIconWhite(context, binding.layoutCustomChatMenuFrg.ivChatRightIcon);
        Themes.getInstance(context).changeIconWhite(context, binding.layoutCustomChatMenuFrg.btnSecondRight);


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
        themes.changeIconColor(context, binding.toolbarChatScreenFrg.ivLeft);
        themes.changePostIconColor(context, binding.toolbarChatScreenFrg.ivRight);


    }

    private void chatAnonymous() {
        binding.rvChatListFrg.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvChatListFrg.setLayoutManager(layoutManager);
        ChatScreenModel chatScreenModel = new ChatScreenModel();
        chatScreenModel.setMessages("Hi");
        chatScreenModels.add(chatScreenModel);

        chatScreenModel = new ChatScreenModel();
        chatScreenModel.setMessages("Hello");
        chatScreenModels.add(chatScreenModel);

        chatScreenAdapter = new AnonymousChatAdapter(context, chatScreenModels, new OnItemLongClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        });
        binding.rvChatListFrg.setAdapter(chatScreenAdapter);
    }

  /*  private void chatRecyclerView() {
        // binding.rvChatListFrg.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvChatListFrg.setLayoutManager(layoutManager);
        chatScreenAdapter = new ChatScreenAdapter(AnonymousChatActivity.this, groupDataIntoHashMap(chatScreenModels), new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.llChatReciever:

                        if (((ChatModelObject)chatScreenAdapter.getItem(position)).getChatModel().getUpload_type() != null && ((ChatModelObject)chatScreenAdapter.getItem(position)).getChatModel().getUpload_type().length() > 0) {
                            switch (((ChatModelObject)chatScreenAdapter.getItem(position)).getChatModel().getUpload_type()) {
                                case "audio":

                                    break;
                                case "video":
                                    context.startActivity(new Intent(context, VideoDetailActivity.class).putExtra("url", ((ChatModelObject)chatScreenAdapter.getItem(position)).getChatModel().getBody()));
                                    break;
                                case "location":
                                    Gson gson = new Gson();
                                    LocationModel locationModel = gson.fromJson(((ChatModelObject)chatScreenAdapter.getItem(position)).getChatModel().getBody(), LocationModel.class);
                                    CommonUtils.showMap(context, String.valueOf(locationModel.getLatitude()), String.valueOf(locationModel.getLongitude()), locationModel.getName());
                                    break;
                                case "contact":

                                    break;
                                case "image":

                                    break;
                            }
                        }
                        break;

                    case R.id.llAddView:
                        if (((ChatModelObject)chatScreenAdapter.getItem(position)).getChatModel().getUpload_type() != null && ((ChatModelObject)chatScreenAdapter.getItem(position)).getChatModel().getUpload_type().length() > 0) {
                            switch (chatScreenModels.get(position).getUpload_type()) {
                                case "audio":

                                    break;
                                case "video":
                                    context.startActivity(new Intent(context, VideoDetailActivity.class).putExtra("url",((ChatModelObject)chatScreenAdapter.getItem(position)).getChatModel().getBody()));
                                    break;
                                case "location":
                                    Gson gson = new Gson();
                                    LocationModel locationModel = gson.fromJson(((ChatModelObject)chatScreenAdapter.getItem(position)).getChatModel().getBody(), LocationModel.class);
                                    CommonUtils.showMap(context, String.valueOf(locationModel.getLatitude()), String.valueOf(locationModel.getLongitude()), locationModel.getName());
                                    break;
                                case "contact":

                                    break;
                                case "image":

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
    }*/

    private void showBottomBar(ArrayList<GetChatResonse.UserInfoBean> chatScreenModels) {
        int count=0;
        for (int i = 0; i <chatScreenModels.size() ; i++) {
            if(chatScreenModels.get(i).isSelected()){
                count=+1;
            }
        }
        if(count>0){
            binding.layoutChatClickItem.getRoot().setVisibility(View.VISIBLE);
        }else {
            binding.layoutChatClickItem.getRoot().setVisibility(View.GONE);
        }
    }

    @Override
    public void setToolBar() {
        binding.toolbarChatScreenFrg.ivLeft.setVisibility(View.VISIBLE);
        binding.toolbarChatScreenFrg.ivLeft.setImageResource(R.mipmap.back_icon);
        binding.toolbarChatScreenFrg.tvHeader.setVisibility(View.VISIBLE);
        binding.toolbarChatScreenFrg.tvHeader.setText(R.string.anonymoususer);
        binding.toolbarChatScreenFrg.ivRight.setVisibility(View.VISIBLE);
        binding.toolbarChatScreenFrg.ivRight.setImageResource(R.mipmap.three_dot);

        // For set button on changed of text
        binding.layoutCustomChatMenuFrg.etChatMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length() > 0){
                    if (darkThemeEnabled)
                        binding.layoutCustomChatMenuFrg.tvSendButton.setImageResource(R.mipmap.send_icon);
                    else
                        binding.layoutCustomChatMenuFrg.tvSendButton.setImageResource(R.mipmap.up_send);

                    binding.layoutCustomChatMenuFrg.tvSendButton.setListenForRecord(false);
                }
                else{
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
        binding.toolbarChatScreenFrg.ivLeft.setOnClickListener(this);
        binding.toolbarChatScreenFrg.ivRight.setOnClickListener(this);
        binding.toolbarChatScreenFrg.ivRightSecond.setOnClickListener(this);
        binding.layoutCustomChatMenuFrg.btnSecondRight.setOnClickListener(this);
        binding.layoutCustomChatMenuFrg.btnSecondRight.setVisibility(View.GONE);
        binding.layoutCustomChatMenuFrg.ivChatRightIcon.setVisibility(View.VISIBLE);
        binding.layoutCustomChatMenuFrg.ivChatRightIcon.setImageResource(R.mipmap.attachment_icon);
        binding.layoutCustomChatMenuFrg.ivChatLeftIcon.setOnClickListener(this);
        binding.layoutCustomChatMenuFrg.ivChatRightIcon.setOnClickListener(this);
        binding.layoutCustomChatMenuFrg.ivCancelReply.setOnClickListener(this);

        binding.layoutChatClickItem.ivBackward.setOnClickListener(this);
        binding.layoutChatClickItem.ivForward.setOnClickListener(this);
        binding.layoutChatClickItem.ivCopy.setOnClickListener(this);
        binding.layoutChatClickItem.ivDelete.setOnClickListener(this);
        binding.layoutChatClickItem.ivShare.setOnClickListener(this);
        binding.layoutChatClickItem.ivStared.setOnClickListener(this);
        binding.llChat.setOnClickListener(this);
        binding.llVoiceCall.setOnClickListener(this);
        binding.llVideoCall.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llChat:
                ToastUtils.showToastShort(context,"Work in progress.");
                break;
            case R.id.llVoiceCall:
                ToastUtils.showToastShort(context,"Work in progress.");
                break;
                case R.id.llVideoCall:
                ToastUtils.showToastShort(context,"Work in progress.");
                break;

            case R.id.btnAnonEnableUser:
                revealDialog.dismiss();
                break;
            case R.id.ivRight:
                openMenuPopup(view) ;
                break;
            case R.id.ivLeft:
                finish();
                break;
            case R.id.btnSecondRight:
                takePicture(AnonymousChatActivity.this,"");
                break;
            case R.id.ivChatRightIcon:
                //openGallery();
                break;
            case R.id.tvattachDocuments:
                if(attachementDialog!=null){
                    attachementDialog.dismiss();
                }
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                Intent i = Intent.createChooser(intent, "File");
                startActivityForResult(i, CHOOSE_FILE_REQUESTCODE);
                break;
            case R.id.tvattachCamera:
                if(attachementDialog!=null){
                    attachementDialog.dismiss();
                }
                if (!CheckPermission.checkCameraPermission(context)) {
                    CheckPermission.requestCameraPermission((Activity) context, CheckPermission.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                } else {
                    addPhotoDialog();
                }
                break;
            case R.id.tvAttachGallery:
                if(attachementDialog!=null){
                    attachementDialog.dismiss();
                }
                if (!CheckPermission.checkCameraPermission(context)) {
                    CheckPermission.requestCameraPermission((Activity) context, CheckPermission.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                } else {
                    openGallery();
                }
                break;
            case R.id.tvAttachLocation:
//                ToastUtils.showToastShort(context, "Work in progress");
                if(attachementDialog!=null){
                    attachementDialog.dismiss();
                }
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(AnonymousChatActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tvAttachContacts:
                if(attachementDialog!=null){
                    attachementDialog.dismiss();
                }
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                startActivityForResult(intent,RQS_PICK_CONTACT );
                break;

            default:
                break;

        }
    }

    private void addPhotoDialog() {
        final CharSequence[] items = {getString(R.string.take_a_photo), getString(R.string.from_gallery)};
        new MaterialDialog.Builder(context)
                .title(R.string.choose_photo).titleGravity(GravityEnum.CENTER)
                .items(items)
                .itemsColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()))
                .backgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeStory()))
                .titleColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()))
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
            intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[] {"image/*", "video/*"});
            startActivityForResult(intent, com.yellowseed.imageUtils.TakePictureUtils.PICK_GALLERY);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == com.yellowseed.imageUtils.TakePictureUtils.PICK_GALLERY) {
            if (resultCode == Activity.RESULT_OK) {
                if(intent.getData()!=null&&intent.getData().toString().contains("video")){
                    video=String.valueOf(System.currentTimeMillis()/1000);
                    try {
                        InputStream inputStream = context.getContentResolver().openInputStream(intent.getData());
                        FileOutputStream fileOutputStream = new FileOutputStream(new File(this.context.getExternalFilesDir("temp"), video + ".mp4"));
                        com.yellowseed.imageUtils.TakePictureUtils.copyStream(inputStream, fileOutputStream);
                        fileOutputStream.close();
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    File imageFile = new File(context.getExternalFilesDir("temp"), video+".mp4");
                    appVideo(imageFile);
                }else {
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
        }

        else if (requestCode == CHOOSE_FILE_REQUESTCODE) {
            if (resultCode == Activity.RESULT_OK){
                String document=String.valueOf(System.currentTimeMillis()/1000);
                try {
                    InputStream inputStream = context.getContentResolver().openInputStream(intent.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(new File(this.context.getExternalFilesDir("temp"), video + ".pdf"));
                    com.yellowseed.imageUtils.TakePictureUtils.copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                File imageFile = new File(context.getExternalFilesDir("temp"), document+".pdf");
                appDocument(imageFile);
            }
        }
        else if (requestCode == CROP_FROM_CAMERA) {
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
        }/*else if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(intent, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                LocationModel locationModel=new LocationModel();
                locationModel.setName( place.getName().toString());
                locationModel.setLatitude(place.getLatLng().latitude);
                locationModel.setLongitude(place.getLatLng().longitude);
                chatScreenModel = new GetChatResonse.UserInfoBean();
                chatScreenModel.setBody(new Gson().toJson(locationModel));
                chatScreenModel.setCreated_at(String.valueOf(System.currentTimeMillis()/1000));
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


           //     chatScreenAdapter.setDataChange(groupDataIntoHashMap(chatScreenModels));
                binding.layoutCustomChatMenuFrg.etChatMessage.setText("");
                binding.rvChatListFrg.post(new Runnable() {
                    @Override
                    public void run() {
                        // Call smooth scroll
                        binding.rvChatListFrg.smoothScrollToPosition(chatScreenAdapter.getItemCount());
                    }
                });*/

            }
        /*else if(requestCode == RQS_PICK_CONTACT) {
            if (resultCode == RESULT_OK) {
                Uri contactData = intent.getData();
                Cursor cursor = managedQuery(contactData, null, null, null, null);
                cursor.moveToFirst();

                String number = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                ContactModel contactModel=new ContactModel();
                contactModel.setName(name);
                contactModel.setNumber(number);

                chatScreenModel = new GetChatResonse.UserInfoBean();
                chatScreenModel.setBody(new Gson().toJson(contactModel));
                chatScreenModel.setCreated_at(String.valueOf(System.currentTimeMillis()/1000));
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
                chatScreenModels.add(chatScreenModel);
                chatScreenAdapter.setDataChange(groupDataIntoHashMap(chatScreenModels));;
                binding.layoutCustomChatMenuFrg.etChatMessage.setText("");
                RoomChatTable roomChatTable =new RoomChatTable(context);
                roomChatTable.insertSingleRoomInformation(chatScreenModel,roomId);
                roomChatTable.closeDB();
                binding.rvChatListFrg.post(new Runnable() {
                    @Override
                    public void run() {
                        // Call smooth scroll
                        binding.rvChatListFrg.smoothScrollToPosition(chatScreenAdapter.getItemCount());
                    }
                });
            }*/



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




    private void openMenuPopup( View targetView) {


        BubbleLayout customLayout = (BubbleLayout) LayoutInflater.from(context).inflate(R.layout.anonymous_chat_popup, null);
        customLayout.setBubbleColor(ContextCompat.getColor(context, Themes.getInstance(context).setPopupBackground()));

        final PopupWindow quickAction = BubblePopupHelper.create(context, customLayout);
        LinearLayout llMain = (LinearLayout) customLayout.findViewById(R.id.llMain);
        LinearLayout llExit = (LinearLayout) customLayout.findViewById(R.id.llExit);

        llMain.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setPopupBackground()));
        TextView tvEnable = (TextView) customLayout.findViewById(R.id.tvEnable);
        TextView tvReveal = (TextView) customLayout.findViewById(R.id.tvReveal);
        TextView tvLogout = (TextView) customLayout.findViewById(R.id.tvExit);
        ImageView ivEnable = customLayout.findViewById(R.id.ivEnable);
        ImageView ivReveal = customLayout.findViewById(R.id.ivReveal);
        tvEnable.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        tvLogout.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        tvReveal.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        Themes.getInstance(context).changeIconColorToWhite(context, ivEnable);
        Themes.getInstance(context).changeIconColorToWhite(context, ivReveal);



        tvEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToastShort(context, "Avater is enabled");
            }
        });
        tvReveal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quickAction.dismiss();

                revealDialogLayout();

            }


        });
        llExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }


        });

        quickAction.showAsDropDown(targetView);
    }


    private void revealDialogLayout() {
        LayoutenableavatarBinding layoutenableavatarBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layoutenableavatar, null, false);
        layoutenableavatarBinding.llMain.setBackground(ContextCompat.getDrawable(context, Themes.getInstance(context).setDarkSearchDrawable()));
        layoutenableavatarBinding.tvAnonEnableUser.setTextColor(ContextCompat.getColor(context,Themes.getInstance(context).setDarkThemeText()));
        revealDialog = DialogUtils.createDialog(context, layoutenableavatarBinding.getRoot());
        layoutenableavatarBinding.btnAnonEnableUser.setOnClickListener(this);
    }



    private void sendReadStatus(String msg_id) {
        JsonObject object = new JsonObject();
        object.addProperty("read_by","read_by_user");
        object.addProperty("room_id",roomId);
        object.addProperty("upload_type","Recieved");
        object.addProperty("msg_id",msg_id);
        object.addProperty("assoc_id",chatUserId);
        if(webSocketClient!=null)
            webSocketClient.send(new Gson().toJson(object).toString());
    }

    public void sendMessage(GetChatResonse.UserInfoBean chatScreenModel) {
        replyMessage=null;
        binding.layoutCustomChatMenuFrg.llReply.setVisibility(View.GONE);
        JsonObject object = new JsonObject();
        object.addProperty("local_message_id","");
        object.addProperty("sender_id",new PrefManager(context).getUserId());
        object.addProperty("room_id",roomId);
        object.addProperty("is_group","");
        object.addProperty("body",chatScreenModel.getBody());
        object.addProperty("assoc_id",chatUserId);
        object.addProperty("is_group",false);
        object.addProperty("message_type","");
        if(chatScreenModel.getReply_message()!=null) {
            object.addProperty("reply_id",chatScreenModel.getReply_message().getMessage_id());
            JsonObject reply=new JsonObject();
            reply.addProperty("sender_id",chatScreenModel.getReply_message().getSender_id());
            reply.addProperty("message_id",chatScreenModel.getReply_message().getMessage_id());
            reply.addProperty("body",chatScreenModel.getReply_message().getBody());
            reply.addProperty("upload_type",chatScreenModel.getReply_message().getUpload_type());
            object.add("reply_message", reply);
        }
        object.addProperty("created_at",chatScreenModel.getCreated_at());
        object.addProperty("upload_type",chatScreenModel.getUpload_type());
        object.addProperty("type","Joined");
        if(webSocketClient!=null)
            webSocketClient.send(new Gson().toJson(object).toString());
    }

/*    private void appAudio(String imageFile) {
        if(CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();
            Map<String, RequestBody> user = new HashMap<>();
            RequestBody fileBody = RequestBody.create(MediaType.parse("audio/*"), new File(imageFile));
            user.put("audio\"; filename=\"" + imageFile, fileBody);
            Call<ApiResponse> call = ApiExecutor.getClient(context).apiAddAudio(user);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            ToastUtils.showToastShort(context,response.body().getResponseMessage());

                            chatScreenModel = new GetChatResonse.UserInfoBean();
                            AudioModel audioModel=new AudioModel();
                            audioModel.setUrl(response.body().getAudio());
                            audioModel.setRecordTime(record_time);

                            chatScreenModel.setBody(new Gson().toJson(audioModel));
                            chatScreenModel.setCreated_at(String.valueOf(System.currentTimeMillis()/1000));
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
                            chatScreenAdapter.setDataChange(groupDataIntoHashMap(chatScreenModels));;
                            binding.layoutCustomChatMenuFrg.etChatMessage.setText("");
                            binding.rvChatListFrg.post(new Runnable() {
                                @Override
                                public void run() {
                                    binding.rvChatListFrg.smoothScrollToPosition(chatScreenAdapter.getItemCount());
                                }
                            });
                            record_time=0;
                        }
                        else
                        {
                            ToastUtils.showToastShort(context,getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    LogUtils.errorLog("Failure", "" + ((t != null && t.getMessage() != null) ? t.getMessage() : "---"));
                    ToastUtils.showToastShort(context,getString(R.string.server_error_msg));
                }
            });
        }
        else
        {
            ToastUtils.showToastShort(context,context.getString(R.string.internet_alert_msg));
        }
    }*/

    private void appVideo(File imageFile) {
        if(CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();

            Map<String, RequestBody> user = new HashMap<>();
            RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"),imageFile);
            user.put("video\"; filename=\"" + imageFile.getName(), fileBody);
            Call<ApiResponse> call = ApiExecutor.getClient(context).apiAddVideo(user);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            ToastUtils.showToastShort(context,response.body().getResponseMessage());

                            chatScreenModel = new GetChatResonse.UserInfoBean();
                            chatScreenModel.setBody(response.body().getVideo());
                            chatScreenModel.setCreated_at(String.valueOf(System.currentTimeMillis()/1000));
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

                        }
                        else
                        {
                            ToastUtils.showToastShort(context,getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    LogUtils.errorLog("Failure", "" + ((t != null && t.getMessage() != null) ? t.getMessage() : "---"));
                    ToastUtils.showToastShort(context,getString(R.string.server_error_msg));
                }
            });
        }
        else
        {
            ToastUtils.showToastShort(context,context.getString(R.string.internet_alert_msg));
        }
    }


    private void appDocument(File imageFile) {
        if(CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();
            Map<String, RequestBody> user = new HashMap<>();
            RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"),imageFile);
            user.put("doc\"; filename=\"" + imageFile.getName(), fileBody);
            Call<ApiResponse> call = ApiExecutor.getClient(context).apiAddDocument(user);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            ToastUtils.showToastShort(context,response.body().getResponseMessage());

                            chatScreenModel = new GetChatResonse.UserInfoBean();
                            chatScreenModel.setBody(response.body().getVideo());
                            chatScreenModel.setCreated_at(String.valueOf(System.currentTimeMillis()/1000));
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

                        }
                        else
                        {
                            ToastUtils.showToastShort(context,getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    LogUtils.errorLog("Failure", "" + ((t != null && t.getMessage() != null) ? t.getMessage() : "---"));
                    ToastUtils.showToastShort(context,getString(R.string.server_error_msg));
                }
            });
        }
        else
        {
            ToastUtils.showToastShort(context,context.getString(R.string.internet_alert_msg));
        }
    }
    private void appImage(File imageFile) {
        if(CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();

            Map<String, RequestBody> user = new HashMap<>();
            RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpg"), imageFile);
            user.put("image\"; filename=\"" + imageFile.getName(), fileBody);
            Call<ApiResponse> call = ApiExecutor.getClient(context).apiAddImage(user);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            ToastUtils.showToastShort(context,response.body().getResponseMessage());

                            chatScreenModel = new GetChatResonse.UserInfoBean();
                            chatScreenModel.setBody(response.body().getImage());
                            chatScreenModel.setCreated_at(String.valueOf(System.currentTimeMillis()/1000));
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
                        //    chatScreenAdapter.setDataChange(groupDataIntoHashMap(chatScreenModels));;
                            binding.layoutCustomChatMenuFrg.etChatMessage.setText("");
                            binding.rvChatListFrg.post(new Runnable() {
                                @Override
                                public void run() {
                                    // Call smooth scroll
                                    binding.rvChatListFrg.smoothScrollToPosition(chatScreenAdapter.getItemCount());
                                }
                            });

                        }
                        else
                        {
                            ToastUtils.showToastShort(context,getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    LogUtils.errorLog("Failure", "" + ((t != null && t.getMessage() != null) ? t.getMessage() : "---"));
                    ToastUtils.showToastShort(context,getString(R.string.server_error_msg));
                }
            });
        }
        else
        {
            ToastUtils.showToastShort(context,context.getString(R.string.internet_alert_msg));
        }
    }


    private ArrayList<ListObject> groupDataIntoHashMap(List<GetChatResonse.UserInfoBean> chatModelList) {
        LinkedHashMap<String, Set<GetChatResonse.UserInfoBean>> groupedHashMap = new LinkedHashMap<>();
        Set<GetChatResonse.UserInfoBean> list = null;
        for (GetChatResonse.UserInfoBean chatModel : chatModelList) {
            //Log.d(TAG, travelActivityDTO.toString());
            String hashMapKey = TimeStampFormatter.getDayFromTS(chatModel.getCreated_timestamp(),"dd-MM-yyyy");
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

        return consolidatedList;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(webSocketClient!=null) {
            webSocketClient.close();
        }
    }


    @Override
    public void onBackPressed() {
        finish();
      //  apiDisconnectChat();
    }
    private void apiDisconnectChat() {
        if(CommonUtils.isOnline(context)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("room_id", roomId);
            Call<GetChatResonse> call = ApiExecutor.getClient(context).apiDisconnectAnonymousChat(jsonObject);
            call.enqueue(new Callback<GetChatResonse>() {
                @Override
                public void onResponse(@NonNull Call<GetChatResonse> call, @NonNull final Response<GetChatResonse> response) {
                    try {
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(@NonNull Call<GetChatResonse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                }
            });
        }
        else
        {
            ToastUtils.showToastShort(context,context.getString(R.string.internet_alert_msg));
        }
    }

}

