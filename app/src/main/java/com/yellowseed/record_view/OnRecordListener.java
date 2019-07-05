package com.yellowseed.record_view;

/**
 * Created by Devlomi on 24/08/2017.
 */

public interface OnRecordListener {
    void onStart();
    void onCancel();
    void onFinish(long recordTime);
    void onLessThanSecond();
}
