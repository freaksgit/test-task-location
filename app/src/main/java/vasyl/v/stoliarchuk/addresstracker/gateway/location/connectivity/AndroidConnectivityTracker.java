package vasyl.v.stoliarchuk.addresstracker.gateway.location.connectivity;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;

import com.cantrowitz.rxbroadcast.RxBroadcast;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import vasyl.v.stoliarchuk.addresstracker.features.map.MapActivity;
import vasyl.v.stoliarchuk.addresstracker.util.DeviceUtils;

public class AndroidConnectivityTracker implements ConnectivityTracker {
    private final MapActivity mapActivity;
    private final IntentFilter intentFilter;
    private final DeviceUtils deviceUtils;

    public AndroidConnectivityTracker(MapActivity mapActivity, DeviceUtils deviceUtils) {
        this.mapActivity = mapActivity;
        this.deviceUtils = deviceUtils;
        intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
    }

    @Override
    public Flowable<Boolean> listenConnectivityState() {
        return RxBroadcast.fromBroadcast(mapActivity, intentFilter)
                .map(intent -> deviceUtils.isAnyNetworkAvailable())
                .toFlowable(BackpressureStrategy.MISSING);
    }
}
