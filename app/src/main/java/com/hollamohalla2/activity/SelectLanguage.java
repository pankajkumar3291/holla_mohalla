package com.hollamohalla2.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.hollamohalla2.R;
import com.hollamohalla2.utility.MyUtil;
import com.orhanobut.hawk.Hawk;

import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class SelectLanguage extends AppCompatActivity implements  EasyPermissions.PermissionCallbacks{

    private FusedLocationProviderClient mFusedLocationClient;


    @Override
    protected void onStart() {
        super.onStart();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            MyUtil.setCurrentLocation(getApplicationContext(), location);
                        }
                    }
                });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        String[] perms = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            // ...
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                MyUtil.setCurrentLocation(getApplicationContext(), location);
                            }
                        }
                    });




        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.location_rationale),
                    1, perms);
        }
        if(Hawk.get("alreadylogin",false))
        {
            Intent i = new Intent(SelectLanguage.this, DownloadApiMapActivity.class);
            startActivity(i);

            // close this activity
            finish();
        }
        setContentView(R.layout.activity_select_language);
        ButterKnife.bind(this);










    }



    @OnClick(R.id.FButton)
    public void setPunjabi()
    {
        Hawk.put("alreadylogin",true);
        Hawk.put("lang",2);  //punjabi
        updateLocale(getApplicationContext(),"pb");
        Intent i = new Intent(SelectLanguage.this, DownloadApiMapActivity.class);
        startActivity(i);
        // close this activity
        finish();
    }

    @OnClick(R.id.FButton2)
    public void setEnglish()
    {
        Hawk.put("alreadylogin",true);
        Hawk.put("lang",1);  // english
        updateLocale(getApplicationContext(),"eng");
        Intent i = new Intent(SelectLanguage.this, DownloadApiMapActivity.class);
        startActivity(i);
        // close this activity
        finish();
    }
    public static void updateLocale(Context context, String localeCode) {
        // Update application locale
        Locale newLocale = new Locale(localeCode);

        Configuration config = new Configuration();
        config.locale = newLocale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        Locale.setDefault(newLocale);
    }


    @AfterPermissionGranted(1)
    public void getLocation()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            MyUtil.setCurrentLocation(getApplicationContext(), location);
                        }
                    }
                });


    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        getLocation();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
