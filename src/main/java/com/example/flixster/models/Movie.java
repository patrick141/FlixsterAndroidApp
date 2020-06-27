package com.example.flixster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Movie {
    String backdropPath;
    String posterPath;
    String title;
    String overview;
    Double voteAverage;
    Double popularity;
    Integer id;
    String releaseDate;

    //Default constructor
    public Movie(){};

    //Attribute of movie objects.
    public Movie(JSONObject jsonObject) throws JSONException {
        posterPath = jsonObject.getString("poster_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        backdropPath = jsonObject.getString("backdrop_path");
        voteAverage = jsonObject.getDouble("vote_average");
        id = jsonObject.getInt("id");
        releaseDate = jsonObject.getString("release_date");
        popularity = jsonObject.getDouble("popularity");
    }

    //Generate our list of movie objects.
    public static List<Movie> fromJsonArray(JSONArray movieJsonArray) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        for(int i = 0; i < movieJsonArray.length(); i++){
            movies.add(new Movie(movieJsonArray.getJSONObject(i)));
        }
        return movies;
    }

    //get our BackDrop Image Link
    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", backdropPath);
    }

    //get our PosterPath image link
    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    //Getters for our other movie attributes
    public String getTitle() { return title; }

    public String getOverview() { return overview; }

    public Double getVoteAverage() { return voteAverage; }

    public Double getPopularity(){return popularity;}

    public Integer getId() {return id;}

    public String getReleaseDate(){return releaseDate;}

}
