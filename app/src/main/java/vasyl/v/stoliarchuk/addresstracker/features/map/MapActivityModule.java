package vasyl.v.stoliarchuk.addresstracker.features.map;

import android.content.Context;
import android.location.Criteria;
import android.location.LocationManager;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import vasyl.v.stoliarchuk.addresstracker.data.AddressDataSource;
import vasyl.v.stoliarchuk.addresstracker.di.ActivityScope;
import vasyl.v.stoliarchuk.addresstracker.di.DiName;
import vasyl.v.stoliarchuk.addresstracker.gateway.location.connectivity.AndroidConnectivityTracker;
import vasyl.v.stoliarchuk.addresstracker.gateway.location.connectivity.ConnectivityTracker;
import vasyl.v.stoliarchuk.addresstracker.gateway.location.location.AndroidLocationTracker;
import vasyl.v.stoliarchuk.addresstracker.gateway.location.location.LocationTracker;
import vasyl.v.stoliarchuk.addresstracker.util.DeviceUtils;

@Module
public abstract class MapActivityModule {

    @Binds
    abstract MapContract.View bindMapView(MapActivity mapActivity);

    @Provides
    @ActivityScope
    static MapContract.Presenter provideMapPresenter(MapContract.View mvpView,
                                                     LocationTracker locationTracker,
                                                     ConnectivityTracker connectivityTracker,
                                                     @Named(DiName.REPOSITORY) AddressDataSource addressRepository) {
        return new MapPresenter(mvpView, locationTracker, connectivityTracker, addressRepository);
    }

    @Provides
    @ActivityScope
    static LocationManager provideLocationManager(MapActivity mapActivity) {
        return (LocationManager) mapActivity.getSystemService(Context.LOCATION_SERVICE);
    }

    @Provides
    static Criteria provideCriteria() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
        criteria.setAltitudeRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);
        criteria.setBearingRequired(false);
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
        return criteria;
    }

    @Provides
    @ActivityScope
    static LocationTracker provideLocationTracker(LocationManager locationManager, Criteria criteria) {
        final long minTimeBetweenUpdates = TimeUnit.MINUTES.toMillis(1);
        final long minDistanceBetweenUpdates = 10; // in meters
        return new AndroidLocationTracker(locationManager, minTimeBetweenUpdates, minDistanceBetweenUpdates, criteria);
    }

    @Provides
    @ActivityScope
    static ConnectivityTracker provideConnectivityTracker(MapActivity mapActivity, DeviceUtils deviceUtils) {
        return new AndroidConnectivityTracker(mapActivity, deviceUtils);
    }
}
