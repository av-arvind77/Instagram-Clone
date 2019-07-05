package com.yellowseed.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.yellowseed.R;
import com.yellowseed.adapter.NewCallAdapter;
import com.yellowseed.databinding.ActivityNewCallBinding;
import com.yellowseed.model.NewCallModel;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.Themes;

import java.util.ArrayList;
import java.util.List;

public class NewCallActivity extends BaseActivity {

    private ActivityNewCallBinding binding;
    private Context context;
    private ArrayList<NewCallModel> newCallModels = new ArrayList<>();
    private NewCallAdapter adapter;
    private String[] name = {"John Thomas", "Jenny Koul", "Mike Keel", "Julie", "Tiny Tim", "Kelly Kim"};
    private int[] img = {R.mipmap.profile_icon, R.mipmap.profile_icon2, R.mipmap.profile_icon3, R.mipmap.profile_icon4, R.mipmap.profile_icon3, R.mipmap.profile_icon};
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_call);
        context = NewCallActivity.this;
        initializedControl();
        setToolBar();
        setOnClickListener();

    }

    @Override
    public void initializedControl() {
        setCurrentTheme();
        chatsRecylerView();

        binding.etCallContactList.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void chatsRecylerView() {
        binding.rvCallContact.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvCallContact.setLayoutManager(layoutManager);
        newCallModels.addAll(prepareData());
        adapter = new NewCallAdapter(context, newCallModels);
        binding.rvCallContact.setAdapter(adapter);
    }

    private List<NewCallModel> prepareData() {
        List<NewCallModel> data = new ArrayList<>();
        for (int i = 0; i < name.length; i++) {
            NewCallModel model = new NewCallModel();
            model.setName(name[i]);
            model.setImg(img[i]);
            data.add(model);
        }
        return data;
    }


    private void setCurrentTheme() {
        binding.layoutChatsContacts.toolbarMain.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        binding.layoutChatsContacts.tvRighttext.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setTolbarText()));
        binding.llMain.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        binding.layoutChatsContacts.tvHeader.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.viewLine.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setViewLine()));

        Themes.getInstance(context).changeIconColor(context, binding.layoutChatsContacts.ivLeft);
        binding.etCallContactList.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setToolbarWhiteText()));
        binding.etCallContactList.setBackground(ContextCompat.getDrawable(context, Themes.getInstance(context).setDarkEditProfileBackground()));
        binding.etCallContactList.setHintTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkEditProfileHint()));

    }


    @Override
    public void setToolBar() {
        binding.layoutChatsContacts.ivLeft.setVisibility(View.VISIBLE);
        binding.layoutChatsContacts.ivLeft.setImageResource(R.mipmap.back_icon);
        binding.layoutChatsContacts.tvHeader.setVisibility(View.VISIBLE);
        binding.layoutChatsContacts.tvHeader.setText("New Call");
        binding.layoutChatsContacts.tvRighttext.setVisibility(View.VISIBLE);
        binding.layoutChatsContacts.tvRighttext.setText("Cancel");
        binding.layoutChatsContacts.ivRight.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivLeft:
                onBackPressed();
                break;
            case R.id.ivRight:
                break;
            case R.id.tvRighttext:
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    public void setOnClickListener() {
        binding.layoutChatsContacts.ivLeft.setOnClickListener(this);
        binding.layoutChatsContacts.ivRight.setOnClickListener(this);
        binding.layoutChatsContacts.tvRighttext.setOnClickListener(this);

    }


}
