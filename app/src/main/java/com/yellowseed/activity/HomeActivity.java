package com.yellowseed.activity;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quickblox.core.exception.QBResponseException;
import com.quickblox.sample.core.ui.dialog.ProgressDialogFragment;
import com.quickblox.sample.core.utils.SharedPrefsHelper;
import com.quickblox.sample.video.VideoApp;
import com.quickblox.sample.video.activities.CallActivity;
import com.quickblox.sample.video.activities.PermissionsActivity;
import com.quickblox.sample.video.services.CallService;
import com.quickblox.sample.video.util.OnNewSessionListener;
import com.quickblox.sample.video.util.QBResRequestExecutor;
import com.quickblox.sample.video.utils.Consts;
import com.quickblox.sample.video.utils.PermissionsChecker;
import com.quickblox.sample.video.utils.QBEntityCallbackImpl;
import com.quickblox.sample.video.utils.WebRtcSessionManager;
import com.quickblox.users.model.QBUser;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.yellowseed.R;
import com.yellowseed.adapter.SavedUserAdapter;
import com.yellowseed.database.UserTable;
import com.yellowseed.databinding.ActivityHomeBinding;
import com.yellowseed.databinding.LayoutUserPostBinding;
import com.yellowseed.databinding.LayoutswitchaccountBinding;
import com.yellowseed.drawer.FragmentDrawer;
import com.yellowseed.fragments.AnonymousFragment;
import com.yellowseed.fragments.ChatsFragment;
import com.yellowseed.fragments.ExploreBottomFragment;
import com.yellowseed.fragments.HomeBottomFragment;
import com.yellowseed.fragments.NotificationFragment;
import com.yellowseed.imageUtils.CircleTransformation;
import com.yellowseed.listener.ApiCallback;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.listener.OnItemTouchHelper;
import com.yellowseed.model.reqModel.DeviceModel;
import com.yellowseed.model.reqModel.UserModel;
import com.yellowseed.model.resModel.GetChatResonse;
import com.yellowseed.utils.ActivityController;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.PrefManager;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;
import com.yellowseed.webservices.ApiRequest;
import com.yellowseed.webservices.ApiResponse;
import com.yellowseed.webservices.CommonApi;

import java.io.Serializable;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class HomeActivity extends BaseActivity implements FragmentDrawer.FragmentDrawerListener, View.OnClickListener, OnItemClickListener {
    public ActionBarDrawerToggle drawerToggle;
    public Toolbar mToolbar;
    public OnItemTouchHelper mListener;
    protected QBResRequestExecutor requestExecutor;
    private Themes themes;
    private Context mContext;
    private ActivityHomeBinding binding;
    private long DRAWER_CLOSE_TIME = 170;
    private UserModel userData = new UserModel();
    private LayoutUserPostBinding layoutUserPostBinding;
    private MenuItem menuItem;
    private boolean isRunForCall;
    private WebRtcSessionManager webRtcSessionManager;
    private PermissionsChecker checker;
    private boolean darkThemeEnabled;
    private FragmentDrawer drawerFragment;
    private LinearLayout llProfile, llSetting, llFollowersH, llFollowingH, llTerms, llPrivacy, llAbout, llSwitch, llLogout, llNightMode, llDrawerHeader, llMain;
    private TextView tvUserNameH, tvUserEmail, tvUserEmailH, tv_NumberFollowing, tv_NumberFollowers, tvFollowers, tvFollowing, tvProfile, tvSettings, tvTerms, tvPrivacy, tvAbout,
            tvSwitchAccount, tvLogout, tvNightMode, tvUserImage;
    private ImageView ivUser1, ivUserImageH, ivUserImageH1, ivLeftImage, ivUser, ivProfile, ivSettings, ivTerms, ivPrivacyPolicy, ivAbout, ivSwitch, ivLogout, ivNightMode;
    private CheckBox cbNight;
    private View viewDrawerView, viewDrawerBottom;
    private Fragment mFragment;
    private TextView tvUserImageDrawer;
    public static void start(Context context, boolean isRunForCall) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra(Consts.EXTRA_IS_STARTED_FOR_CALL, isRunForCall);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        mContext = HomeActivity.this;
        themes = new Themes(mContext);
        // checker = new PermissionsChecker(getApplicationContext());
        darkThemeEnabled = CommonUtils.getPreferencesBoolean(mContext, AppConstants.DARK_THEME);
        //   requestExecutor = VideoApp.getInstance().getQbResRequestExecutor();
        getIds();
        cbNight.setChecked(darkThemeEnabled);
        setSupportActionBar(binding.layoutHomeActivity.toolbarMain);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        binding.layoutHomeActivity.toolbarMain.setNavigationIcon(null);
        initializedControl();
        inflateMenuBottom();
        //     CommonUtils.savePreferencesBoolean(mContext, AppConstants.DARK_THEME, false);
        setToolBar();
        setOnClickListener();
        getIntentValue();
        cbNight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cbNight.isChecked()) {
                    drawerFragment.setDrakmode(true);
                    ToastUtils.showToastShort(mContext, "Night mode enabled.");
                    CommonUtils.savePreferencesBoolean(mContext, AppConstants.DARK_THEME, true);
                    inflateMenuBottom();
                    setCurrentTheme();
                    Intent intent = new Intent("theme_change");
                    intent.putExtra("key", true);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                    Window window = HomeActivity.this.getWindow();
// clear FLAG_TRANSLUCENT_STATUS flag:
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
// finally change the color
                    window.setStatusBarColor(HomeActivity.this.getResources().getColor(R.color.darkgrey));
                } else {
                    drawerFragment.setDrakmode(false);
                    CommonUtils.savePreferencesBoolean(mContext, AppConstants.DARK_THEME, false);
                    ToastUtils.showToastShort(mContext, "Night mode disabled.");
                    inflateMenuBottom();
                    setCurrentTheme();
                   /* Intent intent = new Intent();
                    intent.setAction("THEMEBROADCAST");
                    sendBroadcast(intent);*/
                    Intent intent = new Intent("theme_change");
                    intent.putExtra("key", "My Data");
                    // put your all data using put extra
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                    Window window = HomeActivity.this.getWindow();
// clear FLAG_TRANSLUCENT_STATUS flag:
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
// finally change the color
                    window.setStatusBarColor(HomeActivity.this.getResources().getColor(R.color.lightgrey));
                }
            }
        });
    }
    public void inflateMenuBottom() {
    /*    if (CommonUtils.getPreferencesBoolean(mContext, AppConstants.DARK_THEME)) {
            binding.bottomNavigationHomeBottom.getMenu().removeItem(R.id.action_Home);
            binding.bottomNavigationHomeBottom.getMenu().removeItem(R.id.action_Search);
            binding.bottomNavigationHomeBottom.getMenu().removeItem(R.id.action_Message);
            binding.bottomNavigationHomeBottom.getMenu().removeItem(R.id.action_Chat);
            binding.bottomNavigationHomeBottom.getMenu().removeItem(R.id.action_DoubleMessage);
            binding.bottomNavigationHomeBottom.inflateMenu(R.menu.bottom_navigation_home_dark);
            BottomNavigationViewHelper.removeShiftMode(binding.bottomNavigationHomeBottom);
        } else {*/
        binding.bottomNavigationHomeBottom.getMenu().removeItem(R.id.action_Home);
        binding.bottomNavigationHomeBottom.getMenu().removeItem(R.id.action_Search);
        binding.bottomNavigationHomeBottom.getMenu().removeItem(R.id.action_Message);
        binding.bottomNavigationHomeBottom.getMenu().removeItem(R.id.action_Chat);
        binding.bottomNavigationHomeBottom.getMenu().removeItem(R.id.action_DoubleMessage);
        binding.bottomNavigationHomeBottom.inflateMenu(R.menu.bottomnavigation_home);
        BottomNavigationViewHelper.removeShiftMode(binding.bottomNavigationHomeBottom);
    }
    private void getIds() {
        llFollowersH = (LinearLayout) findViewById(R.id.llFollowersH);
        llFollowingH = (LinearLayout) findViewById(R.id.llFollowingH);
        llSetting = (LinearLayout) findViewById(R.id.llSettings);
        llSetting = (LinearLayout) findViewById(R.id.llSettings);
        llSetting = (LinearLayout) findViewById(R.id.llSettings);
        llProfile = (LinearLayout) findViewById(R.id.llProfile);
        llSetting = (LinearLayout) findViewById(R.id.llSettings);
        llTerms = (LinearLayout) findViewById(R.id.llTerms);
        llPrivacy = (LinearLayout) findViewById(R.id.llPrivacyPolicy);
        llAbout = (LinearLayout) findViewById(R.id.llAboutUs);
        llSwitch = (LinearLayout) findViewById(R.id.llSwitchAccount);
        llLogout = (LinearLayout) findViewById(R.id.llLogout);
        llNightMode = (LinearLayout) findViewById(R.id.llSwitchNightMode);
        tvUserNameH = (TextView) findViewById(R.id.tvUserNameH);
        tvUserEmail = (TextView) findViewById(R.id.tvUserEmailH);
        tv_NumberFollowing = (TextView) findViewById(R.id.tv_NumberFollowing);
        tv_NumberFollowers = (TextView) findViewById(R.id.tv_NumberFollowers);
        ivUser1 = (ImageView) findViewById(R.id.ivUserImageH1);
        tvUserImage = (TextView) findViewById(R.id.tvUserImage);
        tvUserImageDrawer = (TextView) findViewById(R.id.tvUserImageDrawer);
        ivUser = (ImageView) findViewById(R.id.ivUserImageH);
        cbNight = (CheckBox) findViewById(R.id.cbNight);
        //  llDrawerHeader = (LinearLayout) findViewById(R.id.llDrawerHeader);
        tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        tvFollowing = (TextView) findViewById(R.id.tvFollowing);
        tvFollowing = (TextView) findViewById(R.id.tvFollowing);
        tvProfile = (TextView) findViewById(R.id.tvProfile);
        tvSettings = (TextView) findViewById(R.id.tvSettings);
        tvTerms = (TextView) findViewById(R.id.tvTerms);
        tvPrivacy = (TextView) findViewById(R.id.tvPrivacy);
        tvAbout = (TextView) findViewById(R.id.tvAbout);
        tvSwitchAccount = (TextView) findViewById(R.id.tvSwitchAccount);
        tvLogout = (TextView) findViewById(R.id.tvLogout);
        tvNightMode = (TextView) findViewById(R.id.tvNightMode);
        ivSettings = (ImageView) findViewById(R.id.ivSettings);
        ivTerms = (ImageView) findViewById(R.id.ivTerms);
        ivPrivacyPolicy = (ImageView) findViewById(R.id.ivPrivacyPolicy);
        ivAbout = (ImageView) findViewById(R.id.ivAbout);
        ivSwitch = (ImageView) findViewById(R.id.ivSwitch);
        ivLogout = (ImageView) findViewById(R.id.ivLogout);
        ivNightMode = (ImageView) findViewById(R.id.ivNightMode);
        ivProfile = findViewById(R.id.ivProfile);
        llMain = findViewById(R.id.llMain);
        ivLeftImage = (ImageView) findViewById(R.id.ivLeftImage);
        tvUserEmailH = (TextView) findViewById(R.id.tvUserEmailH);
        viewDrawerBottom = (View) findViewById(R.id.viewDrawerBottom);
        viewDrawerView = (View) findViewById(R.id.viewDrawerTop);
    }
    private void setCurrentTheme() {
        binding.layoutHomeActivity.toolbarMain.setBackgroundColor(ContextCompat.getColor(mContext, themes.setDarkTheme()));
        binding.layoutHomeActivity.tvHeader.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
        binding.bottomNavigationHomeBottom.setBackgroundColor(ContextCompat.getColor(mContext, themes.setDarkBottom()));
        binding.homeActivityView.setBackgroundColor(ContextCompat.getColor(mContext, themes.setViewLineGrey()));
        llMain.setBackgroundColor(ContextCompat.getColor(mContext, themes.setDarkTheme()));
        tvUserNameH.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
        tvUserImageDrawer.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
        tvUserEmail.setTextColor(ContextCompat.getColor(mContext, themes.setGreyHint()));
        tv_NumberFollowing.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
        tv_NumberFollowers.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
        tvFollowers.setTextColor(ContextCompat.getColor(mContext, themes.setGreyHint()));
        tvFollowing.setTextColor(ContextCompat.getColor(mContext, themes.setGreyHint()));
        tvPrivacy.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
        tvProfile.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
        tvSettings.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
        tvTerms.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
        tvPrivacy.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
        tvAbout.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
        tvSwitchAccount.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
        tvLogout.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
        tvNightMode.setTextColor(ContextCompat.getColor(mContext, themes.setDarkThemeText()));
        viewDrawerBottom.setBackgroundColor(ContextCompat.getColor(mContext, themes.setViewLineGrey()));
        viewDrawerView.setBackgroundColor(ContextCompat.getColor(mContext, themes.setViewLineGrey()));
        themes.changeIconColorToWhite(mContext, ivProfile);
        themes.changeIconColorToWhite(mContext, ivSettings);
        themes.changeIconColorToWhite(mContext, ivTerms);
        themes.changeIconColorToWhite(mContext, ivPrivacyPolicy);
        themes.changeIconColorToWhite(mContext, ivAbout);
        themes.changeIconColorToWhite(mContext, ivSwitch);
        themes.changeIconColorToWhite(mContext, ivLogout);
        themes.changeShareIcon(mContext, ivNightMode);
        themes.changeIconColor(mContext, binding.layoutHomeActivity.ivRight);
/*        int id = binding.bottomNavigationHomeBottom.getSelectedItemId();
        binding.bottomNavigationHomeBottom.setSelectedItemId(id);*/
        if (CommonUtils.getPreferencesBoolean(mContext, AppConstants.DARK_THEME)) {
            binding.bottomNavigationHomeBottom.getMenu().findItem(R.id.action_Search).setIcon(R.mipmap.search_deactive);
        }
        else {
            binding.bottomNavigationHomeBottom.getMenu().findItem(R.id.action_Search).setIcon(R.mipmap.search_ico);
        }
        if (CommonUtils.getPreferencesBoolean(mContext, AppConstants.DARK_THEME)) {
            binding.bottomNavigationHomeBottom.getMenu().findItem(R.id.action_Home).setIcon(R.mipmap.home_deactive);
        }
        else {
            binding.bottomNavigationHomeBottom.getMenu().findItem(R.id.action_Home).setIcon(R.mipmap.home_icon);
        }
        if (CommonUtils.getPreferencesBoolean(mContext, AppConstants.DARK_THEME)) {
            binding.bottomNavigationHomeBottom.getMenu().findItem(R.id.action_Message).setIcon(R.mipmap.d_notification_dark);
        }
        else {
            binding.bottomNavigationHomeBottom.getMenu().findItem(R.id.action_Message).setIcon(R.mipmap.d_notification_light);
        }
        if (CommonUtils.getPreferencesBoolean(mContext, AppConstants.DARK_THEME)) {
            binding.bottomNavigationHomeBottom.getMenu().findItem(R.id.action_Chat).setIcon(R.mipmap.chat_deactive);
        }
        else {
            binding.bottomNavigationHomeBottom.getMenu().findItem(R.id.action_Chat).setIcon(R.mipmap.comment_icon_buttom);
        }
        if (CommonUtils.getPreferencesBoolean(mContext, AppConstants.DARK_THEME)) {
            binding.bottomNavigationHomeBottom.getMenu().findItem(R.id.action_DoubleMessage).setIcon(R.mipmap.help_deactive);
        }
        else {
            binding.bottomNavigationHomeBottom.getMenu().findItem(R.id.action_DoubleMessage).setIcon(R.mipmap.help_icon);
        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getExtras() != null) {
            isRunForCall = intent.getExtras().getBoolean(Consts.EXTRA_IS_STARTED_FOR_CALL);
            if (isRunForCall) {
                CallActivity.start(HomeActivity.this, true, "");
            }
        }
    }
    /* QuickBlox for video call*/
    private void qbLogin() {
        QBUser user = new QBUser();
        user.setLogin(userData.getId() + "@yellowseed.com");
        user.setPassword("yellowseed@123");
        user.setId(Integer.valueOf(userData.getQb_id()));
        SharedPrefsHelper.getInstance().saveQbUser(user);
        final QBUser qbUser = SharedPrefsHelper.getInstance().getQbUser();
        requestExecutor.signInUser(user, new QBEntityCallbackImpl<QBUser>() {
            @Override
            public void onSuccess(QBUser result, Bundle params) {
                Log.e("QB Success", "QB Success");
                ProgressDialogFragment.hide(getSupportFragmentManager());
                SharedPrefsHelper.getInstance().saveQbUser(result);
                if (SharedPrefsHelper.getInstance().hasQbUser()) {
                    CallService.start(HomeActivity.this, qbUser);
                    if (checker.lacksPermissions(Consts.PERMISSIONS[1])) {
                        startPermissionsActivity(true);
                    }
                }
            }
            @Override
            public void onError(QBResponseException responseException) {
                Log.e("QB falied", "QB falied");
                ProgressDialogFragment.hide(getSupportFragmentManager());
//                Toaster.longToast(R.string.sign_up_error);
            }
        });
    }
    private void startPermissionsActivity(boolean checkOnlyAudio) {
        PermissionsActivity.startActivity(this, checkOnlyAudio, Consts.PERMISSIONS);
    }
    private void getIntentValue() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            if (getIntent().getStringExtra(AppConstants.FROM) != null && getIntent().getStringExtra(AppConstants.FROM).equalsIgnoreCase(GroupEditActivity.class.getSimpleName())) {
                binding.homeActivityView.setVisibility(View.VISIBLE);
                binding.layoutHomeActivity.ivLeftImage.setVisibility(View.GONE);
                binding.layoutHomeActivity.tvUserImage.setVisibility(View.GONE);
                binding.layoutHomeActivity.tvLeftText.setVisibility(View.VISIBLE);
                binding.layoutHomeActivity.tvLeftText.setText(R.string.edit);
                binding.layoutHomeActivity.tvHeader.setText(R.string.chats);
                binding.bottomNavigationHomeBottom.setItemIconTintList(null);
                binding.bottomNavigationHomeBottom.getMenu().findItem(R.id.action_Chat).setIcon(R.mipmap.comment_icon_buttom_sel);
                mFragment = new ChatsFragment();
                CommonUtils.setFragment(mFragment, true, HomeActivity.this, R.id.fl_MainHome, "");
            }
            else if (getIntent().getStringExtra(AppConstants.FROM) != null && getIntent().getStringExtra(AppConstants.FROM).
                    equalsIgnoreCase(ChatsScreenFrgActivity.class.getSimpleName())) {
                GetChatResonse.UserInfoBean infoBean = (GetChatResonse.UserInfoBean) getIntent().getSerializableExtra("forward");
                binding.homeActivityView.setVisibility(View.VISIBLE);
                binding.layoutHomeActivity.ivLeftImage.setVisibility(View.GONE);
                binding.layoutHomeActivity.tvUserImage.setVisibility(View.GONE);
                binding.layoutHomeActivity.tvLeftText.setVisibility(View.VISIBLE);
                binding.layoutHomeActivity.tvLeftText.setText(R.string.edit);
                binding.layoutHomeActivity.tvHeader.setText(R.string.chats);
                binding.bottomNavigationHomeBottom.setItemIconTintList(null);
                binding.bottomNavigationHomeBottom.getMenu().findItem(R.id.action_Chat).setIcon(R.mipmap.comment_icon_buttom_sel);
                Bundle bundle = new Bundle();
                bundle.putSerializable(AppConstants.IS_FORWARD, (Serializable) infoBean);
                mFragment = new ChatsFragment();
                CommonUtils.setFragment(mFragment, bundle, true, HomeActivity.this, R.id.fl_MainHome, "");
            }
        }
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mListener.onItemTouch(ev) || super.dispatchTouchEvent(ev);
    }
    @Override
    public void initializedControl() {
        setCurrentTheme();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isRunForCall = extras.getBoolean(Consts.EXTRA_IS_STARTED_FOR_CALL);
        }
        AppConstants.clickMenuItem = false;
        mToolbar = findViewById(R.id.toolbar_main);
        binding.bottomNavigationHomeBottom.setItemIconTintList(null);
        binding.bottomNavigationHomeBottom.getMenu().findItem(R.id.action_Home).setIcon(R.mipmap.home_icon_sel);

       // CommonUtils.userProfile(mContext, binding.layoutHomeActivity.ivLeftImage, binding.layoutHomeActivity.tvUserImage);
        mFragment = new HomeBottomFragment();
        CommonUtils.setFragment(mFragment, true, this, R.id.fl_MainHome, "");
        BottomNavigationViewHelper.removeShiftMode(binding.bottomNavigationHomeBottom);
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) binding.bottomNavigationHomeBottom.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            // set your height here
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, displayMetrics);
            // set your width here
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, displayMetrics);
            iconView.setLayoutParams(layoutParams);
        }
        binding.bottomNavigationHomeBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                menuItem = item;
                setToolBar();
                setBottomNavigationIcon();
                userData = PrefManager.getInstance(HomeActivity.this).getCurrentUser();
                binding.layoutHomeActivity.ivLeftImage.setImageResource(R.mipmap.header_img);
               // CommonUtils.userProfile(mContext, binding.layoutHomeActivity.ivLeftImage, binding.layoutHomeActivity.tvUserImage);
                switch (item.getItemId()) {
                    case R.id.action_Home:
                        AppConstants.clickStoryItem = true;
                        binding.homeActivityView.setVisibility(View.VISIBLE);
                        if (CommonUtils.getPreferencesBoolean(mContext, AppConstants.DARK_THEME)) {
                            item.setIcon(R.mipmap.home_active);
                        }
                        else {
                            item.setIcon(R.mipmap.home_icon_sel);
                        }
                        binding.layoutHomeActivity.ivRight.setImageResource(R.mipmap.share_post_icon);
                        mFragment = new HomeBottomFragment();
                        CommonUtils.setFragment(mFragment, true, HomeActivity.this, R.id.fl_MainHome, "");
                        break;
                    case R.id.action_Search:
                        AppConstants.clickStoryItem = false;
                        binding.layoutHomeActivity.ivLeftImage.setVisibility(View.VISIBLE);
                        //  CommonUtils.userProfile(mContext, binding.layoutHomeActivity.ivLeftImage, binding.layoutHomeActivity.tvUserImage);
                        binding.homeActivityView.setVisibility(View.VISIBLE);
                        binding.layoutHomeActivity.tvHeader.setText(R.string.explore);
                        binding.layoutHomeActivity.ivRight.setImageResource(R.mipmap.search_header_img);
                        if (CommonUtils.getPreferencesBoolean(mContext, AppConstants.DARK_THEME)) {
                            item.setIcon(R.mipmap.search_active);
                        }
                        else {
                            item.setIcon(R.mipmap.search_ico_sel);
                        }
                        mFragment = new ExploreBottomFragment();
                        CommonUtils.setFragment(mFragment, true, HomeActivity.this, R.id.fl_MainHome, "");
                        break;
                    case R.id.action_Message:
                        binding.homeActivityView.setVisibility(View.GONE);
                        binding.layoutHomeActivity.tvHeader.setText(R.string.notification);
                        binding.layoutHomeActivity.ivRight.setImageResource(R.mipmap.user_add_icon);
                        if (CommonUtils.getPreferencesBoolean(mContext, AppConstants.DARK_THEME)) {
                            item.setIcon(R.mipmap.a_notification_dark);
                        }
                        else {
                            item.setIcon(R.mipmap.notification);
                        }
                        mFragment = new NotificationFragment();
                        CommonUtils.setFragment(mFragment, true, HomeActivity.this, R.id.fl_MainHome, "");
                        break;
                    case R.id.action_Chat:
                        binding.homeActivityView.setVisibility(View.VISIBLE);
                        binding.layoutHomeActivity.ivLeftImage.setVisibility(View.GONE);
                        binding.layoutHomeActivity.tvUserImage.setVisibility(View.GONE);
                        binding.layoutHomeActivity.tvLeftText.setVisibility(View.VISIBLE);
                        binding.layoutHomeActivity.tvLeftText.setText(R.string.edit);
                        binding.layoutHomeActivity.tvHeader.setText(R.string.chats);
                        if (CommonUtils.getPreferencesBoolean(mContext, AppConstants.DARK_THEME)) {
                            item.setIcon(R.mipmap.chat_active);
                        }
                        else {
                            item.setIcon(R.mipmap.comment_icon_buttom_sel);
                        }
                        mFragment = new ChatsFragment();
                        CommonUtils.setFragment(mFragment, true, HomeActivity.this, R.id.fl_MainHome, "");
                        break;
                    case R.id.action_DoubleMessage:
                        binding.homeActivityView.setVisibility(View.VISIBLE);
                        binding.layoutHomeActivity.tvHeader.setText(R.string.anonymous);
                        binding.layoutHomeActivity.ivRight.setVisibility(View.GONE);
                        if (CommonUtils.getPreferencesBoolean(mContext, AppConstants.DARK_THEME)) {
                            item.setIcon(R.mipmap.help_active);
                        }
                        else {
                            item.setIcon(R.mipmap.help_icon_sel);
                        }
                        mFragment = new AnonymousFragment();
                        CommonUtils.setFragment(mFragment, true, HomeActivity.this, R.id.fl_MainHome, "");
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        setupDrawer();
    }
    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fl_MainHome);
        if (fragment instanceof HomeBottomFragment || fragment instanceof ExploreBottomFragment
                || fragment instanceof NotificationFragment || fragment instanceof AnonymousFragment || fragment instanceof ChatsFragment) {
            showAlert(getString(R.string.are_you_sure_you_want_to_exit));
        }
        else {
            getSupportFragmentManager().popBackStack();
        }
    }
    /**
     * Show popup to exit app
     */
    private void showAlert(String msg) {
        AlertDialog.Builder builder;
        if (CommonUtils.getPreferencesBoolean(mContext, AppConstants.DARK_THEME)) {
            builder = new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AlertDialogCustom));
        }
        else {
            builder = new AlertDialog.Builder(mContext);
        }
        builder.setTitle("")
                .setMessage(msg)
                .setCancelable(false)
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        moveTaskToBack(true);
                        // finish();
                        System.exit(0);
                        dialog.dismiss();
                    }
                });
        builder.show();
    }
    @Override
    protected void onResume() {
        AppConstants.clickMenuItem = false;
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, binding.drawerLayout, binding.layoutHomeActivity.toolbarMain);
        drawerFragment.setDrawerListener(this);
       /* CommonApi.callGetProfileAPI(mContext, false, new ApiCallback() {
            @Override
            public void onSuccess(ApiResponse apiResponse) {
                userData = apiResponse.getUser();
                if (userData.getQb_id() != null && userData.getQb_id().length() > 0) {
                    qbLogin();
                }
                //  CommonUtils.savePreferencesString(mContext,AppConstants.USER_ID, userData.getUid());
               *//* Picasso.with(mContext).load(userData.getImage()).placeholder(R.drawable.user_placeholder).
                       transform(new CircleTransformation()).memoryPolicy(MemoryPolicy.NO_CACHE).into(binding.layoutHomeActivity.ivLeftImage);
             *//*
                ///CommonUtils.userProfile(mContext, binding.layoutHomeActivity.ivLeftImage, binding.layoutHomeActivity.tvUserImage);
                setUserData(userData);
            }
            @Override
            public void onFailure(ApiResponse apiResponse) {
            }
        });*/
        super.onResume();
    }
    public void setBottomNavigationIcon() {
        if (CommonUtils.getPreferencesBoolean(mContext, AppConstants.DARK_THEME)) {
            binding.bottomNavigationHomeBottom.getMenu().findItem(R.id.action_Home).setIcon(R.mipmap.home_deactive);
            binding.bottomNavigationHomeBottom.getMenu().findItem(R.id.action_Search).setIcon(R.mipmap.search_deactive);
            binding.bottomNavigationHomeBottom.getMenu().findItem(R.id.action_Message).setIcon(R.mipmap.d_notification_dark);
            binding.bottomNavigationHomeBottom.getMenu().findItem(R.id.action_Chat).setIcon(R.mipmap.chat_deactive);
            binding.bottomNavigationHomeBottom.getMenu().findItem(R.id.action_DoubleMessage).setIcon(R.mipmap.help_deactive);
        } else {
            binding.bottomNavigationHomeBottom.getMenu().findItem(R.id.action_Home).setIcon(R.mipmap.home_icon);
            binding.bottomNavigationHomeBottom.getMenu().findItem(R.id.action_Search).setIcon(R.mipmap.search_ico);
            binding.bottomNavigationHomeBottom.getMenu().findItem(R.id.action_Message).setIcon(R.mipmap.notification_unselect);
            binding.bottomNavigationHomeBottom.getMenu().findItem(R.id.action_Chat).setIcon(R.mipmap.comment_icon_buttom);
            binding.bottomNavigationHomeBottom.getMenu().findItem(R.id.action_DoubleMessage).setIcon(R.mipmap.help_icon);
        }
    }
    @Override
    public void setToolBar() {
        binding.layoutHomeActivity.ivLeftImage.setVisibility(View.VISIBLE);
        binding.layoutHomeActivity.tvUserImage.setVisibility(View.VISIBLE);
        binding.layoutHomeActivity.ivLeftImage.setImageResource(R.mipmap.header_img);

        binding.layoutHomeActivity.tvLeftText.setVisibility(View.GONE);
        binding.layoutHomeActivity.tvHeader.setVisibility(View.VISIBLE);
        binding.layoutHomeActivity.tvHeader.setText(R.string.home);
        binding.layoutHomeActivity.ivRight.setVisibility(View.VISIBLE);
        binding.layoutHomeActivity.ivRightSecond.setVisibility(View.GONE);
    }
    @Override
    public void setOnClickListener() {
        binding.layoutHomeActivity.ivLeftImage.setOnClickListener(this);
        binding.layoutHomeActivity.tvUserImage.setOnClickListener(this);
        binding.layoutHomeActivity.ivRight.setOnClickListener(this);
        tvUserEmailH.setOnClickListener(this);
        ivLeftImage.setOnClickListener(this);
        llFollowersH.setOnClickListener(this);
        llFollowingH.setOnClickListener(this);
        llAbout.setOnClickListener(this);
        llProfile.setOnClickListener(this);
        llSetting.setOnClickListener(this);
        llTerms.setOnClickListener(this);
        llPrivacy.setOnClickListener(this);
        llSwitch.setOnClickListener(this);
        llLogout.setOnClickListener(this);
        llNightMode.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvUserNameH:
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Bundle bundle = new Bundle();
                        bundle.putString(AppConstants.FROM, "myprofile");
                        ActivityController.startActivity(HomeActivity.this, MyProfileActivity.class, bundle);
                    }
                }, DRAWER_CLOSE_TIME);
                break;
            case R.id.tvUserEmailH:
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Bundle bundle = new Bundle();
                        bundle.putString(AppConstants.FROM, "myprofile");
                        ActivityController.startActivity(HomeActivity.this, MyProfileActivity.class, bundle);
                    }
                }, DRAWER_CLOSE_TIME);
                break;
            // case R.id.tvUserImage:
            case R.id.tvUserImage:
            case R.id.ivLeftImage:
                binding.drawerLayout.openDrawer(Gravity.START);
                break;
            case R.id.llProfile:
                binding.drawerLayout.closeDrawers();
                ActivityController.startActivity(HomeActivity.this, MyProfileActivity.class);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Bundle bundle = new Bundle();
                        bundle.putString(AppConstants.FROM, "myprofile");
                        ActivityController.startActivity(HomeActivity.this, MyProfileActivity.class, bundle);
                    }
                }, DRAWER_CLOSE_TIME);
                break;
            case R.id.llTerms:
                binding.drawerLayout.closeDrawers();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(HomeActivity.this, TermsAndConditionActivity.class).putExtra("from", HomeActivity.class.getSimpleName()));
                    }
                }, DRAWER_CLOSE_TIME);
                break;
            case R.id.llFollowersH:
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ActivityController.startActivity(mContext, FollowerListActivity.class);
                    }
                }, DRAWER_CLOSE_TIME);
                break;
            case R.id.llFollowingH:
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ActivityController.startActivity(mContext, FollowerListActivity.class);
                    }
                }, DRAWER_CLOSE_TIME);
                break;
            case R.id.llSettings:
                binding.drawerLayout.closeDrawers();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ActivityController.startActivity(mContext, SettingsActivity.class);
                    }
                }, DRAWER_CLOSE_TIME);
                break;
            case R.id.llPrivacyPolicy:
                binding.drawerLayout.closeDrawers();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ActivityController.startActivity(mContext, PrivacyPolicyActivity.class);
                    }
                }, DRAWER_CLOSE_TIME);
                break;
            case R.id.llSwitchAccount:
                onSwitchSelectedSettings();


                //ToastUtils.showToastShort(mContext, "Work in progress");
                break;
            case R.id.llAboutUs:
                binding.drawerLayout.closeDrawers();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ActivityController.startActivity(mContext, AboutUSActivity.class);
                    }
                }, DRAWER_CLOSE_TIME);
                break;
            case R.id.llLogout:
                binding.drawerLayout.closeDrawers();
                AlertDialog.Builder builder;
                if (CommonUtils.getPreferencesBoolean(mContext, AppConstants.DARK_THEME)) {
                    builder = new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AlertDialogCustom));
                } else {
                    builder = new AlertDialog.Builder(mContext);
                }
                builder.setTitle(R.string.logout_cnfm);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityController.startActivity(mContext, LoginActivity.class);
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                break;
            case R.id.llSwitchNightMode:
                break;
            default:
                break;
        }
    }
    private void setupDrawer() {
        binding.layoutHomeActivity.ivLeftImage.setImageResource(R.mipmap.header_img);

      /*  if (!TextUtils.isEmpty(PrefManager.getInstance(mContext).getUserPic())) {
            *//*Picasso.with(mContext).load(PrefManager.getInstance(mContext).getUserPic()).placeholder(R.drawable.user_placeholder).
                    transform(new CircleTransformation()).memoryPolicy(MemoryPolicy.NO_CACHE).into(binding.layoutHomeActivity.ivLeftImage);
*//*
            CommonUtils.userProfile(mContext, binding.layoutHomeActivity.ivLeftImage, binding.layoutHomeActivity.tvUserImage);
        }*/

    }
    /**
     * api for user logout
     *
     * @param dialog
     */
    private void callLogoutAPI(final DialogInterface dialog) {
        if (CommonUtils.isOnline(mContext)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(mContext);
            progressDialog.show();
            ApiRequest apiRequest = new ApiRequest();
            apiRequest.setDevice(new DeviceModel());
            apiRequest.getDevice().setDevice_token(PrefManager.getInstance(mContext).getKeyDeviceToken());
            apiRequest.getDevice().setDevice_type(AppConstants.DEVICE_TYPE);
            Call<ApiResponse> call = ApiExecutor.getClient(mContext).apiLogout(apiRequest);
            call.enqueue(new retrofit2.Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    try {
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (response != null && response.body() != null) {
                        if (response.body().getResponseCode() == AppConstants.SUCCESS) {
                            callSuccessResponse(response.body(), dialog);
                        } else if (response.body().getResponseCode() == AppConstants.USER_BLOCKED) {
                            startActivity(new Intent(mContext, LoginActivity.class));
                            finishAffinity();
                            CommonUtils.clearPrefData(mContext);
                        } else {
                            ToastUtils.showToastShort(mContext, response.body().getResponseMessage());
                        }
                    } else {
                        ToastUtils.showToastShort(mContext, getString(R.string.server_error_msg));
                    }
                }
                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(mContext, getString(R.string.server_error_msg));
                }
            });
        } else {
            ToastUtils.showToastShort(mContext, mContext.getString(R.string.internet_alert_msg));
        }
    }

    private void onSwitchSelectedSettings() {
        LayoutswitchaccountBinding layoutswitchaccountBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.layoutswitchaccount, null, false);

        final Dialog dialog = DialogUtils.createDialog(mContext, layoutswitchaccountBinding.getRoot());
        layoutswitchaccountBinding.llMain.setBackground(ContextCompat.getDrawable(mContext, Themes.getInstance(mContext).setDarkStoryBackground()));
        layoutswitchaccountBinding.tvName1.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        layoutswitchaccountBinding.tvName2.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        layoutswitchaccountBinding.tvSwitchAccountss.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        layoutswitchaccountBinding.vieWLine.setBackgroundColor(ContextCompat.getColor(mContext,themes.setViewLineGrey()));
        layoutswitchaccountBinding.viewLine2.setBackgroundColor(ContextCompat.getColor(mContext,themes.setViewLineGrey()));
        layoutswitchaccountBinding.viewLine3.setBackgroundColor(ContextCompat.getColor(mContext,themes.setViewLineGrey()));
        layoutswitchaccountBinding.tvSwitchAccountss.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setDarkThemeText()));
        layoutswitchaccountBinding.tvSwitchAccountssCancel.setTextColor(ContextCompat.getColor(mContext, Themes.getInstance(mContext).setCancel()));

        layoutswitchaccountBinding.tvSwitchAccountssCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        layoutswitchaccountBinding.tvSwitchAccountssID1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        layoutswitchaccountBinding.tvSwitchAccountssID2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
    }



    private void callSuccessResponse(ApiResponse response, DialogInterface dialog) {
        dialog.dismiss();
        CommonUtils.clearPrefData(mContext);
        Intent intent = new Intent(mContext, LoginActivity.class);
        startActivity(intent);
        finishAffinity();
    }
    @Override
    public void onItemClick(View view, int position) {
        binding.drawerLayout.closeDrawers();
    }
    /*    @Override
        public void onNewSession(QBRTCSession session) {
            //TODO : Call Intent here
            //setCurrentSession(session);
    //        HomeActivity.start(context, true);
            Log.e(TAG, "Session " + session.getSessionID() + " are incoming");
            NotificationManager notifManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            notifManager.cancelAll();
            Intent intent = getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putExtra(Consts.EXTRA_IS_STARTED_FOR_CALL, true);
            mContext.startActivity(intent);
            isRunForCall = true;
            if (isRunForCall && webRtcSessionManager.getCurrentSession() != null) {
                CallActivity.start(HomeActivity.this, true, "");
            }
        }*/
    @Override
    public void onDrawerItemSelected(View view, int position) {
    }
    public void setUserData(UserModel userData) {
        tvUserNameH.setText(userData.getName());
        tvUserEmail.setText(userData.getUser_name());
        if (!(mFragment instanceof ChatsFragment)) {
            CommonUtils.userProfile(mContext, ivUser1, tvUserImageDrawer);
        }
        if (!TextUtils.isEmpty(userData.getImage())) {
            /*ivUser1.setVisibility(View.GONE);
            tvUserImage.setVisibility(View.VISIBLE);*/
        /*   Picasso.with(mContext).load(userData.getImage()).placeholder(R.drawable.user_placeholder).
                   transform(new CircleTransformation()).into(ivUser1);*/
        }
        if (userData.getAvatar_img() != null && userData.getAvatar_img().getAvatar_image() != null && !TextUtils.isEmpty(userData.getAvatar_img().getAvatar_image())) {
            Picasso.with(mContext).load(userData.getAvatar_img().getAvatar_image()).placeholder(R.drawable.user_placeholder).
                    transform(new CircleTransformation()).into(ivUser);
        }
        tv_NumberFollowing.setText("" + (userData.getFollow() == 0 ? 0 : userData.getFollow()));
        tv_NumberFollowers.setText("" + (userData.getFollower() == 0 ? 0 : userData.getFollower()));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mFragment != null) {
            mFragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}

