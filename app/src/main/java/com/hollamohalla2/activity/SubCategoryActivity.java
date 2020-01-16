package com.hollamohalla2.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.hollamohalla2.adapters.SubCategoryAdapter;
import com.hollamohalla2.R;
import com.hollamohalla2.utility.MyUtil;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;
import com.orhanobut.logger.Logger;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubCategoryActivity extends AppCompatActivity {


    @BindView(R.id.rcSubCategory)
    RecyclerView recyclerView;


    JSONArray jsonArray;
    ItemAdapter<SubCategoryAdapter> subCategoryAdapterItemAdapter;
    List<SubCategoryAdapter> subCategoryAdapterslist;
    LinearLayoutManager linearLayoutManager;
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
                            MyUtil.setCurrentLocation(getApplicationContext(),location);


                        }
                    }
                });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subcategory);
        ButterKnife.bind(this);
        subCategoryAdapterslist = new ArrayList<>();
        if(getIntent().hasExtra("subcategory"))
        {
            String s = getIntent().getStringExtra("subcategory");
            try {
                jsonArray = new JSONArray(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            for(int i=0;i<jsonArray.length();i++)
            {
                SubCategoryAdapter subCategoryAdapter = new SubCategoryAdapter();
                try {
                    subCategoryAdapter.setLat(jsonArray.getJSONObject(i).getString("lat"));
                    subCategoryAdapter.setLng(jsonArray.getJSONObject(i).getString("lng"));
                    subCategoryAdapter.setName(jsonArray.getJSONObject(i).getString("title"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                subCategoryAdapterslist.add(subCategoryAdapter);
            }
        }

        //   getFilterArrayList(subCategoryAdapterslist);









        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
//create the ItemAdapter holding your Items
        subCategoryAdapterItemAdapter = new ItemAdapter();
//create the managing FastAdapter, by passing in the itemAdapter
        FastAdapter fastAdapter = FastAdapter.with(subCategoryAdapterItemAdapter);

//set our adapters to the RecyclerView


        final List<SubCategoryAdapter> subCategoryAdapterList  = getFilterArrayList(subCategoryAdapterslist);

        //Collections.reverse(subCategoryAdapterList);


        subCategoryAdapterItemAdapter.add(subCategoryAdapterList);

        recyclerView.setAdapter(fastAdapter);



        fastAdapter.withSelectable(true);

        fastAdapter.withOnClickListener(new OnClickListener<SubCategoryAdapter>() {
            @Override
            public boolean onClick(View v, IAdapter<SubCategoryAdapter> adapter, SubCategoryAdapter item, final int position) {
                try {

                    MaterialDialog dialog = new MaterialDialog.Builder(SubCategoryActivity.this)
                            .title(R.string.location)
                            .content(item.getName())
                            .positiveText(R.string.navigate)
                            .negativeText(R.string.dismiss)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    if(isGoogleMapsInstalled())
                                    {

                                        String uri1 = "google.navigation:q=%f,%f &mode=w";
                                        double v1 = Double.valueOf(subCategoryAdapterList.get(position).getLat());
                                        double v2 = Double.valueOf(subCategoryAdapterList.get(position).getLng());

                                        Intent navIntent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(String
                                                .format(Locale.US, uri1,v1 ,v2 )));

                                        navIntent1.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                        navIntent1.setPackage("com.google.android.apps.maps");
                                        startActivity(navIntent1);
                                    }
                                }
                            })
                            .show();


                } catch (Exception e) {
                    e.printStackTrace();
                    TastyToast.makeText(getApplicationContext(),"No Data",TastyToast.LENGTH_SHORT,TastyToast.INFO).show();
                }


                return true;
            }
        });
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

    public  void showAll(View view)
    {

        Intent intent = new Intent(getApplicationContext(),MapActivity.class);
        intent.putExtra("list",(Serializable) subCategoryAdapterslist);
        intent.putExtra("cname",getIntent().getStringExtra("cname"));
        startActivity(intent);





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





}
