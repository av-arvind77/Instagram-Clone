package com.yellowseed.quickblox.call;

import android.content.Context;

import com.quickblox.sample.video.utils.WebRtcSessionManager;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.yellowseed.activity.HomeActivity;

/**
 * Created by admin on 09-05-2017.
 */

public class VideoCallSessionManager extends WebRtcSessionManager {
    //  private static VideoCallSessionManager instance;
    Context context;

    private VideoCallSessionManager(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public QBRTCSession getCurrentSession() {
        return super.getCurrentSession();
    }


    @Override
    public void setCurrentSession(QBRTCSession qbCurrentSession) {
        super.setCurrentSession(qbCurrentSession);
    }

    @Override
    public void onReceiveNewSession(QBRTCSession session) {
        super.onReceiveNewSession(session);
        setCurrentSession(session);
         HomeActivity.start(context, true);

    }

    @Override
    public void onSessionClosed(QBRTCSession session) {
        super.onSessionClosed(session);
    }
}
