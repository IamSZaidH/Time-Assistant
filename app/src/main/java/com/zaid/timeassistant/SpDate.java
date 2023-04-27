package com.zaid.timeassistant;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.zaid.timeassistant.receiver.DateBroadcastReceiver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SpDate extends AppCompatActivity {
    TextView time, speakDate;
    ImageView timepickerclock;
    Switch dailySwitch;

    public void init() {
        speakDate = findViewById(R.id.speakDate);
        time = findViewById(R.id.time);
        if (!Common.getSpDateTime(SpDate.this, Common.spDateTime).isEmpty()) {
            time.setText(Common.getSpDateTime(SpDate.this, Common.spDateTime));
        }
        timepickerclock = findViewById(R.id.timepickerclock);
        dailySwitch = findViewById(R.id.spDateReapeat);
        Speaker.init(SpDate.this);
        dailySwitch.setChecked(Common.getSHPref(Common.spDateTimeSwitch, SpDate.this));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_date);

        // Initialize UI elements
        init();

        // Set click listeners for time text and clock icon
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popTimePicker();
            }
        });
        timepickerclock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popTimePicker();
            }
        });
        dailySwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dailySwitch.isChecked()) {
                    startAlarm(getApplicationContext());
                } else {
//                    stop alarm
                    stopAlarm();


                }
            }
        });
        speakDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Speaker.speakDate();
            }
        });
    }

    public void popTimePicker() {
        Calendar alarmTime = Calendar.getInstance();
        // Create a TimePickerDialog to allow the user to select the alarm time
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            // Set the alarm time on the calendar object
            Common.setSpDateHourOfDayandMinute(getApplicationContext(), Common.spDateHourOFDay, hourOfDay);
            Common.setSpDateHourOfDayandMinute(getApplicationContext(), Common.spDateMinute, minute);
            alarmTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            alarmTime.set(Calendar.MINUTE, minute);
            alarmTime.set(Calendar.SECOND, 0);
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            String timeString = dateFormat.format(alarmTime.getTime());

            // Display the selected time on the TextView
            time.setText(timeString);
            Common.setSpDateTime(SpDate.this, Common.spDateTime, timeString);

            // Check if the selected time is in the past, if so, add one day to the calendar object
            Calendar now = Calendar.getInstance();
            if (alarmTime.before(now)) {
                alarmTime.add(Calendar.DATE, 1);
            }

            // Get the number of milliseconds for the alarm time
            long alarmTimeMillis = alarmTime.getTimeInMillis();
            Common.setSpDateTimeMilli(SpDate.this, Common.spDateTimeMilli, alarmTimeMillis);

            if (dailySwitch.isChecked()) {
                startAlarm(getApplicationContext());
            } else {
                // Stop alarm
                stopAlarm();
            }
        },
                // Set the initial time on the TimePickerDialog to the current time
                alarmTime.get(Calendar.HOUR_OF_DAY),
                alarmTime.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(this)
        );
        timePickerDialog.show();
    }

    private void startAlarm(Context context) {
        Calendar alarmTime = Calendar.getInstance();
        alarmTime.set(Calendar.HOUR_OF_DAY, Common.getHourOfDayandMinute(context, Common.spDateHourOFDay));
        alarmTime.set(Calendar.MINUTE, Common.getHourOfDayandMinute(context, Common.spDateMinute));
        alarmTime.set(Calendar.SECOND, 0);
        Calendar now = Calendar.getInstance();
        if (alarmTime.before(now)) {
            alarmTime.add(Calendar.DATE, 1);
            Log.d("xyz", "Daily Alarm: Tomorrow ");
        }
        Log.d("xyz", "Daily Alarm: today ");

        // Get the number of milliseconds for the alarm time

        if (time.getText().equals("") || time.getText().equals("Select Time")) {
            Toast.makeText(SpDate.this, "Please Set the Time Before Enable", Toast.LENGTH_SHORT).show();
            Common.setShPref(Common.spDateTimeSwitch, false, SpDate.this);
        } else {
            if (Common.getSpDateTimeMilli(SpDate.this, Common.spDateTimeMilli) == Long.MIN_VALUE) {
                Toast.makeText(SpDate.this, "Please Set the Time Before Enable", Toast.LENGTH_SHORT).show();
                Common.setShPref(Common.spDateTimeSwitch, false, SpDate.this);
            } else {
////                            Start Alarm
                long alarmTimeMillis = alarmTime.getTimeInMillis();
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(context, DateBroadcastReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Common.spDateCode, intent, 0);
                AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(alarmTimeMillis, pendingIntent);
                alarmManager.setAlarmClock(alarmClockInfo, pendingIntent);
                Log.d("xyz", "Daily set");
                Common.setShPref(Common.spDateTimeSwitch, true, SpDate.this);
            }
        }


    }

    private void stopAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(SpDate.this, DateBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(SpDate.this, Common.spDateCode, intent, 0);
        alarmManager.cancel(pendingIntent);
        Log.d("xyz", "Daily cancel");
        Common.setShPref(Common.spDateTimeSwitch, false, SpDate.this);
        dailySwitch.setChecked(false);
    }
}
// Show a
