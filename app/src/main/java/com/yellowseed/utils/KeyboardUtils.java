package com.yellowseed.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


public class KeyboardUtils {

    private KeyboardUtils() {
        throw new Error("U will not able to instantiate it");
    }

    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager in = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            View view = activity.findViewById(android.R.id.content);
            assert in != null;
            in.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Throwable ignored) {
        }

    }
}
