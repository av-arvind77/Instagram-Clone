package com.yellowseed.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.google.gson.JsonObject;
import com.yellowseed.R;
import com.yellowseed.adapter.NewGroupNextAdapter;
import com.yellowseed.databinding.ActivityNewGroupNextBinding;
import com.yellowseed.fragments.ChatsFragment;
import com.yellowseed.fragments.GridFragment;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.DirectModel;
import com.yellowseed.model.UserListModel;
import com.yellowseed.model.resModel.GroupMemberResonse;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewGroupNextActivity extends BaseActivity {
    private ActivityNewGroupNextBinding binding;
    private Context context;
    private ArrayList<DirectModel> models=new ArrayList<>();
    private NewGroupNextAdapter adapter;
    private int count = 0;
    private int page = 1;
    private int totalRecord;
    private String from;
    private Themes themes;
    private boolean darkThemeEnabled;
    private String groupName, assocId, groupImage;
   // private List<GroupMemberResonse.MembersBean> arlMembersList = new ArrayList<>();
    private String[] names = {"John Thomas", "Jenny Koul", "Mike Keel", "Julie Kite", "Tiny Tim", "Tiny Tim", "Tiny Tim", "Tiny Tim", "Tiny Tim"};
    private int[] images = {R.mipmap.profile_icon, R.mipmap.profile_icon2, R.mipmap.profile_icon3, R.mipmap.profile_icon4, R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon2, R.mipmap.profile_icon3, R.mipmap.profile_icon4};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_group_next);
        context = NewGroupNextActivity.this;
        themes = new Themes(context);
        darkThemeEnabled = CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME);
      getIntentValue();
        initializedControl();
        setToolBar();
        setOnClickListener();
       /* if (CommonUtils.isOnline(context)) {
            searchUserApi(false, "", page);
        } else {
            ToastUtils.showToastShort(context, context.getString(R.string.internet_alert_msg));
        }*/
    }

    private void getIntentValue() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            from = getIntent().getStringExtra(AppConstants.FROM);
            if (getIntent().getStringExtra(AppConstants.FROM) != null) {
                if (from.equalsIgnoreCase(GroupEditActivity.class.getSimpleName())) {
                    if (getIntent().getStringExtra(AppConstants.NAME) != null) {
                        groupName = getIntent().getStringExtra(AppConstants.NAME);
                    }
                    if (getIntent().hasExtra("selectedIds")) {
                        models = (ArrayList<DirectModel>) getIntent().getSerializableExtra("selectedIds");
                        if (models != null && models.size() > 0) {
                            count = models.size();
                        }
                    }
                    if (getIntent().getStringExtra(AppConstants.ASSOC_ID) != null) {
                        assocId = getIntent().getStringExtra(AppConstants.ASSOC_ID);
                    }
                    if (getIntent().getStringExtra(AppConstants.USER_IMAGE) != null) {
                        groupImage = getIntent().getStringExtra(AppConstants.USER_IMAGE);
                    }
                }
            }
        }
        }


    @Override
    public void initializedControl() {
        nwGroupRecyclerView();
        setCurrentTheme();
        binding.tvNewGroupNextNumbering.setText(count + " " + getString(R.string.of) + " " + models.size());

        binding.etSearchNewGroupNext.addTextChangedListener(new TextWatcher() {
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
        binding.toolbarNewGroupNext.toolbarMain.setBackgroundColor(ContextCompat.getColor(context, themes.setDarkTheme()));
        binding.toolbarNewGroupNext.tvHeader.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.toolbarNewGroupNext.tvRighttext.setTextColor(ContextCompat.getColor(context, themes.setTolbarText()));
        themes.changeIconColor(context, binding.toolbarNewGroupNext.ivLeft);
        binding.llMain.setBackgroundColor(ContextCompat.getColor(context, themes.setDarkTheme()));
        binding.etSearchNewGroupNext.setBackground(ContextCompat.getDrawable(context, themes.setDarkEditProfileBackground()));
        binding.tvNewGroupNextNumbering.setTextColor(ContextCompat.getColor(context, themes.setDarkGreyTextColor()));
        binding.viewLine.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setViewLineGrey()));


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



    private void nwGroupRecyclerView() {
        binding.rvNewGroupNext.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvNewGroupNext.setLayoutManager(layoutManager);
        models = new ArrayList<>();
        models.addAll(prepareData());
        adapter = new NewGroupNextAdapter(models, this, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {

                    case R.id.chechBoxDirectadd:

                        if (models.get(position).isChecked()){
                            models.get(position).setChecked(false);
                            count--;
                            binding.tvNewGroupNextNumbering.setText(count + " " +getString(R.string.of)+ " " + models.size());
                        }
                        else {
                            models.get(position).setChecked(true);
                            count++;
                            binding.tvNewGroupNextNumbering.setText(count +" " + getString(R.string.of)+ " " + models.size());
                        }


                        break;

                    default:
                        break;
                }
            }
        });
        binding.rvNewGroupNext.setAdapter(adapter);
    }

    private ArrayList<DirectModel> prepareData() {
        ArrayList<DirectModel> data = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            DirectModel model = new DirectModel();
            model.setImage_url(images[i]);
            model.setName_url(names[i]);
            data.add(model);
        }
        return data;
    }






    /*


    private void nwGroupRecyclerView() {
        binding.rvNewGroupNext.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvNewGroupNext.setLayoutManager(layoutManager);
        models = new ArrayList<>();
        adapter = new NewGroupNextAdapter(models, this, arlMembersList, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {

                    case R.id.chechBoxDirectadd:

                        if (models.get(position).isChecked()) {
                            models.get(position).setChecked(false);
                            count--;
                            binding.tvNewGroupNextNumbering.setText(count + " " + getString(R.string.of) + " " + models.size());
                        } else {
                            models.get(position).setChecked(true);
                            count++;
                            binding.tvNewGroupNextNumbering.setText(count + " " + getString(R.string.of) + " " + models.size());
                        }

                        break;

                    default:
                        break;
                }
            }
        });
        binding.rvNewGroupNext.setAdapter(adapter);
    }*/

    @Override
    public void setToolBar() {
        binding.toolbarNewGroupNext.ivLeft.setVisibility(View.VISIBLE);
        binding.toolbarNewGroupNext.ivLeft.setImageResource(R.mipmap.back_icon);
        binding.toolbarNewGroupNext.tvHeader.setVisibility(View.VISIBLE);


        if (from != null && from.equalsIgnoreCase("broadcast"))
        {
            binding.toolbarNewGroupNext.tvHeader.setText(R.string.broadcast);
            binding.toolbarNewGroupNext.tvRighttext.setText(R.string.done);
        }
        else if (from != null && from.equalsIgnoreCase("forward")) {
            binding.toolbarNewGroupNext.tvHeader.setText(R.string.forwardTo);
            binding.toolbarNewGroupNext.tvRighttext.setText(R.string.send);
        } else {

            if (from != null) {
                if (from.equalsIgnoreCase(GroupEditActivity.class.getSimpleName())) {
                    binding.toolbarNewGroupNext.tvHeader.setText(R.string.add_members);
                    binding.toolbarNewGroupNext.tvRighttext.setText(R.string.done);
                } else {
                    binding.toolbarNewGroupNext.tvHeader.setText(R.string.newgroupu);
                    binding.toolbarNewGroupNext.tvRighttext.setText(R.string.nextu);
                }
            }
            else {
                binding.toolbarNewGroupNext.tvHeader.setText(R.string.newgroupu);
                binding.toolbarNewGroupNext.tvRighttext.setText(R.string.nextu);
            }
        }
        binding.toolbarNewGroupNext.ivRight.setVisibility(View.GONE);
        binding.toolbarNewGroupNext.tvRighttext.setVisibility(View.VISIBLE);


    }

    @Override
    public void setOnClickListener() {
        binding.toolbarNewGroupNext.ivLeft.setOnClickListener(this);
        binding.toolbarNewGroupNext.tvRighttext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivLeft:
                onBackPressed();
                break;
            case R.id.tvRighttext:
           //     ActivityController.startActivity(context, NewGroupDoneActivity.class);






                ArrayList<String> seletedIds = new ArrayList<>();
                ArrayList<DirectModel> userListModels = new ArrayList<>();
                for (int i = 0; i < models.size(); i++) {
                    if (models.get(i).isChecked()) {
                        seletedIds.add(models.get(i).getId());
                        DirectModel userListModel = new DirectModel();
                        userListModel.setChecked(models.get(i).isChecked());
                        userListModel.setId(models.get(i).getId());
                        userListModel.setImage_url(models.get(i).getImage_url());
                        userListModel.setName_url(models.get(i).getName_url());
                        userListModels.add(userListModel);
                    }
                }

                if (from != null && from.equalsIgnoreCase("broadcast")) {
                    if (seletedIds.size() > 0) {
                        Intent intent = new Intent(context, HomeActivity.class);
                        startActivity(intent);

                    }
                } else if (from != null && from.equalsIgnoreCase("forward")) {

                    Intent intent = new Intent();
                    intent.putExtra("selectedIds", (Serializable) adapter.getSelectedIds());
                    setResult(RESULT_OK, intent);
                    finish();

                }


                   /* else
                        {

                    }*/


                else {
                    if (binding.toolbarNewGroupNext.tvRighttext.getText().toString().trim().equalsIgnoreCase(getString(R.string.done))) {
                        Intent intent = new Intent();
                        intent.putExtra("selectedIds", (Serializable) adapter.getSelectedIds());
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        startActivity(new Intent(context, NewGroupDoneActivity.class).putExtra(AppConstants.PARTICIPANTS,"participatant"));

                    }
                }



























             /*   ArrayList<String> seletedIds = new ArrayList<>();
                ArrayList<UserListModel> userListModels = new ArrayList<>();
                for (int i = 0; i < models.size(); i++) {
                    if (models.get(i).isChecked()) {
                        seletedIds.add(models.get(i).getId());
                        UserListModel userListModel = new UserListModel();
                        userListModel.setChecked(models.get(i).isChecked());
                        userListModel.setId(models.get(i).getId());
                        userListModel.setImage(models.get(i).getImage());
                        userListModel.setName(models.get(i).getName());
                        userListModels.add(userListModel);
                    }
                }

                if (from != null && from.equalsIgnoreCase("broadcast")) {
                    if (seletedIds.size() > 0) {
                        CreateBroadcastRequest createBroadcastRequest = new CreateBroadcastRequest();
                        createBroadcastRequest.setName(seletedIds.size() + " Members");
                        createBroadcastRequest.setMember_ids(seletedIds);
                        createBroadCast(createBroadcastRequest);
                    }
                } else if (from != null && from.equalsIgnoreCase("forward")) {

                    Intent intent = new Intent();
                    intent.putExtra("selectedIds", (Serializable) adapter.getSelectedIds());
                    setResult(RESULT_OK, intent);
                    finish();

                }*/


                   /* else
                        {

                    }*/
/*

                else {
                    if (binding.toolbarNewGroupNext.tvRighttext.getText().toString().trim().equalsIgnoreCase(getString(R.string.done))) {
                        Intent intent = new Intent();
                        intent.putExtra("selectedIds", (Serializable) adapter.getSelectedIds());
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {


                        startActivity(new Intent(context, NewGroupDoneActivity.class).putExtra(AppConstants.PARTICIPANTS, userListModels));

                         }
                    }*/





                break;
            default:
                break;
        }
    }

   /* private void searchUserApi(final boolean isSearching, String text, final int pageNo) {
        if (CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            if (!isSearching) {
                progressDialog.show();
            }

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("name", text);
            jsonObject.addProperty("page", pageNo);

            Call<ApiResponse> call = ApiExecutor.getClient(context).apiSearchUser(jsonObject);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    if (!isSearching) {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                    if (response != null && response.body() != null) {
                        if (response.body().getResponseCode() == AppConstants.SUCCESS) {
                            if (response.body().getUsers() != null && response.body().getUsers().size() > 0) {
                                models.clear();
                                models.addAll(response.body().getUsers());
                                ArrayList<UserListModel> listTemp = new ArrayList();
                                listTemp.addAll(models);
                                if (arlMembersList != null && arlMembersList.size() > 0) {
                                    for (int i = 0; i < models.size(); i++) {
                                        for (int j = 0; j < arlMembersList.size(); j++) {
                                            if (models.get(i).getId().equalsIgnoreCase(models.get(j).getId())) {
                                                listTemp.remove(models.get(i));
                                                break;
                                            }
                                        }
                                    }
                                }
                                models.clear();
                                models.addAll(listTemp);
                                adapter.notifyDataSetChanged();
                            }

                            totalRecord = response.body().getPagination().getTotal_records();
                        }
                    } else {
                        ToastUtils.showToastShort(context, getString(R.string.server_error_msg));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    if (!isSearching) {
                        progressDialog.dismiss();
                    }
                    t.printStackTrace();
                    ToastUtils.showToastShort(context, getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(context, context.getString(R.string.internet_alert_msg));
        }
    }*/


    private void createBroadCast(CreateBroadcastRequest jsonObject) {
        if (CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            if (progressDialog != null) {
                progressDialog.show();
            }
            Call<ApiResponse> call = ApiExecutor.getClient(context).apiCreateBraodcast(jsonObject);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    if (response != null && response.body() != null) {
                        if (response.body().getResponseCode() == AppConstants.SUCCESS) {
                            ToastUtils.showToastShort(context, response.body().getResponseMessage());
                            Intent intent = new Intent(context, HomeActivity.class);
                            startActivity(intent);
                        }
                    } else {
                        ToastUtils.showToastShort(context, getString(R.string.server_error_msg));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    t.printStackTrace();
                    ToastUtils.showToastShort(context, getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(context, context.getString(R.string.internet_alert_msg));
        }
    }
}
