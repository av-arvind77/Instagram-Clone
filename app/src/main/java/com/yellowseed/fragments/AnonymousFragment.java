package com.yellowseed.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yellowseed.R;
import com.yellowseed.activity.AnonymousSearchingActivity;
import com.yellowseed.databinding.FragmentAnonymousBinding;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseFragment;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.Themes;


public class AnonymousFragment extends BaseFragment implements View.OnClickListener {
    View view;
    private FragmentAnonymousBinding binding;
    private Context context;
    private String type = "";
    private Themes themes;
    private boolean darkThemeEnabled;
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override

        public void onReceive(Context context, Intent intent) {

            String data = intent.getStringExtra("key");
            setCurrentTheme();

        }

    };
    private LocalBroadcastManager lbm;

    public AnonymousFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        themes = new Themes(context);


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_anonymous, container, false);
        view = binding.getRoot();
        initializedControl();
        setToolBar();
        setOnClickListener();
        lbm = LocalBroadcastManager.getInstance(getActivity());
        lbm.registerReceiver(receiver, new IntentFilter("theme_change"));
        return view;
    }

    @Override
    public void initializedControl() {
        setCurrentTheme();

    }


    private void setCurrentTheme() {
        binding.tvSelect.setTextColor(ContextCompat.getColor(context, themes.setDarkGreyTextColor()));
        binding.tvChat.setTextColor(ContextCompat.getColor(context, themes.setDarkGreyTextColor()));
        binding.tvAudio.setTextColor(ContextCompat.getColor(context, themes.setDarkGreyTextColor()));
        binding.tvVideo.setTextColor(ContextCompat.getColor(context, themes.setDarkGreyTextColor()));
        binding.btnAnonymousConnectClick.setTextColor(ContextCompat.getColor(context, themes.setDarkThemeText()));
        darkThemeEnabled = CommonUtils.getPreferencesBoolean(context, AppConstants.DARK_THEME);

        if (darkThemeEnabled)
            binding.ivAnonymousChat.setImageResource(R.mipmap.w_message_deactive);
        else
            binding.ivAnonymousChat.setImageResource(R.mipmap.anonymous_chat_btn);

        if (darkThemeEnabled)
            binding.ivAnonymousAudio.setImageResource(R.mipmap.w_call_deactive);
        else
            binding.ivAnonymousAudio.setImageResource(R.mipmap.anonymous_call_btn);

        if (darkThemeEnabled)
            binding.ivAnonymousVideo.setImageResource(R.mipmap.w_video_deactive);
        else
            binding.ivAnonymousVideo.setImageResource(R.mipmap.anonymous_video_btn);
    }

    @Override
    public void setToolBar() {

    }

    @Override
    public void setOnClickListener() {
        binding.btnAnonymousConnectClick.setEnabled(false);
        binding.ivAnonymousChat.setOnClickListener(this);
        binding.ivAnonymousAudio.setOnClickListener(this);
        binding.ivAnonymousVideo.setOnClickListener(this);
        binding.btnAnonymousConnectClick.setOnClickListener(this);
    }

    private void anonymousConnect() {
        binding.ivAnonymousChat.setImageResource(R.mipmap.anonymous_chat_btn);
        binding.ivAnonymousAudio.setImageResource(R.mipmap.anonymous_call_btn);
        binding.ivAnonymousVideo.setImageResource(R.mipmap.anonymous_video_btn);
        if (darkThemeEnabled)
            binding.ivAnonymousChat.setImageResource(R.mipmap.w_message_deactive);
        else
            binding.ivAnonymousChat.setImageResource(R.mipmap.anonymous_chat_btn);


        if (darkThemeEnabled)
            binding.ivAnonymousAudio.setImageResource(R.mipmap.w_call_deactive);
        else
            binding.ivAnonymousAudio.setImageResource(R.mipmap.anonymous_call_btn);

        if (darkThemeEnabled)
            binding.ivAnonymousVideo.setImageResource(R.mipmap.w_video_deactive);
        else
            binding.ivAnonymousVideo.setImageResource(R.mipmap.anonymous_video_btn);

       /* binding.ivAnonymousChat.setImageResource(R.mipmap.anonymous_chat_btn);
         binding.ivAnonymousAudio.setImageResource(R.mipmap.anonymous_call_btn);
          binding.ivAnonymousVideo.setImageResource(R.mipmap.anonymous_video_btn);*/
        binding.btnAnonymousConnectClick.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_anonymous_chat:
                anonymousConnect();
                type = "chat";
                if (darkThemeEnabled)
                    binding.ivAnonymousChat.setImageResource(R.mipmap.messanger_active);
                else
                    binding.ivAnonymousChat.setImageResource(R.mipmap.anonymous_chat_btn_sel);
                binding.btnAnonymousConnectClick.setEnabled(true);
                binding.btnAnonymousConnectClick.setBackground(ContextCompat.getDrawable(context,R.drawable.bg_yellow_dark));

                break;
            case R.id.iv_anonymous_audio:
                anonymousConnect();
                type = "audio";
                if (darkThemeEnabled)
                    binding.ivAnonymousAudio.setImageResource(R.mipmap.call_active);
                else
                    binding.ivAnonymousAudio.setImageResource(R.mipmap.anonymous_call_btn_sel);
                binding.btnAnonymousConnectClick.setBackground(ContextCompat.getDrawable(context,R.drawable.bg_yellow_dark));

                binding.btnAnonymousConnectClick.setEnabled(true);
                break;
            case R.id.iv_anonymous_video:
                anonymousConnect();
                type = "video";
                if (darkThemeEnabled)
                    binding.ivAnonymousVideo.setImageResource(R.mipmap.video_active);
                else
                    binding.ivAnonymousVideo.setImageResource(R.mipmap.anonymous_video_btn_sel);
                binding.btnAnonymousConnectClick.setEnabled(true);
                binding.btnAnonymousConnectClick.setBackground(ContextCompat.getDrawable(context,R.drawable.bg_yellow_dark));

                break;

            case R.id.btn_AnonymousConnectClick:


                Intent intent = new Intent(context, AnonymousSearchingActivity.class);
                intent.putExtra("type", type);
                startActivity(intent);

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
