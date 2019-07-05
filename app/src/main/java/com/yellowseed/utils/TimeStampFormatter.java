package com.yellowseed.utils;

import android.text.TextUtils;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class TimeStampFormatter {


    /**
     * yyyy-MM-dd 1969-12-31
     * yyyy-MM-dd 1970-01-01
     * yyyy-MM-dd HH:mm 1969-12-31 16:00
     * yyyy-MM-dd HH:mm 1970-01-01 00:00
     * yyyy-MM-dd HH:mmZ 1969-12-31 16:00-0800
     * yyyy-MM-dd HH:mmZ 1970-01-01 00:00+0000
     * yyyy-MM-dd HH:mm:ss.SSSZ 1969-12-31 16:00:00.000-0800
     * yyyy-MM-dd HH:mm:ss.SSSZ 1970-01-01 00:00:00.000+0000
     * yyyy-MM-dd'T'HH:mm:ss.SSSZ 1969-12-31T16:00:00.000-0800
     * yyyy-MM-dd'T'HH:mm:ss.SSSZ 1970-01-01T00:00:00.000+0000
     */
 //   public static String weekDayName[]={"SUN","MON","TUE","WED","THU","FRI","SAT"};
    public static String getValueFromTS(String timeStamp, String dateFormat) {
        try {
            if (TextUtils.isEmpty(timeStamp)) {
                return "";
            }
            if (timeStamp.length() == 10) {
                DateFormat formatter1 = new SimpleDateFormat(dateFormat, Locale.getDefault());
                return formatter1.format(new Date((Long.valueOf(timeStamp) * 1000)));
            } else if (timeStamp.length() == 13) {
                DateFormat formatter1 = new SimpleDateFormat(dateFormat, Locale.getDefault());
                return formatter1.format(new Date((Long.valueOf(timeStamp))));
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getDayFromTS(String timeStamp, String dateFormat) {
        try {
            if (TextUtils.isEmpty(timeStamp)) {
                return "";
            }
            if (timeStamp.length() == 10) {
                DateFormat formatter1 = new SimpleDateFormat(dateFormat, Locale.getDefault());
                return formatter1.format(new Date((Long.valueOf(timeStamp) * 1000)));
            } else if (timeStamp.length() == 13) {
                DateFormat formatter1 = new SimpleDateFormat(dateFormat, Locale.getDefault());
                return formatter1.format(new Date((Long.valueOf(timeStamp))));
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    public static String getMonthYearFromTS(String timeStamp, String dateFormat) {
        try {
            if (TextUtils.isEmpty(timeStamp)) {
                return "";
            }
            if (timeStamp.length() == 10) {
                DateFormat formatter1 = new SimpleDateFormat(dateFormat, Locale.getDefault());
                return formatter1.format(new Date((Long.valueOf(timeStamp) * 1000)));
            } else if (timeStamp.length() == 13) {
                DateFormat formatter1 = new SimpleDateFormat(dateFormat, Locale.getDefault());
                return formatter1.format(new Date((Long.valueOf(timeStamp))));
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    public static String getGmtValueFromTS(String timeStamp, String dateFormat) {
        try {
            if (TextUtils.isEmpty(timeStamp)) {
                return "";
            }
            if (timeStamp.length() == 10) {
                DateFormat formatter1 = new SimpleDateFormat(dateFormat, Locale.getDefault());
                return formatter1.format(new Date((Long.valueOf(timeStamp) * 1000)));
            } else if (timeStamp.length() == 13) {
                DateFormat formatter1 = new SimpleDateFormat(dateFormat, Locale.getDefault());
                formatter1.setTimeZone(TimeZone.getTimeZone("GMT"));
                return formatter1.format(new Date((Long.valueOf(timeStamp))));
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getValueFromTS1(String timeStamp, String dateFormat) {
        try {
            if (TextUtils.isEmpty(timeStamp)) {
                return "";
            }
            if (timeStamp.length() == 10) {
                DateFormat formatter1 = new SimpleDateFormat(dateFormat, Locale.getDefault());
                return formatter1.format(new Date((Long.valueOf(timeStamp))));
            } else if (timeStamp.length() == 13) {
                DateFormat formatter1 = new SimpleDateFormat(dateFormat, Locale.getDefault());
                return formatter1.format(new Date((Long.valueOf(timeStamp))));
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static long getTimeStamp(String date, String dateFormat) {
        try {
            if (!TextUtils.isEmpty(date)) {
                DateFormat formatter = new SimpleDateFormat(dateFormat, Locale.getDefault());

                Date newDate = (Date) formatter.parse(date);
                LogUtils.errorLog("TimeStampFormatter", "_____dateFormat_____" + dateFormat);
                LogUtils.errorLog("TimeStampFormatter", "_____getTimeStamp_____" + newDate.getTime());
                return newDate.getTime();
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static long getTimeStampAsPerTimeZone(String date, String dateFormat) {
        try {
            if (!TextUtils.isEmpty(date)) {
                DateFormat formatter = new SimpleDateFormat(dateFormat, Locale.US);
                formatter.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));

                Date newDate = (Date) formatter.parse(date);
                LogUtils.errorLog("TimeStampFormatter", "_____dateFormat_____" + dateFormat);
                LogUtils.errorLog("TimeStampFormatter", "_____getTimeStamp_____" + newDate.getTime());
                return newDate.getTime();
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    public static String changeDateTimeFormat(String date, String oldFormat, String newFormat) {
        SimpleDateFormat inputFormat = new SimpleDateFormat(oldFormat, Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat(newFormat, Locale.getDefault());
        Date newDate;
        String str;
        try {
            newDate = inputFormat.parse(date);
            str = outputFormat.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
//        LogUtils.errorLog("TimeStampFormatter", "_____oldFormat_____" + oldFormat);
//        LogUtils.errorLog("TimeStampFormatter", "_____newFormat_____" + newFormat);
//        LogUtils.errorLog("TimeStampFormatter", "_____date_____" + date);
//        LogUtils.errorLog("TimeStampFormatter", "_____changeDateFormat_____" + str);

        return str;
    }

    public static String changeDateTimeFormatAfterOneDay(String date, String oldFormat, String newFormat) {
        SimpleDateFormat inputFormat = new SimpleDateFormat(oldFormat, Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat(newFormat, Locale.getDefault());
        Date newDate;
        String str;
        try {
            newDate = inputFormat.parse(date);
            str = outputFormat.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
//        LogUtils.errorLog("TimeStampFormatter", "_____oldFormat_____" + oldFormat);
//        LogUtils.errorLog("TimeStampFormatter", "_____newFormat_____" + newFormat);
//        LogUtils.errorLog("TimeStampFormatter", "_____date_____" + date);
//        LogUtils.errorLog("TimeStampFormatter", "_____changeDateFormat_____" + str);

        return str;
    }

    public static String getDateSuffix(int day) {
        switch (day) {
            case 1:
            case 21:
            case 31:
                return (day + "st");

            case 2:
            case 22:
                return (day + "nd");

            case 3:
            case 23:
                return (day + "rd");

            default:
                return (day + "th");
        }
    }

    public static String getCommentTime(String currentDate, String startDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        try {
            Date date1 = simpleDateFormat.parse(getValueFromTS(currentDate, "MM/dd/yyyy"));
            Date date2 = simpleDateFormat.parse(getValueFromTS(startDate, "MM/dd/yyyy"));
            return printDifference(date1, date2, startDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    //1 minute = 60 seconds
    //1 hour = 60 x 60 = 3600
    //1 day = 3600 x 24 = 86400
    public static String printDifference(Date startDate, Date endDate, String date) {

        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays,
                elapsedHours, elapsedMinutes, elapsedSeconds);

        if (elapsedDays < 0 && elapsedSeconds < 60) {
            return String.valueOf(elapsedSeconds) + "secs ago";
        } else if (elapsedDays < 0 && elapsedSeconds > 60 && elapsedMinutes < 60) {
            return String.valueOf(elapsedMinutes) + "mins ago";
        } else if (elapsedDays < 0 && elapsedMinutes > 60 && elapsedHours < 24) {
            return String.valueOf(elapsedHours) + "hrs ago";
        } else {
            return (getValueFromTS(date, "MM/dd/yyyy"));
        }
    }




}
