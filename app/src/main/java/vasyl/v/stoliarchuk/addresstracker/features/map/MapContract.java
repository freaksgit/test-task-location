package vasyl.v.stoliarchuk.addresstracker.features.map;

import android.location.Location;

import vasyl.v.stoliarchuk.addresstracker.data.entity.Place;

public interface MapContract {
    interface View{

        void checkLocationPermission();

        void updateMapWithLocation(Location location);

        void showNeedLocationProviderAvailabilityMessage();

        void hideNeedLocationProviderAvailabilityMessage();

        void showPermissionDeniedFinishToast();

        void finish();

        void hideProgress();

        void setPlace(Place place);

        void showConnectionFailedMessage();

        void showBadLocationDataMessage();

        void showServerErrorMessage();

        void showUnknownErrorMessage();

        void toggleAddressTextVisibility(boolean visible);
    }

    interface Presenter{

        void onLocationPermissionGranted();

        void onLocationPermissionDenied();

        void unsubscribe();

        void subscribe();
    }
}
