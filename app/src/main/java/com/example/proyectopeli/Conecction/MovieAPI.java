package com.example.proyectopeli.Conecction;
import com.example.proyectopeli.Entidad.MovieReponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
public interface MovieAPI {

    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(@Query("api_key") String apiKey, @Query("language") String language);

    @GET("movie/now_playing")
    Call<MovieResponse> getCarteleraMovies(@Query("api_key") String apiKey, @Query("language") String language);

    @GET("movie/top_rated")
    Call<MovieResponse> getTendenciaMovies(@Query("api_key") String apiKey, @Query("language") String language);

    @GET("search/movie")
    Call<MovieResponse> getSearchMovies(@Query("api_key") String apiKey , @Query("language") String language,@Query("query") String query );
}
