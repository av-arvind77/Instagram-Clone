package com.yellowseed.activity;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.JsonObject;
import com.yellowseed.R;
import com.yellowseed.adapter.FollowRequestAdapter;
import com.yellowseed.adapter.FollowingRequestFollowAdapter;
import com.yellowseed.databinding.ActivityFollowingRequestBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.FollowingRequestModel;
import com.yellowseed.model.reqModel.ApproveRequestList;
import com.yellowseed.model.reqModel.DestroyRequestList;
import com.yellowseed.model.resModel.ApiNotificationResonse;
import com.yellowseed.model.resModel.RequestListResonse;
import com.yellowseed.utils.ActivityController;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;
import com.yellowseed.webservices.ApiResponse;
import com.yellowseed.webservices.requests.UnFollowRequest;
import com.yellowseed.webservices.requests.UserFollow;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowingRequestActivity extends BaseActivity {
    private ActivityFollowingRequestBinding binding;
    private Context context;
    private ArrayList<FollowingRequestModel> followingRequestModels=new ArrayList<>();
    private ArrayList<FollowingRequestModel> followingRequestModelsOne=new ArrayList<>();
    private FollowRequestAdapter followRequestAdapter;
    private FollowingRequestFollowAdapter followingRequestFollowAdapter;
    private String[] names = {"John Thomas", "Jenny Koul", "Mike Keel"};
    private int[] images = {R.mipmap.profile_icon3, R.mipmap.profile_icon3, R.mipmap.profile_icon3};
    private String[] followers = {"2 Mutual followers", "2 Mutual followers", "3 Mutual followers"};
    private int totalPage;
    private ArrayList<RequestListResonse.UsersBean> requestList = new ArrayList<>();
    private int currentPage = 1;
    private int myPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_following_request);
        context = FollowingRequestActivity.this;
        followingRequestModels = new ArrayList<>();
        followingRequestModelsOne = new ArrayList<>();
        initializedControl();
        setToolBar();
        setOnClickListener();

      /*  if(CommonUtils.isOnline(context)) {
            callRequstListAPI(currentPage);
        }
        else
        {
            ToastUtils.showToastShort(context,context.getString(R.string.internet_alert_msg));
        }*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public void initializedControl() {
        setCurrentTheme();
        chatRecyclerViewAccept();
        chatRecyclerViewFollow();
    }











    private void setCurrentTheme() {
        binding.llMain.setBackgroundColor(ContextCompat.getColor(context,Themes.getInstance(context).setDarkTheme()));
        binding.toolbarFollowingRequest.toolbarMain.setBackgroundColor(ContextCompat.getColor(context,Themes.getInstance(context).setDarkTheme()));
        binding.toolbarFollowingRequest.tvHeader.setTextColor(ContextCompat.getColor(context,Themes.getInstance(context).setDarkThemeText()));
        binding.followSuggestions.setTextColor(ContextCompat.getColor(context,Themes.getInstance(context).setSuggestionText()));
        binding.followSeeAll.setTextColor(ContextCompat.getColor(context,Themes.getInstance(context).setSeeAll()));

        Themes.getInstance(context).changeIconColor(context,binding.toolbarFollowingRequest.ivLeft);
        Themes.getInstance(context).changeIconColor(context,binding.ivArrow);
        binding.view.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setViewLineGrey()));
        binding.followView.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setViewLineGrey()));
        binding.followView.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setViewLineGrey()));


    }

  /*  private void chatRecyclerViewFollow() {
  *//*      final LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);


        binding.rvAcceptReject.setLayoutManager(layoutManager);

        //For divide the recycler item
        DividerItemDecoration itemDecorator = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(context, R.drawable.divider));
        binding.rvAcceptReject.addItemDecoration(itemDecorator);

        followingRequestModelsOne = new ArrayList<>();
//        followingRequestModelsOne.addAll(prepareData1());
        followRequestAdapter = new FollowRequestAdapter(requestList, this, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                myPosition = position;

                switch (view.getId()) {
                    case R.id.llFollowUSer:
                        ActivityController.startActivity(context, FollowerProfileActivity.class);
                        break;
                    case R.id.btnFollowingAccept:
                        callApproveRequstAPI(position);
                        break;
                    case R.id.btnFollowingReject:
                        apiDestroyUser(position);
                        break;
                    default:
                        break;

                }
            }
        }
        );
        binding.rvAcceptReject.setAdapter(followRequestAdapter);


        binding.rvAcceptReject.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int lastvisibleitemposition = layoutManager.findLastVisibleItemPosition();
                if (requestList != null && requestList.size() > 0) {

                    if (lastvisibleitemposition == followRequestAdapter.getItemCount() - 1) {
                        if (currentPage < totalPage){
                            callRequstListAPI(++currentPage);
                        }
                    }
                }
            }
        });

    }*//*



            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            binding.rvAcceptReject.setLayoutManager(layoutManager);

            //For divide the recycler item
            *//*DividerItemDecoration itemDecorator = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
            itemDecorator.setDrawable(ContextCompat.getDrawable(context, R.drawable.divider));
            binding.rvAcceptReject.addItemDecoration(itemDecorator);
*//*
            followingRequestModelsOne = new ArrayList<>();
            followingRequestModelsOne.addAll(prepareData1());
            followRequestAdapter = new FollowRequestAdapter(followingRequestModelsOne, this, new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    switch (view.getId()) {
                        case R.id.llFollowUSer:
                            ActivityController.startActivity(context, FollowerProfileActivity.class);
                            break;
                        case R.id.btnFollowingAccept:
                            followingRequestModelsOne.remove(position);
                            followRequestAdapter.notifyItemRemoved(position);
                            break;
                        case R.id.btnFollowingReject:
                            followingRequestModelsOne.remove(position);
                            followRequestAdapter.notifyItemRemoved(position);
                            break;
                        default:
                            break;

                    }
                }
            }
            );
            binding.rvAcceptReject.setAdapter(followRequestAdapter);

        }
*/


//    private void chatRecyclerViewAccept() {
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        binding.rvFollowCross.setLayoutManager(layoutManager);
//
//        //For divide the recycler item
//        DividerItemDecoration itemDecorator = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
//        itemDecorator.setDrawable(ContextCompat.getDrawable(context, R.drawable.divider));
//        binding.rvFollowCross.addItemDecoration(itemDecorator);
//
//        followingRequestModels = new ArrayList<>();
//        followingRequestModels.addAll(prepareData());
//        followingRequestFollowAdapter = new FollowingRequestFollowAdapter(followingRequestModels, this, new OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                switch (view.getId()) {
//                    case R.id.llFollowUSer:
//                        ActivityController.startActivity(context, FollowerProfileActivity.class);
//                        break;
//                    case R.id.btnFollowCross:
//                        followingRequestModels.remove(position);
//                        followingRequestFollowAdapter.notifyItemRemoved(position);
//                        break;
//                    case R.id.btnFollowingAccept:
//                        followingRequestModels.remove(position);
//                        followingRequestFollowAdapter.notifyItemRemoved(position);
//                        break;
//                    default:
//                        break;
//
//                }
//            }
//        }
//        );
//        binding.rvFollowCross.setAdapter(followingRequestFollowAdapter);
//
//
//
//
//    }

//    private ArrayList<FollowingRequestModel> prepareData1() {
//        ArrayList<FollowingRequestModel> data = new ArrayList<>();
//        for (int i = 0; i < names.length; i++) {
//            FollowingRequestModel model = new FollowingRequestModel();
//            model.setUserFollowersPicture(images[i]);
//            model.setUserFollowingName(names[i]);
//            model.setUserFollowers(followers[i]);
//            data.add(model);
//        }
//        return data;
//    }

//    private ArrayList<FollowingRequestModel> prepareData() {
//        ArrayList<FollowingRequestModel> data = new ArrayList<>();
//        for (int i = 0; i < names.length; i++) {
//            FollowingRequestModel model = new FollowingRequestModel();
//            model.setUserFollowersPicture(images[i]);
//            model.setUserFollowingName(names[i]);
//            model.setUserFollowers(followers[i]);
//            data.add(model);
//        }
//        return data;
//    }

       /* private ArrayList<FollowingRequestModel> prepareData1() {
            ArrayList<FollowingRequestModel> data = new ArrayList<>();
            for (int i = 0; i < names.length; i++) {
                FollowingRequestModel model = new FollowingRequestModel();
                model.setUserFollowersPicture(images[i]);
                model.setUserFollowingName(names[i]);
                model.setUserFollowers(followers[i]);
                data.add(model);
            }
            return data;
        }
*/










    private void chatRecyclerViewFollow() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvAcceptReject.setLayoutManager(layoutManager);

        //For divide the recycler item
      /*  DividerItemDecoration itemDecorator = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(context, R.drawable.divider));
        binding.rvAcceptReject.addItemDecoration(itemDecorator);
*/
        followingRequestModelsOne = new ArrayList<>();
        followingRequestModelsOne.addAll(prepareData1());
        followRequestAdapter = new FollowRequestAdapter(followingRequestModelsOne, this, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.llFollowUSer:
                        ActivityController.startActivity(context, FollowerProfileActivity.class);
                        break;
                    case R.id.btnFollowingAccept:
                        followingRequestModelsOne.remove(position);
                        followRequestAdapter.notifyItemRemoved(position);
                        break;
                    case R.id.btnFollowingReject:
                        followingRequestModelsOne.remove(position);
                        followRequestAdapter.notifyItemRemoved(position);
                        break;
                    default:
                        break;

                }
            }
        }
        );
        binding.rvAcceptReject.setAdapter(followRequestAdapter);

    }


    private void chatRecyclerViewAccept() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvFollowCross.setLayoutManager(layoutManager);

        //For divide the recycler item
     /*   DividerItemDecoration itemDecorator = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(context, R.drawable.divider));
        binding.rvFollowCross.addItemDecoration(itemDecorator);
*/
        followingRequestModels = new ArrayList<>();
        followingRequestModels.addAll(prepareData());
        followingRequestFollowAdapter = new FollowingRequestFollowAdapter(followingRequestModels, this, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.llFollowUSer:
                        ActivityController.startActivity(context, MyProfileActivity.class);
                        break;
                    case R.id.btnFollowCross:
                        followingRequestModels.remove(position);
                        followingRequestFollowAdapter.notifyItemRemoved(position);
                        break;
                    case R.id.btnFollowingAccept:
                        followingRequestModels.remove(position);
                        followingRequestFollowAdapter.notifyItemRemoved(position);
                        break;
                    default:
                        break;

                }
            }
        }
        );
        binding.rvFollowCross.setAdapter(followingRequestFollowAdapter);




    }

    private ArrayList<FollowingRequestModel> prepareData1() {
        ArrayList<FollowingRequestModel> data = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            FollowingRequestModel model = new FollowingRequestModel();
            model.setUserFollowersPicture(images[i]);
            model.setUserFollowingName(names[i]);
            model.setUserFollowers(followers[i]);
            data.add(model);
        }
        return data;
    }

    private ArrayList<FollowingRequestModel> prepareData() {
        ArrayList<FollowingRequestModel> data = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            FollowingRequestModel model = new FollowingRequestModel();
            model.setUserFollowersPicture(images[i]);
            model.setUserFollowingName(names[i]);
            model.setUserFollowers(followers[i]);
            data.add(model);
        }
        return data;
    }










        @Override
    public void setToolBar() {
        binding.toolbarFollowingRequest.ivLeft.setVisibility(View.VISIBLE);
        binding.toolbarFollowingRequest.ivLeft.setImageResource(R.mipmap.back_icon);
        binding.toolbarFollowingRequest.tvHeader.setVisibility(View.VISIBLE);
        binding.toolbarFollowingRequest.tvHeader.setText(R.string.folllowrequests);
        binding.toolbarFollowingRequest.ivRight.setVisibility(View.GONE);

    }

    @Override
    public void setOnClickListener() {
        binding.toolbarFollowingRequest.ivLeft.setOnClickListener(this);
        binding.followSeeAll.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.ivLeft:
                finish();
                break;
            case R.id.followSeeAll:
                followingRequestModelsOne.addAll(prepareData1());
                followRequestAdapter.notifyDataSetChanged();
                binding.followSeeAll.setVisibility(View.GONE);
                binding.ivArrow.setVisibility(View.GONE);
                break;
            default:
                break;

           /* case R.id.ivLeft:
                finish();
                break;
            case R.id.followSeeAll:
//                followingRequestModelsOne.addAll(prepareData1());
                followRequestAdapter.notifyDataSetChanged();
                binding.followSeeAll.setVisibility(View.GONE);
                break;
            default:
                break;*/
        }
    }

    /**
     * method for create story api
     */
    private void callRequstListAPI(final int pageNo) {
        if(CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("page_no", pageNo);

            Call<RequestListResonse> call = ApiExecutor.getClient(context).apiRequestList(jsonObject);
            call.enqueue(new Callback<RequestListResonse>() {
                @Override
                public void onResponse(@NonNull Call<RequestListResonse> call, @NonNull final Response<RequestListResonse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            requestList.clear();
                            if (response.body().getUsers() != null && response.body().getUsers().size() > 0) {
                                requestList.addAll(response.body().getUsers());
                            }
                            followRequestAdapter.notifyDataSetChanged();

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
                public void onFailure(@NonNull Call<RequestListResonse> call, @NonNull Throwable t) {
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
    }


    /**
     * Approve request
     * @param position
     * @return
     */
    private ApproveRequestList approveRequestList(int position){
        ApproveRequestList list = new ApproveRequestList();

        ApproveRequestList.UserBean userBean = new ApproveRequestList.UserBean();
        userBean.setId(requestList.get(position).getId());

        list.setUser(userBean);

        return list;
    }


    /**
     * method for create story api
     */
    private void callApproveRequstAPI(int position) {
        if(CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();

            Call<RequestListResonse> call = ApiExecutor.getClient(context).apiApproveRequest(approveRequestList(position));
            call.enqueue(new Callback<RequestListResonse>() {
                @Override
                public void onResponse(@NonNull Call<RequestListResonse> call, @NonNull final Response<RequestListResonse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            if (response.body().getResponseMessage() != null) {
                                CommonUtils.showToast(context, response.body().getResponseMessage());
                            }
                            requestList.remove(myPosition);
                            followRequestAdapter.notifyItemRemoved(myPosition);
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
                public void onFailure(@NonNull Call<RequestListResonse> call, @NonNull Throwable t) {
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
    }

    /**
     * Approve request
     * @param position
     * @return
     */
    private DestroyRequestList destroyRequestList(int position){
        DestroyRequestList list = new DestroyRequestList();

        DestroyRequestList.UserBean userBean = new DestroyRequestList.UserBean();
        userBean.setId(requestList.get(position).getId());
        userBean.setDoX("reject");

        list.setUser(userBean);

        return list;
    }

    private void apiDestroyUser(final int position) {
        if(CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();

            Call<ApiResponse> call = ApiExecutor.getClient(context).apiDestroyRequest(destroyRequestList(position));
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            if (response.body().getResponseMessage() != null){
                                CommonUtils.showToast(context, response.body().getResponseMessage());
                            }
                            requestList.remove(position);
                            followRequestAdapter.notifyItemRemoved(position);
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
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
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
    }
}