package com.yellowseed.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yellowseed.R;
import com.yellowseed.adapter.DocumentsGroupAdapter;
import com.yellowseed.adapter.NotificationLayoutAdapter;
import com.yellowseed.databinding.FragmentAllNotificationBinding;
import com.yellowseed.databinding.FragmentDocumentsBinding;
import com.yellowseed.model.DocumentsModel;
import com.yellowseed.model.NotificationLayoutModel;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseFragment;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.Themes;

import java.util.ArrayList;


public class DocumentsFragment extends BaseFragment {
    public Context context;
    private FragmentDocumentsBinding binding;
    private ArrayList<DocumentsModel> models=new ArrayList<>();
    private String[] day = {"Yesterday"};
    private Themes themes;
    private boolean darkThemeEnabled;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public DocumentsFragment() {
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_documents, container, false);
        View view = binding.getRoot();
        themes = new Themes(context);
        darkThemeEnabled = CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME);
        initializedControl();
        setToolBar();
        setOnClickListener();
        return view;
    }

    @Override
    public void initializedControl() {
        setCurrentTheme();
        binding.rvDocumentsGroup.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        binding.rvDocumentsGroup.setLayoutManager(layoutManager);

        DocumentsGroupAdapter documentsGroupAdapter = new DocumentsGroupAdapter(models,context);
        binding.rvDocumentsGroup.setAdapter(documentsGroupAdapter);
    }

    private void setCurrentTheme() {
        binding.llMain.setBackgroundColor(ContextCompat.getColor(context,themes.setDarkTheme()));
    }

    private ArrayList<DocumentsModel> prepareData() {
        ArrayList<DocumentsModel> documentsModels = new ArrayList<>();
        for (int i = 0; i < day.length; i++) {
            DocumentsModel documentsModel = new DocumentsModel();
            documentsModel.setDayName(day[i]);
            models.add(documentsModel);
        }
        return models;
    }
    @Override
    public void setToolBar() {

    }

    @Override
    public void setOnClickListener() {

    }
}
