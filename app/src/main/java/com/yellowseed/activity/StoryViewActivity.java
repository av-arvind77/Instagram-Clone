package com.yellowseed.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yellowseed.R;
import com.yellowseed.adapter.DirectUserPendingAdapter;
import com.yellowseed.adapter.StoryViewVotersAdapter;
import com.yellowseed.databinding.ActivityStoryViewBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.model.MessageEvent;
import com.yellowseed.model.SendToModel;
import com.yellowseed.model.reqModel.ViewerModel;
import com.yellowseed.model.resModel.StoryDetailResponseModel;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.Themes;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;
import com.yellowseed.webservices.ApiRequest;
import com.yellowseed.webservices.ApiResponse;
import com.yellowseed.webservices.requests.Viewer;
import com.yellowseed.webservices.requests.ViewerRequest;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class StoryViewActivity extends BaseActivity {

    private ActivityStoryViewBinding binding;
    private Context context;
    private ArrayList<SendToModel> models;
    private String[] names = {"John Thomas", "Jenny Koul"};
    private int[] images = {R.mipmap.profile_icon3, R.mipmap.profile_icon};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_story_view);
        context = StoryViewActivity.this;
        initializedControl();
        setToolBar();
        setOnClickListener();

    }





    private ArrayList<SendToModel> prepareData() {

        ArrayList<SendToModel> data = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            SendToModel model = new SendToModel();
            model.setName_url(names[i]);
            model.setImage_url(images[i]);
            data.add(model);
        }
        return data;

    }



    private int getVotersType1(StoryDetailResponseModel.StoriesBean.PollBean pollBean, List<StoryDetailResponseModel.StoriesBean.PollBean> pollBeanList) {
        int count = 0;
        if (pollBeanList.size() > 0) {
            for (int i = 0; i < pollBeanList.size(); i++) {
                if (pollBean.getType1().equalsIgnoreCase(pollBeanList.get(i).getPoll_content())) {
                    count = count + 1;
                }
            }
        }
        return count;
    }

    private int getVotersType2(StoryDetailResponseModel.StoriesBean.PollBean pollBean, List<StoryDetailResponseModel.StoriesBean.PollBean> pollBeanList) {
        int count = 0;
        if (pollBeanList.size() > 0) {
            for (int i = 0; i < pollBeanList.size(); i++) {
                if (pollBean.getType2().equalsIgnoreCase(pollBeanList.get(i).getPoll_content())) {
                    count = count + 1;
                }
            }
        }
        return count;
    }

    @Override
    public void initializedControl() {
        setCurrentTheme();
        setRecyclerViewer();
        setRecyclerVoter();
    }

    private void setCurrentTheme() {
        binding.llMain.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        binding.storyViewToolBar.toolbarMain.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkTheme()));
        binding.storyViewToolBar.tvHeader.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        Themes.getInstance(context).changeIconColor(context, binding.storyViewToolBar.ivLeft);
        Themes.getInstance(context).changeIconColor(context, binding.storyViewToolBar.ivRight);
        binding.tvCountType1.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setTolbarText()));
        binding.tvCountType2.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setGreyHint()));
        binding.tvPollType2.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setGreyHint()));
        binding.tvPollType1.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkGreyTextColor()));
        binding.tvNumberViwers.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.tvNumberVoter.setTextColor(ContextCompat.getColor(context, Themes.getInstance(context).setDarkThemeText()));
        binding.llYes.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setYesLayoutColor()));
        binding.llNo.setBackgroundColor(ContextCompat.getColor(context, Themes.getInstance(context).setNoLayoutColor()));
    }

   /* public void setRecyclerViewer() {
        binding.rvViewers.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvViewers.setLayoutManager(layoutManager);

        adapterViewer = new DirectUserPendingAdapter(viewerModelList, this, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {


                }
            }
        });
        binding.rvViewers.setAdapter(adapterViewer);

    }


    public void setRecyclerVoter() {

        binding.rvVoters.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvVoters.setLayoutManager(layoutManager);

        adapterVoters = new StoryViewVotersAdapter(pollBeanList, this, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {

                }
            }
        });
        binding.rvVoters.setAdapter(adapterVoters);

    }
*/

    public void setRecyclerViewer() {

        binding.rvViewers.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        binding.rvViewers.setLayoutManager(layoutManager);
        models = new ArrayList<>();
        models.addAll(prepareData());
        DirectUserPendingAdapter adapter = new DirectUserPendingAdapter(models, context, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {


                }
            }
        });
        binding.rvViewers.setAdapter(adapter);

    }


    public void setRecyclerVoter() {

        binding.rvVoters.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvVoters.setLayoutManager(layoutManager);
        models = new ArrayList<>();
        models.addAll(prepareData());
        StoryViewVotersAdapter adapter = new StoryViewVotersAdapter(models, context, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {


                }
            }
        });
        binding.rvVoters.setAdapter(adapter);

    }

    @Override
    public void setToolBar() {
        binding.storyViewToolBar.ivLeft.setVisibility(View.VISIBLE);
        binding.storyViewToolBar.ivLeft.setImageResource(R.mipmap.back_icon);
        binding.storyViewToolBar.tvHeader.setVisibility(View.VISIBLE);
        binding.storyViewToolBar.tvHeader.setText(R.string.story_view);
        binding.storyViewToolBar.ivRight.setVisibility(View.VISIBLE);
        binding.storyViewToolBar.ivRight.setImageResource(R.mipmap.delete_icon);

    }

    @Override
    public void setOnClickListener() {
        binding.storyViewToolBar.ivLeft.setOnClickListener(this);
        binding.storyViewToolBar.ivRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ivLeft:
                onBackPressed();
                break;

            case R.id.ivRight:
                ToastUtils.showToastShort(context,"Story Deleted successfully");

              //  deleteDialog();
                break;
            default:
                break;
        }
    }

    private void deleteDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.delete_story);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ToastUtils.showToastShort(context,"Story Deleted successfully");
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


    private void callSuccessResponse(ApiResponse response, DialogInterface dialog) {
        dialog.dismiss();
        finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            EventBus.getDefault().unregister(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
