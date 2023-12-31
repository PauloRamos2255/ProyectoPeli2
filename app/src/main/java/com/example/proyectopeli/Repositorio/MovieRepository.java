package com.example.proyectopeli.Repositorio;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.proyectopeli.Conecction.APIConexion;
import com.example.proyectopeli.Conecction.MovieAPI;
import com.example.proyectopeli.Entidad.Movie;
import com.example.proyectopeli.Entidad.MovieReponse;
import com.example.proyectopeli.Entidad.Video;
import com.example.proyectopeli.Entidad.VideoResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MovieRepository {

    private final MovieAPI movieApi;
    String language = "es-ES";

    public MovieRepository() {
        Retrofit retrofit = APIConexion.getClient();
        movieApi = retrofit.create(MovieAPI.class);
    }

    public void getPopularMovies(String apiKey, final OnMoviesCallback callback) {
        Call<MovieReponse> call = movieApi.getPopularMovies(apiKey,language);

        call.enqueue(new Callback<MovieReponse>() {
            @Override
            public void onResponse(Call<MovieReponse> call, Response<MovieReponse> response) {
                if (response.isSuccessful()) {
                    MovieReponse movieResponse = response.body();

                    if (movieResponse != null) {
                        List<Movie> movies = movieResponse.getResults();
                        callback.onSuccess(movies);
                    }
                } else {
                    callback.onError("Error al obtener la lista de películas");
                }
            }

            @Override
            public void onFailure(Call<MovieReponse> call, Throwable t) {
                t.printStackTrace();
                callback.onError("Error de red al obtener la lista de películas");
            }
        });
    }

    public interface OnMoviesCallback {
        void onSuccess(List<Movie> movies);

        void onError(String errorMessage);
    }



    public void getSearchMovies(String apiKey, String query,final SearchCallback callback){
        Call<MovieReponse> call = movieApi.getSearchMovies(apiKey ,language, query);
        call.enqueue(new Callback<MovieReponse>() {
            @Override
            public void onResponse(Call<MovieReponse> call, Response<MovieReponse> response) {
                if (response.isSuccessful()) {
                    MovieReponse movieResponse = response.body();
                    if (movieResponse != null) {
                        List<Movie> movies = movieResponse.getResults();

                        List<Movie> filteredMovies = new ArrayList<>();
                        String searchText = query.toLowerCase();

                        for (Movie movie : movies) {
                            if (movie.getTitle().toLowerCase().contains(searchText)) {
                                filteredMovies.add(movie);
                            }
                        }

                        callback.onSuccess(filteredMovies);
                    }
                } else {
                    callback.onError("Error al obtener la lista de películas");
                }
            }
            @Override
            public void onFailure(Call<MovieReponse> call, Throwable t) {
                t.printStackTrace();
                callback.onError("Error de red al obtener la lista de películas");
            }
        });
    }

    public interface SearchCallback {
        void onSuccess(List<Movie> moviess);

        void onError(String errorMessage);
    }

    public void getCarteleraMovies(String apiKey, final MoviesCallback callback){

        Call<MovieReponse> call = movieApi.getCarteleraMovies(apiKey ,language );
        call.enqueue(new Callback<MovieReponse>() {
            @Override
            public void onResponse(Call<MovieReponse> call, Response<MovieReponse> response) {
                if (response.isSuccessful()) {
                    MovieReponse movieResponse = response.body();
                    if (movieResponse != null) {
                        List<Movie> movies = movieResponse.getResults();
                        callback.onSuccess(movies);
                    }
                } else {
                    callback.onError("Error al obtener la lista de películas");
                }
            }
            @Override
            public void onFailure(Call<MovieReponse> call, Throwable t) {
                t.printStackTrace();
                callback.onError("Error de red al obtener la lista de películas");
            }
        });
    }

    public interface MoviesCallback {
        void onSuccess(List<Movie> moviess);

        void onError(String errorMessage);
    }

    public void getTendenciaMovies(String apiKey, final TendenciaCall callback){

        Call<MovieReponse> call = movieApi.getTendenciaMovies(apiKey ,language );
        call.enqueue(new Callback<MovieReponse>() {
            @Override
            public void onResponse(Call<MovieReponse> call, Response<MovieReponse> response) {
                if (response.isSuccessful()) {
                    MovieReponse movieResponse = response.body();
                    if (movieResponse != null) {
                        List<Movie> movies = movieResponse.getResults();
                        callback.onSuccess(movies);
                    }
                } else {
                    callback.onError("Error al obtener la lista de películas");
                }
            }
            @Override
            public void onFailure(Call<MovieReponse> call, Throwable t) {
                t.printStackTrace();
                callback.onError("Error de red al obtener la lista de películas");
            }
        });
    }

    public interface TendenciaCall {
        void onSuccess(List<Movie> moviess);

        void onError(String errorMessage);
    }

    public void getEstrenosMovies(String apiKey, final EstrenosCall callback){

        Call<MovieReponse> call = movieApi.getEstrenosMovies(apiKey ,language );
        call.enqueue(new Callback<MovieReponse>() {
            @Override
            public void onResponse(Call<MovieReponse> call, Response<MovieReponse> response) {
                if (response.isSuccessful()) {
                    MovieReponse movieResponse = response.body();
                    if (movieResponse != null) {
                        List<Movie> movies = movieResponse.getResults();
                        callback.onSuccess(movies);
                    }
                } else {
                    callback.onError("Error al obtener la lista de películas");
                }
            }
            @Override
            public void onFailure(Call<MovieReponse> call, Throwable t) {
                t.printStackTrace();
                callback.onError("Error de red al obtener la lista de películas");
            }
        });
    }

    public interface EstrenosCall {
        void onSuccess(List<Movie> moviess);

        void onError(String errorMessage);
    }


    public void fetchVideosForMovie(String movieId, String apiKey, final ObtenerVideo listener) {
        Call<VideoResponse> call = movieApi.getMovieVideos(movieId, apiKey, language);

        call.enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                if (response.isSuccessful()) {
                    VideoResponse videoResponse = response.body();
                    if (videoResponse != null) {
                        List<Video> videos = videoResponse.getResults();
                        listener.onSuccess(videos);
                    } else {
                        listener.onError("Error en la respuesta de la API al obtener la lista de videos para la película");
                    }
                } else {
                    listener.onError("Error en la respuesta de la API al obtener la lista de videos para la película");
                }
            }

            @Override
            public void onFailure(Call<VideoResponse> call, Throwable t) {
                listener.onError("Error de conexión al obtener la lista de videos para la película");
            }
        });
    }


    public interface ObtenerVideo {
        void onSuccess(List<Video> videos);
        void onError(String errorMessage);
    }


}