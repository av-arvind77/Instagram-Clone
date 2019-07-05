package com.yellowseed.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yellowseed.R;
import com.yellowseed.adapter.ThemesAdapter;
import com.yellowseed.databinding.ActivityThemeSelectionBinding;
import com.yellowseed.model.AvtarImageModel;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;

import java.util.ArrayList;

public class ThemeSelectionActivity extends BaseActivity {
    private ActivityThemeSelectionBinding binding;
    private Context context;
    private int[] themes = {R.mipmap.theme_one, R.mipmap.theme_one, R.mipmap.theme_one, R.mipmap.theme_one, R.mipmap.theme_one, R.mipmap.theme_one, R.mipmap.theme_one, R.mipmap.theme_one, R.mipmap.theme_one};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = ThemeSelectionActivity.this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_theme_selection);
        initializedControl();
        setOnClickListener();
        setToolBar();
    }

    @Override
    public void initializedControl() {
        setCurrentTheme();
        initViews();
    }

    private void setCurrentTheme() {
        binding.viewLine.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setViewLineGrey()));

    }

    private void initViews() {
        binding.themeHolder.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context,3);
        binding.themeHolder.setLayoutManager(layoutManager);

        ArrayList<AvtarImageModel> images = getData();
        ThemesAdapter adapter = new ThemesAdapter(images, context);
        binding.themeHolder.setAdapter(adapter);
    }

    private ArrayList<AvtarImageModel> getData() {
        ArrayList<AvtarImageModel> data = new ArrayList<>();
        for (int i = 0; i < themes.length; i++) {
            AvtarImageModel item = new AvtarImageModel();
            item.setImage(themes[i]);
            data.add(item);
        }
        return data;
    }

    @Override
    public void setToolBar() {
        binding.toolbarThemeSelection.ivLeft.setVisibility(View.VISIBLE);
        binding.toolbarThemeSelection.tvHeader.setVisibility(View.VISIBLE);
        binding.toolbarThemeSelection.tvHeader.setText(R.string.themes);
        binding.toolbarThemeSelection.tvRighttext.setVisibility(View.VISIBLE);
        binding.toolbarThemeSelection.tvRighttext.setText(R.string.set);
    }

    @Override
    public void setOnClickListener() {
        binding.toolbarThemeSelection.ivLeft.setOnClickListener(this);
        binding.toolbarThemeSelection.tvRighttext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivLeft:
                finish();
                break;
            case R.id.tvRighttext:
                finish();
                ToastUtils.showToastShort(context,"Background Changed!");
                break;
            default:
                break;
        }
    }
}
