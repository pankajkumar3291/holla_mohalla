package com.hollamohalla2.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.afollestad.bridge.Bridge;
import com.afollestad.bridge.BridgeException;
import com.afollestad.bridge.Request;
import com.github.lzyzsd.circleprogress.CircleProgress;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.hollamohalla2.R;
import com.hollamohalla2.utility.MyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.views.MapView;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class DownloadApiMapActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private static final int LOCATION = 1;
    private MapView map;
    CircleProgress circleProgress;
    private FusedLocationProviderClient mFusedLocationClient;
    ProgressDialog progressDialog;

    String category = "";
    String subcategory = "";


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
        setContentView(R.layout.activity_main);
        circleProgress = (CircleProgress) findViewById(R.id.circle_progress);




/*for(int i=0;i<10;i++)
{
    com.hollamohalla2.Activity.CATEGORY category = com.hollamohalla2.Activity.CATEGORY.findById(com.hollamohalla2.Activity.CATEGORY.class,i);
    SubCategoryActivity subCategory = new SubCategoryActivity(i+",",)

}*/



    /*    //geoPackageFile = new File(Environment.getExternalStorageDirectory() +"/osmdroid/MapquestOSM.zip");

        *//*Splash.Builder splash = new Splash.Builder(this, getSupportActionBar());
        splash.perform();
*//*
        //Context ctx = getApplicationContext();
        //important! set your user agent to prevent getting banned from the osm servers
        // Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

       *//* geoPackageFile = new File(Environment.getExternalStorageDirectory().getPath()+"/osmdroid/"+R.raw.berlinmap);

        if(!geoPackageFile.exists())
        {
            try {
                geoPackageFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }*//*

        String path = Environment.getExternalStorageDirectory() + "/osmdroid";

        File file = new File(Environment.getExternalStorageDirectory() + "/osmdroid");
        if(!file.exists())
        {
            boolean result =  file.mkdir();
        }





        setContentView(R.layout.activity_main);


        map = (MapView) findViewById(R.id.map);

        map.setTileSource( new XYTileSource("berlinmap", 0, 17, 256, ".png", new String[0]));

        Marker startMarker = new Marker(map);
        startMarker.setPosition(BERLIN);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(startMarker);

        Configuration.getInstance().setDebugMode(true);



        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        map.setUseDataConnection(true);

        map.getTileProvider();
        IMapController mapController = map.getController();
        mapController.setCenter(BERLIN);
        mapController.animateTo(BERLIN);
        mapController.setZoom(6);
        */


        String[] perms = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

        if (EasyPermissions.hasPermissions(this, perms)) {
            initial();
            // Already have permission, do the thing
            // ...
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.location_rationale),
                    LOCATION, perms);
        }


    }

    @AfterPermissionGranted(LOCATION)
    private void initial() {



        progressDialog = new ProgressDialog(DownloadApiMapActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.pleasewait));
        progressDialog.show();


        final Thread th2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    JSONObject jsonObject = new JSONObject();

                    try {

                        if (MyUtil.getCurrentLocation(getApplicationContext()) == null) {
                            if (ActivityCompat.checkSelfPermission(DownloadApiMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(DownloadApiMapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                                    .addOnSuccessListener(DownloadApiMapActivity.this, new OnSuccessListener<Location>() {
                                        @Override
                                        public void onSuccess(Location location) {
                                            // Got last known location. In some rare situations this can be null.
                                            if (location != null) {
                                                // Logic to handle location object
                                                MyUtil.setCurrentLocation(getApplicationContext(), location);
                                                Intent intent = getIntent();
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                            }
                                            else
                                            {
                                                Intent intent = new Intent(getApplicationContext(),CategoryActivity.class);
                                                startActivity(intent);
                                                finish();

                                            }
                                        }
                                    });
                        }
                        else
                        {
                            jsonObject.put("lat",MyUtil.getCurrentLocation(getApplicationContext()).getLatitude()+"");
                            jsonObject.put("lng",MyUtil.getCurrentLocation(getApplicationContext()).getLongitude()+"");
                            Request request = Bridge
                                    .post(MyUtil.URLSUBCATEGORY)
                                    .body(jsonObject)
                                    .request();

                            JSONObject response =request.response().asJsonObject();
                            SharedPreferences sharedPreferences  = getSharedPreferences("fe",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("subcategory",response.toString());
                            editor.apply();

                            Intent intent = new Intent(getApplicationContext(),CategoryActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (BridgeException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
        });




         Thread th1 =    new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Request request = Bridge
                            .get(MyUtil.URLCATEGORY)
                            .request();

                    JSONArray response =request.response().asJsonArray();
                    SharedPreferences sharedPreferences  = getSharedPreferences("fe",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("category",response.toString());
                    editor.apply();
                    th2.start();
                    progressDialog.dismiss();



                } catch (BridgeException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();

                    SharedPreferences sharedPreferences  = getSharedPreferences("fe",MODE_PRIVATE);
                    String cat = sharedPreferences.getString("category",null);
                    if(cat!=null)
                    {
                        Intent intent = new Intent(getApplicationContext(),CategoryActivity.class);
                        startActivity(intent);
                        finish();
                    }














                }
            }
        });



        th1.start();



    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {




    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }









}
