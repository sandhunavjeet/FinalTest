package com.example.myapplication;
import android.content.Intent;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class MobileMessagingService extends WearableListenerService {
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        if (messageEvent.getPath().equals("/my_path")) {

            String msg = new String(messageEvent.getData());
            Intent i = new Intent();
            i.setAction(Intent.ACTION_SEND);
            i.putExtra("message", msg);

            //Broadcast the received Data Layer messages locally
            LocalBroadcastManager.getInstance(this).sendBroadcast(i);
        } else {
            super.onMessageReceived(messageEvent);
        }
    }

}