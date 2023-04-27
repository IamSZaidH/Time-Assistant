package com.zaid.timeassistant.services;

import android.app.Service;
import android.content.Intent;
import android.media.AudioAttributes;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.util.Log;

import com.zaid.timeassistant.Common;
import com.zaid.timeassistant.Speaker;

import java.util.Locale;

public class SpeakingService extends Service implements TextToSpeech.OnInitListener {

    @Override
    public void onInit(int status) {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build();
        if (status == TextToSpeech.SUCCESS) {
            // Set the language and voice here
            String[] arr = Common.getLangCountry(Common.getLang(getApplicationContext()));
            Locale locale = new Locale(arr[0], arr[1]);
            Speaker.tts.setLanguage(locale);
            Speaker.tts.setAudioAttributes(audioAttributes);
            Speaker.tts.setVoice(new Voice(Common.getVoice(getApplicationContext()), locale, Voice.QUALITY_NORMAL, Voice.LATENCY_VERY_HIGH, false, null));
        } else {
            Log.e("TTS", "Initialization failed");
        }
    }

    @Override
    public void onCreate() {
        Speaker.context = getApplicationContext();
        Log.d("xyz", "Service: Speaker init");
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("xyz", "Spekin Service" + intent.getStringExtra("time"));
        if (Speaker.tts == null) {
            Speaker.tts = new TextToSpeech(this, this);
        } else {
            if (intent.getStringExtra("time").equals(Common.spDateTime)) {
                Speaker.speakIt(Common.nowDateis());
                Log.d("xyz", "Service: noTHeDateis()");

            } else if (intent.getStringExtra("time").equals(Common.spReminderTime)) {

                if (Common.getSentence(getApplicationContext()).isEmpty()) {
                    Speaker.speakIt("This is the Default reminder. You can set your own in Sentence in Hindi,Urdu and English language.");
                } else {

                    Speaker.speakIt(Common.getSentence(getApplicationContext()));
                }
                Common.setShPref(Common.spReminderswitch, false, getApplicationContext());
                Log.d("xyz", "Service: Reminder running");

            } else if (intent.getStringExtra("time").contains(Common.hourlySHPREF)) {
                Speaker.speakHourly();
                Log.d("xyz", "Service: Speaker speakHourly()");
            } else if (intent.getStringExtra("time").contains(Common.thirtySHPREF)) {
                Speaker.speakThirty();
                Log.d("xyz", "Service: Speaker speakThirty()");
            } else if (intent.getStringExtra("time").contains(Common.fiftySHPREF)) {
                Speaker.speakFifteen();
                Log.d("xyz", "Service: Speaker speakThirteen()");
            } else {
                Speaker.speakIt("Now the Time is. " + Common.nowTimeis().get("hour") + ":" + Common.nowTimeis().get("minute"));
            }
        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }
}