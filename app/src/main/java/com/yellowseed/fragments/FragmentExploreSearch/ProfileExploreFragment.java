package com.yellowseed.fragments.FragmentExploreSearch;


import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.yellowseed.R;
import com.yellowseed.adapter.ProfileExploreFragmentAdapter;
import com.yellowseed.adapter.UserListAdapter;
import com.yellowseed.databinding.FragmentHomeBottomBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.FollowingRequestModel;
import com.yellowseed.model.SearchModel;
import com.yellowseed.model.UserListModel;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseFragment;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;
import com.yellowseed.webservices.ApiResponse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Response;

public class ProfileExploreFragment extends BaseFragment implements OnItemClickListener {
    private FragmentHomeBottomBinding binding;
    private UserListAdapter adapter;
    private EditText etSearch;
    private ProfileExploreFragmentAdapter profileExploreFragmentAdapter;


    private ArrayList<FollowingRequestModel> followingRequestModels = new ArrayList<>();
    private String[] names = {"John Thomas", "Jenny Koul", "Mike Keel", "Tim", "Kelly ", "Mike Keel", "Jenny Jonas", "Nick "};
    private String[] type = {"Suggestion", "Suggestion", "Suggestion", "Recent", "Recent", "Recent", "Recent", "Recent"};
    private int[] images = {R.mipmap.profile_icon3, R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon3, R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon3, R.mipmap.profile_icon};
    private String[] followers = {"2 Mutual followers", "2 Mutual followers", "3 Mutual followers", "2 Mutual followers", "3 Mutual followers", "2 Mutual followers", "2 Mutual followers", "2 Mutual followers"};


    private Context mContext;
    private int page = 1;
    private boolean isLastPage;
    private boolean isLoading;
    private Integer totalRecord;
    private ArrayList<UserListModel> userList = new ArrayList<>();
    private Themes themes;
    private Boolean darkThemeEnabled;

    public ProfileExploreFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        darkThemeEnabled = CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME);
        themes = new Themes(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            EventBus.getDefault().register(ProfileExploreFragment.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_bottom, container, false);
        View view = binding.getRoot();
        initializedControl();
        setToolBar();
        setOnClickListener();
       /* if (CommonUtils.isOnline(mContext)) {
            callUserListApi(page, "", true);
        } else {
            ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
        }*/
        return view;
    }


    @Override
    public void initializedControl() {
        initView();
        setCurrentTheme();

        etSearch = getActivity().findViewById(R.id.etBroadcastSearch);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0)
                    filter(s.toString());
                else
                {
                    followingRequestModels = new ArrayList<>();
                    followingRequestModels.addAll(prepareData1());
                    profileExploreFragmentAdapter.updatedList(followingRequestModels);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }
    public void filter(String text){

        ArrayList<FollowingRequestModel> filteredName = new ArrayList<>();

        for (FollowingRequestModel model : followingRequestModels){

            if (model.getUserFollowingName().toLowerCase().contains(text.toLowerCase())){
                filteredName.add(model);
            }
        }
        profileExploreFragmentAdapter.updatedList(filteredName);
    }

    private void setCurrentTheme() {
        binding.llHomeFragment.setBackgroundColor(ContextCompat.getColor(mContext, themes.setDarkTheme()));
    }


    private void initView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        binding.rvPost.setLayoutManager(layoutManager);
        followingRequestModels = new ArrayList<>();
        followingRequestModels.addAll(prepareData1());

        Collections.sort(followingRequestModels, new Comparator<FollowingRequestModel>() {
            @Override
            public int compare(FollowingRequestModel o1, FollowingRequestModel o2) {
                return o2.getType().compareTo(o1.getType());
            }
        });
        profileExploreFragmentAdapter = new ProfileExploreFragmentAdapter(followingRequestModels, mContext, this);
        binding.rvPost.setAdapter(profileExploreFragmentAdapter);



     /*   final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        binding.rvPost.setLayoutManager(layoutManager);
        adapter = new UserListAdapter(mContext, userList, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {


            }
        });
        binding.rvPost.setAdapter(adapter);
*/
      /*  binding.rvPost.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastvisibleitemposition = layoutManager.findLastVisibleItemPosition();
                if (userList != null && userList.size() > 0) {
                    if (totalRecord == page) {
                        isLastPage = true;
                    }
                    if (lastvisibleitemposition == adapter.getItemCount() - 1) {

                        if (!isLoading && !isLastPage) {
                            isLoading = true;
                            callUserListApi(++page, "", true);
                        }
                    }
                }
            }
        });*/
        binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
          binding.swipeContainer.setRefreshing(false);

                /*      page = 1;
                isLoading = true;
                isLastPage = false;
                callUserListApi(page, "", true);*/
            }
        });

    }
    private ArrayList<FollowingRequestModel> prepareData1() {
        ArrayList<FollowingRequestModel> data = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            FollowingRequestModel model = new FollowingRequestModel();
            model.setUserFollowersPicture(images[i]);
            model.setUserFollowingName(names[i]);
            model.setUserFollowers(followers[i]);
            model.setType(type[i]);
            data.add(model);
        }
        return data;
    }
    @Override
    public void setToolBar() {

    }

    @Override
    public void setOnClickListener() {

    }

    @Override
    public void onItemClick(View view, int position) {

    }


    private void callUserListApi(final int pageNo, String search, final boolean isProgress) {
        if (CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            if (isProgress) {
                progressDialog.show();
            }
            JsonObject postRequest = new JsonObject();
            postRequest.addProperty("page", page);
            postRequest.addProperty("name", search);

            Call<ApiResponse> call = ApiExecutor.getClient(mContext).apiGetSearchUserList(postRequest);
            call.enqueue(new retrofit2.Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    isLoading = false;
                    try {

                        if (response != null && response.body() != null) {
                            if (response.body().getResponseCode() == AppConstants.SUCCESS) {
                                if (pageNo == 1) {
                                    userList.clear();
                                    binding.swipeContainer.setRefreshing(false);
                                }
                                if (response.body().getUsers().size() > 0) {
                                    userList.addAll(response.body().getUsers());
                                }
                                adapter.notifyDataSetChanged();
                                totalRecord = response.body().getPagination().getMax_page_size();
                            } else if (response.body().getResponseCode() == AppConstants.USER_BLOCKED) {

                            } else {
                                ToastUtils.showToastShort(mContext, response.body().getResponseMessage());
                            }
                        } else {
                            ToastUtils.showToastShort(mContext, getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    if (page == 1) {
                        binding.swipeContainer.setRefreshing(false);
                    }
                    isLoading = false;
                    if (isProgress) {
                        progressDialog.dismiss();
                    }
                    t.printStackTrace();
                    ToastUtils.showToastShort(mContext, getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SearchModel searchModel) {
       /* if (searchModel != null && searchModel.getType() != null && searchModel.getType().length() > 0 && searchModel.getType().equalsIgnoreCase("profile")) {
            if (CommonUtils.isOnline(mContext)) {
                callUserListApi(1, searchModel.getSearch(), true);
            } else {
                ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
            }
        }*/
    }



}
