package com.example.proyectopeli.Entidad;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Video  {
    @SerializedName("key")
    private String key;

    public Video() {
    }

    public Video(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
