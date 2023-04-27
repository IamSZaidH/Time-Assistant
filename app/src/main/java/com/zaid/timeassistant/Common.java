package com.zaid.timeassistant;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Common {
    public static int spDateCode = 8588;
    public static int hourlyCode=1111;
    public static int thirtyCode=2222;
    public static int fifteenCode=3333;
    public static String spTimeSharedPref = "SpTime";
    public static String hourlySHPREF = "hourly";
    public static String thirtySHPREF = "thirty";
    public static String fiftySHPREF = "fifteen";
    public static String langHPREF = "lang";
    public static String voicePREF = "voice";
    public static String hindi = "hindi";
    public static String english = "english";
    public static String urdu = "urdu";
    public static String spDateTime="spDateTime";
    public static String spDateTimeSwitch="spDateTimeSwitch";

    public static String spDateTimeMilli="spDateTimeMilli";
    public static String spDateHourOFDay="spDateHourOFDay";
    public static String spDateMinute="spDateMinute";

    public static SharedPreferences shPref;
    public static String spReminderswitch="spReminder";
    public static String spReminderTime="spReminderText";
    public static String spReminderDate="spReminderDate";
    public static String spReminderTimeMilli="spReminderTimeMilli";
    public static String sentence="sentence102";
    @NonNull
    public static String nowDateis() {
// Create a date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault());

// Get the current date
        Date currentDate = new Date();

// Format the date as a string with the day name and month name
        // Print the date
        return dateFormat.format(currentDate);


    }

    public static Map<String, String> nowTimeis() {
        Calendar calendar = Calendar.getInstance();
        Map<String, String> timeMap = new HashMap<>();
        int h = calendar.get(Calendar.HOUR);
        String hh = "";
        String mm = "";
        if (h == 0) {
            h = 12;
        }
        if (h < 10) {
            hh = "0" + h;
        } else {
            hh = String.valueOf(h);
        }
        timeMap.put("hour", hh);
        int m = calendar.get(Calendar.MINUTE);
        if (m < 10) {
            mm = String.format("%02d", m);
            timeMap.put("minute", mm);
        } else {
            mm = String.valueOf(m);
            timeMap.put("minute", mm);
        }
        int amPm = calendar.get(Calendar.AM_PM);
        String amPmStr = (amPm == Calendar.AM) ? "AM" : "PM";
        timeMap.put("am_pm", amPmStr);
        return timeMap;
    }

    public static void setShPref(String x, boolean y, Context context) {
        shPref = context.getSharedPreferences(Common.spTimeSharedPref, MODE_PRIVATE);
        SharedPreferences.Editor editor = shPref.edit();
        editor.putBoolean(x, y);
        editor.apply();
    }

    public static Boolean getSHPref(String x, Context context) {
        shPref = context.getSharedPreferences(Common.spTimeSharedPref, MODE_PRIVATE);
        return shPref.getBoolean(x,false);
    }

    public static void setSpLang(Context context, String x) {
        shPref = context.getSharedPreferences(Common.spTimeSharedPref, MODE_PRIVATE);
        SharedPreferences.Editor editor = shPref.edit();
        editor.putString(langHPREF, x);
        editor.apply();
    }

    public static String getLang(Context context) {
        shPref = context.getSharedPreferences(Common.spTimeSharedPref, MODE_PRIVATE);
        return shPref.getString(langHPREF, english);
    }

    public static String[] getLangCountry(String x) {
        String[] arr = new String[2];
        if (x.contains(Common.english)) {
            arr[0] = "en";
            arr[1] = "US";
            return arr;
        } else if (x.contains(Common.hindi)) {
            arr[0] = "hi";
            arr[1] = "IN";
            return arr;
        } else if (x.contains(Common.urdu)) {
            arr[0] = "ur";
            arr[1] = "PK";
            return arr;
        } else {
            arr[0] = "en";
            arr[1] = "US";
            return arr;
        }
    }

    public static void setVoice(Context context, String x) {
        shPref = context.getSharedPreferences(Common.spTimeSharedPref, MODE_PRIVATE);
        SharedPreferences.Editor editor = shPref.edit();
        editor.putString(Common.voicePREF, x);
        editor.apply();
        Log.d("xyz", "voice set: " + x);
    }

    public static String getVoice(Context context) {
        shPref = context.getSharedPreferences(Common.spTimeSharedPref, MODE_PRIVATE);
        String v = shPref.getString(Common.voicePREF, "");
        Log.d("xyz", "voice get: " + v);
        return v;
    }
    public static void setSpDateTime(Context context,String y,String x){
        shPref=context.getSharedPreferences(Common.spTimeSharedPref,MODE_PRIVATE);
        SharedPreferences.Editor editor=shPref.edit();
        editor.putString(y, x);
        editor.apply();
        Log.d("xyz", "Date time set: " + x);

    }
    public static String getSpDateTime(Context context,String y) {
        shPref = context.getSharedPreferences(Common.spTimeSharedPref, MODE_PRIVATE);
        String x=shPref.getString(y,"");
        Log.d("xyz", "voice get: " + x);
        return x;
    }
    public static void setSpDateTimeMilli(Context context,String  y,long x) {
        shPref=context.getSharedPreferences(Common.spTimeSharedPref,MODE_PRIVATE);
        SharedPreferences.Editor editor=shPref.edit();
        editor.putLong(y, x);
        editor.apply();
        Log.d("xyz", "Date time set milli: " + x);
    }

    public static long getSpDateTimeMilli(Context context,String y) {
        shPref = context.getSharedPreferences(Common.spTimeSharedPref, MODE_PRIVATE);
        long x=shPref.getLong(y,Long.MIN_VALUE);
        Log.d("xyz", "voice get: " + x);
        return x;
    }
    public static void setSentence(String x,Context context){
        shPref=context.getSharedPreferences(Common.spTimeSharedPref,MODE_PRIVATE);
        SharedPreferences.Editor editor=shPref.edit();
        editor.putString(Common.sentence,x);
        editor.apply();
    }
    public static String getSentence(Context context){
        shPref=context.getSharedPreferences(Common.spTimeSharedPref,MODE_PRIVATE);
        return shPref.getString(Common.sentence,"");
    }

    public static void setSpDateHourOfDayandMinute(Context context,String x,int y) {
        shPref=context.getSharedPreferences(Common.spTimeSharedPref,MODE_PRIVATE);
        SharedPreferences.Editor editor=shPref.edit();
        editor.putInt(x,y);
        editor.apply();
    }
    public static int getHourOfDayandMinute(Context context,String x){
        shPref=context.getSharedPreferences(Common.spTimeSharedPref,MODE_PRIVATE);
        return shPref.getInt(x,0);
    }
}


