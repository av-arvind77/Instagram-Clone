package com.yellowseed.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yellowseed.R;
import com.yellowseed.adapter.NewChatAdapter;
import com.yellowseed.adapter.SearchFollowingAdapter;
import com.yellowseed.databinding.ActivityNewChatBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.SearchFollowingModel;
import com.yellowseed.utils.ActivityController;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;

import java.util.ArrayList;

public class NewChatActivity extends BaseActivity {
    private ArrayList<SearchFollowingModel> arlModel = new ArrayList<>();
    private NewChatAdapter adapter;
    private ActivityNewChatBinding binding;
    private Context mContext;
    private Themes themes;
    private Boolean darkThemeEnabled;
    private String[] names = {"John Thomas", "Jenny Koul", "Mike Keel", "Julie Kite", "Tiny Tim", "Julie Kite", "Tiny Tim", "Tiny Tim", "Julie Kite", "Tiny Tim", "Julie Kite", "Tiny Tim", "Tiny Tim"};
    private int[] images = {R.mipmap.profile_icon3, R.mipmap.profile_icon3, R.mipmap.profile_icon3, R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon3, R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon3, R.mipmap.profile_icon3, R.mipmap.profile_icon};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=DataBindingUtil.setContentView(this,R.layout.activity_new_chat);
        mContext = NewChatActivity.this;
        darkThemeEnabled = CommonUtils.getPreferencesBoolean(mContext, AppConstants.DARK_THEME);
        themes = new Themes(mContext);
        initializedControl();
        setToolBar();
        setOnClickListener();
    }

    @Override
    public void initializedControl() {
        setCurrentTheme();
        searchFollowingRecylerView();

    }
    private void setCurrentTheme() {
        binding.llSearchFollowing.setBackgroundColor(ContextCompat.getColor(mContext, themes.setDarkTheme()));
        binding.layoutsearchfollowing.toolbarMain.setBackgroundColor(ContextCompat.getColor(mContext, themes.setDarkTheme()));
        binding.layoutsearchfollowing.tvRighttext.setTextColor(ContextCompat.getColor(mContext, themes.setTolbarText()));
        binding.layoutsearchfollowing.tvHeader.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
        binding.etSearchFollowing.setBackground(ContextCompat.getDrawable(mContext, themes.setDarkSearchDrawable()));
        binding.etSearchFollowing.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
        binding.etSearchFollowing.setHintTextColor(ContextCompat.getColor(mContext,themes.setDarkHintColor()));
        binding.viewLine.setBackgroundColor(ContextCompat.getColor(mContext, themes.setViewLineGrey()));
        Themes.getInstance(mContext).changeIconColor(mContext,binding.layoutsearchfollowing.ivLeft);

    }

    private void searchFollowingRecylerView() {

        binding.rvNewChat.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvNewChat.setLayoutManager(layoutManager);
        arlModel = new ArrayList<>();
        arlModel.addAll(prepareData());
        adapter = new NewChatAdapter(arlModel, mContext, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.llFollowingAnother:
                        ToastUtils.showToastShort(mContext,"Work in Progress");
                        break;
                    default:
                        break;
                }
            }
        });
        binding.rvNewChat.setAdapter(adapter);
    }

    private ArrayList<SearchFollowingModel> prepareData() {
        ArrayList<SearchFollowingModel> data = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            SearchFollowingModel model = new SearchFollowingModel();
            model.setName_url(names[i]);
            model.setImage_url(images[i]);
            data.add(model);
        }
        return data;
    }

    public void filter(String text){

        ArrayList<SearchFollowingModel> filteredName = new ArrayList<>();

        for (SearchFollowingModel model : arlModel){

            if (model.getName_url().toLowerCase().contains(text.toLowerCase())){
                filteredName.add(model);
            }
        }
        adapter.updatedList(filteredName);
    }

    @Override
    public void setToolBar() {
        binding.layoutsearchfollowing.ivRight.setVisibility(View.GONE);
        binding.layoutsearchfollowing.ivLeft.setVisibility(View.VISIBLE);
        binding.layoutsearchfollowing.ivLeft.setImageResource(R.mipmap.back_icon);
        binding.layoutsearchfollowing.tvHeader.setVisibility(View.VISIBLE);
        binding.layoutsearchfollowing.tvHeader.setText(R.string.following);
    }

    @Override
    public void setOnClickListener() {
        binding.layoutsearchfollowing.ivLeft.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivLeft:
                finish();
                break;
            default:
                break;
        }
    }
}
