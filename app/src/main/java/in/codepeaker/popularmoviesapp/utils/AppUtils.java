package in.codepeaker.popularmoviesapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by github.com/codepeaker on 24/11/17.
 */

public class AppUtils {
    public static boolean CheckEnabledInternet(Context context) {
        boolean isConnected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[]  networkInfos = new NetworkInfo[0];
        if (connectivityManager != null) {
            networkInfos = connectivityManager.getAllNetworkInfo();
        }
        for(NetworkInfo networkInfo : networkInfos) {
            if(networkInfo.getState()== NetworkInfo.State.CONNECTED) {
                isConnected = true;
                break;
            }
        }
       return isConnected;
    }
}
