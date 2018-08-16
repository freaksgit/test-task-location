package vasyl.v.stoliarchuk.addresstracker.features.map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import vasyl.v.stoliarchuk.addresstracker.R;
import vasyl.v.stoliarchuk.addresstracker.data.entity.Place;

public class MapActivity extends DaggerAppCompatActivity implements MapContract.View, OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private Marker locationMarker;
    private Snackbar snackbar;

    @Inject
    MapContract.Presenter presenter;

    private GoogleMap map;
    private AlertDialog needLocationProviderDialog;
    private AlertDialog locationPermissionRationaleDialog;

    private ProgressBar progressBar;
    private TextView addressTextView;
    private CoordinatorLayout rootLayout;
    private CardView addressCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.activity_map_fragment);
        mapFragment.getMapAsync(this);
        progressBar = findViewById(R.id.activity_map_address_progress);
        addressTextView = findViewById(R.id.activity_map_address_text);
        rootLayout = findViewById(R.id.activity_map_root);
        addressCard = findViewById(R.id.activity_map_card);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.subscribe();
    }

    @Override
    public void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                showLocationPermissionRationale();
            } else {
                requestLocationPermission();
            }
        } else {
            presenter.onLocationPermissionGranted();
        }
    }

    @Override
    public void updateMapWithLocation(Location location) {
        if (map == null) {
            return;
        }
        LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        if (locationMarker == null) {
            locationMarker = map.addMarker(new MarkerOptions().position(userLatLng).title(getString(R.string.map_activity_marker_message)));
        } else {
            locationMarker.setPosition(userLatLng);
        }
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15.0f));
    }

    @Override
    public void showNeedLocationProviderAvailabilityMessage() {
        if (needLocationProviderDialog == null) {
            needLocationProviderDialog = new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setMessage(R.string.location_provider_availability_needed_message)
                    .setPositiveButton(getString(R.string.location_provider_availability_needed_positive_button), (dialog, which) -> startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                    .setNegativeButton(getString(R.string.location_provider_availability_needed_negative_button), (dialog, which) -> {
                        dialog.dismiss();
                    }).create();
        }
        needLocationProviderDialog.show();
    }

    @Override
    public void hideNeedLocationProviderAvailabilityMessage() {
        if (needLocationProviderDialog.isShowing()) {
            needLocationProviderDialog.dismiss();
        }
    }

    @Override
    public void showPermissionDeniedFinishToast() {
        Toast.makeText(this, R.string.permission_denied_finish_message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setPlace(Place place) {
        if (place != null) {
            addressTextView.setText(place.getDisplayName());
        }
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
    }

    private void showLocationPermissionRationale() {
        if (locationPermissionRationaleDialog == null) {
            locationPermissionRationaleDialog = new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setMessage(R.string.permission_location_rationale_message)
                    .setPositiveButton(getString(R.string.permission_location_rationale_positive_button), (dialog, which) -> requestLocationPermission())
                    .setNegativeButton(getString(R.string.permission_location_rationale_negative_button), (dialog, which) -> {
                        dialog.dismiss();
                        presenter.onLocationPermissionDenied();
                    }).create();
        }
        locationPermissionRationaleDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    presenter.onLocationPermissionGranted();
                } else {
                    presenter.onLocationPermissionDenied();
                }
                break;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.unsubscribe();
    }

    @Override
    public void showConnectionFailedMessage() {
        snackbar = Snackbar.make(rootLayout, "Smth going wrong. Please check you internet connection or try again later.", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void showBadLocationDataMessage() {
        snackbar = Snackbar.make(rootLayout, "Location data is bad.", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void showServerErrorMessage() {
        snackbar = Snackbar.make(rootLayout, "Smth going wrong. Try again.", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void showUnknownErrorMessage() {
        snackbar = Snackbar.make(rootLayout, "Smth going wrong. Please try again.", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void toggleAddressTextVisibility(boolean visible) {
        if (visible) {
            addressCard.setVisibility(View.VISIBLE);
        } else {
            addressCard.setVisibility(View.INVISIBLE);
        }
    }
}
