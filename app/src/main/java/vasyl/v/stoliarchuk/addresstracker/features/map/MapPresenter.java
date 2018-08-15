package vasyl.v.stoliarchuk.addresstracker.features.map;

import android.util.Log;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import vasyl.v.stoliarchuk.addresstracker.data.AddressDataSource;
import vasyl.v.stoliarchuk.addresstracker.gateway.location.LocationTracker;

public class MapPresenter implements MapContract.Presenter {
    private static final String TAG = MapPresenter.class.getSimpleName();
    private final CompositeDisposable disposables = new CompositeDisposable();

    private final MapContract.View mvpView;
    private final LocationTracker locationTracker;
    private final AddressDataSource addressRepository;

    public MapPresenter(MapContract.View mvpView,
                        LocationTracker locationTracker,
                        AddressDataSource addressRepository) {
        this.mvpView = mvpView;
        this.locationTracker = locationTracker;
        this.addressRepository = addressRepository;
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
                .map(location -> {
                    mvpView.updateMapWithLocation(location);
                    return location;
                })
                .observeOn(Schedulers.io())
                .flatMapMaybe(location -> addressRepository.getPlace(location.getLatitude(), location.getLongitude()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(place -> {
                    mvpView.hideProgress();
                    mvpView.setPlace(place);
                });
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
