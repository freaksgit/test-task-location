package vasyl.v.stoliarchuk.addresstracker.features.map;

import android.location.Location;

public interface MapContract {
    interface View{

        void checkLocationPermission();

        void updateMapWithLocation(Location location);

        void showNeedLocationProviderAvailabilityMessage();

        void hideNeedLocationProviderAvailabilityMessage();

        void showPermissionDeniedFinishToast();

        void finish();
    }

    interface Presenter{

        void onLocationPermissionGranted();

        void onLocationPermissionDenied();

        void unsubscribe();

        void subscribe();
    }
}
