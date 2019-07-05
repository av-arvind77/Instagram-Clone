package com.yellowseed.utils;

import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


public class DateUtils {

    public static final String DATE_FORMAT_FROM_SERVER = "yyyy'-'MM'-'dd'T'HH':'mm':'ss.SSS'Z'";
    public static final String DATE_FORMAT = "EEEE dd MMMM yyyy";
    public static final String DATE_FORMAT_ = "EEE dd MMMM yyyy";
    public static final String DATE_FORMAT_IN_HH_MM = "HH:mm";
    public static final String DATE_FORMAT_IN_HH_MM_A = "hh:mm a";
    public static final String DATE_FORMAT_ON_HOME_ORDER = "EEE, dd MMM, hh:mm a";
    //    public static final String DATE_FORMAT_ON_HOME = "EEE, dd MMM, HH:mm";
    public static final String DATE_FORMAT_ON_HOME = "EEE, dd MMM, hh:mm a";
    public static final String DATE_FORMAT_COMMON = "MMM dd, yyyy";
    public static final String DATE_FORMAT_COMMON_DATE = "MM-dd-yyyy";
    public static final String UTC = "UTC";
    public static final String DATE_FORMAT_SENZPAY = "yyyy-MM-dd";
    public static final String DATE_FORMAT_SENZPAY_ = "dd/MM/yyyy";
    public static final String DATE_FORMAT_SENZPAY__1 = "MMM dd, yyyy hh:mm a";
    public static final String DATE_FORMAT_SENZPAY__REQUEST = "yyyy-MM-dd HH:mm:ss";

    public static String convertDateFormat(String dateToBeChanged, String toFormat) {
        String dateOutput = "";
        try {
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(DateUtils.DATE_FORMAT_FROM_SERVER, Locale.getDefault());
            simpleDateFormat1.setTimeZone(TimeZone.getTimeZone(UTC));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(toFormat, Locale.getDefault());
            dateOutput = simpleDateFormat.format(simpleDateFormat1.parse(dateToBeChanged));
            if (toFormat.equals(DATE_FORMAT_ON_HOME_ORDER) || toFormat.equals(DATE_FORMAT_ON_HOME) || toFormat.equals(DATE_FORMAT_IN_HH_MM_A)) {
                dateOutput = dateOutput.replace("am", "AM").replace("pm", "PM");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateOutput;
    }

    public static String convertDateFormat(String dateToBeChanged, String fromPattern, String toFormat) {
        String dateOutput = "";
        try {
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(fromPattern, Locale.getDefault());
//            simpleDateFormat1.setTimeZone(TimeZone.getTimeZone(UTC));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(toFormat, Locale.getDefault());
            dateOutput = simpleDateFormat.format(simpleDateFormat1.parse(dateToBeChanged));
            if (toFormat.equals(DATE_FORMAT_ON_HOME_ORDER) || toFormat.equals(DATE_FORMAT_ON_HOME) || toFormat.equals(DATE_FORMAT_IN_HH_MM_A)) {
                dateOutput = dateOutput.replace("am", "AM").replace("pm", "PM");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateOutput;
    }

    public static String convertDateFormat(String dateToBeChanged, String fromPattern, String toFormat, boolean nothing) {
        String dateOutput = "";
        try {
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(fromPattern, Locale.getDefault());
//            simpleDateFormat1.setTimeZone(TimeZone.getTimeZone(UTC));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(toFormat, Locale.getDefault());
            dateOutput = simpleDateFormat.format(simpleDateFormat1.parse(dateToBeChanged));
            if (toFormat.equals(DATE_FORMAT_ON_HOME_ORDER) || toFormat.equals(DATE_FORMAT_ON_HOME) || toFormat.equals(DATE_FORMAT_IN_HH_MM_A)) {
                dateOutput = dateOutput.replace("am", "AM").replace("pm", "PM");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateOutput;
    }

    public static String convertTimeStampIntoAgo(String timeStamp) {
        String time;
        try {
            SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_FROM_SERVER, Locale.getDefault());
            format.setTimeZone(TimeZone.getTimeZone(UTC));
            Date past = format.parse(timeStamp);
            Date now = new Date();
            long diffInMilliSec = now.getTime() - past.getTime();
            long diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffInMilliSec);
//            long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
//            long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
//            long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
//            long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());

            long seconds = diffInSec % 60;
            diffInSec /= 60;
            long minutes = diffInSec % 60;
            diffInSec /= 60;
            long hours = diffInSec % 24;
            diffInSec /= 24;
            long days = diffInSec;

            int weeks = (int) (days / 7);
            int months = (int) (days / 30);
            int years = months / 365;

            String ifAgo;
            if (diffInMilliSec > 0) ifAgo = " ago";
            else ifAgo = "";

            time = getTimeString(Math.abs(seconds), Math.abs(minutes), Math.abs(hours), Math.abs(days), Math.abs(weeks), Math.abs(months), Math.abs(years)) + ifAgo;

        } catch (Exception e) {
            time = "";
            e.printStackTrace();
        }
        return time;
    }

    @NonNull
    private static String getTimeString(long seconds, long minutes, long hours, long days, int weeks, int months, int years) {
        String time;
        if (years > 0) time = years + " Y";
        else if (months > 0) time = months + " M";
        else if (weeks > 0) time = weeks + " W";
        else if (days > 0) time = days + " d";
        else if (hours > 0) time = hours + " h";
        else if (minutes > 0) time = minutes + " m";
        else if (seconds > 0) time = seconds + " s";
        else time = "now";
        return time;
    }


    public static String getDateInFormat(String toFormat, long timeStamp) {
        return new SimpleDateFormat(toFormat, Locale.getDefault()).format(timeStamp).replace("am", "AM").replace("pm", "PM");
    }

    public static String getDateInFormat1MonthAgo(String toFormat) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        Date date = calendar.getTime();
        return new SimpleDateFormat(toFormat, Locale.getDefault()).format(date).replace("am", "AM").replace("pm", "PM");
    }

    public static long getTimestampFromDateString(String dateFormat, String dateSting) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.getDefault());
        Date date;
        try {
            date = formatter.parse(dateSting);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
        return date.getTime();
    }


   /* public static long getDate(Context context, String toDateFormat, TextView tv) {
        final Calendar c = Calendar.getInstance();
        Calendar datetime = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        final long timeInMillis;
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                (view, year, monthOfYear, dayOfMonth) -> {
                    datetime.set(Calendar.YEAR, year);
                    datetime.set(Calendar.MONTH, monthOfYear);
                    datetime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    timeInMillis = datetime.getTimeInMillis();
                    tv.setText(DateUtils.getDateInFormat(toDateFormat, timeInMillis));
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
        return timeInMillis;
    }*/



    public static String getDateOfTimestamp(long timeStamp) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = formatter.format(new Date(timeStamp*1000L));
        return dateString;

    }
}
