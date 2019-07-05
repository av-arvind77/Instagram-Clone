package com.yellowseed.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yellowseed.R;
import com.yellowseed.adapter.LinksAdapter;
import com.yellowseed.databinding.FragmentLinksBinding;
import com.yellowseed.model.LinksModel;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseFragment;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.Themes;

import java.util.ArrayList;


public class LinksFragment extends BaseFragment {
    private Context context;
    private FragmentLinksBinding binding;
    private Themes themes;
    private boolean darkThemeEnabled;
    private ArrayList<LinksModel> models;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        themes = new Themes(context);
        darkThemeEnabled = CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME);
    }

    public LinksFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_links,container,false);
        View view = binding.getRoot();
        initializedControl();
        setToolBar();
        setOnClickListener();
        return view;
    }

    @Override
    public void initializedControl() {
        setCurrentTheme();
        binding.rvLinksGroup.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        binding.rvLinksGroup.setLayoutManager(layoutManager);
        models = new ArrayList<>();
        LinksAdapter adapter = new LinksAdapter(models,context);
        binding.rvLinksGroup.setAdapter(adapter);

    }

    private void setCurrentTheme() {
        binding.llMain.setBackgroundColor(ContextCompat.getColor(context,themes.setDarkTheme()));
    }


    @Override
    public void setToolBar() {

    }

    @Override
    public void setOnClickListener() {

    }
}
