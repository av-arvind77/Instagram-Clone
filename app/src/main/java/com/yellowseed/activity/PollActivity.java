package com.yellowseed.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import com.yellowseed.adapter.HomeBottomViewAdapter;
import com.yellowseed.adapter.PollAdapter;
import com.yellowseed.adapter.PollResultAdapter;
import com.yellowseed.databinding.ActivityPollBinding;
import com.yellowseed.model.resModel.PollResultData;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.PrefManager;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.response.homepost.Post;

import java.util.ArrayList;
import java.util.List;

public class PollActivity extends BaseActivity {

    private Context context;
    //private List<PollModel> alPoll = new ArrayList<>();
    private List<String> alPoll = new ArrayList<>();
    private ActivityPollBinding binding;
    private Themes themes;
    private PollAdapter adapter;
    private PollResultAdapter resultAdapter;
    //private String[] options = {"India", "Pakistan", "China"};
    private boolean[] selected = {true, false, false};
    private Object intentData;
    private String from = "";
    private List<PollResultData> pollResult = new ArrayList<>();
    private PopupWindow quickAction;
    private boolean isOpen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_poll);
        context = PollActivity.this;
        themes = new Themes(context);
        getIntentData();
        initializedControl();
    }

    @Override
    public void initializedControl() {
        setCurrentTheme();
        setToolBar();
        setOnClickListener();
        setRecycler();
    }

    public void setCurrentTheme() {
        binding.pollToolBar.toolbarMain.setBackgroundColor(ContextCompat.getColor(context, themes.setDarkTheme()));
        binding.pollToolBar.tvHeader.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        themes.changeIconColor(context, binding.pollToolBar.ivLeft);
        binding.llMain.setBackgroundColor(ContextCompat.getColor(context, themes.setDarkTheme()));

        binding.tvPostUserName.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.tvPostTime.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeGreyText()));

        binding.tvPollQuestion.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.tvPollEndsIn.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeGreyText()));
        binding.tvPostLike.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.tvPostComment.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        binding.tvPostShare.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));

        themes.changeIconColorToWhite(context, binding.ivPostComment);
        themes.changeIconColorToWhite(context, binding.ivPostShare);
        themes.changeIconColorToWhite(context, binding.ivPostDownloads);
        themes.changeIconColorToWhite(context, binding.ivPostMenuItem);
        binding.viewLine.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setViewLineGrey()));

    }

    @Override
    public void setToolBar() {
        binding.pollToolBar.ivLeft.setVisibility(View.VISIBLE);
        binding.pollToolBar.tvHeader.setVisibility(View.VISIBLE);
        binding.pollToolBar.tvHeader.setText("Poll");
    }

    @Override
    public void setOnClickListener() {
        binding.pollToolBar.ivLeft.setOnClickListener(this);
        binding.ivHeart.setOnClickListener(this);
        binding.ivPostComment.setOnClickListener(this);
        binding.ivPostShare.setOnClickListener(this);
        binding.ivPostDownloads.setOnClickListener(this);
        binding.ivPostMenuItem.setOnClickListener(this);
    }


    public void setRecycler() {
        binding.rvPollData.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        binding.rvPollData.setLayoutManager(layoutManager);
        if (from.equalsIgnoreCase(AppConstants.POLL_RESULT)) {
            resultAdapter = new PollResultAdapter(context, pollResult);
            pollResult.addAll(resultData());
            binding.rvPollData.setAdapter(resultAdapter);
        } else {
            adapter = new PollAdapter(context, alPoll, "");
            alPoll.addAll(prepareData());
            binding.rvPollData.setAdapter(adapter);

        }


    }

    private List<String> prepareData() {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            if (i == 0) {
                data.add("India");
            } else if (i == 1) {
                data.add("Pakistan");
            } else {
                data.add("China");
            }

        }
        return data;
    }

    private List<PollResultData> resultData() {
        List<PollResultData> data = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            PollResultData pollResultData = new PollResultData();
            if (i == 0) {
                pollResultData.setPer(100);

                pollResultData.setName("India");
            } else if (i == 1) {
                pollResultData.setPer(0);
                pollResultData.setName("Pakistan");
            } else {
                pollResultData.setPer(0);
                pollResultData.setName("China");
            }
            data.add(pollResultData);

        }
        return data;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivLeft:
                onBackPressed();
                break;
            case R.id.ivHeart:
                binding.ivHeart.setImageResource(R.mipmap.heart_icon_sel);
                break;
            case R.id.ivPostComment:
                CommonUtils.showToast(context, "Work in progress");
                break;
            case R.id.ivPostShare:
                CommonUtils.showToast(context, "Work in progress");
                break;
            case R.id.ivPostDownloads:
                CommonUtils.showToast(context, "Work in progress");
                break;
            case R.id.ivPostMenuItem:
                if (isOpen) {
                    isOpen = false;
                    quickAction.dismiss();
                } else {
                    openMenuPopup(new Post(), findViewById(R.id.ivPostMenuItem), 0);
                }
                break;
            default:
                break;
        }
    }

    public void getIntentData() {
        if (getIntent() != null) {
            if (getIntent().getStringExtra(AppConstants.FROM) != null) {
                from = getIntent().getStringExtra(AppConstants.FROM);
            }

        }


    }

    public void setIntentData(Object intentData) {
        this.intentData = intentData;
    }


    private void openMenuPopup(final Post post, View targetView, final int position) {

        isOpen = true;
        BubbleLayout customLayout = (BubbleLayout) LayoutInflater.from(context).inflate(R.layout.popup_menu, null);
        customLayout.setBubbleColor(ContextCompat.getColor(context, themes.setPopupBackground()));
        quickAction = BubblePopupHelper.create(context, customLayout);
        LinearLayout otherProfile = (LinearLayout) customLayout.findViewById(R.id.llOtherProfile);
        LinearLayout llMyProfile = (LinearLayout) customLayout.findViewById(R.id.llMyProfile);


        llMyProfile.setBackgroundColor(ContextCompat.getColor(context, themes.setPopupBackground()));
        otherProfile.setBackgroundColor(ContextCompat.getColor(context, themes.setPopupBackground()));


        if (post.getUser() != null && new PrefManager(context).getUserId().equalsIgnoreCase(post.getUser().getId())) {
            llMyProfile.setVisibility(View.VISIBLE);
            otherProfile.setVisibility(View.GONE);
        } else {
            llMyProfile.setVisibility(View.GONE);
            otherProfile.setVisibility(View.VISIBLE);

        }
        TextView tvSendToChat = (TextView) customLayout.findViewById(R.id.tvSendToChat);
        TextView tvSendToChat1 = (TextView) customLayout.findViewById(R.id.tvSendToChat1);
        TextView tvShareFb1 = (TextView) customLayout.findViewById(R.id.tvShareFb1);
        TextView tvShareFb = (TextView) customLayout.findViewById(R.id.tvShareFb);
        TextView tvDeletePost = (TextView) customLayout.findViewById(R.id.tvDeletePost);
        TextView tvEditPost = (TextView) customLayout.findViewById(R.id.tvEditPost);
        TextView tvReportUser = (TextView) customLayout.findViewById(R.id.tvReportUser);
        TextView tvDeleteTag = (TextView) customLayout.findViewById(R.id.tvDeleteTag);
        View viewReportUser = (View) customLayout.findViewById(R.id.viewReportUser);
        ImageView ivEdit = customLayout.findViewById(R.id.ivEdit);
        ImageView ivShare = customLayout.findViewById(R.id.ivShare);
        ImageView ivSend2 = customLayout.findViewById(R.id.ivSend2);
        ImageView ivShare1 = customLayout.findViewById(R.id.ivShare1);
        ImageView ivSend = customLayout.findViewById(R.id.ivSend);

        ImageView ivHide = (ImageView) customLayout.findViewById(R.id.ivHide);
        ImageView ivHide1 = (ImageView) customLayout.findViewById(R.id.ivHide1);

        ImageView ivPin = (ImageView) customLayout.findViewById(R.id.ivPin);
        ImageView ivEdit1 = (ImageView) customLayout.findViewById(R.id.ivEdit1);
        ImageView ivRemove = (ImageView) customLayout.findViewById(R.id.ivRemove);
        TextView tvHide = (TextView) customLayout.findViewById(R.id.tvHide);
        TextView tvHide1 = (TextView) customLayout.findViewById(R.id.tvHide1);
        TextView tvEdit2 = (TextView) customLayout.findViewById(R.id.tvEditPost1);
        TextView tvPin = (TextView) customLayout.findViewById(R.id.tvPin);
        TextView tvRemove = (TextView) customLayout.findViewById(R.id.tvRemove);

        tvShareFb.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        tvSendToChat.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        tvEditPost.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        tvShareFb1.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        tvSendToChat1.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        tvHide.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        tvHide1.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        tvPin.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        tvEditPost.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        tvEdit2.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        tvRemove.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        Themes.getInstance(context).changeRightIcon(context, ivEdit);
        Themes.getInstance(context).changeIconColorToWhite(context, ivShare);
        Themes.getInstance(context).changeIconColorToWhite(context, ivSend2);
        Themes.getInstance(context).changeIconColorToWhite(context, ivShare1);
        Themes.getInstance(context).changeIconColorToWhite(context, ivSend);
        Themes.getInstance(context).changeShareIcon(context, ivHide);
        Themes.getInstance(context).changeShareIcon(context, ivHide1);
        Themes.getInstance(context).changeShareIcon(context, ivRemove);


        tvShareFb.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        tvSendToChat.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        tvEditPost.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        tvShareFb1.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        tvSendToChat1.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        themes.changeRightIcon(context, ivEdit);
        themes.changeIconColorToWhite(context, ivShare);
        themes.changeIconColorToWhite(context, ivSend2);
        themes.changeIconColorToWhite(context, ivShare1);
        themes.changeIconColorToWhite(context, ivSend);
        themes.changeIconColorToWhite(context, ivEdit1);
        themes.changePinColor(context, ivPin);


        /*if (models.get(position).getTagFriends() != null && models.get(position).getTagFriends().size() > 0) {
            tvDeleteTag.setVisibility(View.VISIBLE);
            viewReportUser.setVisibility(View.VISIBLE);
        } else {
            tvDeleteTag.setVisibility(View.GONE);
            viewReportUser.setVisibility(View.GONE);

        }*/
        tvDeletePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOpen = false;
                //  deletePostPopup(position, models.get(position).getPost().getId(), models.get(position).getSharedId());
                quickAction.dismiss();

            }
        });
        tvShareFb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOpen = false;
                // facebookSharing(post);
                quickAction.dismiss();
            }
        });
        tvEdit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quickAction.dismiss();;
                ToastUtils.showToastShort(context,"Work in progress");
            }
        });
        tvEditPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quickAction.dismiss();


            }
        });
        tvShareFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOpen = false;
                //facebookSharing(post);
                quickAction.dismiss();
            }
        });
        tvSendToChat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOpen = false;

                if (CommonUtils.isOnline(context)) {
                    //   callGetRoomAPI(post);
                } else {
                    CommonUtils.showToast(context, getString(R.string.internet_alert_msg));
                }
                quickAction.dismiss();
            }
        });
        tvDeleteTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOpen = false;

                // deleteTagPopup(position, models.get(position).getTagFriends().get(position).getId());
                quickAction.dismiss();
            }
        });
        tvSendToChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOpen = false;

                if (CommonUtils.isOnline(context)) {
                    // callGetRoomAPI(post);
                } else {
                    CommonUtils.showToast(context, getString(R.string.internet_alert_msg));
                }
                quickAction.dismiss();
            }
        });
        tvHide1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOpen = false;

                if (CommonUtils.isOnline(context)) {


                    //   removePosition = position;
                    // callHidePost(models.get(position).getPost().getId());


                } else {
                    CommonUtils.showToast(context, getString(R.string.internet_alert_msg));
                }
                quickAction.dismiss();
            }
        });
        tvReportUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOpen = false;
                //   addReportDialog(position);
                quickAction.dismiss();
            }
        });
        tvEditPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOpen = false;
                quickAction.dismiss();
/*
                startActivity(new Intent(context, SocialPostActivity.class)
                        .putExtra(AppConstants.FROM, AppConstants.FROM_EDIT_POST)
                        .putExtra(AppConstants.POSITION, position)
                        .putExtra(AppConstants.POST_MODEL, models.get(position)));*/

            }
        });
        quickAction.showAsDropDown(targetView);


    }


}
