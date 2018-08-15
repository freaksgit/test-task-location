package vasyl.v.stoliarchuk.addresstracker.features.map;

public interface MapContract {
    interface View{

        void checkLocationPermission();
    }

    interface Presenter{

        void onMapReady();

        void onLocationPermissionGranted();

        void onLocationPermissionDenied();
    }
}
