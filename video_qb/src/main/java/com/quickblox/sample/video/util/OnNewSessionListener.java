package com.quickblox.sample.video.util;

import com.quickblox.videochat.webrtc.QBRTCSession;

/**
 * Created by ashutosh.kumar on 10/5/17.
 */

public interface OnNewSessionListener {

    void onNewSession(QBRTCSession session);
}
