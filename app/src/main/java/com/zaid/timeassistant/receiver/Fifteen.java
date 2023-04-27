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

public class Fifteen extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
//
        AlarmManager alarmManager3 = (AlarmManager) context.getSystemService(Service.ALARM_SERVICE);
        Intent i3 = new Intent(context, Fifteen.class);

        PendingIntent pi3 = PendingIntent.getBroadcast(context, Common.fifteenCode, i3, 0);

        AlarmManager.AlarmClockInfo alarmClockInfo3=new AlarmManager.AlarmClockInfo(trigger15and45(),pi3);
        alarmManager3.setAlarmClock(alarmClockInfo3,pi3);
//        alarmManager3.setExact(AlarmManager.RTC_WAKEUP, trigger15and45(), pi3);
        Log.d("xyz", "onStartCommand: Quarter 15 ad 45");
//
        Log.d("xyz", "BroadcastREceiver: Fifteen and 45");
        Intent i=new Intent(context, SpeakingService.class);
        i.putExtra("time", Common.fiftySHPREF);
        context.startService(i);
    }
    private long trigger15and45(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        int minute = calendar.get(Calendar.MINUTE);
        if (minute < 15) {
            calendar.set(Calendar.MINUTE, 15);
        } else if (minute < 45) {
            calendar.set(Calendar.MINUTE, 45);
        } else {
            calendar.add(Calendar.HOUR_OF_DAY, 1);
            calendar.set(Calendar.MINUTE, 15);
        }

        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }

}