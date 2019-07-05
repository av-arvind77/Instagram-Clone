package com.yellowseed.fragments.FragmentExploreSearch;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;

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
import com.yellowseed.activity.FollowerProfileActivity;
import com.yellowseed.activity.MyProfileActivity;
import com.yellowseed.adapter.TagsExploreFragmentAdapter;
import com.yellowseed.databinding.FragmentHomeBottomBinding;
import com.yellowseed.databinding.FragmentTagsExploreBinding;
import com.yellowseed.listener.OnItemClickListener;

import com.yellowseed.model.SearchModel;
import com.yellowseed.model.SendToModel;
import com.yellowseed.utils.ActivityController;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseFragment;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;

import com.yellowseed.webservices.response.homepost.Post;
import com.yellowseed.webservices.response.homepost.PostResponse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;


public class TagsExploreFragment extends BaseFragment implements OnItemClickListener {
    private FragmentTagsExploreBinding binding;
    private Context mContext;
    private EditText etSearch;
    private TagsExploreFragmentAdapter adapter;
    private ArrayList<SendToModel> models;
    private int page = 1;
    private boolean isLastPage;
    private boolean isLoading;
    private int totalRecord;
    private String[] names = {"John Thomas", "Jenny Koul", "Mike Keel", "Julie Kite", "Tiny Tim", "Julie Kite", "Tiny Tim", "Julie Kite", "Tiny Tim", "Julie Kite", "Tiny Tim"};
    private int[] images = {R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon3, R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon3, R.mipmap.profile_icon};


    public TagsExploreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            EventBus.getDefault().register(TagsExploreFragment.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tags_explore, container, false);
        View view = binding.getRoot();


        initializedControl();

        setOnClickListener();
       /* if(CommonUtils.isOnline(mContext)) {
            callPostListAPI(page,"",true);
        }
        else
        {
            ToastUtils.showToastShort(mContext,mContext.getString(R.string.internet_alert_msg));
        }*/
        return view;
    }

    @Override
    public void initializedControl() {
        initView();

        etSearch = (EditText) getActivity().findViewById(R.id.etBroadcastSearch);

        etSearch.addTextChangedListener(new TextWatcher() {
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


    public void filter(String text) {

        ArrayList<SendToModel> filteredName = new ArrayList<>();

        for (SendToModel model : models) {

            if (model.getName_url().toLowerCase().contains(text.toLowerCase())) {
                filteredName.add(model);
            }

        }
        adapter.updatedList(filteredName);

    }


    private void initView() {
        binding.rvTagsExplore.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        binding.rvTagsExplore.setLayoutManager(layoutManager);

        //For divide the recycler item
        /*DividerItemDecoration itemDecorator = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.divider));
        binding.rvTagsExplore.addItemDecoration(itemDecorator);
*/
        models = new ArrayList<>();
        models.addAll(prepareData());
        adapter = new TagsExploreFragmentAdapter(models, mContext, this);
        binding.rvTagsExplore.setAdapter(adapter);
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

    }

    @Override
    public void setOnClickListener() {

    }

    @Override
    public void onItemClick(View view, int position) {
        switch (view.getId()) {
            case R.id.llContacts:
                ActivityController.startActivity(mContext, MyProfileActivity.class);
                break;
        }
    }
}

   /* @Override
    public void initializedControl() {
        initView();
    }

    @Override
    public void setToolBar() {

    }

 *//*   private void initView() {
        binding.rvPost.setHasFixedSize(true);
        final GridLayoutManager linearLayoutManager = new GridLayoutManager(mContext, 3);
        binding.rvPost.setLayoutManager(linearLayoutManager);
        adapter = new TagsExploreFragmentAdapter(mContext, models, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        binding.rvPost.setAdapter(adapter);
       *//**//* binding.rvPost.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastvisibleitemposition = linearLayoutManager.findLastVisibleItemPosition();
                if (models != null && models.size() > 0) {
                    if (totalRecord==page) {
                        isLastPage = true;
                    }
                    if (lastvisibleitemposition == adapter.getItemCount() - 1) {
                        if (!isLoading && !isLastPage) {
                            isLoading = true;
                            callPostListAPI(++page,"",true);
                        }
                    }
                }
            }
        });*//**//*
        binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                binding.swipeContainer.setRefreshing(false);
                *//**//*  page = 1;
                isLoading = true;
                isLastPage=false;
                callPostListAPI(page,"",true);*//**//*
            }
        });
    }*//*


    public void filter(String text){

        ArrayList<SendToModel> filteredName = new ArrayList<>();

        for (SendToModel model : models){

            if (model.getName_url().toLowerCase().contains(text.toLowerCase())){
                filteredName.add(model);
            }

        }
        adapter.updatedList(filteredName);

    }




    private void initView() {
        binding.rvTagsExplore.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mcontext);
        binding.rvTagsExplore.setLayoutManager(layoutManager);

        //For divide the recycler item
        DividerItemDecoration itemDecorator = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(context, R.drawable.divider));
        binding.rvTagsExplore.addItemDecoration(itemDecorator);

        models = new ArrayList<>();
        models.addAll(prepareData());
        adapter = new TagsExploreFragmentAdapter(models, context,this);
        binding.rvTagsExplore.setAdapter(adapter);
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
    public void setOnClickListener() {

    }

    @Override
    public void onItemClick(View view, int position) {
        switch (view.getId()) {

        }
    }

    private void callPostListAPI(final int pageNo, String search, final boolean isProgress) {
        if (CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            if (isProgress) {
                progressDialog.show();
            }
            JsonObject postRequest = new JsonObject();
            postRequest.addProperty("page", page);
            postRequest.addProperty("name", search);

            Call<PostResponse> call = ApiExecutor.getClient(mContext).searchWithHashTags(postRequest);
            call.enqueue(new retrofit2.Callback<PostResponse>() {
                @Override
                public void onResponse(@NonNull Call<PostResponse> call, @NonNull Response<PostResponse> response) {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
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
                                }
                                adapter.notifyDataSetChanged();
                                totalRecord = response.body().getPagination().getMaxPageSize();
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
                    isLoading = false;
                    if (page == 1) {
                        binding.swipeContainer.setRefreshing(false);
                    }
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
        *//*if(searchModel!=null&&searchModel.getType()!=null&&searchModel.getType().length()>0&&searchModel.getType().equalsIgnoreCase("post")) {
            if(CommonUtils.isOnline(mContext)) {
                callPostListAPI(1,searchModel.getSearch(),true);
            }
            else
            {
                ToastUtils.showToastShort(mContext,mContext.getString(R.string.internet_alert_msg));
            }
        }*//*
    }
}
*/