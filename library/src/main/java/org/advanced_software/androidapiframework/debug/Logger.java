package org.advanced_software.androidapiframework.debug;

import android.util.Log;

/**
 * Created by johniak on 8/15/14.
 * Logger
 */
public class Logger {
    public final static String TAG = "AndroidApiFramework";


    public static void d(Object object){
        Log.d(TAG,object.toString());
    }

    public static void v(Object object){
        Log.v(TAG,object.toString());
    }

    public static void i(Object object){
        Log.i(TAG,object.toString());
    }

    public static void w(Object object){
        Log.w(TAG,object.toString());
    }

    public static void e(Object object){
        Log.e(TAG,object.toString());
    }
}
