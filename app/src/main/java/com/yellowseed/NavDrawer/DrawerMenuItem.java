package com.yellowseed.NavDrawer;

import android.content.Context;
import android.widget.TextView;

import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.yellowseed.R;

/**
 * Created by mobiloitte_india on 7/4/18.
 */

@Layout(R.layout.drawers_items)
public class DrawerMenuItem {

    public static final int DRAWER_MENU_ITEM_Profile = 1;
    public static final int DRAWER_MENU_ITEM_Settings = 2;
    public static final int DRAWER_MENU_ITEM_TermsAndConditions = 3;
    public static final int DRAWER_MENU_ITEM_PrivacyPolicy = 4;
    public static final int DRAWER_MENU_ITEM_AboutUs = 5;
    public static final int DRAWER_MENU_ITEM_SwitchAccount = 6;
    public static final int DRAWER_MENU_ITEM_LogOut = 7;
    public static final int DRAWER_MENU_ITEM_NightMode = 8;
    @View(R.id.tvDrawerItem)
    public TextView itemNameTxt;
    private int mMenuPosition;
    private Context mContext;
    private boolean toggleChecked = true;
    private DrawerCallBack mCallBack;
    @View(R.id.tvView)
    private TextView tvView;

    public DrawerMenuItem(Context context, int menuPosition, DrawerCallBack mCallBacks) {
        mContext = context;
        mMenuPosition = menuPosition;
        mCallBack = mCallBacks;
    }

    @Resolve
    private void onResolved() {
        switch (mMenuPosition) {
            case DRAWER_MENU_ITEM_Profile:
                itemNameTxt.setText(mContext.getString(R.string.profile));
                itemNameTxt.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.userprofile_icon, 0, 0, 0);
                break;

            case DRAWER_MENU_ITEM_Settings:
                itemNameTxt.setText(R.string.settings);
                itemNameTxt.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.setting_menu_icon, 0, 0, 0);
                tvView.setVisibility(android.view.View.VISIBLE);
                break;

            case DRAWER_MENU_ITEM_TermsAndConditions:
                itemNameTxt.setText(R.string.termsandconditions);
                itemNameTxt.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.terms_icon, 0, 0, 0);
                break;

            case DRAWER_MENU_ITEM_PrivacyPolicy:
                itemNameTxt.setText(R.string.privacypolicy);
                itemNameTxt.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.privacy_policy_icon, 0, 0, 0);
                break;

            case DRAWER_MENU_ITEM_AboutUs:
                itemNameTxt.setText(R.string.aboutus);
                itemNameTxt.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.info_icon, 0, 0, 0);


                break;
            case DRAWER_MENU_ITEM_SwitchAccount:
                itemNameTxt.setText(R.string.switchaccount);
                itemNameTxt.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.switch_account, 0, 0, 0);
                break;

            case DRAWER_MENU_ITEM_LogOut:
                itemNameTxt.setText(R.string.logout);
                itemNameTxt.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.logout_icon1, 0, 0, 0);
                break;

            case DRAWER_MENU_ITEM_NightMode:
                itemNameTxt.setText("Night Mode");
                itemNameTxt.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.logout_icon1, 0, R.mipmap.toggle_d, 0);
                break;
            default:
                break;
        }
    }

    @Click(R.id.ll_Draweritem)
    private void onMenuItemClick() {
        switch (mMenuPosition) {
            case DRAWER_MENU_ITEM_Profile:
                if (mCallBack != null) mCallBack.onProfileSelected();
                break;
            case DRAWER_MENU_ITEM_Settings:
                if (mCallBack != null) mCallBack.onSettingsSelected();
                break;
            case DRAWER_MENU_ITEM_TermsAndConditions:
                if (mCallBack != null) mCallBack.onTermsAndConditionsSelected();
                break;
            case DRAWER_MENU_ITEM_PrivacyPolicy:
                if (mCallBack != null) mCallBack.onPrivacyPolicySelected();
                break;
            case DRAWER_MENU_ITEM_AboutUs:
                if (mCallBack != null) mCallBack.onAboutUsSelected();
                break;
            case DRAWER_MENU_ITEM_SwitchAccount:
                if (mCallBack != null) mCallBack.onSwitchAccountSelected();
                break;
            case DRAWER_MENU_ITEM_LogOut:
                if (mCallBack != null) mCallBack.onLogoutSelected();
                break;
            case DRAWER_MENU_ITEM_NightMode:
                if (mCallBack != null) mCallBack.onNightModeSelected();
                break;
            default:
                break;

        }

    }

    public void setDrawerCallBack(DrawerCallBack callBack) {
        mCallBack = callBack;
    }

    public interface DrawerCallBack {


        void onProfileSelected();

        void onSettingsSelected();

        void onTermsAndConditionsSelected();

        void onPrivacyPolicySelected();

        void onAboutUsSelected();

        void onSwitchAccountSelected();

        void onLogoutSelected();

        void onNightModeSelected();
    }
}
