package com.morten.hang_o_lyzer;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by Morten on 01-05-2016.
 */
public class DataLayerListenerService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        System.out.println("onMessageRecieved entered");
//        super.onMessageReceived(messageEvent);
        if("/ALKOTJEK".equals(messageEvent.getPath())) {
            final String message = new String(messageEvent.getData());
            Log.d("myTag", "Message path received on watch is: " + messageEvent.getPath());
            Log.d("myTag", "Message received on watch is: " + message);
//            Toast t = Toast.makeText(getApplicationContext(),"Message recieved",Toast.LENGTH_LONG);
//            t.show();

            Intent messageIntent = new Intent();
            messageIntent.setAction(Intent.ACTION_SEND);
            messageIntent.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);
        }
        else {
            super.onMessageReceived(messageEvent);
        }
    }
}
