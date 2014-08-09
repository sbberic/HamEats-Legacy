package com.hameat.hameats.fragments;

import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.hameat.hameats.R;
import com.hameat.hameats.api.apicalls.NearbyPlacesSearchTask;
import com.hameat.hameats.api.apicalls.OnApiResponseListener;

import java.util.ArrayList;

/**
 * Created by sbberic on 7/31/2014.
 */
public class PhotoViewFragment extends Fragment implements OnApiResponseListener,
    LocationListener {

    private static final long LOCATION_REFRESH_TIME = 100;
    private static final float LOCATION_REFRESH_DISTANCE = 200;

    private double curLat, curLong;
    private LocationManager mLocationManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_compare_food, container, false);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                LOCATION_REFRESH_DISTANCE, this);
        Location loc = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(loc != null) {
            curLat = loc.getLatitude();
            curLong = loc.getLongitude();
            doSearch();
        }

    }

    public void doSearch() {
        String base = this.getString(R.string.places_api_base);
        String key = "key=" + this.getString(R.string.google_api_key);
        String location = "&location=" + curLat +"," + curLong;
        String radius = "&radius=500";
        String types = "&types=restaurant|cafe";
        String[] params = {base, key, location, radius, types};
        new NearbyPlacesSearchTask(this.getActivity(), this).execute(params);
    }

    @Override
    public void processResults(Object o) {
        ArrayList<String> photoRefs = (ArrayList<String>) o;


    }

    @Override
    public void onLocationChanged(Location location) {
        if(location.getLongitude() != curLong || location.getLatitude() != curLat) {
            curLat = location.getLatitude();
            curLong = location.getLongitude();
            doSearch();
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
