package com.example.muthuraman.memorableplaces;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.example.muthuraman.memorableplaces.R.drawable.ic_action_name;

public class MyPlaces extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    int i;
    boolean b = false;
    String s;
    Intent intent;
    LocationManager locationManager;
    double d1, d2;
    String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_places);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        if (locationManager.getLastKnownLocation(provider) != null)
            onLocationChanged(locationManager.getLastKnownLocation(provider));

        intent = new Intent();
        i = getIntent().getIntExtra("val", -1);
        d1 = getIntent().getDoubleExtra("lat", -1.1);
        d2 = getIntent().getDoubleExtra("long", -1.1);
        s = getIntent().getStringExtra("address");
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        locationManager.requestLocationUpdates(provider, 1000, 10, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final Geocoder g = new Geocoder(this, Locale.getDefault());
        if (i == 0) {
            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {

                    List<Address> list = null;
                    try {
                        list = g.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    s = list.get(0).getAddressLine(0);
                    d1 = latLng.latitude;
                    d2 = latLng.longitude;

                    mMap.addMarker(new MarkerOptions().title(s).position(latLng));
                    Toast.makeText(getApplicationContext(), list.get(0).getFeatureName() + "\nadded!\nYou can go back and see your favorite places", Toast.LENGTH_SHORT).show();
                    intent.putExtra("lat", d1);
                    intent.putExtra("long", d2);
                    intent.putExtra("address", s);
                    b = true;
                }
            });
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                locationManager.removeUpdates(this);
            }
            mMap.addMarker(new MarkerOptions().position(new LatLng(d1,d2)).title("Marker in " + s));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(d1, d2)));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(16));
            b=true;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location!=null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(7));
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
