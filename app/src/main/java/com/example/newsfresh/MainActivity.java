package com.example.newsfresh;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NewsItemClicked {
    RecyclerView recyclerView;
    NewsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fetchData();
        adapter = new NewsListAdapter(this);
        recyclerView.setAdapter(adapter);
    }
    private void fetchData(){
        String url = "https://gnews.io/api/v4/top-headlines?token=42449c7c18aa9c5180740f044b9382a3&lang=en";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray newsJsonArray = response.getJSONArray("articles");
                            ArrayList<News> newsArray = new ArrayList<>();
                            for (int i = 0 ; i < newsJsonArray.length() ; i++){
                                JSONObject newsJsonObject = newsJsonArray.getJSONObject(i);
                                News news = new News();
                                news.title = newsJsonObject.getString("title");
                                news.url = newsJsonObject.getString("url");
                                news.imageUrl = newsJsonObject.getString("image");

                                newsArray.add(news);
                            }
                            adapter.updateNews(newsArray);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

    }

    @Override
    public void onItemClicked(News item) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        int colorInt = Color.parseColor("#FF0000"); //red
        builder.setToolbarColor(colorInt);
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(item.url));
    }
}