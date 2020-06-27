package com.example.flixster;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.adapters.MovieAdapter;
import com.example.flixster.databinding.ActivityMainBinding;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

//This is where our app will run. Screen will load movies binded top and bottom.
public class MainActivity extends AppCompatActivity {

    // Using movies DB API
    public static final String NOW_PLAYING_URL= "https://api.themoviedb.org/3/movie/now_playing?api_key=";
    public static final String VIDEO_PLAYING_URL1 = "https://api.themoviedb.org/3/movie/";
    public static final String VIDEO_PLAYING_URL2 = "/videos?api_key=";
    public static final String TAG= "MainActivity";

    //Hold the movies.
    List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Use Binding class to make accessing layout items easier.
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        RecyclerView rvMovies = binding.rvMovies;

        //Hold our movies
        movies = new ArrayList<>();

        //Create adapter
        final MovieAdapter movieAdapter = new MovieAdapter(this, movies);

        //Set the adapter on the recycler view
        rvMovies.setAdapter(movieAdapter);

        //Set a Layout Manager on the recycler view
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        //Calling the AsyncHttpClient
        AsyncHttpClient client = new AsyncHttpClient();

        //Calling the Movies DB API
        client.get(NOW_PLAYING_URL + getString(R.string.movies_db_api_key), new JsonHttpResponseHandler() {
            //Method if onSuccess
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                //Reads json request and obtains our movies' data.
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Results: " + results.toString());
                    movies.addAll(Movie.fromJsonArray(results));
                    movieAdapter.notifyDataSetChanged();
                    Log.i(TAG, "Movies are " + movies.size());

                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception", e);
                    e.printStackTrace();
                }
            }

            //Method called if onFailure
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure" + throwable.toString());
            }
        });
    }
}