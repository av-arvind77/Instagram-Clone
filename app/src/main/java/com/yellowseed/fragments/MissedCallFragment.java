package com.yellowseed.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yellowseed.R;
import com.yellowseed.activity.CallDetailsActivity;
import com.yellowseed.adapter.CallHistoryAdapter;
import com.yellowseed.customView.CustomRobotoBoldTextView;
import com.yellowseed.customView.CustomRobotoRegularTextView2;
import com.yellowseed.databinding.FragmentMissedCallBinding;
import com.yellowseed.listener.OnItemClickListener;
import com.yellowseed.listener.OnItemLongClickListener;
import com.yellowseed.model.CallHistoryModel;
import com.yellowseed.utils.BaseFragment;
import com.yellowseed.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class MissedCallFragment extends BaseFragment implements View.OnClickListener {

    private Context context;
    private FragmentMissedCallBinding binding;
    private int flag = 1;
    private boolean isSelected;
    private boolean isLongClick;

    private CallHistoryAdapter adapter;
    private List<CallHistoryModel> alCallHistory = new ArrayList<>();


    private String[] callerName = {"Raj", "Mob", "Jay", "Ris"};
    private String[] callType = {"Missed", "Missed", "Missed", "Missed"};
    private String[] day = {"yesterday", "Today", "Tuesday", "26/10/18"};
    private String[] time = {"2h ago", "4h ago", "8h ago", "10h ago"};
    private int[] userImage = {R.mipmap.profile_icon, R.mipmap.profile_icon, R.mipmap.profile_icon, R.mipmap.profile_icon};
    private int[] calltypeImg = {R.mipmap.video_call_white, R.mipmap.call_white, R.mipmap.video_call_white, R.mipmap.call_white};
    private ImageView ivRight;
    private ImageView ivRightSecond, ivLeft;

    private CustomRobotoBoldTextView tvUserImage;

    private CustomRobotoRegularTextView2 tvLeftText;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_missed_call, container, false);
        initializedControl();
        return binding.getRoot();
    }

    @Override
    public void initializedControl() {
        ivRight = (ImageView) getActivity().findViewById(R.id.ivRight);
        ivRightSecond = (ImageView) getActivity().findViewById(R.id.ivRightSecond);
        ivLeft = (ImageView) getActivity().findViewById(R.id.ivLeft);

        tvLeftText = (CustomRobotoRegularTextView2) getActivity().findViewById(R.id.tvLeftText);
        tvUserImage = (CustomRobotoBoldTextView) getActivity().findViewById(R.id.tvUserImage);

        setToolBar();
        setOnClickListener();
        setRecycler();
    }


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
                        if (((CallDetailsActivity)getContext()).isSingleSelection) {

                            if (alCallHistory.get(position).isSelected()) {

                            alCallHistory.get(position).setSelected(false);
                        } else {
                            alCallHistory.get(position).setSelected(true);
                        }
                        adapter.notifyItemChanged(position);

                         if (isLongClick=true) {

                             for (int i = 0; i < alCallHistory.size(); i++) {

                                 if (alCallHistory.get(i).isSelected()) {
                                     ((CallDetailsActivity) getContext()).changeIcon(true);
                                     break;
                                 } else {
                                     ((CallDetailsActivity) getContext()).changeIcon(false);
                                 }
                             }
                         }
                         }

                        break;

                    case R.id.llSwipe:
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
                if (alCallHistory.get(position).isSelected())
                {

                    alCallHistory.get(position).setSelected(false);

                } else
                {
                    alCallHistory.get(position).setSelected(true);

                }
                adapter.notifyItemChanged(position);

                for (int i = 0; i < alCallHistory.size(); i++) {

                    if (alCallHistory.get(i).isSelected()) {
                        ((CallDetailsActivity) getContext()).changeIcon(true);
                        break;
                    } else {

                        ((CallDetailsActivity) getContext()).changeIcon(false);
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
            model.setDay(day[i]);
            model.setHours(time[i]);
            model.setUserImage(userImage[i]);
            model.setCalltypeImg(calltypeImg[i]);
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


    @Override
    public void setToolBar() {

    }

    @Override
    public void setOnClickListener() {

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }

}




