package com.icar.launcher.location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;

public abstract class LocationInfo implements LocationListener {
    Double locationLatitude, locationLongitude;

    @Override
    public void onLocationChanged(Location location) {

        locationLatitude = location.getLatitude();
        locationLongitude = location.getLongitude();
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    public static LocationManager getLocation(Context context, String provider) {
        LocationManager locationManager;


        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {
            // Write you code here if permission already given.
        }

        try {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(provider, 2000, 5, (LocationListener) context);
            //locationLatitude=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
            //locationLongitude=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
        } catch (SecurityException e) {
            locationManager = null;
            e.printStackTrace();
        }
        return locationManager;
    }

}
