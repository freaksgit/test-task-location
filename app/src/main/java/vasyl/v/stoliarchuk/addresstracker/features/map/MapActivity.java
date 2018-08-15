package vasyl.v.stoliarchuk.addresstracker.features.map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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

public class MapActivity extends DaggerAppCompatActivity implements MapContract.View, OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private Marker locationMarker;

    @Inject
    MapContract.Presenter presenter;

    private GoogleMap map;
    private AlertDialog needLocationProviderDialog;
    private AlertDialog locationPermissionRationaleDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.activity_map_fragment);
        mapFragment.getMapAsync(this);
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
}
