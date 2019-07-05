package com.yellowseed.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.yellowseed.R;
import com.yellowseed.adapter.CallhistoryViewPAgerAdapter;
import com.yellowseed.databinding.ActivityVotesBinding;
import com.yellowseed.fragments.VotesFragment;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.Themes;

public class VotesActivity extends BaseActivity {
    private Context context;
    private ActivityVotesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_votes);
        context = VotesActivity.this;
        initializedControl();
    }

    @Override
    public void initializedControl() {
        setCurrentTheme();
        setToolBar();
        setupViewPager(binding.vpVotes);
        setOnClickListener();

    }

    @Override
    public void setToolBar()
    {
        binding.toolbarVotes.ivLeft.setVisibility(View.VISIBLE);
        binding.toolbarVotes.tvHeader.setVisibility(View.VISIBLE);
        binding.toolbarVotes.tvHeader.setText("Votes");

        binding.toolbarVotes.ivRight.setVisibility(View.GONE);
        binding.toolbarVotes.ivRightSecond.setVisibility(View.GONE);

    }

    private void setupViewPager(ViewPager viewPager) {
        CallhistoryViewPAgerAdapter adapter = new CallhistoryViewPAgerAdapter(getSupportFragmentManager());

        adapter.addFragment(new VotesFragment(), "");
        adapter.addFragment(new VotesFragment(), "");
        adapter.addFragment(new VotesFragment(), "");
        adapter.addFragment(new VotesFragment(), "");

        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                onSwipe(position);

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }


    public void setCurrentTheme() {
        binding.toolbarVotes.toolbarMain.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        binding.toolbarVotes.tvHeader.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.llVotes.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        Themes.getInstance(context).changeIconColor(context, binding.toolbarVotes.ivLeft);
        binding.tvCountry1.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setYellow()));
        binding.tvVotes1.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setYellow()));
        binding.view1.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setYellow()));


    }

    private void onSwipe(int position) {
        switch (position) {
            case 0:
                binding.tvCountry1.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setYellow()));
                binding.tvVotes1.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setYellow()));
                binding.tvCountry2.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setVotesGrey()));
                binding.tvVotes2.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setVotesGrey()));
                binding.tvCountry3.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setVotesGrey()));
                binding.tvVotes3.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setVotesGrey()));
                binding.tvCountry4.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setVotesGrey()));
                binding.tvVotes4.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setVotesGrey()));
                binding.view1.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setYellow()));


                binding.view2.setVisibility(View.GONE);
                binding.view4.setVisibility(View.GONE);
                binding.view3.setVisibility(View.GONE);
                binding.view1.setVisibility(View.VISIBLE);
                break;
            case 1:

                binding.tvCountry2.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setYellow()));
                binding.tvVotes2.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setYellow()));
                binding.tvCountry1.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setVotesGrey()));
                binding.tvVotes1.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setVotesGrey()));
                binding.tvCountry3.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setVotesGrey()));
                binding.tvVotes3.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setVotesGrey()));
                binding.tvCountry4.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setVotesGrey()));
                binding.tvVotes4.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setVotesGrey()));
                binding.view2.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setYellow()));


                binding.view1.setVisibility(View.GONE);
                binding.view4.setVisibility(View.GONE);
                binding.view3.setVisibility(View.GONE);
                binding.view2.setVisibility(View.VISIBLE);
                break;
            case 2:

                binding.tvVotes3.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setYellow()));
                binding.tvCountry3.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setYellow()));
                binding.tvCountry2.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setVotesGrey()));
                binding.tvVotes2.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setVotesGrey()));
                binding.tvCountry1.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setVotesGrey()));
                binding.tvVotes1.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setVotesGrey()));
                binding.tvCountry4.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setVotesGrey()));
                binding.tvVotes4.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setVotesGrey()));
                binding.view3.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setYellow()));


                binding.view2.setVisibility(View.GONE);
                binding.view4.setVisibility(View.GONE);
                binding.view1.setVisibility(View.GONE);
                binding.view3.setVisibility(View.VISIBLE);
                break;
            case 3:

                binding.tvCountry4.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setYellow()));
                binding.tvVotes4.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setYellow()));

                binding.tvCountry2.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setVotesGrey()));
                binding.tvVotes2.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setVotesGrey()));
                binding.tvCountry3.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setVotesGrey()));
                binding.tvVotes3.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setVotesGrey()));
                binding.tvCountry1.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setVotesGrey()));
                binding.tvVotes1.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setVotesGrey()));
                binding.view4.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setYellow()));


                binding.view2.setVisibility(View.GONE);
                binding.view3.setVisibility(View.GONE);
                binding.view1.setVisibility(View.GONE);
                binding.view4.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }


    }

    @Override
    public void setOnClickListener() {
        binding.ll1.setOnClickListener(this);
        binding.ll2.setOnClickListener(this);
        binding.ll3.setOnClickListener(this);
        binding.ll4.setOnClickListener(this);
        binding.toolbarVotes.ivLeft.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivLeft:
                onBackPressed();
                break;
            case R.id.ll1:
                binding.tvCountry1.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setYellow()));
                binding.tvVotes1.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setYellow()));
                binding.tvCountry2.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setVotesGrey()));
                binding.tvVotes2.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setVotesGrey()));
                binding.tvCountry3.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setVotesGrey()));
                binding.tvVotes3.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setVotesGrey()));
                binding.tvCountry4.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setVotesGrey()));
                binding.tvVotes4.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setVotesGrey()));
                binding.view1.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setYellow()));
                binding.vpVotes.setCurrentItem(0);
                break;
            case R.id.ll2:
                binding.tvCountry2.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setYellow()));
                binding.tvVotes2.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setYellow()));
                binding.tvCountry1.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setVotesGrey()));
                binding.tvVotes1.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setVotesGrey()));
                binding.tvCountry3.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setVotesGrey()));
                binding.tvVotes3.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setVotesGrey()));
                binding.tvCountry4.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setVotesGrey()));
                binding.tvVotes4.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setVotesGrey()));
                binding.view2.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setYellow()));

                binding.vpVotes.setCurrentItem(1);
                break;
            case R.id.ll3: binding.tvCountry1.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setYellow()));
                binding.tvVotes3.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setYellow()));
                binding.tvCountry3.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setYellow()));

                binding.tvCountry2.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setVotesGrey()));
                binding.tvVotes2.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setVotesGrey()));
                binding.tvCountry1.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setVotesGrey()));
                binding.tvVotes1.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setVotesGrey()));
                binding.tvCountry4.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setVotesGrey()));
                binding.tvVotes4.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setVotesGrey()));
                binding.view3.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setYellow()));


                binding.vpVotes.setCurrentItem(2);
                break;
            case R.id.ll4:
                binding.tvCountry4.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setYellow()));
                binding.tvVotes4.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setYellow()));

                binding.tvCountry2.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setVotesGrey()));
                binding.tvVotes2.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setVotesGrey()));
                binding.tvCountry3.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setVotesGrey()));
                binding.tvVotes3.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setVotesGrey()));
                binding.tvCountry1.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setVotesGrey()));
                binding.tvVotes1.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setVotesGrey()));
                binding.view4.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setYellow()));


                binding.vpVotes.setCurrentItem(3);
                break;
            default:
                break;
        }

    }
}
