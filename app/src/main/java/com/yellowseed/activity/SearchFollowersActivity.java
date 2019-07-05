package com.yellowseed.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.JsonObject;
import com.yellowseed.R;
import com.yellowseed.adapter.SearchFollowingAdapter;
import com.yellowseed.databinding.ActivitySearchFollowersBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.SearchFollowingModel;
import com.yellowseed.model.UserListModel;
import com.yellowseed.utils.ActivityController;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;
import com.yellowseed.webservices.ApiRequest;
import com.yellowseed.webservices.ApiResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class SearchFollowersActivity extends BaseActivity {
    private ActivitySearchFollowersBinding binding;
    private Context mContext;
    private ArrayList<SearchFollowingModel> arlModel=new ArrayList<>();
    private SearchFollowingAdapter adapter;
    private int page=1;
    private boolean isLastPage;
    private boolean isLoading;
    private Integer totalRecord;
    private Themes themes;
    private Boolean darkThemeEnabled;

    private String[] names = {"John Thomas", "Jenny Koul", "Mike Keel", "Julie Kite", "Tiny Tim", "Julie Kite", "Tiny Tim", "Tiny Tim", "Julie Kite", "Tiny Tim", "Julie Kite", "Tiny Tim", "Tiny Tim"};
    private int[] images = {R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_followers);
        mContext = SearchFollowersActivity.this;
        darkThemeEnabled = CommonUtils.getPreferencesBoolean(mContext, AppConstants.DARK_THEME);
        themes = new Themes(mContext);
        initializedControl();
        setToolBar();
        setOnClickListener();
      //  callFollowersListAPI(page,"");
    }

    @Override
    public void initializedControl() {
        searchFollowersRecylerView();
        setCurrentTheme();

    }
    private void setCurrentTheme() {
        binding.llSearchFollowers.setBackgroundColor(ContextCompat.getColor(mContext, themes.setDarkTheme()));
        binding.layoutsearchfollowers.toolbarMain.setBackgroundColor(ContextCompat.getColor(mContext, themes.setDarkTheme()));
        binding.layoutsearchfollowers.tvRighttext.setTextColor(ContextCompat.getColor(mContext, themes.setTolbarText()));
        binding.layoutsearchfollowers.tvHeader.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
        binding.etSearchfollowers.setBackground(ContextCompat.getDrawable(mContext, themes.setDarkSearchDrawable()));
        binding.etSearchfollowers.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
        binding.etSearchfollowers.setHintTextColor(ContextCompat.getColor(mContext,themes.setDarkHintColor()));
        binding.viewLine.setBackgroundColor(ContextCompat.getColor(mContext, themes.setViewLineGrey()));
        Themes.getInstance(mContext).changeIconColor(mContext,binding.layoutsearchfollowers.ivLeft);


    }

     /*   private void searchFollowersRecylerView() {

        binding.rvSearchfollowers.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvSearchfollowers.setLayoutManager(layoutManager);
        arlModel = new ArrayList<>();

        adapter = new SearchFollowingAdapter(arlModel, this, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.llFollowingAnother:
                        Intent intent=new Intent(mContext,MyProfileActivity.class);
                        intent.putExtra(AppConstants.USER_ID,arlModel.get(position).getId());
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
        binding.rvSearchfollowers.setAdapter(adapter);


        binding.rvSearchfollowers.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastvisibleitemposition = layoutManager.findLastVisibleItemPosition();
                if (arlModel != null && arlModel.size() > 0) {
                    if (adapter != null && adapter.getItemCount() - 1 == totalRecord) {
                        isLastPage = true;
                    }

                    if (lastvisibleitemposition == adapter.getItemCount() - 1) {

                        if (!isLoading && !isLastPage) {
                            isLoading = true;
                            callFollowersListAPI(++page,"");

                        }

                    }
                }
            }
        });
        binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                isLoading = true;
                callFollowersListAPI(page,"");
            }
        });
    }
*/



    private void searchFollowersRecylerView() {

        binding.rvSearchfollowers.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvSearchfollowers.setLayoutManager(layoutManager);
        arlModel = new ArrayList<>();
        arlModel.addAll(prepareData());
        adapter = new SearchFollowingAdapter(arlModel, this, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.llFollowingAnother:
                        ActivityController.startActivity(mContext, FollowerProfileActivity.class);
                        break;
                    default:
                        break;
                }
            }
        });
        binding.rvSearchfollowers.setAdapter(adapter);
    }

    private ArrayList<SearchFollowingModel> prepareData() {
        ArrayList<SearchFollowingModel> data = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            SearchFollowingModel model = new SearchFollowingModel();
            model.setName_url(names[i]);
            model.setImage_url(images[i]);
            data.add(model);
        }
        return data;
    }



    @Override
    public void setToolBar() {
        binding.layoutsearchfollowers.ivRight.setVisibility(View.GONE);
        binding.layoutsearchfollowers.ivLeft.setVisibility(View.VISIBLE);
        binding.layoutsearchfollowers.ivLeft.setImageResource(R.mipmap.back_icon);
        binding.layoutsearchfollowers.tvHeader.setVisibility(View.VISIBLE);
        binding.layoutsearchfollowers.tvHeader.setText(R.string.followers);
    }

    @Override
    public void setOnClickListener() {
        binding.layoutsearchfollowers.ivLeft.setOnClickListener(this);
    }




    public void filter(String text){

        ArrayList<SearchFollowingModel> filteredName = new ArrayList<>();

        for (SearchFollowingModel model : arlModel){

            if (model.getName_url().toLowerCase().contains(text.toLowerCase())){
                filteredName.add(model);
            }

        }
        adapter.updatedList(filteredName);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivLeft:
                finish();
                break;
            default:
                break;
        }
    }

 /*   public  void callFollowersListAPI(final int page , String search) {
        if(CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();
            JsonObject jsonObject=new JsonObject();
            jsonObject.addProperty("page",page);
            jsonObject.addProperty("name",search);
            jsonObject.addProperty("do","followers");
            Call<ApiResponse> call = ApiExecutor.getClient(mContext).apiGetFollowerList(jsonObject);
            call.enqueue(new retrofit2.Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    isLoading = false;
                    try {
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (response != null && response.body() != null) {
                        if (response.body().getResponseCode() == AppConstants.SUCCESS) {
                            if(page==1){
                                arlModel.clear();
                                binding.swipeContainer.setRefreshing(false);
                            }
                            if(response.body().getUsers() != null){
                                arlModel.addAll(response.body().getUsers());
                            }
                            totalRecord = response.body().getPagination().getTotal_records();
                            adapter.notifyDataSetChanged();
                        }
                        else if (response.body().getResponseCode() == AppConstants.USER_BLOCKED){
                            mContext.startActivity(new Intent(mContext, LoginActivity.class));
                            CommonUtils.clearPrefData(mContext);
                            ((Activity)mContext).finishAffinity();

                        }else {
                            ToastUtils.showToastShort(mContext, ""+response.body().getResponseMessage());
                        }
                    }  else {
                        ToastUtils.showToastShort(mContext,mContext.getString(R.string.server_error_msg));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    if(page==1){
                        binding.swipeContainer.setRefreshing(false);
                    }
                    isLoading = false;
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(mContext,mContext.getString(R.string.server_error_msg));
                }
            });
        }
        else
        {
            ToastUtils.showToastShort(mContext,mContext.getString(R.string.internet_alert_msg));
        }
    }*/

}
