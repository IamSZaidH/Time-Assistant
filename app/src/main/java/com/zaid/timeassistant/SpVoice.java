package com.zaid.timeassistant;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.zaid.timeassistant.adaptor.voiceAdapter;

import java.util.LinkedHashMap;

public class SpVoice extends AppCompatActivity {
    private ListView listView;
    private TextView textViewTitle;
    private LinkedHashMap<String, String> voices;

    private void init() {
        listView = findViewById(R.id.listView);
        setVoice();
        textViewTitle = findViewById(R.id.voiceTitle);
        textViewTitle.setText("Set " + Common.getLang(getApplicationContext()).toUpperCase() + " Voices");
    }

    private void setVoice() {
        if (Common.getLang(getApplicationContext()).equals(Common.english)) {
            setEnglishVoices();
            voiceAdapter vadp = new voiceAdapter(getApplicationContext(), voices);
            listView.setAdapter(vadp);
        } else if (Common.getLang(getApplicationContext()).equals(Common.hindi)) {
            setHindiVoices();
            voiceAdapter vadp = new voiceAdapter(getApplicationContext(), voices);
            listView.setAdapter(vadp);
        } else if (Common.getLang(getApplicationContext()).equals(Common.urdu)) {
            setUrduVoices();
            voiceAdapter vadp = new voiceAdapter(getApplicationContext(), voices);
            listView.setAdapter(vadp);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_voice);
        init();
    }

    private void setEnglishVoices() {
        voices = new LinkedHashMap<String, String>();
        voices.put("FEMALE-1", "en-US-language");
        voices.put("FEMALE-2", "en-us-x-iob-local");
        voices.put("FEMALE-3", "en-us-x-iog-local");
        voices.put("FEMALE-4", "en-us-x-sfg-local");
        voices.put("FEMALE-5", "en-us-x-tpc-local");
        voices.put("FEMALE-6", "en-us-x-tpf-local");
        voices.put("MALE-1", "en-us-x-iol-local");
        voices.put("MALE-2", "en-us-x-iom-local");
        voices.put("MALE-3", "en-us-x-tpd-local");
    }

    private void setHindiVoices() {
        voices = new LinkedHashMap<String, String>();
        voices.put("FEMALE-1", "hi-IN-language");
        voices.put("FEMALE-2", "hi-in-x-cfn-local");
        voices.put("FEMALE-3", "hi-in-x-hia-local");
        voices.put("FEMALE-4", "hi-in-x-hic-local");
        voices.put("MALE-1", "hi-in-x-hid-local");
        voices.put("MALE-2", "hi-in-x-hie-local");

    }

    private void setUrduVoices() {
        voices = new LinkedHashMap<String, String>();
        voices.put("FEMALE", "ur-pk-x-cfn-local");
        voices.put("MALE", "ur-pk-x-urm-local");
    }
}