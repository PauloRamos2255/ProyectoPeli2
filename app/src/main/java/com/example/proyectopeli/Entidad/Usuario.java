package com.example.proyectopeli.Entidad;

import java.io.Serializable;

public class Usuario implements Serializable {

    private int id;
    private String nombre;
    private String apellido;
    private String correo;
    private String clave;
    private String numero;
    private boolean restablecer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public boolean isRestablecer() {
        return restablecer;
    }

    public void setRestablecer(boolean restablecer) {
        this.restablecer = restablecer;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }
}

