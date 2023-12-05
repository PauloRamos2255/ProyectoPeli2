package com.example.proyectopeli.ui.dashboard;

import android.widget.EditText;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectopeli.Entidad.Movie;
import com.example.proyectopeli.Repositorio.MovieRepository;

import java.util.List;

public class DashboardViewModel extends ViewModel {

    private MutableLiveData<List<Movie>> searchResults;
    private MovieRepository movieRepository;

    public DashboardViewModel() {
        searchResults = new MutableLiveData<>();
        movieRepository = new MovieRepository();
    }

    public LiveData<List<Movie>> getSearchResults() {
        return searchResults;
    }

    public void searchMovies(String apiKey, String query) {
        movieRepository.getSearchMovies(apiKey, query, new MovieRepository.SearchCallback() {
            @Override
            public void onSuccess(List<Movie> movies) {
                searchResults.setValue(movies);
            }

            @Override
            public void onError(String errorMessage) {
                // Manejar el error según tus necesidades
                // Por ejemplo, imprimir el mensaje de error

            }
        });
    }
}