package com.example.flixster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flixster.MovieDetailsActivity;
import com.example.flixster.R;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

//This class handles how our movie will be display on screen.
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{

    // Hold our context and movies.
    Context context;
    List<Movie> movies;

    //Constructor
    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    // Inflating a layout from XML and returning the holder.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("MovieAdapter", "onCreateViewHolder");
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);
    }

    // Involves populating data into the item through holder.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("MovieAdapter", "onBindViewHolder" + position);
        Movie movie = movies.get(position);
        //Bind movie data into viewholder.
        holder.bind(movie);
    }

    // Return count of items
    @Override
    public int getItemCount() {
        return movies.size();
    }

    //ViewHolder for our RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        //Layout
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            itemView.setOnClickListener(this);
        }

        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            String imageURL;
            // if phone is in landscape, imageURL = back drop image
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imageURL = movie.getBackdropPath();
            }
            // else, in portrait, imageURL = poster path image
            else{
                imageURL = movie.getPosterPath();
            }

            //Rounded corners feature to the photos.
            int radius = 50; // corner radius, higher value = more rounded
            int margin = 0; // crop margin, set to 0 for corners with no crop

            //If on landscape mode, imageURL = backdrop path image.
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                Glide.with(context).load(imageURL).transform(new RoundedCornersTransformation(radius, margin)).placeholder(R.drawable.flicks_backdrop_placeholder).into(ivPoster);
            }
            // else, in portrait, imageURL = poster path image
            else{
                Glide.with(context).load(imageURL).transform(new RoundedCornersTransformation(radius, margin)).placeholder(R.drawable.flicks_movie_placeholder).into(ivPoster);
            }
        }
        //When user clicks on movie, the details activity page opens.
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION){
                //Send our intent to go to movie details activity
                Movie movie = movies.get(position);
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                context.startActivity(intent);
            }
        }
    }
}
