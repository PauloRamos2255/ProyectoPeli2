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
    private MovieRepository movieRepository;

    public HomeViewModel() {
        carteleraMovies = new MutableLiveData<>();
        popularMovies = new MutableLiveData<>();
        movieRepository = new MovieRepository();
        fetchPopularMovies("8300bb0075ff37f5c25ab05fdeb18503"); // Reemplaza "TU_CLAVE_API" con tu clave de API real
        fetchCarteleraMovies("8300bb0075ff37f5c25ab05fdeb18503");

    }


    public LiveData<List<Movie>> getCarteleraMovies() {
        return carteleraMovies;
    }

    public LiveData<List<Movie>> getPopularMovies() {
        return popularMovies;
    }




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

}