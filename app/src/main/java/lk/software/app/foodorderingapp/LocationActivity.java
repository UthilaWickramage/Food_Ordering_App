package lk.software.app.foodorderingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import com.google.android.gms.location.LocationRequest;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import lk.software.app.foodorderingapp.model.User;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 10;
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap googleMap;
FirebaseFirestore firebaseFirestore;
FirebaseUser currentUser;
private ArrayList<String> addressData;

TextView address,area,city;
    private Marker marker_current;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        addressData = new ArrayList<>();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        address = findViewById(R.id.textView25);
        area = findViewById(R.id.textView37);
        city = findViewById(R.id.textView38);

findViewById(R.id.imageView3).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        finish();
    }
});
    }
    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        this.googleMap = map;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        if (checkPermission()) {
            googleMap.setMyLocationEnabled(true);
            getLastLocation();
        } else {
            requestPermissions(
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    LOCATION_PERMISSION_REQUEST_CODE
            );
        }
    }
    private boolean checkPermission() {
        boolean permissions = false;
        // if checks with gps its fine location permission and coarse location is for tower system
        if (
            //this manifest should be android manifest not our project manifest;
                checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            permissions = true;
        }

        return permissions;
    }
    private void moveCamera(LatLng latLng){
        CameraPosition cameraPosition = CameraPosition.builder()
                .target(latLng)
                .zoom(20f)
                .build();

        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        googleMap.animateCamera(cameraUpdate);

    }
//    private void getLastLocation() {
//        if(checkPermission()){
//            Task<Location> lastLocation = fusedLocationProviderClient.getLastLocation();
//            lastLocation.addOnSuccessListener(new OnSuccessListener<Location>() {
//                @Override
//                public void onSuccess(Location location) {
//                    if(location!=null){
//                        currentLocation = location.;
//                        LatLng latLng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
//                        googleMap.addMarker(new MarkerOptions().position(latLng).title("My Location"));
//                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,20));
//
//                    }
//                }
//            });
//        }
//
////        assert checkPermission();
////
////        //communication will drain the battery to give best accuracy location
////        com.google.android.gms.location.LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY,7000)
////                .setWaitForAccurateLocation(true)
////                .setMinUpdateIntervalMillis(2000)
////                .setMaxUpdateDelayMillis(4000)
////                .build();
////
////        fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
////            @Override
////            public void onLocationResult(@NonNull LocationResult locationResult) {
////                super.onLocationResult(locationResult);
////                currentLocation = locationResult.getLastLocation();
////                LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
////
////                if(marker_current==null){
////
////                    MarkerOptions liveLocation = new MarkerOptions()
////                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_location_on_24))
////                            .title("Live Location").position(latLng);
////                    marker_current =googleMap.addMarker(liveLocation);
////                }else{
////                    marker_current.setPosition(latLng);
////                }
////                moveCamera(latLng);
////
////            }
////        }, Looper.getMainLooper());
//
//
//    }
    private void getLastLocation() {
        if(checkPermission()){
            Task<Location> lastLocation = fusedLocationProviderClient.getLastLocation();
            lastLocation.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location!=null){
                        currentLocation = location;
                        LatLng latLng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
                        googleMap.addMarker(new MarkerOptions().position(latLng).title("My Location"));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,20));
                        saveLocation(currentLocation.getLatitude(),currentLocation.getLongitude());
                    }
                }
            });
//            LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY,7000)
//                    .setWaitForAccurateLocation(true)
//                    .setMinUpdateIntervalMillis(2000)
//                    .setMaxUpdateDelayMillis(4000)
//                    .build();
//
//            fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
//                @Override
//                public void onLocationResult(@NonNull LocationResult locationResult) {
//                    super.onLocationResult(locationResult);
//                    currentLocation = locationResult.getLastLocation();
//                    LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
//
//                    if(marker_current==null){
//
//                        MarkerOptions liveLocation = new MarkerOptions()
//                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.man))
//                                .title("Live Location").position(latLng);
//                        marker_current =googleMap.addMarker(liveLocation);
//                    }else{
//                        marker_current.setPosition(latLng);
//                    }
//                    moveCamera(latLng);
//                saveLocation(currentLocation.getLatitude(), currentLocation.getLongitude());
//                }
//            }, Looper.getMainLooper());
        }



    }

    private void saveLocation(double latitude, double longitude) {
        findAddress(getApplicationContext(),latitude,longitude);

        if (currentUser != null) {


            firebaseFirestore.collection("customers").document(currentUser.getUid()).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                User user = task.getResult().toObject(User.class);
                                if (user != null) {
                                    user.setLatitude(String.valueOf(latitude));
                                    user.setLongitude(String.valueOf(longitude));

                                    findAddress(LocationActivity.this, latitude, longitude);
                                    if(!addressData.isEmpty()){
                                        user.setAddress(addressData.get(0));
                                        user.setArea(addressData.get(1));
                                        user.setCity(addressData.get(2));
                                        user.setPostal_code(addressData.get(3));
                                        Log.i("addressData",addressData.get(0));
                                        address.setText(addressData.get(0));
                                        area.setText(addressData.get(3));
                                        city.setText(addressData.get(1));
                                    }else{
                                        Log.i("empty","empty");
                                    }

                                    updateUser(user);
                                }
                            }

                        }
                    });
        }
    }

    private void updateUser(User user) {
        firebaseFirestore.collection("customers").document(currentUser.getUid()).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(),"address details saved successfully",Toast.LENGTH_SHORT).show();


                        finish();
                    }
                });
    }


    private void findAddress(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(latitude, longitude, 1, new Geocoder.GeocodeListener() {
                @Override
                public void onGeocode(@NonNull List<Address> addresses) {

                    String address = addresses.get(0).getAddressLine(0);
                    String subarea = addresses.get(0).getSubAdminArea();
                    String postalCode = addresses.get(0).getPostalCode();
                    String locality = addresses.get(0).getLocality();

                    addressData.add(address);
                    addressData.add(subarea);
                    addressData.add(locality);
                    addressData.add(postalCode);

                    Log.d("address",address);

                    Log.d("address",postalCode);

                    Log.d("subarea",subarea);

                    Log.d("locality",locality);

                }
            });


        }else{

        }
        if(addressData.isEmpty()){
            Log.i("before return","empty");

        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                getLastLocation();
            } else {
                Snackbar.make(findViewById(R.id.constraint), "Location permission denied", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Settings", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                                        //tells the system to start settings as a new task
                                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }).show();
            }
        }
    }
}