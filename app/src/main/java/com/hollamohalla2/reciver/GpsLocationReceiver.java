package com.hollamohalla2.reciver;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import com.sdsmdg.tastytoast.TastyToast;

public class GpsLocationReceiver extends BroadcastReceiver {
    private String locationMode;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
            ContentResolver contentResolver = context.getContentResolver();
            // Find out what the settings say about which providers are enabled
            int mode = Settings.Secure.getInt(
                    contentResolver, Settings.Secure.LOCATION_MODE, Settings.Secure.LOCATION_MODE_OFF);

            if (mode == 0) {
                //gpsoff

                TastyToast.makeText(context,"Please Turn On Location for Hola Mohalla Suvidha",TastyToast.INFO,TastyToast.LENGTH_LONG).show();

            } else if (mode == 3) {
                //gpson


            }


        }
    }
}