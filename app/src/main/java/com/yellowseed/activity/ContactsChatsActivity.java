package com.yellowseed.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.alphabetik.Alphabetik;
import com.google.gson.JsonObject;
import com.yellowseed.R;
import com.yellowseed.adapter.ContactsAdapter;
import com.yellowseed.adapter.SendToAdapter;
import com.yellowseed.databinding.ActivityContactsChatsBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.SendToModel;
import com.yellowseed.model.UserListModel;
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

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactsChatsActivity extends BaseActivity {
    private ActivityContactsChatsBinding binding;
    private Context context;
  //  private ArrayList<UserListModel> models;
 //   private ContactsAdapter adapter;
    private boolean darkThemeEnabled;
    private LinearLayoutManager layoutManager;
    private int totalRecord;
    private boolean isLastPage;
    private int page = 1;


    private ArrayList<SendToModel> models;
    private ContactsAdapter adapter;
    private String[] names = {"John Thomas", "Jenny Koul", "Mike Keel", "Julie Kite", "Tiny Tim","Julie Kite", "Tiny Tim","Julie Kite", "Tiny Tim","Julie Kite", "Tiny Tim"};
    private int[] images = {R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon2, R.mipmap.profile_icon4, R.mipmap.profile_icon,R.mipmap.profile_icon2, R.mipmap.profile_icon4,R.mipmap.profile_icon3, R.mipmap.profile_icon,R.mipmap.profile_icon2, R.mipmap.profile_icon3,R.mipmap.profile_icon3, R.mipmap.profile_icon4};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contacts_chats);
        context = ContactsChatsActivity.this;
        initializedControl();
        setToolBar();
        setOnClickListener();


      /*  if(CommonUtils.isOnline(context)) {
            searchUserApi(false,"", page);
        }
        else
        {
            ToastUtils.showToastShort(context,context.getString(R.string.internet_alert_msg));
        }*/
    }

    @Override
    public void initializedControl() {
        setCurrentTheme();
        chatsRecylerView();

        binding.etChatsContactsSearch.addTextChangedListener(new TextWatcher() {
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
        binding.layoutChatsContacts.toolbarMain.setBackgroundColor(ContextCompat.getColor(context,Themes.getInstance(context).setDarkTheme()));
        binding.llMain.setBackgroundColor(ContextCompat.getColor(context,Themes.getInstance(context).setDarkTheme()));
        binding.layoutChatsContacts.tvHeader.setTextColor(ContextCompat.getColor(context,Themes.getInstance(context).setDarkThemeText()));

        binding.layoutChatsContacts.tvRighttext.setTextColor(ContextCompat.getColor(context,Themes.getInstance(context).setTolbarText()));
        binding.tvNewBroadCast.setTextColor(ContextCompat.getColor(context,Themes.getInstance(context).setBlueContact()));
        binding.tvNewContact.setTextColor(ContextCompat.getColor(context,Themes.getInstance(context).setBlueContact()));

        binding.tvnewGroupChat.setTextColor(ContextCompat.getColor(context,Themes.getInstance(context).setBlueContact()));
        Themes.getInstance(context).changeIconColor(context,binding.layoutChatsContacts.ivLeft);

        Themes.getInstance(context).changeIconColorToWhite(context,binding.ivNewBroadcast);
        Themes.getInstance(context).changeIconColorToWhite(context,binding.ivNewGroup);
        Themes.getInstance(context).changeIconColorToWhite(context,binding.ivNewContact);



        binding.etChatsContactsSearch.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.etChatsContactsSearch.setBackground(ContextCompat.getDrawable(context, Themes.getInstance(context).setDarkStoryBackground()));
    binding.viewLine.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setViewLineGrey()));
    binding.viewBroadcast.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setViewLineGrey()));
    binding.viewGroup.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setViewLineGrey()));
  }
/*
    public void filter(String text){

        ArrayList<SendToModel> filteredName = new ArrayList<>();

        for (SendToModel model : models){

            if (model.getName().toLowerCase().contains(text.toLowerCase())){
                filteredName.add(model);
            }

        }
        // binding.tvNumberingBroadcast.setText(count + " " +getString(R.string.of)+ " " + filteredName.size());

        adapter.updatedList(filteredName);

    }*/

    public void filter(String text) {

        ArrayList<SendToModel> filteredName = new ArrayList<>();

        for (SendToModel model : models) {

            if (model.getName_url().toLowerCase().contains(text.toLowerCase())) {
                filteredName.add(model);
            }

        }
    }
/*

    private void chatsRecylerView() {
        binding.rvChatsContacts.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvChatsContacts.setLayoutManager(layoutManager);
        //For divide the recycler item
        DividerItemDecoration itemDecorator = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(context, R.drawable.divider));
        binding.rvChatsContacts.addItemDecoration(itemDecorator);


        models = new ArrayList<>();
        adapter = new ContactsAdapter(models, this, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.llContacts:
                        startActivity(new Intent(context, ChatsScreenFrgActivity.class).
                                putExtra(AppConstants.CHAT_USER_ID, models.get(position).getId()).
                                putExtra(AppConstants.SENDER_ID, PrefManager.getInstance(context).getUserId()).
                                putExtra(AppConstants.QB_ID, models.get(position).getQb_id()).
                                putExtra(AppConstants.NAME, models.get(position).getName()).putExtra(AppConstants.FROM, AppConstants.CONTACT_CHAT_ACTIVITY).
                                putExtra(AppConstants.USER_IMAGE, models.get(position).getImage()));
                        finish();
                        break;
                }
            }
        });
        binding.rvChatsContacts.setAdapter(adapter);

        binding.rvChatsContacts.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int lastvisibleitemposition = layoutManager.findLastVisibleItemPosition();
                if (models != null && models.size() > 0) {
                    if (adapter != null && adapter.getItemCount() - 1 == totalRecord) {
                        isLastPage = true;
                    }

                    if (lastvisibleitemposition == adapter.getItemCount() - 1) {

                        if (!isLastPage) {
                            searchUserApi(false, "", ++page);
                            // Increment the pagecount everytime we scroll to fetch data from the next page
                            // make isLoading = false once the data is loaded
                            // call mAdapter.notifyDataSetChanged() to refresh the Adapter and Layout

                        }

                    }
                }
            }
        });
    }
*/



    private void chatsRecylerView() {
        binding.rvChatsContacts.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvChatsContacts.setLayoutManager(layoutManager);

        //For divide the recycler item
     /*   DividerItemDecoration itemDecorator = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(context, R.drawable.divider));
        binding.rvChatsContacts.addItemDecoration(itemDecorator);
*/

        models = new ArrayList<>();
        models.addAll(prepareData());
        adapter = new ContactsAdapter(models, this, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.llContacts:
                        ActivityController.startActivity(context, ChatsScreenFrgActivity.class);
                        finish();
                        break;
                }
            }
        });
        binding.rvChatsContacts.setAdapter(adapter);



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
        binding.layoutChatsContacts.ivLeft.setVisibility(View.VISIBLE);
        binding.layoutChatsContacts.ivLeft.setImageResource(R.mipmap.back_icon);
        binding.layoutChatsContacts.tvHeader.setVisibility(View.VISIBLE);
        binding.layoutChatsContacts.tvHeader.setText(R.string.contact);
        binding.layoutChatsContacts.ivRight.setVisibility(View.GONE);
    }

    @Override
    public void setOnClickListener() {
        binding.layoutChatsContacts.ivLeft.setOnClickListener(this);
        binding.tvNewBroadCast.setOnClickListener(this);
        binding.tvnewGroupChat.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivLeft:
                onBackPressed();
                break;

            case R.id.tvNewBroadCast:
                startActivity(new Intent(context, NewGroupNextActivity.class).putExtra(AppConstants.FROM,"broadcast"));
                break;

            case R.id.tvnewGroupChat:
                startActivity(new Intent(context, NewGroupNextActivity.class).putExtra(AppConstants.FROM,"group"));
                break;

            default:
                break;
        }
    }
/*
    private void searchUserApi(final boolean isSearching, String text, final int pageNo) {
        if(CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            if (!isSearching) {
                progressDialog.show();
            }
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("name",text);
            jsonObject.addProperty("page",pageNo);

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
                                adapter.notifyDataSetChanged();
                            }

                            totalRecord = response.body().getPagination().getTotal_records();
                        }
                    }
                    else
                    {
                        ToastUtils.showToastShort(context,getString(R.string.server_error_msg));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    if (!isSearching) {
                        progressDialog.dismiss();
                    }
                    t.printStackTrace();
                    ToastUtils.showToastShort(context,getString(R.string.server_error_msg));
                }
            });
        }
        else
        {
            ToastUtils.showToastShort(context,context.getString(R.string.internet_alert_msg));
        }
    }*/
}