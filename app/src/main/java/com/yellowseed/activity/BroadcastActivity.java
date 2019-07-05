package com.yellowseed.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.yellowseed.R;
import com.yellowseed.adapter.BroadCastAdapter;
import com.yellowseed.databinding.ActivityBroadcastBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.SendToModel;
import com.yellowseed.utils.ActivityController;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;
import com.yellowseed.webservices.ApiResponse;
import com.yellowseed.webservices.CreateBroadcastRequest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BroadcastActivity extends BaseActivity {
    private ActivityBroadcastBinding binding;
    private Context context;
    private ArrayList<SendToModel> models;
    private int count = 0;
    private  BroadCastAdapter adapter;

    private String[] names = {"John Thomas", "Jenny Koul", "Mike Keel", "Julie Kite", "Tiny Tim", "Julie Kite", "Tiny Tim", "Julie Kite", "Tiny Tim", "Julie Kite", "Tiny Tim"};
    private int[] images = {R.drawable.user_placeholder,R.drawable.user_placeholder, R.drawable.user_placeholder, R.drawable.user_placeholder,R.drawable.user_placeholder, R.drawable.user_placeholder, R.drawable.user_placeholder, R.drawable.user_placeholder, R.drawable.user_placeholder, R.drawable.user_placeholder, R.drawable.user_placeholder, R.drawable.user_placeholder};
    private String nameData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_broadcast);
        context = BroadcastActivity.this;
        initializedControl();
        setToolBar();
        setOnClickListener();
    }

    @Override
    public void initializedControl() {
        setCurrentTheme();
        broadcastRecyclerView();
        binding.tvNumberingBroadcast.setText(count + " " +getString(R.string.of)+ " " + models.size());


        binding.etBroadcastSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void setCurrentTheme() {
        binding.viewLine.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setViewLineGrey()));


    }
    public void filter(String text){

        ArrayList<SendToModel> filteredName = new ArrayList<>();

        for (SendToModel model : models){

            if (model.getName_url().toLowerCase().contains(text.toLowerCase())){
                filteredName.add(model);
            }

        }
       // binding.tvNumberingBroadcast.setText(count + " " +getString(R.string.of)+ " " + filteredName.size());

        adapter.updatedList(filteredName);

    }

    private void broadcastRecyclerView() {
        binding.rvBroadcast.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvBroadcast.setLayoutManager(layoutManager);

        //For divide the recycler item
        DividerItemDecoration itemDecorator = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(context, R.drawable.divider));
        binding.rvBroadcast.addItemDecoration(itemDecorator);

        models = new ArrayList<>();
        models.addAll(prepareData());

         adapter = new BroadCastAdapter(models, this, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                switch (view.getId()){
                    case R.id.checkBoxSendTo:


                        if (models.get(position).getChecked()){
                            models.get(position).setChecked(false);
                            count--;

                            String text = count + " " +getString(R.string.of)+ " " + models.size();
                            binding.tvNumberingBroadcast.setText(text);
                            nameData = models.get(position).getName_url();

                        }
                        else {
                            models.get(position).setChecked(true);
                            count++;
                            String text = count + " " + getString(R.string.of) + " " + models.size();
                            binding.tvNumberingBroadcast.setText(text);

                            nameData = models.get(position).getName_url();
                            binding.etBroadcastSearch.append(nameData + " , ");
                        }
                }


            }
        });
        binding.rvBroadcast.setAdapter(adapter);
    }

    private ArrayList<SendToModel> prepareData() {

        ArrayList<SendToModel> data = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            SendToModel model = new SendToModel();
            model.setName_url(names[i]);
            model.setImage_url(images[i]);
            data.add(model);
        }
        return data;

    }

    @Override
    public void setToolBar() {

        binding.layoutBroadcast.ivLeft.setVisibility(View.VISIBLE);
        binding.layoutBroadcast.ivLeft.setImageResource(R.mipmap.back_icon);
        binding.layoutBroadcast.tvHeader.setVisibility(View.VISIBLE);
        binding.layoutBroadcast.tvHeader.setText(R.string.broadcast);
        binding.layoutBroadcast.ivRight.setVisibility(View.GONE);
        binding.layoutBroadcast.tvRighttext.setVisibility(View.VISIBLE);
        binding.layoutBroadcast.tvRighttext.setText(R.string.nextu);
        binding.layoutBroadcast.tvRighttext.setTextColor(getResources().getColor(R.color.blueissh));

    }

    @Override
    public void setOnClickListener() {
        binding.layoutBroadcast.ivLeft.setOnClickListener(this);
        binding.layoutBroadcast.tvRighttext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivLeft:
                onBackPressed();
                break;
            case R.id.tvRighttext:
                onBackPressed();
                break;
                default:
                    break;
        }
    }

    private CreateBroadcastRequest createBroadcastRequest(){
        CreateBroadcastRequest createBroadcastRequest = new CreateBroadcastRequest();


        return createBroadcastRequest;
    }

//    public  void callFollowingListAPI(final int page , String search) {
//        if(CommonUtils.isOnline(context)) {
//            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
//            progressDialog.show();
//
//            Call<ApiResponse> call = ApiExecutor.getClient(context).apiCreateBraodcast();
//            call.enqueue(new Callback<ApiResponse>() {
//                @Override
//                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
//                    progressDialog.dismiss();
//                    try {
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    if (response != null && response.body() != null) {
//                        if (response.body().getResponseCode() == AppConstants.SUCCESS) {
//                        }
//                        else if (response.body().getResponseCode() == AppConstants.USER_BLOCKED){
//                            context.startActivity(new Intent(context, LoginActivity.class));
//                            CommonUtils.clearPrefData(context);
//                            ((Activity)context).finishAffinity();
//                        }else {
//                            ToastUtils.showToastShort(context, ""+response.body().getResponseMessage());
//                        }
//                    }  else {
//                        ToastUtils.showToastShort(context,context.getString(R.string.server_error_msg));
//                    }
//                }
//                @Override
//                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
//                    progressDialog.dismiss();
//                    t.printStackTrace();
//                    ToastUtils.showToastShort(context,context.getString(R.string.server_error_msg));
//                }
//            });
//        }
//        else
//        {
//            ToastUtils.showToastShort(context,context.getString(R.string.internet_alert_msg));
//        }
//    }
}