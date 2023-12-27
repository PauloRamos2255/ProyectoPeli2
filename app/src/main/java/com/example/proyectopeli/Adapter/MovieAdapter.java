package com.example.proyectopeli.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.proyectopeli.DescripcionActivity;
import com.example.proyectopeli.Entidad.Movie;
import com.example.proyectopeli.R;
import com.google.gson.Gson;

import java.util.List;



public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private List<Movie> movies;
    private Context context;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    public void updateMovies(List<Movie> newMovies) {
        movies.clear();
        movies.addAll(newMovies);
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);

        String texto = movie.getTitle();


        int maxLength = 20;

        if (texto.length() > maxLength) {
            String textoCorto = texto.substring(0, maxLength);

            // Agrega puntos suspensivos
            textoCorto += "...";

            // Establece el texto corto en el TextView
            holder.titleTextView.setText(textoCorto);
        } else {
            holder.titleTextView.setText(texto);
        }


        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.defaultplaceholder)
                .error(R.drawable.image);
        Glide.with(holder.itemView.getContext())
                .load("https://image.tmdb.org/t/p/w200" + movie.getPosterFullPath())
                .placeholder(R.drawable.defaultplaceholder)
                .apply(requestOptions)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .error(R.drawable.image)
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String movieJson = new Gson().toJson(movie);
                Intent intent = new Intent(context, DescripcionActivity.class);
                intent.putExtra("movie", movieJson);
                context.startActivity(intent);
            }
        });

    }



    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView;
        public ImageView imageView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.movieTitle);
            imageView = itemView.findViewById(R.id.moviePoster);
        }

    }

}
