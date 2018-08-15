package vasyl.v.stoliarchuk.addresstracker.features.map;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import vasyl.v.stoliarchuk.addresstracker.gateway.location.LocationTracker;

public class MapPresenter implements MapContract.Presenter {

    private final CompositeDisposable disposables = new CompositeDisposable();

    private final MapContract.View mvpView;
    private final LocationTracker locationTracker;

    public MapPresenter(MapContract.View mvpView, LocationTracker locationTracker) {
        this.mvpView = mvpView;
        this.locationTracker = locationTracker;
    }

    @Override
    public void onMapReady() {
        mvpView.checkLocationPermission();
    }

    @Override
    public void onLocationPermissionGranted() {
        Disposable disposable = locationTracker.getLastLocationFlowable()
                .subscribe(mvpView::updateMapWithLocation);
        disposables.add(disposable);
    }

    @Override
    public void onLocationPermissionDenied() {

    }

    @Override
    public void unsubscribe() {
        disposables.clear();
    }


}
