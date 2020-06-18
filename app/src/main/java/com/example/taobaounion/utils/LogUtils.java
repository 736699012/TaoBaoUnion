package com.example.taobaounion.utils;

import android.util.Log;

public class LogUtils {
    private static int currentlev =4;
    private static final int DEBUG_LEV =4;
    private static final int INFO_LEV =3;
    private static final int WARNING_LEV =2;
    private static final int ERROR_LEV =1;

    public static void d(Object object,String log){
        if(currentlev>=DEBUG_LEV){
            Log.d(object.getClass().getSimpleName(),log);
        }
    }
    public static void i(Object object,String log){
        if(currentlev>=INFO_LEV){
            Log.i(object.getClass().getSimpleName(),log);
        }
    }
    public static void w(Object object,String log){
        if(currentlev>=WARNING_LEV){
            Log.w(object.getClass().getSimpleName(),log);
        }
    }public static void e(Object object,String log){
        if(currentlev>=ERROR_LEV){
            Log.e(object.getClass().getSimpleName(),log);
        }
    }


}
