package com.hollamohalla2.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.bridge.Bridge;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnSuccessListener;
import com.hollamohalla2.R;
import com.hollamohalla2.utility.MyUtil;
import com.hollamohalla2.reciver.GpsLocationReceiver;
import com.hollamohalla2.reciver.NetworkReceiver;
import com.sdsmdg.tastytoast.TastyToast;

public class SplashActivity extends AppCompatActivity {

    private static final int REQUEST_CHECK_SETTINGS = 1;
    private FusedLocationProviderClient mFusedLocationClient;
    String category = "{\"1\":[{\"id\":34,\"category\":1,\"lat\":\"31.1779776\",\"lng\":\"76.5623921\",\"title\":\"At Gurudwara Sri Patalpuri Sahib, Kiratpur Sahib\",\"createdon\":\"22-02-2018 04:02 PM\",\"distance\":22.77},{\"id\":39,\"category\":1,\"lat\":\"31.1822814\",\"lng\":\"76.5731836\",\"title\":\"At Gurdwara Baba Gurdita Ji, Kiratpur Sahib \",\"createdon\":\"22-02-2018 04:02 PM\",\"distance\":23.43},{\"id\":111,\"category\":1,\"lat\":\"31.2003204\",\"lng\":\"76.5570084\",\"title\":\"At Nakkaian, Sri Anandpur Sahib\",\"createdon\":\"23-02-2018 05:02 PM\",\"distance\":25.16},{\"id\":110,\"category\":1,\"lat\":\"31.2088284\",\"lng\":\"76.54726\",\"title\":\"At Gurudwara Sri Guru Har Rai Ki, Sri Anandpur Sahib\",\"createdon\":\"23-02-2018 05:02 PM\",\"distance\":26},{\"id\":109,\"category\":1,\"lat\":\"31.2113911\",\"lng\":\"76.5449657\",\"title\":\"Langar From Gram Panchayat Kotla, Sri Anandpur Sahib\",\"createdon\":\"23-02-2018 05:02 PM\",\"distance\":26.27},{\"id\":108,\"category\":1,\"lat\":\"31.2148194\",\"lng\":\"76.5400221\",\"title\":\"At Gurudwara Mitha Sar Sahib, Sri Anandpur Sahib\",\"createdon\":\"23-02-2018 05:02 PM\",\"distance\":26.62},{\"id\":107,\"category\":1,\"lat\":\"31.2194494\",\"lng\":\"76.5343209\",\"title\":\"Langar At Sri Anandpur Sahib-kiratpur Road\",\"createdon\":\"23-02-2018 05:02 PM\",\"distance\":27.11},{\"id\":106,\"category\":1,\"lat\":\"31.2212185\",\"lng\":\"76.5307702\",\"title\":\"At Gurudwara Baba Bhuda Ji, Sri Anandpur Sahib\",\"createdon\":\"23-02-2018 05:02 PM\",\"distance\":27.3},{\"id\":18,\"category\":1,\"lat\":\"31.2235491\",\"lng\":\"76.5252218\",\"title\":\"At Puda Colony Ground, Sri Anandpur Sahib\",\"createdon\":\"22-02-2018 01:02 PM\",\"distance\":27.55},{\"id\":102,\"category\":1,\"lat\":\"31.2276559\",\"lng\":\"76.5201489\",\"title\":\"At Dera Bunga Baba Sher Singh Ji, Sri Anandpur Sahib\",\"createdon\":\"23-02-2018 05:02 PM\",\"distance\":28.01},{\"id\":101,\"category\":1,\"lat\":\"31.2280278\",\"lng\":\"76.519515\",\"title\":\"At Dera Bishanpuri, Sri Anandpur Sahib\",\"createdon\":\"23-02-2018 05:02 PM\",\"distance\":28.05},{\"id\":88,\"category\":1,\"lat\":\"31.2273425\",\"lng\":\"76.4947888\",\"title\":\"At Kila Lohgarh Sahib, Sri Anandpur Sahib\",\"createdon\":\"23-02-2018 04:02 PM\",\"distance\":28.12},{\"id\":100,\"category\":1,\"lat\":\"31.2291398\",\"lng\":\"76.5150848\",\"title\":\"At Gurudwara Rampur Khera, Sri Anandpur Sahib\",\"createdon\":\"23-02-2018 05:02 PM\",\"distance\":28.19},{\"id\":93,\"category\":1,\"lat\":\"31.229575\",\"lng\":\"76.5122527\",\"title\":\"At Dera Bidhi Chand, Sri Anandpur Sahib\",\"createdon\":\"23-02-2018 04:02 PM\",\"distance\":28.25},{\"id\":92,\"category\":1,\"lat\":\"31.2298287\",\"lng\":\"76.5107487\",\"title\":\"At Dam Dami Taksal, Sri Anandpur Sahib\",\"createdon\":\"23-02-2018 04:02 PM\",\"distance\":28.28},{\"id\":89,\"category\":1,\"lat\":\"31.2302229\",\"lng\":\"76.5027047\",\"title\":\"At Baba Balkar Singh Dera, Sri Anandpur Sahib\",\"createdon\":\"23-02-2018 04:02 PM\",\"distance\":28.37},{\"id\":90,\"category\":1,\"lat\":\"31.2306553\",\"lng\":\"76.505501\",\"title\":\"At Gurudwara Shahidi Bagh, Sri Anandpur Sahib\",\"createdon\":\"23-02-2018 04:02 PM\",\"distance\":28.4},{\"id\":91,\"category\":1,\"lat\":\"31.2306557\",\"lng\":\"76.505517\",\"title\":\"At Kila Anandgarh Sahib, Sri Anandpur Sahib\",\"createdon\":\"23-02-2018 04:02 PM\",\"distance\":28.4},{\"id\":17,\"category\":1,\"lat\":\"31.2306394\",\"lng\":\"76.4998897\",\"title\":\"At Northern Railway Station, Sri Anandpur Sahib\",\"createdon\":\"22-02-2018 01:02 PM\",\"distance\":28.44},{\"id\":87,\"category\":1,\"lat\":\"31.2328275\",\"lng\":\"76.4971039\",\"title\":\"At SGPC Ground, Sri Anandpur Sahib\",\"createdon\":\"23-02-2018 04:02 PM\",\"distance\":28.7},{\"id\":25,\"category\":1,\"lat\":\"31.2347051\",\"lng\":\"76.5000561\",\"title\":\"Langar Hall At Gurudwara Sri Kesgarh Sahib Sri Anandpur Sahib\",\"createdon\":\"22-02-2018 02:02 PM\",\"distance\":28.89},{\"id\":80,\"category\":1,\"lat\":\"31.2392733\",\"lng\":\"76.4648406\",\"title\":\"Ghra, Agampur, Sri Anandpur Sahib\",\"createdon\":\"23-02-2018 04:02 PM\",\"distance\":29.85},{\"id\":86,\"category\":1,\"lat\":\"31.2641942\",\"lng\":\"76.4558887\",\"title\":\"At Village Surewal, Nangal Road\",\"createdon\":\"23-02-2018 04:02 PM\",\"distance\":32.73},{\"id\":85,\"category\":1,\"lat\":\"31.2690071\",\"lng\":\"76.4483003\",\"title\":\"At Gurudwara, Vill. Dher, Nangal Road\",\"createdon\":\"23-02-2018 04:02 PM\",\"distance\":33.4},{\"id\":83,\"category\":1,\"lat\":\"31.2732596\",\"lng\":\"76.4410638\",\"title\":\"At Gurudwara Shahidi Bagh Majari, Nangal Road\",\"createdon\":\"23-02-2018 04:02 PM\",\"distance\":34.02}],\"2\":[{\"id\":6,\"category\":2,\"lat\":\"31.2371508\",\"lng\":\"76.4933399\",\"title\":\"At SDM Office\",\"createdon\":\"22-02-2018 12:02 PM\",\"distance\":29.22}],\"3\":[{\"id\":20,\"category\":3,\"lat\":\"31.2286495\",\"lng\":\"76.515041\",\"title\":\"Main Police Control Room At Police Station, Sri Anandpur Sahib\",\"createdon\":\"22-02-2018 01:02 PM\",\"distance\":28.13}],\"4\":[{\"id\":31,\"category\":4,\"lat\":\"31.2287934\",\"lng\":\"76.5148207\",\"title\":\"At Police Station, Sri Anandpur Sahib\",\"createdon\":\"22-02-2018 04:02 PM\",\"distance\":28.15}],\"5\":[{\"id\":19,\"category\":5,\"lat\":\"31.2231648\",\"lng\":\"76.5250833\",\"title\":\"Parking At Puda Colony Ground, Sri Anandpur Sahib\",\"createdon\":\"22-02-2018 01:02 PM\",\"distance\":27.51},{\"id\":14,\"category\":5,\"lat\":\"31.2389538\",\"lng\":\"76.4762239\",\"title\":\"At Dana Mandi, Sri Anandpur Sahib\",\"createdon\":\"22-02-2018 01:02 PM\",\"distance\":29.62},{\"id\":13,\"category\":5,\"lat\":\"31.2497075\",\"lng\":\"76.4794315\",\"title\":\"At Shivalik View Public School, Sri Anandpur Sahib\",\"createdon\":\"22-02-2018 01:02 PM\",\"distance\":30.76}],\"6\":[{\"id\":114,\"category\":6,\"lat\":\"31.1786737\",\"lng\":\"76.5632721\",\"title\":\"At Gurudwara Patalpuri Sahib, Kiratpur Sahib \",\"createdon\":\"23-02-2018 05:02 PM\",\"distance\":22.86},{\"id\":119,\"category\":6,\"lat\":\"31.1813308\",\"lng\":\"76.5642814\",\"title\":\"At Anaj Mandi, Kiratpur Sahib \",\"createdon\":\"23-02-2018 05:02 PM\",\"distance\":23.17},{\"id\":136,\"category\":6,\"lat\":\"31.1828315\",\"lng\":\"76.5675288\",\"title\":\"From Police Station To Bilaspur Road Infront Of Dhabas\",\"createdon\":\"23-02-2018 06:02 PM\",\"distance\":23.39},{\"id\":40,\"category\":6,\"lat\":\"31.1824279\",\"lng\":\"76.573331\",\"title\":\"At Gurudwara Baba Gurdita Ji, Kiratpur Sahib \",\"createdon\":\"22-02-2018 04:02 PM\",\"distance\":23.44},{\"id\":125,\"category\":6,\"lat\":\"31.1892806\",\"lng\":\"76.5640657\",\"title\":\"At ACC Dump, Kiratpur Sahib \",\"createdon\":\"23-02-2018 05:02 PM\",\"distance\":24.04},{\"id\":33,\"category\":6,\"lat\":\"31.2235491\",\"lng\":\"76.5252218\",\"title\":\"Toilet Puda Colony Ground, Sri Anandpur Sahib\",\"createdon\":\"22-02-2018 04:02 PM\",\"distance\":27.55},{\"id\":104,\"category\":6,\"lat\":\"31.2248579\",\"lng\":\"76.523604\",\"title\":\"At Puda Colony Ground, Sri Anandpur Sahib \",\"createdon\":\"23-02-2018 05:02 PM\",\"distance\":27.7},{\"id\":71,\"category\":6,\"lat\":\"31.226345\",\"lng\":\"76.4941573\",\"title\":\"Sen Sec School, Lodhipur, Sri Anandpur Sahib\",\"createdon\":\"23-02-2018 04:02 PM\",\"distance\":28.01},{\"id\":98,\"category\":6,\"lat\":\"31.2283909\",\"lng\":\"76.5144622\",\"title\":\"Backside Of Police Station, Sri Anandpur Sahib \",\"createdon\":\"23-02-2018 04:02 PM\",\"distance\":28.11},{\"id\":68,\"category\":6,\"lat\":\"31.2313339\",\"lng\":\"76.500107\",\"title\":\"Railway Station Grid Infront Of SGPC GROUND, APs\",\"createdon\":\"23-02-2018 03:02 PM\",\"distance\":28.51},{\"id\":61,\"category\":6,\"lat\":\"31.2323449\",\"lng\":\"76.497744\",\"title\":\"Infront Of SGPC GROUND, Sri Anandpur Sahib \",\"createdon\":\"23-02-2018 03:02 PM\",\"distance\":28.64},{\"id\":24,\"category\":6,\"lat\":\"31.2353995\",\"lng\":\"76.5008817\",\"title\":\"Toilets At Gurudwara Sri Kesgarh Sahib, Sri Anandpur Sahib\",\"createdon\":\"22-02-2018 02:02 PM\",\"distance\":28.96},{\"id\":78,\"category\":6,\"lat\":\"31.2373081\",\"lng\":\"76.474902\",\"title\":\"Infront Of Agampur Petrol Pump, Sri Anandpur Sahib\",\"createdon\":\"23-02-2018 04:02 PM\",\"distance\":29.46},{\"id\":74,\"category\":6,\"lat\":\"31.2389542\",\"lng\":\"76.4882847\",\"title\":\"Near Railway Fatak, At Agampur T Point\",\"createdon\":\"23-02-2018 04:02 PM\",\"distance\":29.47},{\"id\":29,\"category\":6,\"lat\":\"31.2389538\",\"lng\":\"76.4762239\",\"title\":\"Toilet Dana Mandi, Sri Anandpur Sahib\",\"createdon\":\"22-02-2018 03:02 PM\",\"distance\":29.62},{\"id\":27,\"category\":6,\"lat\":\"31.249703\",\"lng\":\"76.4793067\",\"title\":\"Toilets At Shivalik View Public School, Sri Anandpur Sahib\",\"createdon\":\"22-02-2018 03:02 PM\",\"distance\":30.76}],\"7\":[{\"id\":113,\"category\":7,\"lat\":\"31.1786737\",\"lng\":\"76.5632721\",\"title\":\"At Gurudwara Patalpuri Sahib, Kiratpur Sahib \",\"createdon\":\"23-02-2018 05:02 PM\",\"distance\":22.86},{\"id\":118,\"category\":7,\"lat\":\"31.1813308\",\"lng\":\"76.5642814\",\"title\":\"At Anaj Mandi, Kiratpur Sahib \",\"createdon\":\"23-02-2018 05:02 PM\",\"distance\":23.17},{\"id\":135,\"category\":7,\"lat\":\"31.1828315\",\"lng\":\"76.5675288\",\"title\":\"From Police Station To Bilaspur Road Infront Of Dhabas\",\"createdon\":\"23-02-2018 06:02 PM\",\"distance\":23.39},{\"id\":38,\"category\":7,\"lat\":\"31.1820538\",\"lng\":\"76.5734082\",\"title\":\"Drinking Water At Gurudwara Baba Gurdita Ji, Kiratpur Sahib \",\"createdon\":\"22-02-2018 04:02 PM\",\"distance\":23.4},{\"id\":124,\"category\":7,\"lat\":\"31.1892818\",\"lng\":\"76.5640633\",\"title\":\"At ACC Dump, Kiratpur Sahib \",\"createdon\":\"23-02-2018 05:02 PM\",\"distance\":24.04},{\"id\":32,\"category\":7,\"lat\":\"31.2235491\",\"lng\":\"76.5252218\",\"title\":\"Drinking water Puda Colony Ground, Sri Anandpur Sahib\",\"createdon\":\"22-02-2018 04:02 PM\",\"distance\":27.55},{\"id\":103,\"category\":7,\"lat\":\"31.2248579\",\"lng\":\"76.5236043\",\"title\":\"At Puda Colony Ground, Sri Anandpur Sahib\",\"createdon\":\"23-02-2018 05:02 PM\",\"distance\":27.7},{\"id\":72,\"category\":7,\"lat\":\"31.226345\",\"lng\":\"76.4941573\",\"title\":\"Sen. Sec. School, Lodhipur, Sri Anandpur Sahib\",\"createdon\":\"23-02-2018 04:02 PM\",\"distance\":28.01},{\"id\":97,\"category\":7,\"lat\":\"31.2283909\",\"lng\":\"76.5144622\",\"title\":\"Backside Of Police Station, Sri Anandpur Sahib\",\"createdon\":\"23-02-2018 04:02 PM\",\"distance\":28.11},{\"id\":67,\"category\":7,\"lat\":\"31.2311648\",\"lng\":\"76.5003303\",\"title\":\"At Railway Station Grid, Imfront Of SGPC Ground, Sri Anandpur Sahib\",\"createdon\":\"23-02-2018 03:02 PM\",\"distance\":28.49},{\"id\":62,\"category\":7,\"lat\":\"31.2323449\",\"lng\":\"76.497744\",\"title\":\"Infront Of SGPC GROUND, Sri Anandpur Sahib \",\"createdon\":\"23-02-2018 03:02 PM\",\"distance\":28.64},{\"id\":23,\"category\":7,\"lat\":\"31.2353565\",\"lng\":\"76.5006109\",\"title\":\"Drinking Water At Sri Kesgarh Sahib, Sri Anandpur Sahib\",\"createdon\":\"22-02-2018 02:02 PM\",\"distance\":28.95},{\"id\":77,\"category\":7,\"lat\":\"31.2373081\",\"lng\":\"76.474902\",\"title\":\"Infront\n" +
            " Of Agampur Petrol Pump, Sri Anandpur Sahib\",\"createdon\":\"23-02-2018 04:02 PM\",\"distance\":29.46},{\"id\":75,\"category\":7,\"lat\":\"31.2389926\",\"lng\":\"76.4881776\",\"title\":\"Near Railway Fatak, At Agampur T Poimy, Sri Anandpur Sahib\",\"createdon\":\"23-02-2018 04:02 PM\",\"distance\":29.47},{\"id\":30,\"category\":7,\"lat\":\"31.2389538\",\"lng\":\"76.4762239\",\"title\":\"Drinking water Dana Mandi, Sri Anandpur Sahib\",\"createdon\":\"22-02-2018 03:02 PM\",\"distance\":29.62},{\"id\":28,\"category\":7,\"lat\":\"31.2497031\",\"lng\":\"76.4793066\",\"title\":\"Drinking Water at Shivalik View Public School, Sri Anandpur Sahib\",\"createdon\":\"22-02-2018 03:02 PM\",\"distance\":30.76}],\"8\":[{\"id\":127,\"category\":8,\"lat\":\"31.1831409\",\"lng\":\"76.5669198\",\"title\":\"At Police Station, Kiratpur Sahib \",\"createdon\":\"23-02-2018 05:02 PM\",\"distance\":23.41},{\"id\":128,\"category\":8,\"lat\":\"31.1867989\",\"lng\":\"76.5658583\",\"title\":\"At Gurudwara Charan Kamal, Kiratpur Sahib \",\"createdon\":\"23-02-2018 06:02 PM\",\"distance\":23.79},{\"id\":122,\"category\":8,\"lat\":\"31.1872121\",\"lng\":\"76.5646677\",\"title\":\"At Bus Stand Shri Kiratpur Sahib \",\"createdon\":\"23-02-2018 05:02 PM\",\"distance\":23.82},{\"id\":95,\"category\":8,\"lat\":\"31.2294514\",\"lng\":\"76.5136673\",\"title\":\"At Sri Guru Teg Bahadur Khalsa College, Sri Anandpur Sahib\",\"createdon\":\"23-02-2018 04:02 PM\",\"distance\":28.23},{\"id\":65,\"category\":8,\"lat\":\"31.2302984\",\"lng\":\"76.5014523\",\"title\":\"At Railway Station, Sri Anandpur Sahib\",\"createdon\":\"23-02-2018 03:02 PM\",\"distance\":28.39},{\"id\":64,\"category\":8,\"lat\":\"31.2306495\",\"lng\":\"76.5057524\",\"title\":\"Kila Anandgarh Sahib, Sri Anandpur Sahib\",\"createdon\":\"23-02-2018 03:02 PM\",\"distance\":28.4},{\"id\":57,\"category\":8,\"lat\":\"31.2331658\",\"lng\":\"76.4966668\",\"title\":\"Verka Chownk, Sri Anandpur Sahib\",\"createdon\":\"23-02-2018 03:02 PM\",\"distance\":28.74},{\"id\":58,\"category\":8,\"lat\":\"31.2346046\",\"lng\":\"76.498013\",\"title\":\"Museum Chownk, Sri Anandpur Sahib\",\"createdon\":\"23-02-2018 03:02 PM\",\"distance\":28.89},{\"id\":10,\"category\":8,\"lat\":\"31.2390962\",\"lng\":\"76.4952006\",\"title\":\"At Civil Hospital, Sri Anandpur Sahib\",\"createdon\":\"22-02-2018 01:02 PM\",\"distance\":29.41},{\"id\":47,\"category\":8,\"lat\":\"31.2373115\",\"lng\":\"76.4751449\",\"title\":\"Agampur, Sri Anandpur Sahib\",\"createdon\":\"23-02-2018 03:02 PM\",\"distance\":29.46},{\"id\":51,\"category\":8,\"lat\":\"31.2426205\",\"lng\":\"76.5006672\",\"title\":\"At Charan Ganga Stadium, Sri Anandpur Sahib\",\"createdon\":\"23-02-2018 03:02 PM\",\"distance\":29.76},{\"id\":43,\"category\":8,\"lat\":\"31.2415254\",\"lng\":\"76.4863533\",\"title\":\"At Mazara Chownk, Sri Anandpur Sahib\",\"createdon\":\"23-02-2018 02:02 PM\",\"distance\":29.77}],\"11\":[{\"id\":21,\"category\":11,\"lat\":\"31.227814\",\"lng\":\"76.5141681\",\"title\":\"At Sri Guru Teg Bahadur Khalsa College, Sri Anandpur Sahib\",\"createdon\":\"22-02-2018 02:02 PM\",\"distance\":28.04}],\"15\":[{\"id\":129,\"category\":15,\"lat\":\"31.184175\",\"lng\":\"76.5683844\",\"title\":\"At PHC, Kiratpur Sahib \",\"createdon\":\"23-02-2018 06:02 PM\",\"distance\":23.55},{\"id\":121,\"category\":15,\"lat\":\"31.1872188\",\"lng\":\"76.5646713\",\"title\":\"At Bus Stand Shri Kiratpur Sahib \",\"createdon\":\"23-02-2018 05:02 PM\",\"distance\":23.82},{\"id\":94,\"category\":15,\"lat\":\"31.2294513\",\"lng\":\"76.5136672\",\"title\":\"At Sri Guru Teg Bahadur Khalsa College, Sri Anandpur Sahib\",\"createdon\":\"23-02-2018 04:02 PM\",\"distance\":28.23},{\"id\":66,\"category\":15,\"lat\":\"31.2302983\",\"lng\":\"76.5014524\",\"title\":\"At Railway Station Sri Anandpur Sahib \",\"createdon\":\"23-02-2018 03:02 PM\",\"distance\":28.39},{\"id\":59,\"category\":15,\"lat\":\"31.2346257\",\"lng\":\"76.498027\",\"title\":\"Near Kesgarh Sahib, Sri Anandpur Sahib\",\"createdon\":\"23-02-2018 03:02 PM\",\"distance\":28.89},{\"id\":11,\"category\":15,\"lat\":\"31.2391124\",\"lng\":\"76.4945854\",\"title\":\"Service At Civil Hospital, Sri Anandpur Sahib\",\"createdon\":\"22-02-2018 01:02 PM\",\"distance\":29.42},{\"id\":42,\"category\":15,\"lat\":\"31.2415359\",\"lng\":\"76.486371\",\"title\":\"At Majara Chownk\",\"createdon\":\"23-02-2018 02:02 PM\",\"distance\":29.77}],\"16\":[{\"id\":138,\"category\":16,\"lat\":\"31.2231654\",\"lng\":\"76.5266326\",\"title\":\"At Jhinjri On Sri Anandpur Sahib-kiratpur Road \",\"createdon\":\"23-02-2018 06:02 PM\",\"distance\":27.51},{\"id\":81,\"category\":16,\"lat\":\"31.2373462\",\"lng\":\"76.4746602\",\"title\":\"At Agampur Dana Mandi\",\"createdon\":\"23-02-2018 04:02 PM\",\"distance\":29.47},{\"id\":26,\"category\":16,\"lat\":\"31.2496881\",\"lng\":\"76.4793743\",\"title\":\"At Shivalik View Public School, Sri Anandpur Sahib\",\"createdon\":\"22-02-2018 03:02 PM\",\"distance\":30.76}],\"17\":[{\"id\":35,\"category\":17,\"lat\":\"31.1774757\",\"lng\":\"76.5629009\",\"title\":\"At Gurudwara Patalpuri Sahib, Kiratpur Sahib\",\"createdon\":\"22-02-2018 04:02 PM\",\"distance\":22.72},{\"id\":37,\"category\":17,\"lat\":\"31.1820339\",\"lng\":\"76.5735721\",\"title\":\"At Gurudwara Baba Gurdita Ji, Kiratpur Sahib\",\"createdon\":\"22-02-2018 04:02 PM\",\"distance\":23.41},{\"id\":56,\"category\":17,\"lat\":\"31.2347605\",\"lng\":\"76.4951507\",\"title\":\"At Bus Stand, Sri Anandpur Sahib\",\"createdon\":\"23-02-2018 03:02 PM\",\"distance\":28.93},{\"id\":22,\"category\":17,\"lat\":\"31.235338\",\"lng\":\"76.4993983\",\"title\":\"At Sri Kesgarh Sahib, Sri Anandpur Sahib\",\"createdon\":\"22-02-2018 02:02 PM\",\"distance\":28.96}],\"18\":[{\"id\":115,\"category\":18,\"lat\":\"31.1786737\",\"lng\":\"76.5632721\",\"title\":\"At Gurudwara Sri Patalpuri Sahib, Kiratpur Sahib \",\"createdon\":\"23-02-2018 05:02 PM\",\"distance\":22.86},{\"id\":120,\"category\":18,\"lat\":\"31.1813309\",\"lng\":\"76.5642814\",\"title\":\"At Anaj Mandi, Kiratpur Sahib \",\"createdon\":\"23-02-2018 05:02 PM\",\"distance\":23.17},{\"id\":137,\"category\":18,\"lat\":\"31.1828315\",\"lng\":\"76.5675288\",\"title\":\"From Police Station To Bilaspur Road Infront Of Dhabas\",\"createdon\":\"23-02-2018 06:02 PM\",\"distance\":23.39},{\"id\":126,\"category\":18,\"lat\":\"31.1892806\",\"lng\":\"76.5640657\",\"title\":\"At ACC Dump, Kiratpur Sahib \",\"createdon\":\"23-02-2018 05:02 PM\",\"distance\":24.04},{\"id\":105,\"category\":18,\"lat\":\"31.2248579\",\"lng\":\"76.523604\",\"title\":\"At Puda Colony Ground, Sri Anandpur Sahib\",\"createdon\":\"23-02-2018 05:02 PM\",\"distance\":27.7},{\"id\":73,\"category\":18,\"lat\":\"31.226345\",\"lng\":\"76.4941573\",\"title\":\"Sen. Sec. School,lodhipur,Sri Anandpur Sahib\",\"createdon\":\"23-02-2018 04:02 PM\",\"distance\":28.01},{\"id\":99,\"category\":18,\"lat\":\"31.2283909\",\"lng\":\"76.5144622\",\"title\":\"Backside Of Police Station, Sri Anandpur Sahib \",\"createdon\":\"23-02-2018 04:02 PM\",\"distance\":28.11},{\"id\":69,\"category\":18,\"lat\":\"31.2313339\",\"lng\":\"76.500107\",\"title\":\"Railway Station Grid Infront Of SGPC GROUND, Sri Anandpur Sahib \",\"createdon\":\"23-02-2018 03:02 PM\",\"distance\":28.51},{\"id\":63,\"category\":18,\"lat\":\"31.2323449\",\"lng\":\"76.497744\",\"title\":\"Infront Of SGPC GROUND, Sri Anandpur Sahib \",\"createdon\":\"23-02-2018 03:02 PM\",\"distance\":28.64},{\"id\":79,\"category\":18,\"lat\":\"31.2373081\",\"lng\":\"76.474902\",\"title\":\"In Front Of Agampur Petrol Pump, Sri Anandpur Sahib\",\"createdon\":\"23-02-2018 04:02 PM\",\"distance\":29.46},{\"id\":76,\"category\":18,\"lat\":\"31.2389926\",\"lng\":\"76.4881776\",\"title\":\"Near Railway Fatak, At Agampur T Point, Sri Anandpur Sahib \",\"createdon\":\"23-02-2018 04:02 PM\",\"distance\":29.47},{\"id\":45,\"category\":18,\"lat\":\"31.2497047\",\"lng\":\"76.4793593\",\"title\":\"At Shivalik View Public School, Sri Anandpur Sahib\",\"createdon\":\"23-02-2018 02:02 PM\",\"distance\":30.76}],\"19\":[{\"id\":116,\"category\":19,\"lat\":\"31.1813308\",\"lng\":\"76.5642814\",\"title\":\"Vetenary Hospital, Kiratpur Sahib \",\"createdon\":\"23-02-2018 05:02 PM\",\"distance\":23.17},{\"id\":44,\"category\":19,\"lat\":\"31.2438921\",\"lng\":\"76.4836094\",\"title\":\"Vetenary Hospital at Sri Anandpur Sahib\",\"createdon\":\"23-02-2018 02:02 PM\",\"distance\":30.07}],\"20\":[{\"id\":143,\"category\":20,\"lat\":\"31.1786737\",\"lng\":\"76.5632721\",\"title\":\"Gurudwara Sri Patalpuri Sahib, Kiratpur Sahib\",\"createdon\":\"23-02-2018 07:02 PM\",\"distance\":22.86},{\"id\":144,\"category\":20,\"lat\":\"31.1822814\",\"lng\":\"76.5731836\",\"title\":\"Gurudwara Baba Gurditta Ji, Kiratpur Sahib\",\"createdon\":\"23-02-2018 07:02 PM\",\"distance\":23.43},{\"id\":139,\"category\":20,\"lat\":\"31.229843\",\"lng\":\"76.5105085\",\"title\":\"Panj Pyara Park, Sri Anandpur Sahib \",\"createdon\":\"23-02-2018 06:02 PM\",\"distance\":28.28},{\"id\":140,\"category\":20,\"lat\":\"31.2309067\",\"lng\":\"76.507339\",\"title\":\"Virasat-E-Khalsa, Sri Anandpur Sahib\",\"createdon\":\"23-02-2018 06:02 PM\",\"distance\":28.42},{\"id\":142,\"category\":20,\"lat\":\"31.2347051\",\"lng\":\"76.5000561\",\"title\":\"Gurudwara Shi Kesgarh Sahib\",\"createdon\":\"23-02-2018 07:02 PM\",\"distance\":28.89}]}";


    String subcate = "[{\"id\":1,\"name\":\"Langar\",\"name_punjabi\":\"ਲੰਗਰ\",\"count\":25},{\"id\":7,\"name\":\"Drinking Water\",\"name_punjabi\":\"ਪੀਣ ਵਾਲਾ ਪਾਣੀ\",\"count\":16},{\"id\":6,\"name\":\"Toilet\",\"name_punjabi\":\"ਟਾਇਲਟ\",\"count\":16},{\"id\":18,\"name\":\"Sub Control Room/\\r\\nHelp Desk\",\"name_punjabi\":\"ਸਬ ਕੰਟਰੋਲ ਰੂਮ/\\r\\nਹੈਲਪ ਡੈਸਕ\",\"count\":12},{\"id\":8,\"name\":\"Medical Team\",\"name_punjabi\":\"ਮੈਡੀਕਲ ਟੀਮ\",\"count\":12},{\"id\":15,\"name\":\"Ambulance\",\"name_punjabi\":\"ਐਂਬੂਲੈਂਸ\",\"count\":7},{\"id\":20,\"name\":\"Places Of Attraction\",\"name_punjabi\":\"ਆਕਰਸ਼ਣ ਦੇ ਸਥਾਨ\",\"count\":5},{\"id\":17,\"name\":\"Joda Ghar\",\"name_punjabi\":\"ਜੋਡਾ ਘਰ\",\"count\":4},{\"id\":5,\"name\":\"Parking Spot\",\"name_punjabi\":\"ਪਾਰਕਿੰਗ ਸਥਾਨ\",\"count\":3},{\"id\":16,\"name\":\"Transit Bus Stop\",\"name_punjabi\":\"ਟ੍ਰਾਂਸਿਟ ਬੱਸ ਸਟੌਪ\",\"count\":3},{\"id\":19,\"name\":\"Horse Treatment Centre\",\"name_punjabi\":\"ਘੋੜਾ ਇਲਾਜ ਕੇਂਦਰ\",\"count\":2},{\"id\":11,\"name\":\"Exhibition Point\",\"name_punjabi\":\"ਪ੍ਰਦਰਸ਼ਨੀ\",\"count\":1},{\"id\":4,\"name\":\"Traffic Control Room\",\"name_punjabi\":\"ਟਰੈਫਿਕ ਕੰਟਰੋਲ ਰੂਮ\",\"count\":1},{\"id\":3,\"name\":\"Police Control Room\",\"name_punjabi\":\"ਪੁਲਿਸ ਕੰਟਰੋਲ ਰੂਮ\",\"count\":1},{\"id\":2,\"name\":\"Civil Control Room\",\"name_punjabi\":\"ਸਿਵਲ ਕੰਟਰੋਲ ਰੂਮ\",\"count\":1}]";
    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences sharedPreferences  = getSharedPreferences("fe",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(sharedPreferences.getString("category",null)==null)
        {
            editor.putString("category",subcate);
            editor.apply();
        }

        SharedPreferences sharedPreferences1  = getSharedPreferences("fe",MODE_PRIVATE);
        if(sharedPreferences1.getString("subcategory",null)==null)
        {
            SharedPreferences.Editor editor1 = sharedPreferences1.edit();
            editor1.putString("subcategory",category);
            editor1.apply();

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
                            MyUtil.setCurrentLocation(getApplicationContext(), location);
                        }
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        invokeLocationRecivier();

        setContentView(R.layout.activity_splash);

        Bridge.config()
                .defaultHeader("Content-Type", "application/json");
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

                assert conMgr != null;
                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

                LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                boolean gps_enabled = false;
                assert lm != null;
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);


               /* if (netInfo == null ){
                    TastyToast.makeText(getApplicationContext(),"Internet Not available",TastyToast.LENGTH_SHORT,TastyToast.INFO).show();
                    finish();
                }
                else */if(!gps_enabled)
                {
                    displayLocationSettingsRequest(getApplicationContext());



                }

                else
                {
                    Intent i1 = new Intent(SplashActivity.this, SelectLanguage.class);
                    startActivity(i1);
                    finish();
                }











            }
        }, 2500);
    }

    private void invokeLocationRecivier() {
        ComponentName component = new ComponentName(getApplicationContext(), GpsLocationReceiver.class);
        int status = getApplicationContext().getPackageManager().getComponentEnabledSetting(component);
        getPackageManager().setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED , PackageManager.DONT_KILL_APP);



        ComponentName component1 = new ComponentName(getApplicationContext(), NetworkReceiver.class);
        int status1 = getApplicationContext().getPackageManager().getComponentEnabledSetting(component);
        getPackageManager().setComponentEnabledSetting(component1, PackageManager.COMPONENT_ENABLED_STATE_ENABLED , PackageManager.DONT_KILL_APP);




    }


    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i("", "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i("", "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(SplashActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i("", "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i("", "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode)
                {
                    case Activity.RESULT_OK:
                    {
                        // All required changes were successfully made
                        Intent i1 = new Intent(SplashActivity.this, SelectLanguage.class);
                        startActivity(i1);
                        finish();

                        break;
                    }
                    case Activity.RESULT_CANCELED:
                    {
                        // The user was asked to change settings, but chose not to
                        TastyToast.makeText(getApplicationContext(), "Location cancelled by user!",Toast.LENGTH_LONG,TastyToast.INFO).show();
                        finish();






                        break;
                    }
                    default:
                    {
                        break;
                    }
                }
                break;
        }
    }
}
