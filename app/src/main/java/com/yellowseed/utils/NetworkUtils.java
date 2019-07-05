package com.yellowseed.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class NetworkUtils {

    private static final int TYPE_WIFI = 1;
    private static final int TYPE_MOBILE = 2;
    private static final int TYPE_NOT_CONNECTED = 0;


    private NetworkUtils() {
        throw new Error("U will not able to instantiate it");
    }

    /**
     * To get information about current internet status
     */
    private static NetworkInfo getActiveNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm != null ? cm.getActiveNetworkInfo() : null;
    }


    /**
     * To get internet status
     */
    public static boolean isConnected(Context context) {
        NetworkInfo info = getActiveNetworkInfo(context);
        return info != null && info.isConnected();
    }


    private static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm != null ? cm.getActiveNetworkInfo() : null;

        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }


}
