package com.example.proyectopeli.Conecction;
import com.example.proyectopeli.Entidad.MovieReponse;
import com.example.proyectopeli.Entidad.VideoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
public interface MovieAPI {

    @GET("movie/popular")
    Call<MovieReponse> getPopularMovies(@Query("api_key") String apiKey, @Query("language") String language);

    @GET("movie/now_playing")
    Call<MovieReponse> getCarteleraMovies(@Query("api_key") String apiKey, @Query("language") String language);

    @GET("movie/top_rated")
    Call<MovieReponse> getTendenciaMovies(@Query("api_key") String apiKey, @Query("language") String language);
    @GET("movie/upcoming")
    Call<MovieReponse> getEstrenosMovies(@Query("api_key") String apiKey, @Query("language") String language);

    @GET("search/movie")
    Call<MovieReponse> getSearchMovies(@Query("api_key") String apiKey , @Query("language") String language,@Query("query") String query );

    @GET("movie/{movieId}/videos")
    Call<VideoResponse> getMovieVideos(@Path("movieId") String movieId, @Query("api_key") String apiKey , @Query("language") String language);
}
