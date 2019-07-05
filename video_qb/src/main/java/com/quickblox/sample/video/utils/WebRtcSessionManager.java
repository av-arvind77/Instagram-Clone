package com.quickblox.sample.video.utils;

import android.content.Context;
import android.util.Log;


import com.quickblox.sample.video.util.OnNewSessionListener;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.callbacks.QBRTCClientSessionCallbacksImpl;

/**
 * Created by tereha on 16.05.16.
 */
public class WebRtcSessionManager extends QBRTCClientSessionCallbacksImpl {
    private static final String TAG = WebRtcSessionManager.class.getSimpleName();

    private static WebRtcSessionManager instance;
    private Context context;

    private static QBRTCSession currentSession;
    private OnNewSessionListener onNewSessionListener;

    public WebRtcSessionManager(Context context) {
        this.context = context;
    }

    public static WebRtcSessionManager getInstance(Context context){
        if (instance == null){
            instance = new WebRtcSessionManager(context);
        }

        return instance;
    }

    public QBRTCSession getCurrentSession() {
        return currentSession;
    }

    public void setCurrentSession(QBRTCSession qbCurrentSession) {
        currentSession = qbCurrentSession;
    }

    @Override
    public void onReceiveNewSession(QBRTCSession session) {
        Log.d(TAG, "onReceiveNewSession to WebRtcSessionManager");
//        currentSession = session;

        if(onNewSessionListener!=null){
            setCurrentSession(session);
            onNewSessionListener.onNewSession(session);
        }
      /*  if (currentSession == null){
            setCurrentSession(session);
            OpponentsActivity.start(context, true);
        }*/
    }

    @Override
    public void onSessionClosed(QBRTCSession session) {
        Log.d(TAG, "onSessionClosed WebRtcSessionManager");

        if (session.equals(getCurrentSession())){
            setCurrentSession(null);
        }


    }

    public void setOnNewSessionListener(OnNewSessionListener onNewSessionListener){
        this.onNewSessionListener = onNewSessionListener;
    }
}
