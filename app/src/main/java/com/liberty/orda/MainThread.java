package com.liberty.orda;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import java.util.List;

public class MainThread implements OnMapReadyCallback {
    public static final MainThread Instance = new MainThread();
    private GoogleMap mMap;
    private MainActivity parentActivity;

    public MainThread() {

    }

    public void initMap(MainActivity activity) {
        parentActivity = activity;

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

        this.setUpClusterer(parentActivity);
        this.addItems();
    }

    public void onMessage(String msg) {

    }

    public void onUnitUpdatePosition() {

    }


    public class MyItem implements ClusterItem {
        private final LatLng position;
        private final String title;
        private final String snippet;

        public MyItem(double lat, double lng, String title, String snippet) {
            position = new LatLng(lat, lng);
            this.title = title;
            this.snippet = snippet;
        }

        @Override
        public LatLng getPosition() {
            return position;
        }

        public String getTitle() {
            return title;
        }

        public String getSnippet() {
            return snippet;
        }
    }



    public ClusterManager<MyItem> clusterManager;

    private void setUpClusterer(MainActivity activity) {
        // Position the map.
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 10));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        //mClusterManager = new ClusterManager<MyItem>(activity, mMap);
        clusterManager = new ClusterManager<MyItem>(activity.getApplicationContext(), mMap);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        //mMap.setOnCameraChangeListener(this.clusterManager);
        //mMap.setOnMarkerClickListener(clusterManager);
    }

    private void addItems() {
        // Set some lat/lng coordinates to start with.
        double lat = 43.237376;
        double lng = 76.857344;

        // Add ten cluster items in close proximity, for purposes of this example.
        for (int i = 0; i < 20; i++) {
            double offset = i / 6000d;
            lat = lat + offset;
            lng = lng + offset;
            MyItem offsetItem = new MyItem(lat, lng, "ee", "bb");
            clusterManager.addItem(offsetItem);
            //mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("Marker"));
        }

        clusterManager.cluster();
    }

    public void onUserInteraction() {
        clusterManager.cluster();
    }
}
