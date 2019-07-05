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

import com.yellowseed.R;
import com.yellowseed.adapter.DirectUserAddAdapter;
import com.yellowseed.adapter.DirectUserAddAdapterDemo;
import com.yellowseed.databinding.ActivityDirectUserAddBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.listener.OnRadioItemClickListener;
import com.yellowseed.model.DirectModel;
import com.yellowseed.model.SendToModel;
import com.yellowseed.utils.ActivityController;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.Themes;

import java.util.ArrayList;

public class DirectUserAddActivity extends BaseActivity {

    private ActivityDirectUserAddBinding binding;
    private Context context;
    private ArrayList<DirectModel> models;
    private DirectUserAddAdapterDemo adapter;
    private String[] names = {"John Thomas", "Jenny Koul", "Mike Keel", "Julie Kite", "Tiny Tim", "Tiny Tim", "Tiny Tim", "Tiny Tim", "Tiny Tim"};
    private int[] images = {R.mipmap.profile_icon, R.mipmap.profile_icon2, R.mipmap.profile_icon3, R.mipmap.profile_icon4, R.mipmap.profile_icon, R.mipmap.profile_icon2, R.mipmap.profile_icon3, R.mipmap.profile_icon4, R.mipmap.profile_icon4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_direct_user_add);
        context = DirectUserAddActivity.this;
        initializedControl();
        setToolBar();
        setOnClickListener();
    }

    @Override
    public void initializedControl() {
        setCurrentTheme();
        directrecyclerView();

        binding.etDirectSearchAdd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                filter(s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    private void setCurrentTheme() {
        binding.toolbarDirectAdd.toolbarMain.setBackgroundColor(ContextCompat.getColor(context,Themes.getInstance(context).setDarkTheme()));
        binding.llMain.setBackgroundColor(ContextCompat.getColor(context,Themes.getInstance(context).setDarkTheme()));
        binding.toolbarDirectAdd.tvHeader.setTextColor(ContextCompat.getColor(context,Themes.getInstance(context).setDarkThemeText()));
        binding.toolbarDirectAdd.tvRighttext.setTextColor(ContextCompat.getColor(context,Themes.getInstance(context).setTolbarText()));
        Themes.getInstance(context).changeIconColor(context,binding.toolbarDirectAdd.ivLeft);
        binding.etDirectSearchAdd.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.etDirectSearchAdd.setBackground(ContextCompat.getDrawable(context, Themes.getInstance(context).setDarkStoryBackground()));
        binding.viewLine.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setViewLineGrey()));
    }

    public void filter(String text){

        ArrayList<DirectModel> filteredName = new ArrayList<>();

        for (DirectModel model : models){

            if (model.getName_url().toLowerCase().contains(text.toLowerCase())){
                filteredName.add(model);
            }
        }
        adapter.updatedList(filteredName);

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void directrecyclerView() {
        binding.rvDirectAdd.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvDirectAdd.setLayoutManager(layoutManager);

        //For divide the recycler item
      /*  DividerItemDecoration itemDecorator = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(context, R.drawable.divider));
        binding.rvDirectAdd.addItemDecoration(itemDecorator);
*/

        models = new ArrayList<>();
        models.addAll(prepareData());
        adapter = new DirectUserAddAdapterDemo(models, this, new OnRadioItemClickListener() {
            @Override
            public void onItemClick(View view, boolean isChecked, int position) {
                switch (view.getId()) {
                    case R.id.llDirectChat:
                    //    ActivityController.startActivity(context, ChatScreenActivity.class);
                        break;

                    default:
                        break;
                }
            }
        });
        binding.rvDirectAdd.setAdapter(adapter);
    }

    private ArrayList<DirectModel> prepareData() {
        ArrayList<DirectModel> data = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            DirectModel model = new DirectModel();
            model.setImage_url(images[i]);
            model.setName_url(names[i]);
            data.add(model);
        }
        return data;
    }

    @Override
    public void setToolBar() {
        binding.toolbarDirectAdd.ivLeft.setVisibility(View.VISIBLE);
        binding.toolbarDirectAdd.ivLeft.setImageResource(R.mipmap.back_icon);
        binding.toolbarDirectAdd.tvHeader.setVisibility(View.VISIBLE);
        binding.toolbarDirectAdd.tvHeader.setText(R.string.direct);
        binding.toolbarDirectAdd.ivRight.setVisibility(View.GONE);
        binding.toolbarDirectAdd.tvRighttext.setVisibility(View.VISIBLE);
        binding.toolbarDirectAdd.tvRighttext.setText(R.string.send);
    }

    @Override
    public void setOnClickListener() {
        binding.toolbarDirectAdd.ivLeft.setOnClickListener(this);
        binding.toolbarDirectAdd.tvRighttext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivLeft:
                onBackPressed();
                break;
            case R.id.tvRighttext:
                finish();
                break;
            default:
                break;
        }
    }
}
