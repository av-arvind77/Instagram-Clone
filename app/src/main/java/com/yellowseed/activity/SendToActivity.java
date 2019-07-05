package com.yellowseed.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.yellowseed.R;
import com.yellowseed.adapter.SendToAdapter;
import com.yellowseed.databinding.ActivitySendToBinding;
import com.yellowseed.model.SendToModel;
import com.yellowseed.utils.BaseActivity;

import java.util.ArrayList;

public class SendToActivity extends BaseActivity {

    private ActivitySendToBinding binding;
    private Context context;
    private ArrayList<SendToModel> models;
    private SendToAdapter adapter;
    private String[] names = {"John Thomas", "Jenny Koul", "Mike Keel", "Julie Kite", "Tiny Tim"};
    private int[] images = {R.mipmap.profile_icon, R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon3, R.mipmap.profile_icon};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_send_to);
        context = SendToActivity.this;

        initializedControl();
        setToolBar();
        setOnClickListener();

    }

    @Override
    public void initializedControl() {
        sendToRecyclerView();

        binding.etSendToSearch.addTextChangedListener(new TextWatcher() {
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
        adapter.updatedList(filteredName);

    }

    private void sendToRecyclerView() {

        binding.rvSendTo.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvSendTo.setLayoutManager(layoutManager);
        models = new ArrayList<>();
        models.addAll(prepareData());
        adapter = new SendToAdapter(models, this);
        binding.rvSendTo.setAdapter(adapter);
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
    public void onClick(View view) {

    }
}
