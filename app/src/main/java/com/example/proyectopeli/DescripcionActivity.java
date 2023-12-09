package com.example.proyectopeli;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import com.example.proyectopeli.Entidad.Movie;
import com.example.proyectopeli.Entidad.Video;
import com.example.proyectopeli.Repositorio.MovieRepository;
import com.google.gson.Gson;

import java.util.List;

public class DescripcionActivity extends AppCompatActivity implements MovieRepository.ObtenerVideo {

    private WebView webViewVideo;
    private MovieRepository movieRepository;
    private TextView descripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcion);

        webViewVideo = findViewById(R.id.vwvideoView);
        descripcion = findViewById(R.id.txtDescripcion);

        movieRepository = new MovieRepository(); // Asegúrate de tener tu instancia de repositorio




        Intent intent = getIntent();
        String movieJson = intent.getStringExtra("movie");
        Movie movie = new Gson().fromJson(movieJson, Movie.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(movie.getTitle());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        descripcion.setText(movie.getOverview());
        obtenerListaDeVideos(movie.getId(), "8300bb0075ff37f5c25ab05fdeb18503");
    }

    private void obtenerListaDeVideos(String movieId, String apiKey) {
        movieRepository.fetchVideosForMovie(movieId, apiKey, this);
    }

    @Override
    public void onSuccess(List<Video> videos) {
        // Asumiendo que solo estás interesado en el primer video
        if (!videos.isEmpty()) {
            Video firstVideo = videos.get(0);
            mostrarVideoEnWebView(firstVideo.getKey());
        } else {
            // Puede agregar manejo de error aquí si la lista de videos está vacía
            Log.e("TuActividad", "La lista de videos está vacía");
        }
    }

    private void mostrarVideoEnWebView(String videoKey) {
        String tmdbVideoId = videoKey;
        String videoUrl = "https://www.youtube.com/embed/" + tmdbVideoId + "?si=LtwOTFrhAhpF8TAv";
        String html = "<html><body><iframe width=\"100%\" height=\"100%\" src=\"" + videoUrl + "\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe></body></html>";

        // Cargar el video en el WebView
        webViewVideo.loadData(html, "text/html", "utf-8");
        webViewVideo.setWebChromeClient(new WebChromeClient());
        WebSettings videoOptions = webViewVideo.getSettings(); // <-- Cambié el nombre de la variable aquí
        videoOptions.setJavaScriptEnabled(true);
        videoOptions.setDatabaseEnabled(true);
        videoOptions.setCacheMode(WebSettings.LOAD_DEFAULT);
        videoOptions.setMediaPlaybackRequiresUserGesture(false);

    }

    @Override
    public void onError(String errorMessage) {
        // Manejar errores si es necesario
        Toast.makeText(this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // o realiza la acción que desees al regresar
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}