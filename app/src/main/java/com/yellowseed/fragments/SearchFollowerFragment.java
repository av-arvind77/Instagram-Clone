package com.yellowseed.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yellowseed.R;
import com.yellowseed.activity.FollowerProfileActivity;
import com.yellowseed.activity.MyProfileActivity;
import com.yellowseed.adapter.BlockedContactsAdapter;
import com.yellowseed.adapter.SearchFollowingAdapter;
import com.yellowseed.databinding.FragmentSearchFollowerBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.SearchFollowingModel;
import com.yellowseed.utils.ActivityController;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseFragment;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.Themes;

import java.util.ArrayList;

import static android.databinding.DataBindingUtil.inflate;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFollowerFragment extends BaseFragment implements View.OnClickListener{
    private Context context;
    private ArrayList<SearchFollowingModel> arlModel=new ArrayList<>();
    private SearchFollowingAdapter adapter;
    private Themes themes;
    private FragmentSearchFollowerBinding binding;
    private Boolean darkThemeEnabled;
    private String[] names = {"John Thomas", "Jenny Koul", "Mike Keel", "Julie Kite", "Tiny Tim", "Julie Kite", "Tiny Tim", "Tiny Tim", "Julie Kite", "Tiny Tim", "Julie Kite", "Tiny Tim", "Tiny Tim"};
    private int[] images = {R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon3, R.mipmap.profile_icon, R.mipmap.profile_icon};



    public SearchFollowerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
        themes = new Themes(context);
        darkThemeEnabled = CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= inflate(inflater,R.layout.fragment_search_follower, container, false);
        initializedControl();

        return binding.getRoot();

    }
    private void setCurrentTheme() {
        binding.llSearchFollowers.setBackgroundColor(ContextCompat.getColor(context, themes.setDarkTheme()));
        binding.etSearchfollowers.setBackground(ContextCompat.getDrawable(context, themes.setDarkSearchDrawable()));
        binding.etSearchfollowers.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.etSearchfollowers.setHintTextColor(ContextCompat.getColor(context,themes.setDarkHintColor()));
        binding.viewLine.setBackgroundColor(ContextCompat.getColor(context, themes.setViewLineGrey()));


    }

    @Override
    public void initializedControl() {
        setCurrentTheme();

        searchFollowersRecylerView();


    }

    @Override
    public void setToolBar() {

    }

    @Override
    public void setOnClickListener() {


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



    private void searchFollowersRecylerView() {

        binding.rvSearchfollowers.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        binding.rvSearchfollowers.setLayoutManager(layoutManager);
        arlModel = new ArrayList<>();
        arlModel.addAll(prepareData());
        adapter = new SearchFollowingAdapter(arlModel, context, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.llFollowingAnother:
                        ActivityController.startActivity(context, MyProfileActivity.class);
                        break;
                    default:
                        break;
                }
            }
        });
        binding.rvSearchfollowers.setAdapter(adapter);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            default:
                break;
        }

    }
}
