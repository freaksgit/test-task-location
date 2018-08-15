package vasyl.v.stoliarchuk.addresstracker.features.map;

import android.location.Location;

public interface MapContract {
    interface View{

        void checkLocationPermission();

        void updateMapWithLocation(Location location);
    }

    interface Presenter{

        void onMapReady();

        void onLocationPermissionGranted();

        void onLocationPermissionDenied();

        void unsubscribe();
    }
}
