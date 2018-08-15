package vasyl.v.stoliarchuk.addresstracker.gateway.location;

import android.annotation.SuppressLint;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import vasyl.v.stoliarchuk.addresstracker.gateway.location.state.LocationProviderState;

public class AndroidLocationTracker implements LocationTracker {
    private static final String TAG = AndroidLocationTracker.class.getSimpleName();
    private final LocationManager locationManager;

    private final long minTimeBetweenUpdates;
    private final float minDistanceBetweenUpdates;

    private final Criteria criteria;

    public AndroidLocationTracker(LocationManager locationManager,
                                  long minTimeBetweenUpdates,
                                  float minDistanceBetweenUpdates,
                                  Criteria criteria) {
        this.locationManager = locationManager;
        this.minTimeBetweenUpdates = minTimeBetweenUpdates;
        this.minDistanceBetweenUpdates = minDistanceBetweenUpdates;
        this.criteria = criteria;
    }

    @SuppressLint("MissingPermission")
    @Override
    public Flowable<Location> getLastLocationFlowable() {

        return Flowable.create(new FlowableOnSubscribe<Location>() {
            @Override
            public void subscribe(FlowableEmitter<Location> emitter) throws Exception {
                final LocationListener locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        if (emitter.isCancelled()) {
                            locationManager.removeUpdates(this);
                        } else {
                            emitter.onNext(location);
                            Log.i(TAG, String.format("New GPS Location - Accuracy=%f, Provider=%s",
                                    location.getAccuracy(), location.getProvider()));
                        }
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {}

                    @Override
                    public void onProviderEnabled(String provider) {}

                    @Override
                    public void onProviderDisabled(String provider) {}
                };

                locationManager.requestLocationUpdates(minTimeBetweenUpdates, minDistanceBetweenUpdates, criteria, locationListener, null);
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                emitIfFresh(emitter, location);
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                emitIfFresh(emitter, location);
            }

            private void emitIfFresh(FlowableEmitter<Location> emitter, Location location) {
                if (location != null) {
                    if (isFreshLocation(location)) {
                        if (!emitter.isCancelled()) {
                            emitter.onNext(location);
                        }
                    }
                }
            }
        }, BackpressureStrategy.MISSING);
    }


    private boolean isFreshLocation(Location location) {
        long currentTime = System.currentTimeMillis();
        return (currentTime - location.getTime()) < minTimeBetweenUpdates;
    }

    @Override
    public boolean isGpsEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    public boolean isNetworkEnabled() {
        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public Flowable<LocationProviderState> listenProvidersStateUpdates() {
        return Flowable.create(new FlowableOnSubscribe<LocationProviderState>() {

            @SuppressLint("MissingPermission")
            @Override
            public void subscribe(FlowableEmitter<LocationProviderState> emitter) throws Exception {
                final LocationListener locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {}

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {}

                    @Override
                    public void onProviderEnabled(String provider) {
                        emitProviderStateUpdate(provider, true);
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                        emitProviderStateUpdate(provider, false);
                    }

                    private void emitProviderStateUpdate(String provider, boolean enabled) {
                        if (emitter.isCancelled()) {
                            locationManager.removeUpdates(this);
                        } else {
                            emitter.onNext(new LocationProviderState(provider, enabled));
                        }
                    }
                };

                locationManager.requestLocationUpdates(Long.MAX_VALUE, Float.MAX_VALUE, new Criteria(), locationListener, null);
            }
        }, BackpressureStrategy.MISSING);
    }
}
