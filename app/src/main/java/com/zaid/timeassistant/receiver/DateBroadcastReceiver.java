package com.zaid.timeassistant.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zaid.timeassistant.Common;
import com.zaid.timeassistant.SpDate;
import com.zaid.timeassistant.services.SpeakingService;

import java.util.Calendar;

public class DateBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intentPrev) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Calendar alarmTime = Calendar.getInstance();
        alarmTime.set(Calendar.HOUR_OF_DAY,Common.getHourOfDayandMinute(context,Common.spDateHourOFDay));
        alarmTime.set(Calendar.MINUTE, Common.getHourOfDayandMinute(context,Common.spDateMinute));
        alarmTime.set(Calendar.SECOND, 0);
        Calendar now = Calendar.getInstance();
        if (alarmTime.before(now)) {
            alarmTime.add(Calendar.DATE, 1);
        }
        // Get the number of milliseconds for the alarm time
        long alarmTimeMillis = alarmTime.getTimeInMillis();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DateBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Common.spDateCode, intent, 0);
        AlarmManager.AlarmClockInfo alarmClockInfo=new AlarmManager.AlarmClockInfo(alarmTimeMillis, pendingIntent);
        alarmManager.setAlarmClock(alarmClockInfo,pendingIntent);
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTimeMillis, pendingIntent);
        Log.d("xyz", "Daily set");
//
        Log.d("xyz", "BroadcastREceiver: Daily");
        Intent i=new Intent(context, SpeakingService.class);
        i.putExtra("time", Common.spDateTime);
        context.startService(i);

    }
}