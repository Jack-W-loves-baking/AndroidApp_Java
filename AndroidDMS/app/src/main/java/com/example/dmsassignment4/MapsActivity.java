package com.example.dmsassignment4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;


import com.android.volley.RequestQueue;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This activity calls google map api and add the user locations as markers to the map.
 */
public class MapsActivity extends AppCompatActivity
        implements GoogleMap.OnMarkerClickListener,OnMapReadyCallback,
        OnSuccessListener<LocationSettingsResponse>, OnFailureListener  {

    //variables declared.
    public static final int RESOLVE_SETTINGS_REQUEST = 1;
    public static final String TAG = "TAG";
    private LocationRequest locationRequest;
    private FusedLocationProviderClient fusedlocationClient;
    private GoogleMap map;
    private LatLng userLatLng;
    private LatLng AUT;
    private LocationCallbackHandler locationCallback;
    public static ArrayList<User> wholeList = new ArrayList<>();
    private TextView textView;
    private Switch mySwitch;
    private ArrayList<LatLng> locations = new ArrayList<>();

    /**
     *
     * @param savedInstanceState called when creating the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Initialize the variables
        textView = findViewById(R.id.infoBoard);

        //initilize the geolocation for AUT.
        AUT = new LatLng(-36.8540, 174.7672);
        //Create the location request
        createLocationRequest();

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        //Switch listener. If turn on the switch, the board will pop up,
        //Otherwise disappear. Default is on.
        mySwitch = findViewById(R.id.switchlocation);
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    textView.setVisibility(View.VISIBLE);
                } else {
                    textView.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    /**
     * Create the location request
     */
    protected void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder
                = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        LocationSettingsRequest settingsRequest = builder.build();
        SettingsClient settingsClient
                = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> checkSettingsTask
                = settingsClient.checkLocationSettings(settingsRequest);
        checkSettingsTask.addOnSuccessListener(this, this);
        checkSettingsTask.addOnFailureListener(this, this);
        fusedlocationClient
                = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallbackHandler();

    }


    /**
     * Override if user paused and get access from outside
     */
    @Override
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedlocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    /**
     * pause the activity
     */
    @Override
    public void onPause() {
        super.onPause();
        fusedlocationClient.removeLocationUpdates(locationCallback);
    }

    /**
     *
     * @param googleMap API to load google map
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        //Simple view
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        UiSettings settings = map.getUiSettings();
        //zoom buttons available
        settings.setZoomGesturesEnabled(true);
        settings.setZoomControlsEnabled(true);
        settings.setMyLocationButtonEnabled(true);
        settings.setMapToolbarEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //for the first init, if the user if null, assign the value of AUT
        if (userLatLng == null) {
            userLatLng = AUT;
        }
        //my location available, together with locate user button.
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 13));

        map.addMarker(new MarkerOptions().position(AUT)
                .title("AUT")
                .snippet("we love software engineer")
                .icon(BitmapDescriptorFactory.defaultMarker
                        (BitmapDescriptorFactory.HUE_GREEN)));

        //Assign markers to different locations
        for (int i = 0; i < wholeList.size(); i++) {

            locations.add(new LatLng(wholeList.get(i).getLatitude(), wholeList.get(i).getLongitude()));
            //add locations to markers
            map.addMarker(new MarkerOptions().position(locations.get(i))
                    .title(wholeList.get(i).getTitle())
                    .snippet("Family member")
                    .icon(BitmapDescriptorFactory.defaultMarker
                            (BitmapDescriptorFactory.HUE_RED)));
        }
    }

    /**
     *
     * @param marker When click the markers, camera will focus on the marker clicked
     * @return
     */
    @Override
    public boolean onMarkerClick(final Marker marker){
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 14));

        return false;
    }
    @Override
    public void onSuccess
            (LocationSettingsResponse locationSettingsResponse) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedlocationClient.requestLocationUpdates(locationRequest,
                locationCallback, Looper.getMainLooper());
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        if (e instanceof ResolvableApiException) {
            // Location settings are not satisfied, but this can be
            // fixed by showing the user a dialog.
            try {
                // Show the dialog by calling startResolutionForResult(),
                // and check the result in onActivityResult().
                ResolvableApiException resolvable
                        = (ResolvableApiException) e;
                resolvable.startResolutionForResult(this,
                        RESOLVE_SETTINGS_REQUEST);
            } catch (IntentSender.SendIntentException sie) {
                Log.w(MapsActivity.class.getName(),
                        "Unable to send dialog intent: " + sie);
            }
        }
    }

    /**
     *
     * @param view fetch the data then call back to onMapReady, reload the information.
     */
    public void retrieveAll(View view) {

        //multi thread, this is to execute json parsing.
        Info info = new Info(textView);
        info.execute();

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /**
     * This is to call the class to upload our current location to the database
     * @param view
     */
    public void uploadInfo(View view) {

        Map<String, Object> maps = new HashMap<>();
        maps.put("Latitude",userLatLng.latitude);
        maps.put("Longitude",userLatLng.longitude);
        maps.put("title",LoginActivity.USERNAME);

        if(userLatLng!=null||LoginActivity.USERNAME!=null){
            Upload task = new Upload(maps);
            task.execute();
        }
    }

    /**
     * Call back function, if the location has been changed.
     */
    private class LocationCallbackHandler extends LocationCallback {

        public void onLocationAvailability(LocationAvailability
                                                   locationAvailability) {
            super.onLocationAvailability(locationAvailability);
            Log.i(MapsActivity.class.getName(),
                    "Location availability changed");
        }

        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (locationResult == null) {
                Log.w(MapsActivity.class.getName(),
                        "Received null location result");
                return;
            }

            Location mostRecent = locationResult.getLastLocation();
            if (map != null)
            {
                userLatLng = new LatLng(mostRecent.getLatitude(),
                        mostRecent.getLongitude());
                Log.i(MapsActivity.class.getName(),
                        "Received user location at " + userLatLng);

            }
            else
            {
                Log.w(MapsActivity.class.getName(),
                        "Receiving locations but maps not yet available");
            }
        }
    }

}