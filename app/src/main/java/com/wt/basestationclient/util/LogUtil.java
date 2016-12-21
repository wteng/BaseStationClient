package com.wt.basestationclient.util;

import android.util.Log;

/**
 * Created by teng on 19/12/16.
 */
public class LogUtil {

    public static final String LOG_NAME = "wtlog";

    public static void i(String msg) {
        if (msg == null) {
            Log.i(LOG_NAME, "null");
            return;
        }
        Log.i(LOG_NAME, msg);
    }

}
