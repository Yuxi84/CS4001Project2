package com.example.ehar.SensorExample;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.util.Observable;
import java.util.jar.Manifest;

/**
 * Created by Yuxi on 9/12/2016.
 */
public class LocationTracker
        extends Observable
        implements LocationListener{

    //instance variables and constants
    private final Activity myAct;
    private LocationManager locationManager;
    private final static int DISTANCE_UPDATES = 0;
    private final static int TIME_UPDATES = 0;
    private final static int PERMISSION_REQUEST_CODE = 1;
    private boolean LocationAvailable;

    public LocationTracker(Activity act){
        myAct = act;
        LocationAvailable = false;
        locationManager = (LocationManager) act.getSystemService(Context.LOCATION_SERVICE);
        if (checkPermission()) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME_UPDATES, DISTANCE_UPDATES, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, TIME_UPDATES, DISTANCE_UPDATES, this);
            if (locationManager != null){
                notifyObservers(locationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
            }
        } else {
            requestPermission();
        }

    }

    //methods to check permissions
    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(myAct, android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED){
            LocationAvailable = true;
            return true;
        }else {
            LocationAvailable = false;
            return false;
        }
    }


    //method to request permissions if checkPermission fails
    private void requestPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(myAct, android.Manifest.permission.ACCESS_FINE_LOCATION)){
            Toast.makeText(myAct, R.string.permissionRationale, Toast.LENGTH_LONG).show();
        }else{
            ActivityCompat.requestPermissions(myAct, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_REQUEST_CODE);
        }
    }

    public void onRequestPermissionResult(int requestCode, String[]permissions[], int[]grantResults){
        switch (requestCode){
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //now turn on monitoring
                    if (checkPermission()){
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME_UPDATES, DISTANCE_UPDATES,this);
                    }else {
                        //No permissions
                        Toast.makeText(myAct, "Permiaaiona not Granted", Toast.LENGTH_LONG).show();
                    }
                    break;
                }
        }
    }



    @Override
    public void onLocationChanged(Location location) {
        setChanged();
        notifyObservers(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        if (checkPermission()){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME_UPDATES, DISTANCE_UPDATES,this);
        }else {
            requestPermission();
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        if (checkPermission()){
            locationManager.removeUpdates(this);
        }else {
            requestPermission();
        }
    }
}
