package com.yellowseed.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yellowseed.R;
import com.yellowseed.adapter.ChatFragmentAdapter;
import com.yellowseed.adapter.DirectAdapter;
import com.yellowseed.adapter.VotesAdapter;
import com.yellowseed.customView.CustomRobotoRegularTextView2;
import com.yellowseed.database.RoomsTable;
import com.yellowseed.databinding.ActivityDirectChatBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.DirectModel;
import com.yellowseed.model.VotesModel;
import com.yellowseed.model.reqModel.ClearRoomRequest;
import com.yellowseed.model.resModel.GetRoomResonse;
import com.yellowseed.utils.ActivityController;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.LogUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DirectChatActivity extends BaseActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, View.OnClickListener {
    public static boolean isCheck = false;
    private ActivityDirectChatBinding binding;
    private Context context;
    private ImageView ivRight;
    private DirectAdapter adapter;
    private CustomRobotoRegularTextView2 tvLeftText;
    private BottomNavigationView bottomNavigationView;
    private int flag = -1;
    private Dialog dialog;

    private boolean darkThemeEnabled;
    private ArrayList<DirectModel> models=new ArrayList<>();
 //   private ArrayList<GetRoomResonse.RoomsBean> models = new ArrayList<>();
    private int totalPage;
    private int currentPage = 1;
    private int myPosition;
    private Themes themes;
    private String[] names = {"John Thomas", "Jenny Koul", "Mike Keel", "Julie Kite", "Tiny Tim", };
    private int[] images = {R.mipmap.profile_icon, R.mipmap.profile_icon2, R.mipmap.profile_icon3, R.mipmap.profile_icon4, R.mipmap.profile_icon};
     private String[] msg={"hii","Hello","I am good","Call u later","Bye","ok"};
     private String[] time={"10.05 pm","6.30 pm","4.21 pm","Yesterday","12/12/2018"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_direct_chat);
        context = DirectChatActivity.this;
        themes = new Themes(context);
        darkThemeEnabled = CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME);
        initializedControl();
        setToolBar();
        setOnClickListener();


/*
        models.clear();
*/
       /* if (CommonUtils.isOnline(context)) {
            callGetRoomAPI(currentPage);
        } else {
            RoomsTable roomsTable = new RoomsTable(context);
            models.addAll(roomsTable.getAllRooms(context));
            adapter.notifyDataSetChanged();
        }*/
    }

    @Override
    public void onResume() {
        super.onResume();
        isCheck = false;
        tvLeftText.setText(R.string.edit);
        binding.btnDelDoneChats.setVisibility(View.GONE);
    /*    RoomsTable roomsTable = new RoomsTable(context);
        models.addAll(roomsTable.getAllRooms(context));*/
        adapter.notifyDataSetChanged();
    }





    private void setRecycler() {


        binding.rvChatFragment.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvChatFragment.setLayoutManager(layoutManager);

        //For divide the recycler item
        DividerItemDecoration itemDecorator = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(context, R.drawable.divider));
        binding.rvChatFragment.addItemDecoration(itemDecorator);


        models = new ArrayList<>();
        models.addAll(chatData());
        adapter = new DirectAdapter(models, this, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.llDirectUser:
                        ActivityController.startActivity(context, ChatsScreenFrgActivity.class);
                        break;
                    default:
                        break;
                }
            }
        });
        binding.rvChatFragment.setAdapter(adapter);



     /*
        binding.rvChatFragment.setHasFixedSize(true);
        LinearLayoutManager manager=new LinearLayoutManager(context);
        binding.rvChatFragment.setLayoutManager(manager);
        models.addAll(chatData());

        adapter=new ChatFragmentAdapter(models,context, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        binding.rvChatFragment.setAdapter(adapter);*/
    }



 /*   private void initView() {
        binding.rvChatFragment.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvChatFragment.setLayoutManager(layoutManager);
        binding.rvChatFragment.setItemAnimator(new DefaultItemAnimator());
        binding.rvChatFragment.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        models = new ArrayList<>();
        models.addAll(chatData());

        adapter = new ChatFragmentAdapter(models, context, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.llChatFraUser:
                        startActivity(new Intent(context, ChatsScreenFrgActivity.class));
                              *//*  putExtra(AppConstants.ASSOC_ID, models.get(position).getAssoc_id()).
                                putExtra(AppConstants.DIRECT, "direct").
                                putExtra(AppConstants.SENDER_ID, models.get(position).getSender_id()).
                                putExtra(AppConstants.ROOM_ID, models.get(position).getRoom_id()).
                                putExtra(AppConstants.IS_GROUP, models.get(position).isIs_group()).
                                putExtra(AppConstants.QB_ID, models.get(position).getQb_id()).
                                putExtra(AppConstants.IS_BLOCKED, models.get(position).isIs_blocked()).
                                putExtra(AppConstants.IS_BROAD_CAST, models.get(position).isIs_broadcast()).
                                putExtra(AppConstants.IS_MUTE, models.get(position).isIs_mute()).
                                putExtra(AppConstants.NAME, models.get(position).getName()).putExtra(AppConstants.FROM, AppConstants.CHAT_FRAGMENT).
                                putExtra(AppConstants.USER_IMAGE, models.get(position).getImage()));
                     *//*

                              break;

                    case R.id.ll_delete:
                        myPosition = position;

                        break;

                    case R.id.ll_mute:

                        if (CommonUtils.isOnline(context)) {
                           *//* if (models.get(position).isIs_mute()) {
                                callMuteRoomAPI(models, false, position);
                            } else {
                                // callMuteRoomAPI(models, true,position);
                                setPostShareDialoge(position);

                            }*//*
                        } else {
                            ToastUtils.showToastShort(context, context.getString(R.string.internet_alert_msg));
                        }
                        break;


                    case R.id.ll_pin:
                       *//* RoomsTable roomsTable = new RoomsTable(context);
                        if (models.get(position).getIs_pinned() == 0) {
                            if (roomsTable.getTotalPinCount() < 3) {
                                models.get(position).setIs_pinned(1);
                                roomsTable.updatePinnedStatus(1, models.get(position).getRoom_id());
                            } else {
                                CommonUtils.showToast(context, "You are not allowed to pin more than 3 chats.");
                            }
                        } else {
                            models.get(position).setIs_pinned(0);
                            roomsTable.updatePinnedStatus(0, models.get(position).getRoom_id());
                        }
                        models.clear();
                        models.addAll(roomsTable.getAllRooms(context));
                        adapter.notifyDataSetChanged();
                        roomsTable.closeDB();*//*

                        break;

                    default:
                        break;
                }
            }
        });
        binding.rvChatFragment.setAdapter(adapter);

        binding.rvChatFragment.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

              *//*  int lastvisibleitemposition = layoutManager.findLastVisibleItemPosition();
                if (models != null && models.size() > 0) {

                    if (lastvisibleitemposition == adapter.getItemCount() - 1) {
                        if (currentPage < totalPage) {
                            callGetRoomAPI(++currentPage);
                        }
                    }
                }*//*
            }
        });

       *//* binding.etSearchChatFragment.addTextChangedListener(new TextWatcher() {
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
*//*

    }*/

    private List< DirectModel> chatData() {
        List<DirectModel> directModels=new ArrayList<>();
        for(int i=0;i<images.length;i++)
        {
            DirectModel modelList=new DirectModel();
            modelList.setImage_url(images[i]);
            modelList.setName_url(names[i]);
            modelList.setLast_message_url(msg[i]);

            modelList.setTime_ago_url(time[i]);
            directModels.add(modelList);
        }
        return directModels;

    }


    public void filter(String text){

        ArrayList<DirectModel> filteredName = new ArrayList<>();

        for (DirectModel model : models){
            if (model.getName_url().toLowerCase().contains(text.toLowerCase())){
                filteredName.add(model);
            }
        }
        adapter.updatedList(filteredName);

    }

/*
    private void setPostShareDialoge(final int position) {


        final CharSequence[] items = {getString(R.string.eight_hrs), getString(R.string.one_week), getString(R.string.one_month),
                getString(R.string.one_year)};
        new MaterialDialog.Builder(context)
                .backgroundColor(ContextCompat.getColor(context, themes.setDarkThemeStory()))
                .title(R.string.mute_notification_d).titleGravity(GravityEnum.CENTER)
                .items(items)
                .itemsColor(ContextCompat.getColor(context, themes.setDarkThemeText()))
                .titleColor(ContextCompat.getColor(context, themes.setDarkThemeText()))
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {

                        if (items[position].equals(getString(R.string.eight_hrs))) {
                            callMuteRoomAPI(models, true, position);

                        } else if (items[position].equals(getString(R.string.one_week))) {
                            callMuteRoomAPI(models, true, position);
                        } else if (items[position].equals(getString(R.string.one_month))) {
                            callMuteRoomAPI(models, true, position);
                        } else if (items[position].equals(getString(R.string.one_year))) {
                            callMuteRoomAPI(models, true, position);
                        }

                    }
                }).negativeText(R.string.cancel).onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        }).show();


    }


    public void filter(String text) {
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
            adapter.updatedList(filteredName, "");
        }

    }*/

    @Override
    public void initializedControl() {
        setCurrentTheme();
        binding.btnDelDoneChats.setVisibility(View.GONE);
        ivRight = (ImageView) findViewById(R.id.ivRight);
        tvLeftText = (CustomRobotoRegularTextView2) findViewById(R.id.tvLeftText);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationHomeBottom);
        //initView();
        setRecycler();
        // itemTouch();



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




    private void setCurrentTheme() {
        binding.toolbarEditProfile.toolbarMain.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        binding.toolbarEditProfile.tvHeader.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.etSearchChatFragment.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));

        Themes.getInstance(context).changeIconColor(context, binding.toolbarEditProfile.ivLeft);
        Themes.getInstance(context).changeRightIcon(context, binding.toolbarEditProfile.ivEdit);
        binding.llMain.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        binding.etSearchChatFragment.setBackground(ContextCompat.getDrawable(context, Themes.getInstance(context).setDarkStoryBackground()));

    }

    @Override
    public void setToolBar() {
        binding.toolbarEditProfile.tvHeader.setText("Direct Share");
        binding.toolbarEditProfile.tvHeader.setVisibility(View.VISIBLE);
        binding.toolbarEditProfile.tvRighttext.setText(R.string.save);
        binding.toolbarEditProfile.tvRighttext.setVisibility(View.GONE);
        binding.toolbarEditProfile.ivLeft.setVisibility(View.VISIBLE);
        binding.toolbarEditProfile.ivEdit.setVisibility(View.VISIBLE);
    }

    @Override
    public void setOnClickListener() {
        binding.btnDelDoneChats.setOnClickListener(this);
        binding.toolbarEditProfile.ivLeft.setOnClickListener(this);
        binding.toolbarEditProfile.ivEdit.setOnClickListener(this);
        ivRight.setOnClickListener(this);
        tvLeftText.setOnClickListener(this);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivEdit:
                ActivityController.startActivity(context, DirectUserAddActivity.class);
                break;
            case R.id.ivLeft:
                finish();
                break;
            case R.id.tvLeftText:
                if (flag == -1) {
                    tvLeftText.setText(R.string.done);
                    bottomNavigationView.setVisibility(View.GONE);
                    binding.btnDelDoneChats.setVisibility(View.VISIBLE);
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


                } else {
                    CommonUtils.showToast(context, getString(R.string.nothing_to_delete));
                }

                break;
            default:
                break;
        }
    }

    /**
     * method for create story api
     */
 /*   private void callGetRoomAPI(final int pageNo) {
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

/*
    private ClearRoomRequest deleteRoomRequest(boolean singleDelete) {
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
                                        models.remove(i);
                                        i--;
                                        roomsTable.deleteRoomChat(clearRoomRequest.getRoom_ids().get(i));
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


}
