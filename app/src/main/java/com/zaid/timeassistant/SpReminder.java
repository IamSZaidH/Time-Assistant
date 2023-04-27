package com.zaid.timeassistant;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.zaid.timeassistant.receiver.Reminder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SpReminder extends AppCompatActivity implements View.OnClickListener {
    private final int UNIQID = 7210;
    private TextView selectTime_tv, displaTime, swText, selectDate_tv;
    private ImageView selectTime_iv, selectDate_iv;
    private MaterialButton testBtn;
    private EditText sentence;
    private Switch sw;

    public void init() {
        sentence = findViewById(R.id.sentence);
        sentence.setText(Common.getSentence(SpReminder.this));
        selectTime_tv = findViewById(R.id.selectTimeTextView);
        selectTime_tv.setOnClickListener(this);
        selectTime_iv = findViewById(R.id.selectTimeImageView);
        selectTime_iv.setOnClickListener(this);
        selectDate_iv = findViewById(R.id.selectDateImageView);
        selectDate_iv.setOnClickListener(this);
        selectDate_tv = findViewById(R.id.selectDateTextView);
        selectDate_tv.setOnClickListener(this);
        String dateDisplay = Common.getSpDateTime(SpReminder.this, Common.spReminderDate);
//        Set Date if already set.
        if (dateDisplay.isEmpty()) {
            selectDate_tv.setText("Select Date");
        } else {
            selectDate_tv.setText(dateDisplay);
        }
        testBtn = findViewById(R.id.testBtn);
        testBtn.setOnClickListener(this);
        displaTime = findViewById(R.id.displayTime);
        sw = findViewById(R.id.switchTextbtn);
        sw.setOnClickListener(this);
        swText = findViewById(R.id.switchText);
        selectTime_tv.setText(Common.getSpDateTime(SpReminder.this, Common.spReminderTime));
        if (Common.getSHPref(Common.spReminderswitch, SpReminder.this)) {
            sw.setChecked(true);
            swText.setText("On");
        } else {
            sw.setChecked(false);
            swText.setText("Off");
        }
        if (Common.getSpDateTime(SpReminder.this, Common.spReminderTime).isEmpty()) {
            selectTime_tv.setText("Select Time");
        } else {
            selectTime_tv.setText(Common.getSpDateTime(SpReminder.this, Common.spReminderTime));
        }

        displaTime.setText(Common.getSpDateTime(SpReminder.this, Common.spReminderTime));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_reminder);
//        code here
        init();
        Speaker.init(getApplicationContext());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.selectTimeImageView:
            case R.id.selectTimeTextView:
                popTimePicker();
                break;
            case R.id.selectDateImageView:
            case R.id.selectDateTextView:
                datePicker();
                break;
            case R.id.testBtn:
                Speaker.speakIt(sentence.getText().toString());
                Common.setSentence(sentence.getText().toString(), SpReminder.this);
                break;
            case R.id.switchTextbtn:
                Common.setSentence(sentence.getText().toString(), SpReminder.this);
                if (!selectTime_tv.getText().equals("Select Time") && !selectDate_tv.getText().equals("Select Date")) {
                    if (sw.isChecked()) {

//                    now set the alarm
                        String selectedDateStr = selectDate_tv.getText().toString();
                        String selectedTimeStr = selectTime_tv.getText().toString();

                        // Parse the date and time strings into a Date object
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());
                        Date selectedDateTime = null;
                        try {
                            selectedDateTime = dateFormat.parse(selectedDateStr + " " + selectedTimeStr);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }

                        // Convert the Date object to a Calendar object
                        Calendar alarmTime = Calendar.getInstance();
                        alarmTime.setTime(selectedDateTime);

                        // Get the current date and time
                        Calendar now = Calendar.getInstance();

// Check if the alarm time is in the future
                        boolean isFuture = alarmTime.after(now);

// If the alarm time is in the future, set the alarm
                        if (isFuture) {
                            // Get the number of milliseconds for the alarm time
                            long alarmTimeMillis = alarmTime.getTimeInMillis();
                            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                            Intent intent = new Intent(SpReminder.this, Reminder.class);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(SpReminder.this, UNIQID, intent, 0);
                            AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(alarmTimeMillis, pendingIntent);
                            alarmManager.setAlarmClock(alarmClockInfo, pendingIntent);
                            swText.setText("on");
                            sw.setChecked(true);
                            Common.setShPref(Common.spReminderswitch, true, SpReminder.this);
                            Log.d("xyz", "Reminder has Set");
                        } else {
                            // The alarm time is in the past, do not set the alarm
                            Toast.makeText(this,"You can not select Past time",Toast.LENGTH_SHORT).show();
                            Common.setSpDateTime(SpReminder.this, Common.spReminderDate,"Select Date");
                            swText.setText("Off");
                            sw.setChecked(false);
                            Common.setShPref(Common.spReminderswitch, false, SpReminder.this);
                            Log.d("xyz", "Selected date and time is in the past");
                        }


                    } else {
                        swText.setText("off");
                        sw.setChecked(false);
                        Common.setShPref(Common.spReminderswitch, false, SpReminder.this);
//                    cancel the alarm
                        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        Intent intent = new Intent(SpReminder.this, Reminder.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(SpReminder.this, UNIQID, intent, 0);
                        alarmManager.cancel(pendingIntent);
                        Log.d("xyz", "Reminder is canceled");
                    }
                } else {
                    swText.setText("off");
                    sw.setChecked(false);
                    Toast.makeText(this, "Please Select the Date & Time.\nBefore Enabling the Reminder", Toast.LENGTH_SHORT).show();
                }
        }
    }

    public void popTimePicker() {
        Calendar alarmTime = Calendar.getInstance();

// Create a TimePickerDialog to allow the user to select the alarm time
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    // Set the alarm time on the calendar object
                    alarmTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    alarmTime.set(Calendar.MINUTE, minute);
                    alarmTime.set(Calendar.SECOND, 0);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                    String timeString = dateFormat.format(alarmTime.getTime());

                    // Display the selected time on the TextView
                    selectTime_tv.setText(timeString);
                    displaTime.setText(timeString);
                    Common.setSpDateTime(SpReminder.this, Common.spReminderTime, timeString);

                    // Check if the selected time is in the past, if so, add one day to the calendar object
//                    if (alarmTime.getTimeInMillis() < System.currentTimeMillis()) {
//                        alarmTime.add(Calendar.DAY_OF_YEAR, 1);
//                    }

                    // Get the number of milliseconds for the alarm time
                    long alarmTimeMillis = alarmTime.getTimeInMillis();
                    Common.setSpDateTimeMilli(SpReminder.this, Common.spReminderTimeMilli, alarmTimeMillis);
//                        setDailyAlarm(alarmTimeMillis);
                    if (sw.isChecked()) {
                        sw.setChecked(false);
                        swText.setText("Off");
                        Common.setShPref(Common.spDateTimeSwitch, false, SpReminder.this);
                    }


                },
                // Set the initial time on the TimePickerDialog to the current time
                alarmTime.get(Calendar.HOUR_OF_DAY),
                alarmTime.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(this)
        );

// Show the TimePickerDialog to the user
        timePickerDialog.show();
    }

    public void datePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, // Context
                (view, year, monthOfYear, dayOfMonth) -> {
                    // Handle the date selection
                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.set(year, monthOfYear, dayOfMonth);
                    Date selectedDate = calendar1.getTime();

                    // Create a SimpleDateFormat object to format the selected date as a string
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

                    // Format the selected date as a string and set it in the TextView
                    selectDate_tv.setText(dateFormat.format(selectedDate));
                    Common.setSpDateTime(SpReminder.this, Common.spReminderDate, dateFormat.format(selectedDate));
                },
                // Set the initial date to display in the dialog
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onBackPressed() {
        Common.setSentence(sentence.getText().toString(), SpReminder.this);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        Common.setSentence(sentence.getText().toString(), SpReminder.this);
        super.onDestroy();
    }
}