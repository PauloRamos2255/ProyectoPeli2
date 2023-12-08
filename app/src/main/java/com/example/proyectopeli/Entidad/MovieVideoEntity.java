package com.example.proyectopeli.Entidad;

import java.util.ArrayList;
import java.util.List;

public class MovieVideoEntity {
    private String movieId;
    private List<Video> videos;

    public MovieVideoEntity(String movieId, List<Video> videos) {
        this.movieId = movieId;
        this.videos = videos;
    }

    public String getMovieId() {
        return movieId;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public List<String> getVideoKeys() {
        List<String> videoKeys = new ArrayList<>();
        for (Video video : videos) {
            videoKeys.add(video.getKey());
        }
        return videoKeys;
    }
}
