package com.yellowseed.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yellowseed.R;
import com.yellowseed.adapter.LiveUserAdapter;
import com.yellowseed.databinding.ActivityLiveUserBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.InstanceListModel;
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
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class LiveUserActivity extends BaseActivity {
    private ActivityLiveUserBinding binding;
    private Context mContext;
    private List<InstanceListModel> arlLiveUsers = new ArrayList<>();
    private LiveUserAdapter liveUserAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_live_user);
        mContext = LiveUserActivity.this;
        setToolBar();
        setOnClickListener();
        initializedControl();
    }

    @Override
    public void initializedControl() {
        setCurrentTheme();
        liveUserAdapter = new LiveUserAdapter(mContext, arlLiveUsers, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(mContext, GoLiveActivity.class).putExtra(AppConstants.USER_ID, /*arlLiveUsers.get(position).getName()*/"Vikas"));
            }
        });
        binding.rvLiveUsers.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
        binding.rvLiveUsers.setLayoutManager(layoutManager);
        binding.rvLiveUsers.setAdapter(liveUserAdapter);
    }

    private void setCurrentTheme() {
        binding.llMain.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkTheme()));
        binding.storyViewToolBar.toolbarMain.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkTheme()));
        binding.storyViewToolBar.tvHeader.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        binding.storyViewToolBar.tvRighttext.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        binding.tvNoDataFound.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        binding.storyViewToolBar.tvGoLive.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setYellow()));
        binding.viewLine.setBackgroundColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setViewLineGrey()));
        Themes.getInstance(mContext).changeIconColor(mContext, binding.storyViewToolBar.ivLeft);
       // Themes.getInstance(mContext).changeIconColor(mContext, binding.storyViewToolBar.ivLive);

       // binding.storyViewToolBar.tvGoLive.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.video_ico, 0, 0);

    }

    /**
     * LikeCommentApi
     */
    private void getAllLiveUser() {
        if (CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();
            Call<ApiResponse> call = ApiExecutor.getClient(mContext).apiLiveUsers();
            call.enqueue(new retrofit2.Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response.body() != null && response.body().getResponseCode() == 200) {
                            if (response.body().getStreams() != null && response.body().getStreams().getInstanceList() != null && response.body().getStreams().getInstanceList().size() > 0) {

                                if (response.body().getStreams().getInstanceList() != null && response.body().getStreams().getInstanceList().size() > 0) {
                                    arlLiveUsers.clear();
                                    arlLiveUsers.addAll(response.body().getStreams().getInstanceList().get(0).getIncomingStreams());
                                    liveUserAdapter.notifyDataSetChanged();

                                }


                            }
                            if (arlLiveUsers.size() > 0) {
                                binding.rvLiveUsers.setVisibility(View.VISIBLE);
                                binding.tvNoDataFound.setVisibility(View.GONE);
                            } else {
                                binding.rvLiveUsers.setVisibility(View.GONE);
                                binding.tvNoDataFound.setVisibility(View.VISIBLE);
                            }
                        } else {
                            ToastUtils.showToastShort(mContext, mContext.getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    ToastUtils.showToastShort(mContext, mContext.getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
        }
    }

    @Override
    public void setToolBar() {
        binding.storyViewToolBar.ivLeft.setVisibility(View.VISIBLE);
        binding.storyViewToolBar.ivLeft.setImageResource(R.mipmap.back_icon);
        binding.storyViewToolBar.tvHeader.setVisibility(View.VISIBLE);
        binding.storyViewToolBar.tvHeader.setText("Live Users");
        binding.storyViewToolBar.tvRighttext.setVisibility(View.VISIBLE);
        binding.storyViewToolBar.tvRighttext.setText("Go Live");
      //  binding.storyViewToolBar.tvGoLive.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.video_ico, 0, 0);

        //  binding.storyViewToolBar.ivLive.setVisibility(View.VISIBLE);

          }

    @Override
    public void setOnClickListener() {
        binding.storyViewToolBar.ivLeft.setOnClickListener(this);
        binding.storyViewToolBar.tvGoLive.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivLeft:
                finish();
                break;
            case R.id.tvGoLive:
                startActivity(new Intent(mContext, GoLiveActivity.class).putExtra(AppConstants.USER_ID, new PrefManager(mContext).getUserId()));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*if (CommonUtils.isOnline(mContext))
            getAllLiveUser();
        else
            ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));*/
    }
}
