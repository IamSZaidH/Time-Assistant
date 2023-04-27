package com.zaid.timeassistant.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zaid.timeassistant.Common;
import com.zaid.timeassistant.services.SpeakingService;

import java.util.Calendar;

public class Hourly extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.d("xyz", "BroadcastREceiver: Houly");
//        Setting the next alarm
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Service.ALARM_SERVICE);
        Intent intent1 = new Intent(context, Hourly.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, Common.hourlyCode, intent1, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + 1);

// Set the alarm using setExactAndAllowWhileIdle to ensure it fires even in Doze mode
        AlarmManager.AlarmClockInfo alarmClockInfo=new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(),pi);
        alarmManager.setAlarmClock(alarmClockInfo,pi);
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);

        Log.d("xyz", "Hourly: Set Hourly alarm");

//
        Intent i = new Intent(context, SpeakingService.class);
        i.putExtra("time", Common.hourlySHPREF);
        context.startService(i);
    }
}