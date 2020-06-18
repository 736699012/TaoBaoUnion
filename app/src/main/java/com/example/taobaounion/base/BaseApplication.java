package com.example.taobaounion.base;

import android.app.Application;
import android.content.Context;

public class BaseApplication extends Application {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getBaseContext();
    }
    public static Context getContext(){
        return sContext;
    }
}
