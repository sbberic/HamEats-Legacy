package com.hameat.hameats.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hameat.hameats.R;
import com.hameat.hameats.api.apicalls.OnApiResponseListener;

/**
 * Created by sbberic on 7/31/2014.
 */
public class PhotoViewFragment extends Fragment implements OnApiResponseListener{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_compare_food, container, false);
        return rootView;
    }

    @Override
    public void processResults() {

    }
}
