package com.example.sendmessage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;



public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

	

    /*
     * A BroadcastReceiver notifies of important Wi-Fi p2p events.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Intent serviceIntent = new Intent(context, ConnectionService.class);  // start ConnectionService
        serviceIntent.setAction(action);   // put in action and extras
        serviceIntent.putExtras(intent);
    	context.startService(serviceIntent);  // start the connection service        
    }
}

