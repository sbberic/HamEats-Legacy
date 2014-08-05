package com.hameat.hameats.api.apicalls;

import android.os.AsyncTask;
import android.util.Pair;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.AbstractMap;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by sbberic on 7/31/2014.
 */
public class AsyncApiTask extends AsyncTask<String, Void, Object>{
    @Override
    protected Object doInBackground(String... params) {
        StringBuilder stringBuilder = new StringBuilder();
        for(String s : params) {
            stringBuilder.append(s);
        }
        try {
            URL url = new URL(stringBuilder.toString());
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.connect();
            String contentType = conn.getContentType();
            Pair kv = new Pair(contentType, conn.getContent());
            return kv;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
