package com.example.cityguide.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class Internet {

    public boolean Connected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobiledata = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi != null && wifi.isConnected() || mobiledata != null && mobiledata.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

}
