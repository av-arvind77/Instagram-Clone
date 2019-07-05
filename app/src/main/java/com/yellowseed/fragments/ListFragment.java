package com.yellowseed.fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonObject;
import com.yellowseed.R;
import com.yellowseed.activity.CommentsActivity;
import com.yellowseed.activity.FollowerProfileActivity;
import com.yellowseed.activity.LoginActivity;
import com.yellowseed.activity.PostTextActivity;
import com.yellowseed.activity.ShowPostItemsActivity;
import com.yellowseed.activity.SignUpActivity;
import com.yellowseed.adapter.ListFragmentAdapter;
import com.yellowseed.adapter.TagFollowingAdapter;
import com.yellowseed.databinding.FragmentListBinding;
import com.yellowseed.databinding.LayoutRepostBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.HomeBottomViewModel;
import com.yellowseed.model.reqModel.UserModel;
import com.yellowseed.model.resModel.GetRoomResonse;
import com.yellowseed.utils.ActivityController;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseFragment;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.ConverterUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.PrefManager;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;
import com.yellowseed.webservices.ApiRequest;
import com.yellowseed.webservices.ApiResponse;
import com.yellowseed.webservices.requests.posts.PostRequest;
import com.yellowseed.webservices.response.post.Post;
import com.yellowseed.webservices.response.post.PostResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListFragment extends BaseFragment {

    private Context mContext;
    private FragmentListBinding binding;
    private ListFragmentAdapter adapter;
    private ArrayList<Post> models;
    private boolean Flag = false;
    private int totalPage;
    private ArrayList<GetRoomResonse.RoomsBean> roomList = new ArrayList<>();

    @Override
    public void onAttach(Context mContext) {
        super.onAttach(mContext);
        this.mContext = mContext;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false);
        View view = binding.getRoot();
        initializedControl();
        setOnClickListener();
        setRecylerView();
        callPostListAPI();
        return view;

    }

    @Override
    public void initializedControl() {

        binding.rvListRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        binding.rvListRecyclerView.setLayoutManager(layoutManager);
        binding.rvListRecyclerView.setNestedScrollingEnabled(false);
        binding.rvListRecyclerView.setFocusable(false);
        models = new ArrayList<>();
       // models.addAll(prepareData());

        adapter = new ListFragmentAdapter(models, mContext, false, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.llRow:

                        break;
                    case R.id.ivPostProfilePic:
                        ActivityController.startActivity(mContext, FollowerProfileActivity.class);
                        break;
                    case R.id.tvPostUserName:
                        ActivityController.startActivity(mContext, FollowerProfileActivity.class);
                        break;
                    case R.id.ivPostComment:
                        ActivityController.startActivity(mContext, CommentsActivity.class);
                        break;
                    case R.id.ivPostImage:
                        ActivityController.startActivity(mContext, ShowPostItemsActivity.class);
                        break;
                    case R.id.ivHeart:
                        break;
                    case R.id.ivPostMenuItem:
                        break;
                    case R.id.ivPostShare:
                        setPostShareDialoge();
                        break;
                    case R.id.ivPostDownloads:
                        ToastUtils.showToastShort(mContext, "Work in Progress!");
                        break;
                }
            }
        });
        binding.rvListRecyclerView.setAdapter(adapter);
    }


    private void setPostShareDialoge() {
        final CharSequence[] items = {getString(R.string.Repost), getString(R.string.directshare)};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals(getString(R.string.Repost))) {

                    ActivityController.startActivity(mContext, PostTextActivity.class);
                } else if (items[item].equals(getString(R.string.directshare))) {
                    if(CommonUtils.isOnline(mContext)){
                        callGetRoomAPI(1);
                    }else {
                        CommonUtils.showToast(mContext,getString(R.string.internet_alert_msg));
                    }
//                    setPostShareDialogeRecyclerView();
                }


            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

    }

    private void setPostShareDialogeRecyclerView() {
        LayoutRepostBinding repostBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.layout_repost, null, false);


        final Dialog dialog = DialogUtils.createDialog(mContext, repostBinding.getRoot());

        repostBinding.ivCancelRepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });
        repostBinding.ivSendRepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });

        //Setting the recycler view inside the dialog
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        repostBinding.rvRepostHome.setLayoutManager(linearLayoutManager);
        TagFollowingAdapter tagFollowingAdapter = new TagFollowingAdapter(mContext,roomList, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(roomList.get(position).isIs_selected()){
                    roomList.get(position).setIs_selected(false);
                }else {
                    roomList.get(position).setIs_selected(true);
                }
                adapter.notifyItemChanged(position);
            }
        });
        repostBinding.rvRepostHome.setAdapter(tagFollowingAdapter);
        repostBinding.rvRepostHome.setNestedScrollingEnabled(false);

    }


//    private ArrayList<HomeBottomViewModel> prepareData() {
//        ArrayList<HomeBottomViewModel> data = new ArrayList<>();
//        for (int i = 0; i < images.length; i++) {
//            HomeBottomViewModel model = new HomeBottomViewModel();
//            model.setPostTime(time[i]);
//            model.setPostContent(content[i]);
//            model.setPostImage(images[i]);
//            data.add(model);
//        }
//        return data;
//    }


    public void setRecylerView() {

    }


    @Override
    public void setToolBar() {

    }

    @Override
    public void setOnClickListener() {

    }

    private void callPostListAPI() {
        if(CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();
            UserModel userModel = PrefManager.getInstance(mContext).getCurrentUser();
            PostRequest postRequest =  new PostRequest();
            postRequest.setId("152378bb-b7db-4304-b2e5-21ce88e99184");
            postRequest.setPage(1);

            Call<PostResponse> call = ApiExecutor.getClient(mContext).apiUserPostList(postRequest);
            call.enqueue(new retrofit2.Callback<PostResponse>() {
                @Override
                public void onResponse(@NonNull Call<PostResponse> call, @NonNull Response<PostResponse> response) {
                    progressDialog.dismiss();
                    try {
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (response != null && response.body() != null) {
                        if (response.body().getResponseCode() == AppConstants.SUCCESS) {
                            //models.addAll(response.body().getPost());
                            adapter.notifyDataSetChanged();
                        }

                        else if (response.body().getResponseCode() == AppConstants.USER_BLOCKED){
                           
                        }
                        else {
                            ToastUtils.showToastShort(mContext,response.body().getResponseMessage());
                        }
                    }  else {
                        ToastUtils.showToastShort(mContext,getString(R.string.server_error_msg));
                    }
                }
                

                @Override
                public void onFailure(@NonNull Call<PostResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(mContext,getString(R.string.server_error_msg));
                }
            });
        }
        else{
            ToastUtils.showToastShort(mContext,mContext.getString(R.string.internet_alert_msg));
        }
    }

    /**
     * method for create story api
     */
    private void callGetRoomAPI(final int pageNo) {
        if(CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("", pageNo);

            Call<GetRoomResonse> call = ApiExecutor.getClient(mContext).apiGetRoom(jsonObject);
            call.enqueue(new Callback<GetRoomResonse>() {
                @Override
                public void onResponse(@NonNull Call<GetRoomResonse> call, @NonNull final Response<GetRoomResonse> response) {
                    progressDialog.dismiss();
                    try {
                        if (response != null && response.body() != null && response.body().getResponseCode() == 200) {
                            roomList.clear();
                            if (response.body().getRooms() != null && response.body().getRooms().size() > 0) {
                                roomList.addAll(response.body().getRooms());
                            }
                            adapter.notifyDataSetChanged();

                            totalPage = response.body().getTotal_pages();
                            setPostShareDialogeRecyclerView();
                        }
                        else
                        {
                            ToastUtils.showToastShort(mContext,mContext.getString(R.string.server_error_msg));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GetRoomResonse> call, @NonNull Throwable t) {
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
    }

}
