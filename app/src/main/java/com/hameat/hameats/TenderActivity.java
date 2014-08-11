package com.hameat.hameats;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.Window;


import com.hameat.hameats.api.apicalls.NearbyPlacesSearchTask;
import com.hameat.hameats.api.apicalls.OnApiResponseListener;
import com.hameat.hameats.fragments.FoodFragment;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TenderActivity extends Activity  implements LocationListener,OnApiResponseListener {
    private static final String TAG = "TenderActivity";
    private static final String PAGE_INDEX = "page";
    private static final String FOOD_KEY = "food";

    private Context mContext;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private int foodNumber;
    ArrayList<Food> mFoods;
    Button mStartButton;
    RelativeLayout parentView;
    //
    private static final long LOCATION_REFRESH_TIME = 100;
    private static final float LOCATION_REFRESH_DISTANCE = 200;

    private double curLat, curLong;
    private LocationManager mLocationManager;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tender);

        //TODO: Save state through destroy
        if (savedInstanceState != null){
            foodNumber = savedInstanceState.getInt(PAGE_INDEX, 0);
            mFoods = (ArrayList)savedInstanceState.getParcelableArrayList(FOOD_KEY);
        }else {
            foodNumber = 0;
            mFoods = new ArrayList<Food>() {};
        }
        mContext = TenderActivity.this;
        parentView = (RelativeLayout)findViewById(R.id.tenderLayout);
        mFragmentManager = getFragmentManager();
        // mFragmentTransaction = mFragmentManager.beginTransaction();
        mStartButton = (Button)findViewById(R.id.startButton);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupStart();
                mStartButton.setVisibility(View.INVISIBLE);
            }
        });
        //
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                LOCATION_REFRESH_DISTANCE, this);
        Location loc = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Log.d(TAG, "locnull"+(loc==null)+"");
        if(loc != null) {
            curLat = loc.getLatitude();
            curLong = loc.getLongitude();
            doSearch();
        }
        doSearch();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt(PAGE_INDEX,foodNumber);
        savedInstanceState.putParcelableArrayList(FOOD_KEY, mFoods);
    }

    public void setupStart(){
        addPicture();
        addPicture();
        addPicture();
    }

    public void addPicture(){
        mFragmentTransaction = mFragmentManager.beginTransaction();
        if (!mFoods.isEmpty())
            mFragmentTransaction.add(R.id.tenderLayout, FoodFragment.newInstance(mFoods.remove(0)));
        else {
            parentView.setBackground(getResources().getDrawable(R.drawable.puppy));
        }
        mFragmentTransaction.commit();
        Log.d(TAG, "Foods left: "+mFoods.size());
    }

    public void doSearch() {
        String base = this.getString(R.string.places_api_base);
        String key = "key=" + this.getString(R.string.google_api_key);
        String location = "&location=" + 37.86750570 +"," + -122.253274000000030000;
        String radius = "&radius=500";
        String types = "&types=restaurant|cafe";
        String[] params = {base, key, location, radius, types};
        new NearbyPlacesSearchTask(this, this).execute(params);
    }

    @Override
    public void processResults(Object o) {
        ArrayList<String> photoRefs = (ArrayList<String>) o;
        for (String photo: photoRefs){
            Food newFood = new Food(photo,"asd");
            mFoods.add(newFood);
        }
        Log.d(TAG, "processresults"+photoRefs.size());

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
