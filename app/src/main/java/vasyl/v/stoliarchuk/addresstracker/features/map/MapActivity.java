package vasyl.v.stoliarchuk.addresstracker.features.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.support.DaggerAppCompatActivity;
import vasyl.v.stoliarchuk.addresstracker.R;

public class MapActivity extends DaggerAppCompatActivity implements MapContract.View, OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Inject
    MapContract.Presenter presenter;

    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        AndroidInjection.inject(this);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.activity_map_fragment);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        presenter.onMapReady();
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

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
    }

    private void showLocationPermissionRationale() {
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage(R.string.permission_location_rationale_message)
                .setPositiveButton(getString(R.string.permission_location_rationale_positive_button), (dialog, which) -> requestLocationPermission())
                .setNegativeButton(getString(R.string.permission_location_rationale_negative_button), (dialog, which) -> {
                    dialog.dismiss();
                    presenter.onLocationPermissionDenied();
                })
                .create()
                .show();
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
}
