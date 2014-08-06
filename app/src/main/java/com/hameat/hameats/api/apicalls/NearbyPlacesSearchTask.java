package com.hameat.hameats.api.apicalls;

import android.content.Context;
import android.util.Pair;

import com.hameat.hameats.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by sbberic on 8/4/2014.
 */
public class NearbyPlacesSearchTask extends AsyncApiTask {

    private Context context;

    public NearbyPlacesSearchTask(Context context) {
        this.context = context;
    }
    @Override
    public Object doInBackground(String... params) {
        Object result = super.doInBackground(params);
        InputStream in = (InputStream) ((Pair) result).second;
        BufferedReader streamReader = null;
        ArrayList<String> photoRefs = new ArrayList<String>();
        try {
            streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);
            JSONObject json = new JSONObject(responseStrBuilder.toString());
            JSONArray results = json.getJSONArray("results");

            for(int i = 0; i < results.length(); i++) {
                JSONObject place = (JSONObject) results.get(i);
                JSONArray photos = (JSONArray) place.get("photos");
                JSONObject photo = (JSONObject) photos.get(0);
                String photoRef = (String) photo.get("photo_reference");;
                photoRefs.add(i, photoRef);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return photoRefs;
    }

    @Override
    public void onPostExecute(Object result) {
        ArrayList<String> photoRefs = (ArrayList<String>) result;
        for(String photoRef : photoRefs) {
            String base = context.getString(R.string.places_photo_api_base);
            String key = "key=" + context.getString(R.string.google_api_key);
            String photoReference = "&photoreference=" + photoRef;
            String maxWidth = "&maxwidth=400";
            String url = base + key + photoReference + maxWidth;

        }
    }
}
