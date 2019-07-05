package com.quickblox.sample.video;

import com.quickblox.sample.core.CoreApp;
import com.quickblox.sample.video.util.QBResRequestExecutor;

public class VideoApp extends CoreApp {
    private static VideoApp instance;
    private QBResRequestExecutor qbResRequestExecutor;

    public static VideoApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initApplication();
    }

    private void initApplication(){
        instance = this;
    }

    public synchronized QBResRequestExecutor getQbResRequestExecutor() {
        return qbResRequestExecutor == null
                ? qbResRequestExecutor = new QBResRequestExecutor()
                : qbResRequestExecutor;
    }
}
