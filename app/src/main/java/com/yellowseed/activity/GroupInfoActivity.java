package com.yellowseed.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.JsonObject;
import com.quickblox.sample.video.activities.CallActivity;
import com.quickblox.sample.video.utils.CollectionsUtils;
import com.quickblox.users.model.QBUser;
import com.quickblox.videochat.webrtc.QBRTCClient;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.QBRTCTypes;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.yellowseed.R;
import com.yellowseed.adapter.GroupInfoAdapter;
import com.yellowseed.database.RoomsBackgroundTable;
import com.yellowseed.database.RoomsTable;
import com.yellowseed.database.WallpaperModel;
import com.yellowseed.databinding.ActivityGroupInfoBinding;
import com.yellowseed.databinding.LayoutavatarimageclickableBinding;
import com.yellowseed.databinding.LayoutchatinfoBinding;
import com.yellowseed.imageUtils.CheckPermission;
import com.yellowseed.imageUtils.CircleTransformation;
import com.yellowseed.imageUtils.CropImage;
import com.yellowseed.imageUtils.TakePictureUtils;
import com.yellowseed.model.EditBroadCastModel;
import com.yellowseed.model.NewGroupDoneMOdel;
import com.yellowseed.model.reqModel.ClearRoomRequest;
import com.yellowseed.model.resModel.GetChatResonse;
import com.yellowseed.model.resModel.GetRoomResonse;
import com.yellowseed.model.resModel.GroupMemberResonse;
import com.yellowseed.model.resModel.ReportPostResponse;
import com.yellowseed.quickblox.call.VideoCallSessionManager;
import com.yellowseed.utils.ActivityController;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.ConverterUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.LogUtils;
import com.yellowseed.utils.PrefManager;
import com.yellowseed.utils.RecyclerItemClickListener;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;
import com.yellowseed.webservices.ApiResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yellowseed.imageUtils.TakePictureUtils.CROP_FROM_CAMERA;

public class GroupInfoActivity extends BaseActivity {
    private ActivityGroupInfoBinding binding;
    private Context context;
    private String  roomID;
    private boolean isEdit;
    private ArrayList<GroupMemberResonse.MembersBean> groupMemberList = new ArrayList<>();
    private GroupInfoAdapter adapter;
    private String muteTime;
    private RoomsTable roomsTable;
    private GetRoomResonse.RoomsBean roomsBean;
    private  Intent intent;
    private List<String> arlDeletedIds=new ArrayList<>();
    private List<String> arlAddIds=new ArrayList<>();
    private static final int TAKE_PICTURE = 1;
    private String image = "";
    private File imageFile;
    private MultipartBody.Part groupImage = null;
    private String type ="";
    private Themes themes;
    private boolean darkThemeEnabled;
    private Dialog chatInfoDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_group_info);
        context = GroupInfoActivity.this;
        themes = new Themes(context);
        darkThemeEnabled = CommonUtils.getPreferencesBoolean(context,AppConstants.DARK_THEME);
     //   roomsTable=new RoomsTable(context);
        getIntentValue();
     /*   if(roomID!=null&&roomID.length()>0){
            roomsBean=roomsTable.getSingleRoom(context,roomID);
        }*/

        SpannableString content = new SpannableString("Add Participants");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        binding.tvAddMembers.setText(content);
       // roomsTable.closeDB();
      //  setUi();
        initializedControl();
        setToolBar();
        setOnClickListener();
    }

    private void getIntentValue() {
      /*  if (getIntent() != null && getIntent().getExtras() != null){
            if (getIntent().getStringExtra(AppConstants.ROOM_ID) != null) {
                roomID = getIntent().getStringExtra(AppConstants.ROOM_ID);
            }
        }*/
    }

    @Override
    public void initializedControl() {
        setcurrentTheme();
        groupInfoParticipantRecylerView();
    }

    private void setcurrentTheme() {
        binding.toolbarGroupInfo.tvHeader.setTextColor(ContextCompat.getColor(context,themes.setDarkThemeText()));
        binding.toolbarGroupInfo.tvRighttext.setTextColor(ContextCompat.getColor(context,themes.setTolbarText()));
        binding.toolbarGroupInfo.toolbarMain.setBackgroundColor(ContextCompat.getColor(context,themes.setDarkTheme()));
        themes.changeIconColor(context,binding.toolbarGroupInfo.ivLeft);


        binding.llMain.setBackgroundColor(ContextCompat.getColor(context,themes.setDarkTheme()));
       /* themes.changeIconColorToWhite(context,binding.ivGroupName);
        themes.changeIconColorToWhite(context,binding.ivEditName);*/
        themes.changeIconColorToWhite(context,binding.ivMedia);
        themes.changeIconColorToWhite(context,binding.ivStarred);
        themes.changeIconColorToWhite(context,binding.ivUser);
        themes.changeIconColorToWhite(context,binding.ivNotification);
        themes.changeIconColorToWhite(context,binding.ivWallpaper);
        themes.changeIconColorToWhite(context,binding.ivNotification);
        themes.changeShareIcon(context,binding.ivView);
        themes.changeShareIcon(context,binding.ivChatSearch);
        binding.tvView.setTextColor(ContextCompat.getColor(context,themes.setDarkThemeText()));
        binding.tvChatSearch.setTextColor(ContextCompat.getColor(context,themes.setDarkThemeText()));


        binding.tvGroupName.setTextColor(ContextCompat.getColor(context,themes.setDarkThemeText()));
        binding.tvMediaDocsLinks.setTextColor(ContextCompat.getColor(context,themes.setDarkThemeText()));
        binding.tvStarredMessageGroup.setTextColor(ContextCompat.getColor(context,themes.setDarkThemeText()));
        binding.tvMuteNotificationsGroup.setTextColor(ContextCompat.getColor(context,themes.setDarkThemeText()));

        binding.tvPhone.setTextColor(ContextCompat.getColor(context,themes.setDarkThemeText()));

        binding.tvWallaperGroup.setTextColor(ContextCompat.getColor(context,themes.setDarkThemeText()));
        binding.tvAddMembers.setTextColor(ContextCompat.getColor(context,themes.setTolbarText()));

        binding.tvCreate.setTextColor(ContextCompat.getColor(context,themes.setTolbarText()));
        binding.tvAddExisting.setTextColor(ContextCompat.getColor(context,themes.setTolbarText()));
        binding.tvShare.setTextColor(ContextCompat.getColor(context,themes.setTolbarText()));

        themes.changeArrowColor(context,binding.ivStaredArrow);
        themes.changeArrowColor(context,binding.ivMediaArrow);

        binding.viewLine1.setBackgroundColor(ContextCompat.getColor(context, themes.setViewLineGrey()));
        binding.viewChatSearch.setBackgroundColor(ContextCompat.getColor(context, themes.setViewLineGrey()));
        binding.viewLineProfile.setBackgroundColor(ContextCompat.getColor(context, themes.setViewLineGrey()));
        binding.viewLine2.setBackgroundColor(ContextCompat.getColor(context, themes.setViewLineGrey()));
        binding.viewLine3.setBackgroundColor(ContextCompat.getColor(context, themes.setViewLineGrey()));
        binding.viewLine4.setBackgroundColor(ContextCompat.getColor(context, themes.setViewLineGrey()));
        binding.viewLine5.setBackgroundColor(ContextCompat.getColor(context, themes.setViewLineGrey()));
        binding.viewLine6.setBackgroundColor(ContextCompat.getColor(context, themes.setViewLineGrey()));
        binding.viewLine7.setBackgroundColor(ContextCompat.getColor(context, themes.setViewLineGrey()));
        binding.viewLine8.setBackgroundColor(ContextCompat.getColor(context, themes.setViewLineGrey()));
        binding.viewLine9.setBackgroundColor(ContextCompat.getColor(context, themes.setViewLineGrey()));
        binding.viewLine11.setBackgroundColor(ContextCompat.getColor(context, themes.setViewLineGrey()));
        binding.viewLine12.setBackgroundColor(ContextCompat.getColor(context, themes.setViewLineGrey()));
        binding.viewLineShare.setBackgroundColor(ContextCompat.getColor(context, themes.setViewLineGrey()));




        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            binding.ivMessage.setImageResource(R.mipmap.group_chat_white);

        }
        else
        {
            binding.ivMessage.setImageResource(R.mipmap.w_messageicon );

        }
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            binding.ivCall.setImageResource(R.mipmap.group_call_white);

        }
        else
        {
            binding.ivCall.setImageResource(R.mipmap.w_callicon );

        }
        if (CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME)) {
            binding.ivVideo.setImageResource(R.mipmap.group_video_white);

        }
        else
        {
            binding.ivVideo.setImageResource(R.mipmap.w_vidoicon );

        }






    }

    private void groupInfoParticipantRecylerView() {
        binding.rvGroupPartipicants.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvGroupPartipicants.setLayoutManager(layoutManager);
        binding.rvGroupPartipicants.setFocusable(false);

        adapter = new GroupInfoAdapter(groupMemberList, this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
               // showMenu(view,position);
                chatInfo(view,position);
            }
        });
        binding.rvGroupPartipicants.setAdapter(adapter);

    }

    private void chatInfo(View view, int position) {

        LayoutchatinfoBinding layoutchatinfoBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layoutchatinfo, null, false);
        chatInfoDialog = DialogUtils.createDialog(context, layoutchatinfoBinding.getRoot());
        layoutchatinfoBinding.tvInfo.setOnClickListener(this);
        layoutchatinfoBinding.tvVoicecall.setOnClickListener(this);
        layoutchatinfoBinding.tvVideoCall.setOnClickListener(this);
        layoutchatinfoBinding.tvMessage.setOnClickListener(this);
        layoutchatinfoBinding.tvRemove.setOnClickListener(this);


        layoutchatinfoBinding.llPopUp.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeDialog( )));
        layoutchatinfoBinding.tvInfo.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText( )));
        layoutchatinfoBinding.tvVoicecall.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText( )));
        layoutchatinfoBinding.tvVideoCall.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText( )));
        layoutchatinfoBinding.tvRemove.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText( )));
        layoutchatinfoBinding.tvMessage.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText( )));
        layoutchatinfoBinding.tvInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                       /* if (CommonUtils.checkPermissionCamera((Activity) context)) {
                            try {
                                Collection<QBUser> selectedUsers = new ArrayList<>();
                                QBUser qbUser = new QBUser();
                                qbUser.setFullName("");
                                qbUser.setId(Integer.valueOf(groupMemberList.get(position).getQb_id()));
                                selectedUsers.add(qbUser);

                                ArrayList<Integer> opponentsList = CollectionsUtils.getIdsSelectedOpponents(selectedUsers);
                                QBRTCTypes.QBConferenceType conferenceType = false
                                        ? QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO
                                        : QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_AUDIO;
                                QBRTCClient qbrtcClient = QBRTCClient.getInstance(getApplicationContext());
                                QBRTCSession newQbRtcSession = qbrtcClient.createNewSessionWithOpponents(opponentsList, conferenceType);
                                VideoCallSessionManager.getInstance(GroupInfoActivity.this).setCurrentSession(newQbRtcSession);
                                CallActivity.start(GroupInfoActivity.this, false, "");
                                Log.d(TAG, "conferenceType = " + conferenceType);
                                //  sendPushService("calling");
                                // dialogType = "clr";
                            } catch (IllegalStateException e) {
                                e.printStackTrace();
                            }
                        } else {
                            CommonUtils.requestPermissionCamera((Activity) context);
                            CommonUtils.requestPermissionCallPhone((Activity) context);
                        }
*/
            }
        });


        layoutchatinfoBinding.tvVideoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             /*   if (CommonUtils.checkPermissionCamera((Activity) context)) {
                    try {
                        Collection<QBUser> selectedUsers = new ArrayList<>();
                        QBUser qbUser = new QBUser();
                        qbUser.setFullName("");
                        qbUser.setId(Integer.valueOf(groupMemberList.get(position).getQb_id()));
                        selectedUsers.add(qbUser);
                        ArrayList<Integer> opponentsList = CollectionsUtils.getIdsSelectedOpponents(selectedUsers);
                        QBRTCTypes.QBConferenceType conferenceType = true
                                ? QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO
                                : QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_AUDIO;
                        QBRTCClient qbrtcClient = QBRTCClient.getInstance(getApplicationContext());
                        QBRTCSession newQbRtcSession = qbrtcClient.createNewSessionWithOpponents(opponentsList, conferenceType);
                        VideoCallSessionManager.getInstance(GroupInfoActivity.this).setCurrentSession(newQbRtcSession);
                        CallActivity.start(GroupInfoActivity.this, false, "");
                        Log.d(TAG, "conferenceType = " + conferenceType);

                        // dialogType = "clr";
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                } else {
                    CommonUtils.requestPermissionCamera((Activity) context);
                    CommonUtils.requestPermissionCallPhone((Activity) context);
                }*/
            }
        });
        layoutchatinfoBinding.tvMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                intent = new Intent(context, ChatsScreenFrgActivity.class);
              /*  intent.putExtra(AppConstants.ASSOC_ID, groupMemberList.get(position).getId());
                intent.putExtra(AppConstants.NAME, groupMemberList.get(position).getName());
                intent.putExtra(AppConstants.NAME, groupMemberList.get(position).getName());
                intent.putExtra(AppConstants.QB_ID, groupMemberList.get(position).getId());
                intent.putExtra(AppConstants.FROM, AppConstants.CHAT_FRAGMENT);
           */     startActivity(intent);
            }});
        layoutchatinfoBinding.tvInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent = new Intent(context, MyProfileActivity.class);
           //     intent.putExtra("user_id", groupMemberList.get(position).getId());
                startActivity(intent);
            }});
        layoutchatinfoBinding.tvRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* if (!PrefManager.getInstance(context).getUserId().equalsIgnoreCase(groupMemberList.get(position).getId())) {
                    if (roomsBean.isIs_broadcast()) {
                        arlDeletedIds = new ArrayList<>();
                        arlAddIds = new ArrayList<>();
                        arlDeletedIds.add(groupMemberList.get(position).getId());
                        EditBroadCastModel model = new EditBroadCastModel();
                        model.setBroadcast_id(roomsBean.getAssoc_id());
                        model.setDelete_member_ids(arlDeletedIds);
                        model.setAdd_member_ids(arlAddIds);
                        editBroadCast(model, position, "delete");
                    } else {
                        callRemoveGroupMemberAPI(groupMemberList.get(position).getId(), position);
                    }
                }*/
            }});



    }

    @Override
    public void setToolBar() {
        binding.toolbarGroupInfo.ivLeft.setVisibility(View.VISIBLE);
        binding.toolbarGroupInfo.ivEdit.setVisibility(View.GONE);
        binding.toolbarGroupInfo.ivLeft.setImageResource(R.mipmap.back_icon);
        binding.toolbarGroupInfo.tvHeader.setVisibility(View.VISIBLE);
        binding.toolbarGroupInfo.tvHeader.setText("John Thomas");
        binding.toolbarGroupInfo.tvRighttext.setVisibility(View.GONE);
        binding.toolbarGroupInfo.tvRighttext.setText(R.string.done);

    }

    @Override
    public void setOnClickListener() {
        binding.toolbarGroupInfo.ivLeft.setOnClickListener(this);
        binding.toolbarGroupInfo.tvHeader.setOnClickListener(this);
        binding.toolbarGroupInfo.tvRighttext.setOnClickListener(this);
        binding.toolbarGroupInfo.ivEdit.setOnClickListener(this);
        binding.tvMediaDocsLinks.setOnClickListener(this);
        binding.tvMuteNotificationsGroup.setOnClickListener(this);
        binding.tvStarredMessageGroup.setOnClickListener(this);
        binding.tvWallaperGroup.setOnClickListener(this);
        binding.tvExitGroup.setOnClickListener(this);
        binding.tvClearChat.setOnClickListener(this);
        binding.tvReportSpam.setOnClickListener(this);
        binding.tvAddMembers.setOnClickListener(this);
        binding.ivImage.setOnClickListener(this);
        binding.tvView.setOnClickListener(this);
       // binding.ivEditName.setOnClickListener(this);
        binding.tvCreate.setOnClickListener(this);
        binding.tvAddExisting.setOnClickListener(this);
        binding.tvShare.setOnClickListener(this);
        binding.tvChatSearch.setOnClickListener(this);
       /* binding.llUserName.setOnClickListener(this);
        binding.tvUserName.setOnClickListener(this);*/
        binding.ivMessage.setOnClickListener(this);
        binding.ivCall.setOnClickListener(this);
        binding.ivVideo.setOnClickListener(this);
        binding.tvMediaDocsLinks.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivLeft:
                onBackPressed();

                break;
          /*  case R.id.ivEditName:
                if(!isEdit){
                    binding.ivEditName.setEnabled(true);
                    isEdit=true;
                }else {
                    if(binding.tvGroupName.getText().toString().trim().length()==0){
                        callEditGroupAPI(binding.tvGroupName.getText().toString().trim());
                    }else {
                        CommonUtils.showToast(context,"Please enter name.");
                    }
                }
                break;*/
            case R.id.tvRighttext:
                onBackPressed();
                break;
            case R.id.tvMediaDocsLinks:

                intent=new Intent(context,PhotoGalleryActivity.class);
              /*  intent.putExtra(AppConstants.ROOM_ID,roomID);
                intent.putExtra(AppConstants.NAME,roomsBean.getName());*/
                startActivity(intent);
                break;
            case R.id.tvWallaperGroup:
                dialogChatWallpaper();
                break;

            case R.id.tvView:
                ToastUtils.showToastShort(context,"Work in progress");
                break;

            case R.id.tvChat:
                ToastUtils.showToastShort(context,"Work in progress");
                break;


            case R.id.ivImage:
              /*  if (roomsBean.isIs_group()){
                    if (!CheckPermission.checkCameraPermission(context)) {
                        CheckPermission.requestCameraPermission((Activity) context, CheckPermission.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                    } else {
                    }
                }*/
                addPhotoDialog();

                break;

            case R.id.tvReportSpam:
                reportUserDialog();
                break;

            case R.id.tvAddMembers:
                intent=new Intent(context,NewGroupNextActivity.class);
               /* intent.putExtra("selectedIds",groupMemberList);
                intent.putExtra(AppConstants.FROM, GroupEditActivity.class.getSimpleName());
                intent.putExtra(AppConstants.NAME, roomsBean.getName());
                intent.putExtra(AppConstants.ASSOC_ID, roomsBean.getAssoc_id());*/
                startActivityForResult(intent,1001);
                break;
            case R.id.tvMuteNotificationsGroup:
                CommonUtils.showToast(context,"Work in progress");
               /* if (roomsBean.isIs_mute()){
                    if(CommonUtils.isOnline(context)) {
                        callUnmuteRoomAPI(false);
                    }else {
                        ToastUtils.showToastShort(context,context.getString(R.string.internet_alert_msg));
                    }
                }
                else
                {
                    muteNotifications();
                }*/
                break;

            case R.id.ivEdit:
                startActivity(new Intent(context, GroupEditActivity.class));
                       /* putExtra(AppConstants.ASSOC_ID, roomsBean.getAssoc_id()).
                        putExtra(AppConstants.ROOM_ID, roomID).
                        putExtra(AppConstants.PARTICIPANTS, groupMemberList));*/
                break;

            case R.id.tvClearChat:
                CommonUtils.showToast(context,"Work in progress");
              /*  if(CommonUtils.isOnline(context)) {
                    callClearChatAPI();
                }
                else
                {
                    ToastUtils.showToastShort(context,context.getString(R.string.internet_alert_msg));
                }*/
                break;
            case R.id.tvStarredMessageGroup:
                startActivity(new Intent(context, StarredMessageActivity.class));
                break;

            case R.id.tvExitGroup:
               switch (binding.tvExitGroup.getText().toString().trim()){
                   case "Exit Group":
                       CommonUtils.showToast(context,"Work in progress");

                    /*   if(CommonUtils.isOnline(context)) {
                           callExitGroupAPI(roomsBean.getAssoc_id());
                       }
                       else
                       {
                           ToastUtils.showToastShort(context,context.getString(R.string.internet_alert_msg));
                       }*/
                       break;
                   case "Block Contact":
                       CommonUtils.showToast(context,"Work in progress");
                      /* if(CommonUtils.isOnline(context)) {
                           callBlockContactAPI();
                       }
                       else
                       {
                           ToastUtils.showToastShort(context,context.getString(R.string.internet_alert_msg));
                       }*/
                       break;
                   case "Unblock Contact":
                       CommonUtils.showToast(context,"Work in progress");

                    /*   if(CommonUtils.isOnline(context)) {
                           callUnblockContactAPI();
                       }
                       else
                       {
                           ToastUtils.showToastShort(context,context.getString(R.string.internet_alert_msg));
                       }*/
                       break;
                   case "Delete Broadcast list":
                       CommonUtils.showToast(context,"Work in progress");

                    /*   if(CommonUtils.isOnline(context)) {
                           ClearRoomRequest apiRequest=new ClearRoomRequest();
                           ArrayList<String> list=new ArrayList<>();
                           list.add(roomID);
                           apiRequest.setRoom_ids(list);
                           callClearRoomAPI(apiRequest);
                       }
                       else
                       {
                           ToastUtils.showToastShort(context,context.getString(R.string.internet_alert_msg));
                       }*/

                       break;

               }

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
                            type="normal";
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

    /*
   This method is being used for taking picture from camera
   */
    public void takePicture(Activity context, String fileName) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            Uri mImageCaptureUri;
            mImageCaptureUri = Uri.fromFile(new File(context.getExternalFilesDir("temp"), fileName + ".jpg"));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, TAKE_PICTURE);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }


    private void muteNotifications() {

        final CharSequence[] items = {getString(R.string.eighthours), getString(R.string.oneweek), getString(R.string.onemonth), getString(R.string.oneyear)};
        new MaterialDialog.Builder(context)
                .title(R.string.mutenotifications).titleGravity(GravityEnum.CENTER)
                .items(items)
                .itemsColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()))
                .backgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeStory()))
                .titleColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()))
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {

                        if (items[position].equals(getString(R.string.eighthours))) {
                            muteTime = "8";
                            dialog.dismiss();
                        } else if (items[position].equals(getString(R.string.oneweek))) {
                            muteTime = "168";
                            dialog.dismiss();
                        } else if (items[position].equals(getString(R.string.onemonth))) {
                            muteTime = "730.001";
                            dialog.dismiss();
                        } else if (items[position].equals(getString(R.string.oneyear))) {
                            muteTime = "8760.00240024";
                            dialog.dismiss();
                        } else {
                            dialog.dismiss();
                        }
                        /*if(CommonUtils.isOnline(context)) {
                            callMuteRoomAPI(true, muteTime);
                        }
                        else
                        {
                            ToastUtils.showToastShort(context,context.getString(R.string.internet_alert_msg));
                        }*/


                    }
                }).negativeText(R.string.cancel).onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        }).show();
    }

    private void reportUserDialog() {
        final CharSequence[] items = {Html.fromHtml("<font color = '#FC2B2B'>Its Spam</font>"), Html.fromHtml("<font color = '#FC2B2B'>Report this Profile</font>"), Html.fromHtml("<font color = '#FC2B2B'>Inappropriate</font>")};

        new MaterialDialog.Builder(context)
                .title(R.string.understandthereason).titleGravity(GravityEnum.CENTER)
                .items(items)
                .itemsColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()))
                .backgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeStory()))
                .titleColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()))
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {


                        CommonUtils.showToast(context,"Work in Progress");
                        dialog.dismiss();

                       /* if (position == 0) {
                            // reportUSerScreem();
                            if(CommonUtils.isOnline(context)) {
                                apiReportUser("Its Spam");
                            }
                            else
                            {
                                ToastUtils.showToastShort(context,context.getString(R.string.internet_alert_msg));
                            }
                            dialog.dismiss();
                        } else if (position == 1) {
                            // reportUSerScreem();
                            if(CommonUtils.isOnline(context)) {
                                apiReportUser("Report This Profile");
                            }
                            else
                            {
                                ToastUtils.showToastShort(context,context.getString(R.string.internet_alert_msg));
                            }
                            dialog.dismiss();
                        } else if (position == 2) {
                            // reportUSerScreem();
                            if(CommonUtils.isOnline(context)) {
                                apiReportUser("Inappropriate");
                            }
                            else
                            {
                                ToastUtils.showToastShort(context,context.getString(R.string.internet_alert_msg));
                            }
                            dialog.dismiss();
                        } else {
                            dialog.dismiss();
                        }*/
                    }
                }).negativeText(R.string.cancel).onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        }).show();

    }

    private void dialogChatWallpaper() {


        final CharSequence[] items = {getString(R.string.default_wallpaper), getString(R.string.solid_color), getString(R.string.themes), getString(R.string.gallery), getString(R.string.no_wallpaper)};
        new MaterialDialog.Builder(context)
                .title(R.string.helpusunderstand).titleGravity(GravityEnum.CENTER)
                .items(items)
                .itemsColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()))
                .backgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeStory()))
                .titleColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()))
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {

                        if (position == 0) {
                            ToastUtils.showToastShort(context, "Default wallpaper applied");
                            WallpaperModel wallpaperModel=new WallpaperModel();
                            wallpaperModel.setType("color");
                            wallpaperModel.setColor_code("#f5fffefe");
                            wallpaperModel.setImage("");
                            RoomsBackgroundTable backgroundTable=new RoomsBackgroundTable(context);
                          /*  if(!backgroundTable.isExist(roomID)) {
                                backgroundTable.saveBackground(wallpaperModel, roomID);
                            }else {
                                backgroundTable.updateBackGround(wallpaperModel, roomID);
                            }*/

                        } else if (position == 1) {
                            Intent intent=new Intent(context,SolidColorSelectionActivity.class);
                            intent.putExtra(AppConstants.ROOM_ID,roomID);
                            intent.putExtra(AppConstants.FROM,"single");
                            startActivity(intent);

                        } else if (position == 2) {
                            //Intent intent=new Intent(context,ThemeSelectionActivity.class);
                            Intent intent=new Intent(context,SolidColorSelectionActivity.class);
                            intent.putExtra(AppConstants.ROOM_ID,roomID);
                            intent.putExtra(AppConstants.FROM,"single");
                            startActivity(intent);
                        } else if (position == 3) {
                            type="wallpaper";
                            image = System.currentTimeMillis() + "_photo.png";
                            openGallery();
                        } else if (position == 4) {
                            WallpaperModel wallpaperModel=new WallpaperModel();
                            wallpaperModel.setType("color");
                            wallpaperModel.setColor_code("#f5fffefe");
                            wallpaperModel.setImage("");
                            RoomsBackgroundTable backgroundTable=new RoomsBackgroundTable(context);
                            if(!backgroundTable.isExist(roomID)) {
                                backgroundTable.saveBackground(wallpaperModel, roomID);
                            }else {
                                backgroundTable.updateBackGround(wallpaperModel, roomID);
                            }
                            ToastUtils.showToastShort(context, "Wallpaper Removed");
                        } else {
                            dialog.dismiss();
                        }
                    }
                }).show();


    }

    public void openGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, TakePictureUtils.PICK_GALLERY);
    }

    /**
     * method for Exit group
     */
    private void callExitGroupAPI(String id) {
        if(CommonUtils.isOnline(context))
        {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("group_id", id);

            Call<GetRoomResonse> call = ApiExecutor.getClient(context).apiLeaveGroup(jsonObject);
            call.enqueue(new Callback<GetRoomResonse>() {
                @Override
                public void onResponse(@NonNull Call<GetRoomResonse> call, @NonNull final Response<GetRoomResonse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            if (response.body().getResponseMessage() != null) {
                                CommonUtils.showToast(context, response.body().getResponseMessage());
                            }
                        }
                        else
                        {
                            ToastUtils.showToastShort(context,context.getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GetRoomResonse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(context,context.getString(R.string.server_error_msg));
                }
            });
        }
        else
        {
            ToastUtils.showToastShort(context,context.getString(R.string.internet_alert_msg));
        }
    }

    /**
     * method for block contact
     */
    private void callBlockContactAPI() {
        if(CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("room_id", roomID);
            jsonObject.addProperty("blocker_id", CommonUtils.getPreferencesString(context, AppConstants.USER_ID));

            Call<GetRoomResonse> call = ApiExecutor.getClient(context).apiBlockChatUser(jsonObject);
            call.enqueue(new Callback<GetRoomResonse>() {
                @Override
                public void onResponse(@NonNull Call<GetRoomResonse> call, @NonNull final Response<GetRoomResonse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            if (response.body().getResponseMessage() != null) {
                                CommonUtils.showToast(context, response.body().getResponseMessage());
                                binding.tvExitGroup.setText("Unblock Contact");
                                RoomsTable roomsTable=new RoomsTable(context);
                                roomsTable.updateBlockStatus(true,roomID);
                                roomsTable.closeDB();
                            }
                        }
                        else
                        {
                            ToastUtils.showToastShort(context,context.getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GetRoomResonse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(context,context.getString(R.string.server_error_msg));
                }
            });
        }
        else
        {
            ToastUtils.showToastShort(context,context.getString(R.string.internet_alert_msg));
        }
    }

    /**
     * method for unblock contact
     */
    private void callUnblockContactAPI() {
        if(CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("room_id", roomID);
            jsonObject.addProperty("blocker_id", CommonUtils.getPreferencesString(context, AppConstants.USER_ID));

            Call<GetRoomResonse> call = ApiExecutor.getClient(context).apiUnblockChatUser(jsonObject);
            call.enqueue(new Callback<GetRoomResonse>() {
                @Override
                public void onResponse(@NonNull Call<GetRoomResonse> call, @NonNull final Response<GetRoomResonse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            if (response.body().getResponseMessage() != null) {
                                CommonUtils.showToast(context, response.body().getResponseMessage());
                                binding.tvExitGroup.setText("Block Contact");
                                RoomsTable roomsTable=new RoomsTable(context);
                                roomsTable.updateBlockStatus(false,roomID);
                                roomsTable.closeDB();
                            }
                        }
                        else
                        {
                            ToastUtils.showToastShort(context,context.getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GetRoomResonse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(context,context.getString(R.string.server_error_msg));
                }
            });
        }
        else
        {
            ToastUtils.showToastShort(context,context.getString(R.string.internet_alert_msg));
        }
    }

    /**
     * method to mute room api
     */
    private void callUnmuteRoomAPI(final boolean mute) {
        if(CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("room_id", roomID);
            jsonObject.addProperty("mute", mute);

            Call<GetRoomResonse> call = ApiExecutor.getClient(context).apiMuteRoom(jsonObject);
            call.enqueue(new Callback<GetRoomResonse>() {
                @Override
                public void onResponse(@NonNull Call<GetRoomResonse> call, @NonNull final Response<GetRoomResonse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            if (response.body().getResponseMessage() != null) {
                                CommonUtils.showToast(context, response.body().getResponseMessage());

                                binding.tvMuteNotificationsGroup.setText(getString(R.string.mute_notification));
                              //  binding.tvMuteNotificationsGroup.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.notification_icon, 0,0,0);
                                binding.ivNotification.setImageResource(R.mipmap.notification_icon);

                                roomsBean.setIs_mute(mute);
                                RoomsTable roomsTable=new RoomsTable(context);
                                roomsTable.updateIsMuteStatus(mute,roomID);
                                roomsTable.closeDB();
                            }
                        }
                        else
                        {
                            ToastUtils.showToastShort(context,context.getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GetRoomResonse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(context,context.getString(R.string.server_error_msg));
                }
            });
        }
        else
        {
            ToastUtils.showToastShort(context,context.getString(R.string.internet_alert_msg));
        }
    }

    /**
     * method to mute room api
     */
    private void callClearChatAPI() {
        if(CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("room_id", roomID);

            Call<GetRoomResonse> call = ApiExecutor.getClient(context).apiClearChat(jsonObject);
            call.enqueue(new Callback<GetRoomResonse>() {
                @Override
                public void onResponse(@NonNull Call<GetRoomResonse> call, @NonNull final Response<GetRoomResonse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            if (response.body().getResponseMessage() != null) {
                                CommonUtils.showToast(context, response.body().getResponseMessage());
                                RoomsTable roomsTable=new RoomsTable(context);
                                roomsTable.deleteRoomChat(roomID);
                                roomsTable.closeDB();
                            }
                        }
                        else
                        {
                            ToastUtils.showToastShort(context,context.getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GetRoomResonse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(context,context.getString(R.string.server_error_msg));
                }
            });
        }
        else
        {
            ToastUtils.showToastShort(context,context.getString(R.string.internet_alert_msg));
        }
    }

    /**
     * method to call group member api
     */
    private void callGroupMemberListAPI(String action) {
        if(CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();

            JsonObject jsonObject = new JsonObject();

            jsonObject.addProperty("group_id", roomsBean.getAssoc_id());
            jsonObject.addProperty("do", action);

            Call<GroupMemberResonse> call = ApiExecutor.getClient(context).apiGroupMemberList(jsonObject);
            call.enqueue(new Callback<GroupMemberResonse>() {
                @Override
                public void onResponse(@NonNull Call<GroupMemberResonse> call, @NonNull final Response<GroupMemberResonse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            if (response.body().getResponseMessage() != null) {
//                                CommonUtils.showToast(context, response.body().getResponseMessage());
                                groupMemberList.clear();
                                if (response.body().getMembers() != null && response.body().getMembers().size() > 0) {
                                    groupMemberList.addAll(response.body().getMembers());
                                    binding.tvNumberOfParticipants.setText(response.body().getMembers().size()+" Participants");
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }
                        else
                        {
                            ToastUtils.showToastShort(context,context.getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GroupMemberResonse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(context,context.getString(R.string.server_error_msg));
                }
            });
        }
        else
        {
            ToastUtils.showToastShort(context,context.getString(R.string.internet_alert_msg));
        }
    }

    /**
     * method for create story api
     */
    private void callMuteRoomAPI(final boolean mute, String time) {
        if(CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("room_id", roomID);
            jsonObject.addProperty("mute", mute);
            jsonObject.addProperty("time", time);

            Call<GetRoomResonse> call = ApiExecutor.getClient(context).apiMuteRoom(jsonObject);
            call.enqueue(new Callback<GetRoomResonse>() {
                @Override
                public void onResponse(@NonNull Call<GetRoomResonse> call, @NonNull final Response<GetRoomResonse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            if (response.body().getResponseMessage() != null) {
                                CommonUtils.showToast(context, response.body().getResponseMessage());

                                binding.tvMuteNotificationsGroup.setText(getString(R.string.unmute_notification));
                              //  binding.tvMuteNotificationsGroup.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.mute_notification_icon, 0,0,0);
                                binding.ivNotification.setImageResource(R.mipmap.mute_notification_icon);

                                RoomsTable roomsTable=new RoomsTable(context);
                                roomsTable.updateIsMuteStatus(mute,roomID);
                                roomsTable.closeDB();
                            }
                        }
                        else
                        {
                            ToastUtils.showToastShort(context,context.getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GetRoomResonse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(context,context.getString(R.string.server_error_msg));
                }
            });
        }
        else
        {
            ToastUtils.showToastShort(context,context.getString(R.string.internet_alert_msg));
        }
    }

    private void apiReportUser(String reason) {
        if(CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("group_id",roomsBean.getAssoc_id());
            jsonObject.addProperty("reason",reason);

            Call<ReportPostResponse> call = ApiExecutor.getClient(context).apiReportUser(jsonObject);
            call.enqueue(new Callback<ReportPostResponse>() {
                @Override
                public void onResponse(@NonNull Call<ReportPostResponse> call, @NonNull final Response<ReportPostResponse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            ToastUtils.showToastShort(context,response.body().getResponseMessage());
                        }
                        else
                        {
                            ToastUtils.showToastShort(context,context.getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(@NonNull Call<ReportPostResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(context,context.getString(R.string.server_error_msg));
                }
            });
        }
        else
        {
            ToastUtils.showToastShort(context,context.getString(R.string.internet_alert_msg));
        }
    }

    private void callClearRoomAPI(final ClearRoomRequest clearRoomRequest) {
        if(CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();

            Call<GetRoomResonse> call = ApiExecutor.getClient(context).apiClearRoom(clearRoomRequest);
            call.enqueue(new Callback<GetRoomResonse>() {
                @Override
                public void onResponse(@NonNull Call<GetRoomResonse> call, @NonNull final Response<GetRoomResonse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            if (response.body().getResponseMessage() != null) {
                                CommonUtils.showToast(context, response.body().getResponseMessage());
                            }
                            RoomsTable roomsTable =new RoomsTable(context);
                            roomsTable.deleteRoomChat(clearRoomRequest.getRoom_ids().get(0));
                            roomsTable.closeDB();
                            Intent intent=new Intent(context,HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            ToastUtils.showToastShort(context,context.getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GetRoomResonse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(context,context.getString(R.string.server_error_msg));
                }
            });
        }
        else
        {
            ToastUtils.showToastShort(context,context.getString(R.string.internet_alert_msg));
        }
    }


    private void editBroadCast(final EditBroadCastModel editBroadCastModel, final int position, final String from) {
        if(CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();
            Call<ApiResponse> call = ApiExecutor.getClient(context).apiEditBroadCast(editBroadCastModel);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            if (response.body().getResponseMessage() != null) {
                                CommonUtils.showToast(context, response.body().getResponseMessage());
                               switch (from){
                                   case "add":
                                       callGroupMemberListAPI("broadcast");
                                       break;
                                   case "delete":
                                       groupMemberList.remove(position);
                                       adapter.notifyDataSetChanged();
                                       binding.tvNumberOfParticipants.setText(String.valueOf(groupMemberList.size())+" Participants");
                                       break;
                               }
                            }
                        }
                        else
                        {
                            ToastUtils.showToastShort(context,context.getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(context,context.getString(R.string.server_error_msg));
                }
            });
        }
        else
        {
            ToastUtils.showToastShort(context,context.getString(R.string.internet_alert_msg));
        }
    }


    private void showMenu(View view, final int position) {
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
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.actionVoiceCall:
                        if (CommonUtils.checkPermissionCamera((Activity) context)) {
                            try {
                                Collection<QBUser> selectedUsers = new ArrayList<>();
                                QBUser qbUser = new QBUser();
                                qbUser.setFullName("");
                                qbUser.setId(Integer.valueOf(groupMemberList.get(position).getQb_id()));
                                selectedUsers.add(qbUser);

                                ArrayList<Integer> opponentsList = CollectionsUtils.getIdsSelectedOpponents(selectedUsers);
                                QBRTCTypes.QBConferenceType conferenceType = false
                                        ? QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO
                                        : QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_AUDIO;
                                QBRTCClient qbrtcClient = QBRTCClient.getInstance(getApplicationContext());
                                QBRTCSession newQbRtcSession = qbrtcClient.createNewSessionWithOpponents(opponentsList, conferenceType);
                                VideoCallSessionManager.getInstance(GroupInfoActivity.this).setCurrentSession(newQbRtcSession);
                                CallActivity.start(GroupInfoActivity.this, false, "");
                                Log.d(TAG, "conferenceType = " + conferenceType);
                                //  sendPushService("calling");
                                // dialogType = "clr";
                            } catch (IllegalStateException e) {
                                e.printStackTrace();
                            }
                        } else {
                            CommonUtils.requestPermissionCamera((Activity) context);
                            CommonUtils.requestPermissionCallPhone((Activity) context);
                        }
                        return true;
                    case R.id.actionVideoCall:
                        if (CommonUtils.checkPermissionCamera((Activity) context)) {
                            try {
                                Collection<QBUser> selectedUsers = new ArrayList<>();
                                QBUser qbUser = new QBUser();
                                qbUser.setFullName("");
                                qbUser.setId(Integer.valueOf(groupMemberList.get(position).getQb_id()));
                                selectedUsers.add(qbUser);
                                ArrayList<Integer> opponentsList = CollectionsUtils.getIdsSelectedOpponents(selectedUsers);
                                QBRTCTypes.QBConferenceType conferenceType = true
                                        ? QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO
                                        : QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_AUDIO;
                                QBRTCClient qbrtcClient = QBRTCClient.getInstance(getApplicationContext());
                                QBRTCSession newQbRtcSession = qbrtcClient.createNewSessionWithOpponents(opponentsList, conferenceType);
                                VideoCallSessionManager.getInstance(GroupInfoActivity.this).setCurrentSession(newQbRtcSession);
                                CallActivity.start(GroupInfoActivity.this, false, "");
                                Log.d(TAG, "conferenceType = " + conferenceType);

                                // dialogType = "clr";
                            } catch (IllegalStateException e) {
                                e.printStackTrace();
                            }
                        } else {
                            CommonUtils.requestPermissionCamera((Activity) context);
                            CommonUtils.requestPermissionCallPhone((Activity) context);
                        }

                        return true;
                    case R.id.actionMessage:
                        intent=new Intent(context,ChatsScreenFrgActivity.class);
                        intent.putExtra(AppConstants.ASSOC_ID,groupMemberList.get(position).getId());
                        intent.putExtra(AppConstants.NAME,groupMemberList.get(position).getName());
                        intent.putExtra(AppConstants.NAME,groupMemberList.get(position).getName());
                            intent.putExtra(AppConstants.QB_ID,groupMemberList.get(position).getId());
                        intent.putExtra(AppConstants.FROM,AppConstants.CHAT_FRAGMENT);
                        startActivity(intent);
                        return true;
                    case R.id.actionInfo:
                        intent=new Intent(context,MyProfileActivity.class);
                        intent.putExtra("user_id",groupMemberList.get(position).getId());
                        startActivity(intent);
                        return true;
                    case R.id.actionRemove:
                        if(!PrefManager.getInstance(context).getUserId().equalsIgnoreCase(groupMemberList.get(position).getId())){
                            if (roomsBean.isIs_broadcast()){
                                arlDeletedIds = new ArrayList<>();
                                arlAddIds = new ArrayList<>();
                                arlDeletedIds.add(groupMemberList.get(position).getId());
                                EditBroadCastModel model = new EditBroadCastModel();
                                model.setBroadcast_id(roomsBean.getAssoc_id());
                                model.setDelete_member_ids(arlDeletedIds);
                                model.setAdd_member_ids(arlAddIds);
                                editBroadCast(model, position, "delete");
                            }else {
                                callRemoveGroupMemberAPI(groupMemberList.get(position).getId(),position);
                            }
                        }
                        return true;

                    default:
                        return false;
                }
            }
        });
        popup.inflate(R.menu.menu_chat_info);
        popup.show();

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK) {
            if (requestCode == 1001) {
                List<String> strings = (List<String>) data.getSerializableExtra("selectedIds");
                if (roomsBean.isIs_broadcast()){
                    if (strings.size() > 0) {
                        arlDeletedIds=new ArrayList<>();
                        arlAddIds=new ArrayList<>();
                        for (int i = 0; i < groupMemberList.size(); i++) {
                            for (int j = 0; j <strings.size() ; j++) {
                                if (!groupMemberList.get(i).getId().equalsIgnoreCase(strings.get(j))) {
                                    arlAddIds.add(strings.get(j));
                                }
                            }
                        }
                        EditBroadCastModel model = new EditBroadCastModel();
                        model.setBroadcast_id(roomsBean.getAssoc_id());
                        model.setDelete_member_ids(arlDeletedIds);
                        model.setAdd_member_ids(arlAddIds);
                        editBroadCast(model, 0, "add");
                    }

                } else {
                    if (strings.size() > 0) {
                        arlAddIds=new ArrayList<>();
                        for (int i = 0; i < groupMemberList.size(); i++) {
                            for (int j = 0; j <strings.size() ; j++) {
                                if (!groupMemberList.get(i).getId().equalsIgnoreCase(strings.get(j))) {
                                    arlAddIds.add(strings.get(j));
                                }
                            }
                        }
                        callAddGroupMemberAPI(arlAddIds);
                    }
                }
            } else if (requestCode == TakePictureUtils.PICK_GALLERY) {
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        InputStream inputStream = context.getContentResolver().openInputStream(data.getData());
                        FileOutputStream fileOutputStream = new FileOutputStream(new File(this.context.getExternalFilesDir("temp"), image + ".jpg"));
                        TakePictureUtils.copyStream(inputStream, fileOutputStream);
                        fileOutputStream.close();
                        inputStream.close();
                        startCropImage(this, image + ".jpg");
                    } catch (Exception e) {
                        //CommonUtils.showToast(mContext, getString(R.string.error_in_picture));

                    }
                }
            } else if (requestCode == TakePictureUtils.TAKE_PICTURE) {
                if (resultCode == Activity.RESULT_OK) {
                    startCropImage(this, image + ".jpg");
                }
            } else if (requestCode == CROP_FROM_CAMERA) {
                //  imageName="picture";
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        String path = data.getStringExtra(CropImage.IMAGE_PATH);
                        if(type.equalsIgnoreCase("normal")) {
                            imageFile = new File(path);
                            Picasso.with(context).load(imageFile).placeholder(R.mipmap.profile_icon).memoryPolicy(MemoryPolicy.NO_CACHE).into(binding.ivImage);
                            callEditGroupAPI(roomsBean.getName());
                        }else {
                            WallpaperModel wallpaperModel=new WallpaperModel();
                            wallpaperModel.setColor_code("");
                            wallpaperModel.setType("image");
                            wallpaperModel.setImage(path);
                            RoomsBackgroundTable backgroundTable=new RoomsBackgroundTable(context);
                            if(!backgroundTable.isExist(roomID)) {
                                backgroundTable.saveBackground(wallpaperModel, roomID);
                            }else {
                                backgroundTable.updateBackGround(wallpaperModel, roomID);
                            }
                            ToastUtils.showToastShort(context, "Wallpaper changed successfully.");
                        }
                    }
                }
            }
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

    /**
     * method for create post api
     */
    private void callRemoveGroupMemberAPI(String userId,final int myPosition) {
        if(CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("user_id", userId);
            jsonObject.addProperty("group_id", roomsBean.getAssoc_id());

            Call<ApiResponse> call = ApiExecutor.getClient(context).apiRemoveGroupMember(jsonObject);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            if (response.body().getResponseMessage() != null){
//                                CommonUtils.showToast(context, response.body().getResponseMessage());
                                groupMemberList.remove(myPosition);
                                adapter.notifyItemRemoved(myPosition);
                                binding.tvNumberOfParticipants.setText(String.valueOf(groupMemberList.size())+" Participants");
                            }
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
                    LogUtils.errorLog("Create room failure", "" + ((t != null && t.getMessage() != null) ? t.getMessage() : "---"));
                    ToastUtils.showToastShort(context,getString(R.string.server_error_msg));
                }
            });
        }
        else
        {
            ToastUtils.showToastShort(context,context.getString(R.string.internet_alert_msg));
        }
    }


    /**
     * method for create post api
     */
    private void callEditGroupAPI(String groupName) {
        if(CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();
            RequestBody groupId = null;
            if (groupName != null) {
                groupId = ConverterUtils.parseString(roomsBean.getAssoc_id());
            }
            RequestBody name = null;
            if (groupName != null) {
                name = ConverterUtils.parseString(groupName);
            }
            if (imageFile != null) {
                groupImage = ConverterUtils.getMultipartFromFile("group_image", imageFile);
            }
            Call<ApiResponse> call = ApiExecutor.getClient(context).apiEditGroup(groupId, name, groupImage);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            if (response.body().getResponseMessage() != null){
                                CommonUtils.showToast(context, response.body().getResponseMessage());
                                RoomsTable roomsTable =new RoomsTable(context);
                                roomsTable.updateImage(response.body().getGroups().getGroup_image(),roomID);
                                finish();
                            }
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
                    LogUtils.errorLog("Create room failure", "" + ((t != null && t.getMessage() != null) ? t.getMessage() : "---"));
                    ToastUtils.showToastShort(context,getString(R.string.server_error_msg));
                }
            });
        }
        else
        {
            ToastUtils.showToastShort(context,context.getString(R.string.internet_alert_msg));
        }
    }

    private void callAddGroupMemberAPI(List<String> arlAddIds) {
        if(CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();

            RequestBody groupId = ConverterUtils.parseString(roomsBean.getAssoc_id());

            /*Tagged friend list*/
            Map<String,RequestBody> member_ids = new HashMap<>();
            for (int i = 0; i < arlAddIds.size(); i++) {
                RequestBody requestBody = ConverterUtils.parseString(arlAddIds.get(i));
                member_ids.put("member_ids["+i+"]",requestBody);
            }

            Call<ApiResponse> call = ApiExecutor.getClient(context).apiAddGroupMember(groupId, member_ids);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            if (response.body().getResponseMessage() != null){CommonUtils.showToast(context, response.body().getResponseMessage());
                                callGroupMemberListAPI("group");
                            }
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
                    LogUtils.errorLog("Create room failure", "" + ((t != null && t.getMessage() != null) ? t.getMessage() : "---"));
                    ToastUtils.showToastShort(context,getString(R.string.server_error_msg));
                }
            });
        }
        else
        {
            ToastUtils.showToastShort(context,context.getString(R.string.internet_alert_msg));
        }
    }
}
