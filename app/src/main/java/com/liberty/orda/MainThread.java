package com.liberty.orda;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class MainThread implements OnMapReadyCallback {
    public static final MainThread Instance = new MainThread();
    private GoogleMap mMap;

    public MainThread() {

    }

    public void initMap(MainActivity activity) {
        SupportMapFragment supportMapFragment = (SupportMapFragment) activity.getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng latlng = new LatLng(43.237376,76.857344 );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 12f));

        Network network = new Network();
        List<LatLng> units = network.getUnitsPosition();
    }

    public void onMessage(String msg) {

    }

    public void onUnitUpdatePosition() {

    }
}
