package com.example.proyectopeli.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectopeli.Entidad.Movie;
import com.example.proyectopeli.Repositorio.MovieRepository;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<List<Movie>> carteleraMovies;
    private MutableLiveData<List<Movie>> popularMovies;
    private MutableLiveData<List<Movie>> tendenciaMovies;
    private MutableLiveData<List<Movie>> estrenosMovies;
    private MutableLiveData<List<Movie>> recienteMovies;
    private MovieRepository movieRepository;
    String ApiKey = "8300bb0075ff37f5c25ab05fdeb18503";

    public HomeViewModel() {
        carteleraMovies = new MutableLiveData<>();
        popularMovies = new MutableLiveData<>();
        tendenciaMovies = new MutableLiveData<>();
        estrenosMovies = new MutableLiveData<>();
        recienteMovies = new MutableLiveData<>();
        movieRepository = new MovieRepository();

        fetchPopularMovies(ApiKey);
        fetchCarteleraMovies(ApiKey);
        fetchTendenciaMovies(ApiKey);
        fetchEstrenosMovies(ApiKey);
        fetchRecienteMovies(ApiKey);

    }


    public LiveData<List<Movie>> getCarteleraMovies() {
        return carteleraMovies;
    }

    public LiveData<List<Movie>> getPopularMovies() {
        return popularMovies;
    }

    public LiveData<List<Movie>> getTendenciaMovies() {return tendenciaMovies;}
    public LiveData<List<Movie>> getEstrenosMovies() {return estrenosMovies;}
    public LiveData<List<Movie>> getRecienteMovies() {return recienteMovies;}


    private void fetchCarteleraMovies(String apiKey) {
        movieRepository.getCarteleraMovies(apiKey, new MovieRepository.MoviesCallback() {
            @Override
            public void onSuccess(List<Movie> moviess) {
                carteleraMovies.setValue(moviess);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    private void fetchPopularMovies(String apiKey) {


        movieRepository.getPopularMovies(apiKey, new MovieRepository.OnMoviesCallback() {
            @Override
            public void onSuccess(List<Movie> moviesList) {
                popularMovies.setValue(moviesList);
            }

            @Override
            public void onError(String errorMessage) {
                // Maneja el error según tus necesidades (puedes mostrar un mensaje en la interfaz de usuario, etc.)
            }
        });
    }

    private void fetchTendenciaMovies(String apiKey) {


        movieRepository.getTendenciaMovies(apiKey, new MovieRepository.TendenciaCall() {
            @Override
            public void onSuccess(List<Movie> moviesList) {
                tendenciaMovies.setValue(moviesList);
            }

            @Override
            public void onError(String errorMessage) {
                // Maneja el error según tus necesidades (puedes mostrar un mensaje en la interfaz de usuario, etc.)
            }
        });
    }

    private void fetchEstrenosMovies(String apiKey) {


        movieRepository.getEstrenosMovies(apiKey, new MovieRepository.EstrenosCall() {
            @Override
            public void onSuccess(List<Movie> moviesList) {
                estrenosMovies.setValue(moviesList);
            }

            @Override
            public void onError(String errorMessage) {
                // Maneja el error según tus necesidades (puedes mostrar un mensaje en la interfaz de usuario, etc.)
            }
        });
    }

    private void fetchRecienteMovies(String apiKey) {

        movieRepository.getRecienteMovies(apiKey, new MovieRepository.RecienteCall() {
            @Override
            public void onSuccess(List<Movie> moviesList) {
                recienteMovies.setValue(moviesList);
            }

            @Override
            public void onError(String errorMessage) {
                // Maneja el error según tus necesidades (puedes mostrar un mensaje en la interfaz de usuario, etc.)
            }
        });
    }


}