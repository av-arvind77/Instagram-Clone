package com.yellowseed.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;

import android.support.v7.widget.GridLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.JsonObject;
import com.yellowseed.R;
import com.yellowseed.activity.DirectScreenActivity;
import com.yellowseed.activity.ExploreSearchActivity;
import com.yellowseed.adapter.ExploreViewAdapter;
import com.yellowseed.databinding.FragmentHomeBottomBinding;
import com.yellowseed.imageUtils.CheckPermission;
import com.yellowseed.imageUtils.TakePictureUtils;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.resModel.StoryListModel;
import com.yellowseed.utils.ActivityController;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseFragment;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;
import com.yellowseed.webservices.response.User;
import com.yellowseed.webservices.response.homepost.Post;
import com.yellowseed.webservices.response.homepost.PostResponse;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;


public class ExploreBottomFragment extends BaseFragment implements View.OnClickListener {
    int page = 1;
    private ExploreViewAdapter adapter;
    private FragmentHomeBottomBinding binding;
    private Context mContext;
    private ImageView ivRight,ivLeft;
    private ArrayList<StoryListModel.StoriesBean> storyList = new ArrayList<>();
    private ArrayList<Post> models = new ArrayList<>();
    private boolean isLastPage;
    private boolean isLoading;
    private Integer totalRecord;
    private ArrayList<User> userList = new ArrayList<>();
    private Themes themes;
    private Boolean darkThemeEnabled;
    private LocalBroadcastManager lbm;
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override

        public void onReceive(Context context, Intent intent) {

            String data = intent.getStringExtra("key");
            adapter.notifyDataSetChanged();
            setCurrentTheme();

            try {
                setHeaderRecylerView();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    };

    public ExploreBottomFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        darkThemeEnabled = CommonUtils.getPreferencesBoolean(mContext, AppConstants.DARK_THEME);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        models = new ArrayList<>();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_bottom, container, false);
        View view = binding.getRoot();

        lbm = LocalBroadcastManager.getInstance(getActivity());
        lbm.registerReceiver(receiver, new IntentFilter("theme_change"));
        initializedControl();
        setToolBar();
        setOnClickListener();
        try {
            setHeaderRecylerView();
        } catch (IOException e) {
            e.printStackTrace();
        }
       /* if (CommonUtils.isOnline(mContext)) {
            callPostListAPI(page);
        } else {
            ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
        }*/
        /*if (CommonUtils.isOnline(mContext)) {
            callStoryListAPI(page);
        } else {
            ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
        }*/
        /*if (CommonUtils.isOnline(mContext)) {
            callTrendingProfile(page);
        } else {
            ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
        }*/
        return view;

    }

    private void setHeaderRecylerView() throws IOException {


        final GridLayoutManager manager = new GridLayoutManager(mContext, 3);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (adapter.getItemViewType(position)) {
                    case 0:
                        return manager.getSpanCount();
                    case 2:
                        return manager.getSpanCount();
                    default:
                        return 1;
                }
            }
        });
        binding.rvPost.setLayoutManager(manager);
        adapter = new ExploreViewAdapter(models, storyList, userList, mContext, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.ivPostProfilePic:
                    case R.id.tvPostUserName:
                    default:
                        break;
                }
            }
        });
        binding.rvPost.setAdapter(adapter);

     /*   binding.rvPost.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int lastvisibleitemposition = manager.findLastVisibleItemPosition();
                if (models != null && models.size() > 0) {
                    if (adapter != null && adapter.getItemCount() - 1 == totalRecord) {
                        isLastPage = true;
                    }

                    if (lastvisibleitemposition == adapter.getItemCount() - 1) {

                        if (!isLoading && !isLastPage) {
                            isLoading = true;
                            callPostListAPI(++page);
                        }

                    }
                }
            }
        });*/

        binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.swipeContainer.setRefreshing(false);
               /* page = 1;
                isLoading = true;
                callStoryListAPI(page);
                callPostListAPI(page);
                callTrendingProfile(page);*/
            }
        });

    }


    @Override
    public void initializedControl() {
        setCurrentTheme();
        try {
            setHeaderRecylerView();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ivRight = (ImageView) getActivity().findViewById(R.id.ivRight);
        ivLeft = (ImageView) getActivity().findViewById(R.id.ivLeft);

    }

    private void setCurrentTheme() {
        // binding.llHomeFragment.setBackgroundColor(ContextCompat.getColor(mContext,themes.setDarkTheme()));
    }

    @Override
    public void setToolBar() {

        ivRight.setImageResource(R.mipmap.search_header_img);
        ivLeft.setVisibility(View.GONE);
    }


    @Override
    public void setOnClickListener() {
        ivRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivRight:
                ActivityController.startActivity(mContext, ExploreSearchActivity.class);
                break;
            case R.id.llHomeFragment:
                CommonUtils.hideSoftKeyboard((Activity) mContext);
                break;
        }
    }


    /*
    This method is being used for taking picture from gallery
    */
    public void openGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, TakePictureUtils.PICK_GALLERY);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CheckPermission.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    //addPhotoDialog();
                } else {
                    CommonUtils.showToast(mContext, "Permission denial");
                }
                break;
        }
    }


    private void callPostListAPI(final int pageNo) {
        if (CommonUtils.isOnline(mContext)) {
            JsonObject postRequest = new JsonObject();
            postRequest.addProperty("page", page);

            Call<PostResponse> call = ApiExecutor.getClient(mContext).trendingPostApi(postRequest);
            call.enqueue(new retrofit2.Callback<PostResponse>() {
                @Override
                public void onResponse(@NonNull Call<PostResponse> call, @NonNull Response<PostResponse> response) {
                  /*if(progressDialog!=null && progressDialog.isShowing()){
                      progressDialog.dismiss();
                  }*/
                    isLoading = false;
                    try {

                        if (response != null && response.body() != null) {
                            if (response.body().getResponseCode() == AppConstants.SUCCESS) {
                                if (pageNo == 1) {
                                    models.clear();
                                    binding.swipeContainer.setRefreshing(false);
                                }
                                if (response.body().getPost().size() > 0) {
                                    models.addAll(response.body().getPost());
                                    adapter.notifyDataSetChanged();
                                }
                                totalRecord = response.body().getPagination().getTotalRecords();
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
                public void onFailure(@NonNull Call<PostResponse> call, @NonNull Throwable t) {
                    if (pageNo == 1) {
                        binding.swipeContainer.setRefreshing(false);
                    }
                    isLoading = false;
                    t.printStackTrace();
                    ToastUtils.showToastShort(mContext, getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
        }
    }


    /**
     * method for call story list api
     */
    private void callStoryListAPI(int page) {
        if (CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("page", page);

            Call<StoryListModel> call = ApiExecutor.getClient(mContext).trendingStoriesApi(jsonObject);
            call.enqueue(new retrofit2.Callback<StoryListModel>() {
                @Override
                public void onResponse(@NonNull Call<StoryListModel> call, @NonNull final Response<StoryListModel> response) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            storyList.clear();
                            storyList.addAll(response.body().getStories());
                            adapter.notifyDataSetChanged();
                        } else {
                            ToastUtils.showToastShort(mContext, mContext.getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<StoryListModel> call, @NonNull Throwable t) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    t.printStackTrace();
                    ToastUtils.showToastShort(mContext, mContext.getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
        }
    }


    /**
     * method for call story list api
     */
    private void callTrendingProfile(int page) {
        if (CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("page", page);

            Call<StoryListModel> call = ApiExecutor.getClient(mContext).trendingProfileApi(jsonObject);
            call.enqueue(new retrofit2.Callback<StoryListModel>() {
                @Override
                public void onResponse(@NonNull Call<StoryListModel> call, @NonNull final Response<StoryListModel> response) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            userList.clear();
                            userList.addAll(response.body().getUsers());
                            adapter.notifyDataSetChanged();
                        } else {
                            ToastUtils.showToastShort(mContext, mContext.getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<StoryListModel> call, @NonNull Throwable t) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    t.printStackTrace();
                    ToastUtils.showToastShort(mContext, mContext.getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
        }
    }

    @Override
    public void onDestroy() {
        lbm.unregisterReceiver(receiver);
        super.onDestroy();
    }
}
