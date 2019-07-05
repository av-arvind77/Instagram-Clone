package com.yellowseed.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;

import com.yellowseed.R;
import com.yellowseed.adapter.DirectUserPendingAdapter;
import com.yellowseed.databinding.ActivityDirectUserpendingBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.SendToModel;
import com.yellowseed.utils.ActivityController;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;

import java.util.ArrayList;

public class DirectUserpendingActivity extends BaseActivity {
    private ActivityDirectUserpendingBinding binding;
    private Context context;
    private ArrayList<SendToModel> models;
    private LinearLayout llacceptreject;
    public static boolean acceptreject = false;
    private DirectUserPendingAdapter adapter;
    private String[] names = {"John Thomas", "Jenny Koul", "Mike Keel", "Julie Kite", "Tiny Tim", "Julie Kite", "Tiny Tim", "Julie Kite", "Tiny Tim", "Julie Kite", "Tiny Tim"};
    private int[] images = {R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon2, R.mipmap.profile_icon3, R.mipmap.profile_icon2, R.mipmap.profile_icon3, R.mipmap.profile_icon4, R.mipmap.profile_icon3, R.mipmap.profile_icon2, R.mipmap.profile_icon, R.mipmap.profile_icon3, R.mipmap.profile_icon4, R.mipmap.profile_icon3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_direct_userpending);
        context = DirectUserpendingActivity.this;

        initializedControl();
        setToolBar();
        setOnClickListener();

    }

    @Override
    public void initializedControl() {
        itemRecyclerView();

        binding.etDirectPendingSearch.addTextChangedListener(new TextWatcher() {
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

    public void filter(String text){

        ArrayList<SendToModel> filteredName = new ArrayList<>();

        for (SendToModel model : models){

            if (model.getName_url().toLowerCase().contains(text.toLowerCase())){
                filteredName.add(model);
            }
        }


    }

    private void itemRecyclerView() {
        binding.rvDirectPendingg.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvDirectPendingg.setLayoutManager(layoutManager);

        //For divide the recycler item
        DividerItemDecoration itemDecorator = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(context, R.drawable.divider));
        binding.rvDirectPendingg.addItemDecoration(itemDecorator);

        models = new ArrayList<>();
        models.addAll(prepareData());

        binding.rvDirectPendingg.setAdapter(adapter);
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
    public void onBackPressed() {
        finish();
    }

    @Override
    public void setToolBar() {
        binding.layoutDirectPending.ivLeft.setVisibility(View.VISIBLE);
        binding.layoutDirectPending.ivLeft.setImageResource(R.mipmap.back_icon);
        binding.layoutDirectPending.tvHeader.setVisibility(View.VISIBLE);
        binding.layoutDirectPending.tvHeader.setText(R.string.directshare);
        binding.layoutDirectPending.ivRight.setVisibility(View.GONE);
    }

    @Override
    public void setOnClickListener() {
        binding.layoutDirectPending.ivLeft.setOnClickListener(this);
        binding.rvDirectPendingg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivLeft:
                onBackPressed();
                break;
//            case R.id.rvDirectPendingg:
//                finish();
//                break;
            default:
                break;
        }
    }
}