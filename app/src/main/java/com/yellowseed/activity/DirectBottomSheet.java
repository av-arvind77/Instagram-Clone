package com.yellowseed.activity;

import android.databinding.DataBindingUtil;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yellowseed.R;
import com.yellowseed.databinding.ActivityDirectBottomSheetBinding;

public class DirectBottomSheet extends AppCompatActivity {
    private ActivityDirectBottomSheetBinding binding;
    BottomSheetDialog bottomSheetDialog;
    BottomSheetBehavior bottomSheetBehavior ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding=DataBindingUtil.setContentView(this,R.layout.activity_direct_bottom_sheet);

    }
}
