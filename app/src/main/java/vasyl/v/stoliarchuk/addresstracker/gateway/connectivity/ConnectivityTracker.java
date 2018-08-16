package vasyl.v.stoliarchuk.addresstracker.gateway.connectivity;

import io.reactivex.Flowable;

public interface ConnectivityTracker {

    Flowable<Boolean> listenConnectivityState();
}
