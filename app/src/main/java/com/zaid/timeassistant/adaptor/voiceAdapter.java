package com.zaid.timeassistant.adaptor;

import static com.zaid.timeassistant.Speaker.tts;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.speech.tts.Voice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zaid.timeassistant.Common;
import com.zaid.timeassistant.R;
import com.zaid.timeassistant.Speaker;

import java.util.LinkedHashMap;
import java.util.Locale;


public class voiceAdapter extends BaseAdapter {
    private Context context;
    private LinkedHashMap<String, String> voices;
    private LayoutInflater inflater;

    public voiceAdapter(Context context, LinkedHashMap<String, String> voices) {
        this.context = context;
        this.voices = voices;
        this.inflater = LayoutInflater.from(context);
        Speaker.init(context);
    }


    @Override
    public int getCount() {
        return voices.size();
    }

    @Override
    public Object getItem(int position) {
        return voices.keySet().toArray()[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row, parent, false);
            holder = new ViewHolder();
            holder.relativeLayout = convertView.findViewById(R.id.main);
            holder.voiceNameTextView = convertView.findViewById(R.id.tv);
            holder.checkBoxImageView = convertView.findViewById(R.id.Img);
            holder.speakImage = convertView.findViewById(R.id.tr);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String voiceName = (String) getItem(position);
        String voiceId = voices.get(voiceName);

        holder.voiceNameTextView.setText(voiceName);

        // set the click listener on the parent layout
        holder.relativeLayout.setOnClickListener(view -> {
            // handle the click event

            for (int i = 0; i < parent.getChildCount(); i++) {
                ViewHolder otherHolder = (ViewHolder) parent.getChildAt(i).getTag();
                String otherVoiceId = voices.get(otherHolder.voiceNameTextView.getText().toString());
                if (!voiceId.equals(otherVoiceId)) {
                    otherHolder.checkBoxImageView.setImageDrawable(null);
                }
            }
            Common.setVoice(context, voiceId);
            holder.checkBoxImageView.setImageResource(android.R.drawable.checkbox_on_background);

            String[] arr = Common.getLangCountry(Common.getLang(context));
            Locale locale = new Locale(arr[0], arr[1]);
            tts.setLanguage(locale);
            tts.setVoice(new Voice(Common.getVoice(context),locale, Voice.QUALITY_NORMAL, Voice.LATENCY_VERY_LOW, false, null));

        });
        holder.speakImage.setOnClickListener(view -> {
            if (holder.checkBoxImageView.getDrawable() != null && holder.checkBoxImageView.getDrawable().getConstantState() != null
                    && holder.checkBoxImageView.getDrawable().getConstantState().equals(context.getDrawable(android.R.drawable.checkbox_on_background).getConstantState())) {

                if (Common.getLang(context).equals(Common.english)){
            Speaker.speakIt("This is an example of voice");
            } else if (Common.getLang(context).equals(Common.hindi)) {
                Speaker.speakIt("यह उदाहरण है वौइस् का ");
            } else if (Common.getLang(context).equals(Common.urdu)) {
                Speaker.speakIt("یہ مثال کے طور پر ایک آواز ہے");

            }
            }else {
                Toast.makeText(context, "Please Select the Language and then click on speak", Toast.LENGTH_LONG).show();
            }
        });
        // check if this voice is selected
        if (voiceId.equals(Common.getVoice(context))) {
            holder.checkBoxImageView.setImageResource(android.R.drawable.checkbox_on_background);
        } else {
            holder.checkBoxImageView.setImageDrawable(null);
        }



        return convertView;
    }


    static class ViewHolder {
        RelativeLayout relativeLayout;
        TextView voiceNameTextView;
        ImageView checkBoxImageView;
        ImageView speakImage;
    }
}
