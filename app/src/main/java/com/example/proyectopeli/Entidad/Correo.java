package com.example.proyectopeli.Entidad;

import com.google.gson.annotations.SerializedName;

public class Correo {

    Usuario usar= new Usuario();

    private String correo;

    private String clave;

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String toJson() {
        return "{\"Correo\": \"" + "Hola" + "\", \"Clave\": \"" + "hOLA" + "\"}";
    }

}
