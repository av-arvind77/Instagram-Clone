package com.yellowseed.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wowza.gocoder.sdk.api.WowzaGoCoder;
import com.wowza.gocoder.sdk.api.broadcast.WZBroadcast;
import com.wowza.gocoder.sdk.api.broadcast.WZBroadcastConfig;
import com.wowza.gocoder.sdk.api.configuration.WZMediaConfig;
import com.wowza.gocoder.sdk.api.devices.WZAudioDevice;
import com.wowza.gocoder.sdk.api.devices.WZCamera;
import com.wowza.gocoder.sdk.api.devices.WZCameraView;
import com.wowza.gocoder.sdk.api.errors.WZError;
import com.wowza.gocoder.sdk.api.errors.WZStreamingError;
import com.wowza.gocoder.sdk.api.status.WZState;
import com.wowza.gocoder.sdk.api.status.WZStatus;
import com.wowza.gocoder.sdk.api.status.WZStatusCallback;
import com.yellowseed.R;
import com.yellowseed.adapter.GoLiveChatAdapter;
import com.yellowseed.databinding.ActivityGoLiveBinding;
import com.yellowseed.model.reqModel.UserModel;
import com.yellowseed.model.resModel.GetChatResonse;
import com.yellowseed.model.resModel.StartedMessageListResponse;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.BaseActivity;
import com.yellowseed.utils.CommonUtils;
import com.yellowseed.utils.DialogUtils;
import com.yellowseed.utils.PrefManager;
import com.yellowseed.utils.ToastUtils;
import com.yellowseed.webservices.ApiExecutor;
import com.yellowseed.webservices.ApiRequest;
import com.yellowseed.webservices.ApiResponse;
import com.yellowseed.webservices.ServiceConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.gusavila92.websocketclient.WebSocketClient;


/**
 * Created by Ashutosh Kumar on 18/11/2017.
 */

public class GoLiveActivity extends BaseActivity implements WZStatusCallback, View.OnClickListener {

    public static final String HOST_ADDRESS = "38.100.110.116";
    public static final int PORT_NUMBER = 1935;
    private static final String APPLICATION_KEY = "GOSK-6E45-010F-653E-3059-7C08";  //PAID License Key
    private static final String APPLICATION_NAME = "live";
    private static final String USER_NAME = "shah";
    private static final String USER_PASSWORD = "12345678";
    // Properties needed for Android 6+ permissions handling
    private static final int PERMISSIONS_REQUEST_CODE = 0x1;
    private ActivityGoLiveBinding binding;
    private Context context;
    // The top level GoCoder API interface
    private WowzaGoCoder goCoder;
    // The GoCoder SDK camera view
    private WZCameraView goCoderCameraView;
    // The GoCoder SDK broadcaster
    private WZBroadcast goCoderBroadcaster;
    // The broadcast configuration settings
    private WZBroadcastConfig goCoderBroadcastConfig;
    private boolean mPermissionsGranted = true;
    private String[] mRequiredPermissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };
    private boolean isStarted;
    private GoLiveChatAdapter adapter;
    private List<GetChatResonse.UserInfoBean> chatMessageList;
    private WebSocketClient webSocketClient;
    private String roomId = "", liveId = "", liveUserId = "";

    //
    // Utility method to check the status of a permissions request for an array of permission identifiers
    //
    private static boolean hasPermissions(Context context, String[] permissions) {
        for (String permission : permissions)
            if (context.checkCallingOrSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                return false;

        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_go_live);
        context = GoLiveActivity.this;

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakelockTag");
        //   goLiveApi("connect");

        wakeLock.acquire();
        initializedControl();
       // initializedWowzaConnection();

       // createWebSocketClient();
        setAdapter();

        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (goCoder != null) {
                    goCoder.endStreaming();
                }
                finish();
                //   goLiveApi("leave");

            }
        });
    }

    private void goLiveApi(final String type) {
        if (CommonUtils.isOnline(context)) {
            final Dialog progressDialog = DialogUtils.customProgressDialog(context);
            progressDialog.show();
            JsonObject jsonObject = new JsonObject();
            if (type.equalsIgnoreCase("connect")) {
                jsonObject.addProperty("connect", "connect");
            } else {
                jsonObject.addProperty("connect", "leave");
                jsonObject.addProperty("live_id", liveId);
            }
            Call<ApiResponse> call = ApiExecutor.getClient(context).apiGoLive(jsonObject);
            call.enqueue(new retrofit2.Callback<ApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse> call, @NonNull final Response<ApiResponse> response) {
                    progressDialog.dismiss();
                    try {
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (response != null && response.body() != null) {
                        if (response.body().getResponseCode() == AppConstants.SUCCESS) {
                            roomId = response.body().getRoom().getId();
                            liveId = response.body().getLive().getId();
                            liveUserId = response.body().getRoom().getLive_user_id();
                            if (type.equalsIgnoreCase("leave")) {
                                finish();
                            }
                        }
                    } else {
                        ToastUtils.showToastShort(context, context.getString(R.string.server_error_msg));
                        finish();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    ToastUtils.showToastShort(context, context.getString(R.string.server_error_msg));
                    finish();
                }
            });
        } else {
            ToastUtils.showToastShort(context, context.getString(R.string.internet_alert_msg));
            finish();
        }
    }

    @Override
    public void initializedControl() {
        context = this;
        isStarted = false;
        chatMessageList = new ArrayList<>();
        binding.tvLiveStatus.setOnClickListener(this);
        binding.ivSendStoryMessage.setOnClickListener(this);
        if (getIntent().getStringExtra(AppConstants.USER_ID).equalsIgnoreCase(new PrefManager(context).getUserId())) {
            binding.llEditView.setVisibility(View.VISIBLE);
            binding.ivCamera.setVisibility(View.VISIBLE);
        } else {
            binding.llEditView.setVisibility(View.VISIBLE);
            binding.ivCamera.setVisibility(View.GONE);
        }
        goCoder = WowzaGoCoder.init(getApplicationContext(), APPLICATION_KEY);

        if (goCoder == null) {
            ToastUtils.showToastShort(context, getString(R.string.something_went_wrong));
            return;
        }
        // Associate the WZCameraView defined in the U/I layout with the corresponding class member
        goCoderCameraView = (WZCameraView) findViewById(R.id.camera_preview);
        ImageView ivCamera = findViewById(R.id.ivCamera);
//        ToggleButton tbMic = findViewById(R.id.toggle_mic);

        // Create an audio device instance for capturing and broadcasting audio
        WZAudioDevice goCoderAudioDevice = new WZAudioDevice();

        // Create a broadcaster instance
        goCoderBroadcaster = new WZBroadcast();

        // Create a configuration instance for the broadcaster
        goCoderBroadcastConfig = new WZBroadcastConfig(WZMediaConfig.FRAME_SIZE_1920x1080);

        // Enable the switch camera button if more than one camera is available
        int numCameras = WZCamera.getNumberOfDeviceCameras();
        ivCamera.setEnabled(numCameras > 1);

        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set the active camera to the front camera if it is not already active
//                if (!goCoderCameraView.getCamera().isFront())
//                    goCoderCameraView.switchCamera();
                // Set the active camera to the front camera if it is not already active
                goCoderCameraView.switchCamera();

            }
        });
        // Mute the audio stream if it isn't already
       /* tbMic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    if (!goCoderBroadcaster.getBroadcastConfig().getAudioBroadcaster().isAudioEnabled()) {
                        goCoderBroadcaster.getBroadcastConfig().getAudioBroadcaster().setAudioEnabled(true);
                    }
                } else {
                    if (goCoderBroadcaster.getBroadcastConfig().getAudioBroadcaster().isAudioEnabled()) {
                        goCoderBroadcaster.getBroadcastConfig().getAudioBroadcaster().setAudioEnabled(false);
                    }
                }
            }
        });*/


        // Set the active camera to the front camera if it is not already active
        if (goCoderCameraView.getCamera() != null && !goCoderCameraView.getCamera().isFront())
            goCoderCameraView.switchCamera();


        // Set the connection properties for the target Wowza Streaming Engine server or Wowza Cloud account
        goCoderBroadcastConfig.setHostAddress(HOST_ADDRESS);
        goCoderBroadcastConfig.setPortNumber(PORT_NUMBER);
        goCoderBroadcastConfig.setApplicationName(APPLICATION_NAME);
        goCoderBroadcastConfig.setStreamName(getIntent().getStringExtra(AppConstants.USER_ID));
        goCoderBroadcastConfig.setUsername(USER_NAME);
        goCoderBroadcastConfig.setPassword(USER_PASSWORD);

        // Designate the camera preview as the video broadcaster
        goCoderBroadcastConfig.setVideoBroadcaster(goCoderCameraView);

        // Designate the audio device as the audio broadcaster
        goCoderBroadcastConfig.setAudioBroadcaster(goCoderAudioDevice);


    }

    @Override
    public void setToolBar() {

    }

    @Override
    public void setOnClickListener() {

    }

    //
    // Called when an activity is brought to the foreground
    //
    @Override
    protected void onResume() {
        super.onResume();

        // If running on Android 6 (Marshmallow) or above, check to see if the necessary permissions
        // have been granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mPermissionsGranted = hasPermissions(this, mRequiredPermissions);
            if (!mPermissionsGranted)
                ActivityCompat.requestPermissions(this, mRequiredPermissions, PERMISSIONS_REQUEST_CODE);
        } else
            mPermissionsGranted = true;

        // Start the camera preview display
        if (mPermissionsGranted && goCoderCameraView != null) {
            if (goCoderCameraView.isPreviewPaused())
                goCoderCameraView.onResume();
            else
                goCoderCameraView.startPreview();
        }

    }

    private void setColorLiveStatus(boolean isOnline) {
        if (isOnline)
            binding.tvLiveStatus.setBackground(CommonUtils.getDrawable(context, R.drawable.rectangle_red_round));
        else
            binding.tvLiveStatus.setBackground(CommonUtils.getDrawable(context, R.drawable.rectangle_green_round));
    }

    //
    // Callback invoked in response to a call to ActivityCompat.requestPermissions() to interpret
    // the results of the permissions request
    //
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        mPermissionsGranted = true;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE: {
                // Check the result of each permission granted
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        mPermissionsGranted = false;
                    }
                }
            }
        }
    }

    //
    // The callback invoked when the broadcast button is pressed
    //
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tvLiveStatus:
                if (!isStarted) {
                    initializedWowzaConnection();
                } else {
                    isStarted = false;
//                    goCoderBroadcaster.endBroadcast(this);
                    setColorLiveStatus(false);
                }
                break;

            case R.id.ivSendStoryMessage:
                if (binding.etChatMessage.getText().toString().trim().length() > 0) {
                    GetChatResonse.UserInfoBean chatScreenModel = new GetChatResonse.UserInfoBean();
                    chatScreenModel.setReply_message(null);
                    chatScreenModel.setCreated_at(String.valueOf(System.currentTimeMillis() / 1000));
                    chatScreenModel.setRoom_id(roomId);
                    chatScreenModel.setCreated_timestamp(String.valueOf(System.currentTimeMillis() / 1000));
                    chatScreenModel.setId("");
                    chatScreenModel.setIs_user_sender(true);
                    chatScreenModel.setIs_stared(false);
                    chatScreenModel.setLocal_message_id(String.valueOf(System.currentTimeMillis() / 1000));
                    chatScreenModel.setRead_status(false);
                    chatScreenModel.setReceiver_image("");
                    chatScreenModel.setBody(binding.etChatMessage.getText().toString().trim());
                    chatScreenModel.setSender_image(new PrefManager(context).getUserPic());
                    chatScreenModel.setSender_id(new PrefManager(context).getUserId());
                    sendMessage(chatScreenModel);
                    binding.etChatMessage.setText("");
                } else {
                    CommonUtils.showToast(context, "Please enter message.");
                }

                break;
        }
    }

    private void initializedWowzaConnection() {
        isStarted = true;
        if (!mPermissionsGranted) return;

        // Ensure the minimum set of configuration settings have been specified necessary to
        // initiate a broadcast streaming session
        try {
            WZStreamingError configValidationError = goCoderBroadcastConfig.validateForBroadcast();

            if (configValidationError != null) {
                ToastUtils.showToastShort(context, getString(R.string.something_went_wrong));
//                Toast.makeText(this, configValidationError.getErrorDescription(), Toast.LENGTH_LONG).show();
                setColorLiveStatus(false);
            } else if (goCoderBroadcaster.getStatus().isRunning()) {
                // Stop the broadcast that is currently running
                goCoderBroadcaster.endBroadcast(this);
                setColorLiveStatus(false);
            } else {
                setColorLiveStatus(true);
                // Start streaming
                goCoderBroadcaster.startBroadcast(goCoderBroadcastConfig, this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //
    // The callback invoked upon changes to the state of the steaming broadcast
    //
    @Override
    public void onWZStatus(final WZStatus goCoderStatus) {
        // A successful status transition has been reported by the GoCoder SDK
        final StringBuffer statusMessage = new StringBuffer("Broadcast status: ");

        switch (goCoderStatus.getState()) {
            case WZState.STARTING:
                statusMessage.append("Broadcast initialization");
                break;

            case WZState.READY:
                statusMessage.append("Ready to begin streaming");
                break;

            case WZState.RUNNING:
                statusMessage.append("Streaming is active");
                break;

            case WZState.STOPPING:
                statusMessage.append("Broadcast shutting down");
                break;

            case WZState.IDLE:
                statusMessage.append("The broadcast is stopped");
                break;

            default:
                return;
        }

        /*// Display the status message using the U/I thread
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(GoLiveActivity.this, statusMessage, Toast.LENGTH_LONG).show();
            }
        });*/
    }

    //
    // The callback invoked when an error occurs during a broadcast
    //
    @Override
    public void onWZError(final WZStatus goCoderStatus) {
        // If an error is reported by the GoCoder SDK, display a message
        // containing the error details using the U/I thread
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                setColorLiveStatus(false);
                ToastUtils.showToastShort(context, getString(R.string.something_went_wrong));
//                Toast.makeText(GoLiveActivity.this,
//                        "Streaming error: " + goCoderStatus.getLastError().getErrorDescription(),
//                        Toast.LENGTH_LONG).show();
            }
        });
    }

    //
    // Enable Android's sticky immersive full-screen mode
    //
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        if (rootView != null)
            rootView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private void setAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setStackFromEnd(true);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new GoLiveChatAdapter(this, chatMessageList);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setAdapter(adapter);
//        linearLayoutManager.scrollToPosition(chatMessageList.size());
    }

    private boolean isLastVisible() {
        LinearLayoutManager layoutManager = ((LinearLayoutManager) binding.recyclerView.getLayoutManager());
        int totalItemCount = layoutManager.getItemCount();
        int lastVisible = layoutManager.findLastVisibleItemPosition();
        boolean endHasBeenReached = lastVisible + 2 >= totalItemCount;
        return endHasBeenReached;
    }

    private void createWebSocketClient() {
        URI uri;
        try {
            uri = new URI(ServiceConstant.WEB_SOCKET_URL_ANONYMOUS + "?id=" + new PrefManager(context).getUserId() + "&type=" + "");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {

            }

            @Override
            public void onTextReceived(final String message) {
                if (message != null) {
                    new Handler(getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            GetChatResonse.UserInfoBean chatScreenModel = new Gson().fromJson(message, GetChatResonse.UserInfoBean.class);
                            adapter.add(chatScreenModel);
                            if (isLastVisible()) {
                                binding.recyclerView.smoothScrollToPosition(adapter.getItemCount());
                            }
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }

            @Override
            public void onBinaryReceived(byte[] data) {
                System.out.println("onBinaryReceived");
            }

            @Override
            public void onPingReceived(byte[] data) {
                System.out.println("onPingReceived");
            }

            @Override
            public void onPongReceived(byte[] data) {
                System.out.println("onPongReceived");
            }

            @Override
            public void onException(Exception e) {
                System.out.println(e.getMessage());
            }

            @Override
            public void onCloseReceived() {
                System.out.println("onCloseReceived");
            }
        };

        webSocketClient.setConnectTimeout(10000);
//        webSocketClient.setReadTimeout(60000);
//        webSocketClient.addHeader("Origin", "http://developer.example.com");
        webSocketClient.enableAutomaticReconnection(5000);
        webSocketClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (goCoderBroadcaster != null && goCoderBroadcaster.getStatus().isRunning()) {
            // Stop the broadcast that is currently running
            goCoderBroadcaster.endBroadcast(this);
            setColorLiveStatus(false);
        }
        if (webSocketClient != null)
            webSocketClient.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (goCoderBroadcaster!=null&&goCoderBroadcaster.getStatus().isRunning()) {
            // Stop the broadcast that is currently running
            goCoderBroadcaster.endBroadcast(this);
            setColorLiveStatus(false);
        }
        if (webSocketClient != null)
            webSocketClient.close();
    }

    public void sendMessage(GetChatResonse.UserInfoBean chatScreenModel) {
        JsonObject object = new JsonObject();
        object.addProperty("local_message_id", "");
        object.addProperty("sender_id", new PrefManager(context).getUserId());
        object.addProperty("room_id", roomId);
        object.addProperty("is_group", "");
        object.addProperty("body", chatScreenModel.getBody());
        object.addProperty("assoc_id", liveUserId);
        object.addProperty("message_type", "");
        object.addProperty("created_at", chatScreenModel.getCreated_at());
        object.addProperty("upload_type", chatScreenModel.getUpload_type());
        object.addProperty("type", "Joined");
        if (webSocketClient != null)
            webSocketClient.send(new Gson().toJson(object).toString());
    }
}
