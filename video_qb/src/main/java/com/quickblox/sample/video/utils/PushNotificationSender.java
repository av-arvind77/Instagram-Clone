package com.quickblox.sample.video.utils;

import com.quickblox.sample.video.R;


import java.util.ArrayList;

/**
 * Created by tereha on 13.05.16.
 */
public class PushNotificationSender {

    public static void sendPushMessage(ArrayList<Integer> recipients, String senderName) {
        String outMessage = String.format(String.valueOf(R.string.text_push_notification_message), senderName);

        // Send Push: create QuickBlox Push Notification Event
       /* QBEvent qbEvent = new QBEvent();
        qbEvent.setNotificationType(QBNotificationType.PUSH);
        qbEvent.setEnvironment(QBEnvironment.DEVELOPMENT);
        // Generic push - will be delivered to all platforms (Android, iOS, WP, Blackberry..)
        qbEvent.setMessage(outMessage);

        StringifyArrayList<Integer> userIds = new StringifyArrayList<>(recipients);
        qbEvent.setUserIds(userIds);

        QBPushNotifications.createEvent(qbEvent).performAsync(null);*/
    }
}
