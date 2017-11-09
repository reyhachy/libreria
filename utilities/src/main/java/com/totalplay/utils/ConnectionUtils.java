package com.totalplay.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by jorgehdezvilla on 09/11/17.
 * TotalPlay
 */

public abstract class ConnectionUtils {
    public ConnectionUtils() {
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService("connectivity");

        try {
            return connectivityManager.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED;
        } catch (Exception var5) {
            ConnectivityManager CManager = (ConnectivityManager)context.getSystemService("connectivity");
            NetworkInfo NInfo = CManager.getActiveNetworkInfo();
            return NInfo != null && NInfo.isConnected() && NInfo.isAvailable();
        }
    }
}
