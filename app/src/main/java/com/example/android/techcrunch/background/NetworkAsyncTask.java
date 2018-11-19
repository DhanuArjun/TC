package com.example.android.techcrunch.background;

import android.os.AsyncTask;
import android.util.Log;

import com.example.android.techcrunch.utils.HttpHelper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public final class NetworkAsyncTask extends AsyncTask<URL, Void, String> {
    private final String TAG = "Frag:: ";

    private static final String techCrunchUrl = "https://techcrunch.com/wp-json/wp/v2/posts?_embed&per_page=10";

    @Override
    protected String doInBackground(URL... urls) {
        String responseFromSite = null ;

        URL url = createUrl(techCrunchUrl);
        try {
            responseFromSite = HttpHelper.downloadUrl(url.toString());
            Log.i(TAG, "doInBackground: " + responseFromSite);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return responseFromSite;
    }

    private URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }



}