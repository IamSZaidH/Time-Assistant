package com.zaid.timeassistant;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DigitalClock;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.zaid.timeassistant.receiver.Fifteen;
import com.zaid.timeassistant.receiver.Hourly;
import com.zaid.timeassistant.receiver.Thirty;

import java.util.Calendar;
import java.util.Map;

public class SpTime extends AppCompatActivity implements View.OnClickListener {

    //    private Speak sp;
    private TextView showDate;
    private DigitalClock dc;
    private CheckBox hourly, thirty, fifteen;


    public SpTime() {
    }

    public void init() {
        showDate = findViewById(R.id.showDate);
        showDate.setOnClickListener(this);

        dc = findViewById(R.id.showTime);
        dc.setOnClickListener(this);
        hourly = findViewById(R.id.hourly);
        hourly.setOnClickListener(this);
        thirty = findViewById(R.id.thirty);
        thirty.setOnClickListener(this);
        fifteen = findViewById(R.id.fifteen);
        fifteen.setOnClickListener(this);
        Common.shPref = getSharedPreferences(Common.spTimeSharedPref, MODE_PRIVATE);
        Speaker.init(getApplicationContext());
//        Allow in background permission
        Intent intent = new Intent();
        String packageName = getPackageName();
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        if (!pm.isIgnoringBatteryOptimizations(packageName)) {
            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + packageName));
            startActivity(intent);
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sptime);
//        Code here
        init();
        Speaker.init(getApplicationContext());
        showDate.setText(Common.nowDateis());
        hourly.setChecked(Common.shPref.getBoolean(Common.hourlySHPREF, false));
        thirty.setChecked(Common.shPref.getBoolean(Common.thirtySHPREF, false));
        fifteen.setChecked(Common.shPref.getBoolean(Common.fiftySHPREF, false));
    }


    //button on click
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
//        allow in background
        Intent intent = new Intent();
        String packageName = getPackageName();
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        if (!pm.isIgnoringBatteryOptimizations(packageName)) {
            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + packageName));
            startActivity(intent);
        }


        switch (view.getId()) {
            case R.id.showDate:
//                Speak sp=new Speak(getApplicationContext());
                Speaker.speakIt(Common.nowDateis());
                break;
            case R.id.showTime:
                Map<String, String> hhmmss = Common.nowTimeis();
                String hh = hhmmss.get("hour");
                String mm = hhmmss.get("minute");
                String time = "now the time is " + hh + ":" + mm;
                Speaker.speakIt(time);

                break;

            case R.id.hourly:
                Common.setShPref(Common.hourlySHPREF, hourly.isChecked(), getApplicationContext());
//                restartService(service);

                AlarmManager alarmManager = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
                Intent i = new Intent(getApplicationContext(), Hourly.class);

                PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), Common.hourlyCode, i, 0);

                if (Common.getSHPref(Common.hourlySHPREF, getApplicationContext())) {
                    // Create an intent to trigger the repeating alarm
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + 1);
                    AlarmManager.AlarmClockInfo alarmClockInfo=new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(),pi);
                    alarmManager.setAlarmClock(alarmClockInfo,pi);
//                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);

                    Log.d("xyz", "Hourly: Set Hourly alarm");
                } else {
                    Log.d("xyz", "Hourly: Canceled");
                    alarmManager.cancel(pi);
                    pi.cancel();
                }
                break;
            case R.id.thirty:
                Common.setShPref(Common.thirtySHPREF, thirty.isChecked(), getApplicationContext());
                AlarmManager alarmManager2 = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
                Intent i2 = new Intent(getApplicationContext(), Thirty.class);
                PendingIntent pi2 = PendingIntent.getBroadcast(getApplicationContext(), Common.thirtyCode, i2, 0);


                if (Common.getSHPref(Common.thirtySHPREF, getApplicationContext())) {
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
                    Log.d("xyz", "onClick: set 30 minute alarm");
// Set the alarm using setExact(); to ensure it fires even in Doze mode
//                    alarmManager2.setExact(AlarmManager.RTC_WAKEUP, alarmMillis, pi2);
                } else {
                    Log.d("xyz", "Thirty: Canceled");
                    alarmManager2.cancel(pi2);
                    pi2.cancel();
                }
                break;
            case R.id.fifteen:
                Common.setShPref(Common.fiftySHPREF, fifteen.isChecked(), getApplicationContext());
                AlarmManager alarmManager3 = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
                Intent i3 = new Intent(getApplicationContext(), Fifteen.class);

                PendingIntent pi3 = PendingIntent.getBroadcast(getApplicationContext(), Common.fifteenCode, i3, 0);
                if (Common.getSHPref(Common.fiftySHPREF, getApplicationContext())) {
                    Log.d("xyz", "onStartCommand: Quarter 15 ad 45");
                    AlarmManager.AlarmClockInfo alarmClockInfo3=new AlarmManager.AlarmClockInfo(trigger15and45(),pi3);
                    alarmManager3.setAlarmClock(alarmClockInfo3,pi3);
//                    alarmManager3.setExact(AlarmManager.RTC_WAKEUP, trigger15and45(), pi3);
                } else {
                    Log.d("xyz", "15 and 45 : Canceled");
                    alarmManager3.cancel(pi3);
                    pi3.cancel();
                }

//                restartService(service);
                break;

        }
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