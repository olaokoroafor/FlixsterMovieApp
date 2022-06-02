package com.example.flixstermovieapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.telephony.ims.ImsMmTelManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.flixstermovieapp.MovieDetailsActivity;
import com.example.flixstermovieapp.R;
import com.example.flixstermovieapp.databinding.ActivityMainBinding;
import com.example.flixstermovieapp.databinding.ItemMovieBinding;
import com.example.flixstermovieapp.models.Movie;

import org.parceler.Parcels;
//import android.jp.wasabeef.RoundedCornersTransformation;

import java.util.List;

public class MovieAdapter  extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{

    Context context;
    List<Movie> movies;
    ItemMovieBinding binding;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout from xml and returning the holder
        binding = ItemMovieBinding.inflate(LayoutInflater.from(context),parent, false);
        return new ViewHolder(binding);
    }

    //involves populating the data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //get movie at the passed in position
        Movie movie = movies.get(position);
        //bind the movie data into the VH
        holder.bind(movie);

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;

         public ViewHolder(@NonNull ItemMovieBinding binding) {
            //super(itemView);
            super(binding.getRoot());
            tvTitle = binding.tvTitle;//itemView.findViewById(R.id.tvTitle);
            tvOverview = binding.tvOverview;//itemView.findViewById(R.id.tvOverview);
            ivPoster = binding.ivPoster;//itemView.findViewById(R.id.ivPoster);
            itemView.setOnClickListener(this);
        }

        public void bind(Movie movie) {
             tvTitle.setText(movie.getTitle());
             tvOverview.setText(movie.getOverview());
             String imageUrl;
             int placeholder;
             if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                imageUrl = movie.getBackdropPath();
                placeholder = R.drawable.flicks_movie_placeholder;
             }
             else{
                 imageUrl = movie.getPosterPath();
                 placeholder = R.drawable.flicks_backdrop_placeholder;
             }
            int radius = 300; // corner radius, higher value = more rounded
             Glide.with(context)
                     .load(imageUrl)
                     .transform(new RoundedCorners(radius))
                     .placeholder(placeholder)
                     .centerCrop()
                     .into(ivPoster );

        }

        @Override
        public void onClick(View v) {
            // gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position, this won't work if the class is static
                Movie movie = movies.get(position);
                // create intent for the new activity
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                // serialize the movie using parceler, use its short name as a key
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                // show the activity
                context.startActivity(intent);
            }

        }
    }
}
