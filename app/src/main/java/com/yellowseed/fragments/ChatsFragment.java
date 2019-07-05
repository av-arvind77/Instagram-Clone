package com.yellowseed.fragments;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.multidots.fingerprintauth.AuthErrorCodes;
import com.multidots.fingerprintauth.FingerPrintAuthCallback;
import com.multidots.fingerprintauth.FingerPrintAuthHelper;
import com.yellowseed.R;
import com.yellowseed.activity.CallDetailsActivity;
import com.yellowseed.activity.ChatsScreenFrgActivity;
import com.yellowseed.activity.ContactsChatsActivity;
import com.yellowseed.activity.RecyclerItemTouchHelper;
import com.yellowseed.adapter.ChatFragmentAdapter;
import com.yellowseed.customView.CustomRobotoBoldTextView;
import com.yellowseed.customView.CustomRobotoRegularTextView2;
import com.yellowseed.database.RoomsTable;
import com.yellowseed.databinding.DialogEnterPinBinding;
import com.yellowseed.databinding.DialogHidePostBinding;
import com.yellowseed.databinding.FragmentChatBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.ChatFragmentModel;
import com.yellowseed.model.DirectModel;
import com.yellowseed.model.reqModel.ClearRoomRequest;
import com.yellowseed.model.resModel.GetRoomResonse;
import com.yellowseed.utils.ActivityController;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseFragment;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.LogUtils;
import com.yellowseed.utils.PinEntryView;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ChatsFragment  extends BaseFragment implements FingerPrintAuthCallback, RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, View.OnClickListener {
    public static boolean isCheck = false;
    private FragmentChatBinding binding;
    private Context context;
    private ImageView ivRight;
    private ImageView ivRightSecond;
    private ChatFragmentAdapter adapter;
    private CustomRobotoBoldTextView tvUserImage;
    private CustomRobotoRegularTextView2 tvLeftText;
    private BottomNavigationView bottomNavigationView;
    private int flag = -1;
    private FingerPrintAuthHelper mFingerPrintAuthHelper;
    private ArrayList<ChatFragmentModel> models = new ArrayList<>();
    //  private ArrayList<GetRoomResonse.RoomsBean> models = new ArrayList<>();
//    private ArrayList<DirectModel> models = new ArrayList<>();
    private int totalPage;
    private int currentPage = 1;
    private int myPosition;
    private Themes themes;
    private Dialog dialog;
    private PinEntryView pinEntryView;
    private Boolean darkThemeEnabled;
    private LocalBroadcastManager lbm;
    private String[] names = {"John Thomas", "Jenny Koul", "Mike Keel", "Julie Kite", "Tiny Tim", "Tiny Tim", "Tiny Tim", "Tiny Tim", "John Thomas", "Jenny Koul", "Mike Keel", "Julie Kite", "Tiny Tim"};
    private int[] images = {R.mipmap.profile_icon, R.mipmap.profile_icon2, R.mipmap.profile_icon3, R.mipmap.profile_icon4, R.mipmap.profile_icon, R.mipmap.profile_icon, R.mipmap.profile_icon2, R.mipmap.profile_icon3, R.mipmap.profile_icon4, R.mipmap.profile_icon, R.mipmap.profile_icon, R.mipmap.profile_icon2, R.mipmap.profile_icon3};
    private String[] time = {"10:35 AM", "12:35 AM", "01:00 AM", "3:35 AM", "4:35 AM", "05:00 AM", "05:30 AM", "02:30 AM", "02:30 AM", "10:35 AM", "12:35 AM", "01:00 AM", "3:35 AM"};
    private String[] message = {"I am good say about you", "Hi, how are you?", " You are so cute", "Dear friend... meet me", "Dear friend... meet me", "Dear friend... meet me", "Dear friend... meet me", "Dear friend... meet me", "Dear friend... meet me", "I am good say about you", "Hi, how are you?", " You are so cute", "Dear friend... meet me"};
    private Animation slideLeftanimation,slideRightAnimation;
    public ChatsFragment() {
        // Required empty public constructor
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getStringExtra("key");
            setTheme();
            adapter.notifyDataSetChanged();
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false);
        themes = new Themes(context);
        darkThemeEnabled = CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME);
        View view = binding.getRoot();
        initializedControl();
        mFingerPrintAuthHelper = FingerPrintAuthHelper.getHelper(context, this);
        lbm = LocalBroadcastManager.getInstance(getActivity());
        lbm.registerReceiver(receiver, new IntentFilter("theme_change"));
        setToolBar();
        setOnClickListener();
       /* if (CommonUtils.isOnline(context)) {
            callGetRoomAPI(currentPage);
        } else {
            RoomsTable roomsTable = new RoomsTable(context);
            models.addAll(roomsTable.getAllRooms(context));
            adapter.notifyDataSetChanged();
        }*/
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        //  models.clear();
        setTheme();
        //  mFingerPrintAuthHelper.startAuth();
        isCheck = false;
        tvLeftText.setText(R.string.edit);
        tvUserImage.setVisibility(View.GONE);
        binding.btnDelDoneChats.setVisibility(View.GONE);
        RoomsTable roomsTable = new RoomsTable(context);
        //  models.addAll(roomsTable.getAllRooms(context));
        adapter.notifyDataSetChanged();
    }
    private void setTheme() {
        binding.llMain.setBackgroundColor(ContextCompat.getColor(context, themes.setDarkTheme()));
        binding.etSearchChatFragment.setHintTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkHintColor()));
        binding.etSearchChatFragment.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        Themes.getInstance(context).changeCallIcon(context, ivRightSecond);
        Themes.getInstance(context).changeRightIcon(context, ivRight);
        binding.rvChatFragment.setBackgroundColor(ContextCompat.getColor(context, themes.setDarkTheme()));
        binding.etSearchChatFragment.setBackground(ContextCompat.getDrawable(context, themes.setDarkStoryBackground()));
        tvLeftText.setTextColor(ContextCompat.getColor(context, themes.setTolbarText()));
    }
    private void initView() {
        binding.rvChatFragment.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.rvChatFragment.setLayoutManager(layoutManager);
        binding.rvChatFragment.setItemAnimator(new DefaultItemAnimator());
        binding.rvChatFragment.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        models.clear();
        models.addAll(prepareData());
        adapter = new ChatFragmentAdapter(models, context, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.llChatFraUser:
                        ActivityController.startActivity(context, ChatsScreenFrgActivity.class);
                        break;
                    case R.id.ll_delete:
                        models.remove(position);
                        adapter.notifyItemRemoved(position);
                        break;
                    case R.id.ll_mute:
                        Toast.makeText(context, "UnMute", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.ll_pin:
                        Toast.makeText(context, "UnPin", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.ll_hide:
                        dialogHide();
                        break;
                    case R.id.llSwipe:
                        view.startAnimation(slideRightAnimation);
                        break;
                    default:
                        break;
                }
            }
        });
        binding.rvChatFragment.setAdapter(adapter);
        binding.etSearchChatFragment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    public void filter(String text) {
        ArrayList<ChatFragmentModel> filteredName = new ArrayList<>();
        for (ChatFragmentModel model : models) {
            if (model.getUserChatFraName().toLowerCase().contains(text.toLowerCase())) {
                filteredName.add(model);
            }
        }
        // binding.tvNumberingBroadcast.setText(count + " " +getString(R.string.of)+ " " + filteredName.size());
        adapter.updatedList(filteredName);
    }
    private ArrayList<ChatFragmentModel> prepareData() {
        ArrayList<ChatFragmentModel> data = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            ChatFragmentModel model = new ChatFragmentModel();
            model.setUserChatFraImage(images[i]);
            model.setUserChatFraName(names[i]);
            model.setUserChatFraTime(time[i]);
            model.setUserChatFraContent(message[i]);
            data.add(model);
        }
        return data;
    }
    private void dialogHide() {
        mFingerPrintAuthHelper = FingerPrintAuthHelper.getHelper(context, this);
        DialogHidePostBinding dialogHidePostBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_hide_post, null, false);
        dialog = DialogUtils.createDialog(context, dialogHidePostBinding.getRoot());
        dialogHidePostBinding.llTouch.setBackground(ContextCompat.getDrawable(context, Themes.getInstance(context).setRoundedBlackBackground()));
        dialogHidePostBinding.tvTouchId.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        dialogHidePostBinding.tvEnableHiddenMode.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        dialogHidePostBinding.tvEnterpasscode.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        dialogHidePostBinding.viewLine.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkGreyTextColor()));
        themes.changeHideIcon(context, dialogHidePostBinding.ivTouch);
        dialogHidePostBinding.tvEnterpasscode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialogPin();
            }
        });
        dialogHidePostBinding.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.background_dark);
    }
    private void setPostShareDialoge(final int position) {
        final CharSequence[] items = {getString(R.string.eight_hrs), getString(R.string.one_week), getString(R.string.one_month),
                getString(R.string.one_year)};
        new MaterialDialog.Builder(context)
                .title(R.string.mute_notification_d).titleGravity(GravityEnum.CENTER)
                .items(items)
                .itemsColor(ContextCompat.getColor(context, themes.setDarkThemeText()))
                .backgroundColor(ContextCompat.getColor(context, themes.setDarkThemeStory()))
                .titleColor(ContextCompat.getColor(context, themes.setDarkThemeText()))
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        if (items[position].equals(getString(R.string.eight_hrs))) {
                            //  callMuteRoomAPI(models, true, position);
                        } else if (items[position].equals(getString(R.string.one_week))) {
                            //  callMuteRoomAPI(models, true, position);
                        } else if (items[position].equals(getString(R.string.one_month))) {
                            //   callMuteRoomAPI(models, true, position);
                        } else if (items[position].equals(getString(R.string.one_year))) {
                            //   callMuteRoomAPI(models, true, position);
                        }
                    }
                }).negativeText(R.string.cancel).onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        }).show();
    }
    private void dialogPin() {
        DialogEnterPinBinding dialogEnterPinBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_enter_pin, null, false);
        dialog = DialogUtils.createDialog(context, dialogEnterPinBinding.getRoot());
        dialogEnterPinBinding.llPin.setBackground(ContextCompat.getDrawable(context, Themes.getInstance(context).setRoundedBlackBackground()));
        dialogEnterPinBinding.tvEnterPin.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        themes.changeHideIcon(context, dialogEnterPinBinding.ivPin);
        dialogEnterPinBinding.pinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 4) {
                    dialog.dismiss();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        //  pinEntryView.addTextChangedListener((TextWatcher) this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }
    /*  public void filter(String text) {
          ArrayList<GetRoomResonse.RoomsBean> filteredName = new ArrayList<>();
          for (GetRoomResonse.RoomsBean model : models) {
              if (model != null && model.getName() != null && model.getName().toLowerCase() != null) {
                  if (model.getName().toLowerCase().contains(text.toLowerCase())) {
                      filteredName.add(model);
                  }
              }
          }
          // binding.tvNumberingBroadcast.setText(count + " " +getString(R.string.of)+ " " + filteredName.size());
          if (filteredName != null) {
              adapter.updatedList(filteredName, text);
          }
      }*/
    @Override
    public void initializedControl() {
        slideLeftanimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_in_from_bottom);
        slideRightAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.lefft_anim);
        binding.btnDelDoneChats.setVisibility(View.GONE);
        ivRight = (ImageView) getActivity().findViewById(R.id.ivRight);
        ivRightSecond = (ImageView) getActivity().findViewById(R.id.ivRightSecond);
        tvLeftText = (CustomRobotoRegularTextView2) getActivity().findViewById(R.id.tvLeftText);
        tvUserImage = (CustomRobotoBoldTextView) getActivity().findViewById(R.id.tvUserImage);
        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottomNavigationHomeBottom);
        setTheme();
        initView();        // itemTouch();
    }
    @Override
    public void setToolBar() {
        tvLeftText.setVisibility(View.VISIBLE);
        ivRightSecond.setVisibility(View.VISIBLE);
        tvUserImage.setVisibility(View.GONE);
        ivRightSecond.setImageResource(R.mipmap.call_yellow);
        ivRight.setImageResource(R.mipmap.edit_yellow_right);
    }
    @Override
    public void setOnClickListener() {
//        binding.tvNewBroadCast.setOnClickListener(this);
//        binding.tvnewGroupChat.setOnClickListener(this);
        binding.btnDelDoneChats.setOnClickListener(this);
        ivRight.setOnClickListener(this);
        ivRightSecond.setOnClickListener(this);
        tvLeftText.setOnClickListener(this);
    }
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.tvNewBroadCast:
//                ActivityController.startActivity(context, BroadcastActivity.class);
//                break;
//            case R.id.tvnewGroupChat:
//                ActivityController.startActivity(context, NewGroupNextActivity.class);
//                break;
            case R.id.ivRight:
                ActivityController.startActivity(context, ContactsChatsActivity.class);
                break;
            case R.id.ivRightSecond:
                ActivityController.startActivity(context, CallDetailsActivity.class);
                break;
            case R.id.tvLeftText:
                if (flag == -1) {
                    tvLeftText.setText(R.string.done);
                    bottomNavigationView.setVisibility(View.GONE);
                    binding.btnDelDoneChats.setVisibility(View.GONE);
                    flag = 1;
                    isCheck = true;
                    adapter.notifyDataSetChanged();
                } else {
                    tvLeftText.setText(R.string.edit);
                    bottomNavigationView.setVisibility(View.VISIBLE);
                    binding.btnDelDoneChats.setVisibility(View.GONE);
                    flag = -1;
                    isCheck = false;
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.btnDelDoneChats:
                if (models != null && models.size() > 0) {
                }
                break;
            default:
                break;
        }
    }
    /**
     * method for create story api
     */
  /*  private void callGetRoomAPI(final int pageNo) {
        if (CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("page_no", pageNo);
            Call<GetRoomResonse> call = ApiExecutor.getClient(context).apiGetRoom(jsonObject);
            call.enqueue(new Callback<GetRoomResonse>() {
                @Override
                public void onResponse(@NonNull Call<GetRoomResonse> call, @NonNull final Response<GetRoomResonse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            models.clear();
                            if (response.body().getRooms() != null && response.body().getRooms().size() > 0) {
                                models.addAll(response.body().getRooms());
                                RoomsTable roomsTable = new RoomsTable(context);
                                roomsTable.deleteAllRooms();
                                roomsTable.insertRoomInformation(response.body().getRooms());
                                roomsTable.closeDB();
                            }
                            totalPage = response.body().getTotal_pages();
                            adapter.notifyDataSetChanged();
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
    }*/
/*    private ClearRoomRequest deleteRoomRequest(boolean singleDelete) {
        ClearRoomRequest apiRequest = new ClearRoomRequest();
        if (singleDelete) {
            ArrayList<String> deleteRoomList = new ArrayList<>();
            for (int i = 0; i < models.size(); i++) {
                if (i == myPosition) {
                    deleteRoomList.add(models.get(i).getRoom_id());
                }
            }
            apiRequest.setRoom_ids(deleteRoomList);
        } else {
            ArrayList<String> deleteRoomList = new ArrayList<>();
            for (int i = 0; i < models.size(); i++) {
                if (models.get(i).isIs_checked_for_delete()) {
                    deleteRoomList.add(models.get(i).getRoom_id());
                }
            }
            apiRequest.setRoom_ids(deleteRoomList);
        }
        LogUtils.LOGE(TAG, new Gson().toJson(apiRequest));
        return apiRequest;
    }*/
    /**
     * method for create story api
     */
/*
    private void callClearRoomAPI(final ClearRoomRequest clearRoomRequest, final boolean singleDelete) {
        if (CommonUtils.isOnline(context)) {
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
                            RoomsTable roomsTable = new RoomsTable(context);
                            if (singleDelete) {
                                models.remove(myPosition);
                                adapter.notifyItemRemoved(myPosition);
                                roomsTable.deleteRoomChat(clearRoomRequest.getRoom_ids().get(myPosition));
                            } else {
                                for (int i = 0; i < models.size(); i++) {
                                    if (models.get(i).isIs_checked_for_delete()) {
                                        roomsTable.deleteRoomChat(models.get(i).getRoom_id());
                                        models.remove(i);
                                        i--;
                                    }
                                }
                                tvLeftText.setText(R.string.edit);
                                bottomNavigationView.setVisibility(View.VISIBLE);
                                binding.btnDelDoneChats.setVisibility(View.GONE);
                                isCheck = false;
                                adapter.notifyDataSetChanged();
                            }
                            roomsTable.closeDB();
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
*/
    /**
     * method for create story api
     */
/*
    private void callMuteRoomAPI(final ArrayList<GetRoomResonse.RoomsBean> models, final boolean mute, final int position) {
        if (CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("room_id", models.get(position).getRoom_id());
            jsonObject.addProperty("mute", mute);
            Call<GetRoomResonse> call = ApiExecutor.getClient(context).apiMuteRoom(jsonObject);
            call.enqueue(new Callback<GetRoomResonse>() {
                @Override
                public void onResponse(@NonNull Call<GetRoomResonse> call, @NonNull final Response<GetRoomResonse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            if (response.body().getResponseMessage() != null) {
//                                adapter.getLayout().close();
                                CommonUtils.showToast(context, response.body().getResponseMessage());
                                RoomsTable roomsTable = new RoomsTable(context);
                                models.get(position).setIs_mute(mute);
                                roomsTable.updateIsMuteStatus(mute, models.get(position).getRoom_id());
                                roomsTable.closeDB();
                                adapter.notifyDataSetChanged();
                            }
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
*/
    @Override
    public void onDestroy() {
        lbm.unregisterReceiver(receiver);
        super.onDestroy();
    }
    @Override
    public void onPause() {
        super.onPause();
        mFingerPrintAuthHelper.stopAuth();
    }
    @Override
    public void onNoFingerPrintHardwareFound() {
        ToastUtils.showToastShort(context, "Your device does not have finger print scanner. Please type 1234 to authenticate.");
       /* mAuthMsgTv.setText("Your device does not have finger print scanner. Please type 1234 to authenticate.");
        mSwitcher.showNext();*/
    }
    @Override
    public void onNoFingerPrintRegistered() {
        ToastUtils.showToastShort(context, "There are no finger prints registered on this device. Please register your finger from settings.");
        //   mGoToSettingsBtn.setVisibility(View.VISIBLE);
    }
    @Override
    public void onBelowMarshmallow() {
        Toast.makeText(context, "You are running older version of android that does not support finger print authentication. Please type 1234 to authenticate.", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onAuthSuccess(FingerprintManager.CryptoObject cryptoObject) {
        Toast.makeText(context, "Authentication succeeded.", Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }
    @Override
    public void onAuthFailed(int errorCode, String errorMessage) {
        switch (errorCode) {
            case AuthErrorCodes.CANNOT_RECOGNIZE_ERROR:
                ToastUtils.showToastShort(context, "Cannot recognize your finger print. Please try again.");
                break;
            case AuthErrorCodes.NON_RECOVERABLE_ERROR:
                ToastUtils.showToastShort(context, "Cannot initialize finger print authentication. Please type 1234 to authenticate.");
                break;
            case AuthErrorCodes.RECOVERABLE_ERROR:
                ToastUtils.showToastShort(context, errorMessage);
                break;
        }
    }
}














