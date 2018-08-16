package vasyl.v.stoliarchuk.addresstracker.gateway.location.location;

import android.location.Location;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import io.reactivex.Flowable;
import vasyl.v.stoliarchuk.addresstracker.gateway.location.location.state.LocationProviderState;

public interface LocationTracker {

    int PROVIDER_TYPE_UNDEFINED = 0;
    int PROVIDER_TYPE_NETWORK = 1;
    int PROVIDER_TYPE_GPS = 2;

    @IntDef({PROVIDER_TYPE_UNDEFINED,
            PROVIDER_TYPE_NETWORK,
            PROVIDER_TYPE_GPS})
    @Retention(RetentionPolicy.SOURCE)
    @interface ProviderType {}

    Flowable<Location> getLastLocationFlowable();

    boolean isGpsEnabled();

    boolean isNetworkEnabled();

    boolean isAnyProviderEnabled();

    Flowable<LocationProviderState> listenProvidersStateUpdates();
}
