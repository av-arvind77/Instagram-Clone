package com.yellowseed.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yellowseed.R;
import com.yellowseed.activity.FollowingRequestActivity;
import com.yellowseed.adapter.NotificationAdapter;
import com.yellowseed.databinding.FragmentNotificationBinding;
import com.yellowseed.fragments.FragmentHomeBottomView.notificationFragment.AllNotificationFragment;
import com.yellowseed.fragments.FragmentHomeBottomView.notificationFragment.MeNotificationFragment;
import com.yellowseed.utils.ActivityController;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseFragment;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.Themes;

public class NotificationFragment extends BaseFragment implements View.OnClickListener {
    View view;
    private Context context;
    private FragmentNotificationBinding binding;
    private ImageView ivRight;
    private Themes themes;
    private Boolean darkThemeEnabled;
    private LocalBroadcastManager lbm;


    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        themes = new Themes(context);
        darkThemeEnabled = CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME);
    }
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override

        public void onReceive( Context context, Intent intent ) {

            String data = intent.getStringExtra("key");
            setCurrentTheme();




        }

    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notification, container, false);
        view = binding.getRoot();

        lbm = LocalBroadcastManager.getInstance(getActivity());
        lbm.registerReceiver(receiver, new IntentFilter("theme_change"));
        initializedControl();
        setToolBar();
        setOnClickListener();
        return view;
    }


    @Override
    public void initializedControl() {
        ivRight = (ImageView) getActivity().findViewById(R.id.ivRight);
        viewPagerTab();
        setCurrentTheme();

    }

    private void setCurrentTheme() {
        binding.llMain.setBackgroundColor(ContextCompat.getColor(context,themes.setDarkTheme()));
        binding.tablayoutNotificationItems.setTabTextColors(ContextCompat.getColor(context, R.color.lightgrey), ContextCompat.getColor(context, themes.setYellow()));
        binding.tablayoutNotificationItems.setSelectedTabIndicatorColor(ContextCompat.getColor(context, themes.setYellow()));
        binding.tablayoutNotificationItems.setBackgroundColor(ContextCompat.getColor(context, themes.setDarkTheme()));

    }

    private void viewPagerTab() {
        setupViewPager(binding.pagerNotificationItem);
        binding.tablayoutNotificationItems.setupWithViewPager(binding.pagerNotificationItem);
    }

    private void setupViewPager(ViewPager pagerNotificationItem) {
        NotificationAdapter adapter = new NotificationAdapter(this, getChildFragmentManager());

        adapter.addFragment(new AllNotificationFragment(), "All");
        adapter.addFragment(new MeNotificationFragment(), "Me");

        pagerNotificationItem.setAdapter(adapter);

//CommonUtils.setFragmentChild(new AllNotificationFragment(),false, this,binding.pagerNotificationItem,getChildFragmentManager());
        // CommonUtils.setFragmentChild(new MeNotificationFragment(),false,this,binding.pagerNotificationItem,getChildFragmentManager());
    }

    @Override
    public void setToolBar() {
        ivRight.setImageResource(R.mipmap.user_add_icon);
    }

    @Override
    public void setOnClickListener() {
        ivRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivRight:
                ActivityController.startActivity(context, FollowingRequestActivity.class);
                break;
            default:
                break;
        }
    }
    @Override
    public void onDestroy() {
        lbm.unregisterReceiver(receiver);
        super.onDestroy();
    }
}
