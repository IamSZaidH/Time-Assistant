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

public class Thirty extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        AlarmManager alarmManager2 = (AlarmManager) context.getSystemService(Service.ALARM_SERVICE);
        Intent i2 = new Intent(context, Thirty.class);
        PendingIntent pi2 = PendingIntent.getBroadcast(context, Common.thirtyCode, i2, 0);
        Calendar now = Calendar.getInstance();

        // Set the alarm time based on the current time
        Calendar alarmTime = (Calendar) now.clone();
        if (now.get(Calendar.MINUTE) < 30) {
            alarmTime.set(Calendar.MINUTE, 30);
        } else {
            alarmTime.add(Calendar.HOUR_OF_DAY, 1);
            alarmTime.set(Calendar.MINUTE, 30);
        }


        long alarmMillis = alarmTime.getTimeInMillis();

        AlarmManager.AlarmClockInfo alarmClockInfo2=new AlarmManager.AlarmClockInfo(alarmMillis,pi2);
        alarmManager2.setAlarmClock(alarmClockInfo2,pi2);

// Set the alarm using setExactAndAllowWhileIdle to ensure it fires even in Doze mode

//        alarmManager2.setExact(AlarmManager.RTC_WAKEUP, alarmMillis, pi2);

        Log.d("xyz", "Thirty: Thirty alarm Set "+alarmTime.getTime().toString());



//        Log.d("xyz", "BroadcastREceiver: Thirty");
        Intent i = new Intent(context, SpeakingService.class);
        i.putExtra("time", Common.thirtySHPREF);
        context.startService(i);
    }
}