package com.zaid.timeassistant;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

public class SpLang extends AppCompatActivity implements View.OnClickListener {
    RelativeLayout h, e, u;
    ImageView ish, ise, isu;

    private void init() {
        h = findViewById(R.id.hindi);
        h.setOnClickListener(this);
        e = findViewById(R.id.english);
        e.setOnClickListener(this);
        u = findViewById(R.id.urdu);
        u.setOnClickListener(this);
        ish = findViewById(R.id.hindiImg);
        ise = findViewById(R.id.englishImg);
        isu = findViewById(R.id.urduImg);

    }

    private void setLanguageCheck() {
        if (Common.getLang(getApplicationContext()).contains(Common.hindi)) {
            ish.setImageResource(android.R.drawable.checkbox_on_background);
            ise.setImageResource(android.R.color.transparent);
            isu.setImageResource(android.R.color.transparent);
        } else if (Common.getLang(getApplicationContext()).contains(Common.english)) {
            ise.setImageResource(android.R.drawable.checkbox_on_background);
            isu.setImageResource(android.R.color.transparent);
            ish.setImageResource(android.R.color.transparent);
        } else if (Common.getLang(getApplicationContext()).contains(Common.urdu)) {
            isu.setImageResource(android.R.drawable.checkbox_on_background);
            ise.setImageResource(android.R.color.transparent);
            ish.setImageResource(android.R.color.transparent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_lang);
        init();
        setLanguageCheck();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.english:
                Common.setSpLang(getApplicationContext(), Common.english);
                setLanguageCheck();
                Speaker.init(getApplicationContext());
                break;
            case R.id.hindi:
                Common.setSpLang(getApplicationContext(), Common.hindi);
                setLanguageCheck();
                Speaker.init(getApplicationContext());

                break;
            case R.id.urdu:
                Common.setSpLang(getApplicationContext(), Common.urdu);
                setLanguageCheck();
                Speaker.init(getApplicationContext());

                break;
        }
    }
}