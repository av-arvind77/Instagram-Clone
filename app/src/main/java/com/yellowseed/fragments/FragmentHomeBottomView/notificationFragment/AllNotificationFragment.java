package com.yellowseed.fragments.FragmentHomeBottomView.notificationFragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonObject;
import com.yellowseed.R;
import com.yellowseed.activity.MyProfileActivity;
import com.yellowseed.activity.ShowPostItemsActivity;
import com.yellowseed.adapter.NotificationLayoutAdapter;
import com.yellowseed.databinding.FragmentAllNotificationBinding;
import com.yellowseed.model.NotificationLayoutModel;
import com.yellowseed.model.resModel.ApiNotificationResonse;
import com.yellowseed.utils.BaseFragment;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.RecyclerItemClickListener;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllNotificationFragment extends BaseFragment {

    public Context context;
    private FragmentAllNotificationBinding binding;
    private ArrayList<NotificationLayoutModel> models;

 //   private ArrayList<ApiNotificationResonse.NotificationsBean> models;
    private int notificationUserImage = R.mipmap.profile_icon3;
    private int notificationUserPostImage = R.mipmap.theme_one;
    String text = "<b>Anurag</b></font> <font color=#70696b>liked</font> <b>Mahima Kappor</b></font> <font color=#70696b>post.</font>";
    String text2 = "<b>Anurag</b></font> <font color=#70696b>commented on </font> <b>Mahima Kappor</b></font> <font color=#70696b>post.</font>";
    String text3= "<b>Anurag</b></font> <font color=#70696b>added a post.</font> ";
    private String time[]={"2 hrs","Yesterday at 2:00 PM","13 March at 4:00 PM"};

    private String[] notificationData = {text,text2,text3};
    private int currentPage = 1;
    private int totalPage;
    private NotificationLayoutAdapter notificationLayoutAdapter;
    private LocalBroadcastManager lbm;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }

    public AllNotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_all_notification, container, false);
        View view = binding.getRoot();
        initializedControl();
        setToolBar();
        setOnClickListener();
        lbm = LocalBroadcastManager.getInstance(getActivity());
        lbm.registerReceiver(receiver, new IntentFilter("theme_change"));

       /* if(CommonUtils.isOnline(context)) {
            callNotificationAPI(currentPage);
        }
        else
        {
            ToastUtils.showToastShort(context,context.getString(R.string.internet_alert_msg));
        }*/

        return view;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override

        public void onReceive( Context context, Intent intent ) {

            String data = intent.getStringExtra("key");
            notificationLayoutAdapter.notifyDataSetChanged();


        }

    };
    @Override
    public void initializedControl() {
        binding.rvAllNotification.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        binding.rvAllNotification.setLayoutManager(layoutManager);

        //For divide the recycler item
        /*DividerItemDecoration itemDecorator = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(context, R.drawable.divider));
        binding.rvAllNotification.addItemDecoration(itemDecorator);*/

      /*  models = new ArrayList<>();
//        models.addAll(prepareData());


        notificationLayoutAdapter = new NotificationLayoutAdapter(models, context, AllNotificationFragment.class.getSimpleName(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(models.get(position).getNotification_type()!=null&&models.get(position).getNotification_type().length()>0){
                    Intent intent=null;
                    switch (models.get(position).getNotification_type()){
                        case "post_like":
                        case "post_tag":
                        case "comment_like":
                        case "comment_create":
                            intent=new Intent(context, ShowPostItemsActivity.class);
                            intent.putExtra("post_id",models.get(position).getPost_info().getId());
                            startActivity(intent);
                            break;

                        case "accept_request":
                        case "follow_create":
                            intent=new Intent(context, MyProfileActivity.class);
                            intent.putExtra("user_id",models.get(position).getUser_id());
                            startActivity(intent);
                            break;
                    }
                }

            }
        });
        binding.rvAllNotification.setAdapter(notificationLayoutAdapter);


        binding.rvAllNotification.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int lastvisibleitemposition = layoutManager.findLastVisibleItemPosition();
                if (models != null && models.size() > 0) {

                    if (lastvisibleitemposition == notificationLayoutAdapter.getItemCount() - 1) {
                        if (currentPage < totalPage){
                            callNotificationAPI(++currentPage);
                        }
                    }
                }
            }
        });
    }*/

    /*private ArrayList<NotificationLayoutModel> prepareData() {
        ArrayList<NotificationLayoutModel> notificationLayoutModels = new ArrayList<>();
        for (int i = 0; i < notificationData.length; i++) {
            NotificationLayoutModel notificationLayoutModel = new NotificationLayoutModel();
            notificationLayoutModel.setNotificationData(notificationData[i]);
            notificationLayoutModel.setNotificationUserImage(notificationUserImage);
            notificationLayoutModel.setNotificationUserPostImage(notificationUserPostImage);
            notificationLayoutModels.add(notificationLayoutModel);
        }
        return notificationLayoutModels;
    }*/


     /*   DividerItemDecoration itemDecorator = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(context, R.drawable.divider));
        binding.rvAllNotification.addItemDecoration(itemDecorator);*/

        models = new ArrayList<>();
        models.addAll(prepareData());


        NotificationLayoutAdapter notificationLayoutAdapter = new NotificationLayoutAdapter(models, context);
        binding.rvAllNotification.setAdapter(notificationLayoutAdapter);

    }

    private ArrayList<NotificationLayoutModel> prepareData() {
        ArrayList<NotificationLayoutModel> notificationLayoutModels = new ArrayList<>();
        for (int i = 0; i < notificationData.length; i++) {
            NotificationLayoutModel notificationLayoutModel = new NotificationLayoutModel();
            notificationLayoutModel.setNotificationData(notificationData[i]);
            notificationLayoutModel.setNotificationUserImage(notificationUserImage);
            notificationLayoutModel.setNotificationUserPostImage(notificationUserPostImage);
            notificationLayoutModel.setNotificationTime(time[i]);
            notificationLayoutModels.add(notificationLayoutModel);
        }
        return notificationLayoutModels;
    }

    @Override
    public void setToolBar() {

    }

    @Override
    public void setOnClickListener() {

    }

    /**
     * method for create story api
     */
  /*  private void callNotificationAPI(final int pageNo) {
        if(CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("page_no", pageNo);

            Call<ApiNotificationResonse> call = ApiExecutor.getClient(context).apiCallAllNotification(jsonObject);
            call.enqueue(new Callback<ApiNotificationResonse>() {
                @Override
                public void onResponse(@NonNull Call<ApiNotificationResonse> call, @NonNull final Response<ApiNotificationResonse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            models.clear();
                            if (response.body().getNotifications() != null && response.body().getNotifications().size() > 0) {
                                models.addAll(response.body().getNotifications());
                            }
                            notificationLayoutAdapter.notifyDataSetChanged();

                            totalPage = response.body().getPagination().getMax_page_size();
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
                public void onFailure(@NonNull Call<ApiNotificationResonse> call, @NonNull Throwable t) {
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
    }*/
    @Override
    public void onDestroy() {
        lbm.unregisterReceiver(receiver);
        super.onDestroy();
    }
}
