package com.example.proyectopeli.BLL;

import android.net.Uri;

import com.example.proyectopeli.Conecction.ConectionBD;
import com.example.proyectopeli.Recurso.Recurso;
import com.example.proyectopeli.Entidad.Usuario;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UsuarioBLL {


    Boolean exitoso;
    public Boolean Registar(Usuario user , StringBuilder urlBuilder ) {
        try {
            Connection conexion = ConectionBD.conectar();
            if (conexion != null) {
                String consulta = "INSERT INTO Usuarios (Nombres, Apellidos, Numero, Correo, Contraseña)VALUES(?,?,?,?,?) ";

                try (PreparedStatement pstmt = conexion.prepareStatement(consulta)) {
                    pstmt.setString(1,user.getNombre());
                    pstmt.setString(2, user.getApellido() );
                    pstmt.setString(3,user.getNumero());
                    pstmt.setString(4,user.getCorreo());
                    pstmt.setString(5, user.getClave());

                    int filasAfectadas = pstmt.executeUpdate();

                    if(filasAfectadas != 0){
                        if(urlBuilder != null){


                            String urlString = urlBuilder.toString();
                            URL url = new URL(urlString);
                            String htmlCorreo= "";

                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                                String line;
                                StringBuilder stringBuilder = new StringBuilder();

                                while ((line = reader.readLine()) != null) {
                                    stringBuilder.append(line);
                                }

                                htmlCorreo = stringBuilder.toString();

                                reader.close();
                                connection.disconnect();
                            }

                            if(htmlCorreo != null){
                                 exitoso = Recurso.enviarCorreo(user.getCorreo() , "Creacion de Cuenta" , htmlCorreo);
                            }
                        }
                    }

                    return exitoso;
                }catch (SQLException e) {
                    e.printStackTrace();
                    return false;
                }
            } else {
                return false;
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

}
