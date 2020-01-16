package com.hollamohalla2.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.util.Log;

import com.hollamohalla2.reciver.GpsLocationReceiver;
import com.hollamohalla2.reciver.NetworkReceiver;

public class OnClearFromRecentService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("ClearFromRecentService", "Service Started");
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("ClearFromRecentService", "Service Destroyed");


    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.e("ClearFromRecentService", "END");
        //Code here
        stopSelf();
        try {
            Intent intent = new Intent(getApplicationContext(), ChatHeadService.class);
            stopService(intent);


            ComponentName component = new ComponentName(OnClearFromRecentService.this, NetworkReceiver.class);
            getPackageManager().setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED , PackageManager.DONT_KILL_APP);

            ComponentName component2 = new ComponentName(OnClearFromRecentService.this, GpsLocationReceiver.class);
            getPackageManager().setComponentEnabledSetting(component2, PackageManager.COMPONENT_ENABLED_STATE_DISABLED , PackageManager.DONT_KILL_APP);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}