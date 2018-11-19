package com.example.android.techcrunch.activities;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.android.techcrunch.R;
import com.example.android.techcrunch.background.NetworkAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String techCrunchUrl = "https://techcrunch.com/wp-json/wp/v2/posts?_embed&per_page=10";

    String newData;
    TextView textView;
    ImageView imageView;
    String imageUrl;
    TextView textView2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text_out);
        imageView = findViewById(R.id.image_out);
        textView2 = findViewById(R.id.text_out2);

       new NetworkAsyncTask().execute();

    }


    private void doIt() {


        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(techCrunchUrl)
                .build();
        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Void, String>  asyncTask = new AsyncTask<Void, Void, String>() {
            private static final String TAG = "Frag";


            @Override
            protected String doInBackground(Void... params) {

                try {
                    Response response = client.newCall(request).execute();

                    if(!response.isSuccessful()){
                        Log.d(TAG, "data is received ");
                        return null;
                    }

                    Log.d(TAG, "data is received ");
                    newData = response.body().string();
                    response.body().close();
                    return newData;
                }catch (Exception e){
                    e.printStackTrace();
                    return null;
                }


            }


            @Override
            protected void onPostExecute(String newData) {


                super.onPostExecute(newData);
                if(newData!= null){
                    Log.d(TAG, newData);


                    try {
                        //create a json array
                        JSONArray json = new JSONArray(newData);
                        //read through every line
                        for(int i =  0; i < json.length(); i++) {
                            JSONObject post = json.getJSONObject(i);
                            String tit = post.getJSONObject("title").getString("rendered");
                            String content = post.getJSONObject("content").getString("rendered");
                            String imageURL = post.getJSONObject("_embedded").getJSONArray("wp:featuredmedia")
                                    .getJSONObject(0).getJSONObject("media_details").getString("file");
                            imageUrl = "http://techcrunch.com/wp-content/uploads/" + imageURL;
                            Log.d(TAG, " this is image" + imageUrl);
                            String description = Jsoup.parse(content).text();
                            String title = Jsoup.parse(tit).text();



                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

        asyncTask.execute();
    }
}