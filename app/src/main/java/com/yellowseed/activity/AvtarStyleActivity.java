package com.yellowseed.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.yellowseed.R;
import com.yellowseed.databinding.ActivityAvtarStyleBinding;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.Themes;

public class AvtarStyleActivity  extends BaseActivity {
    private ActivityAvtarStyleBinding binding;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding= DataBindingUtil.setContentView(this,R.layout.activity_avtar_style);
       context=AvtarStyleActivity.this;
    }

    @Override
    public void initializedControl() {
        setCurrentTheme();

    }
    private void setCurrentTheme() {
        binding.llAvtarStyle.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        binding.bottomNavigationAvtarStyle.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
   }

    @Override
    public void setToolBar() {

    }

    @Override
    public void setOnClickListener() {

    }

    @Override
    public void onClick(View v) {

    }
}
