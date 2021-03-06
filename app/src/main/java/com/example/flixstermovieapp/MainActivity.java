package com.example.flixstermovieapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import com.bumptech.glide.load.engine.Resource;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixstermovieapp.adapters.MovieAdapter;
import com.example.flixstermovieapp.databinding.ActivityMainBinding;
import com.example.flixstermovieapp.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {
    public String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=1e2881a633aacd8d5007ae4052213043";
    public static final String TAG = "MainActivity";
    private  ActivityMainBinding binding;
    List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        //setContentView(R.layout.activity_main);
        setContentView(binding.getRoot());
        RecyclerView rvMovies = binding.rvMovies;//findViewById(R.id.rvMovies);
        movies = new ArrayList<>();

        //create adapter
        MovieAdapter movieAdapter = new MovieAdapter(this, movies);

        //set the adapter on the recycler view
        rvMovies.setAdapter(movieAdapter);

        //set a layoutmanager on the recycler view
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        Log.i(TAG, NOW_PLAYING_URL);


        AsyncHttpClient client = new AsyncHttpClient();
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");

                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Results:" + results.toString());
                    //important to say add all instead of pointing to new object
                    movies.addAll(Movie.fromJsonArray(results));
                    movieAdapter.notifyDataSetChanged();
                    Log.i(TAG, "Results Length:" + movies.size());
                } catch (JSONException e) {
                    Log.e(TAG, "hit json exception", e);
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure" + response + statusCode);
            }
        });
    }
}