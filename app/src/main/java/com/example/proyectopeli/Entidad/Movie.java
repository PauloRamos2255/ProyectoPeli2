package com.example.proyectopeli.Entidad;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Movie {

    private String title;
    private String overview;
    private Date releaseDate;
    @SerializedName("poster_path")
    private String posterFullPath;

    public Movie() {
    }

    public Movie(String title, String overview, Date releaseDate, String posterFullPath) {
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.posterFullPath = posterFullPath;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterFullPath() {
        return posterFullPath;
    }

    public void setPosterFullPath(String posterFullPath) {
        this.posterFullPath = posterFullPath;
    }
}
