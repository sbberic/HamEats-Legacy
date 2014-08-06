package com.hameat.hameats.api.apicalls;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Pair;

import java.io.BufferedInputStream;
import java.io.InputStream;

/**
 * Created by sbberic on 8/5/2014.
 */
public class PhotoSearchTask extends AsyncApiTask {

    @Override
    public Object doInBackground(String... params) {
        Object result = super.doInBackground(params);
        InputStream in = (InputStream) ((Pair) result).second;
        BufferedInputStream buf = new BufferedInputStream(in);
        Bitmap bMap = BitmapFactory.decodeStream(buf);
        return bMap;
    }
    @Override
    public void onPostExecute(Object result) {

    }

}
