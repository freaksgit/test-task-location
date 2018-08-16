package vasyl.v.stoliarchuk.addresstracker.features.map;


import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import vasyl.v.stoliarchuk.addresstracker.data.AddressDataSource;
import vasyl.v.stoliarchuk.addresstracker.gateway.location.connectivity.ConnectivityTracker;
import vasyl.v.stoliarchuk.addresstracker.gateway.location.location.LocationTracker;

public class MapPresenter implements MapContract.Presenter {
    private final CompositeDisposable disposables = new CompositeDisposable();

    private final MapContract.View mvpView;
    private final LocationTracker locationTracker;
    private final ConnectivityTracker connectivityTracker;

    private final AddressDataSource addressRepository;
    private Disposable locationUpdatesDisposable;
    private Disposable locationProviderStateDisposable;
    private Disposable connectivityListenerDisposable;

    public MapPresenter(MapContract.View mvpView,
                        LocationTracker locationTracker,
                        ConnectivityTracker connectivityTracker,
                        AddressDataSource addressRepository) {
        this.mvpView = mvpView;
        this.locationTracker = locationTracker;
        this.connectivityTracker = connectivityTracker;
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
        if (!(locationTracker.isAnyProviderEnabled())) {
            mvpView.showNeedLocationProviderAvailabilityMessage();
        }
        subscribeToProvidersStateUpdates();
        subscribeOnLocationUpdates();
    }

    private void subscribeOnLocationUpdates() {
        locationUpdatesDisposable = locationTracker.getLastLocationFlowable()
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
                    mvpView.toggleAddressTextVisibility(true);
                }, throwable -> handleGetAddressError(throwable), () -> {});
        disposables.add(locationUpdatesDisposable);
    }

    private void subscribeToProvidersStateUpdates() {
        locationProviderStateDisposable = locationTracker.listenProvidersStateUpdates()
                .subscribe(providerState -> {
                    if (locationTracker.isAnyProviderEnabled()) {
                        mvpView.hideNeedLocationProviderAvailabilityMessage();
                    } else {
                        mvpView.showNeedLocationProviderAvailabilityMessage();
                    }
                });
        disposables.add(locationProviderStateDisposable);
    }

    private void handleGetAddressError(Throwable throwable) {
        mvpView.toggleAddressTextVisibility(false);
        if (throwable instanceof ConnectException
                || throwable instanceof SocketTimeoutException
                || throwable instanceof SocketException
                || throwable instanceof UnknownHostException) {
            //failed to connect
            mvpView.showConnectionFailedMessage();
            unsubscribeFromLocationUpdates();
            startListenConnectivityState();
        } else if (throwable instanceof HttpException) {
            int responseCode = ((HttpException) throwable).code();

            if (responseCode >= HttpURLConnection.HTTP_BAD_REQUEST && responseCode < HttpURLConnection.HTTP_INTERNAL_ERROR) {
                //client error
                mvpView.showBadLocationDataMessage();
            } else if (responseCode >= HttpURLConnection.HTTP_INTERNAL_ERROR && responseCode < 600) {
                //server error
                mvpView.showServerErrorMessage();
            }
        } else {
            mvpView.showUnknownErrorMessage();
        }
    }

    private void unsubscribeFromLocationUpdates() {
        if (!locationUpdatesDisposable.isDisposed()) {
            locationUpdatesDisposable.dispose();
        }
        if (!locationProviderStateDisposable.isDisposed()) {
            locationProviderStateDisposable.dispose();
        }
    }

    private void startListenConnectivityState() {
        connectivityListenerDisposable = connectivityTracker.listenConnectivityState()
                .subscribe(isConnected -> {
                    if (isConnected) {
                        trySubscribeOnLocationUpdates();
                        stopListenConnectivityState();
                    }
                });
    }

    private void stopListenConnectivityState() {
        if (!connectivityListenerDisposable.isDisposed()) {
            connectivityListenerDisposable.dispose();
        }
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
