package com.yellowseed.fragments;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonObject;
import com.yellowseed.R;
import com.yellowseed.activity.AddContactToBlockActivity;
import com.yellowseed.adapter.BlockContactListAdapter;
import com.yellowseed.adapter.BlockedContactsAdapter;
import com.yellowseed.databinding.FragmentBlockedProfilesBinding;
import com.yellowseed.listener.OnClickListener;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.listener.OnItemTouchHelper;
import com.yellowseed.model.BlockedContactsModel;
import com.yellowseed.model.UserListModel;
import com.yellowseed.utils.ActivityController;
import com.yellowseed.utils.BaseFragment;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.webservices.ApiExecutor;
import com.yellowseed.webservices.response.BlockUserListResponse;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class BlockedProfilesFragment extends BaseFragment implements View.OnClickListener {

    private FragmentBlockedProfilesBinding binding;
    private Context context;

    private ArrayList<BlockedContactsModel> blockUserList = new ArrayList<>();
    private String[] names = {"John Thomas", "Jenny Koul", "Mike Keel", "Julie Kite", "Tiny Tim", "Julie Kite", "Tiny Tim", "Julie Kite", "Tiny Tim", "Julie Kite", "Tiny Tim", "Tiny Tim", "Julie Kite", "Tiny Tim", "Julie Kite", "Tiny Tim"};
    private int[] images = {R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon3, R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon3, R.mipmap.profile_icon3, R.mipmap.profile_icon3, R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon3, R.mipmap.profile_icon3};

    public BlockedProfilesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_blocked_profiles, container, false);

        //callBlockListApi();
        setToolBar();
        initializedControl();
        setOnClickListener();
      /*  binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callBlockListApi();
            }
        });*/
        return binding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();
        setTheme();
     //   callBlockListApi();
    }

    /**
     * Block List Api
     */
/*    private void callBlockListApi() {
        if (CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("page", "1");

            Call<BlockUserListResponse> call = ApiExecutor.getClient(context).blockUserListApi(jsonObject);
            call.enqueue(new retrofit2.Callback<BlockUserListResponse>() {


                @Override
                public void onResponse(@NonNull Call<BlockUserListResponse> call, Response<BlockUserListResponse> response) {
                    progressDialog.dismiss();
                    binding.swipeContainer.setRefreshing(false);
                    if (response.body() != null && response.body().getResponseCode() == 200) {
                        if (!response.body().getResponseMessage().equalsIgnoreCase("")) {
                            CommonUtils.showToast(context, response.body().getResponseMessage());
                        }
                        if (response.body().getUsers() != null && response.body().getUsers().size() > 0) {
                            blockUserList.clear();
                            blockUserList.addAll(response.body().getUsers());
                        }
                        initializedControl();
                    }

                }

                @Override
                public void onFailure(Call<BlockUserListResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    CommonUtils.showToast(context, getString(R.string.server_error_msg));
                }
            });
        }
    }*/
    private void setTheme() {
        binding.tvAddNewBlockedProfile.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setTolbarText()));
    }
    @Override
    public void initializedControl() {
        binding.blockedProfilesContainer.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.blockedProfilesContainer.setLayoutManager(layoutManager);

        blockUserList.addAll(prepareData());

        BlockedContactsAdapter adapter = new BlockedContactsAdapter( blockUserList, context, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        binding.blockedProfilesContainer.setAdapter(adapter);



    }



    private ArrayList<BlockedContactsModel> prepareData() {
        ArrayList<BlockedContactsModel> data = new ArrayList<>();
        for (int i = 0; i < images.length; i++) {
            BlockedContactsModel item = new BlockedContactsModel();
            item.setUserName(names[i]);
            item.setUserPicture(images[i]);
            data.add(item);
        }
        return data;
    }

    @Override
    public void setToolBar() {

    }

    @Override
    public void setOnClickListener() {
        binding.tvAddNewBlockedProfile.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add_new_blocked_profile:
                ActivityController.startActivity(context, AddContactToBlockActivity.class);
                break;
            default:
                break;
        }
    }
}


