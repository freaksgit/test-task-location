package vasyl.v.stoliarchuk.addresstracker.util;

import android.content.Context;
import android.net.ConnectivityManager;

public class DeviceUtils {
    private final Context context;

    public DeviceUtils(Context context) {this.context = context;}

    /**
     * @return true if there is any (wifi|mobile) connection is active.
     */
    public boolean isAnyNetworkAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm != null && cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}
