package com.example.chrislin.recordrouteapplication.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.chrislin.recordrouteapplication.R;
import com.example.chrislin.recordrouteapplication.database.dao.RouteDao;
import com.example.chrislin.recordrouteapplication.routelist.RouteListActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import io.realm.RealmList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "MapsActivity";

    @BindView(R.id.fab_main_routeController)
    FloatingActionButton fabMainRouteController;
    @BindView(R.id.fab_main_loadRoute)
    FloatingActionButton fabMainLoadRoute;
    private GoogleMap mMap;

    private boolean isRouteRecord = true;
    private FusedLocationProviderClient mFusedLocationClient;
    private ArrayList<String> routeLocationList = new ArrayList<>();
    private static final int LOCATION_REQUEST = 500;

    private RouteDao routeDao;
    private Polyline polyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        routeDao = new RouteDao();

        //get new Locations
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known Locations. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle Locations object
                            if (isRouteRecord) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),location.getLongitude())));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
                                routeLocationList.add(location.getLatitude() + "," + location.getLongitude());
                                Log.e(TAG, "routeLocationList: " + routeLocationList.toString());
                            }
                        }
                    }
                });

        Intent getIntent = getIntent();
        int position = getIntent.getIntExtra("position", -1);
        Log.e(TAG, "position: " + position);
        if (position != -1){
            Toast.makeText(this, routeDao.searchData("id", String.valueOf(position)).first().getRouteLocationList().first(), Toast.LENGTH_SHORT).show();
//            PolylineOptions polylineOptions = new PolylineOptions();
//            polylineOptions.addAll(realmListToArrayList(routeDao.searchData("id", String.valueOf(position)).first().getRouteLocationList()));
//            polylineOptions.width(5);
//            polylineOptions.color(Color.BLUE);
//            polylineOptions.geodesic(true);
//            polyline = mMap.addPolyline(polylineOptions);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case LOCATION_REQUEST:
                if(grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
                        return;
                    }
                    mMap.setMyLocationEnabled(true);
                }
                break;
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setMyLocationEnabled(true);
    }

    @OnClick({R.id.fab_main_routeController, R.id.fab_main_loadRoute})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fab_main_routeController:
                if (isRouteRecord){
                    fabMainRouteController.setImageResource(R.drawable.ic_pause_black_24dp);
                }else {
                    fabMainRouteController.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                }
                isRouteRecord = !isRouteRecord;
                break;
            case R.id.fab_main_loadRoute:
                Intent intent = new Intent(this, RouteListActivity.class);
                startActivity(intent);
                break;
        }
    }

    @OnLongClick(R.id.fab_main_routeController)
    public boolean stopRecord(){
        //add data to Realm
        String newPrimacyKey = String.valueOf(routeDao.searchAll().size() + 1);
        String routeName = "路線" + newPrimacyKey;
        RealmList<String> realmList = arrayListToRealmList(routeLocationList);

        routeDao.insert(newPrimacyKey, routeName, realmList);
//        Log.e(TAG, "insertData");
        Toast.makeText(this, "insert data", Toast.LENGTH_SHORT).show();
        return true;
    }

    private ArrayList<LatLng> realmListToArrayList(RealmList<String> realmList){
        final ArrayList<LatLng> arrayList = new ArrayList<LatLng>();
        for (int i=0; i<realmList.size(); i++){
            String[] latlng = realmList.get(i).split(",");
            Double lat = Double.parseDouble(latlng[0]);
            Double lng = Double.parseDouble(latlng[1]);
            Log.e(TAG, String.valueOf(lat.TYPE));
            arrayList.add(new LatLng(lat, lng));
        }
        return arrayList;
    }

    private RealmList<String> arrayListToRealmList(ArrayList<String> arrayList){
        final RealmList<String> realmList = new RealmList<String>();
        for (int i =0; i<arrayList.size(); i++){
            realmList.add(arrayList.get(i));
        }
        return realmList;
//        return Flowable.just(arrayList)
//                .flatMap(new Function<ArrayList, Publisher<String>>() {
//                    @Override
//                    public Publisher<String> apply(ArrayList arrayList) throws Exception {
//                        return Flowable.fromIterable(arrayList);
//                    }
//                });
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String s) throws Exception {
//                        realmList.add(s);
//                    }
//                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        routeDao.close();
    }

    private void getPermission(){
    }
}
