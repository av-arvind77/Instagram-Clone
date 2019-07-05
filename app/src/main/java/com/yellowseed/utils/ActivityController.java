package com.yellowseed.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;


public class ActivityController {


    /**
     * Start activity.
     *
     * @param context the context
     * @param clazz   the clazz
     */
    public static void startActivity(Context context, Class clazz) {
        Intent intent = new Intent(context, clazz);
        context.startActivity(intent);
    }

    /**
     * Start activity.
     *
     * @param context the context
     * @param intent  the intent
     */
    public static void startActivity(Context context, Intent intent) {
        context.startActivity(intent);
    }

    /**
     * Start activity.
     *
     * @param activity        the activity
     * @param clazz           the clazz
     * @param isClearPrevious the is clear previous
     */
    public static void startActivity(Activity activity, Class clazz, boolean isClearPrevious) {
        Intent intent = new Intent(activity, clazz);
        activity.startActivity(intent);
        if (isClearPrevious)
            activity.finish();
    }


    /**
     * Start activity.
     *
     * @param activity the activity
     * @param clazz    the clazz
     * @param bundle   the bundle
     */
    public static void startActivity(Activity activity, Class clazz, Bundle bundle) {
        Intent intent = new Intent(activity, clazz);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    /**
     * Start activity.
     *
     * @param activity        the activity
     * @param clazz           the clazz
     * @param bundle          the bundle
     * @param isClearPrevious the is clear previous
     */
    public static void startActivity(Activity activity, Class clazz, Bundle bundle, boolean isClearPrevious) {
        Intent intent = new Intent(activity, clazz);
        intent.putExtras(bundle);
        activity.startActivity(intent);
        if (isClearPrevious)
            activity.finish();
    }


    /**
     * Start activity.
     *
     * @param activity        the activity
     * @param clazz           the clazz
     * @param isClearPrevious the is clear previous
     * @param isClearStack    the is clear stack
     */
    public static void startActivity(Activity activity, Class clazz, boolean isClearPrevious, boolean isClearStack) {
        Intent intent = new Intent(activity, clazz);
        activity.startActivity(intent);
        if (isClearStack && isClearPrevious || isClearStack)
            ActivityCompat.finishAffinity(activity);
        if (isClearPrevious)
            activity.finish();
    }

    /**
     * Start activity for result.
     *
     * @param activity    the activity
     * @param clazz       the clazz
     * @param requestCode the request code
     */
    public static void startActivityForResult(Activity activity, Class clazz, int requestCode) {
        Intent intent = new Intent(activity, clazz);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * Start activity for result.
     *
     * @param activity    the activity
     * @param clazz       the clazz
     * @param bundle      the bundle
     * @param requestCode the request code
     */
    public static void startActivityForResult(Activity activity, Class clazz, Bundle bundle, int requestCode) {
        Intent intent = new Intent(activity, clazz);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * Start activity.
     *
     * @param activity        the activity
     * @param clazz           the clazz
     * @param bundle          the bundle
     * @param isClearPrevious the is clear previous
     * @param isClearStack    the is clear stack
     */
    public static void startActivity(Activity activity, Class clazz, Bundle bundle, boolean isClearPrevious, boolean isClearStack) {
        Intent intent = new Intent(activity, clazz);
        intent.putExtras(bundle);
        activity.startActivity(intent);
        if (isClearStack && isClearPrevious || isClearStack)
            ActivityCompat.finishAffinity(activity);
        if (isClearPrevious)
            activity.finish();
    }


    /**
     * Start activity for result.
     *
     * @param activity          the activity
     * @param clazz             the clazz
     * @param bundle            the bundle
     * @param requestCode       the request code
     * @param isEnableAnimation the is enable animation
     */
    public static void startActivityForResult(AppCompatActivity activity, Class clazz, Bundle bundle, int requestCode, boolean isEnableAnimation) {
        Intent intent = new Intent(activity, clazz);
        intent.putExtras(bundle);
        if (!isEnableAnimation) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        }
        activity.startActivityForResult(intent, requestCode);
    }

}
