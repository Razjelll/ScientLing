package com.dyszlewskiR.edu.scientling.services.notification;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Razjelll on 26.03.2017.
 */

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context,Intent intent){
        Log.d(getClass().getName(), "onReceive");
        Intent notificationService = new Intent(context, NotificationService.class);
        context.startService(notificationService);
    }
}
