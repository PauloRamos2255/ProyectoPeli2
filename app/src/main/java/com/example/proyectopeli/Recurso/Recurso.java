package com.example.proyectopeli.Recurso;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.proyectopeli.Entidad.Correo;


import org.json.JSONObject;


import java.nio.charset.StandardCharsets;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.util.UUID;






public class Recurso {


    public static String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            // Convierte el hash a una representación hexadecimal
            StringBuilder builder = new StringBuilder();
            for (byte b : hashBytes) {
                builder.append(String.format("%02x", b));
            }

            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String generarClave() {
        String clave = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6);
        return clave;
    }

    public static boolean enviarCorreo(Correo correo) {
        try {
            String apiUrl = "http://servicecorreo.somee.com/enviarCorreo";
            OkHttpClient client = new OkHttpClient();

            // Convertir el objeto Correo a formato JSON
            String postData = correo.toJson();

            // Configurar la solicitud HTTP
            RequestBody requestBody = RequestBody.create(MediaType.get("application/json; charset=utf-8"), postData);
            Request request = new Request.Builder()
                    .url(apiUrl)
                    .post(requestBody)
                    .build();

            // Enviar la solicitud y obtener la respuesta
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    // Manejar errores de respuesta aquí
                    return false;
                }

                // Leer la respuesta JSON del cuerpo de la respuesta
                String responseBody = response.body().string();

                // Procesar la respuesta JSON
                // En este ejemplo, se espera que la respuesta tenga una propiedad "success"
                return new JSONObject(responseBody).optBoolean("success", true);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean Conexion(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }





}
