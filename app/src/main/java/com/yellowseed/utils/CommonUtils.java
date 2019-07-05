package com.yellowseed.utils;


import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.yellowseed.R;
import com.yellowseed.imageUtils.CircleTransformation;
import com.yellowseed.model.reqModel.UserModel;
import com.yellowseed.webservices.response.homepost.Post;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@SuppressWarnings("ALL")
public class CommonUtils {

    private static final String regex_isValidUserName = "^[a-zA-Z0-9@_.]*$";
    private static final String regex_isValidName = "^[\\p{L} .'-]+$";
    private static final ExecutorService THREADPOOL = Executors.newCachedThreadPool();
    public static boolean DEBUG = true;

    public static int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    // Returns the URI path to the Bitmap displayed in specified ImageView
    public static File getLocalBitmapUri(ImageView imageView, Context context) {
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        File file = null;
        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        Uri bmpUri = null;
        try {
            file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "avatar.png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
            bmpUri = Uri.fromFile(file);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    // Returns the URI path to the Bitmap displayed in specified ImageView
    public static Bitmap getImageBitmap(ImageView imageView, Context context) {
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        File file = null;
        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        try {
            file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "avatar.png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }

    public static long timeElapsedFromDate(Context context, long commentDate) {
        long timeElapsed = 0L;
        long cdLongValue = 0L;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        String gmtTime = df.format(new Date());

        Date currentDate = null;
        try {
            currentDate = df.parse(gmtTime);
            cdLongValue = currentDate.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        long diff = cdLongValue - commentDate;
        long diffSeconds = diff / 1000;
        long diffMinutes = diffSeconds / 60;
        long diffHours = diffMinutes / 60;
        long diffDays = diffHours / 24;
        long diffWeeks = diffDays / 7;
        long diffMonth = diffDays / 30;
        long diffYear = diffDays / 365;


        if (diffYear > 0) {
            timeElapsed = diffYear;
            CommonUtils.savePreferencesString(context, AppConstants.TIME_ELAPSED_STRING, "y ago");
        } else if (diffYear < 1 && diffMonth > 0) {
            timeElapsed = diffMonth;
            CommonUtils.savePreferencesString(context, AppConstants.TIME_ELAPSED_STRING, "m ago");
        } else if (diffYear < 1 && diffMonth < 1 && diffWeeks > 0) {
            timeElapsed = diffWeeks;
            CommonUtils.savePreferencesString(context, AppConstants.TIME_ELAPSED_STRING, "w ago");
        } else if (diffYear < 1 && diffMonth < 1 && diffWeeks < 1 && diffDays > 0) {
            timeElapsed = diffDays;
            CommonUtils.savePreferencesString(context, AppConstants.TIME_ELAPSED_STRING, "d ago");
        } else if (diffYear < 1 && diffMonth < 1 && diffWeeks < 1 && diffDays < 1 && diffHours > 0) {
            timeElapsed = diffHours;
            CommonUtils.savePreferencesString(context, AppConstants.TIME_ELAPSED_STRING, "h ago");
        } else if (diffYear < 1 && diffMonth < 1 && diffWeeks < 1 && diffDays < 1 && diffHours < 1) {
            timeElapsed = commentDate;
        }
        return timeElapsed;
    }

    /**
     * Function to get Progress percentage
     *
     * @param currentDuration
     * @param totalDuration
     */
    public static int getProgressPercentage(long currentDuration, long totalDuration) {
        Double percentage = (double) 0;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        // calculating percentage
        percentage = (((double) currentSeconds) / totalSeconds) * 100;

        // return percentage
        return percentage.intValue();
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Returns the URI path to the Bitmap displayed in specified ImageView
    public static Uri getLocalBitmapUri(String url, Context context) {
        Bitmap bmp = null;
        bmp = getBitmapFromURL(url);
        Uri bmpUri = null;
        try {
            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "text.png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    public static void showMap(Context context, String lat, String lng, String labelLocation) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:<" + lat + ">,<" + lng + ">?q=<" + lat + ">,<" + lng + ">(" + labelLocation + ")"));
        context.startActivity(intent);
    }

    public static Drawable changeDrawableColor(Context context, int icon, int newColor) {
        Drawable mDrawable = ContextCompat.getDrawable(context, icon).mutate();
        mDrawable.setColorFilter(new PorterDuffColorFilter(newColor, PorterDuff.Mode.SRC_IN));
        return mDrawable;
    }

    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static Drawable getDrawable(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getResources().getDrawable(id, null);
        } else {
            return context.getResources().getDrawable(id);
        }
    }

    /*public static Typeface setFontIcon(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/fanskick_font_icon_set.ttf");
    }

    public static Typeface setFontIcon2(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/fanskick_font2_icon_set.ttf");
    }

    public static Typeface setFontBold(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "gilroy_extra_bold.otf");
    }

    public static Typeface setFontLight(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "gilroy_light.otf");
    }*/

    public static void showToast(Context context, String message) {
        boolean showToast = true;

        if (showToast) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html) {
        Spanned result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }
/*
    public static boolean checkPermissionStorage(Activity context) {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            if (result1 == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }*/

    /**
     * This method is being used to validate username.
     *
     * @param userName the string
     * @return boolean.
     */
    public static boolean isValidUserName(String userName) {
        return !userName.isEmpty() && Pattern.compile(regex_isValidUserName).matcher(userName).matches();
    }

    /**
     * Checking the permission storage
     *
     * @param context activity context
     * @return true is PERMISSION_GRANTED else false
     */

    public static boolean isValidName(String userName) {
        return !userName.isEmpty() && Pattern.compile(regex_isValidUserName).matcher(userName).matches();
    }

    /**
     * Requesting the permission storage
     *
     * @param activity activity context
     */


    /**
     * Requesting the permission camera
     *
     * @param activity activity context
     */

    public static boolean checkPermissionStorage(Activity context) {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * @param target email string
     * @return true if valid email else false
     */
    public static boolean isValidEmail(CharSequence target) {
        return target != null && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean emailFieldValidation(String fieldValue) {
        return !Patterns.EMAIL_ADDRESS.matcher(fieldValue).matches();
    }

    public static boolean lengthFieldValidation(String fieldValue, int length) {
        return fieldValue.length() < length;
    }

    public static void setFragment(Fragment fragment, boolean removeStack, FragmentActivity activity, int mContainer, String tag) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction ftTransaction = fragmentManager.beginTransaction();

        if (removeStack) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            if (tag != null)
                ftTransaction.replace(mContainer, fragment, tag);
            else
                ftTransaction.replace(mContainer, fragment);
        } else {
            if (tag != null)
                ftTransaction.replace(mContainer, fragment, tag);
            else
                ftTransaction.replace(mContainer, fragment);
            ftTransaction.addToBackStack(null);
        }
        ftTransaction.commit();
    }

    public static String local(String latitudeFinal, String longitudeFinal) {
        return "https://maps.googleapis.com/maps/api/staticmap?center=" + latitudeFinal + "," + longitudeFinal + "&zoom=18&size=280x280&markers=color:red|" + latitudeFinal + "," + longitudeFinal;
    }

    public static void setFragment(Fragment fragment, Bundle bundle, boolean removeStack, FragmentActivity activity, int mContainer, String tag) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction ftTransaction = fragmentManager.beginTransaction();

        fragment.setArguments(bundle);
        if (removeStack) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            if (tag != null)
                ftTransaction.replace(mContainer, fragment, tag);
            else
                ftTransaction.replace(mContainer, fragment);
        } else {
            if (tag != null)
                ftTransaction.replace(mContainer, fragment, tag);
            else
                ftTransaction.replace(mContainer, fragment);
            ftTransaction.addToBackStack(null);
        }
        ftTransaction.commit();
    }

    public static String getHumanTimeText(long milliseconds) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
    }

    public static void setFragmentChild(Fragment fragment, boolean removeStack, FragmentActivity activity, int mContainer, FragmentManager fm) {
        FragmentManager fragmentManager = fm;
        FragmentTransaction ftTransaction = fragmentManager.beginTransaction();

        if (removeStack) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            ftTransaction.replace(mContainer, fragment);
        } else {
            ftTransaction.replace(mContainer, fragment);
            ftTransaction.addToBackStack(null);
        }

        ftTransaction.commit();

    }

    public static void errorLog(String TAG, String message) {
        if (DEBUG) {
            android.util.Log.e(TAG, message);
        }
    }

    public static boolean isDateAfter(String startDate, String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date1 = null;
        try {
            date1 = sdf.parse(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date2 = null;
        try {
            date2 = sdf.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date1 != null && date2 != null) {
            if (date1.compareTo(date2) <= 0) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /*public static void setToolbar(final Activity activity, String header) {
        ((TextView) activity.findViewById(R.id.tvHeader)).setText(header);
        activity.findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
    }*/

    /*
     * To fetch primary
     *
     * */
    public static List getEmailList(Context context) {
        List<String> emailList = new ArrayList<>();
        Pattern gmailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(context).getAccounts();
        for (Account account : accounts) {
            if (gmailPattern.matcher(account.name).matches()) {
                if (account.name != null)
                    emailList.add(account.name);
            }
        }
        return emailList;
    }

    public static boolean isImageFile(String path) {
        try {
            String mimeType = URLConnection.guessContentTypeFromName(path);
            return mimeType != null && mimeType.startsWith("image");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public static boolean isVideoFile(String path) {
        try {
            String mimeType = URLConnection.guessContentTypeFromName(path);
            return mimeType != null && mimeType.startsWith("video");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Bitmap retriveVideoFrameFromVideo(String videoPath) throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

    public static String getStatus(int status) {
        if (status == 0) return "Order placed";
        else if (status == 1) return "Packed";
        else if (status == 2) return "Dispatched";
        else if (status == 3) return "Delivered";
        else return "";
    }

    public static void runButNotOn(Runnable toRun, Thread notOn) {
        if (Thread.currentThread() == notOn) {
            THREADPOOL.submit(toRun);
        } else {
            toRun.run();
        }
    }


    public static void showOptionsDialog(Context context) {
        // custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_dialog_other_user_post);

        // set the custom dialog components - text, image and button
        TextView shareOnFb = (TextView) dialog.findViewById(R.id.text);
        TextView sendToChat = (TextView) dialog.findViewById(R.id.text);
        TextView reportPost = (TextView) dialog.findViewById(R.id.text);
        dialog.show();
    }


    public static void savePreferencesInt(Context context, String key, int value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();

    }

    public static int getPreferencesInt(Context context, String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(key, 0);
    }


    public static void savePreferencesString(Context context, String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();

    }

    public static String getPreferencesString(Context context, String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(key, "");
    }

    public static void savePreferencesBoolean(Context context, String key, boolean value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getPreferencesBoolean(Context context, String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(key, false);
    }

    public static void showSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        if (inputMethodManager != null) {
            inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /*public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }*/

    public static int getStatusBarHeight(Activity context) {
        Rect rectangle = new Rect();
        Window window = context.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        int statusBarHeight = rectangle.top;
        int contentViewTop = window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
//        int titleBarHeight = contentViewTop - statusBarHeight;
        return contentViewTop + statusBarHeight;
    }

    public static void setOverflowButtonColor(final Toolbar toolbar, final int color) {
        Drawable drawable = toolbar.getOverflowIcon();
        if (drawable != null) {
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable.mutate(), color);
            toolbar.setOverflowIcon(drawable);
        }
    }

    public static void colorMenuItem(MenuItem menuItem, Integer color) {
        if (color == null) {
            return; // nothing to do.
        }
        if (menuItem == null)
            return;

        Drawable drawable = menuItem.getIcon();
        if (drawable != null) {
            // If we don't mutate the drawable, then all drawables with this id will have the ColorFilter
            drawable.mutate();
            if (color != null) {
                drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

    /*public static void setSpinnerMaxHeight(Context context, Spinner spinner, int height) {
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);
            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner);
//            popupWindow.setBackgroundDrawable(getDrawable(context, R.drawable.rectangle_dark_grey_round));
            // Set popupWindow height to 500px
            popupWindow.setHeight(height);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }
    }*/
    public static Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp;
        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;


    }


    /*
     *
     * method to check internet connectivity
     * */
    public static boolean isOnline(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            return false;
        }
        return true;
    }


    public static void getThePlace(Activity activity, int code) {
        AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(Place.TYPE_COUNTRY)
                .setCountry("IN")
                .build();
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .setFilter(autocompleteFilter)
                    .build(activity);
            activity.startActivityForResult(intent, code);
        } catch (GooglePlayServicesRepairableException |
                GooglePlayServicesNotAvailableException t) {

            LogUtils.errorLog("GooglePlayServices Failure", "" + ((t != null && t.getMessage() != null) ? t.getMessage() : "---"));
        } catch (Exception t) {

            LogUtils.errorLog("Exception", "" + ((t != null && t.getMessage() != null) ? t.getMessage() : "---"));
        }

    }

    public static void clearPrefData(Context mContext) {
        PrefManager.getInstance(mContext).removeFromPreference(PrefManager.KEY_IS_LOGIN);
        PrefManager.getInstance(mContext).removeFromPreference(PrefManager.KEY_ACCESS_TOKEN);
        PrefManager.getInstance(mContext).removeFromPreference(PrefManager.KEY_USER_ID);
    }

    public static boolean isValidPhone(String phone) {
        String expression = "^([0-9\\+]|\\(\\d{1,3}\\))[0-9\\-\\. ]{6,15}$";
        CharSequence inputString = phone;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputString);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public static void saveUserData(Context mContext, UserModel userModel) {
        PrefManager.getInstance(mContext).setKeyIsLogin(true);
        PrefManager.getInstance(mContext).setKeyAccessToken(userModel.getAccess_token());
        PrefManager.getInstance(mContext).setUserId(userModel.getId());
        PrefManager.getInstance(mContext).setQBid(userModel.getQb_id());

        PrefManager.getInstance(mContext).setKeyAccessToken(userModel.getAccess_token());
        if (!TextUtils.isEmpty(userModel.getImage())) {
            PrefManager.getInstance(mContext).setUserProfilePic(userModel.getImage());
        }
    }


    public static String getFormattedDate(Post post) {
        String setDateValue = "";
        String setHourValue = "";
        SimpleDateFormat readDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        readDate.setTimeZone(TimeZone.getTimeZone("GMT")); // missing line
        Date date = null;
        Date postDate = null;
        String strDate = "";

        if (post != null && post.getPost() != null) {
            try {
                date = readDate.parse(post.getPost().getCreatedAt());
                String time = readDate.format(date);

                SimpleDateFormat formattedDate = new SimpleDateFormat(
                        "yyyy-MM-dd");
                postDate = formattedDate.parse(time);
                String postDateValue = formattedDate.format(postDate);

                formattedDate.setTimeZone(TimeZone.getTimeZone("GMT"));
                String gmtTime = formattedDate.format(new Date());

                Date currentDate = null;
                currentDate = formattedDate.parse(gmtTime);
                String currentDateValue = formattedDate.format(currentDate);

                if (postDateValue.equalsIgnoreCase(currentDateValue)) {
                    SimpleDateFormat setDateFormat = new SimpleDateFormat("hh:mm a");
                    setDateValue = setDateFormat.format(new Date(Long.parseLong(String.valueOf(date.getTime()))));
                    strDate = "Today at " + setDateValue;

                } else {

                    SimpleDateFormat setDateFormat = new SimpleDateFormat("dd MMM yyyy");
                    setDateValue = setDateFormat.format(new Date(Long.parseLong(String.valueOf(date.getTime()))));

                    SimpleDateFormat setHourFormat = new SimpleDateFormat("hh:mm a");
                    setHourValue = setHourFormat.format(new Date(Long.parseLong(String.valueOf(date.getTime()))));
                    strDate = setDateValue + " at " + setHourValue;
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return strDate;
    }


    public static String manageGMTLocale(String lastUpdated) throws ParseException {
        String result = "";

//        SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy/MM/dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sourceFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date parsed = sourceFormat.parse(lastUpdated); // => Date is in UTC now


        SimpleDateFormat destFormat = new SimpleDateFormat("dd/MM/yyyy, hh:mm a");
        destFormat.setTimeZone(TimeZone.getDefault());
        result = destFormat.format(parsed);
        return result;
    }

    public static boolean checkPermissionCamera(Activity context) {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean checkPermissionAudioOutput(Activity context) {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.CAPTURE_AUDIO_OUTPUT);
        int result1 = ContextCompat.checkSelfPermission(context, Manifest.permission.MODIFY_AUDIO_SETTINGS);
        int result2 = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermissionAudio(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAPTURE_AUDIO_OUTPUT}, AppConstants.PERMISSION_REQUEST_CODE);
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.MODIFY_AUDIO_SETTINGS}, AppConstants.PERMISSION_REQUEST_CODE);
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECORD_AUDIO}, AppConstants.PERMISSION_REQUEST_CODE);
    }

    public static void requestPermissionStorage(Activity activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, AppConstants.PERMISSION_REQUEST_CODE);
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, AppConstants.PERMISSION_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, AppConstants.PERMISSION_REQUEST_CODE);
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, AppConstants.PERMISSION_REQUEST_CODE);
        }
    }

    public static void requestPermissionCamera(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, AppConstants.PERMISSION_REQUEST_CODE);
    }

    public static void requestPermissionsendSms(Activity activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.SEND_SMS)) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.SEND_SMS}, AppConstants.PERMISSION_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.SEND_SMS}, AppConstants.PERMISSION_REQUEST_CODE);
        }
    }

    public static void requestPermissionGetUserContacts(Activity activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_CONTACTS)) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CONTACTS}, AppConstants.PERMISSION_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CONTACTS}, AppConstants.PERMISSION_REQUEST_CODE);
        }
    }

    public static void requestPermissionCallPhone(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, AppConstants.PERMISSION_REQUEST_CODE);
    }


    public static void requestPermission(Activity context) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, AppConstants.PERMISSION_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, AppConstants.PERMISSION_REQUEST_CODE);
        }
    }

    public static boolean checkPermission(Activity context) {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }


    public static void userProfile(Context context, ImageView img, TextView tvName) {
        String url = CommonUtils.getPreferencesString(context, AppConstants.USER_PROFILE_IMAGE);
        String name = CommonUtils.getPreferencesString(context, AppConstants.USER_NAME);
        String mName[] = CommonUtils.getPreferencesString(context, AppConstants.USER_NAME).split(" ");
        tvName.setTextColor(ContextCompat.getColor(context,R.color.white));

        if (url != null && !url.equalsIgnoreCase("")) {
            tvName.setVisibility(View.GONE);
            img.setVisibility(View.VISIBLE);
            Picasso.with(context).load(url).placeholder(R.drawable.user_placeholder).
                    transform(new CircleTransformation()).into(img);

        } else {
            if (mName != null && mName.length > 1) {
                tvName.setText(String.valueOf(mName[0].charAt(0)) + String.valueOf(mName[1].charAt(0)));

            } else if (mName != null && mName.length > 0) {
                tvName.setText(String.valueOf(mName[0].charAt(0)));

            } else {
                tvName.setText("?");

            }
            tvName.setVisibility(View.VISIBLE);
            img.setVisibility(View.GONE);

        }

    }

    public static void otherUserProfile(Context context, ImageView img, String url, TextView tvName, String name) {
        String mName[] = name.split(" ");
        tvName.setTextColor(ContextCompat.getColor(context,R.color.white));

        if (url != null && !url.equalsIgnoreCase("")) {
            tvName.setVisibility(View.GONE);

            img.setVisibility(View.VISIBLE);
            Picasso.with(context).load(url).
                    transform(new CircleTransformation()).placeholder(R.drawable.user_placeholder).
                    memoryPolicy(MemoryPolicy.NO_CACHE)
                    .transform(new CircleTransformation()).into(img);


        } else {
            if (mName != null && mName.length > 1) {
                tvName.setText(String.valueOf(mName[0].charAt(0)) + String.valueOf(mName[1].charAt(0)));

            } else if (mName != null && mName.length > 0) {
                tvName.setText(String.valueOf(mName[0].charAt(0)));

            } else {
                tvName.setText("?");

            }
            tvName.setVisibility(View.VISIBLE);
            img.setVisibility(View.GONE);

        }

    }


}
