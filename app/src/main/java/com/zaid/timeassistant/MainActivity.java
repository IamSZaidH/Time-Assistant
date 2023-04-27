package com.zaid.timeassistant;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private WebView clock;
    private LinearLayout speakingC, lang, voice, spDate,spReminder;

    public void init() {

        clock = findViewById(R.id.clock1);
        speakingC = findViewById(R.id.speakingClock);
        speakingC.setOnClickListener(this);
        lang = findViewById(R.id.lang);
        lang.setOnClickListener(this);
        voice = findViewById(R.id.voice);
        voice.setOnClickListener(this);
        spDate=findViewById(R.id.spDate);
        spDate.setOnClickListener(this);
        spReminder=findViewById(R.id.spReminder);
        spReminder.setOnClickListener(this);
//        String x = String.valueOf(isServiceRunning(SpeakHourlyService.class));
//        Toast.makeText(this, x, Toast.LENGTH_SHORT).show();
//        if (!Boolean.valueOf(x)){
//
//            Common.setShPref(Common.hourlySHPREF,false,getApplicationContext());
//            Common.setShPref(Common.thirtySHPREF,false,getApplicationContext());
//            Common.setShPref(Common.fiftySHPREF,false,getApplicationContext());
//        }
//        Allow in background permission

        Intent intent = new Intent();
        String packageName = getPackageName();
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        if(!pm.isIgnoringBatteryOptimizations(packageName)) {
            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + packageName));
            startActivityForResult(intent, 185); // request code 123
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 185) { // check if the request code matches
            Intent intent = new Intent();
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                // User allowed the app to ignore battery optimizations
                // Perform any necessary actions here
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivityForResult(intent, 185);
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // code here
        init();
        clock.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        clock.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        clock.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        clock.getSettings().setJavaScriptEnabled(true);
        clock.loadUrl("file:///android_asset/clock1.html");
        Speaker.init(getApplicationContext());


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.speakingClock:

                startActivity(new Intent(MainActivity.this, SpTime.class));
                break;
            case R.id.lang:
                startActivity(new Intent(MainActivity.this, SpLang.class));
                break;
            case R.id.voice:
                startActivity(new Intent(MainActivity.this, SpVoice.class));
                break;
            case R.id.spDate:
                startActivity(new Intent(MainActivity.this, SpDate.class));
                break;
            case R.id.spReminder:
                startActivity(new Intent(MainActivity.this, SpReminder.class));
                break;
        }
    }


}