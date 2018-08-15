package vasyl.v.stoliarchuk.addresstracker.features.map;

import android.util.Log;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import vasyl.v.stoliarchuk.addresstracker.gateway.location.LocationTracker;

public class MapPresenter implements MapContract.Presenter {
    private static final String TAG = MapPresenter.class.getSimpleName();
    private final CompositeDisposable disposables = new CompositeDisposable();

    private final MapContract.View mvpView;
    private final LocationTracker locationTracker;

    public MapPresenter(MapContract.View mvpView, LocationTracker locationTracker) {
        this.mvpView = mvpView;
        this.locationTracker = locationTracker;
    }

    @Override
    public void subscribe() {
        mvpView.checkLocationPermission();
    }


    @Override
    public void onLocationPermissionGranted() {
        trySubscribeOnLocationUpdates();
    }

    private void trySubscribeOnLocationUpdates() {
        Log.d(TAG, "trySubscribeOnLocationUpdates: ");
        if (!(locationTracker.isAnyProviderEnabled())) {
            mvpView.showNeedLocationProviderAvailabilityMessage();
        }

        subscribeToProvidersStateUpdates();
        subscribeOnLocationUpdates();
    }

    private void subscribeOnLocationUpdates() {
        Disposable disposable = locationTracker.getLastLocationFlowable()
                .subscribe(mvpView::updateMapWithLocation);
        disposables.add(disposable);
    }

    private void subscribeToProvidersStateUpdates() {
        Disposable disposable = locationTracker.listenProvidersStateUpdates()
                .subscribe(providerState -> {
                    if (locationTracker.isAnyProviderEnabled()) {
                        mvpView.hideNeedLocationProviderAvailabilityMessage();
                    } else {
                        mvpView.showNeedLocationProviderAvailabilityMessage();
                    }
                });
        disposables.add(disposable);
    }

    @Override
    public void onLocationPermissionDenied() {
        mvpView.showPermissionDeniedFinishToast();
        mvpView.finish();
    }

    @Override
    public void unsubscribe() {
        disposables.clear();
    }
}
