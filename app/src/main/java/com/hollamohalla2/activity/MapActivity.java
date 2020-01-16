package com.hollamohalla2.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CustomCap;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.hollamohalla2.adapters.SubCategoryAdapter;
import com.hollamohalla2.directionJsonParser.DirectionsJSONParser;
import com.hollamohalla2.R;
import com.hollamohalla2.utility.MyUtil;
import com.orhanobut.logger.Logger;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Overlay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hollamohalla2.service.ChatHeadService.chatHeadImage;

public class MapActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.map)
    com.google.android.gms.maps.MapView mMapView;

    @BindView(R.id.steptaken)
    TextView tvSteps;

    @BindView(R.id.meterdistance)
    TextView tvmeterTime;





    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    public static GeoPoint CURRENT;
    com.google.android.gms.maps.model.Marker mCurrentLocMarker;
    GoogleMap gmap;
    /*  for single data*/
    LatLng latlngSingle = null;
    String title = null;
    String cName = null;

    /* for list data*/
    List<SubCategoryAdapter> subCategoryAdapterList;


    private List<Overlay> mapOverlays;
    private Projection projection;


    ArrayList<Marker> markerArrayList;
    private String distance;

    PolylineOptions poly;
    Polyline polyline;
    private String duration;
    private String timeinsec;
    private String mtime;
    private String steps;
    LatLngBounds.Builder builder;

    @Override
    protected void onStart() {
        super.onStart();

      /*  StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://18.217.234.39/NXSPOT/v1.1/insertLeaveASpot",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MapActivity.this, error.getLocalizedMessage() + "", Toast.LENGTH_SHORT).show();
                    }
                }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("lat", MyUtil.getCurrentLocation(getApplicationContext()).getLatitude()+"");
                params.put("lang", MyUtil.getCurrentLocation(getApplicationContext()).getLongitude() + "");

                return params;
            }

        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

*/




    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=walking";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        return url;
    }

    private class DownloadTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            String data = "";

            try {
                data = downloadUrl(params[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);

        }


    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {//  main drawer class

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }


        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            poly = new PolylineOptions();
            if(result !=null)


                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList<>();
                    List<HashMap<String, String>> path = result.get(i);
                    for (int z = 0; z < path.size(); z++) {
                        HashMap<String, String> point1 = path.get(z);
                        if (z == 0) {    // Get distance from the list
                            distance = (String) point1.get("distance");
                            mtime= (String) point1.get("meterdistance");
                            continue;
                        } else if (z == 1) { // Get duration from the list
                            duration = (String) point1.get("duration");
                            timeinsec = (String) point1.get("timeinsec");
                            continue;
                        }
                        //  tvDuration.setText(duration);
                        HashMap<String, String> point = path.get(z);
                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng latLng = new LatLng(lat, lng);
                        // builder.include(latLng);
                        points.add(latLng);
                    }
                    // tvtime.setText(distance + " in " + duration);

                    poly.addAll(points);

                    List<PatternItem> pattern = Arrays.<PatternItem>asList(
                            new Dot(), new Gap(20), new Dot(), new Gap(20));




                    poly.color(getResources().getColor(R.color.accent));
                    poly.visible(true);

                    polyline = gmap.addPolyline(poly);
                    polyline.setEndCap(
                            new CustomCap(BitmapDescriptorFactory.fromResource(R.drawable.foot),
                                    16));

                    polyline.setStartCap(new CustomCap(BitmapDescriptorFactory.fromResource(R.drawable.foot)));
                    polyline.setPattern(pattern);



                    tvmeterTime.setText(getResources().getString(R.string.estimatetime)+" "+duration+"");

                    int steps = (int) (Integer.valueOf(mtime) *1.3123);

                    tvSteps.setText(getResources().getString(R.string.totalsteps)+" "+steps+"");

                    builder = new LatLngBounds.Builder();

                    for (Marker marker : markerArrayList) {
                        builder.include(marker.getPosition());
                    }
                    LatLngBounds bounds = builder.build();

                    int padding = 150; // offset from edges of the map in pixels
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    gmap.animateCamera(cu);

                }

        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately
        try {
            MapsInitializer.initialize(getApplicationContext().getApplicationContext());

        } catch (Exception e) {
            e.printStackTrace();
        }


        if (getIntent().hasExtra("title") && getIntent().hasExtra("lat") && getIntent().hasExtra("lang")) {
            latlngSingle = new LatLng(Double.valueOf(getIntent().getStringExtra("lat")), Double.valueOf(getIntent().getStringExtra("lang")));
            title = getIntent().getStringExtra("title");


        }

        if (getIntent().hasExtra("cname")) {
            cName = getIntent().getStringExtra("cname");

        }

        if (getIntent().hasExtra("list")) {

            subCategoryAdapterList = (ArrayList<SubCategoryAdapter>) getIntent().getSerializableExtra("list");
        }


        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                gmap = googleMap;
                LatLng current = null;
                CameraPosition cameraPosition = null;


                try {
                    current = new LatLng(MyUtil.getCurrentLocation(getApplicationContext()).getLatitude(), MyUtil.getCurrentLocation(getApplicationContext()).getLongitude());


                    Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("current", "drawable", getApplicationContext().getPackageName()));
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, 100, 100, false);

                    MarkerOptions markerOptions = new MarkerOptions().position(current)
                            .title(getResources().getString(R.string.currentlocation))
                            .snippet(getResources().getString(R.string.yourlocation))
                            .icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap));

                    mCurrentLocMarker = googleMap.addMarker(markerOptions);  // adding current location
                    cameraPosition = new CameraPosition.Builder().target(current).zoom(19).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    markerArrayList = new ArrayList<>();
                    markerArrayList.add(mCurrentLocMarker);

                    if (latlngSingle != null && title != null) {
                        Bitmap resizedBitmap1 = Bitmap.createScaledBitmap(getBitmap(title), 100, 100, false);
                        MarkerOptions markerOptions1 = new MarkerOptions().position(latlngSingle)
                                .title(title)
                        /*       .snippet("Your Location")*/
                                .icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap1));
                        googleMap.addMarker(markerOptions1);
                    } else if (subCategoryAdapterList != null) {

                        setMarkerForAllData(current);


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


                gmap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(final Marker marker) {

                        try {
                            if(marker.getTitle().equalsIgnoreCase(getResources().getString(R.string.currentlocation)))
                            {
                                TastyToast.makeText(getApplicationContext(),getResources().getString(R.string.thisisyourloc),TastyToast.LENGTH_SHORT,TastyToast.INFO).show();
                            }
                            else
                            {

                                MaterialDialog dialog = new MaterialDialog.Builder(MapActivity.this)
                                        .title(R.string.location)
                                        .content(marker.getTitle())
                                        .positiveText(R.string.navigate)
                                        .negativeText(R.string.dismiss)
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                opengoogleintent(marker);
                                            }
                                        })
                                        .show();

                              /*  new LovelyStandardDialog(MapActivity.this, LovelyStandardDialog.ButtonLayout.VERTICAL)
                                        .setTopColorRes(R.color.md_blue_400)
                                        .setButtonsColorRes(R.color.md_deep_orange_900)
                                        .setIcon(R.drawable.ic_action_info)
                                        .setTitle("Location")
                                        .setMessage(marker.getTitle())
                                        .setPositiveButton(R.string.navigate, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                opengoogleintent(marker);

                                            }
                                        })
                                        .setNegativeButton(R.string.dismiss, null)
                                        .show();
*/




                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                            opengoogleintent(marker);
                        }


                        return false;
                    }
                });
            }
        });

    }

    private void setMarkerForAllData(LatLng latLngcurrent) {

        for (int i = 0; i < subCategoryAdapterList.size(); i++) {

            LatLng latLng = new LatLng(Double.valueOf(subCategoryAdapterList.get(i).getLat()), Double.valueOf(subCategoryAdapterList.get(i).getLng()));

            //  Bitmap resizedBitmap1 = Bitmap.createScaledBitmap(getBitmap(title), 100, 100, false);

            MarkerOptions markerOptions1 = new MarkerOptions().position(latLng)
                    .title(subCategoryAdapterList.get(i).getName())
                        /*       .snippet("Your Location")*/
                    .icon(BitmapDescriptorFactory.fromBitmap(getBitmap(cName)));

            if(i==0)
            {
                markerArrayList .add(gmap.addMarker(markerOptions1));
            }
            else
            {
                gmap.addMarker(markerOptions1);
            }




/*
            gmap.addPolyline(new PolylineOptions()
                    .add(latLngcurrent, new LatLng(Double.valueOf(subCategoryAdapterList.get(i).getLat()), Double.valueOf(subCategoryAdapterList.get(i).getLng())))
                    .width(5)
                    .color(Color.RED));*/






        }


        LatLng current = new LatLng(Double.valueOf(MyUtil.getCurrentLocation(getApplication()).getLatitude()),Double.valueOf(MyUtil.getCurrentLocation(getApplication()).getLongitude()));



        LatLng dest = new LatLng(Double.valueOf(subCategoryAdapterList.get(0).getLat()),Double.valueOf(subCategoryAdapterList.get(0).getLng()));


        String url = getDirectionsUrl(current, dest);
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(url);


    }

    @Override
    protected void onResume() {
        super.onResume();
        buildGoogleApiClient();
        mGoogleApiClient.connect();


        if(chatHeadImage!=null)
        {

            chatHeadImage.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000); // Update location every second

        try {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                // public void onRequestPermissionsResult(int requestCode, String[] permissions,
                // int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, new com.google.android.gms.location.LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    Logger.i(location + "");
                    mLastLocation = location;

                    if (mCurrentLocMarker != null) {
                        LatLng current = null;
                        current = new LatLng(location.getLatitude(), location.getLongitude());
                        mCurrentLocMarker.setPosition(current);

                        MyUtil.setCurrentLocation(getApplication(),location);
                        Logger.i("logger",mLastLocation);

                    }


                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        //  mapController.zoomToSpan(Math.abs(mLastLocation.getLatitude() - mLastLocation.getLatitude()), Math.abs(current.getLongitudeE6() - destination.getLongitudeE6());
        Logger.i(mLastLocation + "");
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
    }

    public void openStreetMap(Context context) {


       /* Context ctx = getApplicationContext();
        // important! set your user agent to prevent getting banned from the osm servers
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        map.setTileSource( new XYTileSource("berlinmap", 0, 15, 256, ".png", new String[0]));
        Configuration.getInstance().setDebugMode(true);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        map.setUseDataConnection(true);
        map.getTileProvider();

        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        map.setUseDataConnection(false);*/


    }


    public Bitmap getBitmap(String name) {
        Bitmap bitmap = null;


        if (cName.contains("Langar")) {
            bitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("langer", "drawable", getApplicationContext().getPackageName()));
            bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);

        } else if (cName.contains("Civil Control Room")) {
            bitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("ccr", "drawable", getApplicationContext().getPackageName()));
            bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
        } else if (cName.contains("Police Control Room")) {
            bitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("pcr", "drawable", getApplicationContext().getPackageName()));
            bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
        } else if (cName.contains("Traffic Control Room")) {
            bitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("tcr", "drawable", getApplicationContext().getPackageName()));
            bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
        } else if (cName.contains("Parking Spot")) {
            bitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("parkingspot", "drawable", getApplicationContext().getPackageName()));
            bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
        } else if (cName.contains("Toilet")) {
            bitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("toilet", "drawable", getApplicationContext().getPackageName()));
            bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
        } else if (cName.contains("Drinking Water")) {
            bitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("drinkingwater", "drawable", getApplicationContext().getPackageName()));
            bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
        } else if (cName.contains("Medical Team")) {
            bitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("dispensary", "drawable", getApplicationContext().getPackageName()));
            bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
        } else if (cName.contains("Exhibition Point")) {
            bitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("exhibition", "drawable", getApplicationContext().getPackageName()));
            bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
        } else if (cName.contains("Ambulance")) {
            bitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("ambulance", "drawable", getApplicationContext().getPackageName()));
            bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
        } else if (cName.contains("Transit Bus Stop")) {
            bitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("busstop", "drawable", getApplicationContext().getPackageName()));
            bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
        } else if (cName.contains("Joda Ghar")) {
            bitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("joda", "drawable", getApplicationContext().getPackageName()));
            bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
        }else if (cName.contains("Help Desk")) {
            bitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("helpdesk", "drawable", getApplicationContext().getPackageName()));
            bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
        }else if (cName.contains("Horse Treatment Centre")) {
            bitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("htr", "drawable", getApplicationContext().getPackageName()));
            bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
        }else if (cName.contains("Places Of Attraction")) {
            bitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("poa", "drawable", getApplicationContext().getPackageName()));
            bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
        }
        else
        {
            bitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("ic_action_custom", "drawable", getApplicationContext().getPackageName()));
            bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
        }


        return bitmap;


    }

    public boolean isGoogleMapsInstalled()
    {
        try
        {
            ApplicationInfo info = getApplicationContext().getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0 );
            return true;
        }
        catch(PackageManager.NameNotFoundException e)
        {
            return false;
        }
    }

    private void opengoogleintent(Marker marker) {

        if(isGoogleMapsInstalled())
        {


            if(chatHeadImage!=null)
            {
                chatHeadImage.setVisibility(View.VISIBLE);

            }

            LatLng cLatlng= new LatLng(marker.getPosition().latitude,marker.getPosition().longitude);


            String uri1 = "google.navigation:q=%f, %f &mode=w";

            Intent navIntent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(String
                    .format(Locale.US, uri1, cLatlng.latitude, cLatlng.longitude)));

            navIntent1.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            navIntent1.setPackage("com.google.android.apps.maps");
            startActivity(navIntent1);
        }
        else
        {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.apps.maps"));
            startActivity(intent);

        }

    }


    private List<SubCategoryAdapter> getFilterArrayList(final List<SubCategoryAdapter> subCategoryAdapterslist) {

        ArrayList<Double> distanceArrayList = new ArrayList<>();
        ArrayList<SubCategoryAdapter> subCategoryAdapterArrayList = new ArrayList<>();

        Logger.i(subCategoryAdapterslist+"  Old");
        if(MyUtil.getCurrentLocation(getApplicationContext())!=null)
        {
            for(int i=0;i<subCategoryAdapterslist.size();i++)
            {

                Double lat = Double.valueOf(subCategoryAdapterslist.get(i).getLat());
                Double lng =Double.valueOf(subCategoryAdapterslist.get(i).getLng());


                Double distance =  distance(MyUtil.getCurrentLocation(getApplicationContext()).getLatitude(),MyUtil.getCurrentLocation(getApplicationContext()).getLongitude(),lat,lng);
                distanceArrayList.add(distance);
                subCategoryAdapterArrayList.add(subCategoryAdapterslist.get(i));
                subCategoryAdapterArrayList.get(i).distance = distance;
                subCategoryAdapterArrayList.get(i).steps = (distance*1000) *1.3 ;

            }

            class StudentDateComparator implements Comparator<SubCategoryAdapter> {
                public int compare(SubCategoryAdapter s1, SubCategoryAdapter s2) {
                    return Double.compare(s1.distance, s2.distance);
                }
            }

            ArrayList<SubCategoryAdapter> infos = new ArrayList<SubCategoryAdapter>();
// fill array
            Collections.sort(infos, new StudentDateComparator());

//            Collections.sort(subCategoryAdapterArrayList, new Comparator< SubCategoryAdapter >() {
//                @Override public int compare(SubCategoryAdapter p1, SubCategoryAdapter p2) {
//
//                    return (int) (p1.distance- p2.distance); // Ascending
//                }
//            });


            Logger.i(subCategoryAdapterArrayList+"New");

            //   subCategoryAdapterItemAdapter.add(subCategoryAdapterArrayList);



        }




        return subCategoryAdapterArrayList;







    }


    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

}