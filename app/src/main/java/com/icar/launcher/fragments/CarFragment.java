package com.icar.launcher.fragments;


import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.icar.launcher.R;
import com.icar.launcher.interfaces.GpsListener;
import com.icar.launcher.location.CLocation;
import com.icar.launcher.location.LocationInfo;

import in.unicodelabs.kdgaugeview.KdGaugeView;

public class CarFragment extends Fragment implements GpsListener {

    KdGaugeView speedoMeterView;
    ViewGroup thiscontainer;

    public CarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_car, container, false);
        thiscontainer = container;

        speedoMeterView = rootView.findViewById(R.id.speedMeter);
        LocationManager locationManager = LocationInfo.getLocation(thiscontainer.getContext());
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, (long) 0, (float) 0, this);

        return rootView;
    }

    private void updateSpeed(CLocation location) {
        // TODO Auto-generated method stub
        float nCurrentSpeed = 0;

        if (location != null) {
            //location.setUseMetricunits(this.useMetricUnits());
            nCurrentSpeed = location.getSpeed();
        }
        speedoMeterView.setSpeed(nCurrentSpeed);
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        if (location != null) {
            CLocation myLocation = new CLocation(location, true);
            this.updateSpeed(myLocation);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onGpsStatusChanged(int event) {
        // TODO Auto-generated method stub

    }

}
