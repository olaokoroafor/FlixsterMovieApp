package com.example.flixstermovieapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixstermovieapp.databinding.ActivityMainBinding;
import com.example.flixstermovieapp.databinding.ActivityMovieDetailsBinding;
import com.example.flixstermovieapp.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import okhttp3.Headers;

public class MovieDetailsActivity extends AppCompatActivity {
    private ActivityMovieDetailsBinding binding;
    public static final String TAG = "MovieDetailActivity";
    // the movie to display
    Movie movie;

    // the view objects
    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;
    ImageView ivActivityPoster;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // resolve the view objects
        tvTitle = (TextView) binding.tvTitle;//findViewById(R.id.tvTitle);
        tvOverview = (TextView) binding.tvOverview;//findViewById(R.id.tvOverview);
        rbVoteAverage = (RatingBar) binding.rbVoteAverage;//findViewById(R.id.rbVoteAverage);
        ivActivityPoster = (ImageView) binding.ivActivityPoster;//findViewById(R.id.ivActivityPoster);

        // unwrap the movie passed in via intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        AsyncHttpClient client = new AsyncHttpClient();
        client.get( "https://api.themoviedb.org/3/movie/" + movie.getId() + "/videos?api_key=1e2881a633aacd8d5007ae4052213043", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");

                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONObject video_stuff = jsonObject.getJSONArray("results").getJSONObject(0);
                    Log.i(TAG, "Results:" + video_stuff.toString());
                    movie.videoUrl = video_stuff.getString("key");
                    Log.i(TAG, "video url:" + movie.videoUrl);
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

        // set the title and overview
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        Glide.with(this)
                .load(movie.getPosterPath())
                .placeholder(R.drawable.flicks_movie_placeholder)
                .centerCrop()
                .into(ivActivityPoster);

        ivActivityPoster.setClickable(true);
        ivActivityPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create intent for the new activity
                Intent intent = new Intent(MovieDetailsActivity.this, MovieTrailerActivity.class);
                // serialize the movie using parceler, use its short name as a key
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                // show the activity
                MovieDetailsActivity.this.startActivity(intent);

            }
        });

        // vote average is 0..10, convert to 0..5 by dividing by 2
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage / 2.0f);




    }
}
