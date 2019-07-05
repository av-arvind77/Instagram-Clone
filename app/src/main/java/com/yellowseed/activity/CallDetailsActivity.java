package com.yellowseed.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.daasuu.bl.BubbleLayout;
import com.daasuu.bl.BubblePopupHelper;
import com.yellowseed.R;
import com.yellowseed.adapter.CallHistoryAdapter;
import com.yellowseed.adapter.CallhistoryViewPAgerAdapter;
import com.yellowseed.customView.CustomRobotoBoldTextView;
import com.yellowseed.customView.CustomRobotoRegularTextView2;
import com.yellowseed.databinding.ActivityCallDetailsBinding;
import com.yellowseed.fragments.CallHistoryFragment;
import com.yellowseed.fragments.MissedCallFragment;
import com.yellowseed.listener.OnItemLongClickListener;
import com.yellowseed.model.CallHistoryModel;
import com.yellowseed.utils.ActivityController;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.Themes;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class CallDetailsActivity extends BaseActivity {


    public boolean isSingleSelection = false;
    private Context context;
    private ActivityCallDetailsBinding binding;
    private Themes themes;
    private CallHistoryFragment callHistoryFragment;
    private MissedCallFragment missedCallFragment;
    private List<CallHistoryModel> alCallHistory = new ArrayList<>();
    private int flag = 0;


    private CallHistoryAdapter adapter;
    private boolean isLongClick;


    private String[] callerName = {"John Thomas","Jenny Koul","Mike Keel","Tiny Tim", "Kelly Kim", "Jeeny Koul", "Ris","John Thomas","Jenny Koul","Mike Keel"};
    private String[] callType = {"(2)12 october,4:32 pm", "(6)13 october,5:32 pm", "(3)17 october,8:32 pm", "(2)20 october,7:32 pm","(2)22 october,7:32 pm","(2)24 october,7:32 pm","(2)25 october,7:32 pm","(2)26 october,7:32 pm","(2)26 october,7:32 pm","(2)28 october,7:32 pm"};
   // private String[] day = {"Yesterday", "Today", "Tuesday", "26/10/18"};
    private String[] time = {"2h ago", "3h ago", "8h ago","", "Monday", "","","","",""};
    private int[] callStatus={R.mipmap.arrow_1,R.mipmap.arrow_2,R.mipmap.arrow_1,R.mipmap.arrow_2,R.mipmap.arrow_2,R.mipmap.arrow_2,R.mipmap.arrow_1,R.mipmap.arrow_1,R.mipmap.arrow_2,R.mipmap.arrow_2};
    private int[] userImage = {R.mipmap.profile_icon, R.mipmap.profile_icon2, R.mipmap.profile_icon3, R.mipmap.profile_icon4,R.mipmap.profile_icon3,R.mipmap.profile_icon, R.mipmap.profile_icon2, R.mipmap.profile_icon3, R.mipmap.profile_icon4,R.mipmap.profile_icon3,};
    private int[] calltypeImg = {R.mipmap.video_call_white, R.mipmap.call_white, R.mipmap.video_call_white, R.mipmap.call_white,R.mipmap.video_call_white,R.mipmap.video_call_white, R.mipmap.call_white, R.mipmap.video_call_white, R.mipmap.call_white,R.mipmap.video_call_white};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_call_details);
        context = CallDetailsActivity.this;
        themes = new Themes(context);
        initializedControl();
    }

    @Override
    public void initializedControl() {
        setCurrentTheme();
        setToolBar();
        setOnClickListener();
        setRecycler();
       // viewPagerTab();
    }

    @Override
    public void setToolBar() {
        binding.toolbarCallDetail.ivLeft.setVisibility(View.VISIBLE);
        binding.toolbarCallDetail.tvHeader.setVisibility(View.VISIBLE);
        binding.toolbarCallDetail.tvHeader.setText("Call Detail");

        binding.toolbarCallDetail.ivRight.setImageResource(R.mipmap.three_dot);
        binding.toolbarCallDetail.ivRightSecond.setImageResource(R.mipmap.add_call);

        binding.toolbarCallDetail.ivRight.setVisibility(View.VISIBLE);
        binding.toolbarCallDetail.ivRightSecond.setVisibility(View.VISIBLE);

    }
/*
    private void viewPagerTab() {
        setupViewPager(binding.callListContainer);
        binding.callDetailsTab.setupWithViewPager(binding.callListContainer);
    }*/





    public void setRecycler() {
        binding.rvCallHistory.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        binding.rvCallHistory.setLayoutManager(layoutManager);
        alCallHistory.addAll(prepareData());
        adapter = new CallHistoryAdapter(context, alCallHistory, new OnItemLongClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.llParent:
                        if (isSingleSelection) {

                        if (alCallHistory.get(position).isSelected()) {

                            alCallHistory.get(position).setSelected(false);

                        } else {
                            alCallHistory.get(position).setSelected(true);

                        }
                        adapter.notifyItemChanged(position);



                        if (isLongClick=true) {

                            for (int i = 0; i < alCallHistory.size(); i++) {

                                if (alCallHistory.get(i).isSelected()) {
                                    changeIcon(true);
                                    break;
                                } else {
                                    changeIcon(false);
                                }
                            }
                        }
                        }


                        break;
                    case R.id.ivCallType:
                        CommonUtils.showToast(context, "work in progress");
                        break;

                    case R.id.ivDelete:

                        alCallHistory.remove(position);
                        if (alCallHistory.size() == 0) {
                            binding.tvNoData.setVisibility(View.VISIBLE);
                        }
                        adapter.notifyItemRemoved(position);
                        break;
                    default:

                        break;
                }
            }





            @Override
            public void onLongClick(View view, int position) {
                //  ((CallDetailsActivity)getContext()).isSingleSelection=true;

                if (alCallHistory.get(position).isSelected()) {

                    alCallHistory.get(position).setSelected(false);

                } else {
                    alCallHistory.get(position).setSelected(true);

                }
                adapter.notifyItemChanged(position);

                for (int i = 0; i < alCallHistory.size(); i++) {

                    if (alCallHistory.get(i).isSelected()) {
                     changeIcon(true);
                        break;
                    } else {

                    changeIcon(false);
                    }

                }


            }
        });
        binding.rvCallHistory.setAdapter(adapter);
    }

    private List<CallHistoryModel> prepareData() {
        List<CallHistoryModel> data = new ArrayList<>();
        for (int i = 0; i < callerName.length; i++) {
            CallHistoryModel model = new CallHistoryModel();
            model.setUserName(callerName[i]);
            model.setCallType(callType[i]);
            model.setCallStatus(callStatus[i]);
           // model.setDay(day[i]);
            model.setTime(time[i]);
            model.setCalltypeImg(calltypeImg[i]);
            model.setUserImage(userImage[i]);
            data.add(model);
        }
        return data;
    }



    public void deleteData() {
        List<CallHistoryModel> tempList = new ArrayList<>();


        for (int i = 0; i < alCallHistory.size(); i++) {

            if (!alCallHistory.get(i).isSelected()) {
                tempList.add(alCallHistory.get(i));

            }
        }
        alCallHistory.clear();

        alCallHistory.addAll(tempList);
        if (alCallHistory.size() == 0)

        {
            binding.tvNoData.setVisibility(View.VISIBLE);
        }
        adapter.notifyDataSetChanged();


    }


    private void setupViewPager(ViewPager callListContainer) {
        CallhistoryViewPAgerAdapter adapter = new CallhistoryViewPAgerAdapter(getSupportFragmentManager());
        callHistoryFragment = new CallHistoryFragment();
        missedCallFragment = new MissedCallFragment();
        adapter.addFragment(callHistoryFragment, "Call History");
        adapter.addFragment(missedCallFragment, "Missed Call");
        callListContainer.setAdapter(adapter);
    }

    @Override
    public void setOnClickListener() {
        binding.toolbarCallDetail.ivLeft.setOnClickListener(this);
        binding.toolbarCallDetail.ivRight.setOnClickListener(this);
        binding.toolbarCallDetail.ivRightSecond.setOnClickListener(this);
    }


    public void setCurrentTheme() {
        binding.toolbarCallDetail.toolbarMain.setBackgroundColor(ContextCompat.getColor(context, themes.setDarkTheme()));
        binding.toolbarCallDetail.tvHeader.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.viewLine.setBackgroundColor(ContextCompat.getColor(context, themes.setViewLineGrey()));

        themes.changeIconColor(context, binding.toolbarCallDetail.ivLeft);
        themes.changeIconColor(context, binding.toolbarCallDetail.ivRight);
        themes.changeShareIcon(context, binding.toolbarCallDetail.ivRightSecond);
        binding.callDetailsTab.setSelectedTabIndicatorColor(ContextCompat.getColor(context, themes.setSelectedIndicatorColor()));
        binding.llMain.setBackgroundColor(ContextCompat.getColor(context, themes.setDarkTheme()));
        binding.callDetailsTab.setTabTextColors(ContextCompat.getColor(context, R.color.lightgrey), ContextCompat.getColor(context, themes.setYellow()));
        binding.callDetailsTab.setSelectedTabIndicatorColor(ContextCompat.getColor(context, themes.setYellow()));
        binding.callDetailsTab.setBackgroundColor(ContextCompat.getColor(context, themes.setDarkTheme()));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivLeft:
                onBackPressed();
                break;
            case R.id.ivRight:
                openMenuPopup(v);
                break;
            case R.id.ivRightSecond:
                if (flag == 0) {
                    ActivityController.startActivity(context, NewCallActivity.class);

                } else {
             /*       callHistoryFragment.deleteData();
                    missedCallFragment.deleteData();*/
                  deleteData();
                    flag = 0;
                    binding.toolbarCallDetail.ivRightSecond.setImageResource(R.mipmap.add_call);

                }
                break;

            default:
                break;
        }
    }


    private void openMenuPopup(View view) {

        BubbleLayout customLayout;
        customLayout = (BubbleLayout) LayoutInflater.from(this).inflate(R.layout.call_log_popup_menu, null);

        customLayout.setBubbleColor(ContextCompat.getColor(context, Themes.getInstance(context).setPopupBackground()));
        final PopupWindow quickAction = BubblePopupHelper.create(this, customLayout);
        LinearLayout llClear = (LinearLayout) customLayout.findViewById(R.id.llClear);
        BubbleLayout bubbleLayout = customLayout.findViewById(R.id.blMain);
        TextView tvClearCall = (TextView) customLayout.findViewById(R.id.tvClearCall);
        llClear.setBackgroundColor(ContextCompat.getColor(context, themes.setPopupBackground()));
        tvClearCall.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));

        tvClearCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alCallHistory.clear();
                quickAction.dismiss();

            }
        });

        quickAction.showAsDropDown(view);

    }

    public void changeIcon(boolean show) {
        isSingleSelection = show;

        if (show) {
            flag = 1;
            binding.toolbarCallDetail.ivRightSecond.setImageResource(R.mipmap.delete_icon);

        } else {
            flag = 0;
            binding.toolbarCallDetail.ivRightSecond.setImageResource(R.mipmap.add_call);

        }

    }


}
