package com.zaid.timeassistant.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zaid.timeassistant.Common;
import com.zaid.timeassistant.services.SpeakingService;

public class Reminder extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.d("xyz", "BroadcastREceiver: Reminder Broadcast");
        Intent i=new Intent(context, SpeakingService.class);
        i.putExtra("time", Common.spReminderTime);
        context.startService(i);
    }
}