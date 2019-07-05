package com.yellowseed.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;


public class CheckPermission {
    public static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    public static final int REQUEST_CODE_LOCATION = 1;
    public static final int REQUEST_LOCATION_PERMISSION = 444;
    public static final int PERMISSION_REQUEST_CODE = 100;

    public static boolean checkIsMarshMallowVersion() {
        int sdkVersion = Build.VERSION.SDK_INT;
        return sdkVersion >= Build.VERSION_CODES.M;

    }

    public static void requestCallPhonePermission(Activity activity, int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CALL_PHONE)) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        }
    }

    /**
     * Used to check whether Contact permission is provided or not.
     */
    public static boolean checkCallPhonePermission(Context mContext) {
        int result = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Used to check whether SMS permission is provided or not.
     */
    public static boolean checkSMSPermission(Context mContext) {
        int result = ContextCompat.checkSelfPermission(mContext, Manifest.permission.SEND_SMS);
        return result == PackageManager.PERMISSION_GRANTED;
    }


    public static boolean checkCameraPermission(Context mContext) {
        int result = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;

    }

    public static boolean checkContactsPermission(Context mContext) {
        int result = ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_CONTACTS);
        return result == PackageManager.PERMISSION_GRANTED;

    }

    public static void requestCameraPermission(Activity activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA}, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA}, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);

        }
    }

    public static void requestContactsPermission(Activity activity, int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_CONTACTS)) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        }
    }

    public static boolean checkLocationPermission(Context mContext) {
        int result = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;

    }

    public static boolean checkPermissionStorage(Activity context) {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean checkPermissionCamera(Activity context) {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean checkLocationPermission(Activity context) {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        return (result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED);
    }

    public static void requestLocationPermission(Activity activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        }
    }

    public static void requestPermissionStorage(Activity activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    public static void requestPermissionCamera(Activity activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
        }
    }


}