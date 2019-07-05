package com.yellowseed.drawer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.yellowseed.R;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.CommonUtils;

import de.greenrobot.event.EventBus;

/**
 * Created by Pushpender Singh on 1/27/2016.
 */
@SuppressWarnings("ALL")
public class FragmentDrawer extends Fragment {

    private static String TAG = FragmentDrawer.class.getSimpleName();

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private View containerView;
    private FragmentDrawerListener drawerListener;

    public FragmentDrawer() {

    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.drawers_header, container, false);

        return layout;
    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        if(CommonUtils.getPreferencesBoolean(getActivity(), AppConstants.DARK_THEME)){
            containerView.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.black));
        }else {
            containerView.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        }
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                EventBus.getDefault().post(new CloseKeyPad());
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                //toolbar.setAlpha(1 - slideOffset / 2);
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }

    public void setDrakmode(boolean b) {
        if(b){
            containerView.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.black));
        }else {
            containerView.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.white));
        }
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }


    public interface FragmentDrawerListener {
        void onDrawerItemSelected(View view, int position);
    }
}
