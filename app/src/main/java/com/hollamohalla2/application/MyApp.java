package com.hollamohalla2.application;

import android.support.multidex.MultiDexApplication;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.orm.SugarContext;

/*import com.orhanobut.hawk.Hawk;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;*/

/**
 * Created by android on 13/2/18.
 */

public class MyApp extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Hawk.init(getApplicationContext()).build();
        SugarContext.init(getApplicationContext());
        Logger.addLogAdapter(new AndroidLogAdapter());
    }
}
