package co.idearun.twitter.common;


import java.text.SimpleDateFormat;
import android.util.Log;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import timber.log.Timber;

public class TimeAgo2 {

    public String covertTimeToText(String dataDate) {

        String convTime = null;

        String prefix = "";
        String suffix = "Ago";

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            Date pasTime = dateFormat.parse(dataDate);

            Date nowTime = new Date();

            long dateDiff = nowTime.getTime() - pasTime.getTime();

            long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
            long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
            long hour   = TimeUnit.MILLISECONDS.toHours(dateDiff);
            long day  = TimeUnit.MILLISECONDS.toDays(dateDiff);

            if (second < 60) {
                convTime = second + " Seconds " + suffix;
            } else if (minute < 60) {
                convTime = minute + " Minutes "+suffix;
            } else if (hour < 24) {
                convTime = hour + " Hours "+suffix;
            } else if (day >= 7) {
                if (day > 360) {
                    convTime = (day / 360) + " Years " + suffix;
                } else if (day > 30) {
                    convTime = (day / 30) + " Months " + suffix;
                } else {
                    convTime = (day / 7) + " Week " + suffix;
                }
            } else if (day < 7) {
                convTime = day+" Days "+suffix;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return convTime;
    }

    public long convertAgeToYear(String dataDate) {

        String convTime = null;

        String prefix = "";
        String suffix = "Ago";

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss zzz yyyy", Locale.getDefault());
            Date pasTime = dateFormat.parse(dataDate);

            Date nowTime = new Date();

            long dateDiff = nowTime.getTime() - pasTime.getTime();

            long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
            long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
            long hour   = TimeUnit.MILLISECONDS.toHours(dateDiff);
            long day  = TimeUnit.MILLISECONDS.toDays(dateDiff);

            if (second < 60) {
                convTime = second + " Seconds " + suffix;
            } else if (minute < 60) {
                convTime = minute + " Minutes "+suffix;
            } else if (hour < 24) {
                convTime = hour + " Hours "+suffix;
            } else if (day >= 7) {
                if (day > 360) {
                    convTime = (day / 360) + " Years " + suffix;
                } else if (day > 30) {
                    convTime = (day / 30) + " Months " + suffix;
                } else {
                    convTime = (day / 7) + " Week " + suffix;
                }
            } else if (day < 7) {
                convTime = day+" Days "+suffix;
            }
//            Log.e("ConvTime",""+ day);

            return day/360;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }
    public long covertTimeToDay(String dataDate) {

        String convTime = null;

        String prefix = "";
        String suffix = "Ago";

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss zzz yyyy", Locale.getDefault());
            Date pasTime = dateFormat.parse(dataDate);

            Date nowTime = new Date();

            long dateDiff = nowTime.getTime() - pasTime.getTime();

            long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
            long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
            long hour   = TimeUnit.MILLISECONDS.toHours(dateDiff);
            long day  = TimeUnit.MILLISECONDS.toDays(dateDiff);

            if (second < 60) {
                convTime = second + " Seconds " + suffix;
            } else if (minute < 60) {
                convTime = minute + " Minutes "+suffix;
            } else if (hour < 24) {
                convTime = hour + " Hours "+suffix;
            } else if (day >= 7) {
                if (day > 360) {
                    convTime = (day / 360) + " Years " + suffix;
                } else if (day > 30) {
                    convTime = (day / 30) + " Months " + suffix;
                } else {
                    convTime = (day / 7) + " Week " + suffix;
                }
            } else if (day < 7) {
                convTime = day+" Days "+suffix;
            }
//            Log.e("ConvTime",""+ day);

            return day;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public String covertDateToString(Date dataDate) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        Timber.e("covertDateToString"+""+dateFormat.format(dataDate));
           return dateFormat.format(dataDate);

    }

}