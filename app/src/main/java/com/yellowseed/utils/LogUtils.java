package com.yellowseed.utils;

import android.util.Log;


public class LogUtils {


    private static final boolean DEBUG = true;

    public static void errorLog(final String tag, String message) {
        if (DEBUG) {
            Log.d(tag, message);
        }
    }

    public static void LOGV(final String tag, String message) {
        if (DEBUG) {
            Log.v(tag, message);
        }
    }

    public static void LOGI(final String tag, String message) {
        if (DEBUG) {
            Log.i(tag, message);
        }
    }

    public static void LOGW(final String tag, String message) {
        if (DEBUG) {
            Log.w(tag, message);
        }
    }

    public static void LOGE(final String tag, String message) {
        if (DEBUG) {
            Log.e(tag, message);
        }
    }

}
