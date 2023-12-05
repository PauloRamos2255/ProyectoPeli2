package com.example.proyectopeli.Entidad;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MovieReponse {

    @SerializedName("results")
    private List<Movie> results;

    public List<Movie> getResults() {
        return results;
    }
}
