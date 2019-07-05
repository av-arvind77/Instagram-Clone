package com.yellowseed.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yellowseed.R;
import com.yellowseed.adapter.SolidColorsAdapter;
import com.yellowseed.database.RoomsBackgroundTable;
import com.yellowseed.database.RoomsTable;
import com.yellowseed.database.WallpaperModel;
import com.yellowseed.databinding.ActivitySolidColorSelectionBinding;
import com.yellowseed.model.AvtarImageModel;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;

import java.util.ArrayList;

public class SolidColorSelectionActivity extends BaseActivity {
    private ActivitySolidColorSelectionBinding binding;
    private Context context;
    private SolidColorsAdapter adapter;
    private String roomId="",type="";
    private Themes themes;
    private boolean darkThemeEnabled;
    private int[] colors = {R.mipmap.yellow, R.mipmap.pink, R.mipmap.yellow, R.mipmap.yellow, R.mipmap.pink, R.mipmap.yellow, R.mipmap.yellow, R.mipmap.pink, R.mipmap.yellow};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_solid_color_selection);
        context = SolidColorSelectionActivity.this;
        themes = new Themes(context);
        darkThemeEnabled = CommonUtils.getPreferencesBoolean(context,AppConstants.DARK_THEME);
        initializedControl();
        setOnClickListener();
        setToolBar();
        roomId=getIntent().getStringExtra(AppConstants.ROOM_ID);
        type=getIntent().getStringExtra(AppConstants.FROM);
    }

    @Override
    public void initializedControl() {
        setCurrentTheme();
        initViews();
    }



    public void setCurrentTheme(){
        binding.llMain.setBackgroundColor(ContextCompat.getColor(context, themes.setDarkTheme()));
        binding.toolbarSolidColorSelection.toolbarMain.setBackgroundColor(ContextCompat.getColor(context, themes.setDarkTheme()));
        binding.toolbarSolidColorSelection.tvHeader.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.toolbarSolidColorSelection.tvRighttext.setTextColor(ContextCompat.getColor(context, themes.setTolbarText()));
        themes.changeIconColor(context, binding.toolbarSolidColorSelection.ivLeft);

    }

    private void initViews() {
        binding.solidColorHolder.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context,3);
        binding.solidColorHolder.setLayoutManager(layoutManager);
        ArrayList<String> images = getData();
         adapter = new SolidColorsAdapter(images,context);
        binding.solidColorHolder.setAdapter(adapter);





    }


    private ArrayList<String> getData() {
        ArrayList<String> data = new ArrayList<>();
        data.add("#0664A6");
        data.add("#C9C9C9");
        data.add("#70696b");
        data.add("#da3be6");
        data.add("#0664A6");
        data.add("#C9C9C9");
        data.add("#727272");
        data.add("#43a047");
        data.add("#6043a047");
        data.add("#5043a047");
        return data;
    }

    @Override
    public void setToolBar() {
        binding.toolbarSolidColorSelection.ivLeft.setVisibility(View.VISIBLE);
        binding.toolbarSolidColorSelection.tvHeader.setVisibility(View.VISIBLE);
        binding.toolbarSolidColorSelection.tvHeader.setText(R.string.solid_color);
        binding.toolbarSolidColorSelection.tvRighttext.setVisibility(View.VISIBLE);
        binding.toolbarSolidColorSelection.tvRighttext.setText(R.string.set);
    }

    @Override
    public void setOnClickListener() {
        binding.toolbarSolidColorSelection.ivLeft.setOnClickListener(this);
        binding.toolbarSolidColorSelection.tvRighttext.setOnClickListener(this);
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

              /*  if(type!=null&&type.length()>0){
                    switch (type){*/
                       /* case "single":
                            if(adapter.getSelected()!=null&&adapter.getSelected().length()>0) {
                                WallpaperModel wallpaperModel = new WallpaperModel();
                                wallpaperModel.setType("color");
                                wallpaperModel.setImage("");
                                wallpaperModel.setColor_code(adapter.getSelected());
                                ToastUtils.showToastShort(context, "Background Changed!");
                                RoomsBackgroundTable table=new RoomsBackgroundTable(context);
                                if(!table.isExist(roomId)) {
                                    table.saveBackground(wallpaperModel, roomId);
                                }else {
                                    table.updateBackGround(wallpaperModel, roomId);
                                }
                                finish();
                            }
                            break;
                        case "multiple":

                            if(adapter.getSelected()!=null&&adapter.getSelected().length()>0) {

                                RoomsTable roomsTable=new RoomsTable(context);
                                roomsTable.getAllRooms(context);
                                if(roomsTable.getAllRooms(context)!=null&&roomsTable.getAllRooms(context).size()>0){
                                    RoomsBackgroundTable table=new RoomsBackgroundTable(context);
                                    for (int i = 0; i < roomsTable.getAllRooms(context).size(); i++) {
                                        WallpaperModel wallpaperModel = new WallpaperModel();
                                        wallpaperModel.setType("color");
                                        wallpaperModel.setImage("");
                                        wallpaperModel.setColor_code(adapter.getSelected());
                                        ToastUtils.showToastShort(context, "Background Changed!");
                                        if(!table.isExist(roomId)) {
                                            table.saveBackground(wallpaperModel, roomsTable.getAllRooms(context).get(i).getRoom_id());
                                        }else {
                                            table.updateBackGround(wallpaperModel, roomsTable.getAllRooms(context).get(i).getRoom_id());
                                        }

                                    }
                                    finish();
                                    table.closeDB();
                                }
                                roomsTable.closeDB();
                               *//* *//*
                            }*/

                            break;



            default:
                break;
        }
    }
}
