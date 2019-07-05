package com.yellowseed.fcm;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.yellowseed.R;
import com.yellowseed.activity.HomeActivity;
import com.yellowseed.activity.MyProfileActivity;
import com.yellowseed.activity.ShowPostItemsActivity;
import com.yellowseed.utils.AppConstants;
import com.yellowseed.utils.CommonUtils;

import org.json.JSONObject;

//import com.google.gson.Gson;


public class FireBaseMessageService extends FirebaseMessagingService {

    private static final String TAG = "MyFireBaseMsgService";
    private String notificationType;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.e(TAG, "RemotemessageMain" + remoteMessage);
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.e(TAG, "From: " + remoteMessage.getFrom());
        Log.e(TAG, "Remotemessagedata" + remoteMessage.getData());

        if(remoteMessage.getData()!=null) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Gson gson = new Gson();
            JSONObject jsonObject = new JSONObject(remoteMessage.getData());
            FCMResponse fcmResponse = gson.fromJson(jsonObject.toString(), FCMResponse.class);

       sendNotification(fcmResponse);

        }
    }

    private void sendNotification(FCMResponse fcmResponse) {
        Intent intent = null;
        PendingIntent pendingIntent=null;
        if (fcmResponse != null) {

            Bitmap notifyImage = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            String CHANNEL_ID = "my_channel_01";

            if(fcmResponse.getNotification_type()!=null){
                switch (fcmResponse.getNotification_type()){
                    case "post_like":
                    case "post_tag":
                    case "comment_like":
                    case "comment_create":
                        intent = new Intent(this, ShowPostItemsActivity.class);
                        intent.putExtra("post_id",fcmResponse.getId());
                        pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
                        break;

                    case "accept_request":
                    case "follow_create":
                        intent = new Intent(this, MyProfileActivity.class);
                        intent.putExtra("user_id",fcmResponse.getId());
                        pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
                        break;
                    default:
                        intent = new Intent(this, HomeActivity.class);
                        pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);

                        break;
                }
            }


            int notificationId = CommonUtils.getPreferencesInt(this, AppConstants.NOTIFICATION_ID);

            Notification notification;
            if (android.os.Build.VERSION.SDK_INT >= 26) {
                //This only needs to be run on Devices on Android O and above
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                String id = "YOUR_CHANNEL_ID";
                CharSequence name = "YOUR_CHANNEL NAME"; //user visible
                String description = "YOUR_CHANNEL_DESCRIPTION"; //user visible
                int importance = NotificationManager.IMPORTANCE_MAX;
                @SuppressLint("WrongConstant")
                NotificationChannel mChannel = new NotificationChannel(id, name, importance);
                mChannel.setDescription(description);
                mChannel.enableLights(true);
                mChannel.setLightColor(R.color.colorPrimary);
                mChannel.enableVibration(true);
                mChannel.canShowBadge();
                mChannel.setShowBadge(true);
                mChannel.setVibrationPattern(new long[]{0, 1000});
                mNotificationManager.createNotificationChannel(mChannel);
                notification = new Notification.Builder(getApplicationContext(), "YOUR_CHANNEL_ID")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(getString(R.string.app_name))
                        .setTicker(getString(R.string.app_name))
                        .setLargeIcon(notifyImage)
                        .setContentText(fcmResponse.getBody())
                        .setAutoCancel(true)
                        // .setLargeIcon(Bitmap.createScaledBitmap(notifyImage, 128, 128, false))
                        .setContentIntent(pendingIntent)
                        .setOngoing(false).build();
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (notificationManager != null) {
                    notificationManager.notify(notificationId, notification);
                    CommonUtils.savePreferencesInt(this, AppConstants.NOTIFICATION_ID, notificationId+1);
                }
            } else {
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(getString(R.string.app_name))
                        .setLargeIcon(notifyImage)
                        .setContentText(fcmResponse.getBody())
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(fcmResponse.getBody()))
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setPriority(NotificationManager.IMPORTANCE_HIGH)
                        .setColor(getResources().getColor(R.color.colorPrimary))
                        .setAutoCancel(true);
                notificationBuilder.setContentIntent(pendingIntent);

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (notificationManager != null) {
                    notificationManager.notify(notificationId, notificationBuilder.build());
                    CommonUtils.savePreferencesInt(this, AppConstants.NOTIFICATION_ID, notificationId+1);
                }
            }
        }
    }
}



