package com.hollamohalla2.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.hollamohalla2.adapters.CategoryAdapter;
import com.hollamohalla2.adapters.SubCategoryAdapter;
import com.hollamohalla2.R;
import com.hollamohalla2.utility.MyUtil;
import com.hollamohalla2.service.FloatingButton;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CategoryActivity extends AppCompatActivity {
    @BindView(R.id.rcCategory)
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    private ItemAdapter itemAdapter;
    List<CategoryAdapter> stringList;
    List<SubCategoryAdapter> subCategoryAdapterslist;
    private FusedLocationProviderClient mFusedLocationClient;
    @BindView(R.id.textView2)
    TextView tvCall;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            startActivity(new Intent(getApplicationContext(), FloatingButton.class));



        subCategoryAdapterslist = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
//create the ItemAdapter holding your Items
        itemAdapter = new ItemAdapter();
//create the managing FastAdapter, by passing in the itemAdapter
        FastAdapter fastAdapter = FastAdapter.with(itemAdapter);

//set our adapters to the RecyclerView
        recyclerView.setAdapter(fastAdapter);

//set the items to your ItemAdapter

        SharedPreferences sharedPreferences = getSharedPreferences("fe",MODE_PRIVATE);
        final String s = sharedPreferences.getString("subcategory","");

        fastAdapter.withSelectable(true);
        fastAdapter.withOnClickListener(new OnClickListener<CategoryAdapter>() {
            @Override
            public boolean onClick(View v, IAdapter<CategoryAdapter> adapter, CategoryAdapter item, int position) {

                if(subCategoryAdapterslist.size()>0)
                    subCategoryAdapterslist.clear();

                ProgressDialog progressDialog = new ProgressDialog(CategoryActivity.this);
                progressDialog.setMessage("Please Wait");
                progressDialog.show();
                try {
                    JSONObject jsonObject = new JSONObject(s);

                    JSONArray data = jsonObject.getJSONArray(String.valueOf(stringList.get(position).getId()));

                    for(int i=0;i<data.length();i++)
                    {
                        SubCategoryAdapter subCategoryAdapter = new SubCategoryAdapter();
                        try {
                            subCategoryAdapter.setLat(data.getJSONObject(i).getString("lat"));
                            subCategoryAdapter.setLng(data.getJSONObject(i).getString("lng"));
                            subCategoryAdapter.setName(data.getJSONObject(i).getString("title"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        subCategoryAdapterslist.add(subCategoryAdapter);
                    }
                    progressDialog.dismiss();

                    if(isNetworkAvailable())
                    {
                        Intent intent = new Intent(getApplicationContext(),MapActivity.class);
                        intent.putExtra("list",(Serializable) subCategoryAdapterslist);
                        intent.putExtra("cname",item.getName());
                        startActivity(intent);

                    }
                    else
                    {


                        Intent intent = new Intent(getApplicationContext(),SubCategoryActivity.class);
                        JSONObject jsonObject1 = new JSONObject(s);
                        JSONArray data1 = jsonObject1.getJSONArray(String.valueOf(stringList.get(position).getId()));
                        intent.putExtra("subcategory",data1+"");
                        startActivity(intent);
                    }







                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    TastyToast.makeText(getApplicationContext(),"No Data",TastyToast.LENGTH_SHORT,TastyToast.INFO).show();
                }


                return true;
            }
        });
    }



    private void getData(final ItemAdapter itemAdapter) throws Exception {
        stringList = new ArrayList<>();
        try {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    JSONArray response = null;
                    SharedPreferences sharedPreferences  = getSharedPreferences("fe",MODE_PRIVATE);
                    String s = sharedPreferences.getString("category","");

                    try {
                        response = new JSONArray(s);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for(int i = 0; i<response.length(); i++)
                    {
                        CategoryAdapter category = new CategoryAdapter();
                        try {
                            category.setId(response.getJSONObject(i).getInt("id"));
                            category.setName(response.getJSONObject(i).getString("name"));
                            category.setNamePunjabi(response.getJSONObject(i).getString("name_punjabi"));
                            category.setCount(response.getJSONObject(i).getInt("count"));
                            stringList.add(category);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            itemAdapter.add(stringList);
                        }
                    });







                }
            }).start();







        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        if(stringList!=null)
        {
            stringList.clear();
            if(itemAdapter!=null)
                itemAdapter.clear();
        }

        try {
            getData(itemAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
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




    public void help(View view)
    {






    }
    @OnClick(R.id.textView2)
    public void call(View v)
    {



        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + tvCall.getText().toString()));
        startActivity(intent);


    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
