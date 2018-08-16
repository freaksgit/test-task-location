package vasyl.v.stoliarchuk.addresstracker.gateway.location.connectivity;

import io.reactivex.Flowable;
import vasyl.v.stoliarchuk.addresstracker.features.map.MapActivity;

public interface ConnectivityTracker {

    Flowable<Boolean> listenConnectivityState();
}
