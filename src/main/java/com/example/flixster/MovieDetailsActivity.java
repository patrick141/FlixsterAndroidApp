package com.example.flixster;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.databinding.ActivityMovieDetailsBinding;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

public class MovieDetailsActivity extends AppCompatActivity {
    //Reference to movie clicked
    Movie movie;

    //Movie's attributes
    TextView tvTitle;
    TextView tvOverview;
    TextView releaseDate;
    TextView popularity;
    RatingBar rbVoteAverage;
    ImageView ivPoster;
    Button press;
    String videoid;

    //Use for JSON HTTP Respond Handler
    String myURL;
    String realkey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMovieDetailsBinding binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //From binding view, get layout features.
        tvTitle = binding.tvTitle;
        tvOverview = binding.tvOverview;
        rbVoteAverage = binding.rbVoteAverage;
        releaseDate = binding.releaseDateTitle;
        popularity = binding.popularity;
        ivPoster = binding.ivPoster;
        press = binding.pressPlay;

        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));
        videoid = movie.getId().toString();
        Log.i("MovieDetails Activity", "this is " + videoid);
        Log.d("MovieDetails Activity", "this is " + videoid);
        Log.e("MovieDetails Activity", "this is " + videoid);


        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        releaseDate.setText("Released: " + movie.getReleaseDate());
        popularity.setText("Popularity: " + movie.getPopularity());
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Glide.with(this).load(movie.getBackdropPath()).transform(new RoundedCornersTransformation(50, 0)).into(ivPoster);
            //Glide.with(this).load(movie.getBackdropPath()).override(40,40).into(ivPoster);
        }
        else
        {
            Glide.with(this).load(movie.getPosterPath()).transform(new RoundedCornersTransformation(50, 0)).into(ivPoster);
        }
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage = voteAverage >0 ? voteAverage / 2.0f: voteAverage);


        myURL = MainActivity.VIDEO_PLAYING_URL1 + videoid + MainActivity.VIDEO_PLAYING_URL2 + getString(R.string.movies_db_api_key);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(myURL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d("MovieTrailerActivity", "Hello");
                JSONObject result = json.jsonObject;
                try {
                    JSONArray results = result.getJSONArray("results");
                    Log.d("TrailerActivity", "Results: " + results.toString());
                    if(results.length() > 0) {
                        realkey = results.getJSONObject(0).getString("key");
                    }
                } catch (JSONException e) {
                    Log.d("TrailerActivity", "Json Exception");
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d("TrailerActivity", myURL.toString());
            }
        });

        // User can press button "Play Trailer" and play movie trailer. If it has key, the button will play youtube video trailer.
        press.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(realkey != null) {
                    Intent intent = new Intent(MovieDetailsActivity.this, MovieTrailerActivity.class);
                    intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                    intent.putExtra(MovieTrailerActivity.videoid ,realkey);
                    MovieDetailsActivity.this.startActivity(intent);
                }
            }
        });
    }
}