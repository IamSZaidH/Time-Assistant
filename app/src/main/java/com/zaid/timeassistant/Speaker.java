package com.zaid.timeassistant;

import android.content.Context;
import android.media.AudioAttributes;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.util.Log;

import java.util.Locale;
import java.util.UUID;


public class Speaker {
    public static TextToSpeech tts;
    public static Context context;

    public static void init(Context context) {
        Speaker.context = context;
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build();
        tts = new TextToSpeech(context, status -> {
            if (status == TextToSpeech.SUCCESS) {
                // Set the language and voice here
                String[] arr = Common.getLangCountry(Common.getLang(context));
                Locale locale = new Locale(arr[0], arr[1]);
                tts.setLanguage(locale);
                tts.setAudioAttributes(audioAttributes);
                tts.setVoice(new Voice(Common.getVoice(context), locale, Voice.QUALITY_NORMAL, Voice.LATENCY_VERY_HIGH, false, null));
            } else {
                Log.d("xyz", "Initialization failed");
            }
        });
    }

    public static void speakHourly() {
        if (tts != null) {
            String x = speakHourSentence();

            String utteranceId = UUID.randomUUID().toString();
            tts.speak(x, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
        } else {
            restartTTS(context);
        }
    }

    private static String speakHourSentence() {
        String hour = Common.nowTimeis().get("hour");
        int hourInt = Integer.parseInt(hour);
        if (Common.getLang(context).contains(Common.english)) {
            return "Now the Time is. " + hourInt + " o'clock";
        } else if (Common.getLang(context).contains(Common.hindi)) {
            String x;
            if (hourInt == 1) {
                x = hourInt + " बज गया है";
            } else {
                x = hourInt + " बज गये है";
            }
            return x;

        } else if (Common.getLang(context).contains(Common.urdu)) {
            //            String x=hourInt+Common.nowTimeis().get("am_pm");
            return "وقت ہے۔ . " + hourInt + ":00" + Common.nowTimeis().get("am_pm") + ". ہے.";
        } else {
            return hourInt + " " + Common.nowTimeis().get("am_pm");
        }

    }


    public static void speakThirty() {
        if (tts != null) {

            String x = speakThirtySentence();
            String utteranceId = UUID.randomUUID().toString();
            tts.speak(x, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
        } else {
            restartTTS(context);
        }
    }

    private static String speakThirtySentence() {
        String hour = Common.nowTimeis().get("hour");
        int hourInt = Integer.parseInt(hour);
        if (Common.getLang(context).contains(Common.english)) {
            return "Now the Time is. " + Common.nowTimeis().get("hour") + ":30";
        } else if (Common.getLang(context).contains(Common.hindi)) {
            String x;
            if (hourInt == 1) {
                x = "डेढ़ बज गया है";
                return x;
            } else if (hourInt == 2) {
                x = "ढाई बज गये है";
                return x;
            } else {
                x = "साढ़े  " + hourInt + " बज गये है";
                return x;
            }

        } else if (Common.getLang(context).contains(Common.urdu)) {
            return "وقت ہے۔ . " + hourInt + ":30" + Common.nowTimeis().get("am_pm") + ". ہے.";
        } else {
            return hourInt + " " + Common.nowTimeis().get("am_pm");
        }

    }

    public static void speakFifteen() {
        if (tts != null) {
            String x = speakFifteenSentence();

            String utteranceId = UUID.randomUUID().toString();
            tts.speak(x, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
        } else {
            restartTTS(context);
        }
    }

    private static String speakFifteenSentence() {
        String hour = Common.nowTimeis().get("hour");
        String minute = Common.nowTimeis().get("minute");
        int hourInt = Integer.parseInt(hour);
        int mimuteInt = Integer.parseInt(minute);
        String x;
        if (Common.getLang(context).contains(Common.english)) {
            x = "Now the Time is. " + Common.nowTimeis().get("hour") + ":" + Common.nowTimeis().get("minute");
            return x;
        } else if (Common.getLang(context).contains(Common.hindi)) {
            if (mimuteInt <= 20) {
                x = "सवा " + hourInt + " बज गये है";
                return x;
            } else if (mimuteInt >= 45) {
                x = "पौने " + (hourInt == 12 ? 1 : hourInt + 1) + " बज गये है";
                return x;
            }

        } else if (Common.getLang(context).contains(Common.urdu)) {
            if (mimuteInt <= 20) {
                x = "سوا " + hourInt + " بج چکے ہیں";
                return x;
            } else if (mimuteInt >= 45) {
                x = "پونے " + (hourInt == 12 ? 1 : hourInt + 1) + " بج چکے ہیں";
                return x;
            }
        } else {
            return hourInt + " " + Common.nowTimeis().get("am_pm");
        }

        return hour;
    }

    public static void speakIt(String x) {
        if (tts != null) {
            String utteranceId = UUID.randomUUID().toString();
            tts.speak(x, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
        } else {
            restartTTS(context);
        }
    }

    public static void restartTTS(Context context) {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            init(context);
        }
    }

    public static void speakDate() {
        String date = Common.nowDateis();
        String sentence = "";
        if (Common.getLang(context).equals(Common.english)) {
            sentence = "Now the date is. " + date;
        } else if (Common.getLang(context).equals(Common.hindi)) {
            sentence = "आज . " + date + " है।";
        } else if (Common.getLang(context).equals(Common.urdu)) {
            sentence =" آج. "+date+" ہے ";

        }
        else{
            sentence=date;
        }
        Speaker.speakIt(sentence);

    }

}
