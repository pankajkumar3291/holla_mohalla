package com.hollamohalla2.utility;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class MyUtil {
    //18.217.234.39:8080
    public static final String URLCATEGORY = "http://18.217.234.39:8080/api/category/findall";
    public static final String URLSUBCATEGORY = "http://18.217.234.39:8080/api/place/categorywise3";
    private static Location myLocation = null;

    //  http://192.168.0.72:33000

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public static void setCurrentLocation(Context currentLocation, Location location) {
        myLocation = location;
    }

    public static Location getCurrentLocation(Context currentLocation) {

        return   myLocation;
    }
}
