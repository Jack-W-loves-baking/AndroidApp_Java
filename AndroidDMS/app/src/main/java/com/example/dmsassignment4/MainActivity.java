package com.example.dmsassignment4;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

/**
 * This main activity will check if the permission has been granted.
 * If yes, go to login page.
 */
public class MainActivity extends AppCompatActivity {

    public static final int PERMISSION_REQUEST_CODE = 1;
    public static final int PLAY_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     *
     * @return true if location access has been granted. False, if not.
     */
    private boolean hasLocationPermission() {
        int permissionCheck = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {

            //pop up dialog to ask for access.
            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this,R.style.AlertDialog)
                        .setTitle("Request Permission")
                        .setMessage("Need access to location service :)")
                        .setPositiveButton(
                                R.string.request_permission_positive,
                                (dialogInterface, i) -> ActivityCompat.requestPermissions
                                        (MainActivity.this, new String[]
                                                        {Manifest.permission.
                                                                ACCESS_FINE_LOCATION},
                                                PERMISSION_REQUEST_CODE))
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]
                                {Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_REQUEST_CODE);
            }
            return false;
        }
    }

    public void onResume() {
        super.onResume();
        // check whether the device has Google Play services enabled
        GoogleApiAvailability apiAvailability
                = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode == ConnectionResult.SUCCESS) {
            Log.i(MainActivity.class.getName(),
                    "Google Play API available");
        } else {
            Log.w(MainActivity.class.getName(),
                    "Google Play API not available");
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode,
                        PLAY_REQUEST_CODE).show();
            } else
                finish();
        }
        // check whether location services are available
        if (hasLocationPermission()) {

            Log.i(MainActivity.class.getName(), "Location API available");
        } else {
            Log.w(MainActivity.class.getName(),
                    "Location API not available");
        }

    }

    public void goToLogin(View view) {

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {

            //redirect to login page
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            if (hasLocationPermission()) {

                Log.i(MainActivity.class.getName(), "Location API available");
            } else {
                Log.w(MainActivity.class.getName(),
                        " Can't show map without location permission");
            }
        }

    }
}