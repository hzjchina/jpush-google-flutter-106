package com.jiguang.jpush;


import android.util.Log;

import cn.jiguang.internal.JConstants;

public class VDLog {
    public static void d(String tag, String msg) {
        if (JConstants.DEBUG_MODE) {
            Log.d(tag, msg);
        }
    }
    public static void i(String tag, String msg) {
        if (JConstants.DEBUG_MODE) {
            Log.i(tag, msg);
        }
    }
    public static void w(String tag, String msg, Exception e) {
        if (JConstants.DEBUG_MODE)  {
            Log.w(tag, msg,e);
        }
    }
    public static void e(String tag, String msg) {
        if (JConstants.DEBUG_MODE)  {
            Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Exception e) {
        if (JConstants.DEBUG_MODE)  {
            Log.e(tag, msg,e);
        }
    }
}
